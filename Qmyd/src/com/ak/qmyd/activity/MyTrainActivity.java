package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.TrainingListActivity.ViewHolder;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.LoginResult;
import com.ak.qmyd.bean.result.MyTrainResult;
import com.ak.qmyd.bean.result.MyTrainResult.TypeArray;
import com.ak.qmyd.bean.result.TrainListResult;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyTrainActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ListView lv_my_train_listview;
	private ImageButton ib_my_trainF_return;
	private DrawerLayout mDrawerLayout;
	private myBaseAdapter adapter;
	private Map<String, ?> userInfoSp;
	private ArrayList<TypeArray> typeArray;
	private TextView emptyView;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_train);
		loadDialog = UIManager.getLoadingDialog(this);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		findView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_my_message_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		if (NetManager.isNetworkConnected(this)) {
			initView();
			getData();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		adapter = new myBaseAdapter(this);
		lv_my_train_listview.setAdapter(adapter);
	}

	private void getData() {
		loadDialog.show();
		// rest/train/History/Type/{hardId}/{sessionId}/{userId}
		String url = Config.MY_TRAIN_URL + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ userInfoSp.get("sessionId") + "/" + userInfoSp.get("userId");
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest strRequest = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Gson gson = new Gson();
						MyTrainResult mtr = gson.fromJson(response,
								MyTrainResult.class);
						UIManager.toggleDialog(loadDialog);
						if (mtr.getResultCode() == 1) {
							typeArray = mtr.getTypeArray();
							adapter.notifyDataSetChanged();
						} else if (mtr.getResultCode() == 0) {
						}
						setEmptyView();
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		queue.add(strRequest);
	}

	private void initView() {
		ib_my_trainF_return.setOnClickListener(this);

	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setBackgroundColor(Color.WHITE);
		emptyView.setText("暂无训练历程");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_my_train_listview.getParent()).addView(emptyView);
		lv_my_train_listview.setEmptyView(emptyView);
	}

	private void findView() {
		lv_my_train_listview = (ListView) findViewById(R.id.lv_my_train_listview);
		ib_my_trainF_return = (ImageButton) findViewById(R.id.ib_my_trainF_return);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_my_trainF_return:
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
			return typeArray == null ? 0 : typeArray.size();
		}

		@Override
		public Object getItem(int position) {
			return typeArray.get(position);
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
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.activity_my_train_listitem, null);
				holder.iv_my_train_item_img = (ImageView) convertView
						.findViewById(R.id.iv_my_train_item_img);
				holder.tv_my_train_item_type = (TextView) convertView
						.findViewById(R.id.tv_my_train_item_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent itemIntent = new Intent(context,
							TrainHistoryMainActivity.class);
					itemIntent.putExtra("source", "myTrain");
					itemIntent.putExtra("typeId", typeArray.get(position)
							.getTypeId());
					DebugUtility.showLog("当前的typeId"
							+ typeArray.get(position).getTypeId());
					startActivity(itemIntent);
				}
			});
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + typeArray.get(position).getThumbnail(),
					holder.iv_my_train_item_img);
			holder.tv_my_train_item_type.setText(typeArray.get(position)
					.getTypeName());
			return convertView;
		}
	}

	static class ViewHolder {
		ImageView iv_my_train_item_img;
		TextView tv_my_train_item_type;
	}
}
