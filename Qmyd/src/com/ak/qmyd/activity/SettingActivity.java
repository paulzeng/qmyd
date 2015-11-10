package com.ak.qmyd.activity;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.Version;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DataCleanManager;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UpdateManager;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ImageButton ib_setting_return;
	private DrawerLayout mDrawerLayout;
	private RelativeLayout rl_account_safe, rl_clear_cache, rl_advice_back,
			rl_check_update, rl_about_us;

	private TextView tv_clear_size;
	private ToggleButton tb_pushmsg;
	private Map<String, ?> userInfoSp;
	private SharedPreferences sp;
	private SharedPreferences.Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settting);
		findView();
		initView();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_setting_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void findView() {
		sp = this.getSharedPreferences("config", 0);
		edit = sp.edit();
		ib_setting_return = (ImageButton) this
				.findViewById(R.id.ib_setting_return);
		tv_clear_size = (TextView) this.findViewById(R.id.tv_clear_size);
		try {
			tv_clear_size.setText(DataCleanManager.getTotalCacheSize(this));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rl_account_safe = (RelativeLayout) this
				.findViewById(R.id.rl_account_safe);
		rl_clear_cache = (RelativeLayout) this
				.findViewById(R.id.rl_clear_cache);
		rl_advice_back = (RelativeLayout) this
				.findViewById(R.id.rl_advice_back);
		rl_check_update = (RelativeLayout) this
				.findViewById(R.id.rl_check_update);
		rl_about_us = (RelativeLayout) this.findViewById(R.id.rl_about_us);
		tb_pushmsg = (ToggleButton) this.findViewById(R.id.tb_pushmsg);
		tb_pushmsg.setChecked(sp.getBoolean("check", true));
		tb_pushmsg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean check) {
				// TODO Auto-generated method stub
				edit.putBoolean("check",check);
				edit.commit();
				DebugUtility.showLog("checked:"+check);
				if(check){
					startPushService();
				}else{
					stopPushService();
				}
			}
		});
	}

	private void startPushService() {
		 Intent intent = new Intent();
		 intent.setAction("com.ak.qmyd.pushservice");
		 intent.putExtra("status", 1);
		 sendOrderedBroadcast(intent, null);
	}

	private void stopPushService() {
		Intent intent = new Intent();
		 intent.setAction("com.ak.qmyd.pushservice");
		 intent.putExtra("status", 0);
		 sendOrderedBroadcast(intent, null);
	}

	void initView() {
		ib_setting_return.setOnClickListener(this);
		rl_account_safe.setOnClickListener(this);
		rl_clear_cache.setOnClickListener(this);
		rl_advice_back.setOnClickListener(this);
		rl_check_update.setOnClickListener(this);
		rl_about_us.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_setting_return:
			finish();
			break;
		case R.id.rl_account_safe:
			Intent intent1 = new Intent(this, ModifyPasswordActivity.class);
			startActivity(intent1);
			break;
		case R.id.rl_advice_back:
			Intent intent2 = new Intent(this, FeedbackActivity.class);
			startActivity(intent2);
			break;
		case R.id.rl_check_update:
			if (NetManager.isNetworkConnected(this)) {
				checkUpdate();
			} else {
				Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
			}
			break;
		case R.id.rl_clear_cache:
			ImageLoader.getInstance().clearMemoryCache();
			ImageLoader.getInstance().clearDiscCache();
			DataCleanManager.clearAllCache(this);
			showToast("清除缓存完成");
			try {
				tv_clear_size.setText(DataCleanManager.getTotalCacheSize(this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.rl_about_us:
			Intent intent3 = new Intent(this, AboutUsActivity.class);
			startActivity(intent3);
			break;
		}

	}

	void checkUpdate() {
		String versionNum = getVersionCode(this) + "";
		// String versionNum = "2.0";
		String url = Config.BASE_URL + "/api/rest/admin/newVersion/"
				+ MyApplication.instance.getHardId() + "/"
				+ userInfoSp.get("sessionId").toString() + "/"
				+ userInfoSp.get("userId").toString() + "/1/" + versionNum;
		// 1 创建RequestQueue对象
		RequestQueue mRequestQueue = Volley.newRequestQueue(this);
		// 2 创建StringRequest对象
		StringRequest mStringRequest = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						DebugUtility.showLog("版本更新成功结果：" + response);
						parseRespense(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						DebugUtility.showLog("版本更新失败：" + error.getMessage());
						showToast("获取数据失败");
					}
				});
		// 3 将StringRequest添加到RequestQueue
		mRequestQueue.add(mStringRequest);
	}

	void parseRespense(String response) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
				String version = JsonManager.getJsonItem(jsonObj, "version")
						.toString();
				Gson gson = new Gson();
				Version mVersion = gson.fromJson(version, Version.class);
				MyApplication.downloadUrl = mVersion.getNetPath();
				new UpdateManager(SettingActivity.this,
						mVersion.getVersionName()).showNoticeDialog(mVersion
						.getAppDesc());
				showToast(resultInfo);
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			String pkg = context.getPackageName();
			versionCode = context.getPackageManager().getPackageInfo(pkg, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
