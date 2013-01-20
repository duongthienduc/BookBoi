package com.csun.bookboi;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.imagehelper.ImageLoader;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class GoogleBookActivity extends Activity {
	private static final String DEBUG_TAG = "GoogleBookActivity";
	private static final String URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:0735619670";
	private ImageLoader loader;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_book);
		
		loader = new ImageLoader(this);
		//String url = "http://covers.openlibrary.org/b/id/240727-S.jpg";
		//loader.displayImage(url, img);
		new FetchBookCoverTask().execute();
	}
	
	public void loadBookCover(String url) {
		ImageView img = (ImageView) findViewById(R.id.activity_google_book_XML_image_view_book_cover);
		loader.displayImage(url, img);
	}
	
	private class FetchBookCoverTask extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... isbns) {
			InputStream input = RESTUtil.get(URL);
			JSONObject json = JSONUtil.buildObject(input);
			String url = "";
			if (json.has("imageLinks")) {
				try {
					JSONObject o = json.getJSONObject("imageLinks");
					Log.e(DEBUG_TAG, o.toString());
					url = o.getString("smallThumbnail");
				} catch (JSONException e) {
					Log.e(DEBUG_TAG, "Exception while parsing JSON from Google", e);
				}
			}
			return url;
		}
		
		@Override
		protected void onPostExecute(String url) {
			if (!url.equals("")) {
				loadBookCover(url);
			} else {
				Log.e(DEBUG_TAG, "What the hell?");
			}
		}
	}
}
