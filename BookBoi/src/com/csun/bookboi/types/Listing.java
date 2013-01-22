package com.csun.bookboi.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Listing implements BookBoiType, Parcelable {
	public static final int INITIALIZE_STATE_INT = -1;
	public static final double INITIALIZE_STATE_DOUBLE = 0.0;
	public static final String INITIALIZE_STATE_STRING = "unknown";
	
	private int id;
	private double price;
	private String condition;
	private String status;
	private User user;
	private Book book;
	
	public static final Parcelable.Creator<Listing> CREATOR = new Parcelable.Creator<Listing>() {
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override 
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };

	public Listing() {
		id = INITIALIZE_STATE_INT;
		price = INITIALIZE_STATE_DOUBLE;
		condition = INITIALIZE_STATE_STRING;
		status = INITIALIZE_STATE_STRING;
		user = new User();
		book = new Book();
	}
	
	public Listing(Parcel in) {
		readFromParcel(in);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeDouble(price);
		out.writeString(condition);
		out.writeString(status);
		user.writeToParcel(out, flags);
		book.writeToParcel(out, flags);
	}
	
	public void readFromParcel(Parcel in) {
		id = in.readInt();
		price = in.readDouble();
		condition = in.readString();
		status = in.readString();
		user.readFromParcel(in);
		book.readFromParcel(in);
	}

}
