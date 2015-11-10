package com.ak.qmyd.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;

/**
 * @author JGB
 * @date 2015-5-2 下午4:13:02
 */
public class TrainSkillDetailActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_train_skill_detail_return;
	private WebView wv_train_skill_detail_webview;
	private String webUrl;
	private DrawerLayout mDrawerLayout;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_skill_detail);
		loadDialog = UIManager.getLoadingDialog(this);
		findView();
		loadDialog.show();
		if (NetManager.isNetworkConnected(this)) {
			initView();
			webUrl = Config.BASE_URL + "/TrainSkillDetailPage.external?sp=S"
					+ getIntent().getStringExtra("skillId");
			initData();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_skill_detail_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initData() {
		UIManager.toggleDialog(loadDialog);
		wv_train_skill_detail_webview.loadUrl(webUrl);
		// 设置支持JavaScript
		wv_train_skill_detail_webview.getSettings().setJavaScriptEnabled(true);
		wv_train_skill_detail_webview.getSettings().setDefaultTextEncodingName(
				"UTF-8");
		wv_train_skill_detail_webview
				.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		wv_train_skill_detail_webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				wv_train_skill_detail_webview.loadUrl(url);
				return true;
			}
		});

	}

	private void initView() {
		ib_train_skill_detail_return.setOnClickListener(this);
	}

	private void findView() {
		ib_train_skill_detail_return = (ImageButton) findViewById(R.id.ib_train_skill_detail_return);
		wv_train_skill_detail_webview = (WebView) findViewById(R.id.wv_train_skill_detail_webview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_train_skill_detail_return:
			finish();
			break;

		default:
			break;
		}

	}
}
