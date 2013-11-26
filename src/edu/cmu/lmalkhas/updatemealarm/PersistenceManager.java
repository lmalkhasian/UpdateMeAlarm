package edu.cmu.lmalkhas.updatemealarm;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class that handle's the entire applications persistence management. Stores
 * preferences in the SharedPreferences.
 * 
 * @author lenamalkhasian
 * 
 */
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
	public static final String NEWS_WORLD = "world";
	public static final String NEWS_US = "u.s.";
	public static final String NEWS_POLITICS = "politics";
	public static final String NEWS_TECHNOLOGY = "technology";
	public static final String NEWS_SPORTS = "sports";

	private static SharedPreferences mPrefs;

	PersistenceManager() {
		mPrefs = getApplicationContext().getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * Set mPrefs to shared preferences
	 */
	public static void setmPrefs() {
		mPrefs = App.context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * Get all previously stored alarm times.
	 * 
	 * @return Set of alarm times in preferences.
	 */
	public static Set<String> getAllTimes() {
		if (mPrefs == null)
			setmPrefs();

		return mPrefs.getStringSet(PersistenceManager.ALARM_SET_KEY, null);
	}

	/**
	 * Add an alarm time to storage.
	 * 
	 * @param time
	 *            the time to add
	 */
	public static void addTime(String time) {
		if (mPrefs == null)
			setmPrefs();

		Set<String> alarmStrings;
		Set<String> storedStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);
		if (storedStrings == null) {
			alarmStrings = new HashSet<String>();
		} else {
			alarmStrings = new HashSet<String>(storedStrings);
		}
		alarmStrings.add(time);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putStringSet(ALARM_SET_KEY, alarmStrings);
		editor.commit();
		System.out.println("Successfully commited key");
	}

	/**
	 * remove a time from storage
	 * 
	 * @param time
	 *            the time to remove
	 */
	public static void removeTime(String time) {
		if (mPrefs == null)
			setmPrefs();
		Set<String> alarmStrings = mPrefs.getStringSet(ALARM_SET_KEY, null);

		if (alarmStrings != null) {
			// remove the time if it's in storage
			if (alarmStrings.contains(time)) {
				System.out.println("found time " + time + "... removing!");
				alarmStrings.remove(time);
			} else {
				System.out
						.println("did not find the time in persistence manager");
			}
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putStringSet(ALARM_SET_KEY, alarmStrings);
			editor.commit();
		}
	}

	/**
	 * Change a setting (for example whether facebook notifications are read)
	 * 
	 * @param setting
	 *            the setting that is being enabled/disabled
	 * @param on
	 *            whether the setting is enabled or disabled
	 */
	public static void changeSetting(String setting, Boolean on) {
		SharedPreferences prefs = App.context.getSharedPreferences(
				SETTINGS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(setting, on);
		editor.commit();
	}

	/**
	 * returns the value of the setting specified, false if it is not specified.
	 * 
	 * @param setting
	 *            to look up
	 * @return whether setting is enabled or disabled.
	 */
	public static boolean getSetting(String setting) {
		SharedPreferences prefs = App.context.getSharedPreferences(
				SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getBoolean(setting, false);
	}

	/**
	 * Get the news sections that have been enabled.
	 * 
	 * @return a list of semicolon separated news sections.
	 */
	public static String getNewsSections() {
		// check all the news sections here, return them semicolon separated
		String sections = "";
		String COMMA = ",";

		if (getSetting(NEWS_WORLD))
			sections += NEWS_WORLD + COMMA;
		if (getSetting(NEWS_US))
			sections += NEWS_US + COMMA;
		if (getSetting(NEWS_POLITICS))
			sections += NEWS_POLITICS + COMMA;
		if (getSetting(NEWS_TECHNOLOGY))
			sections += NEWS_TECHNOLOGY + COMMA;
		if (getSetting(NEWS_SPORTS))
			sections += NEWS_SPORTS + COMMA;

		if (sections.isEmpty()) {
			sections = NEWS_ALL;
		}

		System.out.println("SECTIONS = " + sections);
		return sections;
	}

}
