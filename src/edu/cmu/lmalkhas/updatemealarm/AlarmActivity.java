package edu.cmu.lmalkhas.updatemealarm;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

public class AlarmActivity extends Activity implements
		TextToSpeech.OnInitListener {

	private MediaPlayer mp;
	TextToSpeech TTS;
	private String fbSummary = "";
	private String newsSummary = "";
	private String weatherSummary = "";
	private String name = "";

	private boolean ttsSuccess = false;
	private boolean fbNotifSuccess = false;
	private boolean newsSuccess = false;
	private boolean weatherSuccess = false;

	private boolean alreadyRead = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		mp = MediaPlayer.create(this, R.raw.lionking);
		TTS = new TextToSpeech(this, this);
		
		if(HomeActivity.alarmBrain == null) {
			HomeActivity.alarmBrain = new AlarmBrain();
		}
		HomeActivity.alarmBrain.removeTime();

		mp.start();

		//if facebook is set to on, get facebook notifications
		if (PersistenceManager.getSetting(PersistenceManager.FB_ALARM)) {
			new Thread(new Runnable() {
				public void run() {
					checkSessionAndGetNotifications();
				}
			}).start();
		}
		else {
			fbNotifSuccess = true;
		}

		//if news is on, get news to read aloud
		if (PersistenceManager.getSetting(PersistenceManager.NEWS_ALARM)) {
			new Thread(new Runnable() {
				public void run() {
					NewsManager nm = new NewsManager();
					newsSummary = nm.getNewsSummary();
					newsSuccess = true;
					readOutNotifications();
				}
			}).start();
		}
		else {
			newsSuccess = true;
		}

		//if weather is on, get weather to read out loud
		if (PersistenceManager.getSetting(PersistenceManager.WEATHER_ALARM)) {
			new Thread(new Runnable() {
				public void run() {
					WeatherManager wm = new WeatherManager();
					weatherSummary = wm.getWeatherSummary();
					weatherSuccess = true;
					readOutNotifications();
				}
			}).start();
		}
		else {
			weatherSuccess = true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		TTS.shutdown();
	}

	/**
	 * 
	 */
	private void checkSessionAndGetNotifications() {
		// make sure we have an active session
		Session session = Session.getActiveSession();
		if (session == null) {
			System.out.println("Session is null, will start a new one");
			startNewSession();
		} else {
			System.out.println("Salid session available");
			getFacebookNotifications(session);
		}
	}

	/**
	 * 
	 */
	private void startNewSession() {

		System.out.println("Starting new session");

		Session.StatusCallback callback = new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				// get facebook notifications once the session becomes active.
				getFacebookNotifications(session);
			}
		};

		Session session = new Session(App.context);
		Session.setActiveSession(session);
		Session.openActiveSession(this, false, callback);
	}

	/**
	 * 
	 * @param fbSession
	 *            the valid Facebook session used to get notifications
	 */
	void getFacebookNotifications(Session fbSession) {

		if (fbSession == null) {
			System.out.println("FBSESSION = NULL in do stuff!!!");
		}
		if (fbSession.isClosed()) {
			System.out.println("FBSESSION IS CLOSED in do stuff");
		}

		// assert the session is valid just in case something went wrong
		assert fbSession != null;
		assert fbSession.isOpened();

		Request notificationsRequest = Request.newGraphPathRequest(fbSession,
				"me/notifications", new Request.Callback() {

					@Override
					// this function is called once the request for the
					// notifications has been completed.
					public void onCompleted(Response response) {

						System.out.println("Notification response completed");

						GraphObject object = response.getGraphObject();
						if (object == null) {
							System.out.println("OBJECT = NULL!!!");
							return;
						}

						JSONObject jsonObj = object.getInnerJSONObject();

						int num_unread = 0;
						String unread_notifs = "";

						try {
							JSONArray jsonArray = jsonObj.getJSONArray("data");

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject obj = jsonArray.getJSONObject(i);

								// get user's name from the first fb
								// notification.
								if (i == 0) {
									name = obj.getJSONObject("to").getString(
											"name");
								}

								// only register unread notifications.
								if (obj.getInt("unread") == 1) {
									num_unread++;
									unread_notifs += obj.getString("title")
											+ " ";
								}
							}

						} catch (JSONException e1) {
							e1.printStackTrace();
						}

						// setup message to be read aloud
						fbSummary = "You have " + num_unread
								+ " unread notifications.";
						if (num_unread > 0) {
							fbSummary += "Here are your unread notifications. "
									+ unread_notifs;
						}
						fbNotifSuccess = true;
						readOutNotifications();
					}

				});

		Bundle params = new Bundle();
		params.putString("include_read", "true");

		notificationsRequest.setParameters(params);
		Request.executeBatchAsync(notificationsRequest);
	}

	@Override
	public void onInit(int initStatus) {

		if (initStatus == TextToSpeech.SUCCESS) {
			System.out.println("000 texttospeech success!");
			TTS.setLanguage(Locale.UK);
			TTS.setSpeechRate((float) 0.8);
			ttsSuccess = true;
			readOutNotifications();
		} else {
			Intent installTTSIntent = new Intent();
			installTTSIntent
					.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
			startActivity(installTTSIntent);
		}
	}

	private void readOutNotifications() {
		// if all the data has been gathered, and the alarm
		// has not already gone off, ring the alarm.
		if (!alreadyRead && ttsSuccess && fbNotifSuccess && newsSuccess
				&& weatherSuccess) {
			alreadyRead = true;
			mp.stop();
			ringTheAlarm();
		}
	}

	private void ringTheAlarm() {

		// create a salutation (good morning, evening, night etc.) based on time
		String salutation = "Good ";

		Calendar currTime = Calendar.getInstance();
		currTime.setTime(new Date());
		int curr_hour = currTime.get(Calendar.HOUR);
		if (curr_hour < 12)
			salutation += "morning";
		else if (curr_hour < 18)
			salutation += "afternoon";
		else if (curr_hour < 21)
			salutation += "evening";
		else
			salutation += "night";

		// add user's name & cute message
		salutation += name;
		salutation += "! Rise and Shine!";

		// speak the summary!
		TTS.speak(salutation + weatherSummary + newsSummary + " " + fbSummary,
				TextToSpeech.QUEUE_FLUSH, null);

		// create the dialog box that will stop the alarm.
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				AlarmActivity.this);

		// set title
		alertDialogBuilder.setTitle("Alarm!");

		// set dialog message
		alertDialogBuilder
				.setMessage("Turn off alarm")
				.setCancelable(false)
				.setPositiveButton("Yes, Please",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								TTS.stop();
								TTS.shutdown();
								finish();
							}
						});

		// create alert dialog
		alertDialogBuilder.create();

		// show the dialog
		alertDialogBuilder.show();
	}

}
