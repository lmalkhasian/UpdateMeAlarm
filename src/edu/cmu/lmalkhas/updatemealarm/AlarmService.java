package edu.cmu.lmalkhas.updatemealarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * The class that is called when the alarm is triggered by the alarm manager
 * @author lenamalkhasian
 * 
 */
public class AlarmService extends Service {

	/**
	 * On create, this app starts a new activity (Alarm Activity), which handles
	 * the dispatch of the alarm.
	 */
	@Override
	public void onCreate() {
		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
				.show();
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

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG)
				.show();
		
		Intent i = new Intent(App.context, AlarmActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);
	}

}