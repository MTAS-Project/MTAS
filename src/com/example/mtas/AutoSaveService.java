package com.example.mtas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.google.android.gms.maps.model.LatLng;

public class AutoSaveService extends Service {
	private static boolean isRunning = false;

	static String currentNetworkType;
	static int currentSignalStrengthIntLevel;

	private static int autosaveInterval = 40;// in minutes
	private static int distanceChange = 1000; // in meters

	private static DBHandler dbHandler;
	private static SharedPreferences sharedPreferences;
	private static TelephonyManager telephonyManager;
	private final MyCustomPhoneListener myListener = new MyCustomPhoneListener();
	// private static Location lastLocation = null;
	private static LocationManager locationManager;
	private static final LocationListener locationListener = new MyCustomLocationListener();

	String msg = "MTAS ";

	public static void stopLocationListener() {
		if (isRunning) {
			locationManager.removeUpdates(locationListener);
		}
	}

	public static void startLocationListener() {
		if (isRunning) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, autosaveInterval * 60000,
					distanceChange, locationListener);
		}
	}

	private static String getNetwork() {
		return telephonyManager.getNetworkOperatorName();
	}

	private static String getManufacturer() {
		return Build.MANUFACTURER;
	}

	private static String getModel() {
		return Build.MODEL;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		isRunning = true;
		sharedPreferences = getSharedPreferences("LastLocation", 0);

		System.out.println(msg + "TRACKROUTESERVICE: On Start Command !");

		dbHandler = new DBHandler(getApplicationContext());

		telephonyManager = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		// strengthValue and networkType is changed using listeners
		telephonyManager.listen(myListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
						| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		startLocationListener();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopLocationListener();
		telephonyManager.listen(myListener, PhoneStateListener.LISTEN_NONE);
		isRunning = false;
	}

	private class MyCustomPhoneListener extends PhoneStateListener {

		@Override
		public void onSignalStrengthsChanged(SignalStrength ss) {
			super.onSignalStrengthsChanged(ss);
			currentSignalStrengthIntLevel = HelpfulStaticFuncs.asuToLevel(ss
					.getGsmSignalStrength());
		}

		@Override
		public void onDataConnectionStateChanged(int state, int networkType) {
			super.onDataConnectionStateChanged(state, networkType);
			currentNetworkType = HelpfulStaticFuncs
					.networkTypeIntToString(networkType);
		}

	}

	private static class MyCustomLocationListener implements LocationListener {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
			// don't try turning AutoSave off
			// just stopService for a while
		}

		public void onLocationChanged(Location loc) {
			Location lastLocation = getLastLocationFromSharedPreferences();

			if (lastLocation == null) {// SharedPreferences will not have this
										// on fresh install and if user clears
										// app's data
				putAsLastLocationIntoSharedPreferences(loc);
			} else if (lastLocation.distanceTo(loc) >= distanceChange) {
				putAsLastLocationIntoSharedPreferences(loc);
			} else {
				return;
			}
			Reception reception = new Reception();
			reception.setNetworkOp(getNetwork());
			reception.setServiceType(currentNetworkType);
			reception.setSignalStrength(currentSignalStrengthIntLevel);
			reception.setMaker(getManufacturer());
			reception.setModel(getModel());
			reception.setLocation(new LatLng(loc.getLatitude(), loc
					.getLongitude()));

			// device's date/time may not be correctly set
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.000", Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = sdf.format(new Date());

			reception.setTimeStamp(date);

			dbHandler.getWritableDatabase();
			dbHandler.addPathReception(reception);

			System.out.println("Reception saved");

		}

		private Location getLastLocationFromSharedPreferences() {
			String lastLatitudeKeyString = "LastLatitude";
			String lastLongitudeKeyString = "LastLongitude";
			String lastLatitude, lastLongitude;

			lastLatitude = sharedPreferences.getString(
					lastLatitudeKeyString, null);
			lastLongitude =  sharedPreferences.getString(
					lastLongitudeKeyString, null);

			if (lastLatitude == null || lastLongitude == null) {
				return null;
			} else {
				Location loc = new Location(LocationManager.GPS_PROVIDER);
				loc.setLatitude(Double.parseDouble(lastLatitude));
				loc.setLongitude(Double.parseDouble(lastLongitude));
				return loc;
			}
		}

		private void putAsLastLocationIntoSharedPreferences(Location loc) {
			Editor editor = sharedPreferences.edit();
			String lastLatitudeKeyString = "LastLatitude";
			String lastLongitudeKeyString = "LastLongitude";

			editor.putString(lastLatitudeKeyString, Double.toString(loc.getLatitude()) );
			editor.putString(lastLongitudeKeyString, Double.toString(loc.getLongitude()) );
			editor.commit();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
