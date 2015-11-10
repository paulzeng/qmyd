package com.ak.qmyd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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

public class AsynchronousTrainActivity extends BaseFragmentActivity implements OnClickListener{

	private ImageButton ib_async_return;
	private ImageView iv_async_img,iv_async_clock,iv_async_list,iv_async_history;
	private TextView tv_async_time,tv_async_count,tv_async_heat;
	private Button bt_train_time_record;
	private DrawerLayout mDrawerLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asynchronous_train);
		    
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		StringRequest strRequest = new StringRequest(Request.Method.GET,
				"http://xxxx.xxx", new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						// 通讯成功的 返回数据
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						// 通讯失败的 抛出异常
					}
				});
		queue.add(strRequest);// 加入到通讯队列中
		
		findView();
		initView();
		initData();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_async_train_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initData() {
		tv_async_time.setText(getIntent().getStringExtra("time"));
		tv_async_count.setText(getIntent().getStringExtra("count"));
		tv_async_heat.setText(getIntent().getStringExtra("heat"));
		
	}

	private void initView() {
		ib_async_return.setOnClickListener(this);
		iv_async_clock.setOnClickListener(this);
		iv_async_list.setOnClickListener(this);
		iv_async_history.setOnClickListener(this);
		bt_train_time_record.setOnClickListener(this);
	}

	private void findView() {
		ib_async_return = (ImageButton) findViewById(R.id.ib_async_return);
		iv_async_img = (ImageView) findViewById(R.id.iv_async_img);
		iv_async_clock = (ImageView) findViewById(R.id.iv_async_clock);
		iv_async_list = (ImageView) findViewById(R.id.iv_async_list);
		iv_async_history = (ImageView) findViewById(R.id.iv_async_history);
		tv_async_time = (TextView) findViewById(R.id.tv_async_time);
		tv_async_count = (TextView) findViewById(R.id.tv_async_count);
		tv_async_heat = (TextView) findViewById(R.id.tv_async_heat);
		bt_train_time_record = (Button) findViewById(R.id.bt_train_time_record);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_async_return:
			finish();
			break;
		case R.id.iv_async_clock:
			skipActivity(AsynchronousTrainActivity.this,TrainAlertDetailActivity.class);
			break;
		case R.id.iv_async_list:
			skipActivity(AsynchronousTrainActivity.this,TrainChapterListAcitivity.class);
			break;
		case R.id.iv_async_history:
			skipActivity(AsynchronousTrainActivity.this,TrainHistoryActivity.class);
			break;
		case R.id.bt_train_time_record:
			finish();
			skipActivity(AsynchronousTrainActivity.this,RecordTrainTimeActivity.class);
			break;

		default:
			break;
		}
	}

	private void skipActivity(Context context,Class clazz) {
		Intent newIntent = new Intent(context,clazz);
		startActivity(newIntent);
	}
}
