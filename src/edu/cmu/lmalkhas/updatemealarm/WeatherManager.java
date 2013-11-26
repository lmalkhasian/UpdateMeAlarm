package edu.cmu.lmalkhas.updatemealarm;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherManager extends Manager{

	final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	String URL = "http://api.wunderground.com/api/04fbf19ce6e52ebe/forecast/astronomy/q/autoip.json";
	
	public String getWeatherSummary() {
		return callWebService(URL);
	}

	protected String processResponse(String response) {
		try {
			JSONObject jsonObj = new JSONObject(response);
			
			String weatherSummary = "Here is your weather summary for today. ";

			// get current day of the week so that we can report weather for only today
			GregorianCalendar currTime = new GregorianCalendar();
			currTime.setTime(new Date());
			int dayofweek = currTime.get(Calendar.DAY_OF_WEEK);
			dayofweek--;

			// get weather
			JSONArray jsonForecastArray = jsonObj.getJSONObject("forecast")
					.getJSONObject("txt_forecast").getJSONArray("forecastday");
			for (int i = 0; i < jsonForecastArray.length() && i < 3; i++) {
				JSONObject obj = jsonForecastArray.getJSONObject(i);
				if(obj.getString("title").contains(days[dayofweek])){
					 weatherSummary += obj.getString("title") + ". " + obj.getString("fcttext") + " ";
					
				}
			}
			
			// get sunrise/sunset
			JSONObject jsonSunPhase = jsonObj.getJSONObject("sun_phase");
			JSONObject jsonSunRise = jsonSunPhase.getJSONObject("sunrise");
			JSONObject jsonSunSet = jsonSunPhase.getJSONObject("sunset");
			String sunSummary = "Today, the sunrise is at "
					+ jsonSunRise.getString("hour") + " "
					+ jsonSunRise.getString("minute") + ". " +
					"The sunset is at "
					+ jsonSunSet.getString("hour") + " "
					+ jsonSunSet.getString("minute") + ". ";

			return weatherSummary + sunSummary;
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}

	}
	
}
