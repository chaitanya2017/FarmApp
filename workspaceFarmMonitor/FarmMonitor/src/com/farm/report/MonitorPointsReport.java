package com.farm.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.farm.monitor.R;
import com.farm.monitor.R.color;
import com.farm.monitor.R.dimen;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;
import com.farm.db.DatabaseHandler;
import com.farm.db.DatabaseManager;
import com.farm.tables.MonitorPoint;

/*
 * This class is used for generating monitor points report
 */
public class MonitorPointsReport {
	public List<MonitorPoint> monitorpoints = null;
	public List<String> columnHeaders = null;
	public List<TextView> headersTextViewsList;
	public Map columnPairs;

	int columnCount = 0;
	DatabaseHandler db = null;
	Activity activity;
	int cellHeight = 50, cellWidth = 120, headerCellHeight = 40;
	int textSizeHeader = 16, textSizeRow = 15;

	public MonitorPointsReport(Activity activity) {
		try {
			db = new DatabaseHandler();
			this.activity = activity;
		} catch (Exception e) {
		}
	}

	/*
	 * Returns all monitor points
	 */
	List<MonitorPoint> getAllMonitorPoints() {
		return monitorpoints;
	}

	/*
	 * gets all monitor points data from database
	 */
	public List<MonitorPoint> getAllDataList() {
		monitorpoints = null;
		String selectQuery;
		Cursor cursor = null;
		try {
			monitorpoints = new ArrayList<MonitorPoint>();
			selectQuery = "SELECT * FROM monitorpoint";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			columnCount = cursor.getColumnCount();
			if (cursor.moveToFirst()) {
				do {
					monitorpoints.add(new MonitorPoint(cursor.getInt(cursor.getColumnIndex("monitorpointid")),
							cursor.getString(cursor.getColumnIndex("monitorpointname")),
							cursor.getInt(cursor.getColumnIndex("pointid")),
							cursor.getString(cursor.getColumnIndex("referenceimagepath")),
							cursor.getString(cursor.getColumnIndex("imagespath")),
							cursor.getString(cursor.getColumnIndex("monitorpointlocation"))));
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return monitorpoints;
	}

	/*
	 * Creates header names for monitor point report
	 */
	public List<String> getAllDataHeaders() {
		columnHeaders = null;
		Cursor cursor = null;
		try {
			columnHeaders = new ArrayList<String>();
			String selectQuery = "SELECT monitorpointid as Id, monitorpointname as Name, monitorpointlocation as Location, pointid as pointid, referenceimagepath as Refimagepath ,imagespath as Imagespath FROM monitorpoint";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			columnCount = cursor.getColumnCount();
			
			columnHeaders.add("Name");
			columnHeaders.add("Location");
			columnHeaders.add("RefImagepath");
			columnHeaders.add("ImagePath");
			
			columnPairs = new HashMap<String, String>();
			columnPairs.put("Name", "monitorpointname");
			columnPairs.put("Location", "monitorpointlocation");
			columnPairs.put("Refimagepath", "referenceimagepath");
			columnPairs.put("imagespath", "Imagespath");
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return columnHeaders;
	}

	/*
	 * creates header row for monitor point report
	 */
	public TableRow getHeaderRow() {
		try {
			TableRow dataRow = (TableRow) activity.findViewById(R.id.tableRowMPHeader);
			headersTextViewsList = new ArrayList<TextView>();
			TableRow.LayoutParams params = new TableRow.LayoutParams();

			TableRow.LayoutParams params1 = new TableRow.LayoutParams();
			TextView t;
			for (int i = 0; i < columnHeaders.size(); i++) {
				if (columnHeaders.get(i).contains("Id"))
					t = getHeaderTextView(columnHeaders.get(i), cellWidth - 30);
				else if (columnHeaders.get(i).contains("Date") || columnHeaders.get(i).contains("Name"))
					t = getHeaderTextView(columnHeaders.get(i), cellWidth + 40);
				else
					t = getHeaderTextView(columnHeaders.get(i), cellWidth);
				dataRow.addView(t, params);
				headersTextViewsList.add(t);
			}
			return dataRow;
		} catch (Exception e) {
			return null;
		}

	}

	/*
	 * creates text view for monitor point report header
	 */
	public TextView getHeaderTextView(String text, int cellWidth) {
		TextView t = new TextView(activity);
		try {

			t.setText(text);
			t.setTextColor(activity.getResources().getColor(color.reportheadertextcolor));

			t.setBackgroundColor(activity.getResources().getColor(color.reportheaderbackcolor));
			t.setTextSize(activity.getResources().getDimension(dimen.reportheadertextsize));

			int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cellWidth,
					activity.getResources().getDisplayMetrics());
			t.setMaxWidth(px);
			t.setMinimumWidth(px);
			int he = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, headerCellHeight,
					activity.getResources().getDisplayMetrics());

			t.setMinimumHeight(he);
			t.setMaxHeight(he);

			int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, textSizeHeader,
					activity.getResources().getDisplayMetrics());
			t.setTextSize(textSize);
			t.setGravity(Gravity.CENTER);
			t.setSingleLine();
			t.setTypeface(null, Typeface.BOLD);
		} catch (Exception e) {
		}
		return t;
	}

	/*
	 * creates a table row for a monitorpoint record
	 */
	public TableRow getDataRow(int i) {
		TableRow dataRow = new TableRow(activity);
		try {
			dataRow.setBackgroundColor(activity.getResources().getColor(color.reportrowbackcolor));
			MonitorPoint monitorpoint = monitorpoints.get(i);

			TableRow.LayoutParams params = new TableRow.LayoutParams();
			dataRow.addView(getTextView("" + monitorpoint.monitorpointName, i, cellWidth + 40), params);
			dataRow.addView(getTextView("" + monitorpoint.monitorpointLocation, i, cellWidth), params);
			dataRow.addView(getTextView("" + monitorpoint.referenceImagePath, i, cellWidth), params);
			dataRow.addView(getTextView("" + monitorpoint.imagesPath, i, cellWidth), params);
		} catch (Exception e) {
		}
		return dataRow;
	}

	/*
	 * creates textview  for monitorpoint row data
	 */
	public TextView getTextView(String text, int i, int cellWidth) {
		TextView t = new TextView(activity);
		try {
			t.setText(text);
			t.setTextColor(activity.getResources().getColor(color.reportdatatextcolor));
			if (i % 2 == 0)
				t.setBackgroundColor(activity.getResources().getColor(color.reportdatabackcolor1));
			else
				t.setBackgroundColor(activity.getResources().getColor(color.reportdatabackcolor2));
			t.setTextSize(activity.getResources().getDimension(dimen.reportdatatextsize));

			int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cellWidth,
					activity.getResources().getDisplayMetrics());
			t.setMaxWidth(px);
			t.setMinimumWidth(px);
			int he = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cellHeight,
					activity.getResources().getDisplayMetrics());
			t.setMinimumHeight(he);
			t.setMaxHeight(he);

			int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, textSizeRow,
					activity.getResources().getDisplayMetrics());
			t.setTextSize(textSize);

			t.setGravity(Gravity.CENTER);
			t.setSingleLine();
		} catch (Exception e) {
		}
		return t;
	}

}
