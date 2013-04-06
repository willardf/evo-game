package com.potato.evolutiongame;

import com.potato.evolutiongame.game.cards.Deck;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends Activity {

	private Button newGameButton;
	private Button loginButton;
	private Button oldGameButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		newGameButton = (Button)findViewById(R.id.newGameButton);
		newGameButton.setOnClickListener(new NewClickListener());
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new LoginClickListener());
		oldGameButton = (Button)findViewById(R.id.oldGameButton);
		oldGameButton.setOnClickListener(new OldClickListener());
		try
		{	
			Deck.Initialize(getApplicationContext());
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private class LoginClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			startActivity(new Intent(getApplicationContext(), LoginActivity.class));
		}		
	}
	private class NewClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(), GameScreenActivity.class);
			i.putExtra(GameScreenActivity.REQUEST_CREATE_GAME, true);
			startActivity(i);
		}		
	}
	private class OldClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(), GameScreenActivity.class);
			i.putExtra(GameScreenActivity.REQUEST_LOAD_GAME, true);
			startActivity(i);
		}		
	}
}
