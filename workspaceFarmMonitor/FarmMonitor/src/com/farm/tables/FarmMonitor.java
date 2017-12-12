package com.farm.tables;

import org.json.JSONObject;

/*
 * Represents farmmonitor table in database 
 */
public class FarmMonitor {
	public int farmId, monitorpointId;
	
	public FarmMonitor(int farmId, int monitorpointId){
		this.farmId = farmId;
		this.monitorpointId = monitorpointId;
	}
	
	/*
	 * Generates Json string corresponding to farmmonitor data
	 */
	public JSONObject getFarmmonitorJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("farmid", ""+farmId);
			obj.put("monitorpointid", ""+monitorpointId);
		} catch (Exception e) {
		}
		return obj;
	}
}
