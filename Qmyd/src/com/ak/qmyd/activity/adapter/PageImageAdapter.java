package com.ak.qmyd.activity.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author JGB
 * @date 2015-4-23 下午12:41:28
 */
public class PageImageAdapter extends PagerAdapter {

	private List<ImageView> images;
	public PageImageAdapter(List<ImageView> images){
		this.images=images;
	}
	@Override
	public int getCount() {
		return images==null?0:images.size();
	}

	@Override
	public Object instantiateItem(View view, int position) {
		ImageView iv = images.get(position % images.size());
		((ViewPager) view).addView(iv, 0);
		return iv;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	// PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		view.removeView(images.get(position % images.size()));
	}

}
