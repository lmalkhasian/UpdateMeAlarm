package edu.cmu.lmalkhas.updatemealarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * Settings activity that allows users to set what they'd like to wake up to.
 * 
 * @author lenamalkhasian
 * 
 */
public class SettingsActivity extends Activity {

	List<CheckBox> newsCheckboxes = new ArrayList<CheckBox>();

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

	/**
	 * set up the UI to properly reflect what is and is not currently enabled.
	 */
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

		// add all the news checkboxes
		CheckBox cb1 = (CheckBox) findViewById(R.id.worldCheckBox);
		newsCheckboxes.add(cb1);
		cb1.setChecked(PersistenceManager
				.getSetting(PersistenceManager.NEWS_WORLD));

		CheckBox cb2 = (CheckBox) findViewById(R.id.usCheckBox);
		newsCheckboxes.add(cb2);
		cb2.setChecked(PersistenceManager
				.getSetting(PersistenceManager.NEWS_US));

		CheckBox cb3 = (CheckBox) findViewById(R.id.politicsCheckBox);
		newsCheckboxes.add(cb3);
		cb3.setChecked(PersistenceManager
				.getSetting(PersistenceManager.NEWS_POLITICS));

		CheckBox cb4 = (CheckBox) findViewById(R.id.technologyCheckBox);
		newsCheckboxes.add(cb4);
		cb4.setChecked(PersistenceManager
				.getSetting(PersistenceManager.NEWS_TECHNOLOGY));

		CheckBox cb5 = (CheckBox) findViewById(R.id.sportsCheckBox);
		newsCheckboxes.add(cb5);
		cb5.setChecked(PersistenceManager
				.getSetting(PersistenceManager.NEWS_SPORTS));

		// add listener to all the checkboxes.
		for (CheckBox cb : newsCheckboxes) {
			cb.setOnCheckedChangeListener(checkChanged);
		}

		setNewsCheckboxesEnabled(PersistenceManager
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

	@SuppressWarnings("unused")
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			System.out.println("state is open!");
		} else if (state.isClosed()) {
			System.out.println("state is closed!");
		}
	}

	/**
	 * Function to easily disable or enable all the sub-news checkboxes
	 * 
	 * @param enabled
	 *            whether to enable the checkboxes or not.
	 */
	private void setNewsCheckboxesEnabled(boolean enabled) {
		for (CheckBox cb : newsCheckboxes) {
			cb.setEnabled(enabled);
		}
	}

	/**
	 * When check is changed, update the system preferences.
	 */
	private OnCheckedChangeListener checkChanged = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.newsCheckBox:
				PersistenceManager.changeSetting(PersistenceManager.NEWS_ALARM,
						isChecked);
				setNewsCheckboxesEnabled(isChecked);
				break;
			case R.id.weatherCheckBox:
				PersistenceManager.changeSetting(
						PersistenceManager.WEATHER_ALARM, isChecked);
				break;
			case R.id.worldCheckBox:
				PersistenceManager.changeSetting(PersistenceManager.NEWS_WORLD,
						isChecked);
				break;
			case R.id.usCheckBox:
				PersistenceManager.changeSetting(PersistenceManager.NEWS_US,
						isChecked);
				break;
			case R.id.politicsCheckBox:
				PersistenceManager.changeSetting(
						PersistenceManager.NEWS_POLITICS, isChecked);
				break;
			case R.id.technologyCheckBox:
				PersistenceManager.changeSetting(
						PersistenceManager.NEWS_TECHNOLOGY, isChecked);
				break;
			case R.id.sportsCheckBox:
				PersistenceManager.changeSetting(
						PersistenceManager.NEWS_SPORTS, isChecked);
				break;
			}

		}
	};

	/**
	 * upon save, close the activity.
	 */
	private OnClickListener saveButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Toast.makeText(SettingsActivity.this, "Changes Saved!",
					Toast.LENGTH_SHORT).show();
			finish();
		}
	};

}
