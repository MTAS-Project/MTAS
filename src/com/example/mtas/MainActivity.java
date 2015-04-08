package com.example.mtas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener;
//import com.example.mtas.SaveCurrentReception.myCustomPhoneListener;
//import com.google.api.services.mapsengine.model.Icon;

public class MainActivity extends FragmentActivity implements
		OnClusterClickListener<Reception> {
	String msg = "MTAS ";
	// private ServerHandlerAsyncTask downloadFeaturesTask = null;
	private ArrayList<ServerHandlerAsyncTask> serverHandlerAsyncTasks = new ArrayList<>();

	private GoogleMap googleMap;

	private ArrayList<Reception> receptions = new ArrayList<Reception>();

	private Location location;

	private ArrayList<String> networks = new ArrayList<String>();
	private ArrayList<String> services = new ArrayList<String>();
	private ArrayList<String> strengths = new ArrayList<String>();
	private ArrayList<String> makers = new ArrayList<String>();
	private ArrayList<String> models = new ArrayList<String>();

	private ArrayList<String> selectedNetworks = new ArrayList<String>();
	private ArrayList<String> selectedServices = new ArrayList<String>();
	private ArrayList<String> selectedStrengths = new ArrayList<String>();
	private ArrayList<String> selectedMakers = new ArrayList<String>();
	private ArrayList<String> selectedModels = new ArrayList<String>();

	// private MyCustomLocationListener locationListener;
	// private LocationManager locationManager=null;

	private DBHandler dbHandler;

	private ProgressBar progressBar;
	private ImageButton refreshData;
	private Button filterButton;
	private TextView lastUpdate;

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	// public void findCurrentLocation()
	// {
	// System.out.println(msg+ "FINDCURRENTLOCATION Called !");
	//
	// locationListener = MyCustomLocationListener.getInstance();
	//
	// locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	//
	// Criteria criteria = new Criteria();
	//
	// String provider = locationManager.getBestProvider(criteria, true);
	// System.out.println(msg+"FINDCURRENTLOCATION: Provider  = "+provider);
	// locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
	// location = locationListener.getLocation();
	// if(location!=null)
	// System.out.println(msg+"FINDCURRENTLOCATION: Location  = "+location.getLatitude()+","+location.getLongitude());
	// }

	public void getLastKnownLocation() {

		System.out.println(msg + "LASTKNOWNLOCATION Called !");

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();

		String provider = locationManager.getBestProvider(criteria, true);

		location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			System.out.println(msg + "Last Location: " + location.getLatitude()
					+ "," + location.getLongitude());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		System.out.println(msg + "The onCreate() event");

		googleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		progressBar = (ProgressBar) findViewById(R.id.mainProgress);
		refreshData = (ImageButton) findViewById(R.id.refreshButton);
		filterButton = (Button) findViewById(R.id.filterButton);
		lastUpdate = (TextView) findViewById(R.id.lastUpdateTV);

		dbHandler = new DBHandler(getApplicationContext());
		dbHandler.getWritableDatabase();

		refreshData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (checkIfOnline(getApplicationContext()))// depends on making
															// refreshData
															// button invisible
				{
					progressBar.setVisibility(View.VISIBLE);
					refreshData.setVisibility(View.INVISIBLE);
					filterButton.setVisibility(View.INVISIBLE);
					Toast.makeText(getApplicationContext(), "Updating data...",
							Toast.LENGTH_SHORT).show();
					// executing new thread
					ServerHandlerAsyncTask refreshAsyncTask = new ServerHandlerAsyncTask();
					serverHandlerAsyncTasks.add(refreshAsyncTask);
					refreshAsyncTask.execute();

				} else {
					Toast.makeText(getApplicationContext(),
							"Internet not available", Toast.LENGTH_LONG).show();
				}
			}
		});

		strengths.add("Weak");
		strengths.add("Fair");
		strengths.add("Good");
		strengths.add("Excellent");

		// clusterMaker = new ClusterManager<Reception>(this, googleMap);
		//
		// renderer = new ReceptionRenderer(this, googleMap, clusterMaker);
		//
		// clusterMaker.setRenderer(renderer);

		startServicesIfFirstLaunch();

		launchApplication();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		if (id == R.id.action_save_current_reception) {
			Intent intent = new Intent(this, SaveCurrentReception.class);
			startActivity(intent);
			return true;

		}
		if (id == R.id.action_show_my_receptions) {
			new showMyReceptions().execute();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Called when the activity is about to become visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		sharedPreferences = getSharedPreferences("LastUpdate", MODE_PRIVATE);
		if (sharedPreferences.contains("timestamp")) {
			String timeStamp = sharedPreferences.getString("timestamp",
					"Not found");
			lastUpdate.setText("Last Updated: " + timeStamp);
		}
		System.out.println(msg + "The onStart() event");
	}

	/**
	 * Called when the activity has become visible.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println(msg + "The onResume() event");
		// if (!isDeviceOnline(false)) {
		// // Internet Connection is not present
		// showInternetUnavailabilityDialog();
		// }
		// if(executeReady == true ){
		//
		// // downloadFeaturesTask = new ServerHandler();
		// downloadFeaturesTask.execute();
		// }
	}

	/**
	 * Called when another activity is taking focus.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(msg, "The onPause() event");
	}

	/**
	 * Called when the activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		System.out.println(msg + "The onStop() event");
		// if(locationManager!=null)
		// {
		// locationManager.removeUpdates(locationListener);
		// locationManager=null;
		// }
	}

	// @Override
	// public void onBackPressed()
	// {
	// new AlertDialog.Builder(this)
	// .setMessage("Are you sure you want to quit?")
	// .setCancelable(false)
	// .setNegativeButton("No", null)
	// .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// MainActivity.this.finish();
	// }
	// })
	// .show();
	// }
	/**
	 * Called just before the activity is destroyed.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println(msg + "The onDestroy() event");
//		googleMap.setMyLocationEnabled(false);
		for (ServerHandlerAsyncTask task : serverHandlerAsyncTasks) {
			task.cancel(true); // true becoz interrupting 'download' will cause
								// no
								// harm instead it will stop useless network
								// activity if any.
								// Useless because user is leaving the activity.
		}
	}

	public void launchApplication() {
		System.out.println(msg + "Launch Application ! -- "
				+ dbHandler.getAllReceptionsCount());

		getLastKnownLocation();
		loadMap();

		// download receptions on first launch
		if (dbHandler.getAllReceptionsCount() == 0) {
			if (checkIfOnline(getApplicationContext())) {
				progressBar.setVisibility(View.VISIBLE);
				refreshData.setVisibility(View.INVISIBLE);
				filterButton.setVisibility(View.INVISIBLE);
				ServerHandlerAsyncTask serverHandlerAsyncTaskForFirstLaunch = new ServerHandlerAsyncTask();
				serverHandlerAsyncTasks
						.add(serverHandlerAsyncTaskForFirstLaunch);
				serverHandlerAsyncTaskForFirstLaunch.execute();
			} else {

			}
		}
		if (dbHandler.getAllReceptionsCount() > 0) {
			receptions = dbHandler.getAllReceptions();
			setDataonMap();
			updateFilterLists();

		}

	}

	// private void showInternetUnavailabilityDialog()
	// {
	// System.out.println(msg+ "Internet Unavailability Dialog !");
	//
	// AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	// alertDialog.setTitle("Device Is Not Connected");
	// alertDialog.setMessage("An active internet connection is needed to download the collected phone receps.");
	// //alertDialog.setIcon(R.id.);
	// alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Device Settings", new
	// DialogInterface.OnClickListener()
	// {
	// public void onClick(DialogInterface dialog, int which)
	// {
	// // User pressed YES button. Write Logic Here
	// Intent intent = new Intent(Settings.ACTION_SETTINGS);
	// startActivity(intent);
	// }
	// });
	// alertDialog.show();
	// }

	public boolean isDeviceOnline(boolean showToastIfConnected) {
		System.out.println(msg + "Is Device Online !");

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (showToastIfConnected)
				Toast.makeText(
						this,
						"Detected "
								+ HelpfulStaticFuncs.capitalize(networkInfo
										.getTypeName()) + " Data Connection",
						Toast.LENGTH_SHORT).show();
			return true;
		} else {
			return false;
		}

	}

	private void loadMap() {
		System.out.println(msg + "Load Map !");
		// MapsInitializer.initialize(this);

		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();

			// Toast.makeText(getApplicationContext(),
			// "Location MA LoadMap : "+latitude+","+longitude,
			// Toast.LENGTH_SHORT).show();

			LatLng latLng = new LatLng(latitude, longitude);

			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));
		} else {
			// Toast.makeText(getApplicationContext(),
			// "Location MA LoadMap : Not Found", Toast.LENGTH_SHORT).show();

		}

		googleMap.setMyLocationEnabled(true);

		// googleMap.setOnMyLocationButtonClickListener(this);

		SearchView searchView = (SearchView) findViewById(R.id.searchLocation);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub

				String location = query;
				if (location.equals("") == false && location != null)// i changed it to AND from OR
				{

					new LocationFinder(getApplicationContext(), googleMap)
							.execute(location);
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	// ------------------------------------------------- CURRENT LOCATION BUTTON
	// LISTENER ----------------------------

	// @Override
	// public boolean onMyLocationButtonClick() {
	//
	// System.out.println(msg+ "Location Button Clicked !");
	// //findCurrentLocation();
	//
	//
	// findCurrentLocation();
	//
	// if(location!=null)
	// {
	// double latitude = location.getLatitude();
	// double longitude = location.getLongitude();
	//
	// Toast.makeText(getApplicationContext(),
	// "Location MA LocationButton : "+latitude+","+longitude,
	// Toast.LENGTH_SHORT).show();
	//
	// LatLng latLng = new LatLng(latitude,longitude);
	//
	// googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	// googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));
	// }
	// else
	// {
	// Toast.makeText(getApplicationContext(),
	// "Location MA LocationButton : Not Found", Toast.LENGTH_SHORT).show();
	//
	// }
	//
	//
	// return false;
	// }
	//
	// ---------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------- FILTER DATA
	// -------------------------------------------------

	public ArrayList<Reception> getFilteredReceptions() {
		boolean match1 = false;
		boolean match2 = false;
		boolean match3 = false;
		boolean match4 = false;
		boolean match5 = false;

		ArrayList<Reception> filteredReceptions = new ArrayList<Reception>();

		if (receptions.isEmpty() == false) {
			if (selectedNetworks.isEmpty() && selectedServices.isEmpty()
					&& selectedStrengths.isEmpty() && selectedMakers.isEmpty()
					&& selectedModels.isEmpty()) {
				filteredReceptions = receptions;
				return filteredReceptions;
			} else {
				for (int i = 0; i < receptions.size(); i++) {
					match1 = false;
					match2 = false;
					match3 = false;
					match4 = false;
					match5 = false;

					if (selectedNetworks.isEmpty() == false) {
						for (int j = 0; j < selectedNetworks.size(); j++) {
							if (receptions.get(i).getNetworkOp()
									.matches(selectedNetworks.get(j))) {
								match1 = true;
								break;
							}
						}
					} else {
						match1 = true;
					}
					if (selectedServices.isEmpty() == false) {
						for (int k = 0; k < selectedServices.size(); k++) {
							if (receptions.get(i).getServiceType()
									.matches(selectedServices.get(k))) {
								match2 = true;
								break;
							}
						}
					} else {
						match2 = true;
					}
					if (selectedStrengths.isEmpty() == false) {
						int strength = 0;
						for (int m = 0; m < selectedStrengths.size(); m++) {

							switch (selectedStrengths.get(m)) {
							case "Weak":
								strength = 1;
								break;
							case "Fair":
								strength = 2;
								break;
							case "Good":
								strength = 3;
								break;
							case "Excellent":
								strength = 4;
								break;
							}
							if (receptions.get(i).getSignalStrength() == strength) {
								match3 = true;
								break;
							}
						}
					} else {
						match3 = true;
					}
					if (selectedMakers.isEmpty() == false) {
						for (int n = 0; n < selectedMakers.size(); n++) {
							if (receptions.get(i).getMaker()
									.matches(selectedMakers.get(n))) {
								match4 = true;
								break;
							}
						}
					} else {
						match4 = true;
					}

					if (selectedModels.isEmpty() == false) {
						for (int n = 0; n < selectedModels.size(); n++) {
							if (receptions.get(i).getModel()
									.matches(selectedModels.get(n))) {
								match5 = true;
								break;
							}
						}
					} else {
						match5 = true;
					}

					if (match1 == true && match2 == true && match3 == true
							&& match4 == true && match5 == true) {
						filteredReceptions.add(receptions.get(i));
						// System.out.print("Reception = ");
						// receptions.get(i).display();
					}
				}
			}
		}
		return filteredReceptions;
	}

	// ---------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------- PLACE DATA ON MAP
	// -------------------------------------------

	public void setDataonMap() {
		ArrayList<Reception> tempRecp = getFilteredReceptions();

		System.out.println(msg + "-- " + tempRecp.size());

		ClusterManager<Reception> clusterMaker = new ClusterManager<Reception>(
				this, googleMap);
		ReceptionRenderer renderer = new ReceptionRenderer(this, googleMap,
				clusterMaker);
		MarkerOptions markerOptions = null;

		googleMap.setOnCameraChangeListener(clusterMaker);
		googleMap.setOnMarkerClickListener(clusterMaker);
		googleMap.clear();

		clusterMaker.setRenderer(renderer);
		clusterMaker.setOnClusterClickListener(this);

		for (int i = 0; i < tempRecp.size(); i++) {

			// marker = googleMap.addMarker(new MarkerOptions()
			// .position(receptions.get(i).getLocation())
			// .title(receptions.get(i).getNetworkOp())
			// .snippet("Network Type/Strength: "+receptions.get(i).getServiceType())
			// .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

			if (tempRecp.get(i).getSignalStrength() != 0) {
				clusterMaker.addItem(tempRecp.get(i));

				// renderer.onBeforeClusterItemRendered(tempRecp.get(i),
				// markerOptions);
				// clusterMaker.addItem(tempRecp.get(i)); //for markers and
				// clusters

				// Circle circle = googleMap.addCircle(new CircleOptions()
				// .center(tempRecp.get(i).getLocation())
				// .radius(10000)
				// .strokeColor(Color.RED)
				// .fillColor(Color.BLUE));
			}

		}

	}

	// ---------------------------------------------------------------------------------------------------------------

	// ----------------------------------------------- LISTENER FOR FILTER
	// BUTTON -----------------------------------

	public void updateFilterLists() {
		for (int i = 0; i < receptions.size(); i++) {
			if (networks.contains(receptions.get(i).getNetworkOp()) == false) {
				networks.add(receptions.get(i).getNetworkOp());
			}
			if (services.contains(receptions.get(i).getServiceType()) == false) {
				services.add(receptions.get(i).getServiceType());
			}
			if (makers.contains(receptions.get(i).getMaker()) == false) {
				makers.add(receptions.get(i).getMaker());
			}
			if (models.contains(receptions.get(i).getModel()) == false) {
				models.add(receptions.get(i).getModel());
			}
		}

	}

	public void onClickFilterButton(View view) {
		System.out.println(msg + "Filter Button Clicked !");

		selectFilters();
	}

	// ---------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------ SENDING POSSIBLE FILTERS
	// -------------------------------------

	public void selectFilters() {
		System.out.println(msg + "Select Filters !");

		if (receptions.isEmpty() == false) {
			// System.out.println(msg+"Makers size  = "+makers.size()+","+models.size());
			System.out.println("FilterActivity Button");

			Intent intent = new Intent(this, FilterActivity.class);
			intent.putExtra("networks", networks);
			intent.putExtra("services", services);
			intent.putExtra("strengths", strengths);
			intent.putExtra("makers", makers);
			intent.putExtra("models", models);
			startActivityForResult(intent, 1);

		}
	}

	// --------------------------------------------------------------------------------------------------------------

	// ----------------------------------------------- GET SELECTED FILTERS
	// ----------------------------------------

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("MAIN : " + requestCode + "," + resultCode);

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();

				selectedNetworks = bundle
						.getStringArrayList("selectedNetworks");
				selectedServices = bundle
						.getStringArrayList("selectedServices");
				selectedStrengths = bundle
						.getStringArrayList("selectedStrengths");
				selectedMakers = bundle.getStringArrayList("selectedMakers");
				selectedModels = bundle.getStringArrayList("selectedModels");

				// for(int i=0;i<selectedServices.size();i++)
				// {
				// System.out.println("MAIN = "+selectedServices.get(i));
				// }
				// System.out.println("MAIN : OK , "+selectedNetworks.size()+","+selectedServices.size());

			}
			if (resultCode == RESULT_CANCELED) {
				System.out.println("MAIN : CANCEL");
			}
			setDataonMap();
		}

	}

	// --------------------------------------------------------------------------------------------------------------

	// ----------------------------------------------- START SERVICES ON START
	// -------------------------------------

	private void startServicesIfFirstLaunch() {

		sharedPreferences = getSharedPreferences("Settings", 0);
		editor = sharedPreferences.edit();

		// if (!sharedPreferences.contains("autosave"))
		// {
		// //this is first launch of the application, start the autoSave service
		// and put this action in sharedPreferences of this application
		// editor.putBoolean("autosave", true);
		// editor.commit();
		//
		// startService(new Intent(this, AutoSaveService.class));
		// }
		if (!sharedPreferences.contains("autoupload")) {
			// this is first launch of the application, start the autoUpload
			// service and put this action in sharedPreferences of this
			// application
			editor.putBoolean("autoupload", true);
			editor.commit();

			// AutoUploadBroadcastReceiver autoUploadBR = new
			// AutoUploadBroadcastReceiver();
			// IntentFilter intentFilter = new IntentFilter();
			// intentFilter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
			// registerReceiver(autoUploadBR, intentFilter);
		}
		if (!sharedPreferences.contains("autosave")) {
			// this is first launch of the application, start the trackRoute
			// service and put this action in sharedPreferences of this
			// application
			editor.putBoolean("autosave", true);
			editor.commit();

			startService(new Intent(this, TrackRouteService.class));
		}

	}

	// --------------------------------------------------------------------------------------------------------------

	// ----------------------------------------------- FETCH DATA FROM SERVER
	// --------------------------------------

	// Uses AsyncTask to create a task away from the main UI thread.

	private class ServerHandlerAsyncTask extends
			AsyncTask<Void, Void, ArrayList<Reception>> {

		@Override
		protected ArrayList<Reception> doInBackground(Void... voidParams) {
			// voidParams is just holder for void
			// Downloading receptions...
			ArrayList<Reception> downloadedList = null;
			while (downloadedList == null) {
				if (isCancelled())
					return null;

				downloadedList = fetchData();
			}
			return downloadedList;
		}

		// onPostExecute sets the receptionsList to lists[0]
		@Override
		protected void onPostExecute(ArrayList<Reception> downloadedReceptions) {

			// use this array for making markers
			if (downloadedReceptions == null)// if in case
				downloadedReceptions = new ArrayList<Reception>();

			receptions = downloadedReceptions;

			dbHandler.deleteAllReceptions();
			dbHandler.addAllReception(receptions);

			progressBar.setVisibility(View.INVISIBLE);
			refreshData.setVisibility(View.VISIBLE);
			filterButton.setVisibility(View.VISIBLE);

			setDataonMap();
			updateFilterLists();

			// ---------------------- Save Last Update Time
			// ---------------------"
			String date = (String) (DateFormat.format("dd-MM-yyyy hh:mm:ss",
					new java.util.Date()));
			editor = sharedPreferences.edit();
			editor.putString("timestamp", date);
			editor.commit();

			lastUpdate.setText("Last Update : " + date);
		}

		private ArrayList<Reception> fetchData() {
			ArrayList<Reception> receptions = null;
			final String uri = "http://disco-idea-89406.appspot.com";

			URL url;
			HttpURLConnection con;
			InputStream inputStream = null;
			try {
				url = new URL(uri);
				con = (HttpURLConnection) url.openConnection();

				if (con.getResponseCode() == 200) {
					System.out.println("Response code is 200");
					inputStream = con.getInputStream();
					String response = readResponseFromStream(inputStream);
					if (isCancelled()) {
						inputStream.close();
						return null;
					}
					receptions = parseResponse(response);
				} else {
					// Incorrect response code
					// System.err.println("Response code: "+con.getResponseCode());
				}
				return receptions;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		private String readResponseFromStream(InputStream in) {
			BufferedReader reader;
			String res = "";
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					if (isCancelled())
						break;
					// System.out.println(line);
					res += line;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return res;
		}

		private ArrayList<Reception> parseResponse(String response) {
			ArrayList<Reception> receptions = new ArrayList<Reception>();

			try {
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonResponse = (JSONObject) jsonParser
						.parse(response);
				JSONArray jsonFeatures = (JSONArray) jsonResponse
						.get("receptions");

				Reception reception;
				JSONObject jsonFeature;
				for (Object obj : jsonFeatures) {
					jsonFeature = (JSONObject) obj;
					double longitude = Double.parseDouble((String) jsonFeature
							.get("Longitude"));
					double latitude = Double.parseDouble((String) jsonFeature
							.get("Latitude"));

					LatLng location = new LatLng(latitude, longitude);

					String operator = (String) jsonFeature
							.get("Service Provider");
					String service = (String) jsonFeature.get("Service Type");
					Integer strength = Integer.parseInt((String) jsonFeature
							.get("Signal Strength"));
					String make = (String) jsonFeature.get("Make");
					String model = (String) jsonFeature.get("Model");
					String timestamp = (String) jsonFeature.get("Timestamp");

					reception = new Reception(location, operator, service,
							strength, make, model, timestamp);
					// reception.display();
					receptions.add(reception);

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// if (receptions.size() != 0)
			return receptions;
			// else
			// return null;
		}

	}

	// -------------------------------------------------------------------------------------------------------------

	@Override
	public boolean onClusterClick(Cluster<Reception> cluster) {

		// Toast.makeText(MainActivity.this, "Work in Progress :",
		// Toast.LENGTH_SHORT).show();
		return false;

	}

	public class showMyReceptions extends
			AsyncTask<Void, Void, ArrayList<Reception>> {
		@Override
		protected ArrayList<Reception> doInBackground(Void... params) {

			System.out.println(msg + " My Receptions Count = "
					+ dbHandler.getPathReceptionsCount());
			if (dbHandler.getPathReceptionsCount() > 0) {
				ArrayList<Reception> receptionList = dbHandler
						.getPathReceptions(0);
				return receptionList;
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Reception> receptionList) {

			if (receptionList != null) {
				for (int i = 0; i < receptionList.size(); i++) {

					int color = 0;
					switch (receptionList.get(i).getSignalStrength()) {
					case 1: {
						color = Color.RED;

						break;
					}
					case 2: {
						color = Color.YELLOW;

						break;
					}
					case 3: {
						color = android.R.color.holo_orange_dark;

						break;
					}
					case 4: {
						color = Color.GREEN;
						break;
					}
					}
					Circle circle = googleMap.addCircle(new CircleOptions()
							.center(receptionList.get(i).getLocation())
							.radius(10).fillColor(color).strokeColor(color)
							.zIndex(100));

				}
			} else {
				Toast.makeText(getApplicationContext(),
						"No Saved Receptions Yet", Toast.LENGTH_LONG).show();
			}
		}
	}

	public boolean checkIfOnline(Context context) {
		// Connectivity Manager handles management of network connection
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo gets instance of current network state
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) // networkInfo
																// returns null
																// in airplane
																// mode
		{
			// Toast.makeText(context, "Device is online",
			// Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;

	}

	// public void showMyReceptions()
	// {
	// System.out.println(msg+"count = "+dbHandler.getPathReceptionsCount());
	//
	// if(dbHandler.getAllReceptionsCount()>0)
	// {
	// ArrayList<Reception> receptionList = dbHandler.getPathReceptions();
	//
	//
	// }
	// }

}
