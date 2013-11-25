package edu.cmu.lmalkhas.updatemealarm;

import java.util.Arrays;
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

public class SettingsActivity extends Activity{

	private GraphUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		setupUI();
		// textToSpeech();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
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

		LoginButton loginB = (LoginButton) findViewById(R.id.loginButton);
		loginB.setPublishPermissions(Arrays.asList("manage_notifications"));
		loginB.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {
				SettingsActivity.this.user = user;
				if (user != null) {
					System.out.println("got user!" + user.getId());
					// System.out.println("got notifications!" +
					// user.getNotifications());
					doStuff(Session.getActiveSession());
				} else {
					System.out.println("user == null");
				}
			}
		});

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setText("Done");
		saveButton.setOnClickListener(saveButtonListener);
	}

	void doStuff(Session fbSession) {
		System.out.println("in do stuff!");
		if(fbSession == null) {
			return;
		}
		System.out.println("fb session is NOT null");
		Request notificationsRequest = Request.newGraphPathRequest(fbSession,
				"me/notifications", new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						
						System.out.println("Notification response completed");
						
						GraphObject object = response.getGraphObject();
						String notifications;
						if (object != null) {
							notifications = object.getProperty("data")
									.toString();
						} else {
							notifications = "Notifications returns null";
						}
					}
				});

		Bundle params = new Bundle();
		params.putString("include_read", "true");

		notificationsRequest.setParameters(params);
		Request.executeBatchAsync(notificationsRequest);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			System.out.println("state is open!");
		} else if (state.isClosed()) {
			System.out.println("state is closed!");
		}
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

}
