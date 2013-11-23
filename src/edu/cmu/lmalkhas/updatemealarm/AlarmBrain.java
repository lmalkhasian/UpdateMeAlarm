package edu.cmu.lmalkhas.updatemealarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmBrain {

	// TODO HAVE A SORTED LIST.
	List<String> alarms = new ArrayList<String>();
	String latestAlarm = null;
	
	public static final String PREFS_NAME = "AlarmTimes";
	private static final String ALARM_SET_KEY = "alarmStringsKey";
	
	private SharedPreferences mPrefs;

	// TODO PERSISTENT STORE OF ALARMS.

	// the default constructor.
	public AlarmBrain(Context context) {
		// get the alarms from memory?
		  	mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		  	mPrefs.getStringSet(ALARM_SET_KEY, null);

	}

	// returns the alarm list currently valid
	public List<String> getAlarmList() {
		return alarms;
	}

	public String getLastAddedAlarm() {
		return latestAlarm;
	}

	public void clearLatest() {
		latestAlarm = null;
	}

	// returns true if successful, false otherwise
	public boolean addAlarm(String time) {
		
		if(find(time)) return false;
		
		//add to alarm list
		alarms.add(time);
		
		//add to persistent store
		Set<String> alarmStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);
		if(alarmStrings != null) {
			
		}
	    SharedPreferences.Editor editor = mPrefs.edit();
	    editor.putString("alarmTimes", time);
	    editor.commit();		
		
		//make this the latest added alarm
		latestAlarm = time;
		
		
		printAlarms();
		return true;
	}

	boolean find(String targetTime) {
		return alarms.contains(targetTime);
	}
	
	void printAlarms() {
		for (String t : alarms) {
			Log.d("debug", t);
		}
	}

}
