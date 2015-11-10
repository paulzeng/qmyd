package com.ak.qmyd.activity.base;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.DynamicMainActivity;
import com.ak.qmyd.activity.HomeActivity;
import com.ak.qmyd.activity.HuodongActivity;
import com.ak.qmyd.activity.LoginActivity;
import com.ak.qmyd.activity.TrainingListActivity;
import com.ak.qmyd.activity.UserCenterActivity;
import com.ak.qmyd.activity.VenuesAppointmentActivity;
import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.view.CircleImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author HM
 * @date 2015-4-8
 */
public class BaseFragmentActivity extends FragmentActivity implements
		IActivitySupport {
	protected String API_URL;
	private boolean isActive;
	private Context context;
	private LinearLayout ll_user_login;
	private TextView tv_username;
	private Button bt_user_loginout_button, bt_user_login_button;
	private CircleImageView iv_user_img;
	private Map<String, ?> userInfoSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		// ImageLoad.initImageLoader(this);
		BitmapUtils.initImageLoader(this);
		isActive = true;
		context = this;
		API_URL = Config.API_URL;
		MyApplication.allActivity.add(this);
	}

	private void initView(View view) {
		if (!MyApplication.instance.userInfoSp.getAll().isEmpty()) {
			ll_user_login.setVisibility(View.VISIBLE);
			tv_username
					.setText((CharSequence) MyApplication.instance.userInfoSp
							.getAll().get("userName"));
			bt_user_login_button.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + userInfoSp.get("thumbnail"), iv_user_img);
		} else {
			ll_user_login.setVisibility(View.GONE);
			bt_user_login_button.setVisibility(View.VISIBLE);
		}
		// bt_user_loginout_button.setOnClickListener(this);
		// bt_user_login_button.setOnClickListener(this);
	}

	private void findView(View view) {
		iv_user_img = (CircleImageView) view.findViewById(R.id.iv_user_img);
		ll_user_login = (LinearLayout) view.findViewById(R.id.ll_user_login);
		tv_username = (TextView) view.findViewById(R.id.tv_username);
		bt_user_loginout_button = (Button) view
				.findViewById(R.id.bt_user_loginout_button);
		bt_user_login_button = (Button) view
				.findViewById(R.id.bt_user_login_button);
	}

	public void setEnableDrawerLayout(DrawerLayout mDrawerLayout) {
		if (mDrawerLayout == null) {
			throw new ClassCastException("findViewBy发现DrawerLayout");
		}
		LayoutInflater LayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = LayoutInflater
				.inflate(R.layout.view_drawer_left_menu, null);
		view.setEnabled(false);
		findView(view);
		initView(view);

		DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(
				getResources().getDimensionPixelSize(
						R.dimen.drawer_left_menu_width),
				DrawerLayout.LayoutParams.MATCH_PARENT);//
		lp.gravity = Gravity.START;
		view.setLayoutParams(lp);
		mDrawerLayout.addView(view);
		// 防止首页获得点击事件
		view.findViewById(R.id.ll_left_menu).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
		view.findViewById(R.id.leftlayout_xl).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String trainid = getSharedPreferences("config", 0)
								.getString("trainid", null);
						if (trainid != null) {
							startActivity(TrainingListActivity.class,
									getSharedPreferences("config", 0)
											.getString("typeId", "1"));
							DebugUtility.showLog("sp里的typeId.."
									+ getSharedPreferences("config", 0)
											.getString("typeId", "1"));
							finish();
						} else {
							showToast("训练内容正在制作，敬请期待");
						}
					}
				});
		view.findViewById(R.id.leftlayout_hd).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(
								HuodongActivity.class,
								getSharedPreferences("config", 0).getString(
										"typeId", "1"));
						finish();
					}
				});
		view.findViewById(R.id.leftlayout_dt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!MyApplication.instance.getUserInfo().isEmpty()) {
							startActivity(DynamicMainActivity.class,
									getSharedPreferences("config", 0)
											.getString("typeId", "1"));
							finish();
						} else {
							Intent intent = new Intent(
									BaseFragmentActivity.this,
									LoginActivity.class);
							intent.putExtra("flag", 1);
							startActivity(intent);
							finish();
						}
					}
				});
		view.findViewById(R.id.leftlayout_cg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(
								VenuesAppointmentActivity.class,
								getSharedPreferences("config", 0).getString(
										"typeId", "1"));
						finish();
					}
				});
		view.findViewById(R.id.leftlayout_wd).setOnClickListener(
				new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!MyApplication.instance.getUserInfo().isEmpty()) {
							Intent intent = new Intent(
									BaseFragmentActivity.this,
									UserCenterActivity.class);
							startActivity(intent);
							finish();
							return;
						} else {
							Intent intent = new Intent(
									BaseFragmentActivity.this,
									LoginActivity.class);
							intent.putExtra("flag", 2);
							startActivity(intent);
							BaseFragmentActivity.this
									.overridePendingTransition(
											R.anim.anim_enter, R.anim.anim_exit);
							finish();
						}

					}
				});
		view.findViewById(R.id.bt_user_login_button).setOnClickListener(
				new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(BaseFragmentActivity.this,
								LoginActivity.class);
						intent.putExtra("flag", 0);
						startActivity(intent);
						BaseFragmentActivity.this.overridePendingTransition(
								R.anim.anim_enter, R.anim.anim_exit);
						finish();
					}
				});

		view.findViewById(R.id.bt_user_loginout_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						loginOut();
					}
				});

	}

	public void baseFinish() {
		BaseFragmentActivity.this.finish();
	}

	public void setTitle(int resId) {
		TextView tvTitle = (TextView) findViewById(R.id.title);
		if (tvTitle != null) {
			tvTitle.setText(resId);
		}
	}

	public void setTitle(String text) {
		TextView tvTitle = (TextView) findViewById(R.id.title);
		if (tvTitle != null) {
			tvTitle.setText(text);
		}
	}

	@Override
	protected void onStart() {
		isActive = true;
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isActive = false;
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void finish() {
		super.finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
	}

	public void defaultFinish() {
		super.finish();
	}

	@Override
	public void showToast(String text, int longint) {
		// TODO Auto-generated method stub
		if (isActive)
			Toast.makeText(context, text, longint).show();

	}

	@Override
	public void showToast(String text) {
		// TODO Auto-generated method stub
		if (isActive)
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showToast(int resId) {
		// TODO Auto-generated method stub
		if (isActive)
			Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void startActivity(Class<?> clazz, String typeId) {
		// TODO Auto-generated method stub
		Intent it = new Intent(context, clazz);
		it.putExtra("typeId", typeId);
		startActivity(it);
	}

	public void loginOut() {
		// rest/admin/loginout
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest strRequest = new StringRequest(Request.Method.POST,
				Config.USER_LOGINOUT_URL, new Listener<String>() {

					@SuppressLint("NewApi")
					@Override
					public void onResponse(String response) {
						Gson gson = new Gson();
						Result r = gson.fromJson(response, Result.class);
						if (r.getResultCode() == 1) {
							ll_user_login.setVisibility(View.GONE);
							bt_user_login_button.setVisibility(View.VISIBLE);
							MyApplication.instance.clearUserInfo();
							showToast("退出登录成功!");
							finish();
							Intent intent = new Intent(
									BaseFragmentActivity.this,
									HomeActivity.class);
							startActivity(intent);
							BaseFragmentActivity.this
									.overridePendingTransition(
											R.anim.anim_enter, R.anim.anim_exit);
						} else if (r.getResultCode() == 2) {
							ll_user_login.setVisibility(View.GONE);
							bt_user_login_button.setVisibility(View.VISIBLE);
							MyApplication.instance.clearUserInfo();
							showToast("退出登录成功!");
							finish();
							Intent intent = new Intent(
									BaseFragmentActivity.this,
									HomeActivity.class);
							startActivity(intent);
							BaseFragmentActivity.this
									.overridePendingTransition(
											R.anim.anim_enter, R.anim.anim_exit);
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", (String) userInfoSp.get("userId"));
				map.put("hardId", MyApplication.instance.getHardId());
				map.put("sessionId", (String) userInfoSp.get("sessionId"));
				return map;
			}
		};
		queue.add(strRequest);
	}

	public void changeLogin() {
		ll_user_login.setVisibility(View.GONE);
		bt_user_login_button.setVisibility(View.VISIBLE);
		MyApplication.instance.clearUserInfo();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (!MyApplication.instance.userInfoSp.getAll().isEmpty()) {
			DebugUtility.showLog("执行了。。。"
					+ Config.BASE_URL
					+ MyApplication.instance.userInfoSp.getAll().get(
							"thumbnail"));
			ll_user_login.setVisibility(View.VISIBLE);
			tv_username
					.setText((CharSequence) MyApplication.instance.userInfoSp
							.getAll().get("userName"));
			bt_user_login_button.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ MyApplication.instance.userInfoSp.getAll().get(
									"thumbnail"), iv_user_img);
		} else {
			ll_user_login.setVisibility(View.GONE);
			bt_user_login_button.setVisibility(View.VISIBLE);
		}
	}
}
