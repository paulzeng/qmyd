package com.ak.qmyd.activity.adapter;

import java.io.Serializable;
import java.util.List;

import com.ak.qmyd.activity.VenuesAppointmentActivity;
import com.ak.qmyd.activity.VenuesDetailsActivity;
import com.ak.qmyd.bean.model.Venues;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


/** 
 * @author HM
 * @date 2015-4-27 下午4:55:58
 */
public class ContentAdapter extends PagerAdapter {
	private List<View> views;
	
	private Context context;
	
	private List<Venues> venues;
	
	private String typeId;

	public ContentAdapter( List<View> views,Context context, List<Venues> venues,String typeId){
		this.views=views;
		this.context=context;	
		this.venues=venues;
		this.typeId=typeId;
	}
	@Override
	public int getCount() {
		return views==null?0:views.size();
	}

	@Override
	public Object instantiateItem(View view, final int position) {
		View v = views.get(position);
		((ViewPager) view).addView(v, 0);
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(context,VenuesDetailsActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("supportId", venues.get(position).getSupportId());
				bundle.putString("venuesId", venues.get(position).getVenuesId());
				bundle.putString("typeId", typeId);
				bundle.putSerializable("venues", (Serializable)venues.get(position));
				intent.putExtras(bundle);
				context.startActivity(intent);
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



