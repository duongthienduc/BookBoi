package com.csun.bookboi.types;

public class Book implements BookBoiType {
	private static final String INITIALIZE_STATE_STRING = "unknown";
	private static final double INITIALIZE_STATE_DOUBLE = 0.0;
	
	private String title;
	private String author;
	private String course;
	private String section;

	private double price;
	private String isbn;
	private String edition;
	
	/**
	 * Constructor
	 * Initialize data to a valid state
	 */
	public Book() {
		title = INITIALIZE_STATE_STRING;
		author = INITIALIZE_STATE_STRING;
		course = INITIALIZE_STATE_STRING;
		section = INITIALIZE_STATE_STRING;
		isbn = INITIALIZE_STATE_STRING;
		edition = INITIALIZE_STATE_STRING;
		
		price = INITIALIZE_STATE_DOUBLE;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}
}
