package com.farm.global;

import android.app.Application;

import com.farm.db.DatabaseManager;
 
/*
 * This class is called when application is initialized. Created single instance of DatabaseManager.
 */
public class GlobalClass extends Application{
    
	
	//private DatabaseManager dbManager;
	
	@Override
    public void onCreate(){
		DatabaseManager.initializeInstance();
    }
 
}