package com.csun.bookboi;

import java.io.InputStream;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.database.UserDatabaseHelper;
import com.csun.bookboi.parsers.ServerResponseParser;
import com.csun.bookboi.types.ServerResponse;
import com.csun.bookboi.utils.JSONUtil;
import com.csun.bookboi.utils.RESTUtil;
import com.csun.bookboi.utils.UiUtil;
import com.csun.bookboi.validation.InputValidator;
import com.csun.bookboi.validation.PasswordValidator;
import com.csun.bookboi.validation.UsernameValidator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.csun.bookboi.utils.Pair;

public class SignUpActivity extends BookBoiBaseActivity {
	private static final String DEBUG_TAG = SignUpActivity.class.getSimpleName();
	
	private SignUpTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		setUpViews();
	}
	
	private boolean validateInput(String username, String password, String confirm) {
		InputValidator usernameValidator = new UsernameValidator();
		InputValidator passwordValidator = new PasswordValidator();
		if (usernameValidator.validate(username) && passwordValidator.validate(password) && password.equals(confirm)) {
			return true;
		} 
		return false;
	}
	
	private void setUpViews() {
		final EditText username = (EditText) findViewById(R.id.activity_signup_XML_edittext_email_id); 
		final EditText password = (EditText) findViewById(R.id.activity_signup_XML_edittext_password_id); 
		final EditText confirm = (EditText) findViewById(R.id.activity_signup_XML_edittext_confirmpassword_id); 
		final Button signup = (Button) findViewById(R.id.activity_signup_XML_button_signup);
		signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateInput(username.getText().toString(), password.getText().toString(), confirm.getText().toString())) {
					launchSignUpTask(username.getText().toString(), password.getText().toString());
				} else {
					// dirty solution
					UiUtil.showText(SignUpActivity.this, "Something went wrong with sign-up");
				}
			}
		});
	}
	
	private boolean hasTaskAvailable() {
        return ((task == null) || (task != null && task.getStatus() == AsyncTask.Status.FINISHED));
	}
	
	private void launchSignUpTask(String username, String password) {
		if (hasTaskAvailable()) {
			task = new SignUpTask(UserDatabaseHelper.buildSignUpQuery(username, password));
			task.execute();
		}
	}
	
	private class SignUpTask extends AsyncTask<Void, Void, ServerResponse> {
		private String url;
		private List<NameValuePair> post;
		private final ProgressDialog progressDialog;

		public SignUpTask(Pair<String, List<NameValuePair>> extras) {
			url = extras.first();
			post = extras.second();
			progressDialog = new ProgressDialog(SignUpActivity.this);
			progressDialog.setCancelable(false);
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
		protected ServerResponse doInBackground(Void... params) {
			ServerResponse r = new ServerResponse();
			InputStream input = null;
			if (!isCancelled()) {
				input = RESTUtil.post(url, post);
				try {
					r = new ServerResponseParser().parse(JSONUtil.buildObject(input));
				} catch (JSONException e) {
					Log.e(DEBUG_TAG, "Parsing JSON error", e);
				}
			}
			return r;
		}

		@Override
		protected void onPostExecute(ServerResponse r) {
			progressDialog.dismiss();
			if (r.getResult() == true) {
				UiUtil.showText(SignUpActivity.this, "Congratulations!");
			}
		}
	}
}
