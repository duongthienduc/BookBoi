package com.csun.bookboi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.database.ListingDatabaseHelper;
import com.csun.bookboi.global.EmailConstants;
import com.csun.bookboi.parsers.BookParser;
import com.csun.bookboi.parsers.ServerResponseParser;
import com.csun.bookboi.services.GMailSender;
import com.csun.bookboi.services.SingletonUser;
import com.csun.bookboi.types.Book;
import com.csun.bookboi.types.Listing;
import com.csun.bookboi.types.ServerResponse;
import com.csun.bookboi.utils.Pair;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;
import com.csun.bookboi.utils.ThreadUtil;
import com.csun.bookboi.utils.UiUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitListingActivity extends BookBoiBaseActivity {
	private static final String DEBUG_TAG = SubmitListingActivity.class.getSimpleName();
	
	private Book book;
	private DisplayImageOptions options;
	private SubmitListingTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_listing);
		
		Bundle bundle = getIntent().getExtras();
		book = (Book) bundle.getParcelable("book");
		
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_book)
			.showStubImage(R.drawable.ic_launcher)
			.cacheInMemory()
			.cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
		setUpBookHeader(book);
		setUpSubmitButton();
	}
	
	private void setUpBookHeader(Book b) {
		ImageView cover = (ImageView) findViewById(R.id.activity_submit_listing_XML_image_view_cover);
		imageLoader.displayImage(b.getCoverUrl(), cover, options);
		final TextView title = (TextView) findViewById(R.id.activity_submit_listing_XML_text_view_title);
		title.setText(b.getTitle());
		final TextView author = (TextView) findViewById(R.id.activity_submit_listing_XML_text_view_author);
		author.setText(b.getAuthor());
		final TextView course = (TextView) findViewById(R.id.activity_submit_listing_XML_text_view_course);
		course.setText(b.getCourse());
		final TextView isbn = (TextView) findViewById(R.id.activity_submit_listing_XML_text_view_isbn);
		isbn.setText(b.getIsbn());
		Animation animation = AnimationUtils.loadAnimation(SubmitListingActivity.this, R.anim.back_and_forth);
		title.startAnimation(animation);
	}

	private void setUpSubmitButton() {
		Button b = (Button) findViewById(R.id.activity_submit_listing_XML_button);
		final EditText p = (EditText) findViewById(R.id.activity_submit_listing_XML_edit_text_price);
		final EditText c = (EditText) findViewById(R.id.activity_submit_listing_XML_edit_text_condition);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (haveTaskAvailable(task)) {
					task = new SubmitListingTask(ListingDatabaseHelper.buildPostQuery(
						SingletonUser.getActiveUser().getId(),  // user id
						book.getId(),                           // book id
						p.getText().toString(),                 // price
						c.getText().toString()));               // condition
					task.execute();
				}
			}
		});
	}
	
	private class SubmitListingTask extends AsyncTask<Void, Void, ServerResponse> {
		private final String url;
		private final List<NameValuePair> listing;
		private final ProgressDialog pd;
		
		public SubmitListingTask(Pair<String, List<NameValuePair>> extras) {
			url = extras.first();
			listing = extras.second();
			pd = new ProgressDialog(SubmitListingActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			pd.setMessage("Submitting in progress...");
			pd.setCancelable(false);
			pd.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
			pd.show();
		}
		
		@Override
		protected ServerResponse doInBackground(Void... params) {
			ServerResponse r = new ServerResponse();
			InputStream input = null;
			if (!isCancelled()) {
				input = RESTUtil.post(url, listing);
				try {
					r = new ServerResponseParser().parse(JSONUtil.buildObject(input));
				} catch (JSONException e) {
					Log.e(DEBUG_TAG, "Parsing JSON error", e);
				}
				// TODO: add correct user email
				sendNotificationEmail("");
			}
			return r;
		}
		
		@Override
		protected void onPostExecute(ServerResponse r) {
			pd.dismiss();
			if (r.getResult()) {
				UiUtil.showText(SubmitListingActivity.this, "Your listing has been posted.");
			} else {
				UiUtil.showText(SubmitListingActivity.this, "Something wrong with your listing!");
			}
		}
	}
	
	private void sendNotificationEmail(String email) {
		try {   
            GMailSender sender = new GMailSender(EmailConstants.USERNAME, EmailConstants.PASSWORD);
            sender.sendMail("Hello seller!", "Your listing is posted sucessfully!", EmailConstants.USERNAME, "ngocchan.nguyen.61@my.csun.edu");
        } catch (Exception e) {   
            Log.e("SendMail", e.getMessage(), e);   
        } 
	}
}
