package com.csun.bookboi.scan;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public final class ScanIntent {
	public static final String INTENT_ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	public static final String INTENT_EXTRA_SCAN_MODE = "SCAN_MODE";
	public static final String INTENT_EXTRA_PRODUCT_MODE = "PRODUCT_MODE";
	public static final String SCAN_RESULT = "SCAN_RESULT";
	public static final String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";
	public static final String FORMAT_EAN_13 = "EAN_13";

	private ScanIntent() {
		
	}

	public static boolean isInstalled(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(INTENT_ACTION_SCAN);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
}
