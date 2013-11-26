package edu.cmu.lmalkhas.updatemealarm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PersistenceManager extends Activity {
	
	public static final String PREFS_NAME = "AlarmTimes";
	public static final String SETTINGS_NAME = "Settings";
	public static final String ALARM_SET_KEY = "alarmStringsKey";
	public static final String SETTINGS_KEY = "settingsKey";
	
	public static final String WEATHER_ALARM = "weatherNotifications";
	public static final String NEWS_ALARM = "newsNotifications";
	public static final String FB_ALARM = "fbNotifications";
	
	public static final String NEWS_CATEGORY = "newsCategory";
	public static final String NEWS_ALL = "all-sections";
	
	
	private static SharedPreferences mPrefs;

	PersistenceManager() {
		mPrefs = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	public static void setmPrefs() {
		mPrefs = App.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	public static Set<String> getAllTimes() {
		
		if(mPrefs == null) 
			setmPrefs();
			
		Set<String> alarmStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);
		HashSet<String> retStrings = new HashSet<String>(alarmStrings);
		System.out.println("RETRIEVED STRINGS");
		return retStrings;
		
	}
	
	public static void addTime(String time) {
		if(mPrefs == null)
			setmPrefs(); 
		
		Set<String> alarmStrings;
		Set<String> storedStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);
		if(storedStrings == null) {
			alarmStrings = new HashSet<String>();
		}
		else {
			alarmStrings = new HashSet<String>(storedStrings);
		}
		alarmStrings.add(time);
	    SharedPreferences.Editor editor = mPrefs.edit();
	    editor.putStringSet(ALARM_SET_KEY, alarmStrings);
	    editor.commit();	
	    System.out.println("Successfully commited key");
	}
	
	
	public static void removeTime(String time) {
		if(mPrefs == null)
			setmPrefs();
		Set<String> alarmStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);

		if(alarmStrings != null) {
			//remove the time if it's in storage
			if(alarmStrings.contains(time)) {
				System.out.println("found time " + time + "... removing!");
				alarmStrings.remove(time);
			}
			else {
				System.out.println("did not find the time in persistence manager");
			}
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putStringSet(ALARM_SET_KEY, alarmStrings);
			editor.commit();
		}
	}
	
	public static void changeSetting(String setting, Boolean on) {
		SharedPreferences prefs = App.context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(setting, on);
	    editor.commit();	
	}
	
	public static boolean getSetting(String setting) {
		SharedPreferences prefs = App.context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getBoolean(setting, false);
	}
	
	public static String getNewsSection() {
		SharedPreferences prefs = App.context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(NEWS_CATEGORY, NEWS_ALL);
	}
	
	public static void changeNewsSection(String category) {
		SharedPreferences prefs = App.context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(NEWS_CATEGORY, category);
	    editor.commit();	
	}

}
