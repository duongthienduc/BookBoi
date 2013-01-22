package com.csun.bookboi.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements BookBoiType, Parcelable {
	public static final String INITIALIZE_STATE_STRING = "unknown";
	public static final int INITIALIZE_STATE_INT = -1;
	public static final double INITIALIZE_STATE_DOUBLE = 0.99;
	
	private int id;
	private String title;
	private String author;
	private String course;
	private String section;

	private double price;
	private String isbn;
	private String edition;
	private String coverUrl;
	
	public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override 
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    
	public static Book makeBook(int id, String title, String author, String course, String section, double price, String isbn, String edition, String coverUrl) {
		Book b = new Book();
		b.setId(id);
		b.setTitle(title);
		b.setAuthor(author);
		b.setSection(section);
		b.setCourse(course);
		b.setSection(section);
		b.setPrice(price);
		b.setEdition(edition);
		b.setCoverUrl(coverUrl);
		return b;
	}
	
	public static class Builder {
		private int id;
		private String title;
		private String author;
		private String course;
		private String section;

		private double price;
		private String isbn;
		private String edition;
		private String coverUrl;

		public Builder() {
			id = INITIALIZE_STATE_INT;
			title = INITIALIZE_STATE_STRING;
			author = INITIALIZE_STATE_STRING;
			course = INITIALIZE_STATE_STRING;
			section = INITIALIZE_STATE_STRING;
			isbn = INITIALIZE_STATE_STRING;
			edition = INITIALIZE_STATE_STRING;
			coverUrl = INITIALIZE_STATE_STRING;
			price = INITIALIZE_STATE_DOUBLE;
		}

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder author(String author) {
			this.author = author;
			return this;
		}

		public Builder course(String course) {
			this.course = course;
			return this;
		}

		public Builder section(String section) {
			this.section = section;
			return this;
		}

		public Builder edition(String edition) {
			this.edition = edition;
			return this;
		}
		
		public Builder isbn(String isbn) {
			this.isbn = isbn;
			return this;
		}
		
		public Builder price(double price) {
			this.price = price;
			return this;
		}

		public Builder coverUrl(String coverUrl) {
			this.coverUrl = coverUrl;
			return this;
		}

		public Book build() {
			return new Book(this);
		}
	}

	public Book(Builder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.author = builder.author;
		this.course = builder.course;
		this.section = builder.section;
		this.isbn = builder.isbn;
		this.edition = builder.edition;
		this.coverUrl = builder.coverUrl;
		this.price = builder.price;
	}
	
	/**
	 * Constructor
	 * Initialize data to a valid state
	 */
	public Book() {
		id = INITIALIZE_STATE_INT;
		title = INITIALIZE_STATE_STRING;
		author = INITIALIZE_STATE_STRING;
		course = INITIALIZE_STATE_STRING;
		section = INITIALIZE_STATE_STRING;
		isbn = INITIALIZE_STATE_STRING;
		edition = INITIALIZE_STATE_STRING;
		coverUrl = INITIALIZE_STATE_STRING;
		
		price = INITIALIZE_STATE_DOUBLE;
	}
	
	public Book makeCopy() {
		Book b = new Book();
		b.setId(id);
		b.setTitle(title);
		b.setAuthor(author);
		b.setSection(section);
		b.setCourse(course);
		b.setSection(section);
		b.setPrice(price);
		b.setEdition(edition);
		b.setCoverUrl(coverUrl);
		return b;
	}
	
	public Book(Parcel in) {
		readFromParcel(in);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
		out.writeInt(id);
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
		id = in.readInt();
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
