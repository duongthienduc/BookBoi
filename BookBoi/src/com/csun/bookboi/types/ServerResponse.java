package com.csun.bookboi.types;

import android.os.Parcel;
import android.os.Parcelable;

public class ServerResponse implements BookBoiType, Parcelable {
	private boolean result;
	
	public ServerResponse() {
		result = false;
	}
	
	public void setResult(boolean r) {
		this.result = r;
	}
	
	public boolean getResult() {
		return result;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
