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
import com.csun.bookboi.database.BookDatabaseHelper;
import com.csun.bookboi.database.BookDatabaseHelper.SearchOption;
import com.csun.bookboi.parsers.BookParser;
import com.csun.bookboi.scan.ScanIntent;
import com.csun.bookboi.types.Book;
import com.csun.bookboi.utils.GoogleBookUtil;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.Pair;
import com.csun.bookboi.utils.RESTUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
public class BuyActivity extends Activity {
	private static final String DEBUG_TAG = BuyActivity.class.getSimpleName();
	
	private ListView bookListView;
	private List<Book> books;
	private BookItemAdapter bookItemAdapter;
	private volatile boolean isLoading;
	private LoadBookFromServerTask task = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		setUpViews();
	}
	
	private void setUpViews() {
		setUpBookOptionsSpinner();
		setUpListView();
		setUpSearchButton();
		setUpScanButton();
	}
	
	private void setUpBookOptionsSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.activity_buy_XML_spinner_search_option);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_book_options, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	private void setUpScanButton() {
		Button scan = (Button) findViewById(R.id.activity_buy_XML_button_scan);
		scan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startScan(1);
			}
		});
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
        	if (requestCode == 1) {
        		final Bundle bundle = data.getExtras();
        		Toast.makeText(this, bundle.getString(ScanIntent.SCAN_RESULT), Toast.LENGTH_LONG).show();
        	}
        }
    }
	
	 private void startScan(int code) {
	        try {
	            final Intent intent = new Intent(ScanIntent.INTENT_ACTION_SCAN);
	            intent.putExtra(ScanIntent.INTENT_EXTRA_SCAN_MODE, ScanIntent.INTENT_EXTRA_PRODUCT_MODE);
	            startActivityForResult(intent, code);
	        } catch (ActivityNotFoundException e) {
	            
	        }
	    }
	
	private void setUpSearchButton() {
		Button b = (Button) findViewById(R.id.activity_buy_XML_button_search);
		final EditText e = (EditText) findViewById(R.id.activity_buy_XML_edit_text_searching_field);
		// final Spinner s = (Spinner) findViewById(R.id.activity_buy_XML_spinner_search_option);
		b.setOnClickListener(new OnClickListener() {
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {
				onSearchTask();
			}
		});
	}
	
	/*
	 * From resource
	 *  <string-array name="search_book_options">
        <item>Title</item>
        <item>Author</item>
        <item>Course</item>
        <item>ISBN</item>
    	</string-array>
	 */
	private BookDatabaseHelper.SearchOption getCurrentSearchOption() {
		Spinner spinner = (Spinner) findViewById(R.id.activity_buy_XML_spinner_search_option);
		if (spinner.getSelectedItem().toString().equals("Title")) {
			return SearchOption.BY_TITLE;
		}
		else if (spinner.getSelectedItem().toString().equals("Author")) {
			return SearchOption.BY_AUTHOR;
		}
		else if (spinner.getSelectedItem().toString().equals("Course")) {
			return SearchOption.BY_COURSE;
		}
		else { // if (spinner.getSelectedItem().toString().equals("ISBN")) 
			return SearchOption.BY_ISBN;
		}
	}
	
	private void onSearchTask() {
		final EditText et = (EditText) findViewById(R.id.activity_buy_XML_edit_text_searching_field);
		if (!TextUtils.isEmpty(et.getText())) {
			String extras = et.getText().toString().trim();
			final Pair<String, List<NameValuePair>> post = BookDatabaseHelper.buildSearchQuery(getCurrentSearchOption(), extras);
			if ((task == null) || (task != null && task.getStatus() == AsyncTask.Status.FINISHED)) {
				synchronized (this) {
					
					clearBookList();
					
					// launch new task
					task = new LoadBookFromServerTask(post.first(), post.second());
					task.execute();
				}
			}
		}
	}
	
	private void setUpListView() {
		books = new ArrayList<Book>();
		bookItemAdapter = new BookItemAdapter(this, books);
		bookListView = (ListView) findViewById(R.id.activity_buy_XML_list_view_book);
		bookListView.setAdapter(bookItemAdapter);
		handleOnScroll();
		handleOnItemClick();
	}
	
	private void handleOnScroll() {
		isLoading = true;
		bookListView.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int loadedItems = firstVisibleItem + visibleItemCount;
				if ((loadedItems == totalItemCount) && !isLoading) {
					if (task != null && (task.getStatus() == AsyncTask.Status.FINISHED)) {
						synchronized (this) {
							// task = new LoadBookFromServerTask(SEARCH_BOOKS_URL);
							// task.execute(SEARCH_BOOKS_URL);
						}
					}
				}
			}
		});
	}
	
	private void handleOnItemClick() {
		bookListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
	}
	
	private synchronized void updateBookList(Book b) {
		books.add(b);
		bookItemAdapter.notifyDataSetChanged();		
	}
	
	private synchronized void clearBookList() {
		books.clear();
		bookItemAdapter.notifyDataSetChanged();
	}
	
	private class LoadBookFromServerTask extends AsyncTask<Void, Book, Boolean> {
		private String url;
		private List<NameValuePair> extras;
		
		public LoadBookFromServerTask(String url, List<NameValuePair> extras) {
			this.url = url;
			this.extras = extras;
		}
		
		@Override
		protected void onPreExecute() {
			// empty
		}
		
		@Override
		protected void onProgressUpdate(Book... b) {
			updateBookList(b[0]);
		}


		protected void onPostExecute(Boolean result) {
			isLoading = false;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			InputStream input = null;
			// get stream from network
			if (!isCancelled()) {
				input = RESTUtil.post(url, extras);
			}
			
			// build JSON array
			JSONArray array = null;
			if (!isCancelled()) {
				array = JSONUtil.buildArray(input);
			}
		
			// parse data
			if (!isCancelled() && array != null) {
				if (array.length() > 0) {
					for (int i = 0; i < array.length(); ++i) {
						if (!isCancelled()) {
							try {
								Book b = new BookParser().parse(array.getJSONObject(i));
								//b.setCoverUrl(getBookCoverUrlFromGoogle(b.getIsbn()));
								publishProgress(b);
							}
							catch (JSONException e) {
								Log.e(DEBUG_TAG, "Exception occured in doInBackGround()", e);
							}
						}
					}
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(BuyActivity.this, "Not found", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
			return true;
		}
	}	
}
