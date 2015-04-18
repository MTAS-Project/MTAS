package com.example.mtas;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

public class MyReceptions extends FragmentActivity {

	private GoogleMap googleMap;
	private DBHandler dbHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_receptions);
		
		dbHandler = new DBHandler(this);
		googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		googleMap.setMyLocationEnabled(true);
		
		showMyReceptions();
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
	}
	
	public void showMyReceptions()
	{
		ArrayList<Reception> myReceptions = new ArrayList<Reception>();
		
		ClusterManager<Reception> clusterMaker = new ClusterManager<Reception>(this, googleMap);
		ReceptionRenderer renderer = new ReceptionRenderer(this, googleMap, clusterMaker);
		MarkerOptions markerOptions = null;

		googleMap.setOnCameraChangeListener(clusterMaker);
		googleMap.setOnMarkerClickListener(clusterMaker);
		googleMap.clear();

		clusterMaker.setRenderer(renderer);
 
		if (dbHandler.getPathReceptionsCount() > 0)
		{
			myReceptions = dbHandler.getPathReceptions(0);
			
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(myReceptions.get(myReceptions.size()-1).getLocation()));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(3));
			
			for (int i = 0; i < myReceptions.size(); i++) 
			{
				if(myReceptions.get(i).getServiceType().equals("H+"))
					myReceptions.get(i).display();
				clusterMaker.addItem(myReceptions.get(i));
			}
		} 
		else 
		{
			Toast.makeText(getApplicationContext(),"No Saved Receptions Yet", Toast.LENGTH_LONG).show();
		}
	}
}
