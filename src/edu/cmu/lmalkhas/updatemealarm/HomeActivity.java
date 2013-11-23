package edu.cmu.lmalkhas.updatemealarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.View;
import android.view.View.OnClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	static public AlarmBrain alarmBrain;
	List<Map<String, String>> alarmList = new ArrayList<Map<String, String>>();
	private SimpleAdapter simpleAdpt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		//instantiate alarmBrain
		alarmBrain = new AlarmBrain(getApplicationContext());
		
		// add title for alarms
		TextView tv = (TextView) findViewById(R.id.alarmsTitle);
		tv.setText("Alarms set:");

		// add text to add new alarms button
		Button b = (Button) findViewById(R.id.addButton);
		b.setText("Add new alarm");
		b.setOnClickListener(addAlarmButtonListener);

		setupList();

	}

	void setupList() {
		// populate the list view of alarms
		ListView lv = (ListView) findViewById(R.id.alarmList);
		populateList();
		simpleAdpt = new SimpleAdapter(this, alarmList,
				android.R.layout.simple_list_item_1, new String[] { "alarm" },
				new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		// add listener to list items
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {
				// We know the View is a TextView so we can cast it
				TextView clickedView = (TextView) view;
				Toast.makeText(
						HomeActivity.this,
						"Item with id [" + id + "] - Position [" + position
								+ "] - Planet [" + clickedView.getText() + "]",
						Toast.LENGTH_SHORT).show();
			}
		});

		// add swipe listener to list items
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parentAdapter,
					View view, int position, long id) {
				Toast.makeText(HomeActivity.this, "list item long clicked",
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}

	void populateList() {
		List<String> alarmBrainList = alarmBrain.getAlarmList();
		for (String time : alarmBrainList) {
			alarmList.add(createAlarm("alarm", time));
		}
	}
	
	void updateAlarmList() {
		String latestAlarm = alarmBrain.getLastAddedAlarm();
		if(latestAlarm != null) {
			alarmList.add(createAlarm("alarm", latestAlarm));
			alarmBrain.clearLatest();
		}
		
	}

	private HashMap<String, String> createAlarm(String key, String value) {
		HashMap<String, String> alarm = new HashMap<String, String>();
		alarm.put(key, value);
		return alarm;
	}

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
	protected void onResume() {
		super.onResume();
		Toast.makeText(HomeActivity.this, "RESUMING!",
				Toast.LENGTH_SHORT).show();
		updateAlarmList();
		simpleAdpt.notifyDataSetChanged();
	}

}
