package com.csun.bookboi.utils;

import android.content.Context;
import android.widget.Toast;

public class UiUtil {
	public static void showText(Context c, String msg) {
		Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
	}
}
