package com.csun.bookboi;

import com.csun.bookboi.utils.GoogleBookUtil;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class ConsoleTestingActivity extends Activity {
	private static final String DEBUG_TAG = ConsoleTestingActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_console);
		testUriVsUrl();
	}
	
	private void testUriVsUrl() {
		Uri uri = Uri.parse("http://bookboi.com/chan/get_book_search_result.php");
		show(uri.toString());
	}
	
	private void testBuildSearchQueryFromISBN() {
		String content = GoogleBookUtil.buildSearchQueryFromISBN("0735619670");
		show(content);
	}
	
	private void show(String content) {
		TextView t = (TextView) findViewById(R.id.activity_console_XML_text_view);
		t.setText(content);
	}
}
