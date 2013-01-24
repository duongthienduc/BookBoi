package com.csun.bookboi;

import com.csun.bookboi.imagehelper.ExtendedImageDownloader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;

public class BookBoiApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(2 * 1024 * 1024) // 2 Mb
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.imageDownloader(new ExtendedImageDownloader(getApplicationContext()))
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() 
				.build();
		ImageLoader.getInstance().init(config);
	}
}
