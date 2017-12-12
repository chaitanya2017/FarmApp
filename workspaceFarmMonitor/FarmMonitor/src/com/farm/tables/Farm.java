package com.farm.tables;

import org.json.JSONObject; 

/*
 *  Represents farm table in database 
 */
public class Farm {
	public int farmId;
	public String farmName;
	public int perimeterId;
	public String addressLine1; 
	public String addressLine2;
	public String landMark;
	public int visitorsCount;
	public double area, perimeter;
	
	public Farm(int farmId, String farmName, int perimeterId, String addressLine1, String addressLine2, String landMark, int visitorsCount){
		this.farmId = farmId;
		this.farmName = farmName;
		this.perimeterId = perimeterId;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.landMark = landMark;
		this.visitorsCount = visitorsCount;			
	}
	
	/*
	 * This constructor is called from farmreport
	 */
	public Farm(int farmId, String farmName, int perimeterId, String addressLine1, String addressLine2, String landMark, int visitorsCount, double area, double perimeter){
		this.farmId = farmId;
		this.farmName = farmName;
		this.perimeterId = perimeterId;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.landMark = landMark;
		this.visitorsCount = visitorsCount;
		this.area = area;	
		this.perimeter = perimeter;	
	}

	/*
	 * Generates Json string corresponding to farm data
	 */
	public JSONObject getFarmJson(){
		JSONObject obj = null;
		try {

			obj = new JSONObject();
			obj.put("farmid", ""+farmId);
			obj.put("farmname", farmName);
			obj.put("perimeterid", ""+perimeterId);
			obj.put("addressline1", addressLine1);
			obj.put("addressline2", addressLine2);
			obj.put("landmark", landMark);
			obj.put("visitorscount", ""+visitorsCount);

		} catch (Exception e) {
			
		}

		return obj;
	}
}
