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
 * @date 2015-4-18 ����12:52:58
 */
public class BitmapUtils {
	/**
	 * ����ԭͼ�ͱ䳤����Բ��ͼƬ
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
			 * ����һ��ͬ����С�Ļ���
			 */
			Canvas canvas = new Canvas(target);
			/**
			 * ���Ȼ���Բ��
			 */
			canvas.drawCircle(min / 2, min / 2, min / 2, paint);
			/**
			 * ʹ��SRC_IN���ο������˵��
			 */
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			/**
			 * ����ͼƬ
			 */
			canvas.drawBitmap(source, 0, 0, paint);
			return target;				
		}else{
			return null;
		}
		
	}
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
}
