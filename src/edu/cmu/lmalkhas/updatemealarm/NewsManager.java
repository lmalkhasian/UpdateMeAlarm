package edu.cmu.lmalkhas.updatemealarm;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsManager {
	// can make "sections" configurable @ some point.

	final String URLbegin = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/";
	String sections = "all-sections";
	final String URLend = "/1.json?api-key=bbdd12afcb4e9e41e4a852fcb9cadc46:12:68474013";
	String URL = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/1.json?api-key=bbdd12afcb4e9e41e4a852fcb9cadc46:12:68474013";

	NewsManager() {
		// TODO: add preferences here.

	}

	public String getNewsSummary() {
		return callWebService();
	}

	private String callWebService() {

		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(URL);
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			String response = httpclient.execute(request, handler);
			result = processResponseAndProcess(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return result;
	}

	// end callWebService()

	private String processResponseAndProcess(String response) {
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("results");

			String newsSummary = "Here are the top ten news stories on New York Times from the last 24 hours.";

			for (int i = 0; i < jsonArray.length() && i < 10; i++) {
				JSONObject obj = jsonArray.getJSONObject(i);

				String titleString = obj.getString("title");
				String abstractString = obj.getString("abstract");

				newsSummary += " Title: " + titleString + ". Abstract: "
						+ abstractString + ".";
			}
			return newsSummary;
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

}
