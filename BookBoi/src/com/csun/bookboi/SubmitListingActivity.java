package com.csun.bookboi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.parsers.BookParser;
import com.csun.bookboi.types.Book;
import com.csun.bookboi.types.Listing;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitListingActivity extends Activity {
	private static final String DEBUG_TAG = SubmitListingActivity.class.getSimpleName();
	private static final String SUBMIT_BOOK_URL = "http://bookboi.com/chan/post_listing.php";
	private ImageLoader imageLoader;
	private Book book;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_listing);
		
		Bundle bundle = getIntent().getExtras();
		book = (Book) bundle.getParcelable("book");
		
		Toast.makeText(this, Integer.toString(book.getId()), Toast.LENGTH_LONG).show();
		setUpBookHeader(book);
		setUpSubmitButton();
	}
	
	private void setUpBookHeader(Book b) {
		ImageView cover = (ImageView) findViewById(R.id.activity_submit_listing_XML_image_view_cover);
		TextView title = (TextView) findViewById(R.id.activity_submit_listing_XML_text_view_title);
		title.setText(b.getTitle());
		TextView author = (TextView) findViewById(R.id.activity_submit_listing_XML_text_view_author);
		author.setText(b.getAuthor());
		TextView course = (TextView) findViewById(R.id.activity_submit_listing_XML_text_view_course);
		course.setText(b.getCourse());
	}

	private void setUpSubmitButton() {
		Button b = (Button) findViewById(R.id.activity_submit_listing_XML_button_submit);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SubmitListingTask(prepareListingInfo(book)).execute(SUBMIT_BOOK_URL);
			}
		});
	}
	
	private List<NameValuePair> prepareListingInfo(Book b) {
		EditText p = (EditText) findViewById(R.id.activity_submit_listing_XML_edit_text_price);
		EditText c = (EditText) findViewById(R.id.activity_submit_listing_XML_edit_text_condition);
		List<NameValuePair> info = new ArrayList<NameValuePair>();
		info.add(new BasicNameValuePair("user_id", "5"));
		info.add(new BasicNameValuePair("book_id", Integer.toString(b.getId())));
		info.add(new BasicNameValuePair("price", p.getText().toString()));
		info.add(new BasicNameValuePair("condition", c.getText().toString()));
		return info;
	}
	
	private class SubmitListingTask extends AsyncTask<String, Void, Boolean> {
		private List<NameValuePair> listing;
		
		public SubmitListingTask(List<NameValuePair> listing) {
			this.listing = listing;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			InputStream input = null;
			// get stream from network
			if (!isCancelled()) {
				input = RESTUtil.post(params[0], listing);
			}
			
			// build JSON array
			JSONObject json = null;
			if (!isCancelled()) {
				json = JSONUtil.buildObject(input);
				if (json.has("result")) {
					try {
						String result = json.getString("result");
						if (result.equals("success")) {
							return true;
						}
					} catch (JSONException e) {
						Log.e(DEBUG_TAG, "Exception occured while parsing JSON", e);
					}
					
				}
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast.makeText(SubmitListingActivity.this, "Your listing has been posted.", Toast.LENGTH_LONG).show();
			} else {
				
			}
		}
		
		
	}
	
}
