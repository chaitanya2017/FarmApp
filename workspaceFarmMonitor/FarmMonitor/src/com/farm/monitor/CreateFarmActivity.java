package com.farm.monitor;

import com.farm.db.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/*
 * Activity for creating a new farm
 */
public class CreateFarmActivity extends Activity implements OnClickListener {
	Button saveFarmTextView;
	Intent intent;
	Bundle b;
	EditText farNameEditText, address1EditText, address2EditText, landmarkEditText;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_farm);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_farm, menu);
		return true;
	}

	/*
	 * creates activity controls
	 */
	void initializeControls() {
		saveFarmTextView = (Button) findViewById(R.id.buttonCreateFarmSaveFarm);
		farNameEditText = (EditText) findViewById(R.id.editTextCFFarmName);
		address1EditText = (EditText) findViewById(R.id.editTextCFAddress1);
		address2EditText = (EditText) findViewById(R.id.editTextCFAddress2);
		landmarkEditText = (EditText) findViewById(R.id.editTextCFLandMark);

		saveFarmTextView.setOnClickListener(this);
	}

	void initialize() {
		getActionBar().hide();
		db = new DatabaseHandler();
		intent = new Intent();
		// b = getIntent().getExtras();
	}

	/*
	 * gets text from controls and creates a new farm
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(saveFarmTextView)) {

			String farmName = farNameEditText.getEditableText().toString().trim();
			String address1 = address1EditText.getEditableText().toString().trim();
			String address2 = address2EditText.getEditableText().toString().trim();
			String landMark = landmarkEditText.getEditableText().toString().trim();

			long farmId = saveFarm(farmName, address1, address2, landMark);
			intent.putExtra("farmid", farmId);
			intent.putExtra("placetype", 1);
			intent.setClass(getApplicationContext(), CreatePerimeterActivity.class);
			startActivity(intent);
			this.finish();
		}

	}

	/*
	 * Saves farm information to database.
	 */
	public long saveFarm(String farmName, String address1, String address2, String landmark) {
		long farmId = db.saveFarm(farmName, address1, address2, landmark);
		return farmId;
	}
}
