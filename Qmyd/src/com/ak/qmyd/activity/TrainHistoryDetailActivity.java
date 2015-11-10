package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.TrainHistoryDetailResult;
import com.ak.qmyd.bean.result.TrainHistoryDetailResult.SectionArray;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class TrainHistoryDetailActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_my_train_history_return;
	private ListView lv_my_train_history_listview;
	private ArrayList<SectionArray> sectionArray;
	private myBaseAdapter myBaseAdapter;
	private DrawerLayout mDrawerLayout;
	private Map<String, ?> userInfoSp;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_train_history_detail);
		loadDialog = UIManager.getLoadingDialog(this);
		findView();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		if (NetManager.isNetworkConnected(this)) {
			initView();
			getData();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_history_detail_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		myBaseAdapter = new myBaseAdapter(this);
		lv_my_train_history_listview.setAdapter(myBaseAdapter);
	}

	private void getData() {
		loadDialog.show();
		// rest/train/History/Detail/{hardId}/{sessionId}/{userId}/{sectionId}/{trainMonth}
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
				: (String) userInfoSp.get("sessionId"));
		String url = Config.TRAIN_HISTORY_DETAIL_URL + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ userInfoSp.get("userId") + "/"
				+ getIntent().getStringExtra("sectionId") + "/" + getIntent().getStringExtra("trainMonth") ;
		DebugUtility.showLog(url);
		StringRequest strRequest = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// 通讯成功的 返回数据
						DebugUtility.showLog(response);
						Gson gson = new Gson();
						TrainHistoryDetailResult thdr = gson.fromJson(response,
								TrainHistoryDetailResult.class);
						UIManager.toggleDialog(loadDialog);
						if (thdr.getResultCode() == 1) {
							sectionArray = thdr.getSectionArray();
							myBaseAdapter.notifyDataSetChanged();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// 通讯失败的 抛出异常
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		queue.add(strRequest);// 加入到通讯队列中
	}

	private void initView() {
		ib_my_train_history_return.setOnClickListener(this);
	}

	private void findView() {
		ib_my_train_history_return = (ImageButton) findViewById(R.id.ib_my_train_history_return);
		lv_my_train_history_listview = (ListView) findViewById(R.id.lv_my_train_history_listview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_my_train_history_return:
			finish();
			break;

		default:
			break;
		}
	}

	class myBaseAdapter extends BaseAdapter {
		private Context context;

		public myBaseAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return sectionArray == null ? 0 : sectionArray.size();
		}

		@Override
		public Object getItem(int position) {
			return sectionArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_my_train_history_detail_item, null);
				holder.tv_train_history_month = (TextView) convertView
						.findViewById(R.id.tv_train_history_month);
				holder.tv_train_history_minute = (TextView) convertView
						.findViewById(R.id.tv_train_history_minute);
				holder.tv_train_history_score = (TextView) convertView
						.findViewById(R.id.tv_train_history_score);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_train_history_month.setText(sectionArray.get(position)
					.getSectionDate());
			holder.tv_train_history_minute.setText(sectionArray.get(position)
					.getSectionTime() + "min");
			holder.tv_train_history_score.setText(sectionArray.get(position)
					.getSectionScore() + "分");
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_train_history_month, tv_train_history_minute,
				tv_train_history_score;
	}

}
