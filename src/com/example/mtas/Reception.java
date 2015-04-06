package com.example.mtas;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Reception implements ClusterItem {
	
	private LatLng location;
	private String networkOp;
	private String serviceType;
	private int signalStrength;
	private String maker;
	private String model;
	private String timeStamp;
	
	Reception()
	{
		
	}

	public Reception(LatLng location, String networkOp, String serviceType, int signalStrength, String maker, String model, String timeStamp) 
	{
		this.location = location;
		this.networkOp = networkOp.trim();
		this.serviceType = serviceType.trim();
		this.signalStrength = signalStrength;
		this.maker = maker.trim();
		this.model = model.trim();
		this.timeStamp = timeStamp.trim();
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public String getNetworkOp() {
		return networkOp;
	}

	public void setNetworkOp(String networkOp) {
		this.networkOp = networkOp.trim();
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType.trim();
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker.trim();
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model.trim();
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp.trim();
	}
	
	public void display()
	{
		System.out.println(this.location+","+this.networkOp+","+this.serviceType+","+this.signalStrength+","+this.maker+","+this.model+","+this.timeStamp);
	}

	@Override
	public LatLng getPosition() {
		
		return location;
	}

	public String toUrlParameters() {
		// TODO Auto-generated method stub
		
		return "longitude="+location.longitude+"&latitude="+location.latitude+"&service_provider="+this.networkOp+"&service_type="+this.serviceType+"&signal_strength="+this.signalStrength+"&make="+this.maker+"&model="+this.model+"&timestamp="+this.timeStamp;
	}
}
