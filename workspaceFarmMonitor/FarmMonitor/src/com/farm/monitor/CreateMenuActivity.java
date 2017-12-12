package com.farm.monitor;

import com.farm.db.DatabaseHandler;
import com.farm.maps.MapUtility;
import com.farm.tables.Point;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Activity for menu
 */
public class CreateMenuActivity extends Activity
		implements OnClickListener, LocationListener, OnMarkerClickListener, OnTouchListener, OnMapClickListener {
	GoogleMap googleMap;

	TextView myLocationTextView, capturePhotoTextView, photosTextView, createFarmTextView, createFieldTextView,
			createMonitorPointTextView;
	Intent intent;
	Bundle b;
	DatabaseHandler db;
	TableLayout mapMenuTableLayout, mainMenuTableLayout;
	LocationManager lm;
	Marker curMarker = null;
	static int mapTypeFlag = 0;
	double latitude, longitude;
	static boolean firstAnimate = false, firstgps = false, animate = false;
	MapUtility mapUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_menu);
		initializeControls();
		initialize();
	}

	@Override
	public void onResume() {
		super.onResume();
		googleMap.clear();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_menu, menu);
		return true;
	}

	/*
	 * Creates activity controls
	 */
	void initializeControls() {
		try {
			googleMap = ((MapFragment) (getFragmentManager().findFragmentById(R.id.mapfragmentMapCreateMenu))).getMap();
		} catch (Exception e) {
			Toast.makeText(this, "Google Map Error..." + e, Toast.LENGTH_LONG).show();
		}

		myLocationTextView = (TextView) findViewById(R.id.textViewCreateMyLocation);
		capturePhotoTextView = (TextView) findViewById(R.id.textViewCreateCapturePhotos);
		photosTextView = (TextView) findViewById(R.id.textViewCreatePhotos);
		createFarmTextView = (TextView) findViewById(R.id.textViewCreateFarm);
		createFieldTextView = (TextView) findViewById(R.id.textViewCreateField);
		createMonitorPointTextView = (TextView) findViewById(R.id.textViewCreateMonitorPoint);

		myLocationTextView.setOnClickListener(this);
		capturePhotoTextView.setOnClickListener(this);
		photosTextView.setOnClickListener(this);
		createFarmTextView.setOnClickListener(this);
		createFieldTextView.setOnClickListener(this);
		createMonitorPointTextView.setOnClickListener(this);
	}

	void initialize() {
		getActionBar().hide();
		initializeMap();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

		googleMap.setOnMapClickListener(this);

		db = new DatabaseHandler();
		intent = new Intent();
		b = getIntent().getExtras();

		mapUtil = new MapUtility();
		mapUtil.drawAllDirectFarmsAndFields(googleMap, this);
		animateMap();

	}

	/*
	 * Initializes google map
	 */
	void initializeMap() {
		try {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setCompassEnabled(true);
			// googleMap.getUiSettings().setZoomControlsEnabled(true);
			// googleMap.setPadding(10, 10, 10, 10);
			googleMap.setOnMarkerClickListener(this);
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * on location changed listener 
	 * @Param location - new location 
	 */
	@Override
	public void onLocationChanged(Location location) {
		try {
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				// setCurLocation(latitude, longitude);
				if (firstAnimate == false) {
					firstAnimate = true;
					firstgps = true;
					// getMyLocation();
				}
			}
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();

		}

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	/*
	 * Handle button click view and start corresponding acitivty
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(myLocationTextView)) {
			getMyLocation();
		} else if (v.equals(capturePhotoTextView)) {
			intent.setClass(getApplicationContext(), CapturePhotoActivity.class);
			startActivity(intent);
		} else if (v.equals(photosTextView)) {

		} else if (v.equals(createFarmTextView)) {
			intent.setClass(getApplicationContext(), CreateFarmActivity.class);
			startActivity(intent);

		} else if (v.equals(createFieldTextView)) {
			intent.setClass(getApplicationContext(), CreateFieldActivity.class);
			startActivity(intent);
		} else if (v.equals(createMonitorPointTextView)) {
			intent.setClass(getApplicationContext(), CreateMonitorPointActivity.class);
			startActivity(intent);
		}

	}
	
	/*
	 * gets current location
	 */
	private void getMyLocation() {
		try {
			if (latitude == 0 || longitude == 0)
				return;
			LatLng latLng = new LatLng(latitude, longitude);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
			googleMap.animateCamera(cameraUpdate);
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * animates google map
	 */
	void animateMap() {
		try {
			if (mapUtil.animateLayoutsList.size() == 0) {
				return;
			}

			try {
				LatLngBounds.Builder b = new LatLngBounds.Builder();
				for (Point p : mapUtil.animateLayoutsList) {
					b.include(new LatLng(p.latitude, p.longitude));
				}

				LatLngBounds bounds = b.build();
				CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
						this.getResources().getDisplayMetrics().widthPixels,
						this.getResources().getDisplayMetrics().heightPixels, 50);
				googleMap.animateCamera(cu);
			} catch (Exception e) {
			}

		} catch (Exception e) {
		}
	}
}
