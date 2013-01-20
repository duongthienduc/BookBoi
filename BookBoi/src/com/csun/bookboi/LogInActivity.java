package com.csun.bookboi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.utils.JSONUtil;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * Login Screen
 * @author chan
 */
public class LogInActivity extends Activity {
	private final String DEBUG_TAG = "MainActivity";
	private final String LOGIN_URL = "http://bookboi.com/chan/login.php";

	private final int DIALOG_LOGIN_ERROR = 0;
	private final int DIALOG_NETWORK_ERROR = 1;
	
	private EditText mUserEditText;
	private EditText mPasswordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setUpUi();
		prepareLogin();
	}

	private void setUpUi() {
		mUserEditText = (EditText) findViewById(R.id.login_xml_edittext_email_id);
		mPasswordEditText = (EditText) findViewById(R.id.login_xml_edittext_password_id);
	}

	private void prepareLogin() {
		if (isNetworkAvailableAndConnected()) {
			findViewById(R.id.login_xml_button_login).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						onPerformLogin();
					}
			});
		} else {
			// TODO: Use DialogFragment
			showDialog(DIALOG_NETWORK_ERROR);
		}
	}

	private void onPerformLogin() {
		if (!TextUtils.isEmpty(mUserEditText.getText()) && !TextUtils.isEmpty(mPasswordEditText.getText())) {
			String username = mUserEditText.getText().toString();
			String password = mPasswordEditText.getText().toString();
			List<NameValuePair> credential = new ArrayList<NameValuePair>();
			credential.add(new BasicNameValuePair("username", username));
			credential.add(new BasicNameValuePair("password", password));
			new UserLogInTask(credential).execute(LOGIN_URL);
		}
	}

	private class UserLogInTask extends AsyncTask<String, Void, InputStream> {
		private List<NameValuePair> credential;
		private final ProgressDialog progressDialog;

		public UserLogInTask(List<NameValuePair> credential) {
			this.credential = credential;
			progressDialog = new ProgressDialog(LogInActivity.this);
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
		protected InputStream doInBackground(String... params) {
			InputStream input = null;
			if (!isCancelled()) {
				input = RESTUtil.post(params[0], credential);
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
							LogInActivity.this.startActivity(new Intent(LogInActivity.this.getApplicationContext(), MainMenuActivity.class));
							LogInActivity.this.finish();
						}
					} catch (JSONException e) {
						Log.v(DEBUG_TAG, "Exception has occured while parsing JSON" + e);
					}
				} else {
					// TODO: Use DialogFragment
					showDialog(DIALOG_LOGIN_ERROR);
				}
			}
		}
	}

	private boolean isNetworkAvailableAndConnected() {
		ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		} else if (!networkInfo.isConnected()) {
			return false;
		} else if (!networkInfo.isAvailable()) {
			return false;
		}
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_LOGIN_ERROR:
				return new 
					AlertDialog.Builder(this)
						.setIcon(R.drawable.error_circle)
						.setTitle("Error Message")
						.setMessage("Invalid Username/Password.\n Please try again.")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
							}
						}).create();
			
			case DIALOG_NETWORK_ERROR: 
				return new 
						AlertDialog.Builder(this)
							.setIcon(R.drawable.error_circle)
							.setTitle("Error network connection")
							.setMessage("Please turn on your network connection and try again!")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									
								}
							}).create();
			}
		return null;
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.activity_main, menu); return true; }
	 */
}
