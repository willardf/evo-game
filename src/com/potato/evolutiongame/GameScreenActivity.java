package com.potato.evolutiongame;

import java.util.ArrayList;

import com.potato.evolutiongame.game.CommunicationException;
import com.potato.evolutiongame.game.Creature;
import com.potato.evolutiongame.game.GameState;
import com.potato.evolutiongame.game.NotMyTurnException;
import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.EnvironmentCard;
import com.potato.evolutiongame.game.cards.PlayerCard;
import com.potato.evolutiongame.views.CardDisplayView;
import com.potato.evolutiongame.views.GameDisplayView;
import com.potato.evolutiongame.views.GameStateDisplayView;
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
	public static final String GID_KEY = "gid";
	public static final String REQUEST_CREATE_GAME = "0xFEE2";
	public static final String REQUEST_LOAD_GAME = "0xFEE3";
	
	private GameState currentState;
	private GameDisplayView centerDisplay;
	private CardDisplayView bottomDisplay;
	private int selectedCard;
	
	private GameState.BodyPartCallback bpCallback;
	private GameStateDisplayView topDisplay;
	private CardDisplayView environmentCardView;
	private CardDisplayView creatureCardView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		topDisplay = (GameStateDisplayView)findViewById(R.id.topDisplayView);
		centerDisplay = (GameDisplayView)findViewById(R.id.centerDisplayView);
		centerDisplay.setOnSlideOverListener(new SlideOverListen());
		centerDisplay.setOnClickListener(new EnvironmentClickListener());
		
		environmentCardView = (CardDisplayView)findViewById(R.id.environmentCardView);
		environmentCardView.setTitle("Environment Cards");
		
		creatureCardView = (CardDisplayView)findViewById(R.id.creatureCardView);
		creatureCardView.setTitle("Creature Cards");
		
		bottomDisplay = (CardDisplayView)findViewById(R.id.bottomDisplayView);
		bottomDisplay.setOnClickListener(new BottomClickListener());
		
		Intent i = getIntent();
		long gid = i.getLongExtra(GID_KEY, -1);
		boolean startGame = i.getBooleanExtra(REQUEST_CREATE_GAME, false);
		boolean loadGame = i.getBooleanExtra(REQUEST_LOAD_GAME, false);
		if (gid > 0)
		{
			try {
				currentState = Communicator.getGameState(gid);
			} catch (Exception e) {
				finishWithError("Couldn't load game. Error: ", e);
				return;
			}
		}
		else if (startGame)
		{
			currentState = new GameState();
		}
		else if (loadGame)
		{
			// Figure this out
		}
		else
		{
			finishWithError("No game state to load.", null);
			return;
		}
		currentState.setOnBodyPartListener(new BodyPartListen());
		currentState.setOnRefreshListener(new RefreshListen());
		updateDisplay();
	}
	private void finishWithError(String string, Exception e) {
		String output = string + (e != null ? e.getMessage() : "");
		Intent errRet = new Intent();
		errRet.putExtra("error", output);
		setResult(RESULT_ERROR, errRet);
		finish();
	}
	private void updateDisplay()
	{
		topDisplay.setGameState(currentState);
		ArrayList<Card> cardList = new ArrayList<Card>();
		for (PlayerCard p : currentState.getCurrentPlayerHand()) cardList.add(p);
		bottomDisplay.setCardList(cardList);
		
		ArrayList<Card> cardList2 = new ArrayList<Card>();
		for (PlayerCard p : currentState.getCurrentPlayerCreature().getCards()) cardList2.add(p);
		creatureCardView.setCardList(cardList2);
		
		ArrayList<Card> cardList3 = new ArrayList<Card>();
		for (EnvironmentCard p : currentState.getEnvironment().getFeatureCards()) cardList3.add(p);
		environmentCardView.setCardList(cardList3);
	}
	private class BottomClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			selectedCard = bottomDisplay.getCardIndexSelected();
			if (selectedCard < 0) return;
			PlayerCard c = currentState.getCurrentPlayerHand().get(selectedCard);
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
					} catch (Exception e) {
						Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
				} catch (Exception e) {
					Toast.makeText(this, "AI Failure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	private class EnvironmentClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			centerDisplay.setVisibility(View.GONE);
			environmentCardView.setVisibility(View.VISIBLE);
			creatureCardView.setVisibility(View.VISIBLE);
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
			Creature c = currentState.getCurrentPlayerCreature();
			
			Intent i = new Intent(getApplicationContext(), CreatureDetailActivity.class);
			i.putExtra(CreatureDetailActivity.FORRESULT_KEY, true);
			i.putExtra(CreatureDetailActivity.CREATURE_KEY, c);
			
			startActivityForResult(i, CreatureDetailActivity.REQUEST_CREATURECARD);
		}
	}
}
