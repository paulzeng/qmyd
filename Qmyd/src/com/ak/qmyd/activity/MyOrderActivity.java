package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.adapt.MyOrderAdapt;
import com.ak.qmyd.fragment.BookingFragment;
import com.ak.qmyd.fragment.ConsumptionFragment;
import com.ak.qmyd.fragment.ExpiredFragment;
import com.ak.qmyd.fragment.FinishedFragment;
import com.ak.qmyd.tools.Tools;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MyOrderActivity extends BaseFragmentActivity implements
		OnClickListener {

	private FragmentManager fm;

	private ViewPager viewPager;

	private List<Fragment> fragmentList;

	private LinearLayout linear_tab;

	private Context context;

	private TextView textView;// 滑动条

	private LinearLayout.LayoutParams lp;

	private int width;// 屏幕宽度;

	private Button btn_booking, btn_consumption, btn_finished, btn_expired;

	private ImageButton imageButton_back;

	private DrawerLayout drawerlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		context = getApplicationContext();
		initFragment();
		findView();
	}

	private void initFragment() {

		fragmentList = new ArrayList<Fragment>();
		BookingFragment bf = new BookingFragment();
		fragmentList.add(bf);
		ConsumptionFragment cf = new ConsumptionFragment();
		fragmentList.add(cf);
		FinishedFragment ff = new FinishedFragment();
		fragmentList.add(ff);
		ExpiredFragment ef = new ExpiredFragment();
		fragmentList.add(ef);

	}

	private void findView() {
		btn_booking = (Button) this.findViewById(R.id.btn_booking);
		btn_consumption = (Button) this.findViewById(R.id.btn_consumption);
		btn_finished = (Button) this.findViewById(R.id.btn_finished);
		btn_expired = (Button) this.findViewById(R.id.btn_expired);
		viewPager = (ViewPager) this.findViewById(R.id.viewPager);
		imageButton_back = (ImageButton) this
				.findViewById(R.id.imageButton_back);
		linear_tab = (LinearLayout) this.findViewById(R.id.linear_tab);
		fm = getSupportFragmentManager();
		addTextView();
		MyOrderAdapt myAdapt = new MyOrderAdapt(fm, fragmentList);
		viewPager.setAdapter(myAdapt);
		changePager();
		btn_booking.setOnClickListener(this);
		btn_consumption.setOnClickListener(this);
		btn_finished.setOnClickListener(this);
		btn_expired.setOnClickListener(this);
		imageButton_back.setOnClickListener(this);
		drawerlayout = (DrawerLayout) this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);
	}

	/**
	 * 滑动条初始化
	 * */
	private void addTextView() {
		textView = new TextView(context);
		width = Tools.getWidth(context);
		lp = new LinearLayout.LayoutParams(width / 4,
				LinearLayout.LayoutParams.MATCH_PARENT);
		textView.setBackgroundColor(getResources().getColor(
				R.color.orange_color));
		textView.setLayoutParams(lp);
		linear_tab.addView(textView);
	}

	private void changePager() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				lp.leftMargin = (int) ((width / 4) * arg0 + (width / 4) * arg1);
				textView.setLayoutParams(lp);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_booking:
			viewPager.setCurrentItem(0);
			break;
		case R.id.btn_consumption:
			viewPager.setCurrentItem(1);
			break;
		case R.id.btn_finished:
			viewPager.setCurrentItem(2);
			break;
		case R.id.btn_expired:
			viewPager.setCurrentItem(3);
			break;
		case R.id.imageButton_back:
			finish();
			break;
		}
	}
}
