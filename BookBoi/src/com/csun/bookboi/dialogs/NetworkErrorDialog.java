package com.csun.bookboi.dialogs;

import com.csun.bookboi.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NetworkErrorDialog extends DialogFragment {
	public NetworkErrorDialog() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(com.csun.bookboi.R.layout.dialog_network_error, container);
		Button ok = (Button) view.findViewById(R.id.dialog_network_error_XML_button_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickCloseDialog();
			}
		});
		return view;
	}
	
	private void onClickCloseDialog() {
		this.dismiss();
	}
}
