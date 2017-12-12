package com.farm.tables;

import org.json.JSONObject;

/*
 * Represents perimeterpoint table in database
 */
public class PerimeterPoint {
	public int perimeterId, pointId;

	public PerimeterPoint(int perimeterId, int pointId){
		this.perimeterId = perimeterId;
		this.pointId = pointId;
	}

	/*
	 * Generates Json string corresponding to perimeterpoint data
	 */
	public JSONObject getPerimeterPointJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("perimeterid", ""+perimeterId);
			obj.put("pointid", ""+pointId);

		} catch (Exception e) {
		}
		return obj;
	}
	
}
