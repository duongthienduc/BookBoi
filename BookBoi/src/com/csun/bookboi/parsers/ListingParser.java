package com.csun.bookboi.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.database.BookDatabaseHelper;
import com.csun.bookboi.database.ListingDatabaseHelper;
import com.csun.bookboi.types.Book;
import com.csun.bookboi.types.Listing;

public class ListingParser extends AbstractParser<Listing> {
	@Override
	public Listing parse(JSONObject json) throws JSONException {
		Listing obj = new Listing();
		if (json.has(ListingDatabaseHelper.LISTING_ID)) {
			obj.setId(json.getInt(ListingDatabaseHelper.LISTING_ID));
		}
		if (json.has(ListingDatabaseHelper.LISTING_BOOK_OBJECT)) {
			obj.setBook(new BookParser().parse(json.getJSONObject(ListingDatabaseHelper.LISTING_BOOK_OBJECT)));
		}
		if (json.has(ListingDatabaseHelper.LISTING_USER_OBJECT)) {
			obj.setUser(new UserParser().parse(json.getJSONObject(ListingDatabaseHelper.LISTING_USER_OBJECT)));
		}
		if (json.has(ListingDatabaseHelper.LISTING_PRICE)) {
			obj.setPrice(json.getDouble(ListingDatabaseHelper.LISTING_PRICE));
		}
		if (json.has(ListingDatabaseHelper.LISTING_BOOK_CONDITION)) {
			obj.setCondition(json.getString(ListingDatabaseHelper.LISTING_BOOK_CONDITION));
		}
		if (json.has(ListingDatabaseHelper.LISTING_STAUS)) {
			obj.setStatus(json.getString(ListingDatabaseHelper.LISTING_STAUS));
		}
		return obj;
	}
}
