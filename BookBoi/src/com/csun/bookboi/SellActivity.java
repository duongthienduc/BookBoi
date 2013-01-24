package com.csun.bookboi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.adapter.BookItemAdapter;
import com.csun.bookboi.parsers.BookParser;
import com.csun.bookboi.types.Book;
import com.csun.bookboi.utils.GoogleBookUtil;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Search options:
 * 		1) Author
 * 		2) Title
 * 		3) ISBN
 * 		4) Course Number
 * @author chan
 *
 */
public class SellActivity extends BookSearchActivity {
	private static final String DEBUG_TAG = SellActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	void launchActivity(Book b) {
		Intent intent = new Intent(getApplicationContext(), SubmitListingActivity.class);
		intent.putExtra("book", b);
		startActivity(intent);
	}	
}
