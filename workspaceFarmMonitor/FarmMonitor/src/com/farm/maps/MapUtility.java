package com.farm.maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;

import com.farm.db.DataIdValue;
import com.farm.db.DatabaseHandler;
import com.farm.db.DatabaseManager;
import com.farm.tables.Point;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

/*
 * This class handles google map functions
 */
public class MapUtility {
	GoogleMap googleMap;
	LocationManager lm;
	Marker curMarker = null;
	double latitude, longitude;
	static boolean firstAnimate = false, firstgps = false, animate = false;
	DatabaseHandler db = null;
	Activity activity = null;
	public List<Point> animateLayoutsList;

	public MapUtility() {
		db = new DatabaseHandler();
		animateLayoutsList = new ArrayList<Point>();
	}

	/*
	 * draw farm boundaries and boundaries of all fields
	 * @googleMap - referene to GoogleMap instance
	 * @Activity - activity on which boundaries will be drawn
	 */
	public void drawAllDirectFarmsAndFields(GoogleMap googleMap, Activity activity) {
		this.googleMap = googleMap;
		this.activity = activity;
		try {
			List<Integer> farmIdsList = db.getFarmIds();
			Iterator<Integer> iterator = farmIdsList.iterator();
			while (iterator.hasNext()) {
				int farmId = iterator.next();
				drawDirectPerimeter(farmId, 1);
				List<DataIdValue> fieldIdsList = db.getFieldsByFarm(farmId);
				Iterator<DataIdValue> farmIterator = fieldIdsList.iterator();
				while (farmIterator.hasNext()) {
					drawDirectPerimeter(farmIterator.next().getKey(), 2);
				}
			}
		} catch (Exception e) {

		}
	}

	/*
	 * draw farm boundaries and boundaries of all fields for a given farm id
	 * @Param googleMap - referene to GoogleMap instance
	 * @Param Activity - activity on which boundaries will be drawn
	 * @Param farmId - farm id 
	 */
	public void drawAllDirectFarmsAndFields(GoogleMap googleMap, Activity activity, int farmId) {
		this.googleMap = googleMap;
		this.activity = activity;
		try {
			List<Integer> farmIdsList = db.getFarmIds();
			Iterator<Integer> iterator = farmIdsList.iterator();
			while (iterator.hasNext()) {
				int farmId1 = iterator.next();
				if (farmId1 == farmId) {
					drawDirectPerimeter(farmId, 1);
					List<DataIdValue> fieldIdsList = db.getFieldsByFarm(farmId);
					Iterator<DataIdValue> farmIterator = fieldIdsList.iterator();
					while (farmIterator.hasNext()) {
						drawDirectPerimeter(farmIterator.next().getKey(), 2);
					}
				}
			}
		} catch (Exception e) {

		}
	}

