package com.csun.bookboi;

import com.csun.bookboi.global.EmailConstants;
import com.csun.bookboi.services.email.GMailSender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class EmailActivity extends Activity {
	private static final String DEBUG_TAG = EmailActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prototype_activity_email);
		// setUpListView();
		// registerSearchButton();
		Button btn = (Button) findViewById(com.csun.bookboi.R.id.prototype_activity_email_XML_button);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendSampleMail();
			}
		});

	}

	private void sendSampleMailViaClient() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { "atbl1511@gmail.com" });
		i.putExtra(Intent.EXTRA_SUBJECT, "BookBoi Subject");
		i.putExtra(Intent.EXTRA_TEXT, "Body");
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(EmailActivity.this,
					"There are no email clients installed.",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void sendSampleMail() {
		try {   
            GMailSender sender = new GMailSender(EmailConstants.USERNAME, EmailConstants.PASSWORD);
            sender.sendMail(
            	EmailConstants.SUBJECT,
                "This is Body",   
                EmailConstants.USERNAME,
                "ngocchan.nguyen.61@my.csun.edu");
        } catch (Exception e) {   
            Log.e("SendMail", e.getMessage(), e);   
        } 
	}
}
