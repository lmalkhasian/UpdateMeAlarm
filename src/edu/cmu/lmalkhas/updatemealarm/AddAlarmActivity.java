package edu.cmu.lmalkhas.updatemealarm;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddAlarmActivity extends Activity {

	private PendingIntent pendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_alarm);

		setButtonListeners();
	}

	private void setButtonListeners() {

		// add text to add new alarms button
		Button addButton = (Button) findViewById(R.id.addButton);
		addButton.setOnClickListener(addButtonListener);

		// add text to add new alarms button
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(cancelButtonListener);
	}

	private OnClickListener cancelButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// cancel this activity... user does not want to set up an alarm
			finish();
		}
	};

	OnClickListener addButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			// get time from the time picker
			TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);
			timePicker.clearFocus();
			int alarm_hour = timePicker.getCurrentHour();
			int alarm_min = timePicker.getCurrentMinute();
			String hourString = Integer.toString(alarm_hour);
			String minuteString = Integer.toString(alarm_min);
			if (minuteString.length() == 1)
				minuteString = "0" + minuteString;

			// if an alarm has already been set for that time, don't recreate it
			// return
			if (HomeActivity.alarmBrain.find(hourString + ":" + minuteString))
				return;

			// add alarm to alarm brain
			HomeActivity.alarmBrain.addAlarm(hourString + ":" + minuteString);

			// add alarm to system
			Intent myIntent = new Intent(AddAlarmActivity.this,
					AlarmService.class);
			pendingIntent = PendingIntent.getService(AddAlarmActivity.this, 0,
					myIntent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

			// get current time
			Calendar currTime = Calendar.getInstance();
			currTime.setTime(new Date());
			int curr_hour = currTime.get(Calendar.HOUR);
			int curr_min = currTime.get(Calendar.MINUTE);

			// CREATE/SET ALARM TIME
			Calendar alarmTime = Calendar.getInstance();

			// if alarm time has passed today set for tomorrow
			if (alarm_hour < curr_hour
					|| (alarm_hour == curr_hour && alarm_min <= curr_min)) {
				// change time for alarm to go off TOMORROW
				System.out
						.println("setting alarm to tomorrow because time has passed today");
				alarmTime.add(Calendar.DAY_OF_MONTH, 1);
			}
			// set hour, min, second
			alarmTime.set(Calendar.HOUR_OF_DAY, alarm_hour);
			alarmTime.set(Calendar.MINUTE, alarm_min);
			alarmTime.set(Calendar.SECOND, 0);

			// set alarm
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					alarmTime.getTimeInMillis(), pendingIntent);
			Toast.makeText(AddAlarmActivity.this,
					"Alarm set check log for time", Toast.LENGTH_LONG).show();
			System.out.println("SET ALARM FOR ALARM TIME = "
					+ alarmTime.get(Calendar.HOUR_OF_DAY) + " "
					+ alarmTime.get(Calendar.MINUTE));

			finish();
		}
	};
}
