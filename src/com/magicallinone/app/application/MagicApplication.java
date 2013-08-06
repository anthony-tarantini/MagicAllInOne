package com.magicallinone.app.application;

import com.magicallinone.app.R; 
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xtremelabs.imageutils.ImageLoader.Options;

public class MagicApplication extends Application {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "magicallinone.db";

	private static Handler sHandler; 
	private static Gson sGson;
	private static Context sContext;
	private static boolean sActivityVisible;
	
	static {
		GsonBuilder builder = new GsonBuilder();
		sGson = builder.create();		
	}
	
	public static boolean isActivityVisible(){
		return sActivityVisible;
	}
	
	public static void activityPaused(){
		sActivityVisible = true;
	}
	
	public static void activityResumed(){
		sActivityVisible = false;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		sHandler = new Handler();
		sContext = getApplicationContext();
	}
	
	public static void runOnUiThread(Runnable runnable){
		sHandler.post(runnable);
	}
	
	public static final Context getContext(){
		return sContext;
	}
	
	public static final ContentResolver getResolver(){
		return sContext.getContentResolver();
	}
	
	public static Gson getParser(){
		return sGson;
	}
	
	public static Options getImageLoaderOptions(){
		Options options = new Options();
		options.placeholderImageResourceId = R.drawable.placeholder;
		return options;
	}
	
	public static SharedPreferences getSharedPreferences(){
		SharedPreferences preferences = sContext.getSharedPreferences(sContext.getPackageName(),  Context.MODE_PRIVATE);
		return preferences;
	}
}
