package com.csun.bookboi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {
	private static final String DEBUG_TAG = MainMenuActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		registerBuyButton();
		registerSellButton();
	}
	
	private void registerSellButton() {
		Button sell = (Button) findViewById(R.id.activity_main_menu_XML_button_sell);
		sell.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	private void registerBuyButton() {
		Button buy = (Button) findViewById(R.id.activity_main_menu_XML_button_buy);
		buy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), BuyActivity.class));
			}
		});
	}
}
