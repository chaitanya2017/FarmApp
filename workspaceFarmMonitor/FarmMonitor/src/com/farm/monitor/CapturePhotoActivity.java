package com.farm.monitor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.farm.db.DataIdValue;
import com.farm.db.DatabaseHandler;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * Activity for taking pictures
 */
public class CapturePhotoActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	DatabaseHandler db;
	Bundle b;
	Intent intent;
	Spinner farmNameSpinner, monitorPointNameSpinner;
	Button takePictureButton;
	private static final int CAMERA_REQUEST = 1888;
	Bitmap image, image1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_photo);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.capture_photo, menu);
		return true;
	}

	/*
	 * creates activity controls
	 */
	void initializeControls() {
		farmNameSpinner = (Spinner) findViewById(R.id.spinnerCapturePhotoFarmName);
		monitorPointNameSpinner = (Spinner) findViewById(R.id.spinnerCapturePhotoMonitorPointName);
		takePictureButton = (Button) findViewById(R.id.buttonCapturePhotoTakePicture);
		farmNameSpinner.setOnItemSelectedListener(this);
		monitorPointNameSpinner.setOnItemSelectedListener(this);
		takePictureButton.setOnClickListener(this);
	}

	void initialize() {
		getActionBar().hide();
		try {
			db = new DatabaseHandler();
			intent = new Intent();

			initSpinners();
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	void initSpinners() {
		try {
			List<DataIdValue> farms = getFarms();
			ArrayAdapter<DataIdValue> dataAdapter = new ArrayAdapter<DataIdValue>(this, R.layout.my_text, farms);
			farmNameSpinner.setAdapter(dataAdapter);
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * gets all farms
	 */
	public List<DataIdValue> getFarms()
	{
		return db.getAllFarms();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int arg2, long arg3) {
		try {
			if (parent.getId() == R.id.spinnerCapturePhotoFarmName) {
				Integer key = ((DataIdValue) farmNameSpinner.getSelectedItem()).getKey();
				List<DataIdValue> monitorPoinitsList = getMonitorPoints(key);
				ArrayAdapter<DataIdValue> dataAdapter = new ArrayAdapter<DataIdValue>(this, R.layout.my_text,
						monitorPoinitsList);
				monitorPointNameSpinner.setAdapter(dataAdapter);
			}
		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
		}
	}
	
	/*
	 * Get All Monitor Points for which pictures shall be taken
	 */
	public List<DataIdValue> getMonitorPoints(Integer key)
	{
		return db.getMonitorPointsByFarmId(key);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	/*
	 * Show device camera
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(takePictureButton)) {
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
	}

	/*
	 * If user clicks a picture then save the picture to local directory and picture path in the database
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
			image = (Bitmap) data.getExtras().get("data");
		}
		Integer farmId = ((DataIdValue) farmNameSpinner.getSelectedItem()).getKey();
		Integer monitorPointId = ((DataIdValue) monitorPointNameSpinner.getSelectedItem()).getKey();
		
		String date = db.getCurrentDateTime();
		saveEvent(farmId, monitorPointId, date);
		image1 = printText(image, farmId, monitorPointId);
		savebitmap(image1, farmId, monitorPointId, date);
		this.finish();
	}
	
	/*
	 * Saves an event to database
	 */
	public long saveEvent(Integer farmId, Integer monitorPointId, String date)
	{
		String eventImageName = farmId + "_" + monitorPointId + "_" + date + ".jpg";
		return db.saveEvent(monitorPointId, "farmimages/" + eventImageName);
	}

	/*
	 * Saves picture to local folder
	 */
	public File savebitmap(Bitmap bmp, Integer farmId, Integer monitorPointId, String date) {
		
		String eventImageName = farmId + "_" + monitorPointId + "_" + date + ".jpg";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString() + File.separator
				+ "farmimages";
		OutputStream outStream = null;
		File file = new File(extStorageDirectory, eventImageName);
		if (file.exists()) {
			file.delete();
			file = new File(extStorageDirectory, eventImageName);
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

	/*
	 * Prints date on a picture
	 */
	public Bitmap printText(Bitmap bmp, int farmId, int monitorPointId) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] bitmapdata = stream.toByteArray();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inMutable = true;
			bmp = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length, options);

			Canvas canvas = new Canvas(bmp);
			canvas.drawBitmap(bmp, 0, 0, null);

			Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
			Paint t = new Paint(Paint.ANTI_ALIAS_FLAG);
			paintText.setColor(Color.RED);
			t.setTextSize(15);
			t.setColor(Color.GREEN);
			try {
				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dt = new Date();
				canvas.drawText("Date: " + timeFormat.format(dt), 10, 10, t);

			} catch (Exception e) {
				return null;
			}

		} catch (Exception e) {
			Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
			return null;
		}
		return bmp;
	}
}
