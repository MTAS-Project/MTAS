package com.example.mtas;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class UploadAsyncTask extends AsyncTask<Context, Void, Void>{

	private DBHandler dbHandler;
	SharedPreferences sPref;
	SharedPreferences.Editor editor;
	private String uploadAddress = "http://disco-idea-89406.appspot.com/upload";
	private Boolean readyToExecute=true;
	@Override
	protected Void doInBackground(Context... params) {
		readyToExecute=false;
		System.out.println("MTAS Intenet enable");
		
		Context context = params[0];
		
		dbHandler = new DBHandler(context);
		ArrayList<Reception> receptions = new ArrayList<>();
		
		int rowNo=0;
		sPref = context.getSharedPreferences("LastUpload", Context.MODE_PRIVATE);
		editor = sPref.edit();
		
		if(!sPref.contains("rowNo"))	// if row No is not saved --> first time
		{
			editor.putInt("rowNo", 0);
			editor.commit();
		}
		else							// get last uploaded record row No 
		{
			rowNo = sPref.getInt("rowNo", 0);
		}
		System.out.println("MTAS rowNo:"+rowNo);
		System.out.println("MTAS Intenet enable");
		
		receptions = dbHandler.getPathReceptions(rowNo);	// get receptions list from database
		//getting ready to open connections
		try {
			URL url = new URL(uploadAddress);
			HttpURLConnection conn;
			String urlParameters;
			DataOutputStream wr;
			int i;
			for(i=0;i<receptions.size();i++)
			{
				if(checkIfOnline(context)==false)				// if device gone offline
				{
					editor.putInt("rowNo", rowNo+i);		
					editor.commit();
					break;
				}
				//upload ith record
				conn = (HttpURLConnection) url.openConnection();
				//add reqeuest header
				conn.setRequestMethod("POST");
				urlParameters = receptions.get(i).toUrlParameters();
				//send post request
				conn.setDoOutput(true);
				wr = new DataOutputStream(conn.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
				int rc = conn.getResponseCode();
//				conn.disconnect();
				System.out.println("MTAS: Url parameters: "+receptions.get(i).toUrlParameters());
				System.out.println("MTAS: Response code:"+rc);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine,response = "";
				while((inputLine = in.readLine())!=null){
					response+=inputLine;
				}
				in.close();
				System.out.println("MTAS Response: "+response);
			}
			editor.putInt("rowNo", rowNo+i);		
			editor.commit();
			
			System.out.println("MTAS After for loop rowNo = "+sPref.getInt("rowNo", 0));
		} catch (MalformedURLException e) {
			 System.out.println("MalformedURLException in AutoUploadService");
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException in AutoUploadService");
//			e.printStackTrace();
		}

		return null;
	}
	protected void onPostExecute(Void... voids ){
		readyToExecute=true;
	}
	public void setNotReadyToExecute(){
		readyToExecute=false;
	}
	public boolean isReadyToExecute(){
		return readyToExecute;
	}
	public boolean checkIfOnline(Context context)
	{
		//Connectivity Manager handles management of network connection
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//NetworkInfo gets instance of current network state 
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if(networkInfo!=null && networkInfo.isConnected()) // networkInfo returns null in airplane mode
		{
			return true;
		}
		return false;
		
	}
	

}
