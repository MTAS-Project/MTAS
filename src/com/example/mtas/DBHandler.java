package com.example.mtas;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;


public class DBHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Receptions.db";
	private static final String All_Receptions = "All_Receptions";
	private static final String Path_Receptions = "Path_Receptions";
	private static final String P_key = "_ID";
	private static final String Make = "Make";
	private static final String Model = "Model";
	private static final String NetworkOperator = "Operator";
	private static final String SignalStrength = "Strength";
	private static final String ServiceName = "Service";
	private static final String Latitude ="Latitude";
	private static final String Longitude ="Longitude";
	private static final String SaveTime = "SaveTime";

	String msg ="MTAS ";
	public DBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		System.out.println(msg+"DBHandler: Constructor !");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		System.out.println(msg+"DBHandler: On Create !");
		createPathReceptionTable(db);
		createAllReceptionTable(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		db.execSQL("DROP TABLE IF EXISTS " + Path_Receptions);
		db.execSQL("DROP TABLE IF EXISTS " + All_Receptions);

		onCreate(db);
	}
	
	public void createPathReceptionTable(SQLiteDatabase db)
	{
		System.out.println(msg+"DBHandler: Create Reception Table !");
		
		String CREATE_RECEPTION_TABLE = "CREATE TABLE IF NOT EXISTS " + Path_Receptions
				+ "("
					+ P_key + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Make + " TEXT,"
					+ Model + " TEXT,"
					+ NetworkOperator + " TEXT,"
					+ SignalStrength + " INTEGER,"
					+ ServiceName + " INTEGER,"
					+ Latitude + "  DOUBLE," 
					+ Longitude+ " DOUBLE," 
					+ SaveTime+ " TEXT" 
				+ ")";
			
		    System.out.println(msg+CREATE_RECEPTION_TABLE);
			
			db.execSQL(CREATE_RECEPTION_TABLE);
	}
	
	public void createAllReceptionTable(SQLiteDatabase db)
	{
		System.out.println(msg+"DBHandler: Create All Reception Table !");
		
		String CREATE_RECEPTION_TABLE = "CREATE TABLE IF NOT EXISTS " + All_Receptions
				+ "("
					+ P_key + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Make + " TEXT,"
					+ Model + " TEXT,"
					+ NetworkOperator + " TEXT,"
					+ SignalStrength + " INTEGER,"
					+ ServiceName + " INTEGER,"
					+ Latitude + "  DOUBLE," 
					+ Longitude+ " DOUBLE," 
					+ SaveTime+ " TEXT" 
				+ ")";
			
			
			System.out.println(msg+CREATE_RECEPTION_TABLE);
			db.execSQL(CREATE_RECEPTION_TABLE);
	}
	
	public void addPathReception(Reception reception ) {
		
		System.out.println(msg+"DBHandler: Add Path Reception !");
		
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Make, reception.getMaker()); 
		values.put(Model, reception.getModel());
		values.put(NetworkOperator, reception.getNetworkOp()); 
		values.put(SignalStrength, reception.getSignalStrength());
		values.put(ServiceName, reception.getServiceType()); 
		values.put(Latitude, reception.getLocation().latitude); 
		values.put(Longitude, reception.getLocation().longitude); 
		
		
		values.put(SaveTime, reception.getTimeStamp());
		
		// Inserting Row
		System.out.println(msg+","+db.insert(Path_Receptions , null, values));
		db.close(); // Closing database connection
		
		System.out.print(msg);
		reception.display();
	}

	public void addAllReception(ArrayList<Reception> reception ) {
		
		System.out.println(msg+"DBHandler: Add All Reception !");
		
		SQLiteDatabase db = this.getWritableDatabase();

		for(int i=0;i<reception.size();i++)
		{
			ContentValues values = new ContentValues();
			values.put(Make, reception.get(i).getMaker()); 
			values.put(Model, reception.get(i).getModel());
			values.put(NetworkOperator, reception.get(i).getNetworkOp()); 
			values.put(SignalStrength, reception.get(i).getSignalStrength());
			values.put(ServiceName, reception.get(i).getServiceType()); 
			values.put(Latitude, reception.get(i).getLocation().latitude); 
			values.put(Longitude, reception.get(i).getLocation().longitude); 
			values.put(SaveTime, reception.get(i).getTimeStamp());
			
			// Inserting Row
			db.insert(All_Receptions, null, values);
//			System.out.print(msg+"---");reception.get(i).display();
		}
		db.close(); // Closing database connection
		
		System.out.print(msg);
//		reception.display();
	}
	
	public ArrayList<Reception> getPathReceptions(int rowNo) 
	{
		System.out.println(msg+"DBHandler: Get Path Receptions !");
		
		ArrayList<Reception> receptions = new ArrayList<Reception>();
		if(getPathReceptionsCount()==rowNo)
		{
			return receptions;
			
		}
		String selectQuery = "SELECT  * FROM " + Path_Receptions +" where "+ P_key +">"+rowNo;
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		// looping through all rows and adding to list
	//	if (cursor.moveToFirst()) {
			do {
				Reception reception = new Reception();
				reception.setMaker(cursor.getString(1));
				reception.setModel(cursor.getString(2));
				reception.setNetworkOp(cursor.getString(3));
				reception.setSignalStrength(Integer.parseInt(cursor.getString(4)));
				reception.setServiceType(cursor.getString(5));
				LatLng location = new LatLng(Double.parseDouble(cursor.getString(6)), Double.parseDouble(cursor.getString(7)));
				reception.setLocation(location);
				reception.setTimeStamp(cursor.getString(8));
				// Adding contact to list
				
				receptions.add(reception);
				
			} while (cursor.moveToNext());
		db.close();
		// return contact list
		return receptions;
	}
	
	public ArrayList<Reception> getAllReceptions() 
	{
		System.out.println(msg+"DBHandler: Get All Receptions !");
		
		ArrayList<Reception> receptions = new ArrayList<Reception>();
		
		String selectQuery = "SELECT  * FROM " + All_Receptions;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		// looping through all rows and adding to list
			do {
				Reception reception = new Reception();
				reception.setMaker(cursor.getString(1));
				reception.setModel(cursor.getString(2));
				reception.setNetworkOp(cursor.getString(3));
				reception.setSignalStrength(Integer.parseInt(cursor.getString(4)));
				reception.setServiceType(cursor.getString(5));
				LatLng location = new LatLng(Double.parseDouble(cursor.getString(6)), Double.parseDouble(cursor.getString(7)));
				reception.setLocation(location);
				reception.setTimeStamp(cursor.getString(8));
				// Adding contact to list
				
				receptions.add(reception);
				//System.out.print(msg);reception.display();
			} while (cursor.moveToNext());
		//}
		db.close();
		// return contact list
		return receptions;
	}
	
	public int getPathReceptionsCount() {
		String countQuery = "SELECT  * FROM " + Path_Receptions;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}
	
	public int getAllReceptionsCount() {
		String countQuery = "SELECT  * FROM " + All_Receptions;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}
	
	public void deleteAllReceptions()
	{
		String query = "DELETE FROM " + All_Receptions;
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(query);
	}

}

