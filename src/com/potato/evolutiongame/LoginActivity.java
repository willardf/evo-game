package com.potato.evolutiongame;

import com.potato.evolutiongame.game.Card;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {
	private Button loginButton;
	private TextView emailText;
	private TextView passwordText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		emailText = (TextView)findViewById(R.id.emailText);
		passwordText = (TextView)findViewById(R.id.passwordText);
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new LoginButtonListener());
		try
		{
			Communicator.Initialize();
			Card.Initialize(getApplicationContext());
		}
		catch(Exception e){}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return false;
	}*/

	class LoginButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			String message = "An error occurred. Try again?";;
			Context ctx = getApplicationContext();
			String email = emailText.getText().toString();
			String password = passwordText.getText().toString();
			long l = -1;
			try{
				l = Communicator.authenticate(email, password);
			}
			catch(Exception e) {}
			finally
			{
				if (l > -1) 	//Move to the next screen
				{
					
				}
				else if (l == -1)
				{
					message = "Invalid login information";
				}
			}
			startActivity(new Intent(getApplicationContext(), GameChooseActivity.class));
			Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
		}
	}
}
