package com.ak.qmyd.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
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
import com.ak.qmyd.tools.SharedPreferencesUtils;

public class MovementAlertTypeActivity extends BaseFragmentActivity implements
		OnClickListener {
	private DrawerLayout mDrawerLayout;
	private ImageButton ib_alert_type_layout_return;
	private RelativeLayout rl_alert_type_layout_no,
			rl_alert_type_layout_monday, rl_alert_type_layout_tuesday,
			rl_alert_type_layout_wednesday, rl_alert_type_layout_thursday,
			rl_alert_type_layout_friday, rl_alert_type_layout_saturday,
			rl_alert_type_layout_sunday;
	private ImageView iv_alert_type_layout_no_img,
			iv_alert_type_layout_monday_img, iv_alert_type_layout_tuesday_img,
			iv_alert_type_layout_wednesday_img,
			iv_alert_type_layout_thursday_img, iv_alert_type_layout_friday_img,
			iv_alert_type_layout_saturday_img, iv_alert_type_layout_sunday_img;
	private String data;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private static final int NOTE_EDIT = 0;  //编辑状态
	private static final int NOTE_INSERT = 1; // 插入状态
	private int state;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert_type_layout);
		sp = getSharedPreferences("config", 0);
		editor = sp.edit();
		findView();
		getWeek();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl__alert_type_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		Intent intent = getIntent();
		if (Intent.ACTION_INSERT == intent.getAction()) {
			state = NOTE_INSERT;
		}else if (Intent.ACTION_EDIT == intent.getAction()) {
			state = NOTE_EDIT;
		}
	}

	private void getWeek() {
		String string = getIntent().getStringExtra("timetype");
		if (string.contains("不重复")) {
			iv_alert_type_layout_no_img.setVisibility(View.VISIBLE);
		}
		if (string.contains("周一")) {
			iv_alert_type_layout_monday_img.setVisibility(View.VISIBLE);
		}
		if (string.contains("周二")) {
			iv_alert_type_layout_tuesday_img.setVisibility(View.VISIBLE);
		}
		if (string.contains("周三")) {
			iv_alert_type_layout_wednesday_img.setVisibility(View.VISIBLE);
		}
		if (string.contains("周四")) {
			iv_alert_type_layout_thursday_img.setVisibility(View.VISIBLE);
		}
		if (string.contains("周五")) {
			iv_alert_type_layout_friday_img.setVisibility(View.VISIBLE);
		}
		if (string.contains("周六")) {
			iv_alert_type_layout_saturday_img.setVisibility(View.VISIBLE);
		}
		if (string.contains("周日")) {
			iv_alert_type_layout_sunday_img.setVisibility(View.VISIBLE);
		}
		if(string.contains("每天")){
			iv_alert_type_layout_monday_img.setVisibility(View.VISIBLE);
			iv_alert_type_layout_tuesday_img.setVisibility(View.VISIBLE);
			iv_alert_type_layout_wednesday_img.setVisibility(View.VISIBLE);
			iv_alert_type_layout_thursday_img.setVisibility(View.VISIBLE);
			iv_alert_type_layout_friday_img.setVisibility(View.VISIBLE);
			iv_alert_type_layout_saturday_img.setVisibility(View.VISIBLE);
			iv_alert_type_layout_sunday_img.setVisibility(View.VISIBLE);
		}
	}

	private void initView() {
		ib_alert_type_layout_return.setOnClickListener(this);
		rl_alert_type_layout_no.setOnClickListener(this);
		rl_alert_type_layout_monday.setOnClickListener(this);
		rl_alert_type_layout_tuesday.setOnClickListener(this);
		rl_alert_type_layout_wednesday.setOnClickListener(this);
		rl_alert_type_layout_thursday.setOnClickListener(this);
		rl_alert_type_layout_friday.setOnClickListener(this);
		rl_alert_type_layout_saturday.setOnClickListener(this);
		rl_alert_type_layout_sunday.setOnClickListener(this);
	}

	private void findView() {
		ib_alert_type_layout_return = (ImageButton) findViewById(R.id.ib_alert_type_layout_return);
		rl_alert_type_layout_no = (RelativeLayout) findViewById(R.id.rl_alert_type_layout_no);
		rl_alert_type_layout_monday = (RelativeLayout) findViewById(R.id.rl_alert_type_layout_monday);
		rl_alert_type_layout_tuesday = (RelativeLayout) findViewById(R.id.rl_alert_type_layout_tuesday);
		rl_alert_type_layout_wednesday = (RelativeLayout) findViewById(R.id.rl_alert_type_layout_wednesday);
		rl_alert_type_layout_thursday = (RelativeLayout) findViewById(R.id.rl_alert_type_layout_thursday);
		rl_alert_type_layout_friday = (RelativeLayout) findViewById(R.id.rl_alert_type_layout_friday);
		rl_alert_type_layout_saturday = (RelativeLayout) findViewById(R.id.rl_alert_type_layout_saturday);
		rl_alert_type_layout_sunday = (RelativeLayout) findViewById(R.id.rl_alert_type_layout_sunday);
		iv_alert_type_layout_no_img = (ImageView) findViewById(R.id.iv_alert_type_layout_no_img);
		iv_alert_type_layout_monday_img = (ImageView) findViewById(R.id.iv_alert_type_layout_monday_img);
		iv_alert_type_layout_tuesday_img = (ImageView) findViewById(R.id.iv_alert_type_layout_tuesday_img);
		iv_alert_type_layout_wednesday_img = (ImageView) findViewById(R.id.iv_alert_type_layout_wednesday_img);
		iv_alert_type_layout_thursday_img = (ImageView) findViewById(R.id.iv_alert_type_layout_thursday_img);
		iv_alert_type_layout_friday_img = (ImageView) findViewById(R.id.iv_alert_type_layout_friday_img);
		iv_alert_type_layout_saturday_img = (ImageView) findViewById(R.id.iv_alert_type_layout_saturday_img);
		iv_alert_type_layout_sunday_img = (ImageView) findViewById(R.id.iv_alert_type_layout_sunday_img);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_alert_type_layout_return:
			finish();
			getString();
			if(NOTE_INSERT == state){
				skipInsertActivity(data);
			}else if (NOTE_EDIT == state){
				skipEditActivity(data);
			}
			break;
		case R.id.rl_alert_type_layout_no:
			changImageView(iv_alert_type_layout_no_img);
			changDayImageView(iv_alert_type_layout_no_img);
			getString();
			break;
		case R.id.rl_alert_type_layout_monday:
			changImageView(iv_alert_type_layout_monday_img);
			changImageViewWeek();
			getString();
			break;
		case R.id.rl_alert_type_layout_tuesday:
			changImageView(iv_alert_type_layout_tuesday_img);
			changImageViewWeek();
			getString();
			break;
		case R.id.rl_alert_type_layout_wednesday:
			changImageView(iv_alert_type_layout_wednesday_img);
			changImageViewWeek();
			getString();
			break;
		case R.id.rl_alert_type_layout_thursday:
			changImageView(iv_alert_type_layout_thursday_img);
			changImageViewWeek();
			getString();
			break;
		case R.id.rl_alert_type_layout_friday:
			changImageView(iv_alert_type_layout_friday_img);
			changImageViewWeek();
			getString();
			break;
		case R.id.rl_alert_type_layout_saturday:
			changImageView(iv_alert_type_layout_saturday_img);
			changImageViewWeek();
			getString();
			break;
		case R.id.rl_alert_type_layout_sunday:
			changImageView(iv_alert_type_layout_sunday_img);
			changImageViewWeek();
			getString();
			break;
		default:
			break;
		}

	}

	private void getString() {
		if (iv_alert_type_layout_no_img.getVisibility() == View.VISIBLE) {
			data = "不重复";
		} else {
			if (iv_alert_type_layout_monday_img.getVisibility() == View.VISIBLE) {
				if (data != null && !data.contains("周一")) {
					data += "周一";
				} else {
					data = "周一";
				}
			}
			if (iv_alert_type_layout_tuesday_img.getVisibility() == View.VISIBLE) {
				if (data != null && !data.contains("周二")) {
					data += "周二";
				} else {
					data = "周二";
				}
			}
			if (iv_alert_type_layout_wednesday_img.getVisibility() == View.VISIBLE) {
				if (data != null && !data.contains("周三")) {
					data += "周三";
				} else {
					data = "周三";
				}
			}
			if (iv_alert_type_layout_thursday_img.getVisibility() == View.VISIBLE) {
				if (data != null && !data.contains("周四")) {
					data += "周四";
				} else {
					data = "周四";
				}
			}
			if (iv_alert_type_layout_friday_img.getVisibility() == View.VISIBLE) {
				if (data != null && !data.contains("周五")) {
					data += "周五";
				} else {
					data = "周五";
				}
			}
			if (iv_alert_type_layout_saturday_img.getVisibility() == View.VISIBLE) {
				if (data != null && !data.contains("周六")) {
					data += "周六";
				} else {
					data = "周六";
				}
			}
			if (iv_alert_type_layout_sunday_img.getVisibility() == View.VISIBLE) {
				if (data != null && !data.contains("周日")) {
					data += "周日";
				} else {
					data = "周日";
				}
			}
		}
	}

	private void changDayImageView(ImageView iv) {
		if (iv == iv_alert_type_layout_no_img) {
			if (iv.getVisibility() == View.VISIBLE) {
				iv_alert_type_layout_monday_img.setVisibility(View.INVISIBLE);
				iv_alert_type_layout_tuesday_img.setVisibility(View.INVISIBLE);
				iv_alert_type_layout_wednesday_img
						.setVisibility(View.INVISIBLE);
				iv_alert_type_layout_thursday_img.setVisibility(View.INVISIBLE);
				iv_alert_type_layout_friday_img.setVisibility(View.INVISIBLE);
				iv_alert_type_layout_saturday_img.setVisibility(View.INVISIBLE);
				iv_alert_type_layout_sunday_img.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void changImageView(ImageView iv) {
		iv_alert_type_layout_no_img.setVisibility(View.INVISIBLE);
		if (iv.getVisibility() == View.INVISIBLE) {
			iv.setVisibility(View.VISIBLE);
		} else {
			iv.setVisibility(View.INVISIBLE);
		}
		if(getViewState()){
			iv_alert_type_layout_no_img.setVisibility(View.VISIBLE);
		}
	}

	
	private boolean getViewState() {
		if((iv_alert_type_layout_monday_img.getVisibility() == View.INVISIBLE)&&(iv_alert_type_layout_tuesday_img.getVisibility() == View.INVISIBLE)&&(iv_alert_type_layout_wednesday_img.getVisibility() == View.INVISIBLE)&&(iv_alert_type_layout_thursday_img.getVisibility() == View.INVISIBLE)&&(iv_alert_type_layout_friday_img.getVisibility() == View.INVISIBLE)&&(iv_alert_type_layout_saturday_img.getVisibility() == View.INVISIBLE)&&(iv_alert_type_layout_sunday_img.getVisibility() == View.INVISIBLE)){
			return true;
		}
		return false;
	}

	private void changImageViewWeek() {
		if (iv_alert_type_layout_no_img.getVisibility() == View.VISIBLE) {
			iv_alert_type_layout_monday_img.setVisibility(View.INVISIBLE);
			iv_alert_type_layout_tuesday_img.setVisibility(View.INVISIBLE);
			iv_alert_type_layout_wednesday_img.setVisibility(View.INVISIBLE);
			iv_alert_type_layout_thursday_img.setVisibility(View.INVISIBLE);
			iv_alert_type_layout_friday_img.setVisibility(View.INVISIBLE);
			iv_alert_type_layout_saturday_img.setVisibility(View.INVISIBLE);
			iv_alert_type_layout_sunday_img.setVisibility(View.INVISIBLE);
		}
	}

	private void skipInsertActivity(String str) {
		Intent newIntent = new Intent(Intent.ACTION_INSERT);
		newIntent.setClass(this, TrainAlertDetailActivity.class);
		newIntent.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("sectionName", getIntent().getStringExtra("sectionName"));
		if((iv_alert_type_layout_monday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_tuesday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_wednesday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_thursday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_friday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_saturday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_sunday_img.getVisibility() == View.VISIBLE)){
			str = "每天";
		}
		newIntent.putExtra("mytime", getIntent().getStringExtra("mytime"));
		newIntent.putExtra("timetype", str);
		newIntent.putExtra("count", getIntent().getStringExtra("count"));
		newIntent.putExtra("way", getIntent().getStringExtra("way"));
		newIntent.putExtra("week",getIntent().getStringExtra("week"));
		newIntent.putExtra("day",getIntent().getStringExtra("day"));
		newIntent.putExtra("index",getIntent().getStringExtra("index"));
		newIntent.putExtra("type", "2");
		editor.putString("data", str);
		editor.commit();
		startActivity(newIntent);
	}
	private void skipEditActivity(String str) {
		Intent newIntent = new Intent(Intent.ACTION_EDIT);
		newIntent.setClass(this, TrainAlertDetailActivity.class);
		newIntent.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("sectionName", getIntent().getStringExtra("sectionName"));
		if((iv_alert_type_layout_monday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_tuesday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_wednesday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_thursday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_friday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_saturday_img.getVisibility() == View.VISIBLE)&&(iv_alert_type_layout_sunday_img.getVisibility() == View.VISIBLE)){
			str = "每天";
		}
		newIntent.putExtra("id",getIntent().getStringExtra("id"));
		newIntent.putExtra("timetype", str);
		newIntent.putExtra("mytime", getIntent().getStringExtra("mytime"));
		newIntent.putExtra("index",getIntent().getStringExtra("index"));
		newIntent.putExtra("time",getIntent().getStringExtra("time"));
		newIntent.putExtra("count", getIntent().getStringExtra("count"));
		newIntent.putExtra("way", getIntent().getStringExtra("way"));
		newIntent.putExtra("week",getIntent().getStringExtra("week"));
		newIntent.putExtra("day",getIntent().getStringExtra("day"));
		newIntent.putExtra("isstart",getIntent().getStringExtra("isstart"));
		editor.putString("data", str);
		editor.commit();
		startActivity(newIntent);
	}
	public String[] getSharedPreference(String key) {
		String regularEx = "#";
		String[] str = null;
		SharedPreferences sp = getSharedPreferences("config",
				Context.MODE_PRIVATE);
		String values;
		values = sp.getString(key, "");
		str = values.split(regularEx);

		return str;
	}

	public void setSharedPreference(String key, String[] values) {
		String regularEx = "#";
		String str = "";
		SharedPreferences sp = getSharedPreferences("config",
				Context.MODE_PRIVATE);
		if (values != null && values.length > 0) {
			for (String value : values) {
				str += value;
				str += regularEx;
			}
			Editor et = sp.edit();
			et.putString(key, str);
			et.commit();
		}
	}
}
