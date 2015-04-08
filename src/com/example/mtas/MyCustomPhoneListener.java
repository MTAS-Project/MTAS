package com.example.mtas;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class MyCustomPhoneListener extends PhoneStateListener {
	private int signal = 0;
	String msg = "MTAS ";

	private static MyCustomPhoneListener phoneListener = null;
	// singletonPattern
	private MyCustomPhoneListener() {
    
	}
	
	// singletonPattern
	public static MyCustomPhoneListener getInstance() {
		if (phoneListener == null) {
			phoneListener = new MyCustomPhoneListener();
		}
		return phoneListener;
	}

	@Override
	public void onSignalStrengthsChanged(SignalStrength ss) {
		super.onSignalStrengthsChanged(ss);
		System.out.println(msg + "PHONELISTENER: Listener called  --");
		signal = asuToLevel(ss.getGsmSignalStrength());
//		System.out.println(msg + "PHONELISTENER: Signal = " + signal);
		// Toast.makeText(getApplicationContext(),
		// "Go to Firstdroid!!! GSM Cinr = "
		// +ss.isGsm()+","+ss.getGsmSignalStrength()+","+ss.getCdmaDbm()+","+ss.getEvdoDbm(),
		// Toast.LENGTH_SHORT).show();
	}

	public int getSignalStrength() {
		return signal;
	}

	public int asuToLevel(int asu) {
		int level = 0;
		if (asu <= 2 || asu == 99) {
			level = 0;
		} else if (asu > 2 && asu <= 5) {
			level = 1;
		} else if (asu > 5 && asu <= 8) {
			level = 2;
		} else if (asu > 8 && asu <= 12) {
			level = 3;
		} else if (asu > 12) {
			level = 4;
		}

		return level;
	}

	public static String levelToString(int level) {
		String quality = "";
		if (level == 0) {
			quality = "No Service";
		} else if (level == 1) {
			quality = "Weak";
		} else if (level == 2) {
			quality = "Fair";
		} else if (level == 3) {
			quality = "Good";
		} else if (level == 4) {
			quality = "Excellent";
		}

		return quality;
	}

}
