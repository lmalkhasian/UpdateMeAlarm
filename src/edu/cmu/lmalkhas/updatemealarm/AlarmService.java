package edu.cmu.lmalkhas.updatemealarm;

import android.app.Service;

import android.content.Intent;

import android.media.MediaPlayer;
import android.os.IBinder;

import android.widget.Toast;

public class AlarmService extends Service {

	private MediaPlayer mp;

	@Override
	public void onCreate() {
		// ALARM IS GOING OFF.
		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
				.show();
		mp = MediaPlayer.create(this, R.raw.alarm);
		// mp.start();

		// TODO: delete the alarm from persistent store
		// TODO: delete alarm from Alarm Brain and display

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