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
import com.farm.tables.Field;
import com.farm.tables.Point;
import com.farm.maps.MapUtility;

/*
 * This class is used for generating fields report
 */
public class FieldsReport {
	public List<Field> fields = null;
	public List<String> columnHeaders = null;
	public List<TextView> headersTextViewsList;
	public Map columnPairs;

	int columnCount = 0;
	DatabaseHandler db = null;
	Activity activity;
	int cellHeight = 50, cellWidth = 120, headerCellHeight = 40;
	int textSizeHeader = 16, textSizeRow = 15;

	public FieldsReport(Activity activity) {
		try {
			db = new DatabaseHandler();
			this.activity = activity;
		} catch (Exception e) {
		}
	}

	/*
	 * Returns all fields
	 */
	List<Field> getAllFields() {
		return fields;
	}

	/*
	 * gets all fields data from database
	 */
	public List<Field> getAllDataList() {
		fields = null;
		String selectQuery;
		Cursor cursor = null;
		try {
			fields = new ArrayList<Field>();
			selectQuery = "SELECT * FROM field";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			columnCount = cursor.getColumnCount();
			if (cursor.moveToFirst()) {
				do {

					List<Point> fieldPointsList = db
							.getAllFieldPointsList(cursor.getInt(cursor.getColumnIndex("fieldid")));
					double area = MapUtility.calcArea(fieldPointsList);
					double perimeter = MapUtility.calcPerimeter(fieldPointsList);
					fields.add(new Field(cursor.getInt(cursor.getColumnIndex("fieldid")),
							cursor.getString(cursor.getColumnIndex("fieldname")),
							cursor.getInt(cursor.getColumnIndex("perimeterid")), area, perimeter));
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return fields;
	}

	/*
	 * Creates header names for fields report
	 */
	public List<String> getAllDataHeaders() {
		columnHeaders = null;
		Cursor cursor = null;
		try {
			columnHeaders = new ArrayList<String>();
			String selectQuery = "SELECT fieldid as FieldId, fieldname as FieldName, perimeterid as PerieterId FROM field";
			cursor = DatabaseManager.getInstance().openDatabase().rawQuery(selectQuery, null);
			columnCount = cursor.getColumnCount();

			columnHeaders.add("Name");
			columnHeaders.add("Area (Acres)");
			columnHeaders.add("Perimeter (Kms)");

			columnPairs = new HashMap<String, String>();
			columnPairs.put("FieldName", "fieldname");
			columnPairs.put("Area", "area");
			columnPairs.put("Perimeter", "perimeter");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return columnHeaders;
	}

	/*
	 * creates header row for fields report
	 */
	public TableRow getHeaderRow() {
		try {
			TableRow dataRow = (TableRow) activity.findViewById(R.id.tableRowFieldsHeader);
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
	 * creates text view for fields report header
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
	 * creates a table row for a field record
	 */
	public TableRow getDataRow(int i) {
		TableRow dataRow = new TableRow(activity);
		try {
			dataRow.setBackgroundColor(activity.getResources().getColor(color.reportrowbackcolor));
			Field field = fields.get(i);
			TableRow.LayoutParams params = new TableRow.LayoutParams();
			dataRow.addView(getTextView("" + field.fieldName, i, cellWidth + 40), params);
			dataRow.addView(getTextView("" + new DecimalFormat("###.##").format(field.area), i, cellWidth), params);
			dataRow.addView(getTextView("" + new DecimalFormat("###.##").format(field.perimeter), i, cellWidth),
					params);

		} catch (Exception e) {
		}
		return dataRow;
	}

	/*
	 * creates textview  for fields row data
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
