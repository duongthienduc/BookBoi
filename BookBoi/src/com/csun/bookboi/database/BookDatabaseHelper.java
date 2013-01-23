package com.csun.bookboi.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.csun.bookboi.utils.Pair;
import com.csun.bookboi.utils.PairFactory;

public class BookDatabaseHelper {
	public static final String BOOK_ID = "id";
	public static final String BOOK_TITLE = "title";
	public static final String BOOK_AUTHOR = "author";
	public static final String BOOK_ISBN = "isbn";
	public static final String BOOK_EDITION = "edition";
	public static final String BOOK_COURSE = "course";
	public static final String BOOK_SECTION = "section";
	
	public static final String URL_INSERT = "";
	public static final String URL_UPDATE = "";
	public static final String URL_DELETE = "";
	public static final String URL_SEARCH = "http://bookboi.com/chan/get_book_search_result.php";
	
	public enum RESTOperation {
		INSERT,
		UPDATE,
		SEARCH,
		DELETE;
		
		public String getUrl() {
			switch (this) {
				case INSERT:
					return URL_INSERT;
				case UPDATE:
					return URL_UPDATE;
				case DELETE:
					return URL_DELETE;
				case SEARCH:
					return URL_SEARCH;
				default:
					throw new UnsupportedOperationException("Invalid RESTOperation");
			}
		}
	}
	
	public enum SearchOption {
		BY_TITLE,
		BY_AUTHOR,
		BY_ISBN,
		BY_COURSE;
		
		public String getSearchOptionString() {
			switch (this) {
				case BY_TITLE:
					return BookDatabaseHelper.BOOK_TITLE;
				case BY_AUTHOR:
					return BookDatabaseHelper.BOOK_AUTHOR;
				case BY_ISBN:
					return BookDatabaseHelper.BOOK_ISBN;
				case BY_COURSE:
					return BookDatabaseHelper.BOOK_COURSE;
				default:
					throw new UnsupportedOperationException("Invalid SearchOption");
			}
		}
	}
	
	public static Pair<String, List<NameValuePair>> buildSearchQuery(SearchOption opt, String extras) {
		List<NameValuePair> p = new ArrayList<NameValuePair>();
		p.add(new BasicNameValuePair("search_by", opt.getSearchOptionString()));
		p.add(new BasicNameValuePair("search_query", extras));
		return PairFactory.makePair(URL_SEARCH, p);
	}
}
