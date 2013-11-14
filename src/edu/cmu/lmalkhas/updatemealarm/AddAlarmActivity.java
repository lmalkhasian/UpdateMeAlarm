package edu.cmu.lmalkhas.updatemealarm;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class AddAlarmActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_alarm);

		setButtonListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_alarm, menu);
		return true;
	}

	private void setButtonListeners() {

		// add text to add new alarms button
		Button addButton = (Button) findViewById(R.id.addButton);
		addButton.setOnClickListener(addButtonListener);

		// add text to add new alarms button
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(cancelButtonListener);
	}
	
	OnClickListener cancelButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			finish();
		}
	};
	
	OnClickListener addButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);
			timePicker.clearFocus();
			String h = timePicker.getCurrentHour().toString();
			String m = timePicker.getCurrentMinute().toString();
			HomeActivity.alarmBrain.addAlarm(h+":"+m);
			finish();
		}
	};
}
