package com.example.mtas;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Reception implements ClusterItem {

	private LatLng location;
	private String networkOp;
	private String serviceType;
	private Integer signalStrength;
	private String maker;
	private String model;
	private String timeStamp;

	Reception() {

	}

	public Reception(LatLng location, String networkOp, String serviceType,
			int signalStrength, String maker, String model, String timeStamp) {
		this.location = location;
		this.networkOp = networkOp;
		this.serviceType = serviceType;
		this.signalStrength = signalStrength;
		this.maker = maker;
		this.model = model;
		this.timeStamp = timeStamp;
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
		this.networkOp = networkOp;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void display() {
		System.out.println(this.location + "," + this.networkOp + ","
				+ this.serviceType + "," + this.signalStrength + ","
				+ this.maker + "," + this.model + "," + this.timeStamp);
	}

	@Override
	public LatLng getPosition() {

		return location;
	}

	public String toUrlParameters() {
		String encoding = "UTF-8";
		String longitude, latitude, network, networkType, strength, make, model, time;
		longitude = this.location.longitude + "";
		latitude = this.location.latitude + "";
		network = networkType = strength = make = model = time = "";
		try {
			network = URLEncoder.encode(this.networkOp, encoding);
			networkType = URLEncoder.encode(this.serviceType, encoding);
			strength = URLEncoder.encode(this.signalStrength.toString(),
					encoding);
			make = URLEncoder.encode(this.maker, encoding);
			model = URLEncoder.encode(this.model, encoding);
			time = URLEncoder.encode(this.timeStamp, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String encodedParameters = "longitude=" + longitude + "&latitude=" + latitude
				+ "&service_provider=" + network + "&service_type="
				+ networkType + "&signal_strength=" + strength + "&make="
				+ make + "&model=" + model + "&timestamp=" + time;
		System.out.println(encodedParameters);
		return encodedParameters;
	}
}
