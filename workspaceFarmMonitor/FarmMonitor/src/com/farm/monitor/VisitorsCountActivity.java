package com.farm.monitor;

import com.farm.db.DataIdValue;
import com.farm.db.DatabaseHandler;

import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Activity for adding visitors count for a farm
 */
public class VisitorsCountActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	Spinner farmSpinner;
	EditText totalVisitorsEditText;
	Button addVisitorsButton;
	TextView totalVisitorsTextView;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visitors_count);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.visitors_count, menu);
		return true;
	}

	/*
	 * creates activity controls
	 */
	void initializeControls() {
		farmSpinner = (Spinner) findViewById(R.id.spinnerVCFarmName);
		totalVisitorsEditText = (EditText) findViewById(R.id.editTextVCVisitorsCount);
		addVisitorsButton = (Button) findViewById(R.id.buttonVCAddVisitors);
		totalVisitorsTextView = (TextView) findViewById(R.id.textViewVCTotalVisitors);

		addVisitorsButton.setOnClickListener(this);
		farmSpinner.setOnItemSelectedListener(this);
	}

	void initialize() {
		getActionBar().hide();
		db = new DatabaseHandler();
		initSpinners();
	}

	void initSpinners() {
		try {
			List<DataIdValue> layouts = db.getAllFarms();
			ArrayAdapter<DataIdValue> dataAdapter = new ArrayAdapter<DataIdValue>(this, R.layout.spinnertext, layouts);
			farmSpinner.setAdapter(dataAdapter);
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * handle on click and add new visitors count
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(addVisitorsButton)) {
			String selectedFarm = farmSpinner.getSelectedItem().toString();
			int visitorsCount = getVisitorCount(selectedFarm);
			visitorsCount += Integer.parseInt(totalVisitorsEditText.getText().toString().trim());
			addVisitors(selectedFarm, visitorsCount);
			totalVisitorsTextView.setText("The number of visitors to \'" + selectedFarm + "\' farm are " + visitorsCount);
		}
	}

	/*
	 * show number of visitors for a given farm
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
		String selectedFarm = null;
		if (parent.equals(farmSpinner)) {
			selectedFarm = farmSpinner.getSelectedItem().toString();
			int visitorsCount = getVisitorCount(selectedFarm);
			totalVisitorsTextView.setText("The number of visitors to \'" + selectedFarm + "\' farm are " + visitorsCount);
		}
	}

	/*
	 * Save visitors count do database
	 */
	public void addVisitors(String selectedFarm, int visitorsCount) {
		int farmId = db.getFarmId(selectedFarm);
		db.addVisitorsCount(farmId, visitorsCount);
	}

	/*
	 * get visitors count from database
	 */
	public int getVisitorCount(String selectedFarm) {
		int farmId = db.getFarmId(selectedFarm);
		int visitorsCount = db.getFarmVisitorsCount(farmId);
		return visitorsCount;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
