package com.csun.bookboi;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.csun.bookboi.adapter.BookItemAdapter;

public class UserSettingsActivity extends BookBoiBaseActivity {
	@Override
	protected void onCreate(Bundle savedStateInstance) {
		super.onCreate(savedStateInstance);
		setContentView(R.layout.activity_user_settings);
	}
}
