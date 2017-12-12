package com.farm.monitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import com.farm.db.DataIdValue;
import com.farm.db.DatabaseHandler;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * Activity for creating a monitor point
 */
public class CreateMonitorPointActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	Spinner farmNameSpinner;
	EditText monitorPointNameEditText, monitorPointLocationEditText;
	Button takeReferencePictureButton, saveMonitorPointButton;
	DatabaseHandler db;
	Intent intent;
	private static final int CAMERA_REQUEST = 1888;
	Bitmap referenceImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_monitor_point);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_monitor_point, menu);
		return true;
	}

	/*
	 * creates activity controls
	 */
	void initializeControls() {
		farmNameSpinner = (Spinner) findViewById(R.id.spinnerCMPFarmName);
		monitorPointNameEditText = (EditText) findViewById(R.id.editTextCMPMonitorPointName);
		monitorPointLocationEditText = (EditText) findViewById(R.id.editTextCMPMonitorPointLocation);
		takeReferencePictureButton = (Button) findViewById(R.id.buttonCMPTakeReferencePicture);
		saveMonitorPointButton = (Button) findViewById(R.id.buttonCMPSaveMonitorPoint);

		takeReferencePictureButton.setOnClickListener(this);
		saveMonitorPointButton.setOnClickListener(this);
		farmNameSpinner.setOnItemSelectedListener(this);
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
			farmNameSpinner.setAdapter(dataAdapter);
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
			System.err.println(e);
		}
	}
	
	/*
	 * Gets all farms
	 */
	public List<DataIdValue> getFarms()
	{
		return db.getAllFarms();
	}

	/*
	 * Handle onclick event and start camera or save monitor point
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(takeReferencePictureButton)) {
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_REQUEST);

		} else if (v.equals(saveMonitorPointButton)) {
			String selectedFarm = farmNameSpinner.getSelectedItem().toString();
			long farmId = getFarm(selectedFarm);
			
			long monitorPointId = saveMonitorPoint(monitorPointNameEditText.getEditableText().toString().trim(),
					monitorPointLocationEditText.getEditableText().toString().trim(), farmId);

			String imageName = "referenceimage_" + farmId + "_" + monitorPointId + ".jpg";
			String path = "farmimages" + File.separator + imageName;
			
			savebitmap(monitorPointId, path, imageName, referenceImage);
			this.finish();
		}
	}
	
	/*
	 * Gets farm id
	 * @farmName - farm name for which id is returned
	 */
	public long getFarm(String farmName)
	{
		return db.getFarmId(farmName);
	}
	
	/*
	 * Saves monitor point information to database.
	 */
	public long saveMonitorPoint(String monitorPointName, String monitorPointLocaiton, long farmId)
	{
		long monitorPointId = db.saveMonitorPoint(monitorPointName, monitorPointLocaiton);
		db.saveFarmMonitor(farmId, monitorPointId);
		return monitorPointId;
	}
	
	/*
	 * gets reference image picture from camera activity
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
			referenceImage = (Bitmap) data.getExtras().get("data");
		}
	}

	/*
	 * Saves reference image to local directory
	 */
	public File savebitmap(long monitorPointId, String path, String imageName, Bitmap bmp) {
		
		db.updateMonitorPoint(monitorPointId, path, "farmimages");
		
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString() + File.separator
				+ "farmimages";
		File dir = new File(extStorageDirectory);
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		OutputStream outStream = null;
		File file = new File(extStorageDirectory, imageName);
		if (file.exists()) {
			file.delete();
			file = new File(extStorageDirectory, imageName);
		}
		try {
			outStream = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
}
