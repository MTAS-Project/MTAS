package com.example.mtas;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.maps.android.clustering.Cluster;

public class ClusterReportFragment extends DialogFragment{
	
	private Cluster<Reception> cluster = null;
	private View view;
	
	
	private class ServiceRecord
	{
		String service;
		int strength;
		int records;
		

		public String getService() {
			return service;
		}
		public void setService(String service) {
			this.service = service;
		}
		public int getStrength() {
			return strength;
		}
		public void setStrength(int strength) {
			this.strength = strength;
		}
		public int getRecords() {
			return records;
		}
		public void setRecords(int records) {
			this.records = records;
		}
	}
	private class NetworkRecord
	{
		String network;
		ArrayList<ServiceRecord> serviceRecord;
		
		public NetworkRecord()	{
			network = "";
			serviceRecord = new ArrayList<ServiceRecord>();
		}
		
		public String getNetwork() {
			return network;
		}
		public void setNetwork(String network) {
			this.network = network;
		}
		
		public ArrayList<ServiceRecord> getServiceRecord()
		{
			return serviceRecord;
		}
		
		public void setServiceRecord(ServiceRecord sr)
		{
			serviceRecord.add(sr);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		System.out.println("MTAS cluster report create");
		
		setRetainInstance(true);
		
		if(cluster != null)
		{	
			view = inflater.inflate(R.layout.activity_cluster_report, container, false);
			
			getDialog().setTitle("Cluster Report");
			showReport();
			return view;
		}
		
		
		return null;
	}
	
	 @Override
	public void onPause() 
	 {
	     super.onPause();
	     System.out.println("MTAS cluster report pause");
		
	 }
	 
	public void setSelectedCluster(Cluster<Reception> cluster)
	{
		System.out.println("MTAS cluster set");
		this.cluster = cluster;
	}
	
	public void showReport()
	{
		ArrayList<Reception> receptions =  new ArrayList<Reception>(cluster.getItems());
		ArrayList<NetworkRecord> networkRecord = new ArrayList<NetworkRecord>();
		
		for(int i=0;i<receptions.size();i++)
		{
			boolean networkMatch = false;
			boolean serviceMatch = false;
			
			for(int j=0;j<networkRecord.size();j++)
			{
				networkMatch = false;
				
				if(networkRecord.get(j).getNetwork().matches(receptions.get(i).getNetworkOp()))
				{
					// if data already contains network then add service record
					serviceMatch = false;
					
					ArrayList<ServiceRecord> temp = new ArrayList<ServiceRecord>();
					temp = networkRecord.get(j).getServiceRecord();
					
					// loop through services added for this network
					for(int k=0; k<temp.size();k++)
					{
						// if service already added in network record
						if(temp.get(k).getService().matches(receptions.get(i).getServiceType()))
						{
							networkRecord.get(j).getServiceRecord().get(k).setStrength(temp.get(k).getStrength()+receptions.get(i).getSignalStrength());
							networkRecord.get(j).getServiceRecord().get(k).setRecords(temp.get(k).getRecords()+1);
							
//							networkRecord.get(j).setServiceRecord(temp.get(k));
							
							serviceMatch = true;
							break;
						}
					}
					if(serviceMatch == false)	//if service not found in network record
					{
						ServiceRecord s = new ServiceRecord();
						s.setService(receptions.get(i).getServiceType());
						s.setStrength(receptions.get(i).getSignalStrength());
						s.setRecords(1);
						networkRecord.get(j).setServiceRecord(s);
					}
					networkMatch = true;
					break;
				}
			}
			
			if(networkMatch == false)	// data not found
			{
				NetworkRecord n = new NetworkRecord();
				n.setNetwork(receptions.get(i).getNetworkOp());
				
				ServiceRecord s = new ServiceRecord();
				s.setService(receptions.get(i).getServiceType());
				s.setStrength(receptions.get(i).getSignalStrength());
				s.setRecords(1);
				
				n.setServiceRecord(s);
				
				networkRecord.add(n);
			}
		}
		
		TableLayout table = (TableLayout) view.findViewById(R.id.table);
		
		
		for(int x=0;x<networkRecord.size();x++)
		{
			System.out.println("MTAS "+networkRecord.get(x).getServiceRecord().size());
			
			this.addRow(table, networkRecord.get(x).getNetwork(), networkRecord.get(x).getServiceRecord());
		}
//		TextView tv = (TextView) view.findViewById(R.id.report);
//		tv.setText(String.valueOf(data.size())+"\n");
//		for(int x=0;x<data.size();x++)
//		{
//			tv.append(data.get(x).getNetwork()+","+data.get(x).getService()+","+
//					data.get(x).getStrength()+","+data.get(x).getRecords()+"\n");
//		}
	}
	
