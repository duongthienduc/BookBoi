package com.csun.bookboi;

import android.app.Activity;
import android.os.Bundle;

public class SubmitBookActivity extends Activity {
	private static final String DEBUG_TAG = SubmitBookActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_book);
	}
}
