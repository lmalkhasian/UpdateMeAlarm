package edu.cmu.lmalkhas.updatemealarm;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class AlarmBrain {
	
	//TODO HAVE A SORTED LIST.
	List<String> alarms = new ArrayList<String>();
	String latestAlarm = null;
	
	//TODO PERSISTENT STORE OF ALARMS.
	//TODO HAVE AN ALARM ACTUALLY GO OFF.
	
	
	//the default constructor.
	public AlarmBrain() {
		//get the alarms from memory?
		
	}
	
	//returns the alarm list currently valid
	public List<String> getAlarmList() {
		return alarms;
	}
	
	public String getLastAddedAlarm() {
		return latestAlarm;
	}
	
	public void clearLatest() {
		latestAlarm = null;
	}
	
	//returns true if successful, false otherwise
	public boolean addAlarm(String time) {
		//TODO ADD CHECK THAT AN ALARM AT SAME TIME DOESNT ALREADY EXIST.
		alarms.add(time);
		latestAlarm = time;
		printAlarms();
		return true;
	}
	
	void printAlarms() {
		for(String t : alarms) {
			Log.d("debug", t);
		}
	}
	

}