	public void addRow(TableLayout table, String network, ArrayList<ServiceRecord> serviceRecord)
	{
		
		
		for(int i=0;i<serviceRecord.size();i++)
		{
			TableRow row = new TableRow(getActivity().getApplicationContext());
			// row 1
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.gravity = Gravity.CENTER;
			rowParams.leftMargin = 10;
			rowParams.rightMargin = 10;
			rowParams.width = LayoutParams.WRAP_CONTENT;
			rowParams.height = LayoutParams.WRAP_CONTENT;
			
			// column 1
			TableRow.LayoutParams colParams1 = new TableRow.LayoutParams();
			colParams1.height = LayoutParams.WRAP_CONTENT;
			colParams1.width = LayoutParams.WRAP_CONTENT;
			
			TextView col1 = new TextView(getActivity().getApplicationContext());
			col1.setText(network);
			col1.setTextColor(Color.BLACK);
			col1.setGravity(Gravity.CENTER);
			col1.setTextSize(12);
					
			row.addView(col1, colParams1);
				
		
			TableRow.LayoutParams colParams2 = new TableRow.LayoutParams();
			colParams2.height = LayoutParams.WRAP_CONTENT;
			colParams2.width = LayoutParams.WRAP_CONTENT;
			TextView col2 = new TextView(getActivity().getApplicationContext());
			col2.setTextColor(Color.BLACK);
			col2.setText(serviceRecord.get(i).getService());
			col2.setGravity(Gravity.CENTER);
			col2.setTextSize(12);
			
			row.addView(col2, colParams2);
			
			TableRow.LayoutParams colParams3 = new TableRow.LayoutParams();
			colParams3.height = LayoutParams.WRAP_CONTENT;
			colParams3.width = LayoutParams.WRAP_CONTENT;
			TextView col3 = new TextView(getActivity().getApplicationContext());
			col3.setText(levelToString(serviceRecord.get(i).getStrength()/serviceRecord.get(i).getRecords()));
			col3.setTextColor(Color.BLACK);
			col3.setGravity(Gravity.CENTER);
			col3.setTextSize(12);
			
			row.addView(col3, colParams3);
			
			TableRow.LayoutParams colParams4 = new TableRow.LayoutParams();
			colParams4.height = LayoutParams.WRAP_CONTENT;
			colParams4.width = LayoutParams.WRAP_CONTENT;
			TextView col4 = new TextView(getActivity().getApplicationContext());
			col4.setText(String.valueOf(serviceRecord.get(i).getRecords()));
			col4.setTextColor(Color.BLACK);
			col4.setGravity(Gravity.CENTER);
			col4.setTextSize(12);
			
			row.addView(col4, colParams4);
			
			table.addView(row, rowParams);
		}
		
	}
	
	private String levelToString(int level) {
		
		if (level == 0) {
			return "No service";
		} else if (level == 1) {
			return "Weak";
		} else if (level == 2) {
			return "Fair";
		} else if (level == 3) {
			return "Good";
		} else if (level == 4) {
			return "Excellent";
		}
		return "Unknown"; // this should not appear
	}

}
