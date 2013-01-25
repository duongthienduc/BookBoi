package com.csun.bookboi;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.dialogs.NetworkErrorDialog;
import com.csun.bookboi.utils.GoogleBookUtil;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class BookBoiBaseActivity extends FragmentActivity {
	protected ImageLoader imageLoader;
	private Thread.UncaughtExceptionHandler handler;
	
	@Override 
	protected void onCreate(Bundle savedStateInstance) {
		super.onCreate(savedStateInstance);
		imageLoader = ImageLoader.getInstance();
		// setUpDisconnectedNetworkHandler();
	}
	
	private void setUpDisconnectedNetworkHandler() {
		handler = new Thread.UncaughtExceptionHandler() {
	        public void uncaughtException(Thread thread, Throwable e) {
	        	showNetworkErrorDialog(e);
	        }
	    };
	    Thread.setDefaultUncaughtExceptionHandler(handler);
	}
	
	public void showNetworkErrorDialog(Throwable t) {
        FragmentManager fm = getSupportFragmentManager();
        NetworkErrorDialog d = new NetworkErrorDialog();
        d.show(fm, "Disconnected");
    }
	
	public boolean haveTaskAvailable(AsyncTask<?, ?, ?> task) {
        return ((task == null) || (task != null && task.getStatus() == AsyncTask.Status.FINISHED));
	}
}
