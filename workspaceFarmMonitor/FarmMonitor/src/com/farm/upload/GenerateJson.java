package com.farm.upload;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.farm.db.DatabaseHandler;
import com.farm.tables.*;

/*
 * This class generates JSON for each table
 */
public class GenerateJson {

	DatabaseHandler db;

	public GenerateJson() {
		db = new DatabaseHandler();
	}

	/*
	 * generates Json for all tables
	 */
	public String getAllJson() {
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject();
			JSONArray farmsJson = getAllFarmsJson();
			if (farmsJson != null) {
				jsonObj.put("farm", farmsJson);
			}

			JSONArray fieldsJson = getAllFieldsJson();
			if (farmsJson != null) {
				jsonObj.put("field", fieldsJson);
			}

			JSONArray farmMonitorJson = getAllFarmMonitorsJson();
			if (farmMonitorJson != null) {
				jsonObj.put("farmmonitor", farmMonitorJson);
			}

			JSONArray monitorPointsJson = getAllMonitorPointsJson();
			if (monitorPointsJson != null) {
				jsonObj.put("monitorpoint", monitorPointsJson);
			}

			JSONArray perimeterJson = getAllPerimeterJson();
			if (perimeterJson != null) {
				jsonObj.put("perimeter", perimeterJson);
			}

			JSONArray perimeterPointJson = getAllPerimeterPointJson();
			if (perimeterPointJson != null) {
				jsonObj.put("perimeterpoint", perimeterPointJson);
			}

			JSONArray pointsJson = getAllPointsJson();
			if (pointsJson != null) {
				jsonObj.put("point", pointsJson);
			}

		} catch (Exception e) {

		}
		return jsonObj.toString();
	}
	
	/*
	 * Creates Json for all images 
	 */
	public String getImagesJson() {
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject();
			JSONArray eventsJson = getAllEventsJson();
			if (eventsJson != null) {
				jsonObj.put("events", eventsJson);
			}

		} catch (Exception e) {

		}
		return jsonObj.toString();
	}

	/*
	 * Creates Json for all farms
	 */
	public JSONArray getAllFarmsJson() {

		JSONArray jsonArrayList = new JSONArray();
		List<Farm> farms = null;
		try {
			farms = db.getFarms();
			if (farms.size() == 0)
				return null;
			Iterator<Farm> iterator = farms.iterator();
			while (iterator.hasNext()) {
				Farm farm = iterator.next();
				jsonArrayList.put(farm.getFarmJson());
			}
		} catch (Exception e) {

		}
		return jsonArrayList;
	}

	/*
	 * Creates Json for all fields
	 */
	public JSONArray getAllFieldsJson() {

		JSONArray jsonArrayList = new JSONArray();
		List<Field> farms = null;
		try {
			farms = db.getFields();
			if (farms.size() == 0)
				return null;
			Iterator<Field> iterator = farms.iterator();
			while (iterator.hasNext()) {
				Field field = iterator.next();
				jsonArrayList.put(field.getFieldJson());
			}
		} catch (Exception e) {
		}
		return jsonArrayList;
	}

	/*
	 * Creates Json for all events
	 */
	public JSONArray getAllEventsJson() {
		JSONArray jsonArrayList = new JSONArray();
		List<Events> eventsList = null;

		try {
			eventsList = db.getEvents();
			if (eventsList.size() == 0)
				return null;
			Iterator<Events> iterator = eventsList.iterator();
			while (iterator.hasNext()) {
				Events events = iterator.next();
				jsonArrayList.put(events.getEventsJson());
			}
		} catch (Exception e) {
		}
		return jsonArrayList;
	}

	/*
	 * Creates Json for data in farmmonitor table
	 */
	public JSONArray getAllFarmMonitorsJson() {
		JSONArray jsonArrayList = new JSONArray();
		List<FarmMonitor> farmMonitorList = null;
		try {
			farmMonitorList = db.getFarmMonitors();
			if (farmMonitorList.size() == 0)
				return null;
			Iterator<FarmMonitor> iterator = farmMonitorList.iterator();
			while (iterator.hasNext()) {
				FarmMonitor farmMonitor = iterator.next();
				jsonArrayList.put(farmMonitor.getFarmmonitorJson());
			}
		} catch (Exception e) {

		}
		return jsonArrayList;
	}

	/*
	 * Creates Json for data in monitorpoint table
	 */
	public JSONArray getAllMonitorPointsJson() {
		List<MonitorPoint> monitorPointList = null;
		JSONArray jsonArrayList = new JSONArray();
		try {
			monitorPointList = db.getMonitorPoints();
			if (monitorPointList.size() == 0)
				return null;
			Iterator<MonitorPoint> iterator = monitorPointList.iterator();
			while (iterator.hasNext()) {
				MonitorPoint monitorPoint = iterator.next();
				jsonArrayList.put(monitorPoint.getMonitorPointJson());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonArrayList;
	}

	/*
	 * Creates Json for data in perimeter table
	 */
	public JSONArray getAllPerimeterJson() {
		List<Perimeter> perimeterList = null;
		JSONArray jsonArrayList = new JSONArray();
		try {
			perimeterList = db.getPerimeters();
			if (perimeterList.size() == 0)
				return null;
			Iterator<Perimeter> iterator = perimeterList.iterator();
			while (iterator.hasNext()) {
				Perimeter perimeter = iterator.next();
				jsonArrayList.put(perimeter.getPerimeterJson());
			}
		} catch (Exception e) {

		}
		return jsonArrayList;
	}

	/*
	 * Creates Json for data in perimeterpoint table
	 */
	public JSONArray getAllPerimeterPointJson() {
		List<PerimeterPoint> perimeterPointsList = null;
		JSONArray jsonArrayList = new JSONArray();
		try {
			perimeterPointsList = db.getPerimeterPoints();
			if (perimeterPointsList.size() == 0)
				return null;
			Iterator<PerimeterPoint> iterator = perimeterPointsList.iterator();
			while (iterator.hasNext()) {
				PerimeterPoint perimeterPoint = iterator.next();
				jsonArrayList.put(perimeterPoint.getPerimeterPointJson());
			}
		} catch (Exception e) {
		}
		return jsonArrayList;
	}

	/*
	 * Creates Json for data in point table
	 */
	public JSONArray getAllPointsJson() {
		List<Point> pointsList = null;
		JSONArray jsonArrayList = new JSONArray();
		try {
			pointsList = db.getPoints();
			if (pointsList.size() == 0)
				return null;
			Iterator<Point> iterator = pointsList.iterator();
			while (iterator.hasNext()) {
				Point point = iterator.next();
				jsonArrayList.put(point.getPointJsonString());
			}
		} catch (Exception e) {

		}
		return jsonArrayList;
	}
}
