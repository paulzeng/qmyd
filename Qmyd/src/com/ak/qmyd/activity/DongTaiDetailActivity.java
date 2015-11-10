package com.ak.qmyd.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.ImageAdapter;
import com.ak.qmyd.activity.adapter.ListDongTaiContentAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.DongTaiContentBean;
import com.ak.qmyd.bean.ImagePath;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.StringUtil;
import com.ak.qmyd.tools.ToastManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CircleImageView;
import com.ak.qmyd.view.ContainsEmojiEditText;
import com.ak.qmyd.view.MyGridView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DongTaiDetailActivity extends BaseFragmentActivity implements

OnClickListener, OnItemClickListener, OnEditorActionListener {

	private int mMaxLenth = 200;// 设置允许输入的字符长度

	private ImageButton imgBtnBack;

	private MyGridView gridview;

	private String id, staffId;

	private View headView;

	private ListView dongtai_pinglun_list;

	private ListDongTaiContentAdapter mAdapter;

	private List<DongTaiContentBean> mData;

	private ArrayList<ImagePath> data;

	private ArrayList<InfoPraiseList> pdata;

	private TextView emptyView, headName, headContent, headTime, tv_zan_count;

	private CircleImageView headImageView, iv_head_img1, iv_head_img2,

	iv_head_img3;

	private RequestQueue mRequestQueue;

	private Map<String, ?> userInfoSp;

	private Dialog loadDialog;

	private ImageView iv_head_more, iv_circle_note_head_zan_img;

	private String state;// 赞的状态

	private DrawerLayout mDrawerLayout;

	private ContainsEmojiEditText et_content_note_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dongtai_detail);

		// 侧滑

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

		setEnableDrawerLayout(mDrawerLayout);

		loadDialog = UIManager.getLoadingDialog(this);

		initView();

		initData();

	}

	void initData() {

		id = this.getIntent().getStringExtra("id");

		staffId = this.getIntent().getStringExtra("staffId");

		httpGetJson(staffId, id);

	}

	void initView() {

		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);

		imgBtnBack.setOnClickListener(this);

		headView = LayoutInflater.from(this).inflate(

		R.layout.dongtai_detail_head, null);

		dongtai_pinglun_list = (ListView) this

		.findViewById(R.id.lv_dongtai_content_list);

		mData = new ArrayList<DongTaiContentBean>();

		data = new ArrayList<ImagePath>();

		pdata = new ArrayList<InfoPraiseList>();

		mAdapter = new ListDongTaiContentAdapter(this, mData);

		dongtai_pinglun_list.setAdapter(mAdapter);

		dongtai_pinglun_list.addHeaderView(headView);

		et_content_note_edit = (ContainsEmojiEditText) this
				.findViewById(R.id.et_content_note_edit);

		et_content_note_edit.setOnEditorActionListener(this);

		et_content_note_edit.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = et_content_note_edit.getText().toString();
				String str = StringUtil.stringFilter(editable); // 过滤特殊字符
				if (!editable.equals(str)) {
					et_content_note_edit.setText(str);
				}
				et_content_note_edit.setSelection(et_content_note_edit.length());
				cou = et_content_note_edit.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = et_content_note_edit.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
					ToastManager.show(DongTaiDetailActivity.this,
							"评论内容不能超过200个字符");
				}
			}
		});

		initHeader();

	}

	void initHeader() {

		gridview = (MyGridView) headView.findViewById(R.id.grid_image);//

		gridview.setAdapter(new ImageAdapter(this, data));

		gridview.setOnItemClickListener(this);

		headImageView = (CircleImageView) headView

		.findViewById(R.id.iv_head_img);

		headImageView.setOnClickListener(this);

		iv_head_img1 = (CircleImageView) headView

		.findViewById(R.id.iv_head_img1);

		iv_head_img1.setOnClickListener(this);

		iv_head_img2 = (CircleImageView) headView

		.findViewById(R.id.iv_head_img2);

		iv_head_img2.setOnClickListener(this);

		iv_head_img3 = (CircleImageView) headView

		.findViewById(R.id.iv_head_img3);

		iv_head_img3.setOnClickListener(this);

		iv_head_more = (ImageView) headView.findViewById(R.id.iv_head_more);

		tv_zan_count = (TextView) headView.findViewById(R.id.tv_zan_count);

		headName = (TextView) headView.findViewById(R.id.tv_head_name);
		headContent = (TextView) headView.findViewById(R.id.tv_head_content);
		headTime = (TextView) headView.findViewById(R.id.tv_head_time);
		iv_circle_note_head_zan_img = (ImageView) this
				.findViewById(R.id.iv_circle_note_head_zan_img);

		iv_circle_note_head_zan_img.setOnClickListener(this);

	}

	private void setEmptyView() {

		emptyView = new TextView(this);

		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,

		LayoutParams.MATCH_PARENT));

		emptyView.setText("暂无评论");

		emptyView.setTextSize(20);

		emptyView.setGravity(Gravity.CENTER_HORIZONTAL

		| Gravity.CENTER_VERTICAL);

		emptyView.setVisibility(View.GONE);

		((ViewGroup) dongtai_pinglun_list.getParent()).addView(emptyView);

		dongtai_pinglun_list.setEmptyView(emptyView);

	}

	void httpGetJson(String staffId, String id) {

		loadDialog.show();

		userInfoSp = MyApplication.instance.userInfoSp.getAll();

		mRequestQueue = Volley.newRequestQueue(this);

		String url = Config.BASE_URL

		+ "/api/rest/circle/queryDongTaiDetailInfo" + "/"

		+ userInfoSp.get("userId") + "/" + staffId + "/" + id + "/"

		+ userInfoSp.get("sessionId") + "/"

		+ MyApplication.instance.getHardId();

		DebugUtility.showLog("请求URL:" + url);

		if (NetManager.isNetworkConnected(this)) {

			StringRequest request = new StringRequest(Request.Method.GET, url,

			new Listener<String>() {

				@Override
				public void onResponse(String response) {

					// TODO Auto-generated method stub

					parseRespense(response);

					UIManager.toggleDialog(loadDialog);

				}

			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {

					// TODO Auto-generated method stub

					showToast("获取数据失败");

					UIManager.toggleDialog(loadDialog);

				}

			}) {

				@Override
				protected Map<String, String> getParams()

				throws AuthFailureError {

					// TODO Auto-generated method stub

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

		JSONObject staffDongTaiObj;

		try {

			jsonObj = new JSONObject(response);

			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")

			.toString();

			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")

			.toString();

			if (resultCode.equals("1")) {

				String staffDongTai = JsonManager.getJsonItem(jsonObj,

				"staffDongTai").toString();

				staffDongTaiObj = new JSONObject(staffDongTai);

				state = JsonManager.getJsonItem(staffDongTaiObj, "state")

				.toString();// 赞状态（1：已赞；2：未赞）

				if (state.equals("1")) {

					iv_circle_note_head_zan_img

					.setBackgroundResource(R.drawable.zan_y);

				} else {

					iv_circle_note_head_zan_img

					.setBackgroundResource(R.drawable.zan_g_);

				}

				String infoCommentList = JsonManager.getJsonItem(

				staffDongTaiObj, "infoCommentList").toString();

				if (infoCommentList != null && !infoCommentList.equals("null")) {
					mData = new Gson().fromJson(infoCommentList,

					new TypeToken<List<DongTaiContentBean>>() {

					}.getType());
				}
				String infoPicList = JsonManager.getJsonItem(staffDongTaiObj,

				"infoPicList").toString();

				data = new Gson().fromJson(infoPicList,

				new TypeToken<List<ImagePath>>() {

				}.getType());

				String infoPraiseList = JsonManager.getJsonItem(

				staffDongTaiObj, "infoPraiseList").toString();

				pdata = new Gson().fromJson(infoPraiseList,

				new TypeToken<List<InfoPraiseList>>() {

				}.getType());

				String praiseNum = JsonManager.getJsonItem(staffDongTaiObj,

				"praiseNum").toString();

				tv_zan_count.setText(praiseNum + "赞");

				String userName = JsonManager.getJsonItem(staffDongTaiObj,

				"userName").toString();

				headName.setText(userName);

				String content = JsonManager.getJsonItem(staffDongTaiObj,

				"content").toString();

				if (content.equals("null")) {
					headContent.setVisibility(View.GONE);
				} else {
					headContent.setText(content);
				}

				// headContent.setText(content);

				String headurl = JsonManager.getJsonItem(staffDongTaiObj,

				"thumbnailPath").toString();

				ImageLoader.getInstance().displayImage(

				Config.BASE_URL + headurl, headImageView);

				String createTime = JsonManager.getJsonItem(staffDongTaiObj,

				"createTime").toString();

				headTime.setText(createTime);

				if ((new SimpleDateFormat("MM-dd", Locale.getDefault())

				.format(new Date()))

				.equals(createTime.substring(5, 10))) {

					headTime.setText("今天" + createTime.substring(10));

				} else {

					headTime.setText(createTime.substring(0, 10));

				}

				// DebugUtility.showLog("评论列表：" + mData.toString());

			} else if (resultCode.equals("0")) {

				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");

			} else if (resultCode.equals("3")) {

				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);

			} else if (resultCode.equals("10000")) {

				DebugUtility.showLog("resultCode=" + resultCode

				+ "未登陆或登陆超时，请重新登陆");

				Intent intent = new Intent(this, LoginActivity.class);

				intent.putExtra("flag", "1");

				startActivity(intent);

			}

			// mAdapter.notifyDataSetChanged();

			if (mData != null) {

				mAdapter = new ListDongTaiContentAdapter(this, mData);

				dongtai_pinglun_list.setAdapter(mAdapter);

			}

			if (data != null) {

				gridview.setAdapter(new ImageAdapter(this, data));

			}

			if (pdata != null) {

				if (pdata.size() == 1) {

					iv_head_img1.setVisibility(View.VISIBLE);
					iv_head_img2.setVisibility(View.GONE);
					iv_head_img3.setVisibility(View.GONE);
					iv_head_more.setVisibility(View.GONE);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(0).getThumbnailPath(),
							iv_head_img1);

				} else if (pdata.size() == 2) {
					iv_head_img1.setVisibility(View.VISIBLE);
					iv_head_img2.setVisibility(View.VISIBLE);
					iv_head_img3.setVisibility(View.GONE);
					iv_head_more.setVisibility(View.GONE);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(0).getThumbnailPath(),
							iv_head_img1);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(1).getThumbnailPath(),
							iv_head_img2);

				} else if (pdata.size() == 3) {

					iv_head_img1.setVisibility(View.VISIBLE);
					iv_head_img2.setVisibility(View.VISIBLE);
					iv_head_img3.setVisibility(View.VISIBLE);
					iv_head_more.setVisibility(View.VISIBLE);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(0).getThumbnailPath(),
							iv_head_img1);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(1).getThumbnailPath(),
							iv_head_img2);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(2).getThumbnailPath(),
							iv_head_img3);

				} else if (pdata.size() > 3) {
					iv_head_img1.setVisibility(View.VISIBLE);
					iv_head_img2.setVisibility(View.VISIBLE);
					iv_head_img3.setVisibility(View.VISIBLE);
					iv_head_more.setVisibility(View.VISIBLE);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(0).getThumbnailPath(),
							iv_head_img1);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(1).getThumbnailPath(),
							iv_head_img2);

					ImageLoader.getInstance().displayImage(

					Config.BASE_URL + pdata.get(2).getThumbnailPath(),
							iv_head_img3);

				}

			} else {

				iv_head_more.setVisibility(View.GONE);

				iv_head_img1.setVisibility(View.GONE);

				iv_head_img2.setVisibility(View.GONE);

				iv_head_img3.setVisibility(View.GONE);

			}

			// setEmptyView();

		} catch (JSONException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,

	long id) {

		Intent intent = new Intent(this, PhotoPreviewActivity.class);

		intent.putExtra("photos", data);

		DebugUtility.showLog("查看的图片：" + data.toString());

		intent.putExtra("position", position);

		startActivity(intent);

	}

	@Override
	public void onClick(View view) {

		// TODO Auto-generated method stub

		switch (view.getId()) {

		case R.id.imgBtnBack:
			if (this.getIntent().getIntExtra("flag", 0) == 3) {
				startActivity(HomeActivity.class, null);
			} else {
				finish();
			}

			break;

		case R.id.iv_circle_note_head_zan_img:

			// TODO 点赞

			httpDianZan();

			break;

		case R.id.iv_head_img:

			Intent intent = new Intent(this, GusterInfoActivity.class);

			intent.putExtra("userid", staffId);

			this.startActivity(intent);

			break;
		case R.id.iv_head_img1:

			// TODO 点赞
			Intent intent1 = new Intent(this, GusterInfoActivity.class);

			intent1.putExtra("userid", pdata.get(0).getUserId());

			this.startActivity(intent1);

			break;
		case R.id.iv_head_img2:
			// TODO 点赞
			Intent intent2 = new Intent(this, GusterInfoActivity.class);

			intent2.putExtra("userid", pdata.get(1).getUserId());

			this.startActivity(intent2);

			break;
		case R.id.iv_head_img3:

			// TODO 点赞
			Intent intent3 = new Intent(this, GusterInfoActivity.class);

			intent3.putExtra("userid", pdata.get(2).getUserId());

			this.startActivity(intent3);

			break;

		}

	}

	void httpDianZan() {

		// loadDialog.show();

		userInfoSp = MyApplication.instance.userInfoSp.getAll();

		mRequestQueue = Volley.newRequestQueue(this);

		String url = Config.BASE_URL
				+ "/api/rest/circle/pariseDongTaiOperation";

		DebugUtility.showLog("请求URL:" + url);

		if (NetManager.isNetworkConnected(this)) {

			StringRequest stringRequest = new StringRequest(

			Request.Method.POST, url, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {

					DebugUtility.showLog("点赞成功结果：" + response);

					parseZanRespense(response);

					// UIManager.toggleDialog(loadDialog);

				}

			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {

					DebugUtility.showLog("点赞失败：" + error.getMessage());

					// UIManager.toggleDialog(loadDialog);

				}

			}) {

				@Override
				protected Map<String, String> getParams() {

					// 在这里设置需要post的参数

					Map<String, String> params = new HashMap<String, String>();

					params.put("hardId", MyApplication.instance.getHardId());

					params.put("sessionId", userInfoSp.get("sessionId")

					.toString());

					params.put("userId", userInfoSp.get("userId").toString());

					params.put("infoId", id);

					params.put("state", state);

					return params;

				}

			};

			mRequestQueue.add(stringRequest);

		} else {

			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();

		}

	}

	void parseZanRespense(String response) {

		JSONObject jsonObj;

		try {

			jsonObj = new JSONObject(response);

			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")

			.toString();

			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")

			.toString();

			if (resultCode.equals("1")) {
				// String praiseInfo = JsonManager.getJsonItem(jsonObj,
				// "praiseInfo")
				// .toString();
				// jsonObj = new JSONObject(praiseInfo);
				// String praiseNum = JsonManager.getJsonItem(jsonObj,
				// "praiseNum")
				// .toString();
				// String state = JsonManager.getJsonItem(jsonObj, "state")
				// .toString();

				// 刷新UI
				if (state.equals("1")) {
					// showToast("已取消赞");
				} else {
					// showToast("已赞");

				}

				httpGetJson(staffId, id);

			} else if (resultCode.equals("0")) {

				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");

			} else if (resultCode.equals("3")) {

				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);

			} else if (resultCode.equals("10000")) {

				DebugUtility.showLog("resultCode=" + resultCode

				+ "未登陆或登陆超时，请重新登陆");

				Intent intent = new Intent(this, LoginActivity.class);

				intent.putExtra("flag", "1");

				startActivity(intent);

			}

		} catch (JSONException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	private class InfoPraiseList {

		private String thumbnailPath, userId;

		public String getThumbnailPath() {

			return thumbnailPath;

		}

		public void setThumbnailPath(String thumbnailPath) {

			this.thumbnailPath = thumbnailPath;

		}

		public String getUserId() {

			return userId;

		}

		public void setUserId(String userId) {

			this.userId = userId;

		}

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (actionId) {
		case EditorInfo.IME_NULL:
			System.out.println("null for default_content: " + v.getText());
			break;
		case EditorInfo.IME_ACTION_SEND:
			String content = et_content_note_edit.getText().toString();
			if (content.trim().equals("") || content == null) {
				ToastManager.show(DongTaiDetailActivity.this, "请输入内容");
			} else {
				httpSendMessage(content);
			}
			break;
		case EditorInfo.IME_ACTION_DONE:
			System.out
					.println("action done for number_content: " + v.getText());
			break;
		}
		return true;
	}

	void httpSendMessage(final String content) {
		loadDialog.show();
		String httpurl = Config.BASE_URL + "/api/rest/circle/replayDongTai";
		RequestQueue requestQueue = Volley
				.newRequestQueue(getApplicationContext());
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				httpurl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						DebugUtility.showLog("提交评论成功结果：" + response);
						parseContentRespense(response);
						UIManager.toggleDialog(loadDialog);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						DebugUtility.showLog("提交评论失败：" + error.getMessage());
						UIManager.toggleDialog(loadDialog);
						showToast("发送失败");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要post的参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("hardId", MyApplication.instance.getHardId());
				params.put("sessionId", userInfoSp.get("sessionId").toString());
				params.put("userId", userInfoSp.get("userId").toString());
				params.put("replayContent", content);
				params.put("infoId", id);
				return params;
			}
		};
		requestQueue.add(stringRequest);
	}

	void parseContentRespense(String response) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
				showToast("发送完成");
				// 刷新UI
				String infoCommentList = JsonManager.getJsonItem(jsonObj,
						"infoCommentList").toString();
				if (mData != null && mData.size() > 0) {
					mData.clear();
				}

				List<DongTaiContentBean> mNewData = new Gson().fromJson(
						infoCommentList,
						new TypeToken<List<DongTaiContentBean>>() {
						}.getType());
				DebugUtility.showLog("获取评论列表" + mNewData.toString());
				mData.addAll(mNewData);
				mAdapter.notifyDataSetChanged();
				et_content_note_edit.setText("");
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (this.getIntent().getIntExtra("flag", 0) == 3) {
				startActivity(HomeActivity.class, null);
				finish();
			} else {
				finish();
			}
		}
		return false;
	}
}
