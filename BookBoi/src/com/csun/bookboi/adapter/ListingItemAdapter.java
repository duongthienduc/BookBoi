package com.csun.bookboi.adapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csun.bookboi.R;
import com.csun.bookboi.types.Listing;
import com.csun.bookboi.utils.UiUtil;

public class ListingItemAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Listing> listings;
	private AtomicBoolean keepOnAppending = new AtomicBoolean(true);
	
	static class ViewHolder {
		TextView seller;
		TextView price;
		TextView condition;
		TextView rating;
		RatingBar ratingBar;
		ProgressBar progress;
		RelativeLayout contentLayout;
		RelativeLayout progressLayout;
	}
	
	public ListingItemAdapter(Context context, List<Listing> listings) {
		this.context = context;
		this.listings = listings;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return listings.size();
	}

	public Object getItem(int index) {
		return listings.get(index);
	}
	
	public long getItemId(int position) {
		return 0;
	}
	
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.view_listing_item, parent, false);
			holder = new ViewHolder();
			holder.seller = (TextView) convertView.findViewById(R.id.view_listing_item_XML_seller);
			holder.price = (TextView) convertView.findViewById(R.id.view_listing_item_XML_price);
			holder.condition = (TextView) convertView.findViewById(R.id.view_listing_item_XML_condition);
			holder.rating = (TextView) convertView.findViewById(R.id.view_listing_item_XML_rating);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.view_listing_item_XML_rating_bar);
			holder.progress = (ProgressBar) convertView.findViewById(R.id.view_listing_item_XML_progress_bar_spinner);
			holder.progressLayout = (RelativeLayout) convertView.findViewById(R.id.view_listing_item_XML_spinner_layout);
			holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.view_listing_item_XML_layout_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (isLastItem(index) && !keepOnAppending.get()) {
			holder.contentLayout.setVisibility(View.GONE);
			holder.progress.setVisibility(View.VISIBLE);
		} else {
			holder.contentLayout.setVisibility(View.VISIBLE);
			holder.progress.setVisibility(View.GONE);
			setContent(holder, listings.get(index));
		}
		return convertView;
	} 
	
	private void setContent(ViewHolder holder, Listing l) {
		holder.seller.setText(l.getUser().getUsername());
		holder.price.setText("$" + l.getPrice());
		holder.condition.setText(l.getCondition());
		holder.rating.setText("100,000 ratings");
		holder.ratingBar.setRating(88.0f);
	}
	
	boolean isLastItem(int index) {
		return index == (listings.size() - 1);
	}
	
	public void stopAppending() {
		keepOnAppending.set(true);
	}
	
	public void restartAppending() {
		keepOnAppending.set(false);
	}
}
