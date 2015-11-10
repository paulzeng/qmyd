package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.adapt.MyCircleDetailListAdapter;
import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.bean.result.CircleDetailResult;
import com.ak.qmyd.bean.result.CircleDetailResult.CircleDetail;
import com.ak.qmyd.bean.result.CircleDetailResult.CircleDetailList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.photoselector.model.PhotoModel;

/**
 * @author JGB
 * @date 2015-6-4 ����5:12:12
 */
public class CircleDetailActivity extends BaseFragmentActivity implements
		OnClickListener, OnScrollListener {

	private ImageButton ib_circle_detail_return;
	private TextView tv_circle_detail_post, tv_circle_detail_name,
			tv_circle_detail_content, tv_empty_text;
	private CircleImageView iv_circle_detail_head_img;
	private Button bt_circle_detail_join;
	private RelativeLayout rl_circle_detail_member, rl_circle_detail_good_note;
	private ListView lv_circle_detail_list;
	private MyCircleDetailListAdapter adapter;
	private Map<String, ?> userInfoSp;
	private CircleDetail circleDetail;
	private ArrayList<CircleDetailList> circleDetailList;
	private TextView emptyView, lv_foot_textView;
	private DrawerLayout mDrawerLayout;
	private View headView;
	private View footview;
	private int currentNumber = 10;
	private int currentPage = 1;
	private ArrayList<CircleDetailList> totalCircleDetailList;// �ܼ��ϵ�����
	private boolean flag = false;// ��֤scroll���β�ִ�У�ֱ�������������������ִ��
	private String state = null;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_detail);
		loadDialog = UIManager.getLoadingDialog(this);
		headView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.activity_circle_detail_head, null);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		findView();
		getData();
		// iv_circle_detail_head_img.setFocusable(true);
		// iv_circle_detail_head_img.setFocusableInTouchMode(true);
		// iv_circle_detail_head_img.requestFocus();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_circle_detail_dl);
		setEnableDrawerLayout(mDrawerLayout);
		totalCircleDetailList = new ArrayList<CircleDetailList>();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getData();
	}

	private void initData() {
		initView();
		initHeadData();
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("��������");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_circle_detail_list.getParent()).addView(emptyView);
		lv_circle_detail_list.setEmptyView(emptyView);
	}

	private void initHeadData() {
		tv_circle_detail_name.setText(circleDetail.getCircleName());
		tv_circle_detail_content.setText(circleDetail.getCircleNotice());
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + circleDetail.getThumbnailPath(),
				iv_circle_detail_head_img);
		if (circleDetail.getState().equals("0")) {
			bt_circle_detail_join.setVisibility(View.VISIBLE);
			bt_circle_detail_join.setText("�޸�");
		} else {
			bt_circle_detail_join.setVisibility(View.VISIBLE);
			if (circleDetail.getState().equals("1")) {
				bt_circle_detail_join.setText("�˳�");
			} else {
				bt_circle_detail_join.setText("����");
			}
		}
	}

	private void getData() {
		loadDialog.show();
		currentPage = 1;
		RequestQueue queue = Volley.newRequestQueue(this);
		// ����StringRequest JsonObjectRequest JsonArrayRequest ImageRequest ��
		// Ҳ���Լ̳�Request�Զ��� (Request�Ƿ���)
		// rest/circle/queryOneCircleInfo/userId/circleId/sessionId/hardId/currPage/size
		String url = Config.CIRCLE_DETAIL_URL + "/" + userInfoSp.get("userId")
				+ "/" + getIntent().getStringExtra("circleId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currentPage + "/"
				+ currentNumber;
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("Ȧ������url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// ͨѶ�ɹ��� ��������
							DebugUtility.showLog("Ȧ����������:" + response);
							Gson gson = new Gson();
							CircleDetailResult cdr = gson.fromJson(response,
									CircleDetailResult.class);
							totalCircleDetailList.clear();
							UIManager.toggleDialog(loadDialog);
							if (cdr.getResultCode() == 1) {
								circleDetail = cdr.getCircleDetail();
								circleDetailList = circleDetail
										.getCircleDetailList();
								if (circleDetail != null) {
									flag = true;
									adapter = new MyCircleDetailListAdapter(
											CircleDetailActivity.this,
											totalCircleDetailList);
									lv_circle_detail_list.setAdapter(adapter);
									initData();
									if (circleDetailList != null) {
										totalCircleDetailList
												.addAll(circleDetailList);
										if (circleDetailList.size() < currentNumber) {
											lv_foot_textView
													.setVisibility(View.GONE);
										} else {
											lv_foot_textView.setText("���ڼ���...");
										}
									} else {
										totalCircleDetailList.clear();
										adapter.notifyDataSetChanged();
										lv_foot_textView.setText("û�и����������");
									}
								}
							} else if (cdr.getResultCode() == 0) {
								flag = false;
								showToast("����ʧ��");
							} else if (cdr.getResultCode() == 10000) {
								flag = false;
								showToast("δ��½���½��ʱ�������µ�½");
								changeLogin();
							}
							if (circleDetailList == null) {
								lv_foot_textView.setVisibility(View.INVISIBLE);
//								if (lv_circle_detail_list.getEmptyView() == null) {
//									setEmptyView();
//								}
								 tv_empty_text.setVisibility(View.VISIBLE);
							}else{
								tv_empty_text.setVisibility(View.GONE);
							}

						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("��ȡ����ʧ��");
							flag = false;
							UIManager.toggleDialog(loadDialog);
						}
					});
			queue.add(strRequest);// ���뵽ͨѶ������
		} else {
			showToast("���粻���ã�������������");
			UIManager.toggleDialog(loadDialog);
		}

	}

	private void initView() {
		ib_circle_detail_return.setOnClickListener(this);
		tv_circle_detail_post.setOnClickListener(this);
		bt_circle_detail_join.setOnClickListener(this);
		rl_circle_detail_member.setOnClickListener(this);
		rl_circle_detail_good_note.setOnClickListener(this);
	}

	private void findView() {
		ib_circle_detail_return = (ImageButton) findViewById(R.id.ib_circle_detail_return);
		tv_circle_detail_post = (TextView) findViewById(R.id.tv_circle_detail_post);
		tv_circle_detail_name = (TextView) headView
				.findViewById(R.id.tv_circle_detail_name);
		tv_circle_detail_content = (TextView) headView
				.findViewById(R.id.tv_circle_detail_content);
		iv_circle_detail_head_img = (CircleImageView) headView
				.findViewById(R.id.iv_circle_detail_head_img);
		bt_circle_detail_join = (Button) headView
				.findViewById(R.id.bt_circle_detail_join);
		rl_circle_detail_member = (RelativeLayout) headView
				.findViewById(R.id.rl_circle_detail_member);
		rl_circle_detail_good_note = (RelativeLayout) headView
				.findViewById(R.id.rl_circle_detail_good_note);
		lv_circle_detail_list = (ListView) findViewById(R.id.lv_circle_detail_list);
		tv_empty_text = (TextView) findViewById(R.id.tv_empty_text);
		lv_circle_detail_list.addHeaderView(headView);
		footview = LayoutInflater.from(this).inflate(
				R.layout.listview_footview, null);
		lv_foot_textView = (TextView) footview.findViewById(R.id.tv_footview);
		lv_circle_detail_list.addFooterView(footview);
		lv_circle_detail_list.setOnScrollListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.ib_circle_detail_return:
			finish();
			break;

		case R.id.tv_circle_detail_post:
			if (circleDetail.getState().equals("2")) {
				showToast("���ȼ���Ȧ��");
			} else {
				skipActivity(PostNoteActivity.class);
			}
			break;

		case R.id.bt_circle_detail_join:
			if (circleDetail.getState().equals("0")) {
				changeCircle();
			} else {
				joinOrQuitCircle();
			}
			break;

		case R.id.rl_circle_detail_member:
			skipActivity(CircleMemberActivity.class);
			break;

		case R.id.rl_circle_detail_good_note:
			skipActivity(GoodNoteActivity.class);
			break;

		default:
			break;
		}
	}

	private void changeCircle() {
		skipActivity(CreateCirclesActivity.class);
	}

	private void joinOrQuitCircle() {
		loadDialog.show();
		RequestQueue queue = Volley.newRequestQueue(this);
		// ����StringRequest JsonObjectRequest JsonArrayRequest ImageRequest ��
		// Ҳ���Լ̳�Request�Զ��� (Request�Ƿ���)
		// rest/circle/addOrDelRelationCircle
		if (NetManager.isNetworkConnected(this)) {
			StringRequest strRequest = new StringRequest(Request.Method.POST,
					Config.JOIN_OR_CANCEL_JOIN_CIRCLE_URL,
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// ͨѶ�ɹ��� ��������
							DebugUtility.showLog("����Ȧ������:" + response);
							Gson gson = new Gson();
							Result r = gson.fromJson(response, Result.class);
							UIManager.toggleDialog(loadDialog);
							if (r.getResultCode() == 1) {
								if (circleDetail.getState().equals("1")) {
									showToast("�˳��ɹ�");
									bt_circle_detail_join.setText("����");
									state = "2";
								} else {
									showToast("����ɹ�");
									bt_circle_detail_join.setText("�˳�");
									state = "1";
								}
							} else if (r.getResultCode() == 0) {
								showToast("����ʧ��");
							} else if (r.getResultCode() == 2) {
								showToast("�����ظ�������˳�");
							} else if (r.getResultCode() == 10000) {
								showToast("δ��½���½��ʱ�������µ�½");
								changeLogin();
							}
						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("��ȡ����ʧ��");
							UIManager.toggleDialog(loadDialog);
						}
					}) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> map = new HashMap<String, String>();
					map.put("userId", (String) userInfoSp.get("userId"));
					map.put("circleId", getIntent().getStringExtra("circleId"));
					map.put("sessionId", (String) userInfoSp.get("sessionId"));
					map.put("hardId", MyApplication.instance.getHardId());
					String myState = (state == null ? circleDetail.getState()
							: state);
					map.put("state", circleDetail.getState());
					return map;
				}

			};
			queue.add(strRequest);// ���뵽ͨѶ������
		} else {
			showToast("���粻���ã�������������");
			UIManager.toggleDialog(loadDialog);
		}
	}

	private void skipActivity(Class clazz) {
		Intent newIntent = new Intent(CircleDetailActivity.this, clazz);
		newIntent.putExtra("isComCircleDetail", "true");
		newIntent.putExtra("circleId", circleDetail.getCircleId());
		newIntent.putExtra("circleName", circleDetail.getCircleName());
		newIntent.putExtra("circleNotice", circleDetail.getCircleNotice());
		newIntent.putExtra("thumbnailPath", circleDetail.getThumbnailPath());
		startActivity(newIntent);
	}

	static class ViewHolder {
		TextView tv_circle_list_item_title, tv_circle_list_item_name,
				tv_circle_list_item_comment, tv_circle_list_item_time;
		ImageView iv_circle_list_item_is_good;
		CircleImageView iv_circle_list_item_img;
		RelativeLayout rl_circle_list_item_user;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		if (requestCode == 0) {// selected image
			if (data != null && data.getExtras() != null) {
				@SuppressWarnings("unchecked")
				List<PhotoModel> photos = (List<PhotoModel>) data.getExtras()
						.getSerializable("photos");
				if (photos == null || photos.isEmpty()) {

				} else {

				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (flag) {
			DebugUtility.showLog(firstVisibleItem + "..." + visibleItemCount
					+ "..." + totalItemCount);
			if ((firstVisibleItem + visibleItemCount) == (totalItemCount)) {
				flag = false;
				loadMore();
			}
		}
	}

	private void loadMore() {
		loadDialog.show();
		currentPage++;
		RequestQueue queue = Volley.newRequestQueue(this);
		// ����StringRequest JsonObjectRequest JsonArrayRequest ImageRequest ��
		// Ҳ���Լ̳�Request�Զ��� (Request�Ƿ���)
		// rest/circle/queryOneCircleInfo/userId/circleId/sessionId/hardId/currPage/size
		String url = Config.CIRCLE_DETAIL_URL + "/" + userInfoSp.get("userId")
				+ "/" + getIntent().getStringExtra("circleId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currentPage + "/"
				+ currentNumber;
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("Ȧ������url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// ͨѶ�ɹ��� ��������
							DebugUtility.showLog("Ȧ����������:" + response);
							Gson gson = new Gson();
							CircleDetailResult cdr = gson.fromJson(response,
									CircleDetailResult.class);
							UIManager.toggleDialog(loadDialog);
							if (cdr.getResultCode() == 1) {
								circleDetail = cdr.getCircleDetail();
								circleDetailList = circleDetail
										.getCircleDetailList();
								if (circleDetail != null) {
									if (circleDetailList != null) {
										flag = true;
										totalCircleDetailList
												.addAll(circleDetailList);
										adapter.notifyDataSetChanged();
										if (circleDetailList.size() < currentNumber) {
											lv_foot_textView
													.setText("û�и����������");
										}
									} else {
										lv_foot_textView.setText("û�и����������");
									}
								}
							} else if (cdr.getResultCode() == 0) {
								flag = false;
								showToast("����ʧ��");
							} else if (cdr.getResultCode() == 10000) {
								flag = false;
								showToast("δ��½���½��ʱ�������µ�½");
								changeLogin();
							}
							if (totalCircleDetailList == null) {
								// lv_foot_textView.setVisibility(View.GONE);
								// if (lv_circle_detail_list.getEmptyView() ==
								// null) {
								// setEmptyView();
								// }
								// tv_empty_text.setVisibility(View.VISIBLE);
							}

						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("��ȡ����ʧ��");
							flag = false;
							UIManager.toggleDialog(loadDialog);
						}
					});
			queue.add(strRequest);// ���뵽ͨѶ������
		} else {
			showToast("���粻���ã�������������");
			UIManager.toggleDialog(loadDialog);
		}

	}

}
