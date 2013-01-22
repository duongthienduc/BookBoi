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
	private static final String DEBUG_TAG = "BuyActivity";
	private final String SEARCH_BOOKS_URL = "http://bookboi.com/chan/get_book_search_result.php";
	
	private ListView bookListView;
	private List<Book> books;
	private BookItemAdapter bookItemAdapter;
	private volatile boolean isLoading;
	private LoadBookFromServerTask task = null;
	private String searchBy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		
		setUpBookOptionsSpinner();
		setUpListView();
		setUpSearchButton();
	}
	
	private void setUpSearchButton() {
		Button b = (Button) findViewById(R.id.activity_buy_XML_button_search);
		final EditText e = (EditText) findViewById(R.id.activity_buy_XML_edit_text_searching_field);
		// final Spinner s = (Spinner) findViewById(R.id.activity_buy_XML_spinner_search_option);
		b.setOnClickListener(new OnClickListener() {
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {
				String searchText = e.getText().toString();
				List<NameValuePair> criteria = new ArrayList<NameValuePair>();
				searchText = searchText.toLowerCase(Locale.ENGLISH);
				searchBy = searchBy.toLowerCase(Locale.ENGLISH);
				Toast.makeText(BuyActivity.this, searchText + searchBy, Toast.LENGTH_LONG).show();
				criteria.add(new BasicNameValuePair("search_by", searchBy));
				criteria.add(new BasicNameValuePair("search_text", searchText));
				if (task != null && (task.getStatus() == AsyncTask.Status.PENDING || task.getStatus() == AsyncTask.Status.FINISHED)) {
					synchronized (this) {
						books.clear();
						bookItemAdapter.notifyDataSetChanged();
						task = new LoadBookFromServerTask(criteria);
						task.execute(SEARCH_BOOKS_URL);
						Toast.makeText(BuyActivity.this, "WTF", Toast.LENGTH_LONG).show();
						Log.v(DEBUG_TAG, "run task????");
					}
				} else {
						task = new LoadBookFromServerTask(criteria);
						task.execute(SEARCH_BOOKS_URL);
				}
			}
		});
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
	
	private void setUpBookOptionsSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.activity_buy_XML_spinner_search_option);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_book_options, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				searchBy = item;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// empty body
			}
		});
	}
	
	private void updateBookList(Book b) {
		books.add(b);
		bookItemAdapter.notifyDataSetChanged();
	}
	
	private class LoadBookFromServerTask extends AsyncTask<String, Book, Boolean> {
		private List<NameValuePair> criteria;
		
		public LoadBookFromServerTask(List<NameValuePair> criteria) {
			this.criteria = criteria;
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
		protected Boolean doInBackground(String... params) {
			InputStream input = null;
			// get stream from network
			if (!isCancelled()) {
				input = RESTUtil.post(params[0], criteria);
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
		
		private String getBookCoverUrlFromGoogle(String isbn) throws JSONException {
			String formatISBN = isbn.substring(0, 3) + isbn.substring(4);
			formatISBN = formatISBN.substring(0, 13);
			InputStream in = RESTUtil.get(GoogleBookUtil.buildSearchQueryFromISBN(formatISBN));
			JSONObject json = JSONUtil.buildObject(in);
			return GoogleBookUtil.parseBookCoverUrl(json);
		}
	}	
}
