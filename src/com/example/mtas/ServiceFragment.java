package com.example.mtas;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ServiceFragment extends Fragment {

	ArrayList<String> services;
	ArrayList<String> selectedServices = new ArrayList<String>();
	
	onServiceSelectedListener listener;
	
	// interface for passing data to calling activity
	
	public interface onServiceSelectedListener 
	{
		public void onServiceSelected(ArrayList<String> service);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		listener = (onServiceSelectedListener) activity;
		
	}
	
//	public void passData(ArrayList<String> networks)
//	{
//		listener.onServiceSelected(networks);
//	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_service_fragment, container, false);
		
		displayList(view);
		
		return view;
	}
	
	public void displayList(View view)
	{
		
		ListView list = (ListView) view.findViewById(R.id.servicesListView);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list_item_checked,services);
		list.setAdapter(adapter);
		
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				boolean found = false;
				if(selectedServices.isEmpty()==false)
				{
					for(int i=0;i<selectedServices.size();i++)
					{
						if(selectedServices.get(i).matches(parent.getItemAtPosition(position).toString()))
						{
							found = true;
							selectedServices.remove(i);
							break;
						}
					}
				}
				if(found == false)
				{
					selectedServices.add(parent.getItemAtPosition(position).toString());
				}

				listener.onServiceSelected(selectedServices);
			}
			
		});
		
	}
	
	public void setServices(ArrayList<String> services)	{
		
		this.services = services;
	}

	@Override
	public void onDestroy()	{
		
		super.onDestroy();
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.section_2, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

}
