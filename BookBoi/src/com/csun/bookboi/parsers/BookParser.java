package com.csun.bookboi.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.types.Book;

public class BookParser extends AbstractParser<Book> {
	@Override
	public Book parse(JSONObject json) throws JSONException {
		Book obj = new Book();
		if (json.has("Title")) {
			obj.setTitle(json.getString("Title"));
		}
		
		if (json.has("Author")) {
			obj.setAuthor(json.getString("Author"));
		}
		
		if (json.has("Course")) {
			obj.setCourse(json.getString("Course"));
		}
		
		if (json.has("Section")) {
			obj.setSection(json.getString("Section"));
		}
		
		if (json.has("ISBN")) {
			obj.setSection(json.getString("ISBN"));
		}
		
		if (json.has("Edition")) {
			obj.setEdition(json.getString("Edition"));
		}
		
		if (json.has("Price")) {
			obj.setPrice(json.getDouble("Price"));
		}
		return obj;
	}
}