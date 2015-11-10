package com.ak.qmyd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;

public class AccountSecurityActivity extends BaseActivity implements
		OnClickListener {
	private ImageButton imgBtnBack;
	private RelativeLayout rl_modifyPhone, rl_modifyPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_security);
		initView();
	}

	void initView() {
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		rl_modifyPhone = (RelativeLayout) this
				.findViewById(R.id.rl_modifyPhone);
		rl_modifyPhone.setOnClickListener(this);
		rl_modifyPwd = (RelativeLayout) this.findViewById(R.id.rl_modifyPwd);
		rl_modifyPwd.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgBtnBack:
			finish();
			break;
		case R.id.rl_modifyPhone:
			Intent intent1 = new Intent(this, ModifyPhoneActivity.class);
			startActivity(intent1);
			break;
		case R.id.rl_modifyPwd:
			Intent intent2 = new Intent(this, ModifyPasswordActivity.class);
			startActivity(intent2);
			break;
		}
	}

}
