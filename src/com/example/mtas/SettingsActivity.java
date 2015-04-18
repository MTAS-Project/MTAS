package com.example.mtas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private Switch autoUpload;
	private Switch autoSave;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		sharedPreferences = getSharedPreferences("Settings", 0);
		editor = sharedPreferences.edit();
		autoUpload = (Switch) findViewById(R.id.autoUploadSwitch);
		autoSave = (Switch) findViewById(R.id.autoSaveSwitch);

		loadPreferences();
	}

	private void loadPreferences() {
		if (sharedPreferences.contains("autoupload")) {
			autoUpload = (Switch) findViewById(R.id.autoUploadSwitch);
			boolean autoUploadOn = sharedPreferences.getBoolean("autoupload",
					true);
			autoUpload.setChecked(autoUploadOn);
		} else {
			editor.putBoolean("autoupload", true);
			editor.commit();
		}

		if (sharedPreferences.contains("autosave")) {
			autoSave = (Switch) findViewById(R.id.autoSaveSwitch);
			boolean autoSaveOn = sharedPreferences.getBoolean("autosave",
					true);
			System.out.println("AS checked: " + autoSaveOn);
			autoSave.setChecked(autoSaveOn);
		} else {
			editor.putBoolean("autosave", true);
			editor.commit();
		}

	}

	// public void onAutoSaveClicked(View view) {
	// // Is the toggle on?
	// boolean on = ((Switch) view).isChecked();
	// editor.putBoolean("autosave", on);
	//
	// System.out.println("Clicked AS: "+on);
	//
	// if (on) {
	// Intent intent = new Intent(this, AutoSaveService.class);
	// // intent.putExtra("timeinterval",3000); // 1 hour
	// startService(intent);
	// } else {
	// Intent intent = new Intent(this, AutoSaveService.class);
	// stopService(intent);
	// }
	// editor.commit();
	// }

	public void onAutoUploadClicked(View view) {
		// Is the toggle on?
		boolean on = ((Switch) view).isChecked();
		editor.putBoolean("autoupload", on);

		System.out.println("Clicked AU: " + on);

		if (on) {
			// IntentFilter intentFilter = new IntentFilter();
			// intentFilter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
			// registerReceiver(autoUploadBR, intentFilter);
			PackageManager packageManager = getPackageManager();
			ComponentName componentName = new ComponentName(
					SettingsActivity.this,
					com.example.mtas.AutoUploadBroadcastReceiver.class);
			packageManager.setComponentEnabledSetting(componentName,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
			Toast.makeText(this, "Autoupload enabled", Toast.LENGTH_SHORT).show();


		} else {
			PackageManager packageManager = getPackageManager();
			ComponentName componentName = new ComponentName(
					SettingsActivity.this,
					com.example.mtas.AutoUploadBroadcastReceiver.class);
			packageManager.setComponentEnabledSetting(componentName,
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
			Toast.makeText(this, "Autoupload disabled", Toast.LENGTH_SHORT).show();

		}
		editor.commit();
	}

	public void onAutoSaveClicked(View view) {
		boolean on = ((Switch) view).isChecked();
		editor.putBoolean("autosave", on);

		System.out.println("AS Clicked: " + on);

		if (on) {
			final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				showGPSDisabledDialog();
				((Switch) view).setChecked(false);
				editor.putBoolean("autosave", false);

			} else {
				Toast.makeText(
				this,
				"Saving anonymous receptions @ 40mins time interval",
				Toast.LENGTH_LONG).show();

				Intent intent = new Intent(this, AutoSaveService.class);
				startService(intent);
			}
		} else {
			Toast.makeText(this, "Stopped saving receptions", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, AutoSaveService.class);
			stopService(intent);
		}
		editor.commit();

	}

	private void showGPSDisabledDialog() {

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Turn on GPS");
		alertDialog.setMessage("GPS is needed to know device's location");
		// alertDialog.setIcon(R.id.);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Location services",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// User pressed YES button. Write Logic Here
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				});
		alertDialog.show();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.menu_main, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	//
	// //noinspection SimplifiableIfStatement
	// if (id == R.id.action_settings) {
	// return true;
	// }
	//
	// return super.onOptionsItemSelected(item);
	// }
}
