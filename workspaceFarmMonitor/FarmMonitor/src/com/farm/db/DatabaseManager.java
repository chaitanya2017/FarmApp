package com.farm.db;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/*
 * This class creates single connection with sqllite database
 */
public class DatabaseManager {

	private int mOpenCounter;

	private static DatabaseManager instance;
	private SQLiteDatabase mDatabase;

	/*
	 * Initialize single instance of database
	 */
	public static synchronized void initializeInstance() {
		try {
			if (instance == null) {
				instance = new DatabaseManager();
			}
		} catch (Exception e) {
		}
	}

	/*
	 * Gets instance of the connected database
	 */
	public static synchronized DatabaseManager getInstance() {

		if (instance == null) {
			throw new IllegalStateException(DatabaseManager.class.getSimpleName()
					+ " is not initialized, call initializeInstance(..) method first.");
		}
		return instance;
	}

	/*
	 * Opens a connection with database
	 */
	public synchronized SQLiteDatabase openDatabase() {

		try {
			mOpenCounter++;
			if (mOpenCounter == 1) {
				// Opening new database
				String dir = Environment.getExternalStorageDirectory().getPath();
				File dbfile = new File(dir + "/farmmonitor.db");
				mDatabase = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
				
			}
		} catch (Exception e) {
		}
		return mDatabase;
	}

	/*
	 * Closes database connection
	 */
	public synchronized void closeDatabase() {
		mOpenCounter--;
		if (mOpenCounter == 0) {
			// Closing database
			mDatabase.close();

		}
	}

}