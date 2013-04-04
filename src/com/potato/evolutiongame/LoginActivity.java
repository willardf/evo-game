package com.potato.evolutiongame;

import com.potato.evolutiongame.game.cards.Deck;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {
	private Button loginButton;
	private EditText usernameText;
	private EditText passwordText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		usernameText = (EditText)findViewById(R.id.usernameText);
		passwordText = (EditText)findViewById(R.id.passwordText);
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new LoginButtonListener());
		try
		{
			Communicator.Initialize();
			Deck.Initialize(getApplicationContext());
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	class LoginButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			String message = "An error occurred. Try again?";;
			Context ctx = getApplicationContext();
			String username = usernameText.getText().toString();
			String password = passwordText.getText().toString();
			long l = -2;
			try {
				l = Communicator.authenticate(username, password);
			}
			catch(Exception e) {
				Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			finally
			{
				if (l > -1) 	//Move to the next screen
				{
					message = username + " logged in!";
					Cookies.set("username", username);
					Cookies.set("password", password);
					Cookies.set("pid", String.valueOf(l));
					
					startActivity(new Intent(getApplicationContext(), GameChooseActivity.class));
				}
				else if (l == -1)
				{
					message = "Invalid login information";
				}
			}
			Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
		}
	}
}
