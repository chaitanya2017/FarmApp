package com.farm.db;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.farm.tables.*;
import android.content.ContentValues;
import android.database.Cursor;


/*
 * This class handles database operations 
 */
public class DatabaseHandler {

	public DatabaseHandler() {

	}

	/*
	 * Saves a farm to database
	 * @Param farmName - Name of the farm
	 * @Param address1 - Address1 of the farm
	 * @Param address2 - Address2 of the farm
	 * @Param landMark - landmark of the farm
	 */
	public long saveFarm(String farmName, String address1, String address2, String landMark) {

		try {
			ContentValues values = new ContentValues();
			values.put("perimeterid", 0);
			values.put("farmname", farmName);
			values.put("addressline1", address1);
			values.put("addressline2", address2);
			values.put("landmark", landMark);
			values.put("uploadstatus", 0);
			values.put("visitorscount", 0);
			return DatabaseManager.getInstance().openDatabase().insert("farm", null, values);
		} catch (Exception e) {

		}

		return -1;
	}

	/*
	 * Gets all farms from database
	 */
	public List<Integer> getFarmIds() {
		List<Integer> farmIdsList = null;
		Cursor cursor = null;
		try {
			farmIdsList = new ArrayList<Integer>();
			String selectQuery = "SELECT farmid FROM farm";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					farmIdsList.add(cursor.getInt(cursor.getColumnIndex("farmid")));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return farmIdsList;
	}

	/*
	 * Gets all fields for a farm
	 * @Param farmId - farmId for which fields data will be retrieved
	 */
	public List<DataIdValue> getFieldsByFarm(Integer farmId) {
		List<DataIdValue> fields = null;
		Cursor cursor = null;
		try {
			fields = new ArrayList<DataIdValue>();
			String selectQuery = "SELECT * FROM field WHERE farmid=" + farmId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					fields.add(new DataIdValue(cursor.getInt(cursor.getColumnIndex("fieldid")),
							cursor.getString(cursor.getColumnIndex("fieldname"))));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return fields;
	}

	/*
	 * Saves a perimeter to database
	 * @Param perimeterName - Name of the perimeter
	 */
	public long savePerimeter(String perimeterName) {
		try {
			ContentValues values = new ContentValues();
			values.put("perimetername", perimeterName);
			values.put("uploadstatus", 0);
			return DatabaseManager.getInstance().openDatabase().insert("perimeter", null, values);
		} catch (Exception e) {
		}
		return -1;
	}

	/*
	 * Gets perimeter id of a farm
	 * @Param farmId - farm id for which perimeter id is retrieved
	 */
	public int getPerimeterIdFromFarm(int farmId) {
		int perimeterId = -1;
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT perimeterid FROM farm where farmid = " + farmId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				perimeterId = cursor.getInt(0);
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return perimeterId;
	}

	/*
	 * Gets perimeter id of a field
	 * @Param fieldId - field id for which perimeter id is retrieved
	 */
	public int getPerimeterIdFromField(int fieldId) {
		int perimeterId = -1;
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT perimeterid FROM field where fieldid=" + fieldId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				perimeterId = cursor.getInt(0);
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return perimeterId;
	}

	/*
	 * Gets point data from database
	 * @Param pointId - point id for which data is retrieved from database
	 */
	public Point getPoint(int pointId) {
		Point point = null;
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT * FROM point where pointid=" + pointId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
				double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
				point = new Point(latitude, longitude);
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return point;
	}

	/*
	 * Returns current date time
	 * 
	 */
	public String getCurrentDateTime() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date dt = new Date();
		String strValue = timeFormat.format(dt);
		return strValue;
	}

	/*
	 * Updates a farm perimeter id
	 * @Param farmId - farm id for which perimeter id is updated
	 * @Param perimeterId - perimeter id
	 */
	public long updateFarmPerimeter(long farmId, long perimeterId) {
		try {
			ContentValues values = new ContentValues();
			values.put("perimeterid", perimeterId);
			return DatabaseManager.getInstance().openDatabase().update("farm", values, "farmid = " + farmId, null);
		} catch (Exception e) {
		}
		return -1;
	}

	/*
	 * Updates a field perimeter id
	 * @Param farmId - farm id for which perimeter id is updated
	 * @Param perimeterId - perimeter id
	 */
	public void updateFieldPerimeter(long fieldId, long perimeterId) {
		try {
			ContentValues values = new ContentValues();
			values.put("perimeterid", perimeterId);
			DatabaseManager.getInstance().openDatabase().update("field", values, "fieldid = " + fieldId, null);
		} catch (Exception e) {
		}
	}

	/*
	 * Saves a point
	 * @Param p - a point
	 */
	public long savePoint(Point p) {
		try {
			ContentValues values = new ContentValues();
			values.put("latitude", p.getLatitude());
			values.put("longitude", p.getLongitude());
			values.put("uploadstatus", 0);
			return DatabaseManager.getInstance().openDatabase().insert("point", null, values);
		} catch (Exception e) {
		}
		return -1;
	}

	/*
	 * Saves perimeter point
	 * @Param perimeterId - perimeter id
	 * @Param pointId - point id
	 */
	public long savePerimeterPoint(long perimeterId, long pointId) {
		try {
			ContentValues values = new ContentValues();
			values.put("perimeterid", perimeterId);
			values.put("pointid", pointId);
			values.put("uploadstatus", 0);
			return DatabaseManager.getInstance().openDatabase().insert("perimeterpoint", null, values);
		} catch (Exception e) {
		}
		return -1;

	}

	/*
	 * Gets all farms
	 */
	public List<DataIdValue> getAllFarms() {
		List<DataIdValue> farms = null;
		Cursor cursor = null;
		try {
			farms = new ArrayList<DataIdValue>();
			String selectQuery = "SELECT * FROM farm";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					farms.add(new DataIdValue(cursor.getInt(0), cursor.getString(1)));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return farms;
	}

	/*
	 * Gets farmid of a farm
	 * @param farmName - name of the farm
	 */
	public Integer getFarmId(String farmName) {
		Integer id = -1;
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT farmid FROM farm WHERE farmname='" + farmName + "'";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				id = cursor.getInt(0);
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return id;
	}

	/*
	 * Saves field to database
	 * @param farmId - farm id
	 * @param perimeterId - perimeter id
	 * @param fieldName - name of the field
	 */
	public long saveField(int farmId, long perimeterId, String fieldName) {
		try {
			ContentValues values = new ContentValues();
			values.put("farmid", farmId);
			values.put("perimeterid", perimeterId);
			values.put("fieldname", fieldName);
			values.put("uploadstatus", 0);
			return DatabaseManager.getInstance().openDatabase().insert("field", null, values);
		} catch (Exception e) {
		}
		return -1;
	}

	/*
	 * gets visitor count of a farm
	 * @param farmId - farm id
	 */
	public Integer getFarmVisitorsCount(int farmId) {
		Integer visitorsCount = -1;
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT visitorscount FROM farm WHERE farmid = " + farmId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				visitorsCount = cursor.getInt(0);
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return visitorsCount;
	}

	/*
	 * adds visitor count to a farm
	 * @param farmId - farm id
	 * @Param visitorsCount - number of visitors that need to be added to the farm
	 */
	public void addVisitorsCount(int farmId, int visitorsCount) {
		try {
			ContentValues values = new ContentValues();
			values.put("visitorscount", visitorsCount);
			DatabaseManager.getInstance().openDatabase().update("farm", values, "farmid = " + farmId, null);
		} catch (Exception e) {
		}
	}

	/*
	 * saves monitor point
	 * @param monitorPointName - monitor point name
	 * @Param monitorPointLocation - monitor point location
	 */
	public long saveMonitorPoint(String monitorPointName, String monitorPointLocation) {
		try {
			ContentValues values = new ContentValues();
			values.put("monitorpointname", monitorPointName);
			values.put("monitorpointlocation", monitorPointLocation);
			values.put("uploadstatus", 0);
			return DatabaseManager.getInstance().openDatabase().insert("monitorpoint", null, values);
		} catch (Exception e) {
		}
		return -1;
	}

	/*
	 * saves farm monitor
	 * @param farmid - farm id
	 * @Param monitorPointId - monitor point id
	 */
	public long saveFarmMonitor(long farmId, long monitorPointId) {
		try {
			ContentValues values = new ContentValues();
			values.put("farmid", farmId);
			values.put("monitorpointid", monitorPointId);
			values.put("uploadstatus", 0);
			return DatabaseManager.getInstance().openDatabase().insert("farmmonitor", null, values);
		} catch (Exception e) {
		}
		return -1;
	}

	/*
	 * updates monitor point farm monitor
	 * @param monitorPointId - monitor point id
	 * @Param path - reference image path
	 * @param imagesPath - images path
	 */
	public void updateMonitorPoint(long monitorPointId, String path, String imagesPath) {
		try {
			ContentValues values = new ContentValues();
			values.put("referenceimagepath", path);
			values.put("imagespath", imagesPath);
			DatabaseManager.getInstance().openDatabase().update("monitorpoint", values,
					"monitorpointid = " + monitorPointId, null);
		} catch (Exception e) {
		}
	}

	/*
	 * gets monitor points of a farm
	 * @param farmId - farm id
	 */
	public List<DataIdValue> getMonitorPointsByFarmId(Integer farmId) {
		List<DataIdValue> monitorPoints = null;
		Cursor cursor = null;
		try {
			monitorPoints = new ArrayList<DataIdValue>();
			String selectQuery = "SELECT * FROM farmmonitor WHERE farmid=" + farmId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					int monitorPointId = cursor.getInt(cursor.getColumnIndex("monitorpointid"));
					monitorPoints.add(new DataIdValue(monitorPointId, getMonitorPointNameById(monitorPointId)));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return monitorPoints;
	}

	/*
	 * gets monitor point name
	 * @param monitorPointId - monitor point id
	 */
	public String getMonitorPointNameById(int monitorPointId) {
		String monitorPointName = null;
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT monitorpointname FROM monitorpoint WHERE monitorpointid = " + monitorPointId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				monitorPointName = cursor.getString(0);
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return monitorPointName;
	}

	/*
	 * gets monitor point ids of a farm
	 * @Param farmid - farm id
	 */
	public List<Integer> getAllMonitorPointIdsFromFarmMonitor(int farmId) {
		List<Integer> farmMonitorPointIdsList = null;
		Cursor cursor = null;
		try {
			farmMonitorPointIdsList = new ArrayList<Integer>();
			String selectQuery = "SELECT monitorpointid FROM farmmonitor where farmid=" + farmId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					farmMonitorPointIdsList.add(cursor.getInt(0));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return farmMonitorPointIdsList;
	}

	/*
	 * gets current time
	 */
	public String getCurrentTime() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Date dt = new Date();
		String strValue = timeFormat.format(dt);
		return strValue;
	}

	/*
	 * gets current date
	 */
	public String getCurrentDate() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = new Date();
		String strValue = timeFormat.format(dt);
		return strValue;
	}

	/*
	 * saves a new event
	 * @Param monitorPointId - monitor point id
	 * @Param eventImageName - image name
	 */
	public long saveEvent(int monitorPointId, String eventImageName) {
		try {
			ContentValues values = new ContentValues();
			values.put("monitorpointid", monitorPointId);
			values.put("eventoccureddate", getCurrentDate());
			values.put("eventoccuredtime", getCurrentTime());
			values.put("imagepath", eventImageName);
			values.put("uploadstatus", 0);
			return DatabaseManager.getInstance().openDatabase().insert("events", null, values);
		} catch (Exception e) {
		}
		return -1;
	}
	
	/*
	 * gets all farms
	 */
	public List<Farm> getFarms() {
		
		List<Farm> farms = null;
		String selectQuery = null;
		int farmId;
		try {
			farms = new ArrayList<Farm>();
			selectQuery = "SELECT * FROM farm where uploadstatus = 0";
			 
			Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					farms.add(new Farm(farmId = cursor.getInt(cursor.getColumnIndex("farmid")), 
							cursor.getString(cursor.getColumnIndex("farmname")), 
							cursor.getInt(cursor.getColumnIndex("perimeterid")), 
							cursor.getString(cursor.getColumnIndex("addressline1")),
							cursor.getString(cursor.getColumnIndex("addressline2")),
							cursor.getString(cursor.getColumnIndex("landmark")),
							cursor.getInt(cursor.getColumnIndex("visitorscount"))
							));
					
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return farms;
	}
	
	/*
	 * gets all fields
	 */
	public List<Field> getFields() {
		
		List<Field> fields = null;
		String selectQuery = null;
		int fieldId;
		try {
			fields = new ArrayList<Field>();
			selectQuery = "SELECT * FROM field where uploadstatus = 0";
			 
			Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					fields.add(new Field(fieldId = cursor.getInt(cursor.getColumnIndex("fieldid")), 
							cursor.getString(cursor.getColumnIndex("fieldname")), 
							cursor.getInt(cursor.getColumnIndex("farmid")), 
							cursor.getInt(cursor.getColumnIndex("perimeterid"))
							));
					
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return fields;
	}
	
	/*
	 * gets all events
	 */
	public List<Events> getEvents() {
		List<Events> events = null;
		String selectQuery=null;
		int eventId;
		try {
			events = new ArrayList<Events>();
			selectQuery = "SELECT eventid, eventoccureddate, eventoccuredtime, imagepath, monitorPointId FROM events where uploadstatus = 0";
			
			Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					events.add(new Events(eventId = cursor.getInt(0), 
							cursor.getString(1), 
							cursor.getString(2), 
							cursor.getString(3), 
							cursor.getInt(4) 
							));
					
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return events;
	}
	
	/*
	 * gets farm monitors
	 */
	public List<FarmMonitor> getFarmMonitors() {
		List<FarmMonitor> farmMonitor = null;
		String selectQuery = null;
		int monitorPointId;
		try {
			farmMonitor = new ArrayList<FarmMonitor>();
			selectQuery = "SELECT * FROM farmmonitor where uploadstatus = 0";
						
			Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					farmMonitor.add(new FarmMonitor(cursor.getInt(cursor.getColumnIndex("farmid")), 
							monitorPointId = cursor.getInt(cursor.getColumnIndex("monitorpointid"))));
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return farmMonitor;
	}
	
	/*
	 * gets all monitor points
	 */
	public List<MonitorPoint> getMonitorPoints() {
		List<MonitorPoint> monitorpoints = null;
		String selectQuery = null;
		try {
			monitorpoints = new ArrayList<MonitorPoint>();
			selectQuery = "SELECT monitorpointid, monitorpointname, referenceimagepath, imagespath, monitorpointLocation FROM monitorpoint where uploadstatus = 0";
						
			Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					monitorpoints.add(new MonitorPoint(
							cursor.getInt(0), 
							cursor.getString(1), 
							-1, 
							cursor.getString(2), 
							cursor.getString(3), 
							cursor.getString(4)));
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return monitorpoints;
	}
	
	/*
	 * gets all perimeters 
	 */
	public List<Perimeter> getPerimeters() {
		List<Perimeter> perimeters = null;
		String selectQuery = null;
		int perimeterId;
		try {
			perimeters = new ArrayList<Perimeter>();
			selectQuery = "SELECT * FROM perimeter where uploadstatus = 0";
			
			Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					perimeters.add(new Perimeter(perimeterId = cursor.getInt(cursor.getColumnIndex("perimeterid")), 
							cursor.getString(cursor.getColumnIndex("perimetername")) 
							));
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return perimeters;
	}
	
	/*
	 * gets all perimeter points
	 */
	public List<PerimeterPoint> getPerimeterPoints() {
		List<PerimeterPoint> perimeterPoints = null;
		String selectQuery = null;
		try {
			perimeterPoints = new ArrayList<PerimeterPoint>();
			selectQuery = "SELECT * FROM perimeterpoint where uploadstatus = 0";
			Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					perimeterPoints.add(new PerimeterPoint(cursor.getInt(cursor.getColumnIndex("perimeterid")), 
							 cursor.getInt(cursor.getColumnIndex("pointid"))));
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return perimeterPoints;
	}

	/*
	 * gets all points
	 */
	public List<Point> getPoints() {
		List<Point> points = null;
		String selectQuery = null;
		try {
			points = new ArrayList<Point>();
			selectQuery = "SELECT * FROM point where uploadstatus = 0";
			Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					points.add(new Point(cursor.getInt(cursor.getColumnIndex("pointid")), 
							cursor.getDouble(cursor.getColumnIndex("latitude")), 
							cursor.getDouble(cursor.getColumnIndex("longitude"))));
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return points;
	}
	
	/*
	 * gets all farm points
	 * @param farmId - farm id
	 */
	public List<Point> getAllFarmPointsList(int farmId) {
		List<Point> pointsList = null;
		try {
			int perimeterId = getPerimeterIdFromFarm(farmId);
			List<Integer> pointIdsList = getAllPerimeterPointIdsList(perimeterId);
			pointsList = getAllPointsList(pointIdsList);

		} catch (Exception e) {
	
		}
		return pointsList;
	}

	/*
	 * gets all field points
	 * @param fieldId - field id
	 */
	public List<Point> getAllFieldPointsList(int fieldId) {
		List<Point> pointsList = null;
		try {
			int perimeterId = getPerimeterIdFromField(fieldId);
			List<Integer> pointIdsList = getAllPerimeterPointIdsList(perimeterId);
			pointsList = getAllPointsList(pointIdsList);

		} catch (Exception e) {
	
		}
		return pointsList;
	}

	/*
	 * gets all perimeter points
	 * @param perimeterId - perimeter id
	 */
	public List<Integer> getAllPerimeterPointIdsList(int perimeterId) {
		List<Integer> layoutPointIdsList = null; // refer
		Cursor cursor = null;
		try {
			layoutPointIdsList = new ArrayList<Integer>();
			String selectQuery = "SELECT pointid FROM perimeterpoint where perimeterid=" + perimeterId;
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					layoutPointIdsList.add(cursor.getInt(0));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		}finally{
			cursor.close();
		}
		return layoutPointIdsList;
	}

	/*
	 * gets all points
	 * @param pointIdsList - point ids
	 */
	public List<Point> getAllPointsList(List<Integer> pointIdsList) {
		List<Point> pointsList = null;
		Point p;
		int pointId;
		try {
			pointsList = new ArrayList<Point>();
			Iterator<Integer> iterator = pointIdsList.iterator();
			while (iterator.hasNext()) {
				pointId = iterator.next();
				p = getPoint(pointId);
				if (p != null) {
					pointsList.add(p);
				}
			}

		} catch (Exception e) {
		}
		return pointsList;
	}
}