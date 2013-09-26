package com.utkarsh.nearme;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class HomePage extends ListActivity {
	// GPS Location
	GPSTracker gps;
	ArrayList<String> itemtitle = new ArrayList<String>();
	ArrayList<String> itemvalues = new ArrayList<String>();
	ArrayList<String> itemimage = new ArrayList<String>();
	TextView home_address,apptitle;
	String addressString,latLongString;
	ImageButton locinfo_btn;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.homepage_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.windowtitle);
		apptitle = (TextView) findViewById(R.id.app_title);
		apptitle.setText("NEAR ME");
		home_address = (TextView)findViewById(R.id.homepage_address_txt);
		locinfo_btn = (ImageButton) findViewById(R.id.locinfo_btn);
		ListView lv = (ListView) findViewById(android.R.id.list);
		lv.setCacheColorHint(Color.TRANSPARENT);
		String sample = "This is test code";
		locinfo_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent filterIntent = new Intent(getApplicationContext(),
						EmailDialog.class);

				startActivity(filterIntent);	

			}
		});

		bindListView() ;

	}

	public void bindListView() {
		itemtitle.add("Resturants & Cafes");
		itemtitle.add("Hospitals & Doctors");
		itemtitle.add("Schools & Colleges");
		itemtitle.add("Departmental Stores");
		itemtitle.add("Pharmacy");
		itemtitle.add("Shopping Malls");
		itemtitle.add("Banks");
		itemtitle.add("Bus Stands");
		itemtitle.add("Railway Stations");
		itemtitle.add("ATMs");		

		itemvalues.add("cafe|restaurant");
		itemvalues.add("hospital|doctor");
		itemvalues.add("school");
		itemvalues.add("department_store");
		itemvalues.add("pharmacy");
		itemvalues.add("shopping_mall");
		itemvalues.add("bank");
		itemvalues.add("bus_station");
		itemvalues.add("train_station");
		itemvalues.add("atm");


		setListAdapter(new IconicAdapter(this));
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {

		Intent filterIntent = new Intent(this.getApplicationContext(),
				RangeDialog.class);
		filterIntent.putExtra("types", itemvalues.get(position));
		filterIntent.putExtra("place", itemtitle.get(position));
		startActivity(filterIntent);
	}

	class IconicAdapter extends ArrayAdapter<String> {
		Activity context;

		IconicAdapter(Activity context) {
			super(context, R.layout.homepage_row, itemtitle);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(R.layout.homepage_row, null);
			}

			TextView label = (TextView) row
					.findViewById(R.id.title1_row);
			ImageView app_icon = (ImageView) row
					.findViewById(R.id.title1_img);

			String title = itemtitle.get(position);
			if(title.equalsIgnoreCase("Resturants & Cafes"))
				app_icon.setImageResource(R.drawable.restaurant_icon);

			if(title.equalsIgnoreCase("Hospitals & Doctors"))
				app_icon.setImageResource(R.drawable.med_icon);

			if(title.equalsIgnoreCase("Schools & Colleges"))
				app_icon.setImageResource(R.drawable.school_icon);

			if(title.equalsIgnoreCase("Departmental Stores"))
				app_icon.setImageResource(R.drawable.departmental_icon);

			if(title.equalsIgnoreCase("Pharmacy"))
				app_icon.setImageResource(R.drawable.pharmacy_icon);

			if(title.equalsIgnoreCase("Shopping Malls"))
				app_icon.setImageResource(R.drawable.shopping_icon);

			if(title.equalsIgnoreCase("Banks"))
				app_icon.setImageResource(R.drawable.bank_icon);

			if(title.equalsIgnoreCase("Bus Stands"))
				app_icon.setImageResource(R.drawable.bus_icon);

			if(title.equalsIgnoreCase("Railway Stations"))
				app_icon.setImageResource(R.drawable.train_icon);

			if(title.equalsIgnoreCase("ATMs"))
				app_icon.setImageResource(R.drawable.atm_icon);

			label.setText(Html.fromHtml(itemtitle.get(position)));

			return (row);
		}
	}
	public String myloc()
	{ 
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

			Geocoder gc = new Geocoder(this, Locale.getDefault());
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
				//	         home_address.setText("Your Location: "+addressString);
			} 
			catch (IOException e)
			{

			}
		} 
		else 
		{
			latLongString = "No location found";
			//	        home_address.setText(latLongString);
		}

		return addressString;
	}
}