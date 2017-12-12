package com.farm.monitor;

import java.util.ArrayList;
import java.util.List;


import com.farm.db.DatabaseHandler;
import com.farm.maps.MapUtility;
import com.farm.tables.Point;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Activity for creating boundaries
 */
public class CreatePerimeterActivity extends Activity implements OnClickListener, LocationListener {
	GoogleMap googleMap;
	TextView walkTextView, saveTextView, latLanTextView, totalPointsTextView, myLocationTextView;

	LocationManager lm;
	DatabaseHandler db;
	Intent intent;
	public Bundle b;
	double latitude, longitude, lmlatitude = 0, lmlongitude = 0;

	static boolean startFlag = false;
	static long perimeterId;
	static int placeType;
	long farmId, fieldId;
	public static List<Point> pointsList = null;
	static Point prev = null, cur = null;
	MapUtility mapUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_perimeter);
		initControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_perimeter, menu);
		return true;
	}

	/*
	 * creates activity controls
	 */
	void initControls() {
		try {
			googleMap = ((MapFragment) (getFragmentManager().findFragmentById(R.id.mapfragmentCreatePerimeterMap)))
					.getMap();
		} catch (Exception e) {
			Toast.makeText(this, "Google Map Error..." + e, Toast.LENGTH_LONG).show();
		}
		walkTextView = (TextView) findViewById(R.id.textViewCPWalk);
		saveTextView = (TextView) findViewById(R.id.textViewCPSave);
		latLanTextView = (TextView) findViewById(R.id.textViewCPLatLan);
		totalPointsTextView = (TextView) findViewById(R.id.textViewCPTotalPoints);
		myLocationTextView = (TextView) findViewById(R.id.textViewCPMyLocation);

		walkTextView.setOnClickListener(this);
		saveTextView.setOnClickListener(this);
		myLocationTextView.setOnClickListener(this);
	}

	void initialize() {
		getActionBar().hide();
		db = new DatabaseHandler();

		initializeMap();
		mapUtil = new MapUtility();
		intent = new Intent();
		b = getIntent().getExtras();
		intent.putExtras(b);
		placeType = b.getInt("placetype");
		if (placeType == 1)
			farmId = b.getLong("farmid");
		else
		{
			farmId = b.getLong("farmid");
			fieldId = b.getLong("fieldid");
		}
		if (pointsList == null)
			pointsList = new ArrayList<Point>();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
		mapUtil.drawAllDirectFarmsAndFields(googleMap, this);
		
		getMyLocation();
		animateLocation();
		animateMap();
	}

	/*
	 * Initializes google map for drawing boundaries
	 */
	void initializeMap() {
		try {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setCompassEnabled(true);

		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}	

	/*
	 * Handle onclick event
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(walkTextView)) {
			if (walkTextView.getText().toString().equals("Walk")) {
				if (startFlag == false) {
					savePerimeterEntry();
					startFlag = true;
					walkTextView.setText("Saving...");
				}
			}
		} else if (v.equals(saveTextView)) {
			close();
		} else if (v.equals(myLocationTextView)) {
			getMyLocation();
		}
	}

	/*
	 * Stored perimeter id in farm or field table
	 */
	public boolean savePerimeterEntry() {
		try {
			perimeterId = db.savePerimeter("");
			placeType = b.getInt("placetype");
			if (placeType == 1) {
				farmId = b.getLong("farmid");
				db.updateFarmPerimeter(farmId, perimeterId);
			} else if (placeType == 2) {
				fieldId = b.getLong("fieldid");
				db.updateFieldPerimeter(fieldId, perimeterId);
			}
			return true;
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
		return false;
	}

	/*
	 * Handle onlocation change event and get new location(latitiude and longitude)
	 */
	@Override
	public void onLocationChanged(Location location) {
		try {
			if (location == null)
				return;

			//if (!location.getProvider().equals(LocationManager.GPS_PROVIDER))
			//	return;
			//Float f= location.getAccuracy();
			//Toast.makeText(this, f.toString(), Toast.LENGTH_LONG).show();
			//location.dis
			//if(location.hasAccuracy())
	        {
				latitude = lmlatitude = location.getLatitude();
				longitude = lmlongitude = location.getLongitude();
	
				displayLocationText(latitude, longitude);// ,altitude);
				
				if (pointsList != null && startFlag == true) {
					if (pointsList.size() == 0) {
						addNewPoint(latitude, longitude);
					} else {
						if (isRequiredChagedLocation(latitude, longitude, 4) == true) {
							if (isMaxAccuracyLevelChecked(latitude, longitude, 2) == true) {
								drawPoint(latitude, longitude);
								savePoint(new Point(latitude, longitude));
								pointsList.add(prev);
								
							}
						}
					}
				}
	        }
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}

	}
	
	/*
	 * saves new point to database
	 * @Param latitude - latitude of new location
	 * @Param longitude - longitude of new location
	 */
	public void addNewPoint(double latitude, double longitude)
	{
		prev = new Point(latitude, longitude);
		pointsList.add(prev);
		drawPoint(latitude, longitude);
		savePoint(new Point(latitude, longitude));
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
	 * Draws boundary in blue or red color on google map
	 * @Param latitude - latitude of new location
	 * @Param longitude - longitude of new location 
	 */
	void drawPoint(double latitude, double longitude) {
		try {
			cur = new Point(latitude, longitude);
			if (pointsList.size() == 1)
				draw(latitude, longitude);
			else {
				if (placeType == 1)
					googleMap.addPolyline((new PolylineOptions())
							.add(new LatLng(prev.getLatitude(), prev.getLongitude()), new LatLng(latitude, longitude))
							.width(6).color(Color.RED).visible(true));
				else if (placeType == 2)
					googleMap.addPolyline((new PolylineOptions())
							.add(new LatLng(prev.getLatitude(), prev.getLongitude()), new LatLng(latitude, longitude))
							.width(6).color(Color.BLUE).visible(true));
			}

			prev = cur;
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Draws a circle for the first point on google map
	 * @Param latitude - latitude of new location
	 * @Param longitude - longitude of new location 
	 */
	void draw(double latitude, double longitude) {
		try {
			googleMap.addCircle(new CircleOptions().center(new LatLng(latitude, longitude)).radius(1)
					.strokeColor(Color.BLUE).fillColor(Color.RED));
			
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Checks whether location is changed for drawing boundaries
	 */
	boolean isRequiredChagedLocation(double latitude, double longitude, int accuracylevel) {
		double val;
		try {
			val = latitude * (Math.pow(10, accuracylevel));
			int preslat = (int) val;// % 10;
			val = longitude * (Math.pow(10, accuracylevel));
			int preslan = (int) val;// % 10;
			val = prev.latitude * (Math.pow(10, accuracylevel));
			int prevlat = (int) val;// % 10;
			val = prev.longitude * (Math.pow(10, accuracylevel));
			int prevlan = (int) val;// % 10;
			if (preslat != prevlat || preslan != prevlan)
				return true;
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
		return false;
	}

	/*
	 * Checks accuracy level for new location
	 */
	boolean isMaxAccuracyLevelChecked(double latitude, double longitude, int accuracylevel) {
		try {
			double preslat = latitude;
			double preslan = longitude;
			double prevlat = prev.latitude;
			double prevlan = prev.longitude;

			double difflat = Math.abs(preslat - prevlat);
			double difflan = Math.abs(preslan - prevlan);
			int lat = (int) (difflat * Math.pow(10, accuracylevel));
			int lan = (int) (difflan * Math.pow(10, accuracylevel));

			if (lat == 0 && lan == 0)
				return true;
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
		return false;
	}

	/*
	 * Displays number of location points on google maps
	 * @Param latitude - latitude of new location
	 * @Param longitude - longitude of new location 
	 */
	void displayLocationText(double latitude, double longitude) {
		try {
			latLanTextView.setText("Lat/Lan:" + latitude + "/" + longitude);
			totalPointsTextView.setText("Count:" + pointsList.size());
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Saves a new point to database
	 * @Param p - reference to point
	 */
	void savePoint(Point p) {
		try {
			long pointId = db.savePoint(p);
			db.savePerimeterPoint(perimeterId, pointId);
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Stop drawing boundaries
	 */
	void close() {
		try {
			pointsList = new ArrayList<Point>();
			startFlag = false;
			prev = cur = null;

			prev = cur = null;
			this.finish();

		} catch (Exception e) {
			//Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Gets current location
	 */
	public boolean getMyLocation() {
		try {
			if (latitude == 0 || longitude == 0) {
				boolean isGPSEnabled = false;
				boolean isNetworkEnabled = false;

				isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
				isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				Location location = null;
				if (!isGPSEnabled && !isNetworkEnabled) {
					// no network provider is enabled
				} else {
					if (isNetworkEnabled) {
						lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
						if (lm != null) {

							location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}

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
	 * Animates current location on google map
	 */
	private void animateLocation() {
		if (latitude == 0 || longitude == 0)
			return;
		LatLng latLng = new LatLng(latitude, longitude);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
		googleMap.animateCamera(cameraUpdate);
	}

	/*
	 * Zooms the map
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
