package com.csun.bookboi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.adapter.BookItemAdapter;
import com.csun.bookboi.parsers.GroupParser;
import com.csun.bookboi.parsers.BookParser;
import com.csun.bookboi.types.Book;
import com.csun.bookboi.types.Group;
import com.csun.bookboi.utils.GoogleBookUtil;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * This class represents CSUN bookstore.
 * User can search for any book in this activity.
 * TODO:
 * 		1) Handle when user pressed the back button.
 * 		2) Test for memory leakage.
 * 
 * @author chan
 */
public class BookStoreActivity extends Activity {
	private static final String DEBUG_TAG = BookStoreActivity.class.getSimpleName();
	private final String GET_BOOKS_URL = "http://bookboi.com/chan/get_all_cs_books.php";
	
	private ListView bookListView;
	private List<Book> books;
	private BookItemAdapter bookItemAdapter;
	private volatile boolean isLoading;
	private LoadBookFromServerTask task;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_store);
		setUpListView();
		registerSearchButton();
	}

	private void registerSearchButton() {
		// GroupParser p = new GroupParser(new BookParser());
		findViewById(R.id.activity_book_store_XML_button_search).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// launch first task
				task = new LoadBookFromServerTask();
				task.execute(GET_BOOKS_URL);
			}
		});
	}
	
	private void setUpListView() {
		books = new ArrayList<Book>();
		bookItemAdapter = new BookItemAdapter(this, books);
		bookListView = (ListView) findViewById(R.id.activity_book_store_XML_list_view);
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
							task = new LoadBookFromServerTask();
							task.execute(GET_BOOKS_URL);
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

	private Book getRandomBook() {
		Book b = new Book();
		b.setTitle("Pro Android 3");
		b.setAuthor("Chan Nguyen");
		b.setEdition("Edition 3");
		b.setCourse("COMP 449");
		b.setPrice(99.99);
		b.setSection("12345");
		return b;
	}
	
	private void updateBookList(Book b) {
		books.add(b);
		bookItemAdapter.notifyDataSetChanged();
	}

	/**
	 * This task will loads N books from the server.
	 * The number N depends on the width/height of 
	 * the device, and display mode (landscape/portrait).
	 * We don't want the user to load all the books since it
	 * will affect the performance badly and the task 
	 * won't be able to finish. Potentially cause memory leakage.
	 * 
	 * TODO: 
	 * 		1) What's the ideal N?
	 * 		2) Implement condition to load book rather than load all.
	 * 
	 * @author chan
	 */
	private class LoadBookFromServerTask extends AsyncTask<String, Book, Boolean> {
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
				input = RESTUtil.get(params[0]);
			}
			
			// build JSON array
			JSONArray array = null;
			if (!isCancelled()) {
				array = JSONUtil.buildArray(input);
			}
		
			// parse data
			if (!isCancelled() && array != null) {
				for (int i = 0; i < array.length(); ++i) {
					if (!isCancelled()) {
						try {
							Book b = new BookParser().parse(array.getJSONObject(i));
							b.setCoverUrl(getBookCoverUrlFromGoogle(b.getIsbn()));
							publishProgress(b);
						}
						catch (JSONException e) {
							Log.e(DEBUG_TAG, "Exception occured in doInBackGround()", e);
						}
					}
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
