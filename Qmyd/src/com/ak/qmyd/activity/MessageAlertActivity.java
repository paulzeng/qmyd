package com.ak.qmyd.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.view.ActionSheet;
import com.ak.qmyd.view.ActionSheet.OnActionSheetSelected;

/**
 * @author JGB
 * @date 2015-5-23 下午5:54:42
 */
public class MessageAlertActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_msg_alert_return;
	private ToggleButton tb_new_msg_img, tb_train_msg_img;
	private RelativeLayout rl_alert_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message_alert);
		findView();
		initView();
	}

	private void initView() {
		ib_msg_alert_return.setOnClickListener(this);
		rl_alert_time.setOnClickListener(this);
	}

	private void findView() {
		ib_msg_alert_return = (ImageButton) findViewById(R.id.ib_msg_alert_return);
		tb_new_msg_img = (ToggleButton) findViewById(R.id.tb_new_msg_img);
		tb_train_msg_img = (ToggleButton) findViewById(R.id.tb_train_msg_img);
		rl_alert_time = (RelativeLayout) findViewById(R.id.rl_alert_time);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_msg_alert_return:
			finish();
			break;
		case R.id.rl_alert_time:
			ActionSheet.showSheet(this, new OnActionSheetSelected() {

				@Override
				public void onClick(int which, String time) {
					switch (which) {
					case 0:
						showToast("取消");
						break;
					case 1:
						showToast("保存时间为:" + time);
						break;
					default:
						break;
					}
				}
			}, null);
			break;
		default:
			break;
		}

	}
}
