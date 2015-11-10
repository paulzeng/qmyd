package com.ak.qmyd.activity;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.EndTrainPlanResult;
import com.ak.qmyd.bean.result.EndTrainPlanResult.TrainObject;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class EndTrainPlanActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_train_result_return, ib_train_result_share;
	private Button bt_train_result_complete;
	private TrainObject trainObject;
	private TextView tv_train_result_score, tv_train_result_time,
			tv_train_result_count, tv_train_result_heat, tv_chapter,
			tv_train_result_line;
	private DrawerLayout mDrawerLayout;
	private String fname;
	private Map<String, ?> userInfoSp;
	private RelativeLayout rl_train_result;
	private Dialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_result);
		loadDialog = UIManager.getLoadingDialog(this);
		findView();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		if (NetManager.isNetworkConnected(this)) {
			initView();
			getData();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_end_train_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void getData() {
		loadDialog.show();
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = Config.TRAIN_END_URL;
		StringRequest strRequest = new StringRequest(Request.Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {

						Gson gson = new Gson();
						EndTrainPlanResult etpr = gson.fromJson(response,
								EndTrainPlanResult.class);
						UIManager.toggleDialog(loadDialog);
						if (etpr.getResultCode() == 1) {
							trainObject = etpr.getTrainObject();
							initData();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("hardId", MyApplication.instance.getHardId());
				String sessionId = (String) userInfoSp.get("sessionId") == null ? "1"
						: (String) userInfoSp.get("sessionId");
				map.put("sessionId", sessionId);
				map.put("sectionId", getIntent().getStringExtra("sectionId"));
				String userId = ((String) userInfoSp.get("userId") == null ? "0"
						: (String) userInfoSp.get("userId"));
				map.put("userId", userId);
				map.put("startTime", getIntent().getStringExtra("startTime"));
				map.put("endTime", getIntent().getStringExtra("endTime"));
				map.put("trainTime", getIntent()
						.getStringExtra("sectionLength"));
				return map;
			}
		};
		queue.add(strRequest);
	}

	private void initData() {
		tv_train_result_score.setText(trainObject.getTrainScore());
		tv_chapter.setText(getIntent().getStringExtra("sectionName"));
		if (trainObject.getTrainScore().equals("0")) {
			tv_train_result_time.setText(getIntent().getStringExtra(
					"intervalTime")
					+ "秒");
			tv_train_result_count.setText("0个");
			tv_train_result_heat.setText("0千卡");
		} else {
			tv_train_result_time.setText(trainObject.getTrainTime());
			tv_train_result_count.setText(trainObject.getTrainAction() + "个");
			tv_train_result_heat.setText(trainObject.getTrainHeat() + "千卡");
		}
	}

	private void initView() {
		ib_train_result_return.setOnClickListener(this);
		ib_train_result_share.setOnClickListener(this);
		bt_train_result_complete.setOnClickListener(this);
	}

	private void findView() {
		ib_train_result_return = (ImageButton) findViewById(R.id.ib_train_result_return);
		ib_train_result_share = (ImageButton) findViewById(R.id.ib_train_result_share);
		bt_train_result_complete = (Button) findViewById(R.id.bt_train_result_complete);
		tv_train_result_score = (TextView) findViewById(R.id.tv_train_result_score);
		tv_train_result_time = (TextView) findViewById(R.id.tv_train_result_time);
		tv_train_result_count = (TextView) findViewById(R.id.tv_train_result_count);
		tv_train_result_heat = (TextView) findViewById(R.id.tv_train_result_heat);
		tv_chapter = (TextView) findViewById(R.id.tv_chapter);
		rl_train_result = (RelativeLayout) findViewById(R.id.rl_train_result);
		tv_train_result_line = (TextView) findViewById(R.id.tv_train_result_line);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_train_result_return:
			finish();
			skipActivity(EndTrainPlanActivity.this,
					SynchronousTrainActivity2.class);
			break;
		case R.id.ib_train_result_share:
			finish();
			rl_train_result.setVisibility(View.INVISIBLE);
			bt_train_result_complete.setVisibility(View.INVISIBLE);
			tv_train_result_line.setVisibility(View.INVISIBLE);
			fname = getImage(v);
			skipActivity(EndTrainPlanActivity.this, TrainShareActivity.class);
			break;
		case R.id.bt_train_result_complete:
			finish();
			// skipActivity(EndTrainPlanActivity.this,
			// TrainingListDatailActivity.class);
			break;

		default:
			break;
		}

	}

	private String getImage(View v) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.CHINA);
		String fname = "/sdcard/" + sdf.format(new Date()) + ".png";
		View view = v.getRootView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		if (bitmap != null) {
			try {
				FileOutputStream out = new FileOutputStream(fname);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		}
		return fname;

	}

	private void skipActivity(Context context, Class clazz) {
		Intent newIntent = new Intent(context, clazz);
		newIntent.putExtra("typeId", getIntent().getStringExtra("typeId"));
		newIntent.putExtra("trainID", getIntent().getStringExtra("trainID"));
		newIntent
				.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("fname", fname);
		newIntent.putExtra("sectionName",
				getIntent().getStringExtra("sectionName"));
		newIntent.putExtra("trainScore", trainObject.getTrainScore());
		startActivity(newIntent);
	}
}
