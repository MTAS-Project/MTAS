package com.example.mtas;

import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.model.LatLng;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class TrackRouteService extends Service	{

	 private DBHandler dbHandler;
	 private Location location;
	 private RecordReceptionThread recordReception;
	 private TelephonyManager tManager;
	 private MyCustomPhoneListener phoneListener = MyCustomPhoneListener.getInstance();
	 
	 String msg = "MTAS ";
	 
//	 private Timer timer = new Timer();
//	 private TimerTask tt = new TimerTask() {
//	        @Override
//	        public void run() 
//	        {
//	        	
//	            //here we have to record receptions along the track
//
//	        	TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//	        	
//	        	int signalStrength = 0;
//	        	
//	        	for(CellInfo cellInfo:tManager.getAllCellInfo())
//	        	{
//	        		if(cellInfo instanceof CellInfoGsm)
//	        		{
//	        			signalStrength = ((CellInfoGsm) cellInfo).getCellSignalStrength().getLevel();
//	        		}
//	        	}
//	        	
//	        	
////	        	int signalStrength =  0;
//	        	
//	        	
//	        	String provider = tManager.getNetworkOperatorName();
//	        	String service = getServiceType(tManager);
//	        	
//	        	String maker = Build.MANUFACTURER;
//	        	String model = Build.MODEL;
//	        	
//	        	LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//	    	    Criteria criteria = new Criteria();
//	    	    	
//	    	    String serviceProvider = locationManager.getBestProvider(criteria, false);
//	    	    location = locationManager.getLastKnownLocation(serviceProvider);
////	    		myCustomLocationListener locListener = new myCustomLocationListener();
////	    		
////	    		
////	        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
////	    	    
//	        	
//	    	    
//	    	    if(location!=null)
//	            {
//	    	    	Reception reception = new Reception();
//		    	    reception.setMaker(maker);
//		    	    reception.setModel(model);
//		    	    reception.setNetworkOp(provider);
//		    	    reception.setServiceType(service);
//		    	    reception.setSignalStrength(signalStrength);
//		    	    reception.setTimeStamp(null);
//	    	    	reception.setLocation(new LatLng(location.getLatitude(),location.getLongitude()));
//	    	    	
////	    	    	dbHandler.addReception(reception);
//	    	    	System.out.print("TR: ");reception.display();
//	    	    	System.out.println("Service TR Location : "+ location.getLatitude()+","+location.getLongitude());
//	            }
//	            else
//	            {
//	            	
//	            	System.out.println("Service TR Location : Not Found");
//	            }
//	    	   
//	    	   
//	        }
//	 }
	  
	
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		System.out.println(msg+"TRACKROUTESERVICE: On Start Command !");
//		dbHandler = new DBHandler(getApplicationContext());
		
//		int timeInterval = 10000;
		
		tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		
		//************************START LISTENING SIGNAL STRENGTH ********************************
        try
        {
        	tManager.listen(phoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        //****************************************************************************************
        
		Toast.makeText(this, "Track Route Service Started !", Toast.LENGTH_SHORT).show();
	    recordReception = new RecordReceptionThread();
	   
	    IntentFilter intentfilter = new IntentFilter();
	    intentfilter.addAction("com.example.mtas.RecordReceptionThread");
		registerReceiver(recordReception, intentfilter);
		
		recordReception.setAlarm(TrackRouteService.this);
		
		
		
//	    timer.scheduleAtFixedRate(tt, 0, timeInterval);
		
		
		
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        System.out.println(msg+"TRACKROUTESERVICE: On DESTROY !");
//        timer.cancel();
//        timer.purge();
        
        //********************************** STOP LISTENING SIGNAL STRENGTH **********************
        try
        {
        	tManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        //****************************************************************************************
        
        recordReception.cancelAlarm(TrackRouteService.this);
        unregisterReceiver(recordReception);
        
        Toast.makeText(this, "Track Route Service Disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
