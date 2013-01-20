package com.csun.bookboi.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class GoogleBookUtil {
	public static final String API_SEARCH_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";
	public static final String API_TAG_DELIM = "&";
	public static final String API_VALUE_DELIM = ":";
	
	public static final String TAG_ISBN = "isbn";
	public static final String TAG_TITLE = "title";
	public static final String TAG_AUTHORS = "authors";
	
	public static class Pair {
		public final String key;
		public final String value;
		
		public Pair(String k, String v) {
			key = k;
			value = v;
		}
	}
	
	public static String buildSearchQueryFromISBN(String isbn) {
		return GoogleBookUtil.buildSearchQuery(new GoogleBookUtil.Pair(GoogleBookUtil.TAG_ISBN, isbn));
	}
	
	public static String buildSearchQueryFromTitle(String title) {
		return GoogleBookUtil.buildSearchQuery(new GoogleBookUtil.Pair(GoogleBookUtil.TAG_TITLE, title));
	}
	
	public static String buildSearchQuery(Pair ... pairs) {
		StringBuilder builder = new StringBuilder();
		builder.append(API_SEARCH_QUERY);
		for (int i = 0; i < pairs.length; ++i) {
			builder.append(pairs[i].key);
			builder.append(API_VALUE_DELIM);
			builder.append(pairs[i].value);
			if (i < (pairs.length - 1)) {
				builder.append(API_TAG_DELIM);
			}
		}
		return builder.toString();
	}
	
	/**
	 * Parse a book cover from a JSONObject return from Google Book API (REST)
	 * 
	 * @param googleJSON
	 * 			The original object return from Google
	 * @return
	 * 			URL to the thumbnail image, if it does not exits we return 
	 * @throws JSONException
	 * 			Parsing exception
	 */
	public static String parseBookCoverUrl(JSONObject googleJSON) throws JSONException {
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
