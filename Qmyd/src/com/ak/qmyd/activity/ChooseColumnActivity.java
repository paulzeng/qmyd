package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.ListMessageAdapter;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.bean.PushInfo;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.db.DBManager;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.PreferenceManager;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ChooseColumnActivity extends BaseActivity implements
		OnClickListener {
	private Button btnChoose;
	private TextView txtAdd;
	private SharedPreferences sp;
	private SharedPreferences.Editor edit;
	private CheckBox chk_lq, chk_ppq, chk_wq, chk_yj, chk_zq, chk_yy;
	private List<HashMap<String, String>> typeList;
	HashMap<String, String> map = new HashMap<String, String>();
	HashMap<String, String> map1 = new HashMap<String, String>();
	HashMap<String, String> map2 = new HashMap<String, String>();
	HashMap<String, String> map3 = new HashMap<String, String>();
	HashMap<String, String> map4 = new HashMap<String, String>();
	HashMap<String, String> map5 = new HashMap<String, String>(); 
	private Dialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_column);
		sp = getSharedPreferences("config", 0); 
		edit = sp.edit();
		initView();
		typeList = new ArrayList<HashMap<String, String>>();
	}

	void initView() {
		btnChoose = (Button) this.findViewById(R.id.btnChoose);
		btnChoose.setOnClickListener(this);
		txtAdd = (TextView) this.findViewById(R.id.txtAdd);
		txtAdd.setOnClickListener(this);
		chk_lq = (CheckBox) this.findViewById(R.id.chk_lq);
		chk_lq.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub
				if (checked) {
					map.put("name", "篮球");
				} else {
					map.clear();
					// map.remove("name");
				}
				if (!map.isEmpty()) {
					typeList.add(map);
				} else {
					typeList.remove(map);
				}

			}
		});
		chk_ppq = (CheckBox) this.findViewById(R.id.chk_ppq);
		chk_ppq.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub
				if (checked) {
					map1.put("name", "乒乓球");
				} else {
					map1.clear();
					// map1.remove("name");
				}
				if (!map1.isEmpty()) {
					typeList.add(map1);
				} else {
					typeList.remove(map1);
				}
			}
		});
		chk_wq = (CheckBox) this.findViewById(R.id.chk_wq);
		chk_wq.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub
				if (checked) {
					map2.put("name", "网球");
				} else {
					map2.clear();
					// map2.remove("name");
				}
				if (!map2.isEmpty()) {
					typeList.add(map2);
				} else {
					typeList.remove(map2);
				}
			}
		});
		chk_yj = (CheckBox) this.findViewById(R.id.chk_yj);
		chk_yj.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub

				if (checked) {
					map3.put("name", "瑜伽");
				} else {
					map3.clear();
					// map3.remove("name");
				}
				if (!map3.isEmpty()) {
					typeList.add(map3);
				} else {
					typeList.remove(map3);
				}
			}
		});
		chk_zq = (CheckBox) this.findViewById(R.id.chk_zq);
		chk_zq.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub
				if (checked) {
					map4.put("name", "足球");
				} else {
					map4.clear();
					// map4.remove("name");
				}
				if (!map4.isEmpty()) {
					typeList.add(map4);
				} else {
					typeList.remove(map4);
				}
			}
		});
		chk_yy = (CheckBox) this.findViewById(R.id.chk_yy);
		chk_yy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub
				if (checked) {
					map5.put("name", "游泳");
				} else {
					map5.clear();
					// map5.remove("name");
				}
				if (!map5.isEmpty()) {
					typeList.add(map5);
				} else {
					typeList.remove(map5);
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btnChoose:
			DebugUtility.showLog("size:" + typeList.size()
					+ typeList.toString());
			String sportsName = "";
			if (typeList.size() == 0) {
				showToast("至少要选择一项运动");
			} else {
				for (HashMap<String, String> map : typeList) {
					if (!map.isEmpty()) {
						sportsName += map.get("name") + "@";
					}
				}
				sportsName = sportsName.substring(0, sportsName.length() - 1);
				DebugUtility.showLog("sportsName:" + sportsName);

				sendType(sportsName);

			}
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

	void sendType(final String type) {
		loadDialog = UIManager.getLoadingDialog(this);
		loadDialog.show();
		// rest/admin/home/index/List/{hardId}/{sessionId}/{userId}/{sportsName}
		RequestQueue mRequestQueue = Volley.newRequestQueue(this);
		String url = Config.BASE_URL + "/api/rest/admin/home/index/List";
		DebugUtility.showLog("发送运动类型url：" + url);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest request = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("发送运动类型成功结果：" + response);
							parseRespense(response);
							Intent intent = new Intent(
									ChooseColumnActivity.this,
									HomeActivity.class);
							edit.putBoolean("isFirst", false);
							edit.commit();
							startActivity(intent);
							overridePendingTransition(R.anim.in_form_left,
									R.anim.out_of_right);
							setGuided();
							UIManager.toggleDialog(loadDialog);
							finish();
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							DebugUtility.showLog("发送运动类型失败："
									+ error.getMessage());
							Intent intent = new Intent(
									ChooseColumnActivity.this,
									HomeActivity.class);
							edit.putBoolean("isFirst", false);
							edit.commit();
							startActivity(intent);
							overridePendingTransition(R.anim.in_form_left,
									R.anim.out_of_right);
							setGuided();
							UIManager.toggleDialog(loadDialog);
							finish();
						}
					}) {
				@Override
				protected Map<String, String> getParams() {
					// 在这里设置需要post的参数
					Map<String, String> params = new HashMap<String, String>();
					params.put("hardId", MyApplication.instance.getHardId());
					params.put("sessionId", "0");
					params.put("userId", "0");
					params.put("sportsName", type);
					return params;
				}
			};
			mRequestQueue.add(request);
		} else {
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
		}

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
				DebugUtility.showLog("上传运动类型：" + response);
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setGuided() {
		SharedPreferences preferences = this.getSharedPreferences("first_pref",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirst", false);
		editor.commit();
	}
}
