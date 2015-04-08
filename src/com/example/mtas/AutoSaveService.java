package com.example.mtas;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.Toast;
//import com.example.mtas.SaveCurrentReception.myCustomPhoneListener;

public class AutoSaveService extends Service {
    // private SQLiteDatabase db = openOrCreateDatabase();

	int signalstrength;
	
	class myCustomPhoneListener extends PhoneStateListener
    {
    	int signal = 0;
    	
    	@Override
    	public void onSignalStrengthsChanged(SignalStrength ss)
    	{
    		super.onSignalStrengthsChanged(ss);
    		System.out.println("Listener called  ---------------------------");
    		
    		signal =  ss.getGsmSignalStrength();
    		signalstrength = ss.getGsmSignalStrength();
    		System.out.println("Signal = "+signal);

//    		Toast.makeText(getApplicationContext(), "Go to Firstdroid!!! GSM Cinr = "
//    	            +ss.isGsm()+","+ss.getGsmSignalStrength()+","+ss.getCdmaDbm()+","+ss.getEvdoDbm(), Toast.LENGTH_SHORT).show();
    		
    	}
    	
    	public int getSignalStrength()
    	{
    		return signal;
    	}
    }
    
    private Timer timer = new Timer();
    private TimerTask tt = new TimerTask() {
        @Override
        public void run() {
            //here we have to get the data attributes from phone and save to db
            //System.out.println(";kjkjk;j;lkj;klj;kj;lkj;lkj");
//        	TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//        	myCustomPhoneListener myListener = new myCustomPhoneListener();
//        	
//        	LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        	Criteria criteria = new Criteria();
//        	
//        	String provider = locationManager.getBestProvider(criteria, true);
//        	System.out.println("Provider  = "+provider);
//        	android.location.Location location = locationManager.getLastKnownLocation(provider);        	
//        	
//        	String service = getServiceType(tManager);
//        	int time = (int) (System.currentTimeMillis());
//        	Timestamp tsTemp = new Timestamp(time);
//        	
//        	Reception reception = new Reception();
//        	
//        	reception.setNetworkOp(tManager.getNetworkOperatorName());
//        	reception.setServiceType(service);
//        	reception.setSignalStrength(signalstrength);
//        	reception.setMaker(Build.MANUFACTURER);
//        	reception.setModel(Build.MODEL);
//        	if(location!=null)
//        	{
//        		reception.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
//        	}
//        	else
//        	{
//        		reception.setLocation(null);
//        	}
//        	reception.setTimeStamp(tsTemp);
//        	
//        	DBHandler db = new DBHandler(getApplicationContext());
//        	db.addReception(reception);
//        	
//        	System.out.print("Service Reception:");
//        	reception.display();
        	
        	System.out.println("Service: AutoSaveService running !");
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	//Integer timeInterval = intent.getIntExtra("timeinterval",5000); //if nothing is passed in intent, 5000 default value will be returned
    	int timeInterval = 5000; //if nothing is passed in intent, 5000 default value will be returned
        String msg = "Saving Receptions Automatically @ Every "+timeInterval/1000+"sec";
    	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        timer.scheduleAtFixedRate(tt, 0, timeInterval);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        Toast.makeText(this, "AutoSave Disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

}
