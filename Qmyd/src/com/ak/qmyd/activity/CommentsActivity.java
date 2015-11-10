package com.ak.qmyd.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CommentsActivity extends BaseActivity implements OnClickListener {

	CheckBox good_praise, bad_comments;

	ImageButton imageButton_back;

	TextView textView_submit;

	private RequestQueue mRequestQueue;

	private Context context;

	private int orderId;

	private int appraiseRank;

	private EditText et_comments;

	private String content;

	private String hardId, sessionId, userId;
	
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		loadDialog = UIManager.getLoadingDialog(this);
		context = getApplicationContext();
		mRequestQueue = Volley.newRequestQueue(context);
		orderId = getIntent().getExtras().getInt("orderId");
		hardId = getIntent().getExtras().getString("hardId");
		sessionId = getIntent().getExtras().getString("sessionId");
		userId = getIntent().getExtras().getString("userId");
		findView();
	}

	private void findView() {
		good_praise = (CheckBox) this.findViewById(R.id.good_praise);
		bad_comments = (CheckBox) this.findViewById(R.id.bad_comments);
		imageButton_back = (ImageButton) this
				.findViewById(R.id.imageButton_back);
		textView_submit = (TextView) this.findViewById(R.id.textView_submit);
		et_comments = (EditText) this.findViewById(R.id.et_comments);
		good_praise.setChecked(true);
		imageButton_back.setOnClickListener(this);
		textView_submit.setOnClickListener(this);
		good_praise.setOnClickListener(this);
		bad_comments.setOnClickListener(this);
		et_comments.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionStart = et_comments.getSelectionStart();
				selectionEnd = et_comments.getSelectionEnd();
				if (temp.length() > 200) {
					s.delete(200, selectionEnd);
					et_comments.setText(s);
					et_comments.setSelection(et_comments.length());// 设置光标在最后
					showToast("评论应控制在200字以内");
				}
			}
		});
	}

	/**
	 * POST提交数据
	 * */

	private void httpPost() {
		loadDialog.show();
		StringRequest request = new StringRequest(Method.POST, Config.BASE_URL
				+ "/api/rest/venues/addVenuesAppraise", new Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject object = new JSONObject(response);
					int resultCode = object.getInt("resultCode");
					// 返回的描述信息
					String info = object.getString("resultInfo");
					UIManager.toggleDialog(loadDialog);
					if (resultCode == 1) {
						Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						Toast.makeText(context, "评论失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				UIManager.toggleDialog(loadDialog);
				Toast.makeText(context, "评论失败", Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("hardId", hardId);
				map.put("sessionId", sessionId);
				map.put("userId", userId);
				map.put("orderId", orderId + "");

				if (good_praise.isChecked()) {
					appraiseRank = 1;
				} else if (bad_comments.isChecked()) {
					appraiseRank = 2;
				}
				map.put("appraiseRank", appraiseRank + "");
				map.put("appraiseContent", content);

				return map;
			}
		};
		mRequestQueue.add(request);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.good_praise:
			bad_comments.toggle();
			break;
		case R.id.bad_comments:
			good_praise.toggle();
			break;
		case R.id.imageButton_back:
			finish();
			break;
		case R.id.textView_submit:
			content = et_comments.getText().toString();
			if (!(content.equals(""))) {
				httpPost();
			} else {
				Toast.makeText(context, "请输入你的评论", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

}
