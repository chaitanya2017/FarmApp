package com.farm.tables;

import org.json.JSONObject;

/*
 * Represents perimeter table in database
 */
public class Perimeter {
	public int perimeterId;
	public String perimeterName;
	public Perimeter(int perimeterId, String perimeterName){
		this.perimeterId = perimeterId;
		this.perimeterName = perimeterName;
	}

	/*
	 * Generates Json string corresponding to perimeter data
	 */
	public JSONObject getPerimeterJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("perimeterid", ""+perimeterId);
			obj.put("perimetername", perimeterName);
		} catch (Exception e) {
		}
		return obj;
	}
}