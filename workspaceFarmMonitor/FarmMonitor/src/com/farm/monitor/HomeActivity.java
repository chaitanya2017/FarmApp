package com.farm.monitor;

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
import android.widget.TextView;
import android.widget.Toast;

import com.farm.db.DatabaseHandler;
import com.farm.maps.MapUtility;
import com.farm.tables.Point;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;


/*
 * Activity for home screen
 */
public class HomeActivity extends Activity
		implements OnClickListener, LocationListener, OnMarkerClickListener, OnTouchListener, OnMapClickListener {
	GoogleMap googleMap;

	TextView myLocationTextView, capturePhotoTextView, photosTextView, createTextView, reportsTextView, uploadTextView;
	Intent intent;
	Bundle b;
	DatabaseHandler db;
	MapUtility mapUtil = null;
	LocationManager lm;
	Marker curMarker = null;
	static int mapTypeFlag = 0;
	double latitude, longitude;
	static boolean firstAnimate = false, firstgps = false, animate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	/*
	 * creates activity controls
	 */
	void initializeControls() {
		try {
			googleMap = ((MapFragment) (getFragmentManager().findFragmentById(R.id.mapfragmentMapMainSelection)))
					.getMap();
		} catch (Exception e) {
			Toast.makeText(this, "Google Map Error..." + e, Toast.LENGTH_LONG).show();
		}

		myLocationTextView = (TextView) findViewById(R.id.textViewHMMyLocation);
		capturePhotoTextView = (TextView) findViewById(R.id.textViewHMCapturePhotos);
		photosTextView = (TextView) findViewById(R.id.textViewHMPhotos);
		createTextView = (TextView) findViewById(R.id.textViewHMCreate);
		reportsTextView = (TextView) findViewById(R.id.textViewHMReports);
		uploadTextView = (TextView) findViewById(R.id.textViewHMUpload);

		myLocationTextView.setOnClickListener(this);
		capturePhotoTextView.setOnClickListener(this);
		photosTextView.setOnClickListener(this);
		createTextView.setOnClickListener(this);
		reportsTextView.setOnClickListener(this);
		uploadTextView.setOnClickListener(this);
	}

	void initialize() {
		getActionBar().hide();
		initializeMap();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

		googleMap.setOnMapClickListener(this);

		db = new DatabaseHandler();
		intent = new Intent();
		b = getIntent().getExtras();
		mapUtil = new MapUtility();
		mapUtil.drawAllDirectFarmsAndFields(googleMap, this);
		animateMap();
	}

	@Override
	public void onResume() {
		super.onResume();
		googleMap.clear();
		initialize();
	}

	/*
	 * Initialized google map
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
	 * handle onlocation change event
	 */
	@Override
	public void onLocationChanged(Location location) {
		try {
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				if (firstAnimate == false) {
					firstAnimate = true;
					firstgps = true;
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
	 * Handle onclick event and start corresponding activity or 
	 * if mylocation get current location
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(myLocationTextView)) {
			getMyLocation();
			animateLocation();
		} else if (v.equals(createTextView)) {
			intent.setClass(getApplicationContext(), CreateMenuActivity.class);
			startActivity(intent);
		} else if (v.equals(reportsTextView)) {
			intent.setClass(getApplicationContext(), ReportsActivity.class);
			startActivity(intent);
		} else if (v.equals(capturePhotoTextView)) {
			intent.setClass(getApplicationContext(), CapturePhotoActivity.class);
			startActivity(intent);
		} else if (v.equals(uploadTextView)) {
			intent.setClass(getApplicationContext(), UploadDataActivity.class);
			startActivity(intent);
		}else if(v.equals(photosTextView)){
			intent.setClass(getApplicationContext(), DisplayImagesActivity.class);
			startActivity(intent);
		}
	}

	/*
	 * Gets current location using GPS or Network
	 */
	public boolean getMyLocation() {
		try {
			if (latitude == 0 || longitude == 0) {
				boolean isGPSEnabled = false;
				boolean isNetworkEnabled = false;

				isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
				//isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				Location location = null;
				if (!isGPSEnabled) {
					// no network provider is enabled
				} else {				

					if (isGPSEnabled) {
						if (location == null) {
							lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
							if (lm != null) {

								location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
								if (location != null) {
									latitude = location.getLatitude();
									longitude = location.getLongitude();
								}
							}
						}
					}
				}
			}

			if (latitude == 0 || longitude == 0)
				return false;

			return true;
		} catch (Exception e) {
			Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return false;
	}

	/*
	 * Animates current location
	 */
	private void animateLocation() {
		if (latitude == 0 || longitude == 0)
			return;
		LatLng latLng = new LatLng(latitude, longitude);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
		googleMap.animateCamera(cameraUpdate);
	}

	/*
	 * Animates google map
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
