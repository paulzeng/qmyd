package com.ak.qmyd.activity;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.db.DBManager;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CircleImageView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserCenterActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ImageView imgViewCircle;
	private ImageButton ib_user_return, ib_user_setting;
	private RelativeLayout rl_user_dynamic, rl_user_focus, rl_user_fans,
			rl_new_message, rl_user_info, rl_my_train_plan, rl_my_reservation,
			rl_my_lottery, rl_my_activity;
	private TextView tv_user_nickname, tv_user_score, tv_user_dynamic,
			tv_user_focus, tv_user_fans, tv_user_brief, tv_fans_add;
	private Map<String, ?> userInfoSp;
	private CircleImageView iv_head_img;
	private DrawerLayout mDrawerLayout;
	private RequestQueue mRequestQueue;
	private String oldfans;
	private Dialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_usercenter);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mRequestQueue = Volley.newRequestQueue(this);
		findView();
		initView();
		initData();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_center_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		httpGetJson();
	}

	private void initData() {
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + userInfoSp.get("thumbnail"), iv_head_img);
		tv_user_nickname.setText((CharSequence) userInfoSp.get("userName"));
		tv_user_score.setText((CharSequence) userInfoSp.get("integral"));
		tv_user_dynamic.setText((CharSequence) userInfoSp.get("dynamic"));
		tv_user_focus.setText((CharSequence) userInfoSp.get("interest"));
		oldfans = userInfoSp.get("fans").toString();
		if (oldfans == null || oldfans.equals("")) {
			tv_user_fans.setText(0);
		} else {
			tv_user_fans.setText((CharSequence) userInfoSp.get("fans"));
		}

		tv_user_brief.setText((CharSequence) userInfoSp.get("description"));
		if (DBManager.getInstance(this).hasNotRead(
				userInfoSp.get("userId").toString())) {
			imgViewCircle.setVisibility(View.VISIBLE);
		} else {
			imgViewCircle.setVisibility(View.INVISIBLE);
		}
	}

	private void findView() {
		imgViewCircle = (ImageView) findViewById(R.id.imgViewCircle);
		ib_user_return = (ImageButton) findViewById(R.id.ib_user_return);
		ib_user_setting = (ImageButton) findViewById(R.id.ib_user_setting);
		iv_head_img = (CircleImageView) findViewById(R.id.iv_head_img);
		rl_user_info = (RelativeLayout) findViewById(R.id.rl_user_info);
		rl_new_message = (RelativeLayout) findViewById(R.id.rl_new_message);
		rl_my_train_plan = (RelativeLayout) findViewById(R.id.rl_my_train_plan);
		rl_my_reservation = (RelativeLayout) findViewById(R.id.rl_my_reservation);
		rl_my_lottery = (RelativeLayout) findViewById(R.id.rl_my_lottery);
		rl_my_activity = (RelativeLayout) findViewById(R.id.rl_my_activity);
		tv_user_nickname = (TextView) findViewById(R.id.tv_user_nickname);
		tv_user_score = (TextView) findViewById(R.id.tv_user_score);
		tv_user_dynamic = (TextView) findViewById(R.id.tv_user_dynamic);
		tv_user_focus = (TextView) findViewById(R.id.tv_user_focus);
		tv_user_fans = (TextView) findViewById(R.id.tv_user_fans);
		tv_fans_add = (TextView) findViewById(R.id.tv_fans_add);
		tv_user_brief = (TextView) findViewById(R.id.tv_user_brief);
		rl_user_dynamic = (RelativeLayout) this
				.findViewById(R.id.rl_user_dynamic);
		rl_user_focus = (RelativeLayout) this.findViewById(R.id.rl_user_focus);
		rl_user_fans = (RelativeLayout) this.findViewById(R.id.rl_user_fans);
	}

	private void initView() {
		ib_user_return.setOnClickListener(this);
		ib_user_setting.setOnClickListener(this);
		rl_user_info.setOnClickListener(this);
		rl_new_message.setOnClickListener(this);
		rl_my_train_plan.setOnClickListener(this);
		rl_my_reservation.setOnClickListener(this);
		rl_my_lottery.setOnClickListener(this);
		rl_my_activity.setOnClickListener(this);
		rl_user_dynamic.setOnClickListener(this);
		rl_user_focus.setOnClickListener(this);
		rl_user_fans.setOnClickListener(this);
	}

	void httpGetJson() {
		loadDialog.show();
		String url = Config.MYINFO_URL + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ userInfoSp.get("sessionId") + "/" + userInfoSp.get("userId");
		DebugUtility.showLog("请求URL:" + url);
		if (NetManager.isNetworkConnected(this)) {
			loadDialog.show();
			StringRequest request = new StringRequest(Request.Method.GET, url,
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							UIManager.toggleDialog(loadDialog);
							parseRespense(response);
							DebugUtility.showLog("获取我的：" + response);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							DebugUtility.showLog("获取我的失败：" + error.getMessage());
							showToast("获取数据失败");
							UIManager.toggleDialog(loadDialog);
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					return super.getParams();
				}
			};
			mRequestQueue.add(request);
		} else {
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
			UIManager.toggleDialog(loadDialog);
		}
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
				String interest = JsonManager.getJsonItem(jsonObj, "interest")
						.toString();
				tv_user_focus.setText(interest);
				String dynamic = JsonManager.getJsonItem(jsonObj, "dynamic")
						.toString();
				tv_user_dynamic.setText(dynamic);
				String fans = JsonManager.getJsonItem(jsonObj, "fans")
						.toString();
				// 保存粉丝的数量
				MyApplication.instance.save("fans", fans);

				int old = Integer.parseInt(oldfans);
				int news = Integer.parseInt(fans);
				int add = news - old;

				if (add <= 0) {
					tv_fans_add.setVisibility(View.GONE);
					tv_user_fans.setText(fans);
				} else {
					tv_fans_add.setVisibility(View.VISIBLE);
					tv_fans_add.setText("+" + add);
				}

				// 重新设置积分
				String userObject = JsonManager.getJsonItem(jsonObj,
						"userObject").toString();
				jsonObj = new JSONObject(userObject);
				String integral = JsonManager.getJsonItem(jsonObj, "integral")
						.toString();
				tv_user_score.setText(integral);
				DebugUtility.showLog("重新获取的积分=" + integral);
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra("flag", "2");
				startActivity(intent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.ib_user_return:
			skipActivity(HomeActivity.class);
			finish();
			break;
		case R.id.ib_user_setting:
			skipActivity(SettingActivity.class);
			break;
		case R.id.rl_user_info:
			Intent intent = new Intent(UserCenterActivity.this,
					GusterInfoActivity.class);
			intent.putExtra("userid", userInfoSp.get("userId").toString());
			startActivity(intent);
			break;
		case R.id.rl_new_message:
			Intent newIntent = new Intent(UserCenterActivity.this,
					MessageCenterActivity.class);
			startActivityForResult(newIntent, 0);
			break;
		case R.id.rl_my_train_plan:
			skipActivity(MyTrainActivity.class);
			break;
		case R.id.rl_my_reservation:
			skipActivity(MyOrderActivity.class);
			break;
		case R.id.rl_my_lottery:
			showToast("敬请期待！");
		case R.id.rl_my_activity:
			showToast("敬请期待！");
			break;
		case R.id.rl_user_dynamic:
			skipActivity(DynamicMainActivity.class);
			break;
		case R.id.rl_user_focus:
			skipActivity(FocusActivity.class);
			break;
		case R.id.rl_user_fans:
			skipActivity(FansActivity.class);
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (DBManager.getInstance(this).hasNotRead(
				userInfoSp.get("userId").toString())) {
			imgViewCircle.setVisibility(View.VISIBLE);
		} else {
			imgViewCircle.setVisibility(View.INVISIBLE);
		}
	}

	private void skipActivity(Class clazz) {
		Intent newIntent = new Intent(UserCenterActivity.this, clazz);
		startActivity(newIntent);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		initData();
		httpGetJson();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(HomeActivity.class, null);
			finish();
		}
		return false;
	}
}
