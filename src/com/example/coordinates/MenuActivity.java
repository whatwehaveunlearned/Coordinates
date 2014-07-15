package com.example.coordinates;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
	//This is needed to assign a variable from layout
	TextView textView;
	//We load the class we have created to manage the GPS
	// GPSTracker class
    GPSTracker gps;
    
    //Handler to and interval to get coordinates
  	final Handler h = new Handler();
  	final int delay = 10000; //milliseconds
  	
  	//Saving in a file
  	String FILENAME = "coordinates_file";
  	FileOutputStream FileOutputStream;
  	DataOutputStream DataOutputStream;
  	
  	
  //Create array to hold coordinate values
	ArrayList<Double> coordinates = new ArrayList<Double>();
	int index = 0; //initialize counter for array
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		
			//Handler to get actual Coordinates every x milliseconds
			h.postDelayed(new Runnable(){
			    @Override
				public void run(){
			        launchPosition();
			        h.postDelayed(this, delay);
			    }
			}, delay);
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		TextView t=(TextView)findViewById(R.id.textView1);
		switch(item.getItemId()) {
		case R.id.menu_item_get:
			launchPosition();
			return true;
			
		case R.id.menu_stop:
			h.removeCallbacksAndMessages(null);
			gps.stopUsingGPS();
			return true;
			
		case R.id.menu_get_all:
			t.setText(""+ coordinates);
			//
			return true;
		case R.id.menu_erase:
			Toast.makeText(getApplicationContext(), "Erase", Toast.LENGTH_LONG).show();
			t.setText("Hi");
			coordinates.clear();
			index = 0;
			return true;
		case R.id.menu_save:
			try {
				FileOutputStream fos = new FileOutputStream(FILENAME);
			     
			      /*
			       * To create DataOutputStream object from FileOutputStream use,
			       * DataOutputStream(OutputStream os) constructor.
			       *
			       */
			     
			       DataOutputStream dos = new DataOutputStream(fos);
			     
			       //double d = 165;
			     
			       /*
			        * To write a double value to a file, use
			        * void writeDouble(double d) method of Java DataOutputStream class.
			        *
			        * This method writes specified double to output stream as 8 bytes value.
			        * Please note that the double value is first converted to long using
			        * Double.doubleToLongBits method and then long is written to
			        * underlying output stream.
			        */
			       
			        dos.writeDouble(coordinates.get(0));
			        dos.writeDouble(coordinates.get(1));
			       
			        /*
			         * To close DataOutputStream use,
			         * void close() method.
			         *
			         */
			       
			         dos.close();
				} catch (Exception e) {
				  e.printStackTrace();
				}
		case R.id.menu_read:
			
	}
			
		return super.onOptionsItemSelected(item);
	}
	
	//Method used to grab the GPS position and show it in screen
	public void launchPosition(){
        // create class object
        gps = new GPSTracker(MenuActivity.this);

        // check if GPS enabled     
        if(gps.canGetLocation()){
             
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
             
            // \n is for new line
            coordinates.add(latitude);
            coordinates.add(longitude);
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + coordinates.get(index) + "\nLong: " + coordinates.get(index+1), Toast.LENGTH_LONG).show();
            index = index +1;
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	//This method is called when is a tapping
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
				//To show the menu openOptionsMenu() is called
				this.openOptionsMenu();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	
}
