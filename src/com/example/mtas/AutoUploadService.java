package com.example.mtas;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//public class AutoUploadService extends Service {
//    public AutoUploadService() {
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "AutoUpload Enabled", Toast.LENGTH_SHORT).show();
//        return START_STICKY;
//    }
//
//    @Overrid
//    public void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(this, "AutoUpload Disabled", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}

public class AutoUploadService extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		if(checkIfOnline(context)==true)	//if Internet is connected upload data 
		{
			Toast.makeText(context, "Internet is enable", Toast.LENGTH_LONG).show();
//		new Upload
			new UploadAsyncTask().execute(context);
			
		}
		
	}

	public boolean checkIfOnline(Context context)
	{
		//Connectivity Manager handles management of network connection
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//NetworkInfo gets instance of current network state 
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if(networkInfo!=null && networkInfo.isConnected()) // networkInfo returns null in airplane mode
		{
			return true;
		}
		return false;
		
	}
	
	
}
