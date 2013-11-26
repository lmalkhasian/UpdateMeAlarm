package edu.cmu.lmalkhas.updatemealarm;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class Manager {

	protected String callWebService(String URL) {

		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(URL);
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			String response = httpclient.execute(request, handler);
			result = processResponse(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return result;
	}

	abstract protected String processResponse(String response);

}
