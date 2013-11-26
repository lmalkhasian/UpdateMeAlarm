package edu.cmu.lmalkhas.updatemealarm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The AlarmBrain class holds the string of alarms that are displayed on the
 * homescreen. It also takes care of adding/deleting alarms to persistent
 * storage.
 * 
 * @author lenamalkhasian
 * 
 */
public class AlarmBrain {

	Set<String> alarms = new HashSet<String>();
	String latestAlarm = null;

	/**
	 * The default constructor for AlarmBrain
	 */
	public AlarmBrain() {
		// get the alarms from memory
		Set<String> storedAlarms = PersistenceManager.getAllTimes();

		// add strings to current alarms
		if (storedAlarms != null)
			alarms.addAll(storedAlarms);
	}

	/**
	 * @return the alarm list currently valid
	 */
	public Set<String> getAlarmList() {
		return alarms;
	}

	/**
	 * @return the most recently added alarm
	 */
	public String getLastAddedAlarm() {
		return latestAlarm;
	}

	/**
	 * clears the most recent added alarm
	 */
	public void clearLatest() {
		latestAlarm = null;
	}

	/**
	 * Add a time to the alarm list.
	 * 
	 * @param time
	 *            the time to add to the alarmList
	 * @return true if successful, false otherwise
	 */
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

	/**
	 * Remove a time from the alarm list
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean shallowRemoveCurrentTime() {
		String time = getCurrentTime();
		if (!find(time))
			return false;
		alarms.remove(time);
		return true;
	}

	/**
	 * @return the current time in the format "hh:mm"
	 */
	private String getCurrentTime() {
		// get current time
		Calendar currTime = Calendar.getInstance();
		currTime.setTime(new Date());
		String curr_hour = Integer.toString(currTime.get(Calendar.HOUR_OF_DAY));
		String curr_min = Integer.toString(currTime.get(Calendar.MINUTE));
		if (curr_min.length() < 2) {
			curr_min = "0" + curr_min;
		}
		if (curr_hour.length() < 2) {
			curr_hour = "0" + curr_hour;
		}
		return curr_hour + ":" + curr_min;
	}

	/**
	 * Remove the time that it is now from the alarmList if it is in there.
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean removeTime() {
		return removeTime(getCurrentTime());
	}

	/**
	 * Remove the time specified from the alarmList if it is in there.
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean removeTime(String time) {
		if (!find(time))
			return false;

		alarms.remove(time);
		PersistenceManager.removeTime(time);
		printAlarms();
		return true;
	}

	/**
	 * @param targetTime
	 *            the time we are looking for
	 * @return true if the targetTime is in alarmList, false otherwise
	 */
	boolean find(String targetTime) {
		return alarms.contains(targetTime);
	}

	/**
	 * prints the alarms to System.out
	 */
	void printAlarms() {
		for (String t : alarms) {
			System.out.print(t + "   ");
		}
	}

}
