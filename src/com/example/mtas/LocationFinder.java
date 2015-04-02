package com.example.mtas;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

public class LocationFinder extends AsyncTask<String,Void,List<Address>>	{

	Context context;
	GoogleMap googleMap;
	
	LocationFinder(Context context, GoogleMap googleMap)	{
		
		this.context = context;
		this.googleMap = googleMap;
		
	}
	@Override
	protected List<Address> doInBackground(String... location) 
	{
		// TODO Auto-generated method stub
		
		Geocoder geoCoder = new Geocoder(context);
		List<Address> address = null;
		
		System.out.println("Searchview = "+location[0]);
		
		try
		{
			for(int i=0;i<10;i++)
			{
				address = geoCoder.getFromLocationName(location[0], 3);
				System.out.println("Searchview = "+location[0]);
				System.out.println("Address = "+address.size());
			}	
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return address;
	}
	
	@Override
	protected void onPostExecute(List<Address> address)
	{
		if(address == null || address.size()==0)
		{
			System.out.println("Geocode : Address Not Found");
		}
		else
		{
			for(int i=0;i<address.size();i++)
			{
				System.out.println("Geocoder : "+address.get(i).toString());
			}
			LatLng latlng = new LatLng(address.get(0).getLatitude(),address.get(0).getLongitude());
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));
		}
	}
}
