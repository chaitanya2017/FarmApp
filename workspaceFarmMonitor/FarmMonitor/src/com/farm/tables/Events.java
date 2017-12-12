package com.farm.tables;

import org.json.JSONObject;
import com.farm.upload.ImageUpload;
import android.os.Environment;


/*
 *  Represents events table in database 
 */
public class Events {
	public int eventId;
	public String eventOccuredDate;
	public String eventOccuredTime;
	public String imagePath;
	public int monitorPointId;

	public Events(int eventId, String eventOccuredDate, String eventOccuredTime, String imagePath, int monitorPointId){
		this.eventId = eventId;
		this.eventOccuredDate = eventOccuredDate;
		this.eventOccuredTime = eventOccuredTime;
		this.imagePath = imagePath;
		this.monitorPointId = monitorPointId;
	}
	
	/*
	 * Generates Json string corresponding to events data
	 */
	public JSONObject getEventsJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			
			obj.put("eventid", ""+eventId);			
			obj.put("eventoccureddate", eventOccuredDate);
			obj.put("eventoccuredtime", eventOccuredTime);
			obj.put("imagepath", imagePath);
			obj.put("monitorpointid", ""+monitorPointId);
			
			if(!imagePath.equals(""))
			{
				String dir = Environment.getExternalStorageDirectory().getPath() + "/";
				obj.put("eventimage", ImageUpload.convertImage(dir + imagePath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
}


