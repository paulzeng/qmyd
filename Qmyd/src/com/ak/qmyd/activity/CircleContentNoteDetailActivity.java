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
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.ImageAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.ImagePath;
import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.bean.result.CircleDongTaiResult;
import com.ak.qmyd.bean.result.LoginResult;
import com.ak.qmyd.bean.result.CircleDongTaiResult.CircleCommentList;
import com.ak.qmyd.bean.result.CircleDongTaiResult.CircleDongTai;
import com.ak.qmyd.bean.result.CircleDongTaiResult.CirclePicList;
import com.ak.qmyd.bean.result.CircleDongTaiResult.CirclePraiseList;
import com.ak.qmyd.bean.result.ZanOrCancelZanResult;
import com.ak.qmyd.bean.result.ZanOrCancelZanResult.PraiseCircleInfo;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.dialog.DeleteDialog;
import com.ak.qmyd.dialog.DeleteDialog.OnActionSheetSelected;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.EncryptUtils;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CircleImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author JGB
 * @date 2015-6-8 下午6:09:17
 */
public class CircleContentNoteDetailActivity extends BaseFragmentActivity
		implements OnClickListener, OnItemClickListener {

	private ImageButton ib_circle_content_note_return;
	private ListView lv_circle_content_note_list;
	private myListViewBaseAdapter listViewBaseAdapter;
	private ImageView iv_circle_note_head_zan_img, iv_circle_note_head_delete,
			iv_circle_note_head_more;
	private CircleImageView iv_circle_note_head_portrait,
			iv_circle_note_head_img0, iv_circle_note_head_img1,
			iv_circle_note_head_img2;
	private TextView tv_circle_note_head_name, tv_circle_note_head_time,
			tv_circle_note_head_title, tv_circle_note_head_content,
			tv_circle_note_head_zan_count;
	private GridView gv_circle_note_head_gridview;
	private EditText et_circle_content_note_edit;
	private Map<String, ?> userInfoSp;
	private CircleDongTai circleDongTai;
	private ArrayList<CircleCommentList> circleCommentList;
	private ArrayList<CirclePicList> circlePicList;
	private ArrayList<CirclePraiseList> circlePraiseList;
	private RelativeLayout rl_circle_note_head_imgs;
	private CircleDongTaiResult cdtr;
	private ArrayList<ImagePath> data;
	private TextView emptyView;
	private PraiseCircleInfo praiseCircleInfo;
	private ArrayList<com.ak.qmyd.bean.result.ZanOrCancelZanResult.CirclePraiseList> circlePraiseList2;
	private String myState;
	private String currentState;
	private DrawerLayout mDrawerLayout;
	private Dialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_content_note);
		loadDialog = UIManager.getLoadingDialog(this);
		emptyView = new TextView(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		findView();
		getData();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_content_note_dl);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void setEmptyView() {
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无评论");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_circle_content_note_list.getParent())
				.addView(emptyView);
		lv_circle_content_note_list.setEmptyView(emptyView);
	}

	private void initData() {
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + circleDongTai.getThumbnailPath(),
				iv_circle_note_head_portrait);
		tv_circle_note_head_name.setText(circleDongTai.getUserName());
		if ((new SimpleDateFormat("MM-dd", Locale.getDefault())
				.format(new Date())).equals(circleDongTai.getCreateTime()
				.substring(5, 10))) {
			tv_circle_note_head_time.setText("今天"
					+ circleDongTai.getCreateTime().substring(10));
		} else {
			tv_circle_note_head_time.setText(circleDongTai.getCreateTime()
					.substring(0, 10));
		}
		tv_circle_note_head_title.setText(circleDongTai.getInfoTitle());
		tv_circle_note_head_content.setText(circleDongTai.getContent());

		if (circleDongTai.getState().equals("1")) {
			iv_circle_note_head_zan_img.setBackgroundResource(R.drawable.zan_y);
		} else if (circleDongTai.getState().equals("2")) {
			iv_circle_note_head_zan_img
					.setBackgroundResource(R.drawable.zan_g_);
		}
		tv_circle_note_head_zan_count.setText(circleDongTai.getPraiseNum()
				+ "个赞");
		initZanPersonImgs();
		if (circleDongTai.getIsDel().equals("1")) {
			iv_circle_note_head_delete.setVisibility(View.VISIBLE);
		} else {
			iv_circle_note_head_delete.setVisibility(View.GONE);
		}
		sendMyReplay();
	}

	private void initZanPersonImgs() {
		if (circlePraiseList == null || circlePraiseList.size() == 0) {
			iv_circle_note_head_img0.setVisibility(View.GONE);
			iv_circle_note_head_img1.setVisibility(View.GONE);
			iv_circle_note_head_img2.setVisibility(View.GONE);
			iv_circle_note_head_more.setVisibility(View.GONE);
		} else if (circlePraiseList.size() == 1) {
			iv_circle_note_head_img0.setVisibility(View.VISIBLE);
			iv_circle_note_head_img1.setVisibility(View.GONE);
			iv_circle_note_head_img2.setVisibility(View.GONE);
			iv_circle_note_head_more.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(0).getThumbnailPath(),
					iv_circle_note_head_img0);
			iv_circle_note_head_img0.setOnClickListener(this);
		} else if (circlePraiseList.size() == 2) {
			iv_circle_note_head_img0.setVisibility(View.VISIBLE);
			iv_circle_note_head_img1.setVisibility(View.VISIBLE);
			iv_circle_note_head_img2.setVisibility(View.GONE);
			iv_circle_note_head_more.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(0).getThumbnailPath(),
					iv_circle_note_head_img0);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(1).getThumbnailPath(),
					iv_circle_note_head_img1);
			iv_circle_note_head_img0.setOnClickListener(this);
			iv_circle_note_head_img1.setOnClickListener(this);
		} else if (circlePraiseList.size() == 3) {
			iv_circle_note_head_img0.setVisibility(View.VISIBLE);
			iv_circle_note_head_img1.setVisibility(View.VISIBLE);
			iv_circle_note_head_img2.setVisibility(View.VISIBLE);
			iv_circle_note_head_more.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(0).getThumbnailPath(),
					iv_circle_note_head_img0);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(1).getThumbnailPath(),
					iv_circle_note_head_img1);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(2).getThumbnailPath(),
					iv_circle_note_head_img2);
			iv_circle_note_head_img0.setOnClickListener(this);
			iv_circle_note_head_img1.setOnClickListener(this);
			iv_circle_note_head_img2.setOnClickListener(this);
		} else {
			iv_circle_note_head_img0.setVisibility(View.VISIBLE);
			iv_circle_note_head_img1.setVisibility(View.VISIBLE);
			iv_circle_note_head_img2.setVisibility(View.VISIBLE);
			iv_circle_note_head_more.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(0).getThumbnailPath(),
					iv_circle_note_head_img0);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(1).getThumbnailPath(),
					iv_circle_note_head_img1);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList.get(2).getThumbnailPath(),
					iv_circle_note_head_img2);
			iv_circle_note_head_img0.setOnClickListener(this);
			iv_circle_note_head_img1.setOnClickListener(this);
			iv_circle_note_head_img2.setOnClickListener(this);
		}
	}

	private void sendMyReplay() {
		et_circle_content_note_edit
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEND) {
							if (et_circle_content_note_edit.getText()
									.toString() == null
									|| et_circle_content_note_edit.getText()
											.length() == 0
									|| et_circle_content_note_edit.getText()
											.toString().equals(" ")) {
								showToast("回复不能为空");
							} else {
								sendMyReplayMessage();
								InputMethodManager imm = (InputMethodManager) v
										.getContext().getSystemService(
												Context.INPUT_METHOD_SERVICE);
								if (imm.isActive()) {
									imm.hideSoftInputFromWindow(
											v.getApplicationWindowToken(), 0);
								}
							}
							return true;
						}
						return false;
					}

				});

	}

	private void sendMyReplayMessage() {
		if (NetManager.isNetworkConnected(this)) {
			// rest/circle/replayCircle
			RequestQueue queue = Volley.newRequestQueue(this);
			StringRequest strRequest = new StringRequest(Request.Method.POST,
					Config.CIRCLE_REPLAY_URL, new Listener<String>() {

						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("返回数据" + response);
							Gson gson = new Gson();
							Result r = gson.fromJson(response, Result.class);
							if (r.getResultCode() == 1) {
								getData();
								showToast("回复成功");
								et_circle_content_note_edit.setText("");
							} else if (r.getResultCode() == 0) {
								showToast("回复失败");
							} else if (r.getResultCode() == 2) {
								showToast("找不到相关圈子信息");
							} else if (r.getResultCode() == 10000) {
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
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
					map.put("infoId", getIntent().getStringExtra("infoId"));
					map.put("replayContent", et_circle_content_note_edit
							.getText().toString());
					map.put("sessionId", (String) userInfoSp.get("sessionId"));
					map.put("hardId", MyApplication.instance.getHardId());
					return map;
				}
			};
			queue.add(strRequest);
		} else {
			showToast("网络不可用,请检查网络设置");
		}
	}

	private void getData() {
		loadDialog.show();
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/circle/queryCircleDetailInfo/userId/circleId/infoId/sessionId/hardId
		String url = Config.CIRCLE_NOTE_DETAIL_URL + "/"
				+ userInfoSp.get("userId") + "/"
				+ getIntent().getStringExtra("circleId") + "/"
				+ getIntent().getStringExtra("infoId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId();
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("帖子详情url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("帖子数据:" + response);
							Gson gson = new Gson();
							cdtr = gson.fromJson(response,
									CircleDongTaiResult.class);
							UIManager.toggleDialog(loadDialog);
							if (cdtr.getResultCode() == 1) {
								circleDongTai = cdtr.getCircleDongTai();
								circleCommentList = circleDongTai
										.getCircleCommentList();
								circlePicList = circleDongTai
										.getCirclePicList();
								circlePraiseList = circleDongTai
										.getCirclePraiseList();
								initListData(response);
								initData();
							} else if (cdtr.getResultCode() == 0) {
								showToast("操作失败");
							} else if (cdtr.getResultCode() == 3) {
								showToast("没有相关数据");
							} else if (cdtr.getResultCode() == 10000) {
								changeLogin();
								showToast("请重新登录,用户名已失效");
							}
							if (lv_circle_content_note_list.getEmptyView() == null) {
								setEmptyView();
							}
						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
							UIManager.toggleDialog(loadDialog);
						}
					});
			queue.add(strRequest);// 加入到通讯队列中
		} else {
			showToast("网络不可用，请检查网络设置");
			UIManager.toggleDialog(loadDialog);
		}
	}

	private void initListData(String response) {
		try {
			JSONObject jsonObj;
			JSONObject staffDongTaiObj;
			jsonObj = new JSONObject(response);
			String staffDongTai = JsonManager.getJsonItem(jsonObj,
					"circleDongTai").toString();
			staffDongTaiObj = new JSONObject(staffDongTai);
			String infoPicList = JsonManager.getJsonItem(staffDongTaiObj,
					"circlePicList").toString();
			data = new Gson().fromJson(infoPicList,
					new TypeToken<List<ImagePath>>() {
					}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		gv_circle_note_head_gridview.setAdapter(new ImageAdapter(this, data));
		gv_circle_note_head_gridview.setOnItemClickListener(this);
		listViewBaseAdapter = new myListViewBaseAdapter(getApplicationContext());
		lv_circle_content_note_list.setAdapter(listViewBaseAdapter);
		Tools.setListViewHeightBasedOnChildren(lv_circle_content_note_list);
	}

	private void initView() {
		ib_circle_content_note_return.setOnClickListener(this);
		iv_circle_note_head_zan_img.setOnClickListener(this);
		iv_circle_note_head_delete.setOnClickListener(this);
		iv_circle_note_head_portrait.setOnClickListener(this);
		tv_circle_note_head_name.setOnClickListener(this);
	}

	private void findView() {
		ib_circle_content_note_return = (ImageButton) findViewById(R.id.ib_circle_content_note_return);
		lv_circle_content_note_list = (ListView) findViewById(R.id.lv_circle_content_note_list);
		iv_circle_note_head_portrait = (CircleImageView) findViewById(R.id.iv_circle_note_head_portrait);
		tv_circle_note_head_name = (TextView) findViewById(R.id.tv_circle_note_head_name);
		tv_circle_note_head_time = (TextView) findViewById(R.id.tv_circle_note_head_time);
		tv_circle_note_head_title = (TextView) findViewById(R.id.tv_circle_note_head_title);
		tv_circle_note_head_content = (TextView) findViewById(R.id.tv_circle_note_head_content);
		gv_circle_note_head_gridview = (GridView) findViewById(R.id.gv_circle_note_head_gridview);
		iv_circle_note_head_zan_img = (ImageView) findViewById(R.id.iv_circle_note_head_zan_img);
		tv_circle_note_head_zan_count = (TextView) findViewById(R.id.tv_circle_note_head_zan_count);
		iv_circle_note_head_delete = (ImageView) findViewById(R.id.iv_circle_note_head_delete);
		et_circle_content_note_edit = (EditText) findViewById(R.id.et_circle_content_note_edit);
		rl_circle_note_head_imgs = (RelativeLayout) findViewById(R.id.rl_circle_note_head_imgs);
		iv_circle_note_head_img0 = (CircleImageView) findViewById(R.id.iv_circle_note_head_img0);
		iv_circle_note_head_img1 = (CircleImageView) findViewById(R.id.iv_circle_note_head_img1);
		iv_circle_note_head_img2 = (CircleImageView) findViewById(R.id.iv_circle_note_head_img2);
		iv_circle_note_head_more = (ImageView) findViewById(R.id.iv_circle_note_head_more);

		et_circle_content_note_edit.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int cou = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
				cou = before + count;
				String editable = et_circle_content_note_edit.getText().toString();
				String str = Tools.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_circle_content_note_edit.setText(str);
					// 设置新的光标所在位置
					et_circle_content_note_edit.setSelection(str.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_circle_content_note_return:
			finish();
			break;
		case R.id.tv_circle_note_head_name:
			skipActivity(GusterInfoActivity.class);
			break;
		case R.id.iv_circle_note_head_zan_img:
			zanOrCancelZan();
			break;
		case R.id.iv_circle_note_head_delete:
			deleteMyCurrentNote();
			break;
		case R.id.iv_circle_note_head_portrait:
			skipActivity(GusterInfoActivity.class);
			break;
		case R.id.iv_circle_note_head_img0:
			skipMyActivity(GusterInfoActivity.class, 0);
			break;
		case R.id.iv_circle_note_head_img1:
			skipMyActivity(GusterInfoActivity.class, 1);
			break;
		case R.id.iv_circle_note_head_img2:
			skipMyActivity(GusterInfoActivity.class, 2);
			break;
		default:
			break;
		}
	}

	private void skipMyActivity(Class clazz, int i) {
		Intent newIntent = new Intent(CircleContentNoteDetailActivity.this,
				clazz);
		newIntent.putExtra("userid", circlePraiseList.get(i).getUserId());
		startActivity(newIntent);
	}

	private void skipActivity(Class clazz) {
		Intent newIntent = new Intent(CircleContentNoteDetailActivity.this,
				clazz);
		newIntent.putExtra("userid", circleDongTai.getUserId());
		startActivity(newIntent);
	}

	private void deleteMyNote() {
		loadDialog.show();
		if (NetManager.isNetworkConnected(this)) {
			// rest/circle/deleteCircleInfo
			RequestQueue queue = Volley.newRequestQueue(this);
			StringRequest strRequest = new StringRequest(Request.Method.POST,
					Config.DELETE_CIRCLE_NOTE_URL, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("删除帖子的回复数据:" + response);
							Gson gson = new Gson();
							Result r = gson.fromJson(response, Result.class);
							UIManager.toggleDialog(loadDialog);
							if (r.getResultCode() == 1) {
								showToast("删除帖子成功");
								finish();
								// Intent newIntent = new Intent(
								// CircleContentNoteDetailActivity.this,
								// CircleDetailActivity.class);
								// newIntent.putExtra("circleId", getIntent()
								// .getStringExtra("circleId"));
								// startActivity(newIntent);
							} else if (r.getResultCode() == 0) {
								showToast("操作失败");
							} else if (r.getResultCode() == 2) {
								showToast("不能重复删除帖子");
							} else if (r.getResultCode() == 10000) {
								changeLogin();
								showToast("请重新登录,用户名已失效");
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
							UIManager.toggleDialog(loadDialog);
						}
					}) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> map = new HashMap<String, String>();
					map.put("userId", (String) userInfoSp.get("userId"));
					map.put("infoId", circleDongTai.getCircleInfoId());
					map.put("sessionId", (String) userInfoSp.get("sessionId"));
					map.put("hardId", MyApplication.instance.getHardId());
					return map;
				}
			};
			queue.add(strRequest);

		} else {
			showToast("网络不可用,请检查网络设置");
			UIManager.toggleDialog(loadDialog);
		}
	}

	private void deleteMyCurrentNote() {
		DeleteDialog.showSheet(this, new OnActionSheetSelected() {

			@Override
			public void onClick(int which) {
				switch (which) {
				case 0:
					showToast("取消删除");
					break;
				case 1:
					deleteMyNote();
					break;
				default:
					break;
				}

			}
		}, null, "确定删除帖子么");
	}

	private void zanOrCancelZan() {
		loadDialog.show();
		if (NetManager.isNetworkConnected(this)) {
			// rest/circle/praiseCircleOperation
			RequestQueue queue = Volley.newRequestQueue(this);
			StringRequest strRequest = new StringRequest(Request.Method.POST,
					Config.CIRCLE_NOTE_ZAN, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("赞的回复数据:" + response);
							Gson gson = new Gson();
							ZanOrCancelZanResult zoczr = gson.fromJson(
									response, ZanOrCancelZanResult.class);
							UIManager.toggleDialog(loadDialog);
							if (zoczr.getResultCode() == 1) {
								if (currentState.equals("1")) {
									// showToast("已取消赞");
								} else {
									// showToast("已赞");
								}
								reInitZanPerson(zoczr);
							} else if (zoczr.getResultCode() == 0) {
								showToast("操作失败");
							} else if (zoczr.getResultCode() == 2) {
								showToast("不能重复赞或者取消赞");
							} else if (zoczr.getResultCode() == 10000) {
								changeLogin();
								showToast("请重新登录,用户名已失效");
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
							UIManager.toggleDialog(loadDialog);
						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					Map<String, String> map = new HashMap<String, String>();
					map.put("userId", (String) userInfoSp.get("userId"));
					map.put("infoId", circleDongTai.getCircleInfoId());
					map.put("sessionId", (String) userInfoSp.get("sessionId"));
					map.put("hardId", MyApplication.instance.getHardId());
					currentState = (myState == null ? circleDongTai.getState()
							: myState);
					map.put("state", currentState);
					return map;
				}
			};
			queue.add(strRequest);
		} else {
			showToast("网络不可用,请检查网络设置");
			UIManager.toggleDialog(loadDialog);
		}
	}

	private void reInitZanPerson(ZanOrCancelZanResult zoczr) {
		praiseCircleInfo = zoczr.getPraiseCircleInfo();
		circlePraiseList2 = praiseCircleInfo.getCirclePraiseList();
		reInitZanPersonImg();
		myState = praiseCircleInfo.getState();
	}

	private void reInitZanPersonImg() {
		if (praiseCircleInfo.getState().equals("1")) {
			iv_circle_note_head_zan_img.setBackgroundResource(R.drawable.zan_y);
		} else if (praiseCircleInfo.getState().equals("2")) {
			iv_circle_note_head_zan_img
					.setBackgroundResource(R.drawable.zan_g_);
		}
		tv_circle_note_head_zan_count.setText(praiseCircleInfo.getPraiseNum()
				+ "个赞");
		if (circlePraiseList2 == null || circlePraiseList2.size() == 0) {
			iv_circle_note_head_img0.setVisibility(View.GONE);
			iv_circle_note_head_img1.setVisibility(View.GONE);
			iv_circle_note_head_img2.setVisibility(View.GONE);
			iv_circle_note_head_more.setVisibility(View.GONE);
		} else if (circlePraiseList2.size() == 1) {
			iv_circle_note_head_img0.setVisibility(View.VISIBLE);
			iv_circle_note_head_img1.setVisibility(View.GONE);
			iv_circle_note_head_img2.setVisibility(View.GONE);
			iv_circle_note_head_more.setVisibility(View.GONE);
			DebugUtility.showLog("点赞列表1 "
					+ circlePraiseList2.get(0).getThumbnailPath());
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(0).getThumbnailPath(),
					iv_circle_note_head_img0);
		} else if (circlePraiseList2.size() == 2) {
			iv_circle_note_head_img0.setVisibility(View.VISIBLE);
			iv_circle_note_head_img1.setVisibility(View.VISIBLE);
			iv_circle_note_head_img2.setVisibility(View.GONE);
			iv_circle_note_head_more.setVisibility(View.GONE);
			DebugUtility.showLog("点赞列表2 "
					+ circlePraiseList2.get(0).getThumbnailPath());
			DebugUtility.showLog("点赞列表2 "
					+ circlePraiseList2.get(1).getThumbnailPath());
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(0).getThumbnailPath(),
					iv_circle_note_head_img0);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(1).getThumbnailPath(),
					iv_circle_note_head_img1);
		} else if (circlePraiseList2.size() == 3) {
			iv_circle_note_head_img0.setVisibility(View.VISIBLE);
			iv_circle_note_head_img1.setVisibility(View.VISIBLE);
			iv_circle_note_head_img2.setVisibility(View.VISIBLE);
			iv_circle_note_head_more.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(0).getThumbnailPath(),
					iv_circle_note_head_img0);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(1).getThumbnailPath(),
					iv_circle_note_head_img1);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(2).getThumbnailPath(),
					iv_circle_note_head_img2);
		} else {
			iv_circle_note_head_img0.setVisibility(View.VISIBLE);
			iv_circle_note_head_img1.setVisibility(View.VISIBLE);
			iv_circle_note_head_img2.setVisibility(View.VISIBLE);
			iv_circle_note_head_more.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(0).getThumbnailPath(),
					iv_circle_note_head_img0);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(1).getThumbnailPath(),
					iv_circle_note_head_img1);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circlePraiseList2.get(2).getThumbnailPath(),
					iv_circle_note_head_img2);
		}
	}

	class myListViewBaseAdapter extends BaseAdapter {

		private Context context;

		public myListViewBaseAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return circleCommentList == null ? 0 : circleCommentList.size();
		}

		@Override
		public Object getItem(int position) {
			return circleCommentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ListViewHolder listViewHolder = null;
			if (convertView == null) {
				listViewHolder = new ListViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.activity_circle_content_note_listitem,
								null);
				listViewHolder.iv_circle_note_listitem_img = (ImageView) convertView
						.findViewById(R.id.iv_circle_note_listitem_img);
				listViewHolder.iv_circle_note_listitem_delete = (ImageView) convertView
						.findViewById(R.id.iv_circle_note_listitem_delete);
				listViewHolder.tv_circle_note_listitem_name = (TextView) convertView
						.findViewById(R.id.tv_circle_note_listitem_name);
				listViewHolder.tv_circle_note_listitem_time = (TextView) convertView
						.findViewById(R.id.tv_circle_note_listitem_time);
				listViewHolder.tv_circle_note_listitem_content = (TextView) convertView
						.findViewById(R.id.tv_circle_note_listitem_content);
				convertView.setTag(listViewHolder);
			} else {
				listViewHolder = (ListViewHolder) convertView.getTag();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent newIntent = new Intent(
							CircleContentNoteDetailActivity.this,
							GusterInfoActivity.class);
					newIntent.putExtra("userid", circleCommentList
							.get(position).getUserId());
					startActivity(newIntent);
				}
			});
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ circleCommentList.get(position)
									.getThumbnailPath(),
					listViewHolder.iv_circle_note_listitem_img);

			listViewHolder.tv_circle_note_listitem_name
					.setText(circleCommentList.get(position).getUserName());
			if ((new SimpleDateFormat("MM-dd", Locale.getDefault())
					.format(new Date())).equals(circleCommentList.get(position)
					.getCreateTime().substring(5, 10))) {
				listViewHolder.tv_circle_note_listitem_time.setText("今天"
						+ circleCommentList.get(position).getCreateTime()
								.substring(10));
			} else {
				listViewHolder.tv_circle_note_listitem_time
						.setText(circleCommentList.get(position)
								.getCreateTime().substring(0, 10));
			}
			if (circleCommentList.get(position).getIsDel().equals("1")) {
				listViewHolder.iv_circle_note_listitem_delete
						.setVisibility(View.VISIBLE);
			} else {
				listViewHolder.iv_circle_note_listitem_delete
						.setVisibility(View.GONE);
			}
			listViewHolder.iv_circle_note_listitem_delete
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							DeleteDialog.showSheet(
									CircleContentNoteDetailActivity.this,
									new OnActionSheetSelected() {

										@Override
										public void onClick(int which) {
											switch (which) {
											case 0:
												showToast("取消删除");
												break;
											case 1:
												deleteReplay(circleCommentList
														.get(position)
														.getCommentId());
												break;
											default:
												break;
											}

										}
									}, null, "确定删除这条回复么");
						}
					});
			listViewHolder.tv_circle_note_listitem_content
					.setText(circleCommentList.get(position).getContent());
			return convertView;
		}
	}

	private void deleteReplay(final String commentId) {
		if (NetManager.isNetworkConnected(this)) {
			// rest/circle/delCircleCommentInfo
			RequestQueue queue = Volley.newRequestQueue(this);
			StringRequest strRequest = new StringRequest(Request.Method.POST,
					Config.DELETE_CIRCLE_REPLAY, new Listener<String>() {

						@Override
						public void onResponse(String response) {
							Gson gson = new Gson();
							Result r = gson.fromJson(response, Result.class);
							if (r.getResultCode() == 1) {
								getData();
								showToast("删除回复成功");
							} else if (r.getResultCode() == 0) {
								showToast("操作失败");
							} else if (r.getResultCode() == 2) {
								showToast("没有相关数据");
							} else if (r.getResultCode() == 10000) {
								changeLogin();
								showToast("请重新登录,用户名已失效");
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
					map.put("commentId", commentId);
					map.put("sessionId", (String) userInfoSp.get("sessionId"));
					map.put("hardId", MyApplication.instance.getHardId());
					return map;
				}
			};
			queue.add(strRequest);
		} else {
			showToast("网络不可用,请检查网络设置");
		}
	}

	static class ListViewHolder {
		ImageView iv_circle_note_listitem_img, iv_circle_note_listitem_delete;
		TextView tv_circle_note_listitem_name, tv_circle_note_listitem_time,
				tv_circle_note_listitem_content;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, PhotoPreviewActivity.class);
		intent.putExtra("photos", data);
		DebugUtility.showLog("查看的图片：" + data.toString());
		intent.putExtra("position", position);
		startActivity(intent);
	}
}
