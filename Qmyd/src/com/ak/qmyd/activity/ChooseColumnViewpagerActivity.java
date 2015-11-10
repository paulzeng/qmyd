package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;

public class ChooseColumnViewpagerActivity extends BaseActivity implements
		OnClickListener, OnPageChangeListener {
	private ViewPager vp;
	private Button btnChoose;
	private List<View> views;
	private ImageView[] dots;
	private int currentIndex;
	private ViewPagerAdapter vpAdapter;
	private LayoutInflater inflater;
	private LinearLayout ll_column01, ll_column02;
	private TextView txtAdd;
    private SharedPreferences sp;
	private SharedPreferences.Editor edit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_column_viewpager);
		sp = getSharedPreferences("config", 0);
		edit = sp.edit();
		inflater = LayoutInflater.from(this);
		initView();
	}

	void initView() {
		btnChoose = (Button) this.findViewById(R.id.btnChoose);
		btnChoose.setOnClickListener(this);
		txtAdd = (TextView) this.findViewById(R.id.txtAdd);
		txtAdd.setOnClickListener(this);
		views = new ArrayList<View>();
		ll_column01 = (LinearLayout) inflater.inflate(R.layout.view_column01,
				null);
		ll_column02 = (LinearLayout) inflater.inflate(R.layout.view_column02,
				null);
		views.add(ll_column01);
		views.add(ll_column02);
		vpAdapter = new ViewPagerAdapter(views, this);

		vp = (ViewPager) findViewById(R.id.vpagerColumn);
		// vp.setPageTransformer(true, new DepthPageTransformer());
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);
		initDots();
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

			}
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btnChoose:
			Intent intent = new Intent(this, HomeActivity.class);
			edit.putBoolean("isFirst", false);
			edit.commit();
			startActivity(intent);
			overridePendingTransition(R.anim.in_form_left, R.anim.out_of_right);
			setGuided();
			finish();
			break;
		case R.id.txtAdd:
			Intent intent2 = new Intent(this, HomeActivity.class);
			edit.putBoolean("isFirst", false);
			edit.commit();
			startActivity(intent2);
			overridePendingTransition(R.anim.in_form_left, R.anim.out_of_right);
			setGuided();
			finish();
			break;
		}
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll_dot);

		dots = new ImageView[views.size()];

		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true); 
			dots[i].setOnClickListener(this);
			dots[i].setTag(i); 
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false); 
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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub 
		setCurDot(arg0);
	}

	public void setGuided() {
		SharedPreferences preferences = this.getSharedPreferences("first_pref",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirst", false);
		editor.commit();
	}
}
