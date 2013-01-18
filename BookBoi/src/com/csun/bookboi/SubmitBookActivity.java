package com.csun.bookboi;

import android.app.Activity;
import android.os.Bundle;

public class SubmitBookActivity extends Activity {
	public static final String TAG = "BookStoreActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_book);
	}
}
