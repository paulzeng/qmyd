package com.ak.qmyd.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.LoginResult;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.EncryptUtils;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class LoginActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_login_return;
	private EditText et_login_phone, et_login_password;
	private Button bt_login;
	private RelativeLayout rl_login_register, rl_login_forget;
	private String phone;
	private String pwd;
	private DrawerLayout mDrawerLayout;
	private LinearLayout ll_login;
	private SharedPreferences sp;
	private int flag;
	private Dialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loadDialog = UIManager.getLoadingDialog(this);
		sp = this.getSharedPreferences("config", 0);
		flag = this.getIntent().getIntExtra("flag", 0);
		findView();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_login_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void startPushService() {
		Intent intent = new Intent();
		intent.setAction("com.ak.qmyd.pushservice");
		intent.putExtra("status", 1);
		sendOrderedBroadcast(intent, null);
	}

	private void getData() {
		loadDialog.show();
		// rest/admin/login
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest strRequest = new StringRequest(Request.Method.POST,
				Config.USER_LOGIN_URL, new Listener<String>() {

					@Override
					public void onResponse(String response) {

						Gson gson = new Gson();
						LoginResult lr = gson.fromJson(response,
								LoginResult.class);
						UIManager.toggleDialog(loadDialog);
						if (lr.getResultCode() == 1) {
							bt_login.setEnabled(true);
							showToast("登录成功");
							lr.getUserObject().setDynamic(lr.getDynamic());
							lr.getUserObject().setInterest(lr.getInterest());
							lr.getUserObject().setFans(lr.getFans());
							MyApplication.instance.saveUserInfo(
									lr.getUserObject(), lr.getSessionId());
							finish();
							DebugUtility.showLog("flag标识: " + flag);
							switch (flag) {
							case 0:
								startActivity(HomeActivity.class, null);
								break;
							case 1:
								startActivity(DynamicMainActivity.class, null);
								break;
							case 2:
								startActivity(UserCenterActivity.class, null);
								break;
							case 3:
								Intent intent = new Intent(LoginActivity.this,
										DongTaiDetailActivity.class);
								intent.putExtra("flag", 3);
								intent.putExtra("id", getIntent()
										.getStringExtra("id"));
								intent.putExtra("staffId", getIntent()
										.getStringExtra("staffId"));
								startActivity(intent);
								break;
							default:
								break;
							}
							// 登录成功开启消息推送
							if (sp.getBoolean("check", true)) {
								startPushService();
							}
						} else if (lr.getResultCode() == 0) {
							showToast("登录失败,账号不存在或密码错误!");
							bt_login.setEnabled(true);
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
				map.put("phone", et_login_phone.getText().toString());
				map.put("passWord", EncryptUtils
						.getMD5AndBase64Str(et_login_password.getText()
								.toString()));
				map.put("hardId",
						((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
								.getDeviceId());
				map.put("system", "android " + Build.VERSION.RELEASE);
				map.put("phoneType", Build.MODEL);
				return map;
			}
		};
		queue.add(strRequest);

	}

	private void initView() {
		ib_login_return.setOnClickListener(this);
		bt_login.setOnClickListener(this);
		rl_login_register.setOnClickListener(this);
		rl_login_forget.setOnClickListener(this);
		et_login_phone
				.setText((getIntent().getStringExtra("phone") == null ? ""
						: getIntent().getStringExtra("phone")));
		ll_login.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), 0);
			}
		});
	}

	private void findView() {
		ib_login_return = (ImageButton) findViewById(R.id.ib_login_return);
		et_login_phone = (EditText) findViewById(R.id.et_login_phone);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		bt_login = (Button) findViewById(R.id.bt_login);
		rl_login_register = (RelativeLayout) findViewById(R.id.rl_login_register);
		rl_login_forget = (RelativeLayout) findViewById(R.id.rl_login_forget);
		ll_login = (LinearLayout) findViewById(R.id.ll_login);
		et_login_phone.setOnEditorActionListener(new OnEditorActionListener() {  
            @Override  
            public boolean onEditorAction(TextView v, int actionId,  
                    KeyEvent event) {  
                if (actionId == EditorInfo.IME_ACTION_DONE) {  
                	login();
                }  
                return false;  
            }  
        });  
		
		et_login_phone.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int cou = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
				cou = before + count;
				String editable = et_login_phone.getText().toString();
				String str = Tools.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_login_phone.setText(str);
					et_login_phone.setSelection(str.length());
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

		et_login_password.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int cou = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
				cou = before + count;
				String editable = et_login_password.getText().toString();
				String str = Tools.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_login_password.setText(str);
					// 设置新的光标所在位置
					et_login_password.setSelection(str.length());
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
		case R.id.ib_login_return:
			startActivity(HomeActivity.class, null);
			finish();
			break;
		case R.id.bt_login:
			if (NetManager.isNetworkConnected(this)) {
				login();
			} else {
				showToast("网络不可用，请检查网络设置");
			}
			break;
		case R.id.rl_login_register:
			Intent newIntent1 = new Intent(LoginActivity.this,
					RegisterActivity.class);
			newIntent1.putExtra("register", "register");
			startActivity(newIntent1);
			MyApplication.instance.addActivity(LoginActivity.this);
			break;
		case R.id.rl_login_forget:
			Intent newIntent2 = new Intent(LoginActivity.this,
					ForgetPwdActivity.class);
			newIntent2.putExtra("forget_pwd", "forget_pwd");
			startActivity(newIntent2);
			MyApplication.instance.addActivity(LoginActivity.this);
			break;
		default:
			break;
		}

	}

	private void skipActivity(Class clazz) {
		Intent newIntent = new Intent(LoginActivity.this, clazz);
		newIntent.putExtra("register", "register");
		newIntent.putExtra("forget_pwd", "forget_pwd");
		startActivity(newIntent);
	}

	private void login() {
		phone = et_login_phone.getText().toString();
		pwd = et_login_password.getText().toString();
		if (phone == null || phone.length() == 0) {
			et_login_phone.requestFocus();
			showToast("请输入手机号码");
			return;
		}
		if (phone.length() != 11) {
			et_login_phone.requestFocus();
			showToast("请输入正确的手机号码");
			return;
		}
		if (!Tools.isMobileNO(phone)) {
			et_login_phone.requestFocus();
			showToast("请输入正确的手机号码");
			return;
		}
		if (pwd == null || pwd.length() == 0) {
			et_login_password.requestFocus();
			showToast("请输入密码");
			return;
		}
		if (pwd.length() < 6) {
			et_login_password.requestFocus();
			showToast("请输入6-18位密码");
			return;
		}
		bt_login.setEnabled(false);
		getData();
	}

	// 监听软键盘进入键
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			login();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(HomeActivity.class, null);
			finish();
		}
		return false;
	}
}
