package com.csun.bookboi.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.csun.bookboi.R;

public class NotFoundDialog extends DialogFragment {
	public NotFoundDialog() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(com.csun.bookboi.R.layout.dialog_search_not_found, container);
		Button ok = (Button) view.findViewById(R.id.dialog_search_not_found_XML_button_ok);
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
