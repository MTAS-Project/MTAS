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

public class StrengthFragment extends Fragment {

	ArrayList<String> strengths;
	ArrayList<String> selectedStrengths = new ArrayList<String>();
	
	onStrengthSelectedListener listener;
	
	// interface for passing data to calling activity
	
	public interface onStrengthSelectedListener 
	{
		public void onStrengthSelected(ArrayList<String> strength);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		listener = (onStrengthSelectedListener) activity;
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_strength_fragment, container, false);
		
		displayList(view);
		
		return view;
	}
	
	public void displayList(View view)
	{
		
		ListView list = (ListView) view.findViewById(R.id.strengthsListView);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list_item_checked,strengths);
		list.setAdapter(adapter);
		
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				boolean found = false;
				if(selectedStrengths.isEmpty()==false)
				{
					for(int i=0;i<selectedStrengths.size();i++)
					{
						if(selectedStrengths.get(i).matches(parent.getItemAtPosition(position).toString()))
						{
							found = true;
							selectedStrengths.remove(i);
							break;
						}
					}
				}
				if(found == false)
				{
					selectedStrengths.add(parent.getItemAtPosition(position).toString());
				}

				listener.onStrengthSelected(selectedStrengths);
			}
			
		});
		
	}
	
	public void setStrengths(ArrayList<String> strengths)	{
		
		this.strengths = strengths;
	}

	@Override
	public void onDestroy()	{
		
		super.onDestroy();
	}

}
