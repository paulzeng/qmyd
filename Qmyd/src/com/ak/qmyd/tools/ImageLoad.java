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

		// ʹ�ý̳� http://blog.csdn.net/vipzjyno1/article/details/23206387
		int size = (int) (Runtime.getRuntime().maxMemory() * (20 / 100.0F));
		DisplayImageOptions mDisplayImageOption = new DisplayImageOptions.Builder()
				.cacheInMemory(true)// �����ڴ滺��
				.cacheOnDisc(true)// ����Ӳ�̻���
				.bitmapConfig(Bitmap.Config.RGB_565)// ͼƬ����
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// ͼ�񽫱����β�����������
				.considerExifParams(true)// ������ת
				.build();

		ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 1)
				//
				.threadPoolSize(4)
				// �̳߳ش�С
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// �Ŷ�
				.memoryCacheSize(size)
				// �ڴ�
				.discCacheSize(52428800)
				// ����
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
