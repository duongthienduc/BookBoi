package com.csun.bookboi.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.csun.bookboi.utils.Pair;
import com.csun.bookboi.utils.PairFactory;

public class ListingDatabaseHelper {
	public static final String LISTING_ID = "id";
	public static final String LISTING_BOOK_ID = "book_id";
	public static final String LISTING_BOOK_OBJECT = "book_object";
	public static final String LISTING_USER_ID = "user_id";
	public static final String LISTING_USER_OBJECT = "user_object";
	public static final String LISTING_PRICE = "price";
	public static final String LISTING_BOOK_CONDITION = "book_condition";
	public static final String LISTING_STAUS = "status";
	
	public static final String URL_SELECT = "http://www.bookboi.com/chan/get_listing.php";
	
	public static Pair<String, List<NameValuePair>> buildSelectQuery(int bookId) {
		List<NameValuePair> p = new ArrayList<NameValuePair>();
		p.add(new BasicNameValuePair("book_id", Integer.toString(bookId)));
		return PairFactory.makePair(URL_SELECT, p);
	}
}
