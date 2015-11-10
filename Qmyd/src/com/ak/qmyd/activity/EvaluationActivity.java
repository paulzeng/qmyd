package com.ak.qmyd.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.adapt.EvaluationAdapt;
import com.ak.qmyd.bean.AppraiseDetail;
import com.ak.qmyd.bean.VenuesEvaluation;
import com.ak.qmyd.bean.VenuesListInformation;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EvaluationActivity extends BaseFragmentActivity implements
		OnClickListener, OnScrollListener {

	int width;

	private RequestQueue mRequestQueue;

	private Context context;

	private List<AppraiseDetail> mList;

	private EvaluationAdapt adapter;

	private TextView textView_appraiseTotalNum, textView_favorNum,
			textView_badNum, listView_foot_textView;

	private ListView listView;

	private ImageButton imageButton_back;

	private int supportId, currPage = 1, pageSize = 5;

	private String hardId;

	private DrawerLayout drawerlayout;

	private boolean flag = false;

//	private ProgressBar progressBar;

	private TextView emptyView;

	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation);
		loadDialog = UIManager.getLoadingDialog(this);
		context = getApplicationContext();
		mRequestQueue = Volley.newRequestQueue(context);
		supportId = getIntent().getExtras().getInt("supportId");
		hardId = MyApplication.instance.getHardId();
		findView();
		httpGetJson();
	}

	private void findView() {
		View footView = LayoutInflater.from(context).inflate(
				R.layout.listview_footview, null);
		View headView = LayoutInflater.from(context).inflate(
				R.layout.evaluation_listview_headview, null);

		textView_appraiseTotalNum = (TextView) headView
				.findViewById(R.id.textView_appraiseTotalNum);
		textView_favorNum = (TextView) headView
				.findViewById(R.id.textView_favorNum);
		textView_badNum = (TextView) headView
				.findViewById(R.id.textView_badNum);
		listView = (ListView) this.findViewById(R.id.listView1);
		imageButton_back = (ImageButton) this
				.findViewById(R.id.imageButton_back);
//		progressBar = (ProgressBar) this.findViewById(R.id.progressBar1);

		listView.addHeaderView(headView);
		listView.addFooterView(footView);
		listView_foot_textView = (TextView) footView
				.findViewById(R.id.tv_footview);
		imageButton_back.setOnClickListener(this);
		listView.setOnScrollListener(this);
		drawerlayout = (DrawerLayout) this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);
	}

	/**
	 * 网络GET请求得到JSON数据存到list集合里
	 * */
	private void httpGetJson() {
		loadDialog.show();
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_EVALUATION_URL);
		String param = Tools.joinUrlByParam(hardId, supportId + "", currPage
				+ "", pageSize + "");
		buffer.append(param);
		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据
						try {
							UIManager.toggleDialog(loadDialog);
//							progressBar.setVisibility(View.GONE);
							listView.setVisibility(View.VISIBLE);
							VenuesEvaluation venuesEvaluation = parseJson(response);
							if (venuesEvaluation != null) {
								textView_appraiseTotalNum
										.setText(venuesEvaluation
												.getAppraiseTotalNum() + "");
								textView_favorNum.setText(venuesEvaluation
										.getFavorNum() + "");
								textView_badNum.setText(venuesEvaluation
										.getBadNum() + "");
								mList = venuesEvaluation
										.getAppraiseDetailList();
								adapter = new EvaluationAdapt(mList, context);
								listView.setAdapter(adapter);
								flag = true;
								if (mList.size() < pageSize) {
										listView_foot_textView
												.setText("没有更多评论");
								}
							} else {
									listView_foot_textView.setText("没有更多评论");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setEmptyView();
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * JSON数据的解析
	 * 
	 * @return
	 * @throws JSONException
	 * */
	private VenuesEvaluation parseJson(String response) throws JSONException {
		Gson gson = new Gson();
		VenuesEvaluation evaluation = new VenuesEvaluation();
		JSONObject object = new JSONObject(response);
		String array;
		Type type = new TypeToken<VenuesEvaluation>() {
		}.getType();
		int resultCode = object.getInt("resultCode");
		// 返回的描述信息
		String info = object.getString("resultInfo");
		if (resultCode == 0) {
			// 返回失败
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		} else if (resultCode == 1) {
			// 返回成功
			array = object.getString("venuseAppraise");
			// 得到集合
			evaluation = gson.fromJson(array, type);

			return evaluation;
			// iniData();
		}
		return null;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.imageButton_back:
			finish();
			break;
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if (flag) {
			if ((arg1 + arg2) == (arg3)) {
				flag = false;
				loadMore();
			}
		}
	}

	private void loadMore() {
		loadDialog.show();
		currPage++;
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_EVALUATION_URL);
		String param = Tools.joinUrlByParam(hardId, supportId + "", currPage
				+ "", pageSize + "");
		buffer.append(param);
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据
						try {
							UIManager.toggleDialog(loadDialog);
							VenuesEvaluation venuesEvaluation = parseJson(response);
							List<AppraiseDetail> list = venuesEvaluation
									.getAppraiseDetailList();
							if (list != null && list.size() != 0) {

								mList.addAll(list);
								adapter.notifyDataSetChanged();
								flag = true;
								if (list.size() < pageSize) {
										listView_foot_textView
												.setText("没有更多评论");
								}
							} else {
									listView_foot_textView.setText("没有更多评论");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);

	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无评价");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}
}
