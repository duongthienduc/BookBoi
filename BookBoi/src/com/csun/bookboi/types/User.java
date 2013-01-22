package com.csun.bookboi.types;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements BookBoiType, Parcelable {
	public static final int INITIALIZE_STATE_INT = -1;
	public static final String INITIALIZE_STATE_STRING = "unknown";

	private int id;
	private String username;
	private String password;
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override 
        public User[] newArray(int size) {
            return new User[size];
        }
    };

	public User() {
		id = INITIALIZE_STATE_INT;
		username = INITIALIZE_STATE_STRING;
		password = INITIALIZE_STATE_STRING;
	}
	
	public User(Parcel in) {
		readFromParcel(in);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(username);
		out.writeString(password);
	}

	public void readFromParcel(Parcel in) {
		id = in.readInt();
		username = in.readString();
		password = in.readString();
	}
}
