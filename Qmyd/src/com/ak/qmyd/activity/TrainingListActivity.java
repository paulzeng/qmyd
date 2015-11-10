package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.TraninListItemEntity;
import com.ak.qmyd.bean.result.TrainListResult;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 训练列表
 * 
 * @author JGB
 * 
 */
public class TrainingListActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ListView listView;
	private ImageButton ib_trainlist_return;
	private ArrayList<TraninListItemEntity> list;
	private myBaseAdapter adapter;
	private DrawerLayout mDrawerLayout;
	private Map<String, ?> userInfoSp;
	private TextView emptyView;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_training_list);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		if (NetManager.isNetworkConnected(this)) {
			getData();
			findView();
			initView();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_train_list_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无训练内容");
		emptyView.setBackgroundColor(Color.WHITE);
		LinearLayout ll_train_list = (LinearLayout) findViewById(R.id.ll_train_list);
		ll_train_list.setBackgroundColor(Color.WHITE);
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
	}

	private void getData() {
		loadDialog.show();
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/train/List/{hardId}/{sessionId}/{typeId}
		String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
				: (String) userInfoSp.get("sessionId"));
		String url = Config.TRAIN_LIST_URL + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ getIntent().getStringExtra("typeId");
		StringRequest strRequest = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// 通讯成功的 返回数据
						Gson gson = new Gson();
						TrainListResult tlr = gson.fromJson(response,
								TrainListResult.class);
						UIManager.toggleDialog(loadDialog);
						if (tlr.getResultCode() == 1) {
							list = tlr.getTrainList();
							adapter = new myBaseAdapter(getApplicationContext());
							listView.setAdapter(adapter);
							// adapter.notifyDataSetChanged();
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
		queue.add(strRequest);// 加入到通讯队列中
	}

	private void initView() {
		ib_trainlist_return.setOnClickListener(this);
	}

	private void findView() {
		listView = (ListView) findViewById(R.id.lv_trainlist);
		ib_trainlist_return = (ImageButton) findViewById(R.id.ib_trainlist_return);
	}

	class myBaseAdapter extends BaseAdapter {
		private Context context;

		public myBaseAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
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
						.inflate(R.layout.activity_training_list_item, null);
				holder.iv_traninlist_item_img = (ImageView) convertView
						.findViewById(R.id.iv_traninlist_item_img);
				holder.tv_traninlist_item_title = (TextView) convertView
						.findViewById(R.id.tv_traninlist_item_title);
				holder.tv_traninlist_item_director = (TextView) convertView
						.findViewById(R.id.tv_traninlist_item_director);
				holder.tv_traninlist_item_type = (TextView) convertView
						.findViewById(R.id.tv_traninlist_item_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent itemIntent = new Intent(context,
							TrainingListDatailActivity.class);
					itemIntent.putExtra("trainID", list.get(position)
							.getTrainId());
					itemIntent.putExtra("typeId",
							getIntent().getStringExtra("typeId"));
					startActivity(itemIntent);
				}
			});
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + list.get(position).getTrainImage(),
					holder.iv_traninlist_item_img);
			holder.tv_traninlist_item_title.setText(list.get(position)
					.getTrainName());
			holder.tv_traninlist_item_director.setText("制定:"
					+ list.get(position).getTrainPerson());
			holder.tv_traninlist_item_type.setText("难度:"
					+ list.get(position).getTrainDifficulty());
			return convertView;
		}
	}

	static class ViewHolder {
		ImageView iv_traninlist_item_img;
		TextView tv_traninlist_item_title, tv_traninlist_item_director,
				tv_traninlist_item_type;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_trainlist_return:
			startActivity(HomeActivity.class,null);
			finish();
			break;

		default:
			break;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(HomeActivity.class,null);
			finish();
		}
		return false;
	}
}
