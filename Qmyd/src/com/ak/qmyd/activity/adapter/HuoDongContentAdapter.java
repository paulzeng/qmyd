package com.ak.qmyd.activity.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.ak.qmyd.bean.result.HomeResult.VenuesEventList;

/**
 * @author JGB
 * @date 2015-7-8 上午11:01:51
 */
public class HuoDongContentAdapter extends PagerAdapter {

	private List<View> views;

	private Context context;

	private List<VenuesEventList> venuesEventList;

	private String typeId;

	public HuoDongContentAdapter(List<View> views, Context context,
			List<VenuesEventList> venuesEventList, String typeId) {
		this.views = views;
		this.context = context;
		this.venuesEventList = venuesEventList;
		this.typeId = typeId;
	}

	@Override
	public int getCount() {
		return views == null ? 0 : views.size();
	}

	@Override
	public Object instantiateItem(View view, final int position) {
		View v = views.get(position);
		((ViewPager) view).addView(v, 0);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		return v;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	// PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		view.removeView(views.get(position));
	}

}
