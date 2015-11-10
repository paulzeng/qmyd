package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.ListFansAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.activity.listener.ListItemClickHelp;
import com.ak.qmyd.bean.Funs;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FansActivity extends BaseFragmentActivity implements
		OnClickListener, ListItemClickHelp, OnItemClickListener {

	private ImageButton imgBtnBack;

	ListView listView;
	ListFansAdapter mAdapter;
	List<Funs> mDate;
	private Map<String, ?> userInfoSp;
	private Dialog loadDialog;
	private RequestQueue mRequestQueue;
	private TextView emptyView;
	private DrawerLayout mDrawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fans);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		initView();
		initData();
	}

	void initView() {
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.lstViewFans);
		// httpGetJson();
		mDate = new ArrayList<Funs>();
		mAdapter = new ListFansAdapter(this, mDate, this);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}

	void initData() {
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		loadDialog = UIManager.getLoadingDialog(this);
		mRequestQueue = Volley.newRequestQueue(this);
		httpGetJson("1", "10");
	}

	void httpGetJson(String currPage, String size) {
		loadDialog.show();
		String httpurl = Config.BASE_URL + "/api/rest/circle/queryMyPersonList"
				+ "/" + userInfoSp.get("userId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currPage + "/"
				+ size;

		DebugUtility.showLog("请求URL:" + httpurl);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest request = new StringRequest(Request.Method.GET,
					httpurl, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							parseRespense(response);
							UIManager.toggleDialog(loadDialog);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							DebugUtility.showLog("获取我的圈子失败："
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
							Toast.makeText(FansActivity.this, "获取数据失败",
									1 * 1000).show();
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					// TODO Auto-generated method stub
					return super.getParams();
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
				String myPersonList = JsonManager.getJsonItem(jsonObj,
						"myPersonList").toString();
				mDate = new Gson().fromJson(myPersonList,
						new TypeToken<List<Funs>>() {
						}.getType());
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
			}
			mAdapter = new ListFansAdapter(this, mDate, this);
			listView.setAdapter(mAdapter);
			setEmptyView();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无关注");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgBtnBack:
			finish();
			break;
		}
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "取消关注", 1 * 1000).show();
		if (mDate.get(position).getState().equals("1")) {
			httpCancelFocus(mDate.get(position).getUserId(), position, "1");
		} else {
			httpCancelFocus(mDate.get(position).getUserId(), position, "2");
		}
	}

	void httpCancelFocus(final String staffId, final int position,
			final String state) {
		loadDialog.show();
		String httpurl = Config.BASE_URL
				+ "/api/rest/circle/addOrDelRelationPerson";
		DebugUtility.showLog("请求URL:" + httpurl);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest stringRequest = new StringRequest(
					Request.Method.POST, httpurl,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("提交取消关注结果：" + response);
							parseCancelRespense(response, position, state);
							UIManager.toggleDialog(loadDialog);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							DebugUtility.showLog("提交取消关注失败："
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
							showToast("发送失败");
						}
					}) {
				@Override
				protected Map<String, String> getParams() {
					// 在这里设置需要post的参数
					Map<String, String> params = new HashMap<String, String>();
					params.put("hardId", MyApplication.instance.getHardId());
					params.put("sessionId", userInfoSp.get("sessionId")
							.toString());
					params.put("userId", userInfoSp.get("userId").toString());
					params.put("staffId", staffId);
					params.put("state", state);
					return params;
				}
			};
			mRequestQueue.add(stringRequest);
		} else {
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
		}
	}

	void parseCancelRespense(String response, int position, String state) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				if (state.equals("1")) {
					mDate.get(position).setState("2");
				} else {
					mDate.get(position).setState("1");
				}
				mAdapter.notifyDataSetChanged();
				// mAdapter = new ListFocusAdapter(this, mDate, this);
				// listView.setAdapter(mAdapter);
				setEmptyView();
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultInfo + "获取消息异常");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, GusterInfoActivity.class);
		intent.putExtra("userid", mDate.get(arg2).getUserId());
		startActivity(intent);
	}
}
