package com.example.mtas;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Build;
import android.provider.Settings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class WelcomeActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		rotateAnimation();
		
		
		
//		/** set time to splash out */
//		final int welcomeScreenDisplay = 5000;
//		/** create a thread to show splash up to splash time */
//		Thread welcomeThread = new Thread() 
//		{
//			int wait = 0;
//	
//			@Override
//			public void run() 
//			{
//				try 
//				{
//					super.run();
//					/**
//					* use while to get the splash time. Use sleep() to increase
//					* the wait variable for every 100L.
//					*/
//					while (wait < welcomeScreenDisplay) 
//					{
//						ImageView myImage = (ImageView) findViewById(R.id.searchIcon);
//						
//						TranslateAnimation animation = new TranslateAnimation(0.0f, 70.0f,0.0f, 0.0f);          
//						//  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
//						
//				        animation.setDuration(1000);  // animation duration
//				        animation.setRepeatCount(2);  // animation repeat count
//				        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
//				        
////						Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
////						
//						myImage.startAnimation(animation);
//						
//						wait += 100;
//						System.out.println("Wait = "+wait);
////						rotateAnimation();
//					}
//					
//				} 
//				catch (Exception e) 
//				{
//					System.out.println("EXc=" + e);
//				}
//				finally 
//				{
//					/**
//					* Called after splash times up. Do some action after splash
//					* times up. Here we moved to another main activity class
//					*/
//					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//					startActivity(intent);
//					finish();
//				}
//			}
//		};
//		welcomeThread.start();
	}

	private void rotateAnimation()
	{
		ImageView myImage = (ImageView) findViewById(R.id.searchIcon);
		
		TranslateAnimation animation = new TranslateAnimation(0.0f, 60.0f,0.0f, 0.0f);  
		//  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
		
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(1);  // animation repeat count
        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )

//		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
		
        
        animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();

			}
		});
		
        myImage.startAnimation(animation);
        
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.welcom, menu);
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
