package edu.cmu.lmalkhas.updatemealarm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmBrain {

	// TODO HAVE A SORTED LIST.
	Set<String> alarms = new HashSet<String>();
	String latestAlarm = null;

	// the default constructor.
	public AlarmBrain() {
		// get the alarms from memory?
		SharedPreferences mPrefs = App.context.getSharedPreferences(PersistenceManager.PREFS_NAME,
				Context.MODE_PRIVATE);
		Set<String> storedAlarms = mPrefs.getStringSet(PersistenceManager.ALARM_SET_KEY, null);
		//add strings to current alarms
		if(storedAlarms != null)
			alarms.addAll(storedAlarms);
	}

	// returns the alarm list currently valid
	public Set<String> getAlarmList() {
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

		if (find(time))
			return false;

		// add to alarm list
		alarms.add(time);

		// make this the latest added alarm
		latestAlarm = time;

		// add to persistent storage
		PersistenceManager.addTime(time);

		printAlarms();
		return true;
	}
	
	//removes the current time
	public boolean removeTime() {
		// get current time
				Calendar currTime = Calendar.getInstance();
				currTime.setTime(new Date());
				String curr_hour = Integer.toString(currTime.get(Calendar.HOUR_OF_DAY));
				String curr_min = Integer.toString(currTime.get(Calendar.MINUTE));
				if(curr_min.length() < 2) {
					curr_min = "0" + curr_min;
				}
				if(curr_hour.length() < 2) {
					curr_hour = "0" + curr_hour;
				}
				System.out.println("removing time " + curr_hour + ":" + curr_min + "from brain and persistence manager");
				return removeTime(curr_hour + ":" + curr_min);
	}
	
	//returns true if time was removed, false if it was not found
	public boolean removeTime(String time) {
		if(!find(time))
			return false;
		
		alarms.remove(time);
		PersistenceManager.removeTime(time);
		printAlarms();
		return true;
	}

	boolean find(String targetTime) {
		return alarms.contains(targetTime);
	}

	void printAlarms() {
		for (String t : alarms) {
			System.out.print(t + "   ");
		}
	}

}
