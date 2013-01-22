package com.potato.evolutiongame;

import java.io.IOException;
import java.util.ArrayList;

import com.potato.evolutiongame.game.GameEntry;
import com.potato.evolutiongame.game.GameState;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class GameChooseActivity extends Activity {
	private Spinner theirTurnSpinner;
	private Spinner yourTurnSpinner;
	private Button theirTurnButton;
	private Button yourTurnButton;
	private Button newGameButton;
	
	private ArrayList<GameEntry> data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_choose);
		
		newGameButton = (Button)findViewById(R.id.newGameButton);
		newGameButton.setOnClickListener(new NewGameClickListener());
		
		theirTurnSpinner = (Spinner)findViewById(R.id.theirTurnSpinner);
		yourTurnSpinner = (Spinner)findViewById(R.id.yourTurnSpinner);
		theirTurnButton = (Button)findViewById(R.id.theirTurnButton);
		theirTurnButton.setOnClickListener(new TheirClickListener());
		
		yourTurnButton = (Button)findViewById(R.id.yourTurnButton);
		yourTurnButton.setOnClickListener(new YourClickListener());

		try {
			data = Communicator.getGames(1);
			populateTheirTurnSpinner();
			populateYourTurnSpinner();
		} catch (IOException e) {
			Toast.makeText(this, "An error occurred while fetching games.", Toast.LENGTH_SHORT).show();
		}
	}
	private void populateYourTurnSpinner()
	{
		ArrayAdapter<GameEntry> adapter = new ArrayAdapter<GameEntry>
			(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		for (GameEntry e : data)
		{
			if (e.isYourTurn())
			{
				adapter.add(e);
			}
		}
		yourTurnSpinner.setAdapter(adapter);
		if (adapter.isEmpty())
		{
			findViewById(R.id.yourTurnLabel).setVisibility(View.GONE);
			yourTurnSpinner.setVisibility(View.GONE);
			yourTurnButton.setVisibility(View.GONE);
		}
	}
	private void populateTheirTurnSpinner()
	{
		ArrayAdapter<GameEntry> adapter = new ArrayAdapter<GameEntry>
			(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		for (GameEntry e : data)
		{
			if (e.isYourTurn() == false)
			{
				adapter.add(e);
			}
		}
		theirTurnSpinner.setAdapter(adapter);
		if (adapter.isEmpty())
		{
			findViewById(R.id.theirTurnLabel).setVisibility(View.GONE);
			theirTurnSpinner.setVisibility(View.GONE);
			theirTurnButton.setVisibility(View.GONE);
		}
	}
	private void loadGame(GameEntry e)
	{
		GameState s = null;
		try {
			s = Communicator.getGameState(e.getId());
		} catch (Exception e1) {
			Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
			return;
		}
		finally
		{
			s.setYourTurn(e.isYourTurn());
			Intent i = new Intent(this, GameScreenActivity.class);
			i.putExtra("GAMESTATE", s);
			startActivity(i);
		}
	}
	
	private class NewGameClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(), NewGameActivity.class);
			startActivity(i);
		}
	}
	private class TheirClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			GameEntry e = (GameEntry)theirTurnSpinner.getSelectedItem();
			loadGame(e);
		}
	}
	private class YourClickListener implements OnClickListener
	{

		@Override
		public void onClick(View arg0) {
			GameEntry e = (GameEntry)yourTurnSpinner.getSelectedItem();
			loadGame(e);
		}
	}
}
