package edu.cmu.lmalkhas.updatemealarm;

import java.util.Arrays;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity {

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

		TextView titleTxtView = (TextView) findViewById(R.id.alarmSoundTitle);
		titleTxtView.setText("Customize Your Alarm");

		
		TextView weatherTxtView = (TextView) findViewById(R.id.weatherLabel);
		weatherTxtView.setText("Weather");
		CheckBox weatherCheckBox = (CheckBox) findViewById(R.id.weatherCheckBox);
		weatherCheckBox.setOnCheckedChangeListener(checkChanged);
		weatherCheckBox.setChecked(PersistenceManager
				.getSetting(PersistenceManager.WEATHER_ALARM));

		TextView newsTxtView = (TextView) findViewById(R.id.newsLabel);
		newsTxtView.setText("Top News Articles");
		CheckBox newsCheckbox = (CheckBox) findViewById(R.id.newsCheckBox);
		newsCheckbox.setOnCheckedChangeListener(checkChanged);
		newsCheckbox.setChecked(PersistenceManager
				.getSetting(PersistenceManager.NEWS_ALARM));

		TextView fbTxtView = (TextView) findViewById(R.id.facebookLabel);
		fbTxtView.setText("Facebook Notifications");
		CheckBox fbCheckBox = (CheckBox) findViewById(R.id.fbCheckBox);
		fbCheckBox.setEnabled(false);
		fbCheckBox.setChecked(PersistenceManager
				.getSetting(PersistenceManager.FB_ALARM));

		// facebook login button
		LoginButton loginB = (LoginButton) findViewById(R.id.loginButton);
		loginB.setPublishPermissions(Arrays.asList("manage_notifications"));
		loginB.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {
				CheckBox fbCheckBox = (CheckBox) findViewById(R.id.fbCheckBox);
				if (user != null) {
					System.out.println("user logged on!");
					PersistenceManager.changeSetting(
							PersistenceManager.FB_ALARM, true);
					fbCheckBox.setChecked(true);
				} else {
					System.out.println("user logged off!");
					PersistenceManager.changeSetting(
							PersistenceManager.FB_ALARM, false);
					fbCheckBox.setChecked(false);
				}
			}
		});

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setText("Done");
		saveButton.setOnClickListener(saveButtonListener);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			System.out.println("state is open!");
		} else if (state.isClosed()) {
			System.out.println("state is closed!");
		}
	}

	private OnCheckedChangeListener checkChanged = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.newsCheckBox:
				PersistenceManager.changeSetting(PersistenceManager.NEWS_ALARM,
						isChecked);
				break;
			case R.id.weatherCheckBox:
				PersistenceManager.changeSetting(PersistenceManager.WEATHER_ALARM,
						isChecked);
				break;
			}

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
