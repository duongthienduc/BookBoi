package com.csun.bookboi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.database.UserDatabaseHelper;
import com.csun.bookboi.dialogs.NetworkErrorDialog;
import com.csun.bookboi.services.SingletonUser;
import com.csun.bookboi.utils.Pair;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.NetworktUtil;
import com.csun.bookboi.utils.RESTUtil;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Login Screen
 * @author chan
 */
public class LogInActivity extends BookBoiBaseActivity {
	private static final String DEBUG_TAG = LogInActivity.class.getSimpleName();
	
	private String activeUsername;
	private String activePassword;
	private LogInTask task = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		prepareLogin();
		prepareSignUp();
	}
	
	private void prepareSignUp() {
		Button signup = (Button) findViewById(R.id.activity_login_XML_button_signup);
		signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LogInActivity.this.getApplicationContext(), SignUpActivity.class));
			}
		});
	}

	private void prepareLogin() {
		Button login = (Button) findViewById(R.id.activity_login_XML_button_login);
		if (NetworktUtil.haveNetworkConnection(LogInActivity.this)) {
			login.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						onPerformLogin();
					}
			});
		} else {
			showNetworkErrorDialog(new Throwable());
		}
	}
	
	/**
	 * Make sure the current task is not running 
	 * @return
	 * 			true if either the task have 
	 * 			not started or already finished
	 */
	private boolean haveTaskAvailable() {
        return ((task == null) || (task != null && task.getStatus() == AsyncTask.Status.FINISHED));
	}
	
	private void onPerformLogin() {
		EditText usernameEdit = (EditText) findViewById(R.id.activity_login_XML_edittext_email_id);
		EditText passwordEdit = (EditText) findViewById(R.id.activity_login_XML_edittext_password_id);
		if (!TextUtils.isEmpty(usernameEdit.getText()) && !TextUtils.isEmpty(passwordEdit.getText())) {
			String username = usernameEdit.getText().toString();
			String password = passwordEdit.getText().toString();
			
			// TODO: parse a complete user from server. 
			// This is dirty hack!
			activeUsername = username;
			activePassword = password;
			
			if (haveTaskAvailable()) {
				// TODO: add logic for validate username/password
				task = new LogInTask(UserDatabaseHelper.buildLogInQuery(username, password));
				task.execute();
			}
		}
	}

	private class LogInTask extends AsyncTask<Void, Void, InputStream> {
		private String url;
		private List<NameValuePair> credential;
		private final ProgressDialog progressDialog;

		public LogInTask(Pair<String, List<NameValuePair>> extras) {
			url = extras.first();
			credential = extras.second();
			progressDialog = new ProgressDialog(LogInActivity.this);
			progressDialog.setMessage("Logging in...");
			progressDialog.setCancelable(true);
			progressDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
		}

		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}

		@Override
		protected InputStream doInBackground(Void... params) {
			InputStream input = null;
			if (!isCancelled()) {
				input = RESTUtil.post(url, credential);
			}
			return input;
		}

		@Override
		protected void onPostExecute(InputStream input) {
			progressDialog.dismiss();
			if (input != null) {
				JSONObject result = JSONUtil.buildObject(input);
				if (result != null && result.has("id")) {
					try {
						int id = result.getInt("id");
						if (id != 0) {
							Log.v(DEBUG_TAG, "What the fuck?");
							SingletonUser.setActiveUser(id, activeUsername, activePassword);
							LogInActivity.this.startActivity(new Intent(LogInActivity.this.getApplicationContext(), MainMenuActivity.class));
							LogInActivity.this.finish();
						}
					} catch (JSONException e) {
						Log.e(DEBUG_TAG, "Exception has occured while parsing JSON", e);
					}
				} else {
					showDialogLogInError();
				}
			}
		}
	}
	
	private void showDialogLogInError() {
		FragmentManager fm = getSupportFragmentManager();
	    NetworkErrorDialog d = new NetworkErrorDialog();
	    d.show(fm, "Login Error");
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.activity_main, menu); return true; }
	 */
}
