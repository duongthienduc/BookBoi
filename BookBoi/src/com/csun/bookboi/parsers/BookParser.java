package com.csun.bookboi.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.database.BookColumns;
import com.csun.bookboi.types.Book;

public class BookParser extends AbstractParser<Book> {
	@Override
	public Book parse(JSONObject json) throws JSONException {
		Book obj = new Book();
		if (json.has(BookColumns.BOOK_ID)) {
			obj.setId(json.getInt(BookColumns.BOOK_ID));
		}
		
		if (json.has(BookColumns.BOOK_TITLE)) {
			obj.setTitle(json.getString(BookColumns.BOOK_TITLE));
		}
		
		if (json.has(BookColumns.BOOK_AUTHOR)) {
			obj.setAuthor(json.getString(BookColumns.BOOK_AUTHOR));
		}
		
		if (json.has(BookColumns.BOOK_COURSE)) {
			obj.setCourse(json.getString(BookColumns.BOOK_COURSE));
		}
		
		if (json.has(BookColumns.BOOK_SECTION)) {
			obj.setSection(json.getString(BookColumns.BOOK_SECTION));
		}
		
		if (json.has(BookColumns.BOOK_ISBN)) {
			obj.setIsbn(json.getString(BookColumns.BOOK_ISBN));
		}
		
		if (json.has(BookColumns.BOOK_EDITION)) {
			obj.setEdition(json.getString(BookColumns.BOOK_EDITION));
		}
		
		if (json.has("Cover")) {
			obj.setCoverUrl(json.getString("Cover"));
		}
		
		return obj;
	}
}