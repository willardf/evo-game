package com.potato.evolutiongame;

import java.util.ArrayList;

import com.potato.evolutiongame.game.CommunicationException;
import com.potato.evolutiongame.game.GameEntry;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class GameChooseActivity extends Activity {
	public static final int REQUEST_CHOOSE_GAME = 0x1111;
	private Spinner theirTurnSpinner;
	private Spinner yourTurnSpinner;
	private Button theirTurnButton;
	private Button yourTurnButton;
	private Button newGameButton;
	
	private ArrayList<GameEntry> data;
	private Button refreshButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_choose);
		
		newGameButton = (Button)findViewById(R.id.newGameButton);
		newGameButton.setOnClickListener(new NewGameClickListener());
		
		refreshButton = (Button)findViewById(R.id.refreshButton);
		refreshButton.setOnClickListener(new RefreshClickListener());
		
		theirTurnSpinner = (Spinner)findViewById(R.id.theirTurnSpinner);
		yourTurnSpinner = (Spinner)findViewById(R.id.yourTurnSpinner);
		theirTurnButton = (Button)findViewById(R.id.theirTurnButton);
		theirTurnButton.setOnClickListener(new TheirClickListener());
		
		yourTurnButton = (Button)findViewById(R.id.yourTurnButton);
		yourTurnButton.setOnClickListener(new YourClickListener());

		refreshLists();
	}
	private void refreshLists() {
		int pid = Integer.parseInt(Cookies.get("pid"));
		try {
			data = Communicator.getGames(pid);
			populateTheirTurnSpinner();
			populateYourTurnSpinner();
		} catch (CommunicationException e) {
			Toast.makeText(this, "Couldn't fetch games list.", Toast.LENGTH_SHORT).show();
		}
	}
	private void populateYourTurnSpinner()
	{
		ArrayAdapter<GameEntry> adapter = new ArrayAdapter<GameEntry>
			(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		for (GameEntry e : data)
		{
			if (e.isYourTurn()) adapter.add(e);
		}
		yourTurnSpinner.setAdapter(adapter);
		if (adapter.isEmpty())
		{
			findViewById(R.id.yourTurnLabel).setVisibility(View.GONE);
			yourTurnSpinner.setVisibility(View.GONE);
			yourTurnButton.setVisibility(View.GONE);
		}
		else
		{
			findViewById(R.id.yourTurnLabel).setVisibility(View.VISIBLE);
			yourTurnSpinner.setVisibility(View.VISIBLE);
			yourTurnButton.setVisibility(View.VISIBLE);
		}
	}
	private void populateTheirTurnSpinner()
	{
		ArrayAdapter<GameEntry> adapter = new ArrayAdapter<GameEntry>
			(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		for (GameEntry e : data)
		{
			if (e.isYourTurn() == false) adapter.add(e);
		}
		theirTurnSpinner.setAdapter(adapter);
		if (adapter.isEmpty())
		{
			findViewById(R.id.theirTurnLabel).setVisibility(View.GONE);
			theirTurnSpinner.setVisibility(View.GONE);
			theirTurnButton.setVisibility(View.GONE);
		}
		else
		{
			findViewById(R.id.theirTurnLabel).setVisibility(View.VISIBLE);
			theirTurnSpinner.setVisibility(View.VISIBLE);
			theirTurnButton.setVisibility(View.VISIBLE);
		}
	}
	private void loadGame(GameEntry e)
	{
		try {
			Intent i = new Intent(this, GameScreenActivity.class);
			i.putExtra("gid", e.getId());
			startActivityForResult(i, GameScreenActivity.REQUEST_GAMESCREEN);
		}
		catch (Exception e2){
			Toast.makeText(getApplicationContext(), "Couldn't start game: " + e2.getMessage(), Toast.LENGTH_SHORT).show();				
		}
	}
	
	private class NewGameClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(), NewGameActivity.class);
			startActivityForResult(i, NewGameActivity.REQUEST_NEWGAME);
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
	private class RefreshClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			refreshLists();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    if (resultCode == NewGameActivity.RESULT_DROP_TO_LOGIN) this.finishActivity(RESULT_OK);
	    if (resultCode == NewGameActivity.RESULT_START) {
	    	GameEntry e = new GameEntry(data.getLongExtra("gid", -1), " ", 0, true);
	    	this.loadGame(e);
	    }
	    if (resultCode == GameScreenActivity.RESULT_ERROR)
	    {
	    	String msg = data.getStringExtra("error");
	    	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	    }
	}
}
