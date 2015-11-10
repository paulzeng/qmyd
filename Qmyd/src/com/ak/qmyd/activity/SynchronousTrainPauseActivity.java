package com.ak.qmyd.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SynchronousTrainPauseActivity extends BaseFragmentActivity implements OnClickListener {

	private TextView tv_async_train_pause_ball,tv_async_train_pause_chapter;
	private ImageView iv_async_train_pause_img;
	private SharedPreferences sp;
	private DrawerLayout mDrawerLayout;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asynchronous_train_pause);
		
		sp = getSharedPreferences("config",Context.MODE_PRIVATE);
		editor = sp.edit();
		findView();
		initView();
		initData();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_train_pause_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initData() {
		tv_async_train_pause_ball.setText(getIntent().getStringExtra("ball"));
		tv_async_train_pause_chapter.setText(getIntent().getStringExtra("sectionName"));
	}

	private void initView() {
		iv_async_train_pause_img.setOnClickListener(this);
		
	}

	private void findView() {
		tv_async_train_pause_ball = (TextView) findViewById(R.id.tv_async_train_pause_ball);
		tv_async_train_pause_chapter = (TextView) findViewById(R.id.tv_async_train_pause_chapter);
		iv_async_train_pause_img = (ImageView) findViewById(R.id.iv_async_train_pause_img);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_async_train_pause_img:
			finish();
			Intent newIntent = new Intent(SynchronousTrainPauseActivity.this,SynchronousTrainActivity2.class);
			editor.putString("currentTime", getIntent().getStringExtra("currentTime"));
			editor.commit();
			newIntent.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
			newIntent.putExtra("sectionName", getIntent().getStringExtra("sectionName"));
			newIntent.putExtra("pause", "pause");
			newIntent.putExtra("startTime", getIntent().getStringExtra("startTime"));
			newIntent.putExtra("typeId", getIntent().getStringExtra("typeId"));
			newIntent.putExtra("trainID", getIntent().getStringExtra("trainID"));
			startActivity(newIntent);
			break;

		default:
			break;
		}
		
	}
}
