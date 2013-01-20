package com.csun.bookboi.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements BookBoiType, Parcelable {
	public static final String INITIALIZE_STATE_STRING = "unknown";
	public static final double INITIALIZE_STATE_DOUBLE = 0.99;
	
	private String title;
	private String author;
	private String course;
	private String section;

	private double price;
	private String isbn;
	private String edition;
	private String coverUrl;
	
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
		coverUrl = INITIALIZE_STATE_STRING;
		
		price = INITIALIZE_STATE_DOUBLE;
	}
	
	public Book(Parcel in) {
		readFromParcel(in);
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
	
	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(title);
		out.writeString(author);
		out.writeString(course);
		out.writeString(section);
		out.writeDouble(price);
		out.writeString(isbn);
		out.writeString(edition);
		out.writeString(coverUrl);
	}
	
	public void readFromParcel(Parcel in) {
		title = in.readString();
		author = in.readString();
		course = in.readString();
		section = in.readString();
		price = in.readDouble();
		isbn = in.readString();
		edition = in.readString();
		coverUrl = in.readString();
	}

	
}
