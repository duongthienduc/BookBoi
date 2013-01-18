package com.csun.bookboi.provider;

public class BookProvider {
	private final String title;
	private final String author;
	private final String course;
	private final String section;
	
	private double price;
	private String isbn;
	private String edition;
	
	public BookProvider(String title, String author, String course, String section) {
		this.title = title;
		this.author = author;
		this.course = course;
		this.section = section;
		this.price = 99.99;
		this.isbn = "Unknown";
		this.edition = "Unknown";
	}
}
