package com.example.mtas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", 0);

		if (sharedPreferences.contains("autosave")) {
			boolean autoSaveOn = sharedPreferences.getBoolean("autosave",
					true);
			if(autoSaveOn){
				Intent serviceIntent = new Intent(context, AutoSaveService.class);
				context.startService(serviceIntent);
			}	
		}
	}
}
