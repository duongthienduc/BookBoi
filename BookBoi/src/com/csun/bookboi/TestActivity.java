package com.csun.bookboi;

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
	private static final String DEBUG_TAG = "TestActivity";
	
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
		
		findViewById(R.id.activity_test_xml_button_main_menu).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
			}
		});
		
		findViewById(R.id.activity_test_xml_button_submit_book).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SubmitBookActivity.class));
			}
		});
		
		findViewById(R.id.activity_test_xml_button_signup).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
			}
		});
		
		findViewById(R.id.activity_test_xml_button_bookstore).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), BookStoreActivity.class));
			}
		});
	}
}