package com.csun.bookboi;

import com.csun.bookboi.utils.GoogleBookUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ConsoleTestingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_console);
		String content = GoogleBookUtil.buildSearchQueryFromISBN("0735619670");
		// testWhat(content);
		content = "978-0132838733";
		String another = content.substring(0, 2) + content.substring(4);
		testWhat(another);
	}
	
	private void testWhat(String content) {
		TextView t = (TextView) findViewById(R.id.activity_console_XML_text_view);
		t.setText(content);
	}
}
