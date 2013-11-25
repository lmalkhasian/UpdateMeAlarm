package edu.cmu.lmalkhas.updatemealarm;

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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

public class AlarmActivity extends Activity implements
		TextToSpeech.OnInitListener {

	private MediaPlayer mp;
	TextToSpeech TTS;
	private String message = "";
	private boolean alreadyRead = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
		mp = MediaPlayer.create(this, R.raw.alarm);
		TTS = new TextToSpeech(this, this);

		Session session = Session.getActiveSession();
		
		Session.StatusCallback callback = new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				// TODO Auto-generated method stub
				doStuff(session);
			}
	    };
		
		if (session == null) {
			System.out.println("No active session, getting a new one.");
			session = new Session(App.context);
			Session.setActiveSession(session);
			Session.openActiveSession(this, false, callback);
		} else {
			System.out
					.println("There is an active session, proceding to getting notifications.");
			doStuff(session);
		}
	}

	void doStuff(Session fbSession) {
		System.out.println("in do stuff!");

		if (fbSession == null) {
			System.out.println("FBSESSION = NULL!!!");
			return;
		}
		if (fbSession.isClosed()) {
			System.out.println("FBSESSION IS CLOSED");
			return;
		}

		Request notificationsRequest = Request.newGraphPathRequest(fbSession,
				"me/notifications", new Request.Callback() {

					@Override
					public void onCompleted(Response response) {

						System.out.println("Notification response completed");

						GraphObject object = response.getGraphObject();
						if (object == null) {
							System.out.println("OBJECT = NULL!!!");
							return;
						}
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
									if(num_read >= 5) break;
									num_read++;
									read_notifs += obj.getString("title") + " ";
								}
							}

						} catch (JSONException e1) {
							e1.printStackTrace();
						}

						//setup message to be read aloud
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
			TTS.setLanguage(Locale.ENGLISH);
			if(!alreadyRead) {
				readIt(message);
			}
		} else {
			Intent installTTSIntent = new Intent();
			installTTSIntent
					.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
			startActivity(installTTSIntent);
		}
	}
	
	private void readIt(String message) {
		popUpAlert();
	}	
	
	private void popUpAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				AlarmActivity.this);

		// set title
		alertDialogBuilder.setTitle("Alarm!");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Turn off alarm")
				.setCancelable(false)
				.setPositiveButton("Yes, Please",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, rm
								// Facebook notifications
								// TODO: REMOVE
								TTS.stop();
								finish();
							}
						});
		// create alert dialog
		alertDialogBuilder.create();
		TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);

	}

}
