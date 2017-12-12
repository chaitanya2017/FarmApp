package com.farm.db;

/*
 * This class is used for maintaining key value pairs
 */
public class DataIdValue {
	public DataIdValue(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }

    Integer key;
    String value;
}
