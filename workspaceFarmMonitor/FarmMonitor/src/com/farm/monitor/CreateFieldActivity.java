package com.farm.monitor;

import java.util.List;

import com.farm.db.DataIdValue;
import com.farm.db.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * Activity for creating a new field
 */
public class CreateFieldActivity extends Activity implements OnClickListener {
	Spinner farmSpinner;
	EditText fieldNameEditText;
	Button saveFieldButton;
	DatabaseHandler db;
	Intent intent;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_field);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_field, menu);
		return true;
	}

	/*
	 * creates activity controls
	 */
	void initializeControls() {
		farmSpinner = (Spinner) findViewById(R.id.spinnerCreateFieldFarmName);
		fieldNameEditText = (EditText) findViewById(R.id.editTextCreateFieldFieldName);
		saveFieldButton = (Button) findViewById(R.id.buttonCreateFieldSaveField);

		saveFieldButton.setOnClickListener(this);
	}

	void initialize() {
		getActionBar().hide();

		db = new DatabaseHandler();
		initSpinners();
		intent = new Intent();
	}

	private void initSpinners() {
		try {
			List<DataIdValue> farms = getFarms();
			ArrayAdapter<DataIdValue> dataAdapter = new ArrayAdapter<DataIdValue>(this, R.layout.spinnertext, farms);
			farmSpinner.setAdapter(dataAdapter);
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
			System.err.println(e);
		}
	}
	
	/*
	 * gets all farms 
	 */
	public List<DataIdValue> getFarms()
	{
		return db.getAllFarms();
	}
	
	/*
	 * gets text from controls and creates a new field
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(saveFieldButton)) {
			if (fieldNameEditText.getText().toString().equals("") == true) {
				Toast.makeText(this, "Enter Farm Name", Toast.LENGTH_LONG).show();
				return;
			}

			String farmName = farmSpinner.getSelectedItem().toString();
			String fieldName = fieldNameEditText.getText().toString().trim();
			saveField(farmName, fieldName);
			intent.setClass(getApplicationContext(), CreatePerimeterActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

	/*
	 * Saves field information to database
	 */
	public long saveField(String farmName, String fieldName) {
		try {
			int farmId = db.getFarmId(farmName);
			long fieldId = db.saveField(farmId, 0, fieldName);
			intent.putExtra("farmid", farmId);
			intent.putExtra("fieldid", fieldId);
			intent.putExtra("placetype", 2);
			return fieldId;
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
		return -1;
	}
}
