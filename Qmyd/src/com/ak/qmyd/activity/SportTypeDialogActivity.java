package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.SportsTypeListResult;
import com.ak.qmyd.bean.result.SportsTypeListResult.SportsList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * @author JGB
 * @date 2015-6-16 上午10:48:35
 */
public class SportTypeDialogActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ListView lv_create_circle_type;
	private TextView tv_create_circle_type_cancle, tv_create_circle_type_ok;
	private myListAdapter adapter;
	private Map<String, ?> userInfoSp;
	private ArrayList<SportsList> sportsList;
	private ViewHolder holder = null;
	private Map<Integer, Boolean> isSelected;
	private List beSelectedData = new ArrayList();
	// private SharedPreferences sp;
	// private SharedPreferences.Editor edit;
	private String selectedType;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_circle_type_dialog);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		setFinishOnTouchOutside(false);
		// sp = getSharedPreferences("config", 0);
		// edit = sp.edit();
		findView();
		getData();
	}

	private void getData() {
		// rest/circle/queryAllSportsList/userId/sessionId/hardId
		String url = Config.QUERY_SPORTS_TYPE_URL + "/"
				+ userInfoSp.get("userId") + "/" + userInfoSp.get("sessionId")
				+ "/" + MyApplication.instance.getHardId();
		DebugUtility.showLog("查询运动类型url" + url);
		if (NetManager.isNetworkConnected(this)) {
			RequestQueue queue = Volley.newRequestQueue(this);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("返回运动类型数据" + response);
							Gson gson = new Gson();
							SportsTypeListResult stlr = gson.fromJson(response,
									SportsTypeListResult.class);
							if (stlr.getResultCode() == 1) {
								sportsList = stlr.getSportsList();
								initData();
								initView();
							} else if (stlr.getResultCode() == 0) {
								showToast("操作失败");
							} else if (stlr.getResultCode() == 3) {
								showToast("没有相关数据");
							} else if (stlr.getResultCode() == 10000) {
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
						}
					});
			queue.add(strRequest);
		} else {
			showToast("网络不可用,请检查网络设置");
		}
	}

	private void initData() {
		if (isSelected != null)
			isSelected = null;
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < sportsList.size(); i++) {
			if (i == 0) {
				isSelected.put(i, true);
			} else {
				isSelected.put(i, false);
			}
		}
		// 清除已经选择的项
		if (beSelectedData.size() > 0) {
			beSelectedData.clear();
		}
		adapter = new myListAdapter(this);
		lv_create_circle_type.setAdapter(adapter);
		lv_create_circle_type.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		adapter.notifyDataSetChanged();
	}

	private void initView() {
		tv_create_circle_type_cancle.setOnClickListener(this);
		tv_create_circle_type_ok.setOnClickListener(this);
	}

	private void findView() {
		lv_create_circle_type = (ListView) findViewById(R.id.lv_create_circle_type);
		tv_create_circle_type_cancle = (TextView) findViewById(R.id.tv_create_circle_type_cancle);
		tv_create_circle_type_ok = (TextView) findViewById(R.id.tv_create_circle_type_ok);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_create_circle_type_cancle:
			finish();
			// edit.putString("selectedType", "");
			// edit.commit();
			break;
		case R.id.tv_create_circle_type_ok:
			if (selectedType == null || selectedType == "") {
				selectedType = sportsList.get(0).getTypeName();
			}
			// edit.putString("selectedType", selectedType);
			// edit.commit();
			Intent i = new Intent(SportTypeDialogActivity.this,
					CreateCirclesActivity.class);
			Bundle b = new Bundle();
			b.putString("selectedType", selectedType);
			i.putExtras(b);
			SportTypeDialogActivity.this.setResult(100, i);
			SportTypeDialogActivity.this.finish();
			break;
		default:
			break;
		}
	}

	class myListAdapter extends BaseAdapter {

		private Context context;

		public myListAdapter(Context context) {
			this.context = context;
			// initListImg();
		}

		@Override
		public int getCount() {
			return sportsList == null ? 0 : sportsList.size();
		}

		@Override
		public Object getItem(int position) {
			return sportsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_create_circle_type_listitem, null);
				holder.tv_create_circle_type_name = (TextView) convertView
						.findViewById(R.id.tv_create_circle_type_name);
				holder.cb_create_circle_type_cb = (CheckBox) convertView
						.findViewById(R.id.cb_create_circle_type_cb);
				holder.rl_create_circle_type = (RelativeLayout) convertView
						.findViewById(R.id.rl_create_circle_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.rl_create_circle_type
					.setOnClickListener(new OnClickListener() {

						private boolean cu;

						@Override
						public void onClick(View v) {
							selectedType = sportsList.get(position)
									.getTypeName();
							// 当前点击的CB
							if (!isSelected.get(position)) {
								cu = true;
							} else {
								cu = true;
							}
							// 先将所有的置为FALSE
							for (Integer p : isSelected.keySet()) {
								isSelected.put(p, false);
							}
							// 再将当前选择CB的实际状态
							isSelected.put(position, cu);
							myListAdapter.this.notifyDataSetChanged();
							beSelectedData.clear();
						}
					});
			holder.tv_create_circle_type_name.setText(sportsList.get(position)
					.getTypeName());
			holder.cb_create_circle_type_cb
					.setChecked(isSelected.get(position));
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_create_circle_type_name;
		CheckBox cb_create_circle_type_cb;
		RelativeLayout rl_create_circle_type;
	}
}
