package com.potato.evolutiongame;

import java.util.ArrayList;

import com.potato.evolutiongame.game.Card;
import com.potato.evolutiongame.game.GameState;
import com.potato.evolutiongame.views.CardDisplayView;
import com.potato.evolutiongame.views.GameDisplayView;
import com.potato.evolutiongame.views.SlideOverListener;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;

public class GameScreenActivity extends Activity {
	
	private GameState currentState;
	private GameDisplayView centerDisplay;
	private CardDisplayView bottomDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		centerDisplay = (GameDisplayView)findViewById(R.id.centerDisplayView);
		centerDisplay.setOnSlideOverListener(new SlideOverListen());
		bottomDisplay = (CardDisplayView)findViewById(R.id.bottomDisplayView);
		bottomDisplay.setOnClickListener(new BottomClickListener());
		
		ArrayList<Card> l = new ArrayList<Card>();
		l.add(Card.getCard(0));
		l.add(Card.getCard(0));
		l.add(Card.getCard(0));
		l.add(Card.getCard(0));
		l.add(Card.getCard(0));
		bottomDisplay.setCardList(l);
		
		Intent i = getIntent();
		currentState = (GameState)i.getSerializableExtra("GAMESTATE");
	}
	private class BottomClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			int select = bottomDisplay.getCardSelected();
			select++;
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
}
