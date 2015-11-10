package com.ak.qmyd.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.bean.result.RegisterDetailResult;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.EncryptUtils;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.RegisterTask;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.RegisterTask.RegisterCallback;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * @author HM
 * @date 2015-5-14 下午8:02:43
 */
public class RegisterDetailActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_register_detail_return;
	private EditText et_register_detail_code, et_register_detail_password,
			et_register_detail_nichen;
	private Button bt_register_detail_get, bt_register_detail_button;
	private int count = 60;
	private String code;
	private String pwd;
	private String nichen;
	private DrawerLayout mDrawerLayout;
	private LinearLayout ll_register_detail;
	private Dialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_detail);
		loadDialog = UIManager.getLoadingDialog(this);
		findView();
		initView();
		MyApplication.instance.addActivity(RegisterDetailActivity.this);
		codeHandler.sendEmptyMessage(0);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_register_detail_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initView() {
		ib_register_detail_return.setOnClickListener(this);
		bt_register_detail_get.setOnClickListener(this);
		bt_register_detail_button.setOnClickListener(this);
		ll_register_detail.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), 0);
			}
		});
	}

	private void findView() {
		ll_register_detail = (LinearLayout) findViewById(R.id.ll_register_detail);
		ib_register_detail_return = (ImageButton) findViewById(R.id.ib_register_detail_return);
		et_register_detail_code = (EditText) findViewById(R.id.et_register_detail_code);
		et_register_detail_password = (EditText) findViewById(R.id.et_register_detail_password);
		et_register_detail_nichen = (EditText) findViewById(R.id.et_register_detail_nichen);
		bt_register_detail_get = (Button) findViewById(R.id.bt_register_detail_get);
		bt_register_detail_button = (Button) findViewById(R.id.bt_register_detail_button);
		et_register_detail_code.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					immediateRegister();
				}
				return false;
			}
		});
		et_register_detail_password.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					immediateRegister();
				}
				return false;
			}
		});
		et_register_detail_nichen.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					immediateRegister();
				}
				return false;
			}
		});
		et_register_detail_password.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int cou = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
				cou = before + count;
				String editable = et_register_detail_password.getText()
						.toString();
				String str = Tools.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_register_detail_password.setText(str);
					et_register_detail_password.setSelection(str.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		et_register_detail_nichen.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int cou = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
				cou = before + count;
				String editable = et_register_detail_nichen.getText()
						.toString();
				String str = Tools.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_register_detail_nichen.setText(str);
					et_register_detail_nichen.setSelection(str.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_register_detail_return:
			finish();
			break;
		case R.id.bt_register_detail_get:
			loadDialog.show();
			if (NetManager.isNetworkConnected(this)) {
				getReData();
			} else {
				showToast("网络不可用，请检查网络设置");
			}
			codeHandler.sendEmptyMessage(0);
			break;
		case R.id.bt_register_detail_button:
			immediateRegister();
			break;
		default:
			break;
		}

	}

	private void immediateRegister() {
		if (NetManager.isNetworkConnected(this)) {
			toRegister();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
	}

	private void toRegister() {
		code = et_register_detail_code.getText().toString();
		pwd = et_register_detail_password.getText().toString();
		nichen = et_register_detail_nichen.getText().toString();
		if (code == null || code.length() == 0) {
			et_register_detail_code.requestFocus();
			showToast("请输入验证码");
			return;
		}
		if (code.length() != 6) {
			et_register_detail_code.requestFocus();
			showToast("请输入正确的验证码");
			return;
		}
		if (pwd == null || pwd.length() == 0) {
			et_register_detail_password.requestFocus();
			showToast("请输入密码");
			return;
		}
		if (pwd.length() < 6) {
			et_register_detail_password.requestFocus();
			showToast("请输入6-18位密码");
			return;
		}
		if (nichen == null || nichen.length() == 0) {
			et_register_detail_nichen.requestFocus();
			showToast("请输入昵称");
			return;
		}
		if (nichen.length() < 1) {
			et_register_detail_nichen.requestFocus();
			showToast("请输入1-10位昵称");
			return;
		}
		getData();
	}

	private void getData() {
		loadDialog.show();
		// rest/admin/ commitIdentifying
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest strRequest = new StringRequest(Request.Method.POST,
				Config.USER_REGISTER_DETAIL_URL, new Listener<String>() {

					@Override
					public void onResponse(String response) {

						Gson gson = new Gson();
						RegisterDetailResult rdl = gson.fromJson(response,
								RegisterDetailResult.class);
						UIManager.toggleDialog(loadDialog);
						if (rdl.getResultCode() == 1) {
							showToast("注册成功");
							skipActivity(LoginActivity.class);
						} else if (rdl.getResultCode() == 2) {
							showToast("验证失败，验证码输入错误");
						} else if (rdl.getResultCode() == 3) {
							showToast("验证码已过期，请重新发送");
						} else if (rdl.getResultCode() == 4) {
							showToast("注册失败");
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("phone", getIntent().getStringExtra("phone"));
				map.put("hardId", MyApplication.instance.getHardId());
				map.put("indetifyingCode", code);
				map.put("passWord", EncryptUtils.getMD5AndBase64Str(pwd));
				map.put("name", nichen);
				return map;
			}
		};
		queue.add(strRequest);

	}

	private void getReData() {
		// rest/admin/identifyingCode

		new RegisterTask(this, getIntent().getStringExtra("phone"),
				new RegisterCallback() {
					@Override
					public void onRegisterSuccess(String authBean) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						Result r = gson.fromJson(authBean, Result.class);
						UIManager.toggleDialog(loadDialog);
						if (r.getResultCode() == 1) {
							showToast("验证码已发送成功");
							changGetButtonBg();
						} else if (r.getResultCode() == 2) {
							showToast("验证码发送时失败");
						}
					}

					@Override
					public void onRegisterError() {
						showToast("获取验证码错误,请重新获取");
						UIManager.toggleDialog(loadDialog);
					}
				}).execute();

		// RequestQueue queue = Volley.newRequestQueue(this);
		// StringRequest strRequest = new StringRequest(Request.Method.POST,
		// Config.USER_REGISTER_URL, new Listener<String>() {
		//
		// @Override
		// public void onResponse(String response) {
		//
		// Gson gson = new Gson();
		// Result r = gson.fromJson(response, Result.class);
		// if (r.getResultCode() == 1) {
		// showToast("验证码已发送成功");
		// changGetButtonBg();
		// } else if (r.getResultCode() == 2) {
		// showToast("验证码发送时失败");
		// }
		// }
		// }, new ErrorListener() {
		//
		// @Override
		// public void onErrorResponse(VolleyError error) {
		// showToast("网络不可用，请检查网络设置");
		// }
		// }) {
		// @Override
		// protected Map<String, String> getParams() {
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("phone", getIntent().getStringExtra("phone"));
		// map.put("hardId", MyApplication.instance.getHardId());
		// return map;
		// }
		// };
		// queue.add(strRequest);

	}

	protected void changGetButtonBg() {
		bt_register_detail_get.setEnabled(false);

	}

	private Handler codeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			count--;
			if (count > 0) {
				bt_register_detail_get.setText("获取中(" + count + ")");
				bt_register_detail_get.setEnabled(false);
				codeHandler.sendEmptyMessageDelayed(0, 1000);
			} else {
				et_register_detail_code.requestFocus();
				count = 60;
				bt_register_detail_get.setText("重新获取");
				bt_register_detail_get.setEnabled(true);
			}
		};
	};

	private void skipActivity(Class clazz) {
		Intent newIntent = new Intent(RegisterDetailActivity.this, clazz);
		newIntent.putExtra("phone", getIntent().getStringExtra("phone"));
		MyApplication.instance.removeActivity();
		startActivity(newIntent);
	}

	// 监听软键盘进入键
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			immediateRegister();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
}
