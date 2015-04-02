package com.example.mtas;

import java.sql.Timestamp;

import com.google.android.gms.maps.model.LatLng;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.widget.Toast;

public class RecordReceptionThread extends BroadcastReceiver
{

	String msg="MTAS ";
	private Context contxt;
	private LocationManager locationManager=null;
	private MyCustomLocationListener locationListener = MyCustomLocationListener.getInstance();
	private MyCustomPhoneListener phoneListener = MyCustomPhoneListener.getInstance();
	private DBHandler dbHandler;
	
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		this.contxt = context;
		PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock =  powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wake Lock");

		wakeLock.acquire();
		recordReception();
		wakeLock.release();
	}
	
	public void setAlarm(Context context)
	{
		System.out.println(msg+"RceptionThread: Alarm Set !");
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intnt = new Intent();
		intnt.setAction("com.example.mtas.RecordReceptionThread");
		context.sendBroadcast(intnt);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intnt,0);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 600*1000, pendingIntent);	//900 = 15 minutes
	}
	
	public void cancelAlarm(Context context)
	{
		System.out.println(msg+"RceptionThread: Alarm Cancel !");

		Intent intnt = new Intent();
		intnt.setAction("com.example.mtas.RecordReceptionThread");
		context.sendBroadcast(intnt);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intnt, 0);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		
		if(locationManager!=null)
		{
			locationManager.removeUpdates(locationListener);
			locationManager = null;
		}
		
	}
	
	public void recordReception()
	{
		System.out.println(msg+"RceptionThread: Record Reception !");
		
		TelephonyManager tManager = (TelephonyManager) contxt.getSystemService(Context.TELEPHONY_SERVICE);
		
		String service = getServiceType(tManager);
    	String maker = Build.MANUFACTURER;
    	String model = Build.MODEL;
        String operator = tManager.getNetworkOperatorName();
        Location location = findCurrentLocation();
        int strength = phoneListener.getSignalStrength();
        
    	
    	dbHandler = new DBHandler(contxt);
    	System.out.println(msg+"Path Receptions count = "+dbHandler.getPathReceptionsCount());
    	
    	
        if(location!=null)
        {
        	Reception reception = new Reception();
        	reception.setNetworkOp(operator);
        	reception.setServiceType(service);
        	reception.setSignalStrength(strength);
        	reception.setMaker(maker);
        	reception.setModel(model);
        	reception.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        	
        	String date = (String) (DateFormat.format("yyyy-MM-dd hh:mm:ss.000", new java.util.Date()));
        	
        	reception.setTimeStamp(date);
        	
        	
        	dbHandler.addPathReception(reception);
        	System.out.print(msg+"--");reception.display();
        }
        
        
       
	}
	
	public Location findCurrentLocation()
    {
		System.out.println(msg+ "Find Current Location Called !");
		
		
    	locationManager = (LocationManager) contxt.getSystemService(Context.LOCATION_SERVICE);

    	Criteria criteria = new Criteria();
    	
    	String provider = locationManager.getBestProvider(criteria, true);
    	System.out.println(msg+"Find Current Location: Provider  = "+provider);
    	locationManager.requestLocationUpdates(provider, 5*1000, 0, locationListener);
    	
    	return locationListener.getLocation();
//    	if(location!=null)
//    		System.out.println(msg+"FINDCURRENTLOCATION: Location  = "+location.getLatitude()+","+location.getLongitude());
    }
	
	public String getServiceType(TelephonyManager tManager)
    {
    	switch(tManager.getNetworkType())
    	{
	    	case 1:
	    	{
	    		return "G";			//GRPS
	    	}
	    	case 2:
	    	{
	    		return "E";			//EDGE
	    	}
	    	case 11:
	    	{
	    		return "2G";
	    	}
	    	case 3:
	    	{
	    		return "3G";
	    	}
	    	case 10:
	    	{
	    		return "H";
	    	}
	    	case 13:
	    	{
	    		return "4G";
	    	}
	    	case 15:
	    	{
	    		return "H+";
	    	}
    	}
    	return "No Network";
    }
	 
}
