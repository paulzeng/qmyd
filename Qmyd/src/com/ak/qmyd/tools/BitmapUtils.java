package com.ak.qmyd.tools;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

/** 
 * @author HM
 * @date 2015-4-18 下午12:52:58
 */
public class BitmapUtils {
	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	public static Bitmap createCircleImage(Bitmap source)
	{
		if(source!=null){
			int min=source.getWidth();
			int height=source.getHeight();
			
			Log.i("ceshi", "min"+min);
			Log.i("ceshi", "height"+height);
			if(min>height){
				min=height;
			}
			Log.i("ceshi", "min1"+min);
			final Paint paint = new Paint();
			paint.setAntiAlias(true);
			Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
			/**
			 * 产生一个同样大小的画布
			 */
			Canvas canvas = new Canvas(target);
			/**
			 * 首先绘制圆形
			 */
			canvas.drawCircle(min / 2, min / 2, min / 2, paint);
			/**
			 * 使用SRC_IN，参考上面的说明
			 */
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			/**
			 * 绘制图片
			 */
			canvas.drawBitmap(source, 0, 0, paint);
			return target;				
		}else{
			return null;
		}
		
	}
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
}
