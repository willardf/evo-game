package com.potato.evolutiongame;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NewGameActivity extends Activity {
	private static final int CONTACT_PICKER_RESULT = 1001;
	
	Button startGameButton;
	Button contactsButton;
	TextView emailText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		emailText = (TextView)findViewById(R.id.emailText);
		startGameButton = (Button)findViewById(R.id.startGameButton);
		startGameButton.setOnClickListener(new StartGameListener());
		contactsButton = (Button)findViewById(R.id.contactsButton);
		contactsButton.setOnClickListener(new ContactsListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_new_game, menu);
		return true;
	}
	private class StartGameListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			
		}
	}
	private class ContactsListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
		            Contacts.CONTENT_URI);  
		    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT); 
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    if (resultCode == RESULT_OK) {  
	        switch (requestCode) {  
	        case CONTACT_PICKER_RESULT:  
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
	        	    emailText.setText(email);
	        	}
	            break;
	        }
	    } else {  
	        // gracefully handle failure  
	        //Log.w(DEBUG_TAG, "Warning: activity result not ok");  
	    }  
	}  
}
