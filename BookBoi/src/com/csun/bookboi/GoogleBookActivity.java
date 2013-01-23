package com.csun.bookboi;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.imagehelper.ImageLoaderSlow;
import com.csun.bookboi.utils.GoogleBookUtil;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class GoogleBookActivity extends Activity {
	private static final String DEBUG_TAG = GoogleBookActivity.class.getSimpleName();
	private ImageLoaderSlow loader;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_book);
		
		loader = new ImageLoaderSlow(this);
		String url = GoogleBookUtil.buildSearchQuery(new GoogleBookUtil.Pair(GoogleBookUtil.TAG_ISBN, "0735619670"));
		new FetchBookCoverTask().execute(url);
	}
	
	public void loadBookCover(String url) {
		ImageView img = (ImageView) findViewById(R.id.activity_google_book_XML_image_view_book_cover);
		loader.displayImage(url, img);
	}
	
	
	private class FetchBookCoverTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			InputStream input = RESTUtil.get(urls[0]);
			JSONObject json = JSONUtil.buildObject(input);
			String url = "";
			try {
				url = parseBookCoverUrl(json);
			} catch (JSONException e) {
				Log.e(DEBUG_TAG, "Exception while parsing JSON from Google", e);
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
		
		private String parseBookCoverUrl(JSONObject googleJSON) throws JSONException {
			String url = "";
			if (googleJSON.has("items")) {
				// since we passed in only 1 ISBN, the array of items should have
				// only 1 item, so the first item is the one we need to fetch
				JSONObject item = googleJSON.getJSONArray("items").getJSONObject(0);
				if (item.has("volumeInfo")) {
					JSONObject info = item.getJSONObject("volumeInfo");
					if (info.has("imageLinks")) {
						JSONObject images = info.getJSONObject("imageLinks");
						if (images.has("thumbnail")) {
							url = images.getString("thumbnail");
						}
					}
				}
			}
			return url;
		}
	}
}
