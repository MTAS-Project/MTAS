package com.example.mtas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class TrackRouteService extends Service {


	String  currentNetworkType;
	int currentSignalStrengthIntLevel;

	private DBHandler dbHandler;
	private TelephonyManager telephonyManager;
	private MyCustomPhoneListener myListener = new MyCustomPhoneListener();

	private LocationManager locationManager;
	private LocationListener locationListener = new MyCustomLocationListener();

	// private MyCustomPhoneListener myListener = new MyCustomPhoneListener();

	String msg = "MTAS ";


	private String getNetwork() {
		return telephonyManager.getNetworkOperatorName();
	}

	private String getManufacturer() {
		return Build.MANUFACTURER;
	}

	private String getModel() {
		return Build.MODEL;
	}


	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		int autosaveInterval=30*60;//no of seconds

//		System.out.println(msg + "TRACKROUTESERVICE: On Start Command !");
		dbHandler = new DBHandler(getApplicationContext());

		telephonyManager = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		// strengthValue and networkType is changed using listeners
		telephonyManager.listen(myListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
						| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				autosaveInterval * 1000, 500, locationListener);

		Toast.makeText(this, "Autosave Enabled @ "+autosaveInterval/60+"mins time interval",
				Toast.LENGTH_LONG).show();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

//		System.out.println(msg + "TRACKROUTESERVICE: On DESTROY !");
		telephonyManager.listen(myListener, PhoneStateListener.LISTEN_NONE);
		locationManager.removeUpdates(locationListener);
		Toast.makeText(this, "Autosave Disabled", Toast.LENGTH_LONG)
				.show();
	}


	private class MyCustomPhoneListener extends PhoneStateListener {

		@Override
		public void onSignalStrengthsChanged(SignalStrength ss) {
			super.onSignalStrengthsChanged(ss);
			asuToString(ss.getGsmSignalStrength());
		}

		@Override
		public void onDataConnectionStateChanged(int state, int networkType) {
			super.onDataConnectionStateChanged(state, networkType);
			networkTypeToString(networkType);
		}

		private String networkTypeToString(int networkType) {
			switch (networkType) {
			case 1: {
				return currentNetworkType="G"; // GRPS
			}
			case 2: {
				return currentNetworkType="E"; // EDGE
			}
			case 11: {
				return currentNetworkType="2G";
			}
			case 3: {
				return currentNetworkType="3G";
			}
			case 10: {
				return currentNetworkType="H";
			}
			case 13: {
				return currentNetworkType="4G";
			}
			case 15: {
				return currentNetworkType="H+";
			}
			}
			return "Unkown("+networkType+")"; // this should not be returned, cater for all unknown types
		}

		private String asuToString(int asu) {
			if (asu <= 2 || asu == 99) {
				currentSignalStrengthIntLevel=0;
				return "No service";
			} else if (asu > 2 && asu <= 5) {
				currentSignalStrengthIntLevel=1;
				return "Weak";
			} else if (asu > 5 && asu <= 8) {
				currentSignalStrengthIntLevel=2;
				return "Fair";
			} else if (asu > 8 && asu <= 12) {
				currentSignalStrengthIntLevel=3;
				return "Good";
			} else if (asu > 12) {
				currentSignalStrengthIntLevel=4;
				return "Excellent";
			}
			currentSignalStrengthIntLevel=-1;	//Strength -1 means unknown signal strength 
			return "Unknown"; // this should not appear
		}

	}

	private class MyCustomLocationListener implements LocationListener {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
	//try turning AutoSave off
		}

		@SuppressLint("SimpleDateFormat") @Override
		public void onLocationChanged(Location loc) {
			Reception reception = new Reception();

			reception.setNetworkOp(getNetwork());
			reception.setServiceType(currentNetworkType);
			reception.setSignalStrength(currentSignalStrengthIntLevel);
			reception.setMaker(getManufacturer());
			reception.setModel(getModel());
			reception.setLocation(new LatLng(loc.getLatitude(), loc
					.getLongitude()));

			final SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.000");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = sdf.format(new Date());

			reception.setTimeStamp(date);

			 dbHandler.getWritableDatabase();
			 dbHandler.addPathReception(reception);

			Toast.makeText(getApplicationContext(),
					"Reception saved", Toast.LENGTH_SHORT).show();

		}
	    

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


}
