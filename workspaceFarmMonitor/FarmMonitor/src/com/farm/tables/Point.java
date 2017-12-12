package com.farm.tables;

import org.json.JSONObject;

/*
 * Represents point table in database
 */
public class Point {
	public int pointId, userId, deviceId;
	public double latitude, longitude, altitude;

	public Point(int pointId, double latitude, double longitude) {
		this.pointId = pointId;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/*
	 * Generates Json string corresponding to point data
	 */
	public JSONObject getPointJsonString() {
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("pointid", "" + pointId);
			obj.put("latitude", "" + latitude);
			obj.put("longitude", "" + longitude);
		} catch (Exception e) {
		}
		return obj;
	}

	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}
}
