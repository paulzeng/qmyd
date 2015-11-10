package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.TrainSkillResult;
import com.ak.qmyd.bean.result.TrainSkillResult.TrainSkillList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
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

/**
 * @author JGB
 * @date 2015-5-2 下午3:41:22 训练技巧
 */
public class TrainSkillActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_train_skill_return;
	private ListView lv_train_skill_list;
	private myBaseAdapter myBaseAdapter;
	private ArrayList<TrainSkillList> trainSkillList;
	private DrawerLayout mDrawerLayout;
	private Map<String, ?> userInfoSp;
	private TextView emptyView;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_skill);
		loadDialog = UIManager.getLoadingDialog(this);
		findView();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		if (NetManager.isNetworkConnected(this)) {
			initView();
			getData();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		myBaseAdapter = new myBaseAdapter(this);
		lv_train_skill_list.setAdapter(myBaseAdapter);
		setEmptyView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_train_skill_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void getData() {
		loadDialog.show();
		RequestQueue queue = Volley.newRequestQueue(this);
		// rest/train/trainSkill/List/{hardId}/{sessionId}/{typeId}/{currPage}/{pageSize}
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
				: (String) userInfoSp.get("sessionId"));
		String url = Config.TRAIN_SKILL_URL + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ MyApplication.instance.getTypeId() + "/1" + "/200";// 测试url
		StringRequest strRequest = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// 通讯成功的 返回数据
						Gson gson = new Gson();
						TrainSkillResult tsr = gson.fromJson(response,
								TrainSkillResult.class);
						UIManager.toggleDialog(loadDialog);
						if (tsr.getResultCode() == 1) {
							trainSkillList = tsr.getTrainSkillList();
							myBaseAdapter.notifyDataSetChanged();
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
	}

	private void initView() {
		ib_train_skill_return.setOnClickListener(this);

	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无训练技巧");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_train_skill_list.getParent()).addView(emptyView);
		lv_train_skill_list.setEmptyView(emptyView);
	}

	private void findView() {
		ib_train_skill_return = (ImageButton) findViewById(R.id.ib_train_skill_return);
		lv_train_skill_list = (ListView) findViewById(R.id.lv_train_skill_list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_train_skill_return:
			finish();
			break;

		default:
			break;
		}
	}

	class myBaseAdapter extends BaseAdapter {

		private Context context;

		public myBaseAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return trainSkillList == null ? 0 : trainSkillList.size();
		}

		@Override
		public Object getItem(int position) {
			return trainSkillList.get(position);
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
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_train_skill_list_item, null);
				holder = new ViewHolder();
				holder.tv_train_skill_listitem = (TextView) convertView
						.findViewById(R.id.tv_train_skill_listitem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent newIntent = new Intent(TrainSkillActivity.this,
							TrainSkillDetailActivity.class);
					newIntent.putExtra("skillId", trainSkillList.get(position)
							.getSkillId());
					startActivity(newIntent);
				}
			});
			holder.tv_train_skill_listitem.setText(trainSkillList.get(position)
					.getSkillName());
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_train_skill_listitem;
	}
}
