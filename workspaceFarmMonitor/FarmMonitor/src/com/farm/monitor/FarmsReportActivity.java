package com.farm.monitor;

import java.util.ArrayList;
import java.util.List;

import com.farm.db.DatabaseHandler;
import com.farm.report.FarmsReport;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;

/*
 * Activity for farm report
 */
public class FarmsReportActivity extends Activity {
	public FarmsReport farmsReport = null;
	LinearLayout linearLayout = null;
	TableLayout tableLayout = null;
	Button okButton = null;
	PopupWindow popup = null;
	DatabaseHandler db;
	List<TableRow> rowsList = null;
	TableLayout dataTable = null;
	TableLayout headerTable = null;
	static boolean status = false;

	Bundle b;
	Intent intent;
	List<String> popupItems;
	ListView popupListView;
	int selectedPos;
	Dialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_farms_report);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.farms_report, menu);
		return true;
	}
	
	/*
	 * creates activity controls
	 */
	void initializeControls() {
		try {
			getActionBar().hide();
			db = new DatabaseHandler();
			rowsList = new ArrayList<TableRow>();
	
		} catch (Exception e) {
		}
	}

	/*
	 * Gets farms list from database and displays in a list
	 */
	void initialize() {
		try {
			db = new DatabaseHandler();
			intent = new Intent();
			b = getIntent().getExtras();

			rowsList = new ArrayList<TableRow>();
			farmsReport = new FarmsReport(this);
			farmsReport.getAllDataHeaders();
			headerTable = (TableLayout) findViewById(R.id.tableLayoutFRDataReport);
			dataTable = (TableLayout) findViewById(R.id.tableLayoutFRDataTable);
			farmsReport.getHeaderRow();
			farmsReport.getAllDataList();
			for (int i = 0; i < farmsReport.farms.size(); i++) {
				TableRow row = farmsReport.getDataRow(i);
				dataTable.addView(row);
				rowsList.add(row);
			}
		} catch (Exception e) {
		}
	}
}
