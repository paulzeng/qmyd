package com.ak.qmyd.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;

public class ModifyPhoneActivity extends BaseActivity implements
		OnClickListener {

	private ImageButton imgBtnBack;
	private TextView ib_submit_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_phone);
		initView();
	}

	void initView() {
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		ib_submit_phone = (TextView) this.findViewById(R.id.ib_submit_phone);
		ib_submit_phone.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgBtnBack:
			finish();
			break;
			
		case R.id.ib_submit_phone:
			showToast("Ìá½»ÐÞ¸Ä");
			break;
		}
	}

}
