package edu.cmu.lmalkhas.updatemealarm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsManager extends Manager {
	// can make "sections" configurable @ some point.

	final String URLbegin = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/";
	String sections = "all-sections";
	final String URLend = "/1.json?api-key=bbdd12afcb4e9e41e4a852fcb9cadc46:12:68474013";
	String URL = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/science/1.json?api-key=bbdd12afcb4e9e41e4a852fcb9cadc46:12:68474013";

	NewsManager() {
		// TODO: add preferences here.
	}

	public String getNewsSummary() {
		return callWebService(URL);
	}

	protected String processResponse(String response) {
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
