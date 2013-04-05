package com.potato.evolutiongame;

import com.potato.evolutiongame.game.CommunicationException;
import com.potato.evolutiongame.game.GameState;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewGameActivity extends Activity {
	
	private static final int REQUEST_CONTACT_PICKER = 1110;
	public static final int REQUEST_NEWGAME = 1111;
	public static final int RESULT_DROP_TO_LOGIN = 1112;
	public static final int RESULT_START = 0xFCB1;
	
	Button startGameButton;
	Button contactsButton;
	TextView usernameText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		usernameText = (TextView)findViewById(R.id.usernameText);
		startGameButton = (Button)findViewById(R.id.startGameButton);
		startGameButton.setOnClickListener(new StartGameListener());
		contactsButton = (Button)findViewById(R.id.contactsButton);
		contactsButton.setOnClickListener(new ContactsListener());
	}

	private class StartGameListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			String message = "Server error.";
			int ret = 0;
			
			GameState s = null;
			try {
				String oUsername = usernameText.getText().toString();
				s = Communicator.startGame( oUsername, 10 );
				ret = 1;
			} catch (CommunicationException e) {
				ret = Integer.parseInt(e.getMessage());
			} catch (Exception e) {
				message = e.getMessage();
			}
			
			switch (ret)
			{
				case 1:
					message = "Game Started!";
					
					Intent i = new Intent();
					i.putExtra("gid", s.getGid());
					setResult(RESULT_START, i);
					finish();
					break;
				case -1:
					message = "Your login information has changed.";
					setResult(RESULT_DROP_TO_LOGIN);
					finish();
					break;
				case -2:
					message = "That user doesn't exist.";
					break;
				case -3:
					message = "You already have a game with that user.";
					break;
				case -4:
					message = "Invalid game parameters.";
					break;
			}
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		}
	}
	private class ContactsListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);  
		    startActivityForResult(contactPickerIntent, REQUEST_CONTACT_PICKER); 
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    if (resultCode == RESULT_OK) {  
	        switch (requestCode) {  
		        case REQUEST_CONTACT_PICKER:  
		            // handle contact results
		        	Uri result = data.getData();
		        	String id = result.getLastPathSegment();  
		        	Cursor cursor = getContentResolver().query(  
		        	        Email.CONTENT_URI, null,
		        	        Email.CONTACT_ID + "=?",
		        	        new String[]{id}, null);
		        	if (cursor.moveToFirst()) {  
		        	    int emailIdx = cursor.getColumnIndex(Email.DATA);  
		        	    String email = cursor.getString(emailIdx);
		        	    usernameText.setText(email);
		        	}
		            break;
	        }
	    } else {
	    	// Cancel or fail, nothing happens
	    }  
	}  
}
