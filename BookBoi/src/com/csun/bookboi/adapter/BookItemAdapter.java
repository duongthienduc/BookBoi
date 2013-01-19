package com.csun.bookboi.adapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.Inflater;

import com.csun.bookboi.R;
import com.csun.bookboi.types.Book;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookItemAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Book> books;
	private AtomicBoolean keepOnAppending = new AtomicBoolean(true);
	
	static class ViewHolder {
		ImageView bookCoverImageView;
		TextView bookTitleTextView;
		TextView bookAuthorTextView;
		TextView courseTextView;
		TextView priceTextView;
		ProgressBar progressBar;
		RelativeLayout bookContentLayout;
	}
	
	public BookItemAdapter(Context context, List<Book> books) {
		this.context = context;
		this.books = books;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return books.size();
	}

	public Object getItem(int index) {
		return books.get(index);
	}
	
	public long getItemId(int position) {
		return 0;
	}
	
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.view_book_item, parent, false);
			holder = new ViewHolder();
			holder.bookContentLayout = (RelativeLayout) convertView.findViewById(R.id.view_book_item_XML_relative_layout_book_content);
			holder.bookCoverImageView = (ImageView) convertView.findViewById(R.id.view_book_item_XML_image_view_book_cover);
			holder.bookTitleTextView = (TextView) convertView.findViewById(R.id.view_book_item_XML_text_view_book_title);
			holder.bookAuthorTextView = (TextView) convertView.findViewById(R.id.view_book_item_XML_text_view_book_author);
			holder.courseTextView = (TextView) convertView.findViewById(R.id.view_book_item_XML_text_view_book_course);
			holder.priceTextView = (TextView) convertView.findViewById(R.id.view_book_item_XML_text_view_book_price);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.view_book_item_XML_progress_bar_spinner);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (isLastItem(index)) {
			holder.bookContentLayout.setVisibility(View.GONE);
			holder.progressBar.setVisibility(View.VISIBLE);
		} else {
			holder.bookContentLayout.setVisibility(View.VISIBLE);
			holder.progressBar.setVisibility(View.GONE);
			setContent(holder, books.get(index));
		}
		return convertView;
	} 
	
	private void setContent(ViewHolder holder, Book b) {
		holder.bookTitleTextView.setText(b.getTitle());
		holder.bookAuthorTextView.setText(b.getAuthor());
		holder.courseTextView.setText(b.getCourse());
		holder.priceTextView.setText("$" + Double.toString(b.getPrice()));
	}
	
	boolean isLastItem(int index) {
		return index == (books.size() - 1);
	}
	
	public void stopAppending() {
		keepOnAppending.set(true);
	}
	
	public void restartAppending() {
		keepOnAppending.set(false);
	}
}