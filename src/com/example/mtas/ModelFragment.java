package com.example.mtas;

import java.util.ArrayList;

import com.example.mtas.MakerFragment.onMakerSelectedListener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class ModelFragment extends Fragment {

	ArrayList<String> models;
	ArrayList<String> selectedModels = new ArrayList<String>();
	
	onModelSelectedListener listener;
	EditText filterText; 
	// interface for passing data to calling activity
	
	public interface onModelSelectedListener 
	{
		public void onModelSelected(ArrayList<String> model);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		listener = (onModelSelectedListener) activity;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflator.inflate(R.layout.activity_model_fragment, container, false);
		filterText = (EditText) view.findViewById(R.id.filter_text);
		
		displayList(view);
		
		return view;
	}

	public void displayList(View view)
	{
		
		ListView list = (ListView) view.findViewById(R.id.modelsListView);
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list_item_checked,models);
		list.setAdapter(adapter);
		
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				boolean found = false;
				if(selectedModels.isEmpty()==false)
				{
					for(int i=0;i<selectedModels.size();i++)
					{
						if(selectedModels.get(i).matches(parent.getItemAtPosition(position).toString()))
						{
							found = true;
							selectedModels.remove(i);
							break;
						}
					}
				}
				if(found == false)
				{
					selectedModels.add(parent.getItemAtPosition(position).toString());
				}

				listener.onModelSelected(selectedModels);
			}
			
		});
		
		filterText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				adapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setModels(ArrayList<String> models)	// list of models available
	{
		
		this.models = models;
	}
	
	

	@Override
	public void onDestroy()	{
		
		
		super.onDestroy();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.model, menu);
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
