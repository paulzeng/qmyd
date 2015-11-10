package com.ak.qmyd.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.tools.DebugUtility;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MovementAlertCountActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_alert_count_layout_return;
	private RelativeLayout rl_alert_count_layout_once,
			rl_alert_count_layout_five, rl_alert_count_layout_ten,
			rl_alert_count_layout_half;
	private ImageView iv_alert_count_layout_once_img,
			iv_alert_count_layout_five_img, iv_alert_count_layout_ten_img,
			iv_alert_count_layout_half_img;
	private String count = null;
	private DrawerLayout mDrawerLayout;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private static final int NOTE_EDIT = 0;  //编辑状态
	private static final int NOTE_INSERT = 1; // 插入状态
	private int state;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert_count_layout);
		sp = getSharedPreferences("config", 0);
		editor = sp.edit();
		findView();
		initView();
		getCount();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl__alert_count_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		if (Intent.ACTION_INSERT == getIntent().getAction()) {
			state = NOTE_INSERT;
		}else if (Intent.ACTION_EDIT == getIntent().getAction()) {
			state = NOTE_EDIT;
		}
	}

	private void getCount() {
		String count = getIntent().getStringExtra("count");
		if(count.equals("一次")){
			iv_alert_count_layout_once_img.setVisibility(View.VISIBLE);
		}else if(count.equals("五分钟")){
			iv_alert_count_layout_five_img.setVisibility(View.VISIBLE);
		}else if(count.equals("十分钟")){
			iv_alert_count_layout_ten_img.setVisibility(View.VISIBLE);
		}else if(count.equals("半小时")){
			iv_alert_count_layout_half_img.setVisibility(View.VISIBLE);
		}
	}

	private void initView() {
		ib_alert_count_layout_return.setOnClickListener(this);
		rl_alert_count_layout_once.setOnClickListener(this);
		rl_alert_count_layout_five.setOnClickListener(this);
		rl_alert_count_layout_ten.setOnClickListener(this);
		rl_alert_count_layout_half.setOnClickListener(this);

	}

	private void findView() {

		ib_alert_count_layout_return = (ImageButton) findViewById(R.id.ib_alert_count_layout_return);
		rl_alert_count_layout_once = (RelativeLayout) findViewById(R.id.rl_alert_count_layout_once);
		rl_alert_count_layout_five = (RelativeLayout) findViewById(R.id.rl_alert_count_layout_five);
		rl_alert_count_layout_ten = (RelativeLayout) findViewById(R.id.rl_alert_count_layout_ten);
		rl_alert_count_layout_half = (RelativeLayout) findViewById(R.id.rl_alert_count_layout_half);
		iv_alert_count_layout_once_img = (ImageView) findViewById(R.id.iv_alert_count_layout_once_img);
		iv_alert_count_layout_five_img = (ImageView) findViewById(R.id.iv_alert_count_layout_five_img);
		iv_alert_count_layout_ten_img = (ImageView) findViewById(R.id.iv_alert_count_layout_ten_img);
		iv_alert_count_layout_half_img = (ImageView) findViewById(R.id.iv_alert_count_layout_half_img);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_alert_count_layout_return:
			finish();
			getString();
			if(NOTE_INSERT == state){
				skipInsertActivity(TrainAlertDetailActivity.class);
			}else if (NOTE_EDIT == state){
				skipEditActivity(TrainAlertDetailActivity.class);
			}
			break;

		case R.id.rl_alert_count_layout_once:
			changImageView(iv_alert_count_layout_once_img);
			if(iv_alert_count_layout_once_img.getVisibility() == View.VISIBLE){
				iv_alert_count_layout_half_img.setVisibility(View.INVISIBLE);
				iv_alert_count_layout_five_img.setVisibility(View.INVISIBLE);
				iv_alert_count_layout_ten_img.setVisibility(View.INVISIBLE);
			}
			break;

		case R.id.rl_alert_count_layout_five:
			changImageView(iv_alert_count_layout_five_img);
			if(iv_alert_count_layout_five_img.getVisibility() == View.VISIBLE){
				iv_alert_count_layout_once_img.setVisibility(View.INVISIBLE);
				iv_alert_count_layout_half_img.setVisibility(View.INVISIBLE);
				iv_alert_count_layout_ten_img.setVisibility(View.INVISIBLE);
			}
			break;

		case R.id.rl_alert_count_layout_ten:
			changImageView(iv_alert_count_layout_ten_img);
			if(iv_alert_count_layout_ten_img.getVisibility() == View.VISIBLE){
				iv_alert_count_layout_once_img.setVisibility(View.INVISIBLE);
				iv_alert_count_layout_five_img.setVisibility(View.INVISIBLE);
				iv_alert_count_layout_half_img.setVisibility(View.INVISIBLE);
			}
			break;

		case R.id.rl_alert_count_layout_half:
			changImageView(iv_alert_count_layout_half_img);
			if(iv_alert_count_layout_half_img.getVisibility() == View.VISIBLE){
				iv_alert_count_layout_once_img.setVisibility(View.INVISIBLE);
				iv_alert_count_layout_five_img.setVisibility(View.INVISIBLE);
				iv_alert_count_layout_ten_img.setVisibility(View.INVISIBLE);
			}
			break;
		default:
			break;
		}
	}

	private void skipInsertActivity(Class clazz) {
		Intent newIntent = new Intent(Intent.ACTION_INSERT);
		newIntent.setClass(this, clazz);
		newIntent.putExtra("mytime", getIntent().getStringExtra("mytime"));
		newIntent.putExtra("count", count);
		newIntent.putExtra("way",  getIntent().getStringExtra("way"));
		newIntent.putExtra("timetype", getIntent().getStringExtra("timetype"));
		newIntent.putExtra("week",getIntent().getStringExtra("week"));
		newIntent.putExtra("day",getIntent().getStringExtra("day"));
		newIntent.putExtra("sectionName", getIntent().getStringExtra("sectionName"));
		newIntent.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("index",getIntent().getStringExtra("index"));
		newIntent.putExtra("type", "2");
		editor.putString("count", count);
		editor.commit();
		startActivity(newIntent);
	}
	private void skipEditActivity(Class clazz) {
		Intent newIntent = new Intent(Intent.ACTION_EDIT);
		newIntent.setClass(this, clazz);
		newIntent.putExtra("time",getIntent().getStringExtra("time"));
		newIntent.putExtra("mytime", getIntent().getStringExtra("mytime"));
		newIntent.putExtra("count", count);
		newIntent.putExtra("id",getIntent().getStringExtra("id"));
		newIntent.putExtra("index",getIntent().getStringExtra("index"));
		newIntent.putExtra("way",  getIntent().getStringExtra("way"));
		newIntent.putExtra("timetype", getIntent().getStringExtra("timetype"));
		newIntent.putExtra("week",getIntent().getStringExtra("week"));
		newIntent.putExtra("day",getIntent().getStringExtra("day"));
		newIntent.putExtra("sectionName", getIntent().getStringExtra("sectionName"));
		newIntent.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("isstart",getIntent().getStringExtra("isstart"));
		editor.putString("count", count);
		editor.commit();
		startActivity(newIntent);
	}
	private void changImageView(ImageView iv) {
		if (iv.getVisibility() == View.INVISIBLE) {
			iv.setVisibility(View.VISIBLE);
		} else {
			iv.setVisibility(View.INVISIBLE);
		}
	}

	private void getString() {
		if (iv_alert_count_layout_once_img.getVisibility() == View.VISIBLE) {
			count = "一次";
		}
		if (iv_alert_count_layout_five_img.getVisibility() == View.VISIBLE) {
			count = "五分钟";
		}
		if (iv_alert_count_layout_ten_img.getVisibility() == View.VISIBLE) {
			count = "十分钟";
		}
		if (iv_alert_count_layout_half_img.getVisibility() == View.VISIBLE) {
			count = "半小时";
		}
	}
}
