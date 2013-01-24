package com.csun.bookboi;

import android.content.Intent;
import android.os.Bundle;

import com.csun.bookboi.types.Book;

public class BuyActivity extends BookSearchActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	void launchActivity(Book b) {
		Intent intent = new Intent(getApplicationContext(), BuyListingActivity.class);
		intent.putExtra("book", b);
		startActivity(intent);
	}	
}
