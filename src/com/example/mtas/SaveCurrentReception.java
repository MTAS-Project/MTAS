package com.example.mtas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class SaveCurrentReception extends Activity {

    TextView recepTV;
    LocationManager lm;
    
    String provider;
    String service;
    int signalstrength;
    String maker;
    String model;
    Location location;
    TelephonyManager tManager;
//    myCustomPhoneListener myListener;
    

//    class myCustomPhoneListener extends PhoneStateListener
//    {
//    	int signal = 0;
//    	
//    	@Override
//    	public void onSignalStrengthsChanged(SignalStrength ss)
//    	{
//    		super.onSignalStrengthsChanged(ss);
//    		System.out.println("Listener called  ---------------------------");
//    		
//	    		signal =  ss.getGsmSignalStrength();
//	    		signalstrength = ss.getGsmSignalStrength();
//	    		System.out.println("Signal = "+signal);
////    		Toast.makeText(getApplicationContext(), "Go to Firstdroid!!! GSM Cinr = "
////    	            +ss.isGsm()+","+ss.getGsmSignalStrength()+","+ss.getCdmaDbm()+","+ss.getEvdoDbm(), Toast.LENGTH_SHORT).show();
//    		
//    	}
//    	
//    	public int getSignalStrength()
//    	{
//    		return signal;
//    	}
//    }
    
    LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location loc) {
			// TODO Auto-generated method stub
			 location = loc;
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_current_reception);

        Bundle extras = getIntent().getExtras();
        signalstrength = extras.getInt("strength");
        
        recepTV = (TextView) findViewById(R.id.receptionTextView);
        
        
//        myListener = new myCustomPhoneListener();
        // Get System TELEPHONY service reference
        tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        currentReception();
    }

    public void currentReception() 
    {
        recepTV.setText("");
        
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    	Criteria criteria = new Criteria();
    	
    	String provider = locationManager.getBestProvider(criteria, false);
    	
    	location = locationManager.getLastKnownLocation(provider);
    	
        
//        try
//        {
//        	tManager.listen(myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
//        }
//        catch(Exception e)
//        {
//        	e.printStackTrace();
//        }

        
    	service = getServiceType(tManager);
    	//signalstrength = myListener.getSignalStrength();
//    	CellInfoGsm cellInfo = (CellInfoGsm) tManager.getAllCellInfo().get(0);
//    	CellSignalStrengthGsm strength = cellInfo.getCellSignalStrength();
    	
    	provider = tManager.getNetworkOperatorName();
    	maker = Build.MANUFACTURER;
    	model = Build.MODEL;
        recepTV.append("Operator: "+tManager.getNetworkOperatorName()+"\n");
        recepTV.append("Service : "+service+"\n");
        recepTV.append("Strength: "+MyCustomPhoneListener.levelToString(signalstrength)+"\n");
        recepTV.append("Make    : "+Build.MANUFACTURER+"\n");
        recepTV.append("Model   : "+Build.MODEL+"\n");
        
//        GsmCellLocation cellLocation = (GsmCellLocation) tManager.getCellLocation();
//      
//        recepTV.append("CellID: "+cellLocation.getCid());
//        recepTV.append("LAC: "+cellLocation.getLac());
//        recepTV.append("MCC: "+tManager.getNetworkOperator().substring(0, 3));
//        recepTV.append("MNC: "+tManager.getNetworkOperator().substring(3));
        System.out.println(location);
        
        if(location!=null)
        {
        	recepTV.append("Location: "+location.getLatitude()+","+location.getLongitude());
        }
        else
        {
        	recepTV.append("Location: Not found");
        }

//         List<CellInfo> loc = tManager.getAllCellInfo();
//         recepTV.append(tManager.getNetworkOperatorName());
//         recepTV.append(tManager.getNetworkOperatorName());
        
//        try
//        {
//        	tManager.listen(myListener, PhoneStateListener.LISTEN_NONE);
//        }
//        catch(Exception e)
//        {
//        	e.printStackTrace();
//        }
    }

    public String getServiceType(TelephonyManager tManager)
    {
    	switch(tManager.getNetworkType())
    	{
	    	case 1:
	    	{
	    		return "G";			//GRPS
	    	}
	    	case 2:
	    	{
	    		return "E";			//EDGE
	    	}
	    	case 11:
	    	{
	    		return "2G";
	    	}
	    	case 3:
	    	{
	    		return "3G";
	    	}
	    	case 10:
	    	{
	    		return "H";
	    	}
	    	case 13:
	    	{
	    		return "4G";
	    	}
	    	case 15:
	    	{
	    		return "H+";
	    	}
    	}
    	return "No Network";
    }
    
    public void saveReception(View v) {

    	String serviceType = getServiceType(tManager);
    	
    	
    	if(location!=null)
    	{
	    	Reception reception = new Reception();
	    	
	    	reception.setNetworkOp(tManager.getNetworkOperatorName());
	    	reception.setServiceType(serviceType);
	    	reception.setSignalStrength(signalstrength);
	    	reception.setMaker(maker);
	    	reception.setModel(model);
	    	reception.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
	    	String date = (String) (DateFormat.format("yyyy-MM-dd hh:mm:ss.000", new java.util.Date()));
	    	reception.setTimeStamp(date);
	    	
	    	DBHandler db = new DBHandler(this);
	    	db.getWritableDatabase();
	    	db.addPathReception(reception);
	    	
	    	Toast.makeText(getApplicationContext(), "Reception saved successfully", Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
    	}
    }
    
    
}
