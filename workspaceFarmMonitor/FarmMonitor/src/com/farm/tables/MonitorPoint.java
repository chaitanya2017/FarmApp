package com.farm.tables;

import org.json.JSONObject;

import com.farm.upload.ImageUpload;

import android.os.Environment;

/*
 * Represents monitorpoint table in database
 */
public class MonitorPoint {
	public int monitorpointId;
	public String monitorpointName;
	public int pointId;
	public String referenceImagePath, imagesPath;
	public String  monitorpointLocation;
	
	public MonitorPoint(int monitorpointId,	String monitorpointName, int pointId, String referenceImagePath, String imagesPath, String monitorpointLocation){
		this.monitorpointId = monitorpointId;
		this.monitorpointName = monitorpointName;
		this.pointId = pointId;
		this.referenceImagePath = referenceImagePath;
		this.imagesPath = imagesPath;
		this.monitorpointLocation = monitorpointLocation;
	}

	/*
	 * Generates Json string corresponding to monitorpoint data
	 */
	public JSONObject getMonitorPointJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("monitorpointid", ""+monitorpointId);
			obj.put("monitorpointname", monitorpointName);
			obj.put("pointid", ""+pointId);
			obj.put("referenceimagepath", referenceImagePath);
			obj.put("imagespath", imagesPath);
			obj.put("monitorpointlocation", monitorpointLocation);
			
			if(!referenceImagePath.equals(""))
			{
				String dir = Environment.getExternalStorageDirectory().getPath() + "/"; 
				String img = ImageUpload.convertImage(dir + referenceImagePath);
				obj.put("referenceimage", img);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
}
