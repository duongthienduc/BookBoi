package com.csun.bookboi;

import com.csun.bookboi.global.EmailConstants;
import com.csun.bookboi.services.email.GMailSender;
import com.csun.bookboi.utils.ThreadUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CheckOutActivity extends Activity {
	private static final String DEBUG_TAG = "CheckOutActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_out);
		registerCheckOutButton();
	}
	
	private void registerCheckOutButton() {
		final Button b = (Button) findViewById(R.id.activity_check_out_XML_button_order);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SendEmailConfirmationTask().execute("ngocchan.nguyen.61@my.csun.edu");
				b.setEnabled(false);
			}
		});
	}
	
	private void updateStatus() {
		TextView status = (TextView) findViewById(R.id.activity_check_out_XML_text_view_messages);
		status.setText("Your ordered has proccessed successfully!");
	}
	
	private void sendMail(String recipent) {
		try {   
            GMailSender sender = new GMailSender(EmailConstants.USERNAME, EmailConstants.PASSWORD);
            sender.sendMail(
            	EmailConstants.SUBJECT,
                "This is Body",   
                EmailConstants.USERNAME,
                "ngocchan.nguyen.61@my.csun.edu");
        } catch (Exception e) {   
            Log.e(DEBUG_TAG, e.getMessage(), e);   
        } 
	}
	
	private class SendEmailConfirmationTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog pd;
		
		@Override 
		protected void onPreExecute() {
			pd = new ProgressDialog(CheckOutActivity.this);
			pd.setMessage("Processing your order, please wait...");
			pd.setIndeterminate(true);
			pd.setCancelable(false);
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(String... recipients) {
			sendMail(recipients[0]);
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			updateStatus();
		}
	}
}
