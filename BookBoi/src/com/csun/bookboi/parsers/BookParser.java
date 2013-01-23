package com.csun.bookboi.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.database.BookDatabaseHelper;
import com.csun.bookboi.types.Book;

public class BookParser extends AbstractParser<Book> {
	@Override
	public Book parse(JSONObject json) throws JSONException {
		Book obj = new Book();
		if (json.has(BookDatabaseHelper.BOOK_ID)) {
			obj.setId(json.getInt(BookDatabaseHelper.BOOK_ID));
		}
		
		if (json.has(BookDatabaseHelper.BOOK_TITLE)) {
			obj.setTitle(json.getString(BookDatabaseHelper.BOOK_TITLE));
		}
		
		if (json.has(BookDatabaseHelper.BOOK_AUTHOR)) {
			obj.setAuthor(json.getString(BookDatabaseHelper.BOOK_AUTHOR));
		}
		
		if (json.has(BookDatabaseHelper.BOOK_COURSE)) {
			obj.setCourse(json.getString(BookDatabaseHelper.BOOK_COURSE));
		}
		
		if (json.has(BookDatabaseHelper.BOOK_SECTION)) {
			obj.setSection(json.getString(BookDatabaseHelper.BOOK_SECTION));
		}
		
		if (json.has(BookDatabaseHelper.BOOK_ISBN)) {
			obj.setIsbn(json.getString(BookDatabaseHelper.BOOK_ISBN));
		}
		
		if (json.has(BookDatabaseHelper.BOOK_EDITION)) {
			obj.setEdition(json.getString(BookDatabaseHelper.BOOK_EDITION));
		}
		
		if (json.has("Cover")) {
			obj.setCoverUrl(json.getString("Cover"));
		}
		
		return obj;
	}
}