package com.utkarsh.nearme;


import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



public class MainActivity extends ListActivity {

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager1 alert = new AlertDialogManager1();

	// Google Places
	GooglePlaces googlePlaces;

	// Places List
	PlacesList nearPlaces;

	// GPS Location
	GPSTracker gps;
	
	// Place Details
		PlaceDetails placeDetails;

	// Button
	ImageButton Mapbtn;

	// Progress dialog
//	ProgressDialog pDialog;
	
	MyProgressDialog pDialog;
	
	// Places Listview
	ListView lv;
	
	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	
	ArrayList<String> referenceList = new ArrayList<String>();
	ArrayList<String> nameList = new ArrayList<String>();
	ArrayList<String> distList = new ArrayList<String>();
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	String types,search_place,radius_str;
	Context mcontext;
	String internetdata;
	 double lat;
	 double lng,radius;
	 String latLongString,addressString;
	 TextView apptitle,activity_title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.windowtitle);
		apptitle = (TextView) findViewById(R.id.app_title);
		activity_title = (TextView) findViewById(R.id.main_activity_title);
		Bundle b = getIntent().getExtras();
		types = b.getString("types");
		search_place = b.getString("place");
		radius_str = b.getString("radius_str");
		radius = Double.parseDouble(radius_str);
		radius = radius*1000;
		apptitle.setText("NEAR ME");
		activity_title.setText(search_place.toUpperCase());
		cd = new ConnectionDetector(getApplicationContext());
		ListView lv = (ListView) findViewById(android.R.id.list);
		lv.setCacheColorHint(Color.TRANSPARENT);
		
		
		  /*  StrictMode.ThreadPolicy policy = 
		    new StrictMode.ThreadPolicy.Builder().permitAll().build();      
		        StrictMode.setThreadPolicy(policy);*/
		 
		
		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog1(MainActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
	
		
		// creating GPS Class object
		gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			
			//lat = gps.getLatitude();
			
			//lng = gps.getLongitude();

			
		} else {
			// Can't get user's current location
			alert.showAlertDialog1(MainActivity.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			// stop executing code by return
			return;
		}

		// Getting listview
//		lv = (ListView) findViewById(R.id.list);
		
		// button show on map
		Mapbtn = (ImageButton) findViewById(R.id.map_btn);

		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		
		new LoadPlaces().execute();

		/** Button click event for shown on map */
		Mapbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						PlacesMapActivity.class);
				// Sending user current geo location
				i.putExtra("user_latitude", Double.toString(lat));
				i.putExtra("user_longitude", Double.toString(lng));
				
				// passing near places to map activity
				i.putExtra("near_places", nearPlaces);
				// staring activity
				startActivity(i);
			}
		});
		

	}
	

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
    	// getting values from selected ListItem
//      String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
      String reference = referenceList.get(position);
      
      // Starting new intent
      Intent in = new Intent(getApplicationContext(),
              SinglePlaceActivity.class);
      
      // Sending place refrence id to single place activity
      // place refrence id used to get "Place full details"
      in.putExtra(KEY_REFERENCE, reference);
      startActivity(in);
	}
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new MyProgressDialog(MainActivity.this);
			pDialog.setMessage(Html.fromHtml("<b>Searching</b><br/>Nearby "+search_place+".."));
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			
			googlePlaces = new GooglePlaces();
			
			try {


				lat = gps.getLatitude();
				
				lng = gps.getLongitude();
				nearPlaces = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * and show the data in UI
		 * Always use runOnUiThread(new Runnable()) to update UI from background
		 * thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = nearPlaces.status;
					
					// Check for all possible status
					if(status.equals("OK")){
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();
								
								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map.put(KEY_REFERENCE, p.reference);
								referenceList.add(p.reference);
								// Place name
								map.put(KEY_NAME, p.name);
								nameList.add(p.name);
								
								try {
									googlePlaces = new GooglePlaces();
									placeDetails = googlePlaces.getPlaceDetails(p.reference);
									if (placeDetails.result != null) {
										double latitude = placeDetails.result.geometry.location.lat;
										double longitude = placeDetails.result.geometry.location.lng;
										
										float [] dist = new float[1];
									    Location.distanceBetween(lat, lng, latitude, longitude, dist);
									    dist[0] = dist[0]*0.000621371192f;
									    float f_dist = Math.round(dist[0]*10)/10.0f;
									    
										String dist_str = String.valueOf(f_dist);
										
										distList.add(dist_str);
										
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								// adding HashMap to ArrayList
								placesListItems.add(map);
							}
							 

							bindList();
						}
					}
					else if(status.equals("ZERO_RESULTS")){
						// Zero results found
//						String place_search = search_place.toLowerCase();
						alert.showAlertDialog1(MainActivity.this, "Near Places",
								"Sorry no "+search_place.toLowerCase()+ " found within "+String.valueOf(radius/1000)+"Km of your range..",
								false);
						
					}
					else if(status.equals("UNKNOWN_ERROR"))
					{
						alert.showAlertDialog1(MainActivity.this, "Places Error",
								"Sorry server is too busy.Please try after some time.",
								false);
						
					}
					else if(status.equals("OVER_QUERY_LIMIT"))
					{
						alert.showAlertDialog1(MainActivity.this, "Places Error",
								"Sorry server is too busy.Please try after some time.",
								false);
						
					}
					else if(status.equals("REQUEST_DENIED"))
					{
						alert.showAlertDialog1(MainActivity.this, "Places Error",
								"Sorry your request is denied",
								false);
						
					}
					else if(status.equals("INVALID_REQUEST"))
					{
						alert.showAlertDialog1(MainActivity.this, "Places Error",
								"Invalid Request",
								false);
						
					}
					else
					{
						alert.showAlertDialog1(MainActivity.this, "Places Error",
								"Sorry server is too busy.Please try after some time.",
								false);
						
					}
				}
			});
			pDialog.dismiss();
		}

	}
public void bindList(){
	setListAdapter(new IconicAdapter(this));
}
	public class AlertDialogManager1 {
		/**
		 * Function to display simple Alert Dialog
		 * @param context - application context
		 * @param title - alert dialog title
		 * @param message - alert message
		 * @param status - success/failure (used to set icon)
		 * 				 - pass null if you don't want icon
		 * */
		Context mcontext;
		MainActivity mainactivity;
		public void showAlertDialog1(final Context context, String title, String message,
				Boolean status) {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();

			// Setting Dialog Title
			alertDialog.setTitle(title);

			// Setting Dialog Message
			alertDialog.setMessage(message);

			if(status != null)
				// Setting alert dialog icon
				alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					finish();
					
				}
			});

			// Showing Alert Message
			alertDialog.show();
		}
	}
	class IconicAdapter extends ArrayAdapter<String> {
		Activity context;

		IconicAdapter(Activity context) {
			super(context, R.layout.main_activity_row, nameList);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(R.layout.main_activity_row, null);
			}
			TextView Name = (TextView) row
					.findViewById(R.id.name);
			TextView Distance = (TextView) row
					.findViewById(R.id.distance);
			Name.setText(nameList.get(position));
			String reference = referenceList.get(position);

			Distance.setText("Approx "+distList.get(position)+" miles away");
			return (row);
		}
	}
	
	

}
