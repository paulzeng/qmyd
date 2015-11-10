package com.ak.qmyd.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.ForgetPwdTask;
import com.ak.qmyd.tools.ForgetPwdTask.ForgetPwdCallback;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.google.gson.Gson;

/**
 * @author HM
 * @date 2015-5-27 下午7:59:23
 */
public class ForgetPwdActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ImageButton ib_register_return;
	private EditText et_register_phone;
	private Button bt_register_obtain;
	private String phone;
	private DrawerLayout mDrawerLayout;
	private TextView tv_register_or_forget_title;
	private LinearLayout ll_register;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		loadDialog = UIManager.getLoadingDialog(this);
		findView();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_register_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void findView() {
		ll_register = (LinearLayout) this.findViewById(R.id.ll_register);
		ib_register_return = (ImageButton) this
				.findViewById(R.id.ib_register_return);
		et_register_phone = (EditText) this
				.findViewById(R.id.et_register_phone);
		bt_register_obtain = (Button) this
				.findViewById(R.id.bt_register_obtain);
		tv_register_or_forget_title = (TextView) this
				.findViewById(R.id.tv_register_or_forget_title);
		if (getIntent().getStringExtra("forget_pwd").equals("forget_pwd")) {
			tv_register_or_forget_title.setText("找回密码");
		}
		et_register_phone.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					getVerificationCode();
				}
				return false;
			}
		});
	}

	private void initView() {
		ib_register_return.setOnClickListener(this);
		bt_register_obtain.setOnClickListener(this);
		ll_register.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), 0);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_register_return:
			finish();
			break;
		case R.id.bt_register_obtain:
			getVerificationCode();
			break;
		}
	}

	private void getVerificationCode() {
		phone = et_register_phone.getText().toString();
		if (phone == null || phone.length() == 0) {
			et_register_phone.requestFocus();
			showToast("请输入手机号码");
			return;
		}
		if (phone.length() != 11) {
			et_register_phone.requestFocus();
			showToast("请输入正确的手机号码");
			return;
		}
		if(!Tools.isMobileNO(phone)){
			et_register_phone.requestFocus();
			showToast("请输入正确的手机号码");
			return;
		}
		loadDialog.show();
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("正在获取验证码。。。");
			getCode();
		} else {
			UIManager.toggleDialog(loadDialog);
			showToast("网络不可用，请检查网络设置");
		}
	}

	private void getCode() {
		new ForgetPwdTask(this, phone, new ForgetPwdCallback() {
			@Override
			public void onForgetPwdSuccess(String authBean) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				Result r = gson.fromJson(authBean, Result.class);
				UIManager.toggleDialog(loadDialog);
				if (r.getResultCode() == 1) {
					showToast("验证码已发送成功");
					skipActivity(ForgetPwdDetailActivity.class);
					MyApplication.instance.addActivity(ForgetPwdActivity.this);
				} else if (r.getResultCode() == 2) {
					showToast("验证码发送时失败");
				} else if (r.getResultCode() == 0) {
					showToast("手机号还未注册");
					et_register_phone.requestFocus();
				}
			}

			@Override
			public void onForgetPwdError() {
				UIManager.toggleDialog(loadDialog);
				showToast("获取验证码错误,请重新获取");
			}
		}).execute();
	}

	protected void skipActivity(Class clazz) {
		Intent newIntent = new Intent(ForgetPwdActivity.this, clazz);
		newIntent.putExtra("phone", phone);
		startActivity(newIntent);
	}

	// 监听软键盘进入键
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			getVerificationCode();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
}
