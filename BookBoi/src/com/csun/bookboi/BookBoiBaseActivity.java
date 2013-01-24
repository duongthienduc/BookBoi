package com.csun.bookboi;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.dialogs.NetworkErrorDialog;
import com.csun.bookboi.utils.GoogleBookUtil;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class BookBoiBaseActivity extends FragmentActivity {
	private Thread.UncaughtExceptionHandler handler;
	
	@Override 
	protected void onCreate(Bundle savedStateInstance) {
		super.onCreate(savedStateInstance);
		setUpDisconnectedNetworkHandler();
	}
	
	private void setUpDisconnectedNetworkHandler() {
		handler = new Thread.UncaughtExceptionHandler() {
	        public void uncaughtException(Thread thread, Throwable e) {
	        	showNetworkErrorDialog(e);
	        }
	    };
	    Thread.setDefaultUncaughtExceptionHandler(handler);
	}
	
	private void showNetworkErrorDialog(Throwable t) {
        FragmentManager fm = getSupportFragmentManager();
        NetworkErrorDialog d = new NetworkErrorDialog();
        d.show(fm, "Disconnected");
    }
}
