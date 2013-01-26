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
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
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
	private AtomicBoolean keepOnAppending = new AtomicBoolean(true);
	private DisplayImageOptions options;
	private ViewType viewType;

	public enum ViewType {
		GRID_VIEW, LIST_VIEW;
	}

	static class ListViewItemViewHolder {
		ImageView cover;
		TextView title;
		TextView author;
		TextView course;
		TextView price;
		ProgressBar progressBar;
		RelativeLayout bookContentLayout;
	}

	static class GridViewItemViewHolder {
		ImageView cover;
		TextView title;
	}

	public BookItemAdapter(Context context, List<Book> books, ViewType viewType) {
		this.context = context;
		this.books = books;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.viewType = viewType;

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_book)
				.showStubImage(R.drawable.ic_book)
				.cacheInMemory()
				.cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading()
				.build();
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
			ListViewItemViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.view_book_item, parent, false);
				holder = new ListViewItemViewHolder();
				holder.bookContentLayout = (RelativeLayout) convertView.findViewById(R.id.view_book_item_XML_relative_layout_book_content);
				holder.cover = (ImageView) convertView.findViewById(R.id.view_book_item_XML_image_view_book_cover);
				holder.title = (TextView) convertView.findViewById(R.id.view_book_item_XML_text_view_book_title);
				holder.author = (TextView) convertView.findViewById(R.id.view_book_item_XML_text_view_book_author);
				holder.course = (TextView) convertView.findViewById(R.id.view_book_item_XML_text_view_book_course);
				holder.price = (TextView) convertView.findViewById(R.id.view_book_item_XML_text_view_book_price);
				holder.progressBar = (ProgressBar) convertView.findViewById(R.id.view_book_item_XML_progress_bar_spinner);
				convertView.setTag(holder);
			} else {
				holder = (ListViewItemViewHolder) convertView.getTag();
			}

			if (isLastItem(index) && !keepOnAppending.get()) {
				holder.bookContentLayout.setVisibility(View.GONE);
				holder.progressBar.setVisibility(View.VISIBLE);
			} else {
				holder.bookContentLayout.setVisibility(View.VISIBLE);
				holder.progressBar.setVisibility(View.GONE);
				setItemListContent(holder, books.get(index));
			}
		} else {
			GridViewItemViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.view_book_item_grid, parent, false);
				holder = new GridViewItemViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.view_book_item_grid_XML_textview_title);
				holder.cover = (ImageView) convertView.findViewById(R.id.view_book_item_grid_XML_imageview_cover);
				convertView.setTag(holder);
			} else {
				holder = (GridViewItemViewHolder) convertView.getTag();
			}
			setItemGridContent(holder, books.get(index));
		}
		return convertView;
	}

	private void setItemListContent(ListViewItemViewHolder holder, Book b) {
		holder.title.setText(b.getTitle());
		holder.author.setText(b.getAuthor());
		holder.course.setText(b.getCourse());
		holder.price.setText("$" + Double.toString(b.getPrice()));
		if (!b.getCoverUrl().equals(Book.INITIALIZE_STATE_STRING) && !b.getCoverUrl().equals("")) {
			ImageLoader.getInstance().displayImage(b.getCoverUrl(), holder.cover, options);
		}
	}
	
	private void setItemGridContent(GridViewItemViewHolder holder, Book b) {
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