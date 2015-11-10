package com.ak.qmyd.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.User;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.ImageLoad;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.MediaManager;
import com.ak.qmyd.tools.StringUtil;
import com.ak.qmyd.tools.ToastManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CircleImageView;
import com.ak.qmyd.view.ContainsEmojiEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.upload.MultiPartStack;
import com.android.volley.upload.MultiPartStringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EditUserInfoActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ImageButton imgBtnBack;
	private TextView ib_complete, tv_sex;
	private RelativeLayout rl_user_head, rl_user_sex;
	private ContainsEmojiEditText edt_username, edt_user_jianjie;
	private EditText edt_height, edt_weight;
	private String username, userdesc, height, weight, sex;
	private String intsex = "1";
	private CircleImageView headView;
	Dialog selectHeadDialog, selectSexDialog;
	private Bitmap bm;
	private Map<String, Object> userInfoSp;
	private Dialog loadDialog;
	private static RequestQueue mSingleQueue;
	private DrawerLayout mDrawerLayout;
	int mMaxLenth = 10;// 设置允许输入的字符长度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_userinfo);
		ImageLoad.initImageLoader(this);
		userInfoSp = MyApplication.instance.getUserInfo();
		mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
		initData();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_message_center_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	void initView() {
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);
		headView = (CircleImageView) this.findViewById(R.id.iv_user_info_img);
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + userInfoSp.get("thumbnail"), headView);

		ib_complete = (TextView) this.findViewById(R.id.ib_complete);
		ib_complete.setOnClickListener(this);
		tv_sex = (TextView) this.findViewById(R.id.tv_sex);
		tv_sex.setText(sex);
		rl_user_head = (RelativeLayout) this.findViewById(R.id.rl_user_head);
		rl_user_head.setOnClickListener(this);
		rl_user_sex = (RelativeLayout) this.findViewById(R.id.rl_user_sex);
		rl_user_sex.setOnClickListener(this);

		edt_username = (ContainsEmojiEditText) this
				.findViewById(R.id.edt_username);
		edt_username.setText(username);
		edt_username.setSelection(edt_username.getText().length());

		edt_username.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = edt_username.getText().toString();
				String str = StringUtil.stringFilter(editable); // 过滤特殊字符
				if (!editable.equals(str)) {
					edt_username.setText(str);
				}
				edt_username.setSelection(edt_username.length());
				cou = edt_username.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = edt_username.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
					ToastManager.show(EditUserInfoActivity.this, "用户名不能超过十个字符");
				}
			}
		});

		edt_user_jianjie = (ContainsEmojiEditText) this
				.findViewById(R.id.edt_user_jianjie);
		edt_user_jianjie.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = edt_user_jianjie.getText().toString();
				String str = StringUtil.stringFilter(editable); // 过滤特殊字符
				if (!editable.equals(str)) {
					edt_user_jianjie.setText(str);
				}
				edt_user_jianjie.setSelection(edt_user_jianjie.length());
				cou = edt_user_jianjie.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cou > 200) {
					selectionEnd = edt_user_jianjie.getSelectionEnd();
					s.delete(200, selectionEnd);
					ToastManager.show(EditUserInfoActivity.this,
							"评论内容不能超过200个字符");
				}
			}
		});
		edt_user_jianjie.setText(userdesc);

		edt_height = (EditText) this.findViewById(R.id.edt_height);
		edt_height.setText(height);
		edt_height.setSelection(edt_height.getText().length());

		edt_weight = (EditText) this.findViewById(R.id.edt_weight);
		edt_weight.setText(weight);
		edt_weight.setSelection(edt_weight.getText().length());
	}

	void initData() {
		if (userInfoSp.get("userName") != null
				&& !userInfoSp.get("userName").equals("")) {
			username = userInfoSp.get("userName").toString();
		}
		if (userInfoSp.get("description") != null
				&& !userInfoSp.get("description").equals("")) {
			userdesc = userInfoSp.get("description").toString();
		}
		if (userInfoSp.get("height") != null
				&& !userInfoSp.get("height").equals("")) {
			height = userInfoSp.get("height").toString();
		}
		if (userInfoSp.get("weight") != null
				&& !userInfoSp.get("weight").equals("")) {
			weight = userInfoSp.get("weight").toString();
		}
		if (userInfoSp.get("sex") != null && !userInfoSp.get("sex").equals("")) {
			sex = userInfoSp.get("sex").toString();
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgBtnBack:
			finish();
			break;
		case R.id.ib_complete:
			username = edt_username.getText().toString();
			userdesc = edt_user_jianjie.getText().toString();
			height = edt_height.getText().toString().trim();
			weight = edt_weight.getText().toString().trim();

			if (sex.equals("女")) {
				intsex = "1";
			} else {
				intsex = "0";
			}

			int userheight = Integer.parseInt(height);
			int userweight = Integer.parseInt(weight);

			if (userheight > 0 && userheight < 250 && userweight > 0
					&& userweight < 500) {
				sendUserInfo(username, intsex, height, weight, userdesc);
			} else if (userheight <= 0 || userheight >= 250) {
				showToast("请输入正确的身高值");
			} else if (userweight <= 0 || userweight >= 600) {
				showToast("请输入正确的体重值");
			}

			break;
		case R.id.rl_user_head:
			View headselectview = View.inflate(this,
					R.layout.layout_action_sheet_select_userhead, null);
			selectHeadDialog = UIManager.getActionSheet(this, headselectview);
			selectHeadDialog.show();
			break;
		case R.id.rl_user_sex:

			View sexselectview = View.inflate(this,
					R.layout.layout_action_sheet_select_sex, null);
			selectSexDialog = UIManager.getCommonActionSheet(this,
					sexselectview);
			selectSexDialog.show();
			break;
		case R.id.action_sheet_take_photo: {
			MediaManager.getPhotoFromCamera(this);
			selectHeadDialog.dismiss();
			break;
		}
		case R.id.action_sheet_pick_photo: {
			MediaManager.getPhotoFromAlbum(this);
			selectHeadDialog.dismiss();
			break;
		}
		case R.id.action_sheet_male: {
			intsex = "0";
			sex = "男";
			tv_sex.setText("男");
			selectSexDialog.dismiss();
			break;
		}
		case R.id.action_sheet_pick_female: {
			intsex = "1";
			sex = "女";
			tv_sex.setText("女");
			selectSexDialog.dismiss();
			break;
		}
		case R.id.action_sheet_cancle: {
			showToast("取消");
			break;
		}
		}
	}

	private void sendUserInfo(final String name, final String intsex,
			final String height, final String weight, final String userdesc) {
		loadDialog = UIManager.getLoadingDialog(this);
		loadDialog.show();
		// TODO Auto-generated method stub
		String httpurl = Config.BASE_URL + "/api/rest/admin/updateUser";
		RequestQueue requestQueue = Volley
				.newRequestQueue(getApplicationContext());
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				httpurl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						DebugUtility.showLog("修改用户信息成功结果：" + response);
						parseRespense(response);
						UIManager.toggleDialog(loadDialog);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						DebugUtility.showLog("修改用户信息失败：" + error.getMessage());
						showToast("修改用户信息失败");
						UIManager.toggleDialog(loadDialog);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要post的参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("hardId", MyApplication.instance.getHardId());
				params.put("sessionId", userInfoSp.get("sessionId").toString());
				params.put("userId", userInfoSp.get("userId").toString());
				params.put("userName", name);
				params.put("sex", intsex);
				params.put("height", height);
				params.put("weight", weight);
				params.put("description", userdesc);
				return params;
			}
		};
		requestQueue.add(stringRequest);
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
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
				showToast("修改成功");
				String userInfo = JsonManager.getJsonItem(jsonObj, "user")
						.toString();
				User user = new Gson().fromJson(userInfo, User.class);
				MyApplication.instance.saveModifyUserInfo(user);
				Intent it = new Intent();
				setResult(Activity.RESULT_OK, it);
				finish();

			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		MediaManager.onActivityResult(this, handler, requestCode, resultCode,
				data);
	}

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.SHOW_PHOTO: {
				FileInputStream fis;
				try {
					fis = new FileInputStream((File) msg.obj);
					bm = BitmapFactory.decodeStream(fis);
					uploadHeadImage((File) msg.obj);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (bm == null) {
				} else {
					headView.setImageBitmap(bm);
				}

				break;
			}
			}
		}
	};

	void uploadHeadImage(File file) {
		Map<String, File> files = new HashMap<String, File>();
		files.put("body", file);

		Map<String, String> params = new HashMap<String, String>();
		params.put("token", "DJrlPbpJQs21rv1lP41yiA==");

		String uri = Config.BASE_URL + "/api/rest/admin/uploadUserImg/"
				+ MyApplication.instance.getHardId() + "/"
				+ userInfoSp.get("sessionId").toString() + "/"
				+ userInfoSp.get("userId").toString();

		addPutUploadFileRequest(uri, files, params, mResonseListenerString,
				mErrorListener, null);

	}

	public OnFocusChangeListener onFocusAutoClearHintListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			EditText textView = (EditText) v;
			String hint;
			if (hasFocus) {
				hint = textView.getHint().toString();
				textView.setTag(hint);
				textView.setHint("");
			} else {
				hint = textView.getTag().toString();
				textView.setHint(hint);
			}
		}
	};

	Listener<JSONObject> mResonseListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			DebugUtility.showLog(" on response json" + response.toString());
		}
	};

	Listener<String> mResonseListenerString = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			DebugUtility.showLog(" on response String" + response.toString());
		}
	};

	ErrorListener mErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (error != null) {
				if (error.networkResponse != null)
					DebugUtility.showLog(" error "
							+ new String(error.networkResponse.data));
			}
		}
	};

	public static void addPutUploadFileRequest(final String url,
			final Map<String, File> files, final Map<String, String> params,
			final Listener<String> responseListener,
			final ErrorListener errorListener, final Object tag) {
		if (null == url || null == responseListener) {
			return;
		}

		MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
				Request.Method.POST, url, responseListener, errorListener) {

			@Override
			public Map<String, File> getFileUploads() {
				return files;
			}

			@Override
			public Map<String, String> getStringUploads() {
				return params;
			}

		};
		DebugUtility.showLog(" volley put : uploadFile " + url);

		mSingleQueue.add(multiPartRequest);
	}

}
