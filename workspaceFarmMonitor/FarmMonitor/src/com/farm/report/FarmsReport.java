package com.farm.report;

import java.text.DecimalFormat;
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
import com.farm.tables.Farm;
import com.farm.tables.Point;
import com.farm.maps.MapUtility;

/*
 * This class is used for generating farms report
 */
public class FarmsReport {
	public List<Farm> farms = null;
	public List<String> columnHeaders = null;
	public List<TextView> headersTextViewsList;
	public Map columnPairs;

	int columnCount = 0;
	DatabaseHandler db = null;
	Activity activity;
	int cellHeight = 50, cellWidth = 120, headerCellHeight = 40;
	int textSizeHeader = 16, textSizeRow = 15;

	public FarmsReport(Activity activity) {
		try {
			db = new DatabaseHandler();
			this.activity = activity;
		} catch (Exception e) {
		}
	}

	/*
	 * Returns all farms
	 */
	List<Farm> getAllFarms() {
		return farms;
	}

	/*
	 * gets all farms data from database
	 */
	public List<Farm> getAllDataList() {
		farms = null;
		String selectQuery;
		Cursor cursor = null;
		try {
			farms = new ArrayList<Farm>();
			selectQuery = "SELECT * FROM farm";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			columnCount = cursor.getColumnCount();
			if (cursor.moveToFirst()) {
				do {

					List<Point> farmPointsList = db
							.getAllFarmPointsList(cursor.getInt(cursor.getColumnIndex("farmid")));
					double area = MapUtility.calcArea(farmPointsList);
					double perimeter = MapUtility.calcPerimeter(farmPointsList);
					farms.add(new Farm(cursor.getInt(cursor.getColumnIndex("farmid")),
							cursor.getString(cursor.getColumnIndex("farmname")),
							cursor.getInt(cursor.getColumnIndex("perimeterid")),
							cursor.getString(cursor.getColumnIndex("addressline1")),
							cursor.getString(cursor.getColumnIndex("addressline2")),
							cursor.getString(cursor.getColumnIndex("landmark")),
							cursor.getInt(cursor.getColumnIndex("visitorscount")), area, perimeter));
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return farms;
	}

	/*
	 * Creates header names for farms report
	 */
	public List<String> getAllDataHeaders() {
		columnHeaders = null;
		Cursor cursor = null;
		try {
			columnHeaders = new ArrayList<String>();
			String selectQuery = "SELECT farmid as Id, farmname as Name, addressline1 as Address1, addressline2 as Address2, landmark as LandMark FROM farm";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			columnCount = cursor.getColumnCount();
			columnHeaders.add("Name");
			columnHeaders.add("Area (Acres)");
			columnHeaders.add("Perimeter (Kms)");
			columnHeaders.add("Address1");
			columnHeaders.add("Address2");
			columnHeaders.add("LandMark");

			columnPairs = new HashMap<String, String>();
			columnPairs.put("Name", "farmname");
			columnPairs.put("Area", "area");
			columnPairs.put("Perimeter", "perimeter");
			columnPairs.put("Address1", "addressline1");
			columnPairs.put("Address2", "addressline2");
			columnPairs.put("LandMark", "landmark");
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return columnHeaders;
	}

	/*
	 * creates header row for farms report
	 */
	public TableRow getHeaderRow() {
		try {
			TableRow dataRow = (TableRow) activity.findViewById(R.id.tableRowFRHeader);
			headersTextViewsList = new ArrayList<TextView>();
			TableRow.LayoutParams params = new TableRow.LayoutParams();

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
	 * creates text view for farms report header
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
	 * creates a table row for a farm record
	 */
	public TableRow getDataRow(int i) {
		TableRow dataRow = new TableRow(activity);
		try {
			dataRow.setBackgroundColor(activity.getResources().getColor(color.reportrowbackcolor));
			Farm farm = farms.get(i);

			TableRow.LayoutParams params = new TableRow.LayoutParams();
			dataRow.addView(getTextView("" + farm.farmName, i, cellWidth + 40), params);
			dataRow.addView(getTextView("" + new DecimalFormat("###.##").format(farm.area), i, cellWidth), params);
			dataRow.addView(getTextView("" + new DecimalFormat("###.##").format(farm.perimeter), i, cellWidth),
					params);
			dataRow.addView(getTextView("" + farm.addressLine1, i, cellWidth), params);
			dataRow.addView(getTextView("" + farm.addressLine2, i, cellWidth), params);

			dataRow.addView(getTextView("" + farm.landMark, i, cellWidth), params);
		} catch (Exception e) {
		}
		return dataRow;
	}

	/*
	 * creates textview for farms row data
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
