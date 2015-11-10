package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;

public class GuideActivity extends BaseFragmentActivity implements
		OnClickListener, OnPageChangeListener {

	private ViewPager vp;
	private List<View> views;
	private ViewPagerAdapter vpAdapter;
	private LayoutInflater inflater;
	private View view2, view3, view4;
	private Bitmap bm1, bm2, bm3, bm4;
	private ImageView view1, image2, image3, image4;
	private ImageView[] dots;
	private int currentIndex;
	private boolean isFirst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFirst = getSharedPreferences("config", 0).getBoolean("isFirst", true);
		if (isFirst == false) {
			startActivity(HomeActivity.class, null);
		} else {
			setContentView(R.layout.activity_guide);
			inflater = LayoutInflater.from(this);
			SharedPreferences preferences = this.getSharedPreferences(
					"first_pref", Context.MODE_PRIVATE);
			if (!preferences.getBoolean("isFirst", true)) {
				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
			setContentView(R.layout.activity_guide);
			inflater = LayoutInflater.from(this);

			bm1 = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.guide01);
			bm2 = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.guide02);
			bm3 = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.guide03);
			bm4 = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.guide04);
			initViews();
		}
	}

	private void initViews() {
		views = new ArrayList<View>();
		view1 = (ImageView) inflater.inflate(R.layout.views_one, null);
		view1.setImageBitmap(bm1);
		view1.setScaleType(ScaleType.FIT_XY);
		view2 = inflater.inflate(R.layout.views_two, null);
		image2 = (ImageView) view2.findViewById(R.id.second_image);
		image2.setImageBitmap(bm2);
		view3 = inflater.inflate(R.layout.views_three, null);
		image3 = (ImageView) view3.findViewById(R.id.third_image);
		image3.setImageBitmap(bm3);
		view4 = inflater.inflate(R.layout.views_four, null);
		image4 = (ImageView) view4.findViewById(R.id.forth_image);
		image4.setImageBitmap(bm4);

		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		vpAdapter = new ViewPagerAdapter(views, this);

		vp = (ViewPager) findViewById(R.id.viewpager);
		// vp.setPageTransformer(true, new DepthPageTransformer());
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);
		initDots();
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);
	}

	private void setCurDot(int positon) {
		if (positon < 0 || positon > views.size() - 1
				|| currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	private void setCurView(int position) {
		if (position < 0 || position >= views.size()) {
			return;
		}

		vp.setCurrentItem(position);
	}

	public class ViewPagerAdapter extends PagerAdapter {

		private List<View> views;
		private Activity activity;

		public ViewPagerAdapter(List<View> views, Activity activity) {
			this.views = views;
			this.activity = activity;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public int getCount() {

			if (views != null) {
				return views.size();
			}

			return 0;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			if (arg1 == 0) {
			}
			if (arg1 == views.size() - 1) {
				Button mStart = (Button) arg0.findViewById(R.id.mstart);

				mStart.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						goHome();
					}
				});
			}
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		public void goHome() {
			Intent intent = new Intent(activity, ChooseColumnActivity.class);
			activity.startActivity(intent);
			overridePendingTransition(R.anim.in_form_left, R.anim.out_of_right);
			activity.finish();
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setCurDot(arg0);
	}

	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// view1.setImageBitmap(null);
	// image2.setImageBitmap(null);
	// image3.setImageBitmap(null);
	// image4.setImageBitmap(null);
	// bm1.recycle();
	// bm2.recycle();
	// bm3.recycle();
	// bm4.recycle();
	// super.onDestroy();
	// }

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}
}
