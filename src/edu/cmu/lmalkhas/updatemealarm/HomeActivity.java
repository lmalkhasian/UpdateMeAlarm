package edu.cmu.lmalkhas.updatemealarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The main activity of the application. It displays a clock, a list of alarms,
 * and navigation tools to go to the addalarm activity and settings activity.
 * 
 * @author lenamalkhasian
 * 
 */
public class HomeActivity extends Activity {

	static public AlarmBrain alarmBrain;
	List<Map<String, String>> alarmList = new ArrayList<Map<String, String>>();
	private SimpleAdapter simpleAdpt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// instantiate alarmBrain, persistence manager
		alarmBrain = new AlarmBrain();

		// add title for alarms
		TextView tv = (TextView) findViewById(R.id.alarmsTitle);
		tv.setText("Alarms set:");

		// add text to add new alarms button
		Button b = (Button) findViewById(R.id.addButton);
		b.setText("Add new alarm");
		b.setOnClickListener(addAlarmButtonListener);

		// add intent to settings button
		Button b2 = (Button) findViewById(R.id.settingsButton);
		b2.setText("Settings");
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HomeActivity.this,
						SettingsActivity.class);
				startActivity(intent);
			}

		});
		setupList();
	}

	/**
	 * sets up the alarm list that is displayed on the home page.
	 */
	void setupList() {
		// populate the list view of alarms
		ListView lv = (ListView) findViewById(R.id.alarmList);
		initialPopulateList();
		simpleAdpt = new SimpleAdapter(this, alarmList,
				android.R.layout.simple_list_item_1, new String[] { "alarm" },
				new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		// add swipe listener to list items
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parentAdapter,
					View view, int position, long id) {

				Toast.makeText(HomeActivity.this, "Alarm deleted",
						Toast.LENGTH_SHORT).show();
				TextView clickedView = (TextView) view;
				String time = clickedView.getText().toString();
				String timeKey = time.replace(":", "");

				// alarmBrain.removeAlarmFromManager(time);
				Intent myIntent = new Intent(HomeActivity.this,
						AlarmService.class);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				PendingIntent displayIntent = PendingIntent.getService(
						getApplicationContext(), Integer.valueOf(timeKey),
						myIntent, 0);
				alarmManager.cancel(displayIntent);

				// remove the alarm from list view and from brain and from
				// persistent storage
				alarmBrain.removeTime(time);

				// update homescreen
				initialPopulateList();
				simpleAdpt.notifyDataSetChanged();

				return true;
			}
		});
	}

	/**
	 * populate the list from scratch.
	 */
	void initialPopulateList() {
		alarmList.clear();
		Set<String> alarmBrainList = alarmBrain.getAlarmList();
		for (String time : alarmBrainList) {
			alarmList.add(createAlarm("alarm", time));
		}
	}

	/**
	 * creates a hashmap of the given key value pairing and returns it.
	 * 
	 * @param key
	 *            from the key-value pair
	 * @param value
	 *            from the key-value pair
	 * @return
	 */
	private HashMap<String, String> createAlarm(String key, String value) {
		HashMap<String, String> alarm = new HashMap<String, String>();
		alarm.put(key, value);
		return alarm;
	}

	/**
	 * The listener for the "Add Alarm" button. When clicked, it starts the
	 * AddAlarmActivity.
	 */
	OnClickListener addAlarmButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(HomeActivity.this,
					AddAlarmActivity.class);
			startActivity(intent);

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// respond to menu item selection
		Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
		startActivity(intent);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		//update the alarm list.
		initialPopulateList();
		simpleAdpt.notifyDataSetChanged();
	}

}
