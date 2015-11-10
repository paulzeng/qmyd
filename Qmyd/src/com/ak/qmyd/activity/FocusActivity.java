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
import com.ak.qmyd.activity.adapter.ListFocusAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.activity.listener.ListItemClickHelp;
import com.ak.qmyd.bean.Focus;
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

public class FocusActivity extends BaseFragmentActivity implements
		OnClickListener, ListItemClickHelp, OnItemClickListener {
	private ImageButton imgBtnBack,ib_search;

	ListView listView;
	ListFocusAdapter mAdapter;

	List<Focus> mDate;
	private Map<String, ?> userInfoSp;
	private Dialog loadDialog;
	private RequestQueue mRequestQueue;
	private TextView emptyView;
	private DrawerLayout mDrawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_focus);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		initView();
		initData();
	}

	void initView() {
		mDate = new ArrayList<Focus>();
		ib_search = (ImageButton) this.findViewById(R.id.ib_search);
		ib_search.setOnClickListener(this);
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.lstViewFocus);
		mAdapter = new ListFocusAdapter(this, mDate, this);
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
		String httpurl = Config.BASE_URL
				+ "/api/rest/circle/queryInterestPersonList" + "/"
				+ userInfoSp.get("userId") + "/" + userInfoSp.get("sessionId")
				+ "/" + MyApplication.instance.getHardId() + "/" + currPage
				+ "/" + size;

		DebugUtility.showLog("����URL:" + httpurl);
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
							DebugUtility.showLog("��ȡ�ҵ�Ȧ��ʧ�ܣ�"
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
							Toast.makeText(FocusActivity.this, "��ȡ����ʧ��",
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
			Toast.makeText(this, "���粻���ã�������������", 1 * 1000).show();
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
				String interestPersonList = JsonManager.getJsonItem(jsonObj,
						"interestPersonList").toString();
				List<Focus> mNewDate = new Gson().fromJson(interestPersonList,
						new TypeToken<List<Focus>>() {
						}.getType());
				mDate.addAll(mNewDate);
				mAdapter.notifyDataSetChanged();
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "��ȡ��Ϣ�쳣");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "δ��½���½��ʱ�������µ�½");
			} 
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
		emptyView.setText("���޹�ע");
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
		case R.id.ib_search:
			Intent intent = new Intent(this,UserSearchActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "ȡ����ע", 1 * 1000).show();
		
		httpCancelFocus(mDate.get(position).getUserId(), position);
	}

	void httpCancelFocus(final String staffId, final int position) {
		loadDialog.show();
		String httpurl = Config.BASE_URL
				+ "/api/rest/circle/addOrDelRelationPerson";
		DebugUtility.showLog("����URL:" + httpurl);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest stringRequest = new StringRequest(
					Request.Method.POST, httpurl,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("�ύȡ����ע�����" + response);
							parseCancelRespense(response, position);
							UIManager.toggleDialog(loadDialog);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							DebugUtility.showLog("�ύȡ����עʧ�ܣ�"
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
							showToast("����ʧ��");
						}
					}) {
				@Override
				protected Map<String, String> getParams() {
					// ������������Ҫpost�Ĳ���
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
			Toast.makeText(this, "���粻���ã�������������", 1 * 1000).show();
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
				showToast("ȡ����ע�ɹ�");
				mDate.remove(position);
				mAdapter.notifyDataSetChanged();
				// mAdapter = new ListFocusAdapter(this, mDate, this);
				// listView.setAdapter(mAdapter);
				setEmptyView();
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultInfo + "��ȡ��Ϣ�쳣");
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
