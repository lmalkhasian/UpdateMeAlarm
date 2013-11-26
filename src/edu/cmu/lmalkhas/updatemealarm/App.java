package edu.cmu.lmalkhas.updatemealarm;

import android.app.Application;
import android.content.Context;

/**
 * The App class provides a way to access the application's context from
 * non-activity classes.
 * 
 * @author lenamalkhasian
 * 
 */
public class App extends Application {

	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

}
