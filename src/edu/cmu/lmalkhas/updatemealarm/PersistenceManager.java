package edu.cmu.lmalkhas.updatemealarm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PersistenceManager extends Activity {
	
	public static final String PREFS_NAME = "AlarmTimes";
	private static final String ALARM_SET_KEY = "alarmStringsKey";
	
	private static SharedPreferences mPrefs;

	PersistenceManager() {
		mPrefs = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	public static Set<String> getAllTimes() { 	//THIS IS RUINING EVERYTHINGGGG!!!
		
		System.out.println("mprefs = " + ((mPrefs!=null)?"true":"false"));
		
		if(mPrefs == null) return null;
		
		Set<String> alarmStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);
		HashSet<String> retStrings = new HashSet<String>(alarmStrings);
		System.out.println("RETRIEVED STRINGS");
		return retStrings;
		
	}
	
	public static void addTime(String time) {
		Set<String> alarmStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);
		if(alarmStrings == null) {
			alarmStrings = new HashSet<String>();
		}
		alarmStrings.add(time);
	    SharedPreferences.Editor editor = mPrefs.edit();
	    editor.putStringSet(ALARM_SET_KEY, alarmStrings);
	    editor.commit();	
	    System.out.println("Successfully commited key");
	}
	
	public static void removeTime(String time) {
		Set<String> alarmStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);
		if(alarmStrings != null) {
			//remove the time if its in storage
			if(alarmStrings.contains(time)) {
				alarmStrings.remove(time);
			}
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putStringSet(ALARM_SET_KEY, alarmStrings);
			editor.commit();
		}
	}

}
