package com.ak.qmyd.activity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ImageButton in_user_info_return;
	private TextView emptyView, tv_user_info_edit, tv_user_name,
			tv_user_info_introduction, tv_user_info_focus, tv_user_info_fans;
	private ListView lv_user_info_Dynamic;
	private DrawerLayout mDrawerLayout;
	private CircleImageView iv_user_info_img;
	private Map<String, ?> userInfoSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		BitmapUtils.initImageLoader(getApplicationContext());
		findView();
		initView();
		initData();
		setEmptyView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_user_info_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initData() {
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + (CharSequence) userInfoSp.get("thumbnail"),
				iv_user_info_img);
		tv_user_name.setText((CharSequence) userInfoSp.get("userName"));
		String description = (String) userInfoSp.get("description"); 
		tv_user_info_introduction.setText("简介："
				+ (description == null ? "生命中最美好的时光是短暂的" : description));
		tv_user_info_focus.setText("关注："
				+ (CharSequence) userInfoSp.get("interest"));
		tv_user_info_fans
				.setText("粉丝：" + (CharSequence) userInfoSp.get("fans"));
	}

	private void findView() {
		iv_user_info_img = (CircleImageView) this.findViewById(R.id.iv_user_info_img);
		in_user_info_return = (ImageButton) this
				.findViewById(R.id.in_user_info_return);
		tv_user_info_edit = (TextView) this
				.findViewById(R.id.tv_user_info_edit);
		tv_user_name = (TextView) this.findViewById(R.id.tv_user_name);
		tv_user_info_introduction = (TextView) this
				.findViewById(R.id.tv_user_info_introduction);
		tv_user_info_focus = (TextView) this
				.findViewById(R.id.tv_user_info_focus);
		tv_user_info_fans = (TextView) this
				.findViewById(R.id.tv_user_info_fans);
	}

	void initView() {
		in_user_info_return.setOnClickListener(this);
		tv_user_info_edit.setOnClickListener(this);
		lv_user_info_Dynamic = (ListView) findViewById(R.id.lv_user_info_Dynamic);
	}

	void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无动态");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_user_info_Dynamic.getParent()).addView(emptyView);
		lv_user_info_Dynamic.setEmptyView(emptyView);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.in_user_info_return:
			finish();
			break;
		case R.id.tv_user_info_edit:
			Intent intent = new Intent(this, EditUserInfoActivity.class);
			startActivity(intent);
			break;
		}
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		initData();
	}
}
