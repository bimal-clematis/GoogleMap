package com.utkarsh.nearme;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.utkarsh.nearme.MyLocation.LocationResult;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;




public class TestYourLocation extends Activity {
	// GPS Location
	
	String addressString,latLongString;
	TextView yourlocation_txt;
	
	Location location;
	double lat,lng;
//	ProgressDialog pDialog;
//	protected LoadPlaces loadplaces;
	MyLocation mylocation;
//	LocationResult locationResult;
	String Provider;
	Geocoder gc;
	ProgressDialog dialog;
	float accuracy;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.your_location_main);
		yourlocation_txt = (TextView)findViewById(R.id.your_location_txt);
		
		
		gc = new Geocoder(getApplicationContext(), Locale.getDefault());
	
		result();
		
	}

	public void result(){
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(getApplicationContext(), locationResult);
	}
	
	LocationResult locationResult = new LocationResult(){
	    @Override
	    public void gotLocation(Location location){
	    	
	    	if(location!=null)
		    {
	    		
		        double lat = location.getLatitude();
		        double lng = location.getLongitude();
		        


		       latLongString = "Lat:" + lat + "\nLong:" + lng;
		       accuracy = location.getAccuracy();
		       
		        try
		        {
		        	
		            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
		            StringBuilder sb = new StringBuilder();
		            if (addresses.size() > 0) 
		            {
		              Address address = addresses.get(0);
		              
		              sb.append(address.getAddressLine(0)).append(",");
		              sb.append(address.getLocality()).append(",");
		              sb.append(address.getAdminArea()).append(",");
		              sb.append(address.getCountryName());

		            }
		         addressString = sb.toString();
		         yourlocation_txt.setText("Your current location :"+addressString);
		         
		         
		        } 
		        catch (IOException e)
		        {
		        	
		        }
		    } 
		    else 
		    {
//		    	addressString = "Couldn't find your current location..";
//		    	yourlocation_txt.setText(addressString);
		    	getlastlocation();
		    	
		    }

	    }
	};
	

	public void getlastlocation(){
		
		Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    
	   
	    LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    String provider = locationManager.getBestProvider(criteria, true);
	   

	    // Update the GUI with the last known 

	    
	    Location location = locationManager.getLastKnownLocation(provider);
	    if(location!=null)
	    {
    		
	        double lat = location.getLatitude();
	        double lng = location.getLongitude();
	        


	       latLongString = "Lat:" + lat + "\nLong:" + lng;
	       accuracy = location.getAccuracy();
	       
	        try
	        {
	        	
	            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
	            StringBuilder sb = new StringBuilder();
	            if (addresses.size() > 0) 
	            {
	              Address address = addresses.get(0);
	              
	              sb.append(address.getAddressLine(0)).append(",");
	              sb.append(address.getLocality()).append(",");
	              sb.append(address.getAdminArea()).append(",");
	              sb.append(address.getCountryName());

	            }
	         addressString = sb.toString();
	         yourlocation_txt.setText("Couldn't find your current location"+"\nYour last known location : "+addressString);
	        
	         
	        } 
	        catch (IOException e)
	        {

	        }
	    } 
	    else 
	    {
	    	addressString = "Couldn't find your location.Please try after some time..";
	    	yourlocation_txt.setText(addressString);
	    	
	    }

	}

}	