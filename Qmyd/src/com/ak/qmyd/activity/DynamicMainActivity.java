package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.DongTaiFragmentAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.fragment.DongTaiFragment;
import com.ak.qmyd.fragment.FindFragment;
import com.ak.qmyd.fragment.QuanZiFragment;
import com.photoselector.model.PhotoModel;

/**
 * 动态
 * 
 * @author thomas
 * 
 */
public class DynamicMainActivity extends BaseFragmentActivity implements
		OnClickListener, OnPageChangeListener {
	public static final String TOP_TAB_INDEX = "top_tab_index";
	public static final String ACTION_CHANGE_TOP_TAB_NEARBY = "action_change_top_tab";
	private ImageButton imgBtnBack, imgBtnAdd;
	private ViewPager mvp;
	private ArrayList<TextView> tvList;
	private ArrayList<RadioButton> rbList;
	private ChangeTopTabReceiver receiver;
	private DrawerLayout mDrawerLayout;
	private String typeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dongtai);
		typeId = getSharedPreferences("config", 0)
				.getString("typeId", "1");
		findViews();
		initViews();
		setListener();
		receiver = new ChangeTopTabReceiver();
		IntentFilter filter = new IntentFilter(ACTION_CHANGE_TOP_TAB_NEARBY);
		this.registerReceiver(receiver, filter);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_center_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	protected void findViews() {
		mvp = (ViewPager) this.findViewById(R.id.dongtai_vp);
		mvp.setOffscreenPageLimit(2);
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		imgBtnAdd = (ImageButton) this.findViewById(R.id.ib_add);
		imgBtnAdd.setOnClickListener(this);

	}

	protected void initViews() {
		// 存放头部导航的tv 文本控件
		tvList = new ArrayList<TextView>();
		// 存放头部导航中的 line控件
		rbList = new ArrayList<RadioButton>();
		TextView leftTV = (TextView) this
				.findViewById(R.id.nearby_top_nav_left_tv);
		TextView middleTV = (TextView) this
				.findViewById(R.id.nearby_top_nav_middle_tv);
		TextView rightTV = (TextView) this
				.findViewById(R.id.nearby_top_nav_right_tv);
		RadioButton leftLine = (RadioButton) this
				.findViewById(R.id.nearby_top_nav_left_line);
		RadioButton middleLine = (RadioButton) this
				.findViewById(R.id.nearby_top_nav_middle_line);
		RadioButton rightLine = (RadioButton) this
				.findViewById(R.id.nearby_top_nav_right_line);
		tvList.add(leftTV);
		tvList.add(middleTV);
		tvList.add(rightTV);
		rbList.add(leftLine);
		rbList.add(middleLine);
		rbList.add(rightLine);
		leftTV.setOnClickListener(this);
		middleTV.setOnClickListener(this);
		rightTV.setOnClickListener(this);
		leftTV.setText("关注");
		middleTV.setText("圈子");
		rightTV.setText("发现");

		// 初始进入的
		this.changeNavText(tvList, 0);

		// 初始化球队，球友，球场三个面板
		ArrayList<Fragment> list = new ArrayList<Fragment>();
		list.add(new DongTaiFragment(typeId));
		list.add(new QuanZiFragment(typeId));
		list.add(new FindFragment(typeId));
		DongTaiFragmentAdapter adapter = new DongTaiFragmentAdapter(
				this.getSupportFragmentManager(), list);
		mvp.setAdapter(adapter);

	}

	protected void setListener() {
		mvp.setOnPageChangeListener(this);
	}

	/**
	 * 改变导航字体
	 * 
	 * @param index
	 */

	public void changeNavText(ArrayList<TextView> tvList, int index) {
		// 改变导航字体
		for (int i = 0, len = tvList.size(); i < len; i++) {
			TextView tv = tvList.get(i);
			//
			tv.setTextColor(Color.rgb(146, 148, 151));
			if (i == index) {// 232,129,59
				tv.setTextColor(Color.rgb(232, 129, 59));
			}

		}

	}

	class ChangeTopTabReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int topTab = intent.getIntExtra(TOP_TAB_INDEX, -1);
			if (topTab != -1) {
				mvp.setCurrentItem(topTab);
				onPageSelected(topTab);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int index = -1;
		switch (v.getId()) {
		case R.id.imgBtnBack:
			startActivity(HomeActivity.class, null);
			finish();
			break;
		case R.id.ib_add:
			Intent intent = new Intent(this, SendDynamicActivity.class);
			intent.putExtra("typeId", typeId);
			this.startActivityForResult(intent, 0); 
			break;
		case R.id.nearby_top_nav_left_tv:
			index = 0;
			break;

		case R.id.nearby_top_nav_middle_tv:
			index = 1;
			break;
		case R.id.nearby_top_nav_right_tv:
			index = 2;
			break;
		}
		if (index != -1) {
			mvp.setCurrentItem(index);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		if (requestCode == 0) {// selected image
			if (data != null && data.getExtras() != null) {
				@SuppressWarnings("unchecked")
				List<PhotoModel> photos = (List<PhotoModel>) data.getExtras()
						.getSerializable("photos");
				if (photos == null || photos.isEmpty()) {

				} else {

				}
			}
		}
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
		this.changeNavText(tvList, arg0);// 改变文字
		rbList.get(arg0).setChecked(true);// 改变底部tab
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(HomeActivity.class,null);
			finish();
		}
		return false;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(receiver);
	}
}
