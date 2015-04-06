package com.example.mtas;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;


public class SettingsActivity extends Activity {

    private Switch autoSave;
    private Switch autoUpload;
    private Switch trackRoute;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AutoUploadBroadcastReceiver autoUploadBR;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences("Settings" ,0);
        editor = sharedPreferences.edit();
        
        autoUploadBR = new AutoUploadBroadcastReceiver();
        loadPreferences();

    }

    private void loadPreferences() {
    	
    	
//        if (sharedPreferences.contains("autosave")) {
//            autoSave = (Switch) findViewById(R.id.autoSaveSwitch);
//            boolean autoSaveOn = sharedPreferences.getBoolean("autosave", true);
//            autoSave.setChecked(autoSaveOn);
//        } else {
//            editor.putBoolean("autosave", true);
//            editor.commit();
//            startService(new Intent(this, AutoSaveService.class));
//        }
        if (sharedPreferences.contains("autoupload")) {
            autoUpload = (Switch) findViewById(R.id.autoUploadSwitch);
            boolean autoUploadOn = sharedPreferences.getBoolean("autoupload", true);
            autoUpload.setChecked(autoUploadOn);
        } else {
            editor.putBoolean("autoupload", true);
            editor.commit();
        }
        
        if(sharedPreferences.contains("autosave")) {
        	trackRoute = (Switch) findViewById(R.id.trackSwitch);
        	boolean trackRouteOn = sharedPreferences.getBoolean("autosave", true);
        	System.out.println("AS checked: "+trackRouteOn);
        	trackRoute.setChecked(trackRouteOn);
        } else {
        	editor.putBoolean("autosave", true);
        	editor.commit();
        	startService(new Intent(this, TrackRouteService.class));
        }

    }

//    public void onAutoSaveClicked(View view) {
//        // Is the toggle on?
//        boolean on = ((Switch) view).isChecked();
//        editor.putBoolean("autosave", on);
//
//        System.out.println("Clicked AS: "+on);
//
//        if (on) {
//            Intent intent = new Intent(this, AutoSaveService.class);
////            intent.putExtra("timeinterval",3000); // 1 hour
//            startService(intent);
//        } else {
//            Intent intent = new Intent(this, AutoSaveService.class);
//            stopService(intent);
//        }
//        editor.commit();
//    }

    public void onAutoUploadClicked(View view) {
        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();
        editor.putBoolean("autoupload", on);

        System.out.println("Clicked AU: "+on);

        if (on) 
        {
//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
//            registerReceiver(autoUploadBR, intentFilter);
        	PackageManager packageManager = getPackageManager();
        	ComponentName componentName = new ComponentName(SettingsActivity.this, com.example.mtas.AutoUploadBroadcastReceiver.class);
        	packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            
        } 
        else 
        {
        	PackageManager packageManager = getPackageManager();
        	ComponentName componentName = new ComponentName(SettingsActivity.this, com.example.mtas.AutoUploadBroadcastReceiver.class);
        	packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            
        }
        editor.commit();
    }

    public void onAutoSaveClicked(View view)
    {
    	
    	boolean on = ((Switch) view).isChecked();
    	editor.putBoolean("autosave", on);
    	
    	System.out.println("AS Clicked: "+on);

        if (on) {
            Intent intent = new Intent(this, TrackRouteService.class);
            startService(intent);
        } else {
            Intent intent = new Intent(this, TrackRouteService.class);
            stopService(intent);
        }
        editor.commit();
    	
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
