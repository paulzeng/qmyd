package com.ak.qmyd.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.ListFocusAdapter;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.listener.ListItemClickHelp;
import com.ak.qmyd.bean.Focus;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.ToastManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserSearchActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, OnEditorActionListener,
		ListItemClickHelp {
	private Map<String, ?> userInfoSp;
	private Dialog loadDialog;
	private ImageButton imgBtnBack, ib_search;
	private ListView searchlistFocus;
	private EditText edt_search;
	private RequestQueue mRequestQueue;
	private List<Focus> mDate;
	private ListFocusAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_search);
		initView();
	}

	void initView() {
		loadDialog = UIManager.getLoadingDialog(this);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		searchlistFocus = (ListView) this.findViewById(R.id.searchlistFocus);
		searchlistFocus.setOnItemClickListener(this);
		edt_search = (EditText) this.findViewById(R.id.edt_search);
		edt_search.setOnEditorActionListener(this);
		mDate = new ArrayList<Focus>();
		mAdapter = new ListFocusAdapter(this, mDate, this);
		searchlistFocus.setAdapter(mAdapter);
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
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (actionId) {
		case EditorInfo.IME_NULL:
			System.out.println("null for default_content: " + v.getText());
			break;
		case EditorInfo.IME_ACTION_SEND:
			System.out
					.println("action done for number_content: " + v.getText());
			break;
		case EditorInfo.IME_ACTION_SEARCH:
			if (edt_search.getText().toString().equals("")) {
				ToastManager.show(this, "请输入用户名关键字");
			} else {
				httpSearch(edt_search.getText().toString());
			}
			break;
		case EditorInfo.IME_ACTION_DONE:
			System.out
					.println("action done for number_content: " + v.getText());
			break;
		}
		return true;
	}

	private void httpSearch(String username) {
		try {
			username = URLEncoder.encode(username, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadDialog.show();
		mRequestQueue = Volley.newRequestQueue(this);
		String httpurl = Config.BASE_URL
				+ "/api/rest/circle/queryPartInterestPersonList" + "/"
				+ userInfoSp.get("userId") + "/" + username + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId();
		;
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
							DebugUtility.showLog("获取运动类型失败："
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
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
				DebugUtility.showLog("搜索好友结果" + response);
				String interestPersonList = JsonManager.getJsonItem(jsonObj,
						"interestPersonList").toString();
				List<Focus> mNewDate = new Gson().fromJson(interestPersonList,
						new TypeToken<List<Focus>>() {
						}.getType());
				mDate.addAll(mNewDate);
				mAdapter.notifyDataSetChanged();
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultInfo + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultInfo);
				ToastManager.show(this, "没有符合搜索条件的用户");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "取消关注", 1 * 1000).show();
		httpCancelFocus(mDate.get(position).getUserId(), position);
	}

	void httpCancelFocus(final String staffId, final int position) {
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
							parseCancelRespense(response, position);
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
					params.put("state", "1");
					return params;
				}
			};
			mRequestQueue.add(stringRequest);
		} else {
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
		}
	}

	void parseCancelRespense(String response, int position) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				showToast("取消关注成功");
				mDate.remove(position);
				mAdapter.notifyDataSetChanged(); 
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
