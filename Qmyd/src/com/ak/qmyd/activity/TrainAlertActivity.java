package com.ak.qmyd.activity;

import android.os.Bundle;
import android.view.Window;

import com.ak.qmyd.activity.base.BaseFragmentActivity;

/** 
 * @author HM
 * @date 2015-5-13 обнГ8:45:52
 */
public class TrainAlertActivity extends BaseFragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
}
