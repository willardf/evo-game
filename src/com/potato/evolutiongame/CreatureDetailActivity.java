package com.potato.evolutiongame;

import java.util.ArrayList;

import com.potato.evolutiongame.game.Creature;
import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.EnvironmentCard;
import com.potato.evolutiongame.views.CardDisplayView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class CreatureDetailActivity extends Activity {
	public static final int REQUEST_CREATURECARD = 3380;
	public static final String RESULT_CREATURECARD_IDX = "cardIdx";
	public static final String FORRESULT_KEY = "forresult";
	public static final String CREATURE_KEY = "creature";
	
	private Button selectButton;
	private CardDisplayView cardView;
	private CreatureDisplayView creatureView;
	private Button cancelButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creature_detail);
		
		creatureView = (CreatureDisplayView)findViewById(R.id.topDisplayView);
		
		cardView = (CardDisplayView)findViewById(R.id.bottomDisplayView);
		cardView.setOnClickListener(new BottomClickListener());
		
		cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new CancelClickListener());
		
		selectButton = (Button)findViewById(R.id.selectButton);
		selectButton.setOnClickListener(new SelectClickListener());
		
		Intent i = getIntent();
		boolean forresult = i.getBooleanExtra(FORRESULT_KEY, false);
		Creature c = (Creature)i.getSerializableExtra(CREATURE_KEY);
		
		ArrayList<Card> cardList = new ArrayList<Card>();
		for (Card p : c.getCards()) cardList.add(p);
		cardView.setCardList(cardList);
		
		if (!forresult) selectButton.setVisibility(View.GONE);
		selectButton.setEnabled(false);
	}

	private class BottomClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			selectButton.setEnabled(true);
		}
	}
	private class SelectClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			int selectedCard = cardView.getCardIndexSelected();
			Intent i = new Intent();
			i.putExtra(RESULT_CREATURECARD_IDX, selectedCard);
			setResult(RESULT_OK, i);
			finish();
		}
	}
	private class CancelClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}
