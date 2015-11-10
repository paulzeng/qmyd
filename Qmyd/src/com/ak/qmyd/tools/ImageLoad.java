package com.ak.qmyd.tools;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ImageLoad {
	
	public static void initImageLoader(Context context) {

		// 使用教程 http://blog.csdn.net/vipzjyno1/article/details/23206387
		int size = (int) (Runtime.getRuntime().maxMemory() * (20 / 100.0F));
		DisplayImageOptions mDisplayImageOption = new DisplayImageOptions.Builder()
				.cacheInMemory(true)// 设置内存缓存
				.cacheOnDisc(true)// 设置硬盘缓存
				.bitmapConfig(Bitmap.Config.RGB_565)// 图片解码
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 图像将被二次采样的整数倍
				.considerExifParams(true)// 考虑旋转
				.build();

		ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 1)
				//
				.threadPoolSize(4)
				// 线程池大小
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// 排队
				.memoryCacheSize(size)
				// 内存
				.discCacheSize(52428800)
				// 闪存
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.defaultDisplayImageOptions(mDisplayImageOption).build();
		ImageLoader.getInstance().init(imageLoaderConfig);
	}

	public static void initImageLoader2(Context context) {
		DisplayImageOptions mDisplayImageOption = new DisplayImageOptions.Builder()
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 1)
				.threadPoolSize(4)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.memoryCacheSize(1)
				.defaultDisplayImageOptions(mDisplayImageOption).build();
		ImageLoader.getInstance().init(imageLoaderConfig);
	}

}
