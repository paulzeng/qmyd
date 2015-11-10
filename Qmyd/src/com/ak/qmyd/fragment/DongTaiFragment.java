package com.ak.qmyd.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.DongTaiDetailActivity;
import com.ak.qmyd.activity.FocusActivity;
import com.ak.qmyd.activity.LoginActivity;
import com.ak.qmyd.activity.adapter.DongTaiListAdapter;
import com.ak.qmyd.activity.base.DongTaiBaseFragment;
import com.ak.qmyd.bean.DongTaiBean;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.ToastManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.PullToRefreshStaggeredGridView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

@SuppressLint("NewApi")
public class DongTaiFragment extends DongTaiBaseFragment implements
		AbsListView.OnScrollListener, AbsListView.OnItemClickListener,
		OnRefreshListener<StaggeredGridView> {
	private static final String TAG = "DongTaiFragment";
	private StaggeredGridView mDongTaiGridView;
	private boolean mHasRequestedMore;
	private DongTaiListAdapter mDongTaiAdapter;
	private ArrayList<DongTaiBean> mDongTaiData;
	private List<DongTaiBean> mNewDongTaiData;
	private LinearLayout tv_haoyou;
	private Map<String, ?> userInfoSp;
	private TextView emptyView;
	private RequestQueue mRequestQueue;
	private Dialog loadDialog;
	private String typeId;
	private static int rows = 1;
	private static int flag = 1;

	/** 下来刷新 **/
	private PullToRefreshStaggeredGridView mPullToRefreshStaggerdGridView;

	public DongTaiFragment(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_dongtai, container, false);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initPullToRefresh();
		initData();
		initViews();

	}

	void initPullToRefresh() {
		mPullToRefreshStaggerdGridView = (PullToRefreshStaggeredGridView) fa
				.findViewById(R.id.pull_grid_view);
		mPullToRefreshStaggerdGridView.setMode(Mode.PULL_FROM_START);
		mPullToRefreshStaggerdGridView.setMode(Mode.BOTH);
		mPullToRefreshStaggerdGridView.setOnRefreshListener(this);
		mDongTaiGridView = mPullToRefreshStaggerdGridView.getRefreshableView();
		mDongTaiData = new ArrayList<DongTaiBean>();
		mDongTaiAdapter = new DongTaiListAdapter(fa, mDongTaiData);
		mDongTaiGridView.setAdapter(mDongTaiAdapter);
		mDongTaiGridView.setOnScrollListener(this);
		mDongTaiGridView.setOnItemClickListener(this);
	}

	protected void initData() {
		loadDialog = UIManager.getLoadingDialog(fa);
		httpGetJson("1", "10");
	}

	void initViews() {
		tv_haoyou = (LinearLayout) fa.findViewById(R.id.txt_haoyou);
		tv_haoyou.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(fa, FocusActivity.class));
			}
		});
	}

	void httpGetJson(String currPage, String size) {
		loadDialog.show();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mRequestQueue = Volley.newRequestQueue(fa);
		String url = Config.BASE_URL + "/api/rest/circle/queryDongTaiInfo"
				+ "/" + userInfoSp.get("userId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currPage + "/"
				+ size + "/" + typeId;
		DebugUtility.showLog("请求URL:" + url);
		if (NetManager.isNetworkConnected(fa)) {
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
							DebugUtility.showLog("获取我的关注失败："
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
							Toast.makeText(fa, "获取数据失败", 1 * 1000).show();
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
			Toast.makeText(fa, "网络不可用，请检查网络设置", 1 * 1000).show();
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
				String dongTaiInfoList = JsonManager.getJsonItem(jsonObj,
						"dongTaiInfoList").toString();
				mNewDongTaiData = new Gson().fromJson(dongTaiInfoList,
						new TypeToken<List<DongTaiBean>>() {
						}.getType());
				if (flag == 1) {
					mDongTaiData.clear();
					rows = 2;
				} else {
					rows++;
				}
				mDongTaiData.addAll(mNewDongTaiData);
				mDongTaiAdapter.notifyDataSetChanged();
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				if (flag == 1) {
					// ToastManager.show(fa, "暂无数据");
				} else {
					ToastManager.show(fa, "已经加载全部动态");
				}
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
				Intent intent = new Intent(fa, LoginActivity.class);
				intent.putExtra("flag", "1");
				startActivity(intent);
			}
			if (mPullToRefreshStaggerdGridView.isRefreshing()) {
				mPullToRefreshStaggerdGridView.onRefreshComplete();
			}
			setEmptyView();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setEmptyView() {
		emptyView = new TextView(fa);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无消息");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) mDongTaiGridView.getParent()).addView(emptyView);
		mDongTaiGridView.setEmptyView(emptyView);
	}

	@Override
	public void onScrollStateChanged(final AbsListView view,
			final int scrollState) {
		Log.d(TAG, "onScrollStateChanged:" + scrollState);
	}

	@Override
	public void onScroll(final AbsListView view, final int firstVisibleItem,
			final int visibleItemCount, final int totalItemCount) {
		if (!mHasRequestedMore) {
			int lastInScreen = firstVisibleItem + visibleItemCount;
			DebugUtility.showLog("lastInScreen=" + lastInScreen + "|"
					+ "totalItemCount=" + totalItemCount);
			if (lastInScreen > totalItemCount) {
				Log.d(TAG, "onScroll lastInScreen - so load more");
				mHasRequestedMore = true;
				onLoadMoreItems();
			}
		}
	}

	private void onLoadMoreItems() {
		// DebugUtility.showLog("上啦加载更多");
		// httpGetJson((rows * 10 + 1) + "", "10");
		// mHasRequestedMore = false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Intent intent = new Intent(fa, DongTaiDetailActivity.class);
		intent.putExtra("id", mDongTaiData.get(position).getMyInfoId());
		intent.putExtra("staffId", mDongTaiData.get(position).getUserId());
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		DebugUtility.showLog("动态界面销毁");
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
		if (refreshView.isHeaderShown()) {
			httpGetJson("1", "10");
			flag = 1;
		} else {
			flag = 2;
			DebugUtility.showLog("上啦加载更多");
			httpGetJson(rows + "", "10");
			// if(mNewDongTaiData.size()>=10){
			// httpGetJson(rows+"", "10");
			// }
		}
	}
}
