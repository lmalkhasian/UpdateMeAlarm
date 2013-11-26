package edu.cmu.lmalkhas.updatemealarm;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class News2Activity extends Activity {

	// can make "sections" configurable @ some point.

	final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	String URL = "http://api.wunderground.com/api/04fbf19ce6e52ebe/forecast/astronomy/q/autoip.json";
	String result = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news2);

		new Thread(new Runnable() {
			public void run() {
				callWebService();
			}
		}).start();

	} // end onCreate()

	public void callWebService() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(URL);
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			result = httpclient.execute(request, handler);
			System.out.println(result);
			processResult();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
	}

	// end callWebService()

	void processResult() {
		try {
			JSONObject jsonObj = new JSONObject(result);
			
			
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

			System.out.println(weatherSummary + sunSummary);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
