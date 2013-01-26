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
import com.csun.bookboi.dialogs.NetworkErrorDialog;
import com.csun.bookboi.dialogs.NotFoundDialog;
import com.csun.bookboi.parsers.BookParser;
import com.csun.bookboi.scan.ScanIntent;
import com.csun.bookboi.types.Book;
import com.csun.bookboi.utils.GoogleBookUtil;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.Pair;
import com.csun.bookboi.utils.RESTUtil;
import com.csun.bookboi.utils.UiUtil;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Base class for BuyActivity & SellActivity.
 * They must override launchActivity(Book b) 
 * method to start a correct activity. Specifically,
 * 		+ BuyActivity must launch BuyListingActivity
 *      + SellActivity must lanuch SubmitListingActivity
 * @author chan
 */
public abstract class BookSearchActivity extends BookBoiBaseActivity {
	private static final String DEBUG_TAG = BookSearchActivity.class.getSimpleName();
	
	private ListView bookListView;
	private GridView bookGridView;
	private List<Book> books;
	private BookItemAdapter bookItemAdapter;
	private volatile boolean isLoading;
	private LoadBookFromServerTask task = null;
	
	abstract void launchActivity(Book b);
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_book);
		books = new ArrayList<Book>();
		bookItemAdapter = new BookItemAdapter(this, books, BookItemAdapter.ViewType.LIST_VIEW);
		setUpViews();
	}
	
	/**
	 * Set up all views
	 */
	private void setUpViews() {
		setUpBookOptionsSpinner();
		setUpGridView();
		setUpListView();
		setUpSearchButton();
		setUpScanButton();
	}
	
	/**
	 * Set up search option from spinner
	 */
	private void setUpBookOptionsSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.activity_buy_XML_spinner_search_option);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_book_options, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	/**
	 * Set up scan intent for scanning ISBN
	 */
	private void setUpScanButton() {
		Button scan = (Button) findViewById(R.id.activity_buy_XML_button_scan);
		scan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int dummy = 0;
				startScan(dummy);
			}
		});
	}
	
	/**
	 * Set up search button, load
	 * search task when user hit search button
	 */
	private void setUpSearchButton() {
		Button b = (Button) findViewById(R.id.activity_buy_XML_button_search);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSearchTask();
			}
		});
	}
	
	/**
	 * Set up list view and hook up
	 * both onScroll & onItemClick event
	 */
	private void setUpListView() {
		bookListView = (ListView) findViewById(R.id.activity_buy_XML_list_view_book);
		bookListView.setAdapter(bookItemAdapter);
		isLoading = true;
		bookListView.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// ignore
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
		bookListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// derived class must override this method in order
				// to launch their correct activity
				launchActivity(books.get(position).makeCopy());
			}
		});
		bookListView.setOnScrollListener(new PauseOnScrollListener(false, true));
	}
	
	/**
	 * Set up list view and hook up
	 * both onScroll & onItemClick event
	 */
	private void setUpGridView() {
		bookGridView = (GridView) findViewById(R.id.activity_buy_XML_grid_view_book);
		bookGridView.setAdapter(bookItemAdapter);
	}
	
	/**
	 * Make sure the current task is not running 
	 * @return
	 * 			true if either the task have 
	 * 			not started or already finished
	 */
	private boolean isTaskAvailable() {
        return ((task == null) || (task != null && task.getStatus() == AsyncTask.Status.FINISHED));
	}
	
	/**
	 * Load book from our server depends on search text
	 * t
	 * @param extras
	 * 			A pair of 
	 * 				+ URL: to php search functionc
	 * 				+ post data: searching text
	 */
	private void tryLoadBook(Pair<String, List<NameValuePair>> extras) {
		if (isTaskAvailable()) {
			task = new LoadBookFromServerTask(extras);
    		task.execute();
    		bookItemAdapter.restartAppending();
		}
	}
	
	/**
	 * Handle scan ISBN. If success, we load
	 * the book (only 1) from our server that matches this ISBN.
	 */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
        	final Bundle bundle = data.getExtras();
        	String text = bundle.getString(ScanIntent.SCAN_RESULT);
        	// test
        	UiUtil.showText(this, text);
			synchronized (this) {
				clearBookList();
				final Pair<String, List<NameValuePair>> extras = BookDatabaseHelper.buildSearchQuery(SearchOption.BY_ISBN, text);
				tryLoadBook(extras);
    		}
        }
    }
	
	/**
	 * Start scan activity
	 * @param code
	 * 			unused
	 */
	private void startScan(int code) {
        try {
            final Intent intent = new Intent(ScanIntent.INTENT_ACTION_SCAN);
            intent.putExtra(ScanIntent.INTENT_EXTRA_SCAN_MODE, ScanIntent.INTENT_EXTRA_PRODUCT_MODE);
            startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Log.e(DEBUG_TAG, BookSearchActivity.class.getSimpleName() + " not found!", e);
        }
    }

	/*
	 * Get the current selection of Spinner and convert
	 * it to a SearchOption. 
	 *  <string-array name="search_book_options">
     *  	<item>	Title	</item>
     *   	<item>	Author	</item>
     *   	<item>	Course	</item>
     *   	<item>	ISBN	</item>
     *	</string-array>
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
	
	/**
	 * Launch search task base on user
	 * input text
	 */
	private void onSearchTask() {
		final EditText edit = (EditText) findViewById(R.id.activity_buy_XML_edit_text_searching_field);
		if (!TextUtils.isEmpty(edit.getText())) {
			String text = edit.getText().toString().trim();
			final Pair<String, List<NameValuePair>> extras = BookDatabaseHelper.buildSearchQuery(getCurrentSearchOption(), text);
			synchronized (this) {
				clearBookList();
				tryLoadBook(extras);
			}
		}
	}
	
	/**
	 * Add a book to the current list
	 * and notify the book adapter to redraw
	 * @param b
	 * 			A book from database
	 */
	private synchronized void updateBookList(Book b) {
		books.add(b);
		bookItemAdapter.notifyDataSetChanged();		
	}
	
	/**
	 * Clear all books and notify
	 * the book adapter to redraw
	 */
	private synchronized void clearBookList() {
		books.clear();
		bookItemAdapter.notifyDataSetChanged();
	}
	
	/**
	 * If there is no more book, 
	 * stop progress bar
	 */
	private void resetPending() {
		bookItemAdapter.stopAppending();
		bookItemAdapter.notifyDataSetChanged();
	}
	
	private class LoadBookFromServerTask extends AsyncTask<Void, Book, Boolean> {
		private String url;
		private List<NameValuePair> post;
		
		public LoadBookFromServerTask(Pair<String, List<NameValuePair>> extras) {
			this.url = extras.first();
			this.post = extras.second();
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
			resetPending();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			InputStream input = null;
			// get stream from network
			if (!isCancelled()) {
				input = RESTUtil.post(url, post);
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
								b.setCoverUrl(getBookCoverUrlFromGoogle(b.getIsbn()));
								publishProgress(b);
							}
							catch (JSONException e) {
								Log.e(DEBUG_TAG, "Parsing book exception", e);
							}
						}
					}
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showNotFoundDialog();
						}
					});
				}
			}
			return true;
		}
	}	
	
	/**
	 * Get the book cover from Google Book from a given ISBN.
	 * @param isbn
	 * 			Book ISBN and must be of the form ISBN-13
	 * @return
	 * 			A URL to book image
	 * @throws 
	 * 			JSONException when parsing wrong format
	 */
	private String getBookCoverUrlFromGoogle(String isbn) throws JSONException {
		InputStream in = RESTUtil.get(GoogleBookUtil.buildSearchQueryFromISBN(isbn));
		isbn = isbn.substring(0, 13);
		JSONObject json = JSONUtil.buildObject(in);
		return GoogleBookUtil.parseBookCoverUrl(json);
	}
	
	private void showNotFoundDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NotFoundDialog d = new NotFoundDialog();
        d.show(fm, "Disconnected");
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_search, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.listview:
	        	bookItemAdapter.setCurrentViewType(BookItemAdapter.ViewType.LIST_VIEW);
	        	bookListView.setVisibility(View.VISIBLE);
	        	bookGridView.setVisibility(View.GONE);
	            return true;
	            
	        case R.id.gridview:
	        	bookItemAdapter.setCurrentViewType(BookItemAdapter.ViewType.GRID_VIEW);
	        	bookListView.setVisibility(View.GONE);
	        	bookGridView.setVisibility(View.VISIBLE);
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override 
	protected void onDestroy() {
		Log.v(DEBUG_TAG, "Clear cached memory");
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
		super.onDestroy();
	}
}