	/*
	 * draw perimeter of a farm or field
	 * @Param id - farm id or field id
	 * @Param perimeterType - perimeter type, farm - 1, field -2 
	 */
	public void drawDirectPerimeter(int id, int perimeterType) {

		int perimeterId = 0;
		PolylineOptions options = null;
		if (perimeterType == 1) {
			perimeterId = db.getPerimeterIdFromFarm(id);
			options = new PolylineOptions().width(6).color(Color.RED).visible(true);
		} else if (perimeterType == 2) {
			perimeterId = db.getPerimeterIdFromField(id);
			options = new PolylineOptions().width(6).color(Color.BLUE).visible(true);
		}
		Cursor cursor = null;
		Point prev = null, pres = null, start = null;
		try {
			String selectQuery = "SELECT pointid FROM perimeterpoint where perimeterid=" + perimeterId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			int count = 0;
			if (cursor.moveToFirst()) {
				prev = start = db.getPoint(cursor.getInt(0));
				options.add(new LatLng(prev.getLatitude(), prev.getLongitude()));
				do {
					int pointId = cursor.getInt(0);
					if (pointId < 0)
						continue;
					pres = db.getPoint(pointId);
					if (perimeterType == 1) {
						options.add(new LatLng(pres.getLatitude(), pres.getLongitude()));
						if (count % 50 == 0)
							animateLayoutsList.add(new Point(pres.latitude, pres.longitude));
						count++;
					} else if (perimeterType == 2)
						options.add(new LatLng(pres.getLatitude(), pres.getLongitude()));
					prev = pres;
				} while (cursor.moveToNext());

				if (pres != null && start != null)
					if (perimeterType == 1)
						options.add(new LatLng(start.getLatitude(), start.getLongitude()));
					else if (perimeterType == 2)
						options.add(new LatLng(start.getLatitude(), start.getLongitude()));
				googleMap.addPolyline(options);
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
	}

	/*
	 * shows the map on google maps
	 */
	void animateMap() {
		try {
			if (animateLayoutsList.size() == 0) {
				return;
			}
			if (animate == false) {
				animate = true;
				try {
					LatLngBounds.Builder b = new LatLngBounds.Builder();
					for (Point p : animateLayoutsList) {
						b.include(new LatLng(p.latitude, p.longitude));
					}

					LatLngBounds bounds = b.build();
					CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
							activity.getResources().getDisplayMetrics().widthPixels,
							activity.getResources().getDisplayMetrics().heightPixels, 50);
					googleMap.animateCamera(cu);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
	}

	/*
	 * calculates area of given latitude and longitude points
	 * @Param - latLans - a list of latitude and longitude points
	 */
	public static double calcArea(List<Point> latLans) {
		double sum = 0;
		double prevcolat = 0;
		double prevaz = 0;
		double colat0 = 0;
		double az0 = 0;
		for (int i = 0; i < latLans.size(); i++) {
			double colat = 2 * Math.atan2(
					Math.sqrt(Math.pow(Math.sin(latLans.get(i).latitude * Math.PI / 180 / 2), 2)
							+ Math.cos(latLans.get(i).latitude * Math.PI / 180)
									* Math.pow(Math.sin(latLans.get(i).longitude * Math.PI / 180 / 2), 2)),
					Math.sqrt(1 - Math.pow(Math.sin(latLans.get(i).latitude * Math.PI / 180 / 2), 2)
							- Math.cos(latLans.get(i).latitude * Math.PI / 180)
									* Math.pow(Math.sin(latLans.get(i).longitude * Math.PI / 180 / 2), 2)));
			double az = 0;
			if (latLans.get(i).latitude >= 90) {
				az = 0;
			} else if (latLans.get(i).latitude <= -90) {
				az = Math.PI;
			} else {
				az = Math.atan2(
						Math.cos(latLans.get(i).latitude * Math.PI / 180)
								* Math.sin(latLans.get(i).longitude * Math.PI / 180),
						Math.sin(latLans.get(i).latitude * Math.PI / 180)) % (2 * Math.PI);
			}
			if (i == 0) {
				colat0 = colat;
				az0 = az;
			}
			if (i > 0 && i < latLans.size()) {
				sum = sum + (1 - Math.cos(prevcolat + (colat - prevcolat) / 2)) * Math.PI
						* ((Math.abs(az - prevaz) / Math.PI)
								- 2 * Math.ceil(((Math.abs(az - prevaz) / Math.PI) - 1) / 2))
						* Math.signum(az - prevaz);
			}
			prevcolat = colat;
			prevaz = az;
		}
		sum = sum + (1 - Math.cos(prevcolat + (colat0 - prevcolat) / 2)) * (az0 - prevaz);
		double area = 5.10072E14 * Math.min(Math.abs(sum) / 4 / Math.PI, 1 - Math.abs(sum) / 4 / Math.PI);
		double displayarea = (area) / (4046.85642);// converting meter sq to acres
		return displayarea;
	}

	/*
	 * calculates length of given latitude and longitude points
	 * @Param - latLans - a list of latitude and longitude points
	 */
	public static double calcPerimeter(List<Point> latLans) {
		double dist = 0d;
		double totaldist = 0d;
		try {
			for (int i = 0, j = 0; i < latLans.size() - 1; i++, j++) {
				
				double theta = latLans.get(j).longitude - latLans.get(j + 1).longitude;
				dist = Math.sin(deg2rad(latLans.get(i).latitude)) * Math.sin(deg2rad(latLans.get(i + 1).latitude))
						+ Math.cos(deg2rad(latLans.get(i).latitude)) * Math.cos(deg2rad(latLans.get(i + 1).latitude))
								* Math.cos(deg2rad(theta));
				dist = Math.acos(dist);
				dist = rad2deg(dist);
				dist = dist * 60 * 1.1515;// getting distance in Miles
				dist = dist * 1.609344;// getting distance in Kilometers
				totaldist = totaldist + dist;
			}
			
			double theta = latLans.get(latLans.size() - 1).longitude - latLans.get(0).longitude;
			dist = Math.sin(deg2rad(latLans.get(latLans.size() - 1).latitude))
					* Math.sin(deg2rad(latLans.get(0).latitude))
					+ Math.cos(deg2rad(latLans.get(latLans.size() - 1).latitude))
							* Math.cos(deg2rad(latLans.get(0).latitude)) * Math.cos(deg2rad(theta));
			dist = Math.acos(dist);
			dist = rad2deg(dist);
			dist = dist * 60 * 1.1515;// getting distance in Miles
			dist = dist * 1.609344;// getting distance in Kilometers
			double displayperimeter = totaldist + dist;
			System.out.println("parameter is : " + displayperimeter);

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return totaldist;
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}
