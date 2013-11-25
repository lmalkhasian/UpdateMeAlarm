package edu.cmu.lmalkhas.updatemealarm;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class News2Activity extends Activity {
	
	//can make "sections" configurable @ some point.
	
	String URL = "http://api.nytimes.com/svc/mostpopular/v2/most-viewed/all-sections/1.json?api-key=bbdd12afcb4e9e41e4a852fcb9cadc46:12:68474013";
	String result = "";
	final String tag = "asdlf";
	private HttpClient httpclient;
	HttpGet request;
	ResponseHandler<String> handler;

	    /** Called when the activity is first created. */  
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_news2);  
/*
	        final EditText txtSearch = (EditText)findViewById(R.id.txtSearch);  
	        txtSearch.setOnClickListener(new EditText.OnClickListener(){  
	            public void onClick(View v){txtSearch.setText("");}  
	        });  

	        final Button btnSearch = (Button)findViewById(R.id.btnSearch);  
	        btnSearch.setOnClickListener(new Button.OnClickListener(){  
	            public void onClick(View v) {  
	                String query = txtSearch.getText().toString();  
	                callWebService(query);  

	            }  
	        });  */
	        new Thread(new Runnable() {
	        	public void run() {
	    	        callWebService("");
	        	}
	        }).start();

	    } // end onCreate()  

	    public void callWebService(String q){  
	        httpclient = new DefaultHttpClient();  
	        HttpGet request = new HttpGet(URL + q);  
	        //request.addHeader("deviceId", deviceId);  
	        ResponseHandler<String> handler = new BasicResponseHandler();  
	        try {  
            	result = httpclient.execute(request, handler);  
            	Log.i(tag, "TEST");
	            System.out.println(result);
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        httpclient.getConnectionManager().shutdown();  
	        Log.i(tag, result);  
	    } 
	    // end callWebService()  
} 

