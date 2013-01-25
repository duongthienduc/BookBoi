package com.csun.bookboi.adapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.Inflater;

import com.csun.bookboi.R;
import com.csun.bookboi.imagehelper.ExtendedImageDownloader;
import com.csun.bookboi.types.Book;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
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
	private ImageLoader loader;
	private AtomicBoolean keepOnAppending = new AtomicBoolean(true);
	private DisplayImageOptions options;
	private ViewType viewType;

	public enum ViewType {
		GRID_VIEW, LIST_VIEW;
	}

	static class ViewHolder {
		ImageView bookCoverImageView;
		TextView bookTitleTextView;
		TextView bookAuthorTextView;
		TextView courseTextView;
		TextView priceTextView;
		ProgressBar progressBar;
		RelativeLayout bookContentLayout;
	}

	static class GridViewHolder {
		ImageView cover;
		TextView title;
	}

	public BookItemAdapter(Context context, List<Book> books, ViewType viewType) {
		this.context = context;
		this.books = books;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.viewType = viewType;

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_book)
				.showStubImage(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
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
		if (viewType == ViewType.LIST_VIEW) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.view_book_item, parent,
						false);
				holder = new ViewHolder();
				holder.bookContentLayout = (RelativeLayout) convertView
						.findViewById(R.id.view_book_item_XML_relative_layout_book_content);
				holder.bookCoverImageView = (ImageView) convertView
						.findViewById(R.id.view_book_item_XML_image_view_book_cover);
				holder.bookTitleTextView = (TextView) convertView
						.findViewById(R.id.view_book_item_XML_text_view_book_title);
				holder.bookAuthorTextView = (TextView) convertView
						.findViewById(R.id.view_book_item_XML_text_view_book_author);
				holder.courseTextView = (TextView) convertView
						.findViewById(R.id.view_book_item_XML_text_view_book_course);
				holder.priceTextView = (TextView) convertView
						.findViewById(R.id.view_book_item_XML_text_view_book_price);
				holder.progressBar = (ProgressBar) convertView
						.findViewById(R.id.view_book_item_XML_progress_bar_spinner);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (isLastItem(index) && !keepOnAppending.get()) {
				holder.bookContentLayout.setVisibility(View.GONE);
				holder.progressBar.setVisibility(View.VISIBLE);
			} else {
				holder.bookContentLayout.setVisibility(View.VISIBLE);
				holder.progressBar.setVisibility(View.GONE);
				setContent(holder, books.get(index));
			}
		} else {
			GridViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.view_book_item_grid, parent, false);
				holder = new GridViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.view_book_item_grid_XML_textview_title);
				holder.cover = (ImageView) convertView.findViewById(R.id.view_book_item_grid_XML_imageview_cover);
				convertView.setTag(holder);
			} else {
				holder = (GridViewHolder) convertView.getTag();
			}
			setContentGrid(holder, books.get(index));
		}
		return convertView;
	}

	private void setContent(ViewHolder holder, Book b) {
		holder.bookTitleTextView.setText(b.getTitle());
		holder.bookAuthorTextView.setText(b.getAuthor());
		holder.courseTextView.setText(b.getCourse());
		holder.priceTextView.setText("$" + Double.toString(b.getPrice()));
		if (!b.getCoverUrl().equals(Book.INITIALIZE_STATE_STRING) && !b.getCoverUrl().equals("")) {
			ImageLoader.getInstance().displayImage(b.getCoverUrl(), holder.bookCoverImageView, options);
		}
	}
	
	private void setContentGrid(GridViewHolder holder, Book b) {
		holder.title.setText(b.getTitle());
		if (!b.getCoverUrl().equals(Book.INITIALIZE_STATE_STRING) && !b.getCoverUrl().equals("")) {
			ImageLoader.getInstance().displayImage(b.getCoverUrl(), holder.cover, options);
		}	
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
	
	public void setCurrentViewType(ViewType viewType) {
		this.viewType = viewType;
	}
}