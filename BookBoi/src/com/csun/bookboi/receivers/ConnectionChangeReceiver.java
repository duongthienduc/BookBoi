package com.csun.bookboi.receivers;

import com.csun.bookboi.dialogs.NetworkErrorDialog;
import com.csun.bookboi.utils.NetworktUtil;
import com.csun.bookboi.utils.UiUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		UiUtil.showText(context, "Network state is changed");
	}
}