package edu.cmu.lmalkhas.updatemealarm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * class that inherits from manager to create https calls to the New York Times
 * API, and then create a summary of the contents returned.
 * 
 * @author lenamalkhasian
 */
public class NewsManager extends Manager {

	// pieces of the request url
	final String URLbegin = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/";
	String sections = "all-sections";
	final String URLend = "/1.json?api-key=bbdd12afcb4e9e41e4a852fcb9cadc46:12:68474013";

	/**
	 * public function that makes the web service call.
	 * @return the summary of the news.
	 */
	public String getNewsSummary() {
		sections = PersistenceManager.getNewsSections();
		String URL = URLbegin + sections + URLend;
		return callWebService(URL);
	}

	/**
	 * process the response from the https request.
	 */
	protected String processResponse(String response) {
		try {
			System.out.println(response);
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("results");

			String newsSummary = "Here are the top news stories on New York Times from the last 24 hours from "
					+ sections + ".";

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
