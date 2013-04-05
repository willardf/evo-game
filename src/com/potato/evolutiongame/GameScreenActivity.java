package com.potato.evolutiongame;

import com.potato.evolutiongame.game.CommunicationException;
import com.potato.evolutiongame.game.Creature;
import com.potato.evolutiongame.game.GameState;
import com.potato.evolutiongame.game.NotMyTurnException;
import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.views.CardDisplayView;
import com.potato.evolutiongame.views.GameDisplayView;
import com.potato.evolutiongame.views.SlideOverListener;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class GameScreenActivity extends Activity {
	public static final int REQUEST_GAMESCREEN = 0xFEE0;
	public static final int RESULT_ERROR = 0xFEE1;
	
	private GameState currentState;
	private GameDisplayView centerDisplay;
	private CardDisplayView bottomDisplay;
	private int selectedCard;
	
	private GameState.BodyPartCallback bpCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		centerDisplay = (GameDisplayView)findViewById(R.id.centerDisplayView);
		centerDisplay.setOnSlideOverListener(new SlideOverListen());
		bottomDisplay = (CardDisplayView)findViewById(R.id.bottomDisplayView);
		bottomDisplay.setOnClickListener(new BottomClickListener());
		
		Intent i = getIntent();
		long gid = i.getLongExtra("gid", -1);
		try {
			currentState = Communicator.getGameState(gid);
		} catch (Exception e) {
			Intent errRet = new Intent();
			errRet.putExtra("error", "Couldn't load game. Error: " + e.getMessage());
			setResult(RESULT_ERROR, errRet);
			finish();
			return;
		}
		currentState.setOnBodyPartListener(new BodyPartListen());
		currentState.setOnRefreshListener(new RefreshListen());
		updateDisplay();
	}
	private void updateDisplay()
	{
		bottomDisplay.setCardList(currentState.getMyPlayerHand());
	}
	private class BottomClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			selectedCard = bottomDisplay.getCardIndexSelected();
			Card c = currentState.getMyPlayerHand().get(selectedCard);
			// Card select logic
			Intent i = new Intent(GameScreenActivity.this, CardViewActivity.class);
			i.putExtra("cardIdx", c.getCardIdx());
			
			startActivityForResult(i, CardViewActivity.REQUEST_CARDVIEW);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (requestCode == CardViewActivity.REQUEST_CARDVIEW)
		{
			switch (resultCode)
			{
				case CardViewActivity.RESULT_CHOOSE:
					try {
						currentState.playCard(selectedCard);
					} catch (IndexOutOfBoundsException e) {
						// Should be possible?
					} catch (CommunicationException e) {
						Toast.makeText(this, "Couldn't update server.", Toast.LENGTH_SHORT).show();
					} catch (NotMyTurnException e) {
						Toast.makeText(this, "It's not your turn.", Toast.LENGTH_SHORT).show();
					}
					break;
				case CardViewActivity.RESULT_DISCARD:
					try {
						currentState.discardFromHand(selectedCard);
					} catch (IndexOutOfBoundsException e) {
						// Should be possible?
					} catch (CommunicationException e) {
						Toast.makeText(this, "Couldn't update server.", Toast.LENGTH_SHORT).show();
					}
					break;
			}
		}
		else if (requestCode == CreatureDetailActivity.REQUEST_CREATURECARD)
		{
			if (resultCode == RESULT_OK)
			{
				int toReplace = data.getIntExtra(CreatureDetailActivity.RESULT_CREATURECARD_IDX, -1);
				try {
					bpCallback.BodyPartCallBack(selectedCard, toReplace);
				} catch (CommunicationException e) {
					Toast.makeText(this, "Couldn't update server: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private class SlideOverListen implements SlideOverListener
	{
		@Override
		public void onSlideOver(int Screen) {
			if (Screen == 0) {
				bottomDisplay.setVisibility(View.VISIBLE);
			}
			else {	// To Opponent view
				bottomDisplay.setVisibility(View.INVISIBLE);
			}
		}	
	}
	private class RefreshListen implements GameState.RefreshListener
	{
		@Override
		public void onRefresh() {
			updateDisplay();	
		}
	}
	private class BodyPartListen implements GameState.BodyPartListener
	{
		@Override
		public void onBodyPartCallback(int toPlay, GameState.BodyPartCallback callback) {
			bpCallback = callback;
			selectedCard = toPlay;
			Creature c = currentState.getMyPlayerCreature();
			
			Intent i = new Intent(getApplicationContext(), CreatureDetailActivity.class);
			i.putExtra(CreatureDetailActivity.FORRESULT_KEY, true);
			i.putExtra(CreatureDetailActivity.CREATURE_KEY, c);
			
			startActivityForResult(i, CreatureDetailActivity.REQUEST_CREATURECARD);
		}
	}
}
