package com.potato.evolutiongame;

import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.Deck;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CardViewActivity extends Activity {
	public static final int REQUEST_CARDVIEW = 0xbee0;
	public static final int RESULT_CHOOSE = 0xbee1;
	public static final int RESULT_DISCARD = 0xbee2;
	public static final int RESULT_CANCEL = 0xbee3;
	
	private Button chooseButton;
	private Button discardButton;
	private Button cancelButton;
	private ImageView cardDisplay;
	private TextView cardNameText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_view);
		
		cardNameText = (TextView)findViewById(R.id.cardNameText);
		
		cardDisplay = (ImageView)findViewById(R.id.cardDisplay);
		cardDisplay.setOnClickListener(new ButtonListener());
		
		chooseButton = (Button)findViewById(R.id.chooseButton);
		chooseButton.setOnClickListener(new ButtonListener());
		
		discardButton = (Button)findViewById(R.id.discardButton);
		discardButton.setOnClickListener(new ButtonListener());
		
		cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new ButtonListener());
		
		Intent i = getIntent();
		int idx = i.getIntExtra("cardIdx", 0);
		Card c = Deck.getCardInstance(idx);
		cardNameText.setText(c.getName());
		cardDisplay.setImageBitmap(c.getImage());
	}

	class ButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			int result = RESULT_CANCEL;
			switch (arg0.getId())
			{
				case R.id.chooseButton:
					result = RESULT_CHOOSE;
					break;
				case R.id.discardButton:
					result = RESULT_DISCARD;
					break;
				default:
				case R.id.cancelButton: 
					result = RESULT_CANCEL;
					break;
			}
			setResult(result);
			finish();
		}
	}
}
