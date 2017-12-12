package com.farm.monitor;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/*
 * Activity for reports
 */
public class ReportsActivity extends Activity implements OnClickListener {

	TextView farmsTextView, fieldsTextView, monitorPointsTextView, visitorsCountTextView;
	Intent intent;
	Bundle b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports);
		initializeControls();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reports, menu);
		return true;
	}
	
	/*
	 * creates activity controls
	 */
	void initializeControls(){
		farmsTextView = (TextView)findViewById(R.id.textViewReportsFarms);
		fieldsTextView = (TextView)findViewById(R.id.textViewReportsFields);
		monitorPointsTextView = (TextView)findViewById(R.id.textViewReportsMonitoringPoints);
		visitorsCountTextView = (TextView)findViewById(R.id.textViewReportsVisitorsCount);
		
		farmsTextView.setOnClickListener(this);
		fieldsTextView.setOnClickListener(this);
		monitorPointsTextView.setOnClickListener(this);
		visitorsCountTextView.setOnClickListener(this);
	}
	void initialize(){
		intent = new Intent();
	}

	/*
	 * Handle onclick event and start corresponding activity
	 */
	@Override
	public void onClick(View v) {
		if(v.equals(visitorsCountTextView)){
			intent.setClass(getApplicationContext(), VisitorsCountActivity.class);
			startActivity(intent);
			this.finish();
		}
		else if(v.equals(farmsTextView)){
			intent.setClass(getApplicationContext(), FarmsReportActivity.class);
			startActivity(intent);
			this.finish();
		}else if(v.equals(fieldsTextView)){
			intent.setClass(getApplicationContext(), FieldsReportActivity.class);
			startActivity(intent);
			this.finish();
		}else if(v.equals(monitorPointsTextView)){
			intent.setClass(getApplicationContext(), MonitorPointsReportActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

}
