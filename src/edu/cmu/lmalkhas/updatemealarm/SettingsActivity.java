package edu.cmu.lmalkhas.updatemealarm;

import java.util.Locale;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity implements
		TextToSpeech.OnInitListener {

	TextToSpeech TTS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		//TTS = new TextToSpeech(this, this);

		System.out.println("before session call");
	    // start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {

	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	                System.out.println("000 User = " + user.getName());
	              }
	              else {
	            	  System.out.println("000 User is null");
	              }
	            }
	          });
	        }
	        else {
	        	System.out.println("000 Session is not open!");
	        }
	      }
	      
	      
	    });
	    
		setupUI();
		// textToSpeech();
		
	}

	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }

	private void preachIt() {
		// TTS.speak("Hello govna, how are you today mate.",
		// TextToSpeech.QUEUE_FLUSH, null);
	}

	private void setupUI() {
		TextView tv1 = (TextView) findViewById(R.id.settingsTitle);
		tv1.setText("Settings\n");

		TextView tv2 = (TextView) findViewById(R.id.alarmSoundTitle);
		tv2.setText("What would you like to wake up to?");

		TextView tv3 = (TextView) findViewById(R.id.facebookLabel);
		tv3.setText("Facebook Notifications");
		CheckBox fbCheckbox = (CheckBox) findViewById(R.id.facebookCheckBox);
		fbCheckbox.setOnCheckedChangeListener(fbCheckChanged);
		// TODO SET TO CORRECT STATUS BASED ON PREFS

		TextView tv4 = (TextView) findViewById(R.id.newsLabel);
		tv4.setText("Top News Articles");
		CheckBox newsCheckbox = (CheckBox) findViewById(R.id.newsCheckBox);
		newsCheckbox.setOnCheckedChangeListener(newsCheckChanged);

		// setup Facebook login button
		/*LoginButton loginButton = (LoginButton) findViewById(R.id.loginButton);
		loginButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						SettingsActivity.this.user = user;
						// updateUI();
						// It's possible that we were waiting for this.user to
						// be populated in order to post a
						// status update.
						// handlePendingAction();
					}
				});*/

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setText("Done");
		saveButton.setOnClickListener(saveButtonListener);
	}

	private OnCheckedChangeListener fbCheckChanged = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				// Toast.makeText(SettingsActivity.this, "box was checked.",
				// Toast.LENGTH_SHORT).show();

				// CONNECT TO FACEBOOK HERE.

			} else {
				// double check that the user actually wants to uncheck the box.
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						SettingsActivity.this);

				// set title
				alertDialogBuilder.setTitle("Remove Facebook Notifications");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to remove Facebook notifications from your alarm?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, rm
										// Facebook notifications
										// TODO: REMOVE
										Toast.makeText(SettingsActivity.this,
												"Remove FB Notifications",
												Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and return the FB
										// checkbox to true
										dialog.cancel();
										setFBChecked(true);
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}

		}

	};

	private void setFBChecked(boolean checked) {
		CheckBox fbcb = (CheckBox) findViewById(R.id.facebookCheckBox);
		fbcb.setChecked(checked);
	}

	private OnCheckedChangeListener newsCheckChanged = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

		}
	};

	private OnClickListener saveButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Toast.makeText(SettingsActivity.this, "Changes Saved!",
					Toast.LENGTH_SHORT).show();
			finish();
		}
	};

	@Override
	public void onInit(int initStatus) {

		if (initStatus == TextToSpeech.SUCCESS) {
			TTS.setLanguage(Locale.ENGLISH);
		} else {
			Intent installTTSIntent = new Intent();
			installTTSIntent
					.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
			startActivity(installTTSIntent);
		}

		preachIt();

	}

}
