package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.util.DebugUtils;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.PersonDynamicAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.activity.listener.ListItemClickHelp;
import com.ak.qmyd.bean.DongTaiContentBean;
import com.ak.qmyd.bean.ImagePath;
import com.ak.qmyd.bean.StaffInfo;
import com.ak.qmyd.bean.StaffInfo.InfoPraise;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.dialog.DeleteDialog;
import com.ak.qmyd.dialog.DeleteDialog.OnActionSheetSelected;
import com.ak.qmyd.tools.BitmapUtils;
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

public class GusterInfoActivity extends BaseFragmentActivity implements
		OnClickListener, ListItemClickHelp, OnEditorActionListener,
		OnScrollListener {
	private ImageButton in_user_info_return;
	private TextView emptyView, tv_user_name, tv_user_info_introduction,
			tv_user_info_focus, tv_user_info_fans, tv_my_info_edit,
			tv_guster_info_edit, tv_my_info_focus;
	// private PullToRefreshListView mPullRefreshListView;
	private ListView lv_user_info_Dynamic;
	private DrawerLayout mDrawerLayout;
	private CircleImageView iv_user_info_img;
	private Map<String, ?> userInfoSp;
	private String userid;
	private RequestQueue mRequestQueue;
	private List<StaffInfo> mDate;
	private List<ImagePath> mImagePathDate;
	private List<ImagePath> mNewImagePathDate;
	private PersonDynamicAdapter mAdapter;
	private MyGridView gridview_sport;
	private View headView;
	private Dialog loadDialog;
	private String staffId;
	private ImageView iv_sport_more;
	private String state;// 赞的状态
	private List<InfoPraise> mInfoPraiseList;
	private ContainsEmojiEditText et_content_edit;
	private LinearLayout ll_edt_content;
	private static int currentPos;
	private View loadMoreView;
	private TextView tv_load_more;
	private boolean flag = false;// 保证scroll初次不执行，直到网络数据请求完成在执行
	private int currentNumber = 10;
	private int currentPage = 1;
	private int mMaxLenth = 200;// 设置允许输入的字符长度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guster_info);
		loadDialog = UIManager.getLoadingDialog(this);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		BitmapUtils.initImageLoader(getApplicationContext());
		findView();
		// initView();
		initData();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_user_info_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initData() {
		userid = getIntent().getStringExtra("userid");
		httpGetJson(userid, "1", "10");
	}

	private void findView() {
		loadMoreView = getLayoutInflater().inflate(R.layout.layout_load_more,
				null);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);

		headView = LayoutInflater.from(this).inflate(
				R.layout.person_info_header, null);
		iv_sport_more = (ImageView) headView.findViewById(R.id.iv_sport_more);
		iv_sport_more.setOnClickListener(this);
		iv_user_info_img = (CircleImageView) headView
				.findViewById(R.id.iv_user_info_img);
		in_user_info_return = (ImageButton) this
				.findViewById(R.id.in_user_info_return);
		tv_user_name = (TextView) headView.findViewById(R.id.tv_user_name);
		tv_user_info_introduction = (TextView) headView
				.findViewById(R.id.tv_user_info_introduction);
		tv_user_info_focus = (TextView) headView
				.findViewById(R.id.tv_user_info_focus);
		tv_user_info_fans = (TextView) headView
				.findViewById(R.id.tv_user_info_fans);
		in_user_info_return.setOnClickListener(this);
		lv_user_info_Dynamic = (ListView) findViewById(R.id.lv_user_info_Dynamic);
		mDate = new ArrayList<StaffInfo>();
		mAdapter = new PersonDynamicAdapter(this, mDate, this);
		lv_user_info_Dynamic.setAdapter(mAdapter);
		lv_user_info_Dynamic.addHeaderView(headView);
		lv_user_info_Dynamic.addFooterView(loadMoreView, null, false);
		lv_user_info_Dynamic.setOnScrollListener(this);
		lv_user_info_Dynamic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GusterInfoActivity.this,
						DongTaiDetailActivity.class);
				intent.putExtra("id", mDate.get(position - 1).getMyInfoId());
				intent.putExtra("staffId", staffId);
				startActivity(intent);
			}
		});

		tv_my_info_edit = (TextView) this.findViewById(R.id.tv_my_info_edit);
		tv_guster_info_edit = (TextView) this
				.findViewById(R.id.tv_guster_info_edit);
		tv_my_info_focus = (TextView) this.findViewById(R.id.tv_my_info_focus);
		gridview_sport = (MyGridView) this.findViewById(R.id.gridview_sport);
		et_content_edit = (ContainsEmojiEditText) this
				.findViewById(R.id.et_content_edit);
		et_content_edit.setOnEditorActionListener(this);
		et_content_edit.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = et_content_edit.getText().toString();
				String str = StringUtil.stringFilter(editable); // 过滤特殊字符
				if (!editable.equals(str)) {
					et_content_edit.setText(str);
				}
				et_content_edit.setSelection(et_content_edit.length());
				cou = et_content_edit.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = et_content_edit.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
					ToastManager
							.show(GusterInfoActivity.this, "评论内容不能超过200个字符");
				}
			}
		});
		ll_edt_content = (LinearLayout) this.findViewById(R.id.ll_edt_content);
	}

	void initView() {
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + (CharSequence) userInfoSp.get("thumbnail"),
				iv_user_info_img);
		tv_user_name.setText((CharSequence) userInfoSp.get("userName"));
		String description = (String) userInfoSp.get("description");
		tv_user_info_introduction.setText("简介："
				+ (description == null ? "暂无简介" : description));
		tv_user_info_focus.setText("关注："
				+ (CharSequence) userInfoSp.get("interest"));
		tv_user_info_fans
				.setText("粉丝：" + (CharSequence) userInfoSp.get("fans"));
	}

	void httpGetJson(String id, String currPage, String size) {
		loadDialog.show();
		mRequestQueue = Volley.newRequestQueue(this);
		String url = Config.BASE_URL + "/api/rest/circle/queryUserPartInfo"
				+ "/" + userInfoSp.get("userId") + "/" + id + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currPage + "/"
				+ size;
		DebugUtility.showLog("请求URL:" + url);
		if (NetManager.isNetworkConnected(this)) {
			loadDialog.show();
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
							flag = false;
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
			UIManager.toggleDialog(loadDialog);
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
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
				String userPartInfo = JsonManager.getJsonItem(jsonObj,
						"userPartInfo").toString();
				jsonObj = new JSONObject(userPartInfo);
				String userName = JsonManager.getJsonItem(jsonObj, "userName")
						.toString();
				tv_user_name.setText(userName);
				staffId = JsonManager.getJsonItem(jsonObj, "userId").toString();
				String descirption = JsonManager.getJsonItem(jsonObj,
						"description").toString();
				tv_user_info_introduction.setText("简介：" + descirption);
				String thumbnailPath = JsonManager.getJsonItem(jsonObj,
						"thumbnailPath").toString();
				ImageLoader.getInstance().displayImage(
						Config.BASE_URL + thumbnailPath, iv_user_info_img);
				String interestNum = JsonManager.getJsonItem(jsonObj,
						"interestNum").toString();
				tv_user_info_focus.setText("关注：" + interestNum);
				String haveNum = JsonManager.getJsonItem(jsonObj, "haveNum")
						.toString();
				tv_user_info_fans.setText("粉丝：" + haveNum);
				String state = JsonManager.getJsonItem(jsonObj, "state")
						.toString();
				if (state.equals("0")) {// 自己
					tv_my_info_edit.setVisibility(View.VISIBLE);
					tv_my_info_edit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(GusterInfoActivity.this,
									EditUserInfoActivity.class);
							startActivityForResult(intent, 0);
						}
					});
				} else if (state.equals("1")) {// 已关注
					tv_my_info_focus.setVisibility(View.VISIBLE);
				} else {// 未关注
					tv_guster_info_edit.setVisibility(View.VISIBLE);
					tv_guster_info_edit
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									httpAddFocus();
								}
							});
				}
				String sportsTypeList = JsonManager.getJsonItem(jsonObj,
						"sportsTypeList").toString();
				DebugUtility.showLog("关注的运动：" + sportsTypeList);
				mImagePathDate = new Gson().fromJson(sportsTypeList,
						new TypeToken<List<ImagePath>>() {
						}.getType());

				String staffInfoList = JsonManager.getJsonItem(jsonObj,
						"staffInfoList").toString();

				List<StaffInfo> mNewDate = new Gson().fromJson(staffInfoList,
						new TypeToken<List<StaffInfo>>() {
						}.getType());

				if (mNewDate != null) {
					mDate.addAll(mNewDate);
					if (mNewDate.size() < currentNumber) {
						loadMoreView.setVisibility(View.GONE);
						loadMoreView.setPadding(0, -loadMoreView.getHeight(),
								0, 0);
						flag = false;
						// ToastManager.show(this, "加载完成");
					} else {
						loadMoreView.setVisibility(View.VISIBLE);
						tv_load_more.setText("加载更多...");
						currentPage++;
						flag = true;
					}
				}

				mAdapter.notifyDataSetChanged();
				if (mDate.size() > 0) {

				} else {
					loadMoreView.setVisibility(View.VISIBLE);
					tv_load_more.setText("暂无动态");
				}

			} else if (resultCode.equals("0")) {
				flag = false;
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				flag = false;
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				flag = false;
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra("flag", "1");
				startActivity(intent);
			}
			if (mImagePathDate != null) {
				gridview_sport.setColumnWidth(dpToPx(25));
				if (mImagePathDate.size() > 5) {
					mNewImagePathDate = mImagePathDate.subList(0, 5);
					gridview_sport.setAdapter(new ImageAdapter(
							mNewImagePathDate));
					iv_sport_more.setVisibility(View.VISIBLE);
				} else {
					gridview_sport.setAdapter(new ImageAdapter(mImagePathDate));
					iv_sport_more.setVisibility(View.GONE);
				}

			}
			// setEmptyView();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void httpAddFocus() {
		loadDialog.show();
		String httpurl = Config.BASE_URL
				+ "/api/rest/circle/addOrDelRelationPerson";
		DebugUtility.showLog("请求URL:" + httpurl);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest stringRequest = new StringRequest(
					Request.Method.POST, httpurl,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("提交取消关注结果：" + response);
							parseAddRespense(response);
							UIManager.toggleDialog(loadDialog);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							DebugUtility.showLog("提交取消关注失败："
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
							showToast("发送失败");
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
					params.put("staffId", userid);
					params.put("state", "2");
					return params;
				}
			};
			mRequestQueue.add(stringRequest);
		} else {
			UIManager.toggleDialog(loadDialog);
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
		}
	}

	void parseAddRespense(String response) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				showToast("添加关注成功");
				tv_my_info_focus.setVisibility(View.VISIBLE);
				tv_guster_info_edit.setVisibility(View.GONE);

			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultInfo + "获取消息异常");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无动态");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_user_info_Dynamic.getParent()).addView(emptyView);
		lv_user_info_Dynamic.setEmptyView(emptyView);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.in_user_info_return:
			finish();
			break;
		case R.id.tv_user_info_edit:
			Intent intent = new Intent(this, EditUserInfoActivity.class);
			startActivity(intent);
			break;

		case R.id.iv_sport_more:
			Intent intentMore = new Intent(this, SportListActivity.class);
			intentMore.putExtra("staffId", staffId);
			startActivity(intentMore);
			break;
		}
	}

	void httpDelete(final String infoId, final int position) {
		loadDialog.show();
		mRequestQueue = Volley.newRequestQueue(this);
		String url = Config.BASE_URL + "/api/rest/circle/deleteDongTaiInfo";
		DebugUtility.showLog("请求URL:" + url);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest stringRequest = new StringRequest(
					Request.Method.POST, url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("提交删除动态结果：" + response);
							parseDelRespense(response, position);
							UIManager.toggleDialog(loadDialog);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							DebugUtility.showLog("提交删除动态失败："
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
							showToast("发送失败");
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
					params.put("infoId", infoId);
					return params;
				}
			};
			mRequestQueue.add(stringRequest);
		} else {
			UIManager.toggleDialog(loadDialog);
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
		}

	}

	void parseDelRespense(String response, int position) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				showToast("删除动态成功");
				mDate.remove(position);
				if (mDate.size() > 0) {

				} else {
					tv_load_more.setText("暂无动态");
				}
				mAdapter.notifyDataSetChanged();
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultInfo + "获取消息异常");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		initData();
	}

	private class ImageAdapter extends BaseAdapter {
		private List<ImagePath> data;

		public ImageAdapter(List<ImagePath> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			CircleImageView imageview;
			if (convertView == null) {
				imageview = new CircleImageView(GusterInfoActivity.this);
				imageview.setLayoutParams(new GridView.LayoutParams(dpToPx(25),
						dpToPx(25)));
				imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageview.setPadding(8, 8, 8, 8);
			} else {
				imageview = (CircleImageView) convertView;
			}
			// imageview.setImageResource(mThumbIds[position]);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + data.get(position).getImagePath(),
					imageview);
			return imageview;
		}
	}

	public int dpToPx(int dpValue) {
		float scale = GusterInfoActivity.this.getResources()
				.getDisplayMetrics().density;
		return Math.round(dpValue * scale);
	}

	@Override
	public void onClick(View item, View widget, final int position, int which) {
		// TODO Auto-generated method stub
		switch (which) {
		case R.id.ll_item_dynamic_reply:
			ll_edt_content.setVisibility(View.VISIBLE);
			et_content_edit.requestFocus();
			currentPos = position;
			// 打开软键盘
			InputMethodManager imm = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.ll_item_dynamic_dianzan:
			state = mDate.get(position).getState();
			httpDianZan(mDate.get(position).getMyInfoId(), state, position);
			break;
		case R.id.iv_delete:
			DeleteDialog.showSheet(this, new OnActionSheetSelected() {

				@Override
				public void onClick(int which) {
					switch (which) {
					case 0:
						DebugUtility.showLog("取消删除"); 
						break;
					case 1:
						httpDelete(mDate.get(position).getMyInfoId(), position);
						break;
					default:
						break;
					}

				}
			}, null, "确定删除这条动态吗?");
			
			break;
		default:
			break;
		}
	}

	void httpDianZan(final String infoId, final String state, final int position) {
		loadDialog.show();
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
							parseZanRespense(response, position);
							UIManager.toggleDialog(loadDialog);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							DebugUtility.showLog("点赞失败：" + error.getMessage());
							UIManager.toggleDialog(loadDialog);
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
					params.put("infoId", infoId);
					params.put("state", state);
					return params;
				}
			};
			mRequestQueue.add(stringRequest);
		} else {
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
		}

	}

	void parseZanRespense(String response, int position) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				String praiseInfo = JsonManager.getJsonItem(jsonObj,
						"praiseInfo").toString();
				jsonObj = new JSONObject(praiseInfo);
				String praiseNum = JsonManager
						.getJsonItem(jsonObj, "praiseNum").toString();
				String state = JsonManager.getJsonItem(jsonObj, "state")
						.toString();
				mDate.get(position).setPraiseNum(praiseNum);
				mDate.get(position).setState(state);

				String infoPraiseList = JsonManager.getJsonItem(jsonObj,
						"infoPraiseList").toString();
				mInfoPraiseList = new Gson().fromJson(infoPraiseList,
						new TypeToken<List<InfoPraise>>() {
						}.getType());
				mDate.get(position).setInfoPraiseList(mInfoPraiseList);

				// 刷新UI
				if (state.equals("2")) {
					// showToast("已取消赞");
				} else {
					// showToast("已赞");
				}
				mAdapter.notifyDataSetChanged();
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

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
				ll_edt_content.setVisibility(View.GONE);
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
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
			String content = et_content_edit.getText().toString();
			if (content.trim().equals("") || content == null) {
				ToastManager.show(this, "请输入内容");
			} else {
				httpSendMessage(content, currentPos);
			}
			break;
		case EditorInfo.IME_ACTION_DONE:
			System.out
					.println("action done for number_content: " + v.getText());
			break;
		}
		return true;
	}

	void httpSendMessage(final String content, final int position) {
		loadDialog.show();
		String httpurl = Config.BASE_URL + "/api/rest/circle/replayDongTai";
		RequestQueue requestQueue = Volley
				.newRequestQueue(getApplicationContext());
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				httpurl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						DebugUtility.showLog("提交评论成功结果：" + response);
						parseContentRespense(response, position);
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
				params.put("infoId", mDate.get(position).getMyInfoId());
				return params;
			}
		};
		requestQueue.add(stringRequest);
	}

	void parseContentRespense(String response, int position) {
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
				et_content_edit.setText("");
				ll_edt_content.setVisibility(View.GONE);
				// 刷新UI
				String infoCommentList = JsonManager.getJsonItem(jsonObj,
						"infoCommentList").toString();
				List<DongTaiContentBean> mNewData = new Gson().fromJson(
						infoCommentList,
						new TypeToken<List<DongTaiContentBean>>() {
						}.getType());
				mDate.get(position).setReplayNum(mNewData.size() + "");
				mDate.get(position).setInfoCommentList(mNewData);
				mAdapter.notifyDataSetChanged();
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

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		// 当列表滑动到列表底部时且正处于停止状态，执行
		if (flag) {
			DebugUtility.showLog(firstVisibleItem + "..." + visibleItemCount
					+ "..." + totalItemCount);
			if ((firstVisibleItem + visibleItemCount) == (totalItemCount)) {
				flag = false;
				loadMore();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	}

	void loadMore() {
		DebugUtility.showLog("加载更多");
		httpGetJson(userid, currentPage + "", "10");
	}
}
