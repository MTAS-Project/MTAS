package com.example.mtas;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyCustomLocationListener implements LocationListener{

	private static Location location;
	String msg="MTAS ";
	
	private static MyCustomLocationListener locationListener = null;
	
	private MyCustomLocationListener()
	{
		
	}
	
	public static MyCustomLocationListener getInstance()
	{
		if(locationListener == null)
		{
			locationListener = new MyCustomLocationListener();
		}
    	
		return locationListener;
	}
	
	@Override
	public void onLocationChanged(Location loc) {
		
		System.out.println(msg+ "Location Listener : On Location Change !");
		// TODO Auto-generated method stub
		location = loc;
		System.out.println(msg+ "Location Listener : RRT 1 = "+loc.getLatitude()+","+loc.getLongitude());
//		Toast.makeText(context, "Location Change = "+loc.getLatitude()+","+loc.getLongitude(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public Location getLocation() {
		
		System.out.println(msg+ "Location Listener : Location = "+location);
		return location;
	}
}
