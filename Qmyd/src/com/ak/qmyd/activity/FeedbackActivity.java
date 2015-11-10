package com.ak.qmyd.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class FeedbackActivity extends BaseActivity implements OnClickListener {
	private ImageButton imgBtnBack;
	private TextView ib_submit_send;
	private EditText edt_feedback;
	private Map<String, ?> userInfoSp;
	private Dialog loadDialog;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				InputMethodManager inputManager = (InputMethodManager) edt_feedback
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edt_feedback, 0);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
		mHandler.sendEmptyMessageDelayed(0, 500);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
	}

	void initView() {
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		ib_submit_send = (TextView) this.findViewById(R.id.ib_submit_send);
		ib_submit_send.setOnClickListener(this);
		edt_feedback = (EditText) this.findViewById(R.id.edt_feedback);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgBtnBack:
			finish();
			break;
		case R.id.ib_submit_send: 
			if (NetManager.isNetworkConnected(this)) {
				if(!edt_feedback.getText().toString().trim().equals("")){
					postFeedback(edt_feedback.getText().toString());
				}else{
					showToast("反馈内容不能为空");
				}
			} else {
				Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
			}
			break;
		}
	}

	void postFeedback(final String content) {
		loadDialog = UIManager.getLoadingDialog(this);
		loadDialog.show();
		String httpurl = Config.BASE_URL
				+ "/api/rest/admin/config/addSuggestion";
		RequestQueue requestQueue = Volley
				.newRequestQueue(getApplicationContext());
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				httpurl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						DebugUtility.showLog("提交意见反馈成功结果：" + response);
						parseRespense(response);
						UIManager.toggleDialog(loadDialog);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						DebugUtility.showLog("提交意见反馈失败：" + error.getMessage());
						UIManager.toggleDialog(loadDialog);
						showToast("发送失败");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要post的参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("hardId", MyApplication.instance.getHardId());
				params.put("sessionId", userInfoSp.get("sessionId").toString());
				params.put("userId", userInfoSp.get("userId").toString());
				params.put("content", content);
				return params;
			}
		};
		requestQueue.add(stringRequest);
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
				showToast("发送完成");
				finish();
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

	@Override
	public void onBackPressed() {
		mHandler.removeMessages(0);
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		mHandler.removeMessages(0);
		super.onDestroy();
	}
}
