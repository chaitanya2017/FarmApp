package com.farm.monitor;

import android.os.Bundle;
import android.os.StrictMode;

import com.farm.upload.GenerateJson;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*
 * Activity for uploading data to server
 */
public class UploadDataActivity extends Activity implements OnClickListener {
	Button uploadDataButton, uploadPicturesButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_data);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload_data, menu);
		return true;
	}

	/*
	 * creates activity controls
	 */
	void initializeControls() {
		uploadDataButton = (Button) findViewById(R.id.buttonUploadDataUploadData);
		uploadPicturesButton = (Button) findViewById(R.id.buttonUploadDataUploadPictures);

		uploadDataButton.setOnClickListener(this);
		uploadPicturesButton.setOnClickListener(this);

	}

	void initialize() {
		getActionBar().hide();
	}

	/*
	 * Handle onclick event and upload Json data 
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(uploadDataButton)) {
			try {
				uploadData(getData());
			} catch (Exception e) {

			}
		} else if (v.equals(uploadPicturesButton)) {
			try {
				uploadData(getImages());
			} catch (Exception e) {

			}
		}
	}
	
	/*
	 * Gets JSON for all tables except event table
	 */
	public String getData()
	{
		return new GenerateJson().getAllJson();
	}
	
	/*
	 * Gets JSON for event table
	 */
	public String getImages()
	{
		return new GenerateJson().getImagesJson();
	}

	/*
	 * Creates json and uploads farm data
	 */
	public String uploadData(String json) throws Exception {
		String response = null;
		try {
			json = URLEncoder.encode("json", "UTF-8") + "=" + URLEncoder.encode(json, "UTF-8");
			String url = "http://172.28.63.136:8080/FarmMonitorServer/FarmServlet";
			//String url = "http://192.168.1.68:8080/FarmMonitorServer/FarmServlet";
			HttpURLConnection connection = createHttpURLConnection(url);
			response = sendHttpData(connection, json);
			Toast.makeText(this, response, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/*
	 * Sends the data to the server and gets the response.
	 */
	public String sendHttpData(HttpURLConnection connection, String encodedData) {
		String line;
		StringBuffer response = new StringBuffer();
		try {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			OutputStream os = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(encodedData);

			writer.flush();
			writer.close();
			os.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();
			System.out.println(response.toString());
		} catch (Exception e) {
			return "error" + e.toString();
		}
		return response.toString();
	}
	

	/*
	 * Creates URL connection to the server
	 */
	public HttpURLConnection createHttpURLConnection(String urlString) {
		URL url;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			connection.setReadTimeout(100000);
			connection.setConnectTimeout(500000);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
		} catch (Exception e) {

		}
		return connection;
	}	
}
