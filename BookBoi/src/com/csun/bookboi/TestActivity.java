package com.csun.bookboi;


import com.csun.bookboi.types.Listing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Only for testing purpose
 * @author chan
 */
public class TestActivity extends Activity {
	private static final String DEBUG_TAG = TestActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		registerActivityButtons();
	}
	
	private void registerActivityButtons() {
		findViewById(R.id.activity_test_xml_button_login).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), LogInActivity.class));
			}
		});
		
		findViewById(R.id.activity_test_xml_button_submit_book).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SubmitBookActivity.class));
			}
		});
		
		findViewById(R.id.activity_test_xml_button_console).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ConsoleTestingActivity.class));
			}
		});
		
		findViewById(R.id.activity_test_xml_button_features).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), FeaturesActivity.class));
			}
		});
		
		findViewById(R.id.activity_test_xml_button_departments).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), DepartmentActivity.class));
			}
		});
		
	}
}
