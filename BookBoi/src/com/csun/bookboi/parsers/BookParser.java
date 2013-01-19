package com.csun.bookboi.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.types.Book;

public class BookParser extends AbstractParser<Book> {
	@Override
	public Book parse(JSONObject json) throws JSONException {
		Book obj = new Book();
		
		if (json.has("title")) {
			obj.setTitle(json.getString("title"));
		}
		
		if (json.has("author")) {
			obj.setAuthor(json.getString("author"));
		}
		
		if (json.has("course")) {
			obj.setCourse(json.getString("course"));
		}
		
		if (json.has("section")) {
			obj.setSection(json.getString("section"));
		}
		
		if (json.has("isbn")) {
			obj.setSection(json.getString("isbn"));
		}
		
		if (json.has("edition")) {
			obj.setEdition(json.getString("edition"));
		}
		
		if (json.has("price")) {
			obj.setPrice(json.getDouble("price"));
		}
		return obj;
	}
}