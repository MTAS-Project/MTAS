package com.example.mtas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class SaveCurrentReception extends Activity {

	TextView networkValue, networkTypeValue, strengthValue, makeValue,
			modelValue, latitudeValue, longitudeValue;

	String onscreenNetwork, onscreenNetworkType, onscreenMake, onscreenModel;
	int onscreenSignalStrengthIntLevel;
	Location onscreenLocation;
	String locationStatus;

	TelephonyManager telephonyManager;
	LocationManager locationManager;

	LocationListener locationListener = new MyCustomLocationListener();
	MyCustomPhoneListener myListener = new MyCustomPhoneListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_current_reception);

		networkValue = (TextView) findViewById(R.id.networkValueTextView);
		networkTypeValue = (TextView) findViewById(R.id.networkTypeValueTextView);
		strengthValue = (TextView) findViewById(R.id.strengthValueTextView);
		makeValue = (TextView) findViewById(R.id.makeValueTextView);
		modelValue = (TextView) findViewById(R.id.modelValueTextView);
		latitudeValue = (TextView) findViewById(R.id.latitudeValueTextView);
		longitudeValue = (TextView) findViewById(R.id.longitudeValueTextView);

		// Get System TELEPHONY,LOCATION service reference
		telephonyManager = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}
	@Override
	protected void onStart() {
		super.onStart();
		AutoSaveService.stopLocationListener();
		currentReception();
	}
	@Override
	protected void onStop() {
		super.onStop();
		telephonyManager.listen(myListener, PhoneStateListener.LISTEN_NONE);
		locationManager.removeUpdates(locationListener);
		AutoSaveService.startLocationListener();
	}

	public void currentReception() {

		// Criteria criteria = new Criteria();
		//
		// String provider = locationManager.getBestProvider(criteria, false);
		//
		// location = locationManager.getLastKnownLocation(provider);

		networkValue.setText(getNetwork());
		networkTypeValue.setText(getNetworkType());

		// strengthValue and networkType is changed using listeners
		telephonyManager.listen(myListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
						| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

		makeValue.setText(getManufacturer());
		modelValue.setText(getModel());

		// location(lat, long) values are changed using listeners
		locationStatus = "Searching for GPS...";
		latitudeValue.setText(locationStatus);
		longitudeValue.setText(locationStatus);
		// starting searching for GPS (listen for location updates)
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);

	}

	private String getNetwork() {
		return onscreenNetwork = telephonyManager.getNetworkOperatorName();
	}

	private String getManufacturer() {
		return onscreenMake = Build.MANUFACTURER;
	}

	private String getModel() {
		return onscreenModel = Build.MODEL;
	}

	public String getNetworkType() {
		int networkType = telephonyManager.getNetworkType();
		switch (networkType) {
		case 1: {
			return onscreenNetworkType = "G"; // GRPS
		}
		case 2: {
			return onscreenNetworkType = "E"; // EDGE
		}
		case 11: {
			return onscreenNetworkType = "2G";
		}
		case 3: {
			return onscreenNetworkType = "3G";
		}
		case 10: {
			return onscreenNetworkType = "H";
		}
		case 13: {
			return onscreenNetworkType = "4G";
		}
		case 15: {
			return onscreenNetworkType = "H+";
		}
		}
		return "Unkown(" + networkType + ")"; // this should not be returned
	}

	// variable to track event time
	private long mLastClickTime = 0;
	public void onClickSaveReceptionButton(View v) {
		// Preventing multiple clicks, using threshold of 2 second
		// although button visual do not show disabled effect
		if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
			return;
		}
		mLastClickTime = SystemClock.elapsedRealtime();

		if (onscreenLocation != null) {

			Reception reception = new Reception();
			reception.setNetworkOp(onscreenNetwork);
			reception.setServiceType(onscreenNetworkType);
			reception.setSignalStrength(onscreenSignalStrengthIntLevel);
			reception.setMaker(onscreenMake);
			reception.setModel(onscreenModel);
			reception.setLocation(new LatLng(onscreenLocation.getLatitude(),
					onscreenLocation.getLongitude()));
			// device's date/time may not be correctly set
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.000", Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = sdf.format(new Date());

			reception.setTimeStamp(date);

			DBHandler db = new DBHandler(this);
			db.getWritableDatabase();
			db.addPathReception(reception);

			Toast.makeText(getApplicationContext(),
					"Reception saved successfully", Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(getApplicationContext(), locationStatus,
					Toast.LENGTH_SHORT).show();
		}
	}

	private class MyCustomPhoneListener extends PhoneStateListener {

		@Override
		public void onSignalStrengthsChanged(SignalStrength ss) {
			super.onSignalStrengthsChanged(ss);
			int asu = ss.getGsmSignalStrength();
			onscreenSignalStrengthIntLevel = HelpfulStaticFuncs
					.asuToLevel(asu);
			strengthValue.setText(HelpfulStaticFuncs.asuToString(asu));
		}

		@Override
		public void onDataConnectionStateChanged(int state, int networkType) {
			super.onDataConnectionStateChanged(state, networkType);
			onscreenNetworkType = HelpfulStaticFuncs
					.networkTypeIntToString(networkType);
			networkTypeValue.setText(onscreenNetworkType);
		}
	}

	private class MyCustomLocationListener implements LocationListener {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
			locationStatus = "Searching for GPS...";
			latitudeValue.setText(locationStatus);
			longitudeValue.setText(locationStatus);
		}

		@Override
		public void onProviderDisabled(String provider) {
			onscreenLocation = null;
			locationStatus = "GPS is needed to get your location";
			latitudeValue.setText("[Turn on GPS]");
			longitudeValue.setText("[Turn on GPS]");
		}

		@Override
		public void onLocationChanged(Location loc) {
			onscreenLocation = loc;
			locationStatus = "Location found";
			latitudeValue.setText(Double.toString(loc.getLatitude()));
			longitudeValue.setText(Double.toString(loc.getLongitude()));

		}
	}

}