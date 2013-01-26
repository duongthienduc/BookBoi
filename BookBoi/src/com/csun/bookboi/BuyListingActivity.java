package com.csun.bookboi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.csun.bookboi.adapter.BookItemAdapter;
import com.csun.bookboi.adapter.ListingItemAdapter;
import com.csun.bookboi.database.ListingDatabaseHelper;
import com.csun.bookboi.parsers.BookParser;
import com.csun.bookboi.parsers.ListingParser;
import com.csun.bookboi.types.Book;
import com.csun.bookboi.types.Listing;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.Pair;
import com.csun.bookboi.utils.RESTUtil;
import com.csun.bookboi.utils.UiUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.graphics.Bitmap;

public class BuyListingActivity extends BookBoiBaseActivity {
	private static final String DEBUG_TAG = BuyListingActivity.class.getSimpleName();
	
	private ListView listingListView;
	private List<Listing> listings;
	private ListingItemAdapter listingItemAdapter;
	private LoadListingTask task = null;
	private volatile boolean isLoading;
	private DisplayImageOptions options;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_listing);
		
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_book)
			.showStubImage(R.drawable.ic_launcher)
			.cacheInMemory()
			.cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new FadeInBitmapDisplayer(400))
			.build();
		
		setUpBookHeaderView();
		setUpListView();
		launchLoadingListingTask();
	}
	
	private void setUpBookHeaderView() {
		Bundle bundle = getIntent().getExtras();
		Book book = (Book) bundle.getParcelable("book");
		TextView title = (TextView) findViewById(R.id.activity_buy_listing_XML_text_view_book_title);
		TextView author = (TextView) findViewById(R.id.activity_buy_listing_XML_text_view_book_author);
		TextView course = (TextView) findViewById(R.id.activity_buy_listing_XML_text_view_course);
		ImageView cover = (ImageView) findViewById(R.id.activity_buy_listing_XML_image_view_book_cover);
		title.setText(book.getTitle());
		author.setText(book.getAuthor());
		course.setText(book.getCourse());
		imageLoader.displayImage(book.getCoverUrl(), cover, options);
	}
	
	private void setUpListView() {
		listings = new ArrayList<Listing>();
		listingItemAdapter = new ListingItemAdapter(this, listings);
		listingItemAdapter.restartAppending();
		listingListView = (ListView) findViewById(R.id.activity_buy_listing_XML_list_view_listing);
		listingListView.setAdapter(listingItemAdapter);
		isLoading = true;
		listingListView.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int loadedItems = firstVisibleItem + visibleItemCount;
				if ((loadedItems == totalItemCount) && !isLoading) {
					if (task != null && (task.getStatus() == AsyncTask.Status.FINISHED)) {
						launchLoadingListingTask();
					}
				}
			}
		});
		
		listingListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				sendEmailToSeller("ngocchan.nguyen.61@my.csun.edu");
			}
		});
	}
	
	private class LoadListingTask extends AsyncTask<Void, Listing, Boolean> {
		private String url;
		private List<NameValuePair> post;
		
		public LoadListingTask(Pair<String, List<NameValuePair>> extras) {
			this.url = extras.first();
			this.post = extras.second();
		}
		
		@Override
		protected void onPreExecute() {
			// empty
		}
		
		@Override
		protected void onProgressUpdate(Listing... l) {
			updateListingList(l[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			listingItemAdapter.stopAppending();
			listingItemAdapter.notifyDataSetChanged();
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
								Listing l = new ListingParser().parse(array.getJSONObject(i));
								publishProgress(l);
							}
							catch (JSONException e) {
								Log.e(DEBUG_TAG, "Exception occured in doInBackGround()", e);
							}
						}
					}
				} 
			}
			return true;
		}
	}	
	
	private void updateListingList(Listing l) {
		listings.add(l);
		listingItemAdapter.notifyDataSetChanged();	
	}
	
	private void launchLoadingListingTask() {
		Bundle bundle = getIntent().getExtras();
		Book book = (Book) bundle.getParcelable("book");
		task = new LoadListingTask(ListingDatabaseHelper.buildSelectQuery(book.getId()));
		task.execute();
	}
	
	private void sendEmailToSeller(String email) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		intent.putExtra(Intent.EXTRA_SUBJECT, "BookBoi Subject");
		intent.putExtra(Intent.EXTRA_TEXT, "Body");
		try {
			startActivity(Intent.createChooser(intent, "Send mail..."));
		} catch (ActivityNotFoundException e) {
			// ignore
		}
	}
}
