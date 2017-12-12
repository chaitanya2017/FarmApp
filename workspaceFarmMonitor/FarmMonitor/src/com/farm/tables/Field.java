package com.farm.tables;

import org.json.JSONObject;

/*
 * Represents field table in database
 */
public class Field {
	public int fieldId;
	public String fieldName;
	public int farmId;
	public int perimeterId;
	public double area, perimeter;
	
	public Field(int fieldId, String fieldName, int farmId, int perimeterId)
	{
		this.fieldId = fieldId;
		this.fieldName = fieldName;
		this.farmId = farmId;
		this.perimeterId = perimeterId;
	}
	
	/*
	 * This constructor is called from fieldreport
	 */
	public Field(int fieldId, String fieldName, int perimeterId, double area, double perimeter){
		this.fieldId = fieldId;
		this.fieldName = fieldName;
		this.perimeterId = perimeterId;
		this.area = area;
		this.perimeter = perimeter;
	}
	
	/*
	 * Generates Json string corresponding to field data
	 */
	public JSONObject getFieldJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("fieldid", ""+fieldId);
			obj.put("fieldname", fieldName);
			obj.put("farmid", ""+farmId);
			obj.put("perimeterid", ""+perimeterId);
		} catch (Exception e) {
		}
		return obj;
	}
}
