package edu.cmu.lmalkhas.updatemealarm;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import android.app.Service;

import android.content.Intent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

import android.widget.Toast;

public class AlarmService extends Service 
implements TextToSpeech.OnInitListener
		{

	private MediaPlayer mp;
	TextToSpeech TTS;
	private String message = "";

	@Override
	public void onCreate() {
		// ALARM IS GOING OFF.
		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
				.show();
		mp = MediaPlayer.create(this, R.raw.alarm);
		TTS = new TextToSpeech(this, this);

		doStuff(Session.getActiveSession());

		// TODO: delete the alarm from persistent store
		// TODO: delete alarm from Alarm Brain and display

	}

	void doStuff(Session fbSession) {
		System.out.println("in do stuff!");
		if (fbSession == null) {
			System.out.println("fbSession is null");
			//Session.openActiveSession(activity, allowLoginUI, callback)
			return;
		}
		System.out.println("fb session is NOT null");
		Request notificationsRequest = Request.newGraphPathRequest(fbSession,
				"me/notifications", new Request.Callback() {

					@Override
					public void onCompleted(Response response) {

						System.out.println("Notification response completed");

						GraphObject object = response.getGraphObject();
						JSONObject jsonObj = object.getInnerJSONObject();

						int num_unread = 0;
						int num_read = 0;
						String unread_notifs = "";
						String read_notifs = "";

						try {
							JSONArray jsonArray = jsonObj.getJSONArray("data");

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject obj = jsonArray.getJSONObject(i);
								if (obj.getInt("unread") == 1) {
									num_unread++;
									unread_notifs += obj.getString("title")
											+ " ";
								} else {
									num_read++;
									read_notifs += obj.getString("title") + " ";
								}
							}

						} catch (JSONException e1) {
							e1.printStackTrace();
						}

						message = "You have " + num_unread
								+ " new notifications.";
						if (num_unread > 0) {
							message += "Here are your unread notifications. "
									+ unread_notifs;
						}
						if (num_read > 0) {
							message += " Here are your older notifications. "
									+ read_notifs;
						}
						
						System.out.println(message);
						readIt(message);
						//readIt(message);

						/*
						 * try { System.out.println(jsonObj.toString(4)); }
						 * catch (JSONException e) { // TODO Auto-generated
						 * catch block e.printStackTrace(); } String
						 * notifications; if (object != null) { notifications =
						 * object.getProperty("data") .toString(); } else {
						 * notifications = "Notifications returns null"; }
						 * System.out.println(notifications);
						 */
					}
				});

		Bundle params = new Bundle();
		params.putString("include_read", "true");

		notificationsRequest.setParameters(params);
		Request.executeBatchAsync(notificationsRequest);
	}

	private void readIt(String message) {
		TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
	}
	
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
		readIt("HELLO GOVNA");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG)
				.show();
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		// TODO Auto-generated method stub

		super.onStart(intent, startId);

		Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG)
				.show();

	}

	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);
	}

}