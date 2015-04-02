package com.example.mtas;

import com.google.android.gms.maps.model.LatLng;

public class Bound {

	private LatLng northEast;
	private LatLng southWest;
	
	Bound()	
	{
		
	}

	public Bound(LatLng northEast, LatLng southWest) {
		
		this.northEast = northEast;
		this.southWest = southWest;
	}

	public LatLng getNorthEast() {
		return northEast;
	}

	public void setNorthEast(LatLng northEast) {
		this.northEast = northEast;
	}

	public LatLng getSouthWest() {
		return southWest;
	}

	public void setSouthWest(LatLng southWest) {
		this.southWest = southWest;
	}
	
}
