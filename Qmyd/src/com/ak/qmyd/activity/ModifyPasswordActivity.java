package com.ak.qmyd.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.EncryptUtils;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.StringUtil;
import com.ak.qmyd.tools.ToastManager;
import com.ak.qmyd.view.ContainsEmojiEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ModifyPasswordActivity extends BaseActivity implements
		OnClickListener {

	private ImageButton imgBtnBack;
	private TextView ib_submit_password;
	private ContainsEmojiEditText edt_old_password, edt_new_password,
			edt_new_password2;
	private Map<String, ?> userInfoSp;
	int mMaxLenth = 18;// ��������������ַ�����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_password);
		initView();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
	}

	void initView() {
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		ib_submit_password = (TextView) this
				.findViewById(R.id.ib_submit_password);
		ib_submit_password.setOnClickListener(this);
		edt_old_password = (ContainsEmojiEditText) this
				.findViewById(R.id.edt_old_password);
		edt_old_password.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = edt_old_password.getText().toString();
				String str = StringUtil.stringFilter(editable); // ���������ַ�
				if (!editable.equals(str)) {
					edt_old_password.setText(str);
				}
				edt_old_password.setSelection(edt_old_password.length());
				cou = edt_old_password.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = edt_old_password.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
					ToastManager.show(ModifyPasswordActivity.this,
							"�������ݲ��ܳ���200���ַ�");
				}
			}
		});
		edt_new_password = (ContainsEmojiEditText) this
				.findViewById(R.id.edt_new_password);
		edt_new_password.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = edt_new_password.getText().toString();
				String str = StringUtil.stringFilter(editable); // ���������ַ�
				if (!editable.equals(str)) {
					edt_new_password.setText(str);
				}
				edt_new_password.setSelection(edt_new_password.length());
				cou = edt_new_password.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = edt_new_password.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
					ToastManager.show(ModifyPasswordActivity.this,
							"�������ݲ��ܳ���200���ַ�");
				}
			}
		});
		edt_new_password2 = (ContainsEmojiEditText) this
				.findViewById(R.id.edt_new_password2);
		edt_new_password2.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = edt_new_password2.getText().toString();
				String str = StringUtil.stringFilter(editable); // ���������ַ�
				if (!editable.equals(str)) {
					edt_new_password2.setText(str);
				}
				edt_new_password2.setSelection(edt_new_password2.length());
				cou = edt_new_password2.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = edt_new_password2.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
					ToastManager.show(ModifyPasswordActivity.this,
							"�������ݲ��ܳ���200���ַ�");
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgBtnBack:
			finish();
			break;
		case R.id.ib_submit_password:
			String oldPwd = edt_old_password.getText().toString();
			String newPwd = edt_new_password.getText().toString();
			String newPwd2 = edt_new_password2.getText().toString();
			if (oldPwd.trim().equals("") || newPwd.trim().equals("")
					|| newPwd2.trim().equals("")) {
				showToast("�������벻�����������¼��");
			} else {
				if (!newPwd.equals(newPwd2)) {
					showToast("��������������벻һ��");
				} else if (oldPwd.trim().equals(newPwd)) {
					showToast("���������������ͬ���������޸�");
				} else {
					if (NetManager.isNetworkConnected(this)) {
						if (newPwd.length() > 5) {
							postModify(EncryptUtils.getMD5AndBase64Str(oldPwd),
									EncryptUtils.getMD5AndBase64Str(newPwd));
						} else {
							showToast("���볤�Ȳ�������6λ");
						}

					} else {
						Toast.makeText(this, "���粻���ã�������������", 1 * 1000).show();
					}
				}
			}
			break;
		}
	}

	void postModify(final String oldPwd, final String newPwd) {
		String httpurl = Config.BASE_URL
				+ "/api/rest/admin/config/updatePassword";
		RequestQueue requestQueue = Volley
				.newRequestQueue(getApplicationContext());
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				httpurl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						DebugUtility.showLog("�ύ�޸�����ɹ������" + response);
						parseRespense(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						DebugUtility.showLog("�ύ�޸�����ʧ�ܣ�" + error.getMessage());
						showToast("��ȡ����ʧ��");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// ������������Ҫpost�Ĳ���
				Map<String, String> params = new HashMap<String, String>();
				params.put("hardId", MyApplication.instance.getHardId());
				params.put("sessionId", userInfoSp.get("sessionId").toString());
				params.put("userId", userInfoSp.get("userId").toString());
				params.put("oldPassword", oldPwd);
				params.put("newPassword", newPwd);
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
				showToast("�޸�����ɹ�,�����µ�¼");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "��ȡ��Ϣ�쳣");
				showToast("�����ԭʼ��������,����������");
				edt_old_password.setText("");
				edt_new_password.setText("");
				edt_new_password2.setText("");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "δ��½���½��ʱ�������µ�½");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
