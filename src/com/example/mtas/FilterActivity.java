package com.example.mtas;

import java.util.ArrayList;
import java.util.Locale;

import com.example.mtas.MakerFragment.onMakerSelectedListener;
import com.example.mtas.ModelFragment.onModelSelectedListener;
import com.example.mtas.NetworkFragment.onNetworkSelectedListener;
import com.example.mtas.ServiceFragment.onServiceSelectedListener;
import com.example.mtas.StrengthFragment.onStrengthSelectedListener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FilterActivity extends Activity implements ActionBar.TabListener,
				onNetworkSelectedListener, onServiceSelectedListener, onStrengthSelectedListener,
				onMakerSelectedListener, onModelSelectedListener
				
{	
	SectionsPagerAdapter mSectionsPagerAdapter;
	ArrayList<String> networks = new ArrayList<String>();
	ArrayList<String> services = new ArrayList<String>();
	ArrayList<String> strengths = new ArrayList<String>();
	private ArrayList<String> makers = new ArrayList<String>();
	private ArrayList<String> models = new ArrayList<String>();
	
	ArrayList<String> selectedNetworks = new ArrayList<String>();
	ArrayList<String> selectedServices = new ArrayList<String>();
	ArrayList<String> selectedStrengths = new ArrayList<String>();
	private ArrayList<String> selectedMakers = new ArrayList<String>();
	private ArrayList<String> selectedModels = new ArrayList<String>();
	
	ViewPager mViewPager;
	
	String buttonState;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		
		
		Bundle extras = getIntent().getExtras();
		networks = extras.getStringArrayList("networks");
		services = extras.getStringArrayList("services");
		strengths = extras.getStringArrayList("strengths");
		makers = extras.getStringArrayList("makers");
		models = extras.getStringArrayList("models");
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() 
		{
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		

		buttonClicked();
	}
	
	public void buttonClicked()
	{
		Button ok = (Button) findViewById(R.id.OkButton);
		Button cancel = (Button) findViewById(R.id.CancelButton);
		
		
		
		ok.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				buttonState = "OK";
				System.out.println("OK Button called");
				returnOKResult();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				buttonState = "Cancel";
				System.out.println("CANCEL Button called");
			
				returnCANCELResult();
			}
		});
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.filter, menu);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	{
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	{
	
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	{
	
		
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			switch(position)
			{
				case 0:
				{
					NetworkFragment fragment = new NetworkFragment();
					fragment.setNetworks(networks);
					
					return fragment;
				}
				case 1:
				{
					ServiceFragment fragment = new ServiceFragment();
					fragment.setServices(services);
					
					return fragment;
				}
				case 2:
				{
					StrengthFragment fragment = new StrengthFragment();
					fragment.setStrengths(strengths);
					
					return fragment;
				}
				case 3:
				{
					MakerFragment fragment = new MakerFragment();
					fragment.setMakers(makers);
					
					return fragment;
				}
				case 4:
				{
					ModelFragment fragment = new ModelFragment();
					fragment.setModels(models);
					
					return fragment;
				}
			}
			return null;
		}

		@Override
		public int getCount() {
									// Show 5 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			case 4:
				return getString(R.string.title_section5).toUpperCase(l);
			}
			return null;
		}
	}
	
	// recieves data from fragments
	
	@Override
	public void onNetworkSelected(ArrayList<String> network) {
		
		selectedNetworks = network;
		
		System.out.println("Size = "+selectedNetworks.size() );
	}

	@Override
	public void onServiceSelected(ArrayList<String> service) {
		
		selectedServices = service;
	}
	
	@Override
	public void onStrengthSelected(ArrayList<String> strength) {
		
		selectedStrengths = strength;
	}
	
	@Override
	public void onMakerSelected(ArrayList<String> maker) {
		
		selectedMakers = maker;
	}
	
	@Override
	public void onModelSelected(ArrayList<String> model) {
		
		selectedModels = model;
	}
	
	public void returnOKResult()
	{

		
		System.out.println("Size return = "+selectedNetworks.size() );
		
		Intent intent = new Intent();
		intent.putExtra("selectedNetworks", selectedNetworks);
		intent.putExtra("selectedServices", selectedServices);
		intent.putExtra("selectedStrengths", selectedStrengths);
		intent.putExtra("selectedMakers", selectedMakers);
		intent.putExtra("selectedModels", selectedModels);
		
		System.out.println("OK called");
		setResult(RESULT_OK, intent);
		
		finish();
	}
	
	public void returnCANCELResult()
	{
		
	
		Intent intent = new Intent();
		System.out.println("CANCEL called");
		setResult(RESULT_CANCELED, intent);
		
		finish();
		
	}
	
	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent();
		System.out.println("CANCEL called");
		setResult(RESULT_CANCELED, intent);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	
}
