package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.Category;
import com.ak.qmyd.bean.TrainDetailExpandableListChildEntity;
import com.ak.qmyd.bean.result.TrainHistoryResult;
import com.ak.qmyd.bean.result.TrainHistoryResult.HistoryList;
import com.ak.qmyd.bean.result.TrainHistoryResult.SectionArray;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
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
import com.nostra13.universalimageloader.core.ImageLoader;

public class TrainHistoryActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_train_history_return;
	private ListView listview;
	private CategoryAdapter mCustomBaseAdapter;
	private List<TrainDetailExpandableListChildEntity> list;
	private ArrayList<HistoryList> historyList;
	private TrainHistoryResult thr;
	private DrawerLayout mDrawerLayout;
	private Map<String, ?> userInfoSp;
	private String typeId;
	private TextView emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_train_history);
		BitmapUtils.initImageLoader(getApplicationContext());
		findView();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		if (NetManager.isNetworkConnected(this)) {
			setData();
			initView();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_train_history_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		listview.setOnItemClickListener(new ItemClickListener());
	}

	private void setData() {
		// rest/train/History/{hardId}/{sessionId}/{userId}/{typeId}
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
				: (String) userInfoSp.get("sessionId"));
		if (getIntent().getStringExtra("source").equals("myTrain")) {
			typeId = getIntent().getStringExtra("typeId");
			DebugUtility.showLog("来自我的训练");
		} else if (getIntent().getStringExtra("source").equals("syncTrain")) {
			typeId = MyApplication.instance.getTypeId();
			DebugUtility.showLog("来自同步训练");
		}
		String userId = ((String) userInfoSp.get("userId") == null ? "0"
				: (String) userInfoSp.get("userId"));
		String url = Config.TRAIN_HISTORY_URL + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ userId + "/" + typeId;
		DebugUtility.showLog("返回历史链接:" + url);
		StringRequest strRequest = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// 通讯成功的 返回数据
						DebugUtility.showLog("返回历史数据" + response);
						Gson gson = new Gson();
						thr = gson.fromJson(response, TrainHistoryResult.class);
						if (thr.getResultCode() == 1) {
							historyList = thr.getHistoryList();
							// 数据
							ArrayList<Category> listData = getData();
							mCustomBaseAdapter = new CategoryAdapter(
									getApplicationContext(), listData);

							// 适配器与ListView绑定
							listview.setAdapter(mCustomBaseAdapter);
							// mCustomBaseAdapter.notifyDataSetChanged();
						}
						setEmptyView();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// 通讯失败的 抛出异常
						showToast("获取数据失败");
					}
				});
		queue.add(strRequest);// 加入到通讯队列中
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无训练历程");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listview.getParent()).addView(emptyView);
		listview.setEmptyView(emptyView);
	}

	private void initView() {
		ib_train_history_return.setOnClickListener(this);

	}

	private void findView() {
		ib_train_history_return = (ImageButton) findViewById(R.id.ib_train_history_return);
		listview = (ListView) findViewById(R.id.lv_train_history_listview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_train_history_return:
			finish();
			// Intent newIntent = new
			// Intent(TrainHistoryActivity.this,SynchronousTrainActivity2.class);
			// newIntent.putExtra("sectionId",
			// getIntent().getStringExtra("sectionId"));
			// startActivity(newIntent);
			break;

		default:
			break;
		}
	}

	private class ItemClickListener implements OnItemClickListener {

		private ArrayList<SectionArray> sectionArray;
		private String sectionId;
		private String trainMonth;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			for (int i = 0; i < historyList.size(); i++) {
				if (position == 0) {
					sectionArray = historyList.get(i).getSectionArray();
					for (int j = 0; j < sectionArray.size(); j++) {
//						if (position == j) {
							sectionId = sectionArray.get(j).getSectionId();
							trainMonth = sectionArray.get(j).getTrainMonth();
						}
//					}
				}
			}
			DebugUtility.showLog("sectionId是" + sectionId + "trainMonth是"
					+ trainMonth);
			Intent newIntent = new Intent(TrainHistoryActivity.this,
					TrainHistoryDetailActivity.class);
			newIntent.putExtra("sectionId", sectionId);
			newIntent.putExtra("trainMonth", trainMonth);
			startActivity(newIntent);
		}

	}

	/**
	 * 创建数据
	 */
	private ArrayList<Category> getData() {
		ArrayList<Category> listData = new ArrayList<Category>();
		for (int i = 0; i < historyList.size(); i++) {
			if (historyList.get(i).getTrainMonth() != null
					&& historyList.get(i).getTrainTotalTime() != null) {
				Category category = new Category(historyList.get(i)
						.getTrainMonth(), historyList.get(i)
						.getTrainTotalTime());
				category.addItem(historyList.get(i).getSectionArray());
				listData.add(category);
			}
		}
		return listData;
	}

	public class CategoryAdapter extends BaseAdapter {

		private static final int TYPE_CATEGORY_ITEM = 0;
		private static final int TYPE_ITEM = 1;

		private ArrayList<Category> mListData;
		private LayoutInflater mInflater;

		public CategoryAdapter(Context context, ArrayList<Category> pData) {
			mListData = pData;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			int count = 0;

			if (null != mListData) {
				// 所有分类中item的总和是ListVIew Item的总个数
				for (Category category : mListData) {
					count += category.getItemCount();
				}
			}

			return count;
		}

		@Override
		public Object getItem(int position) {

			// 异常情况处理
			if (null == mListData || position < 0 || position > getCount()) {
				return null;
			}

			// 同一分类内，第一个元素的索引值
			int categroyFirstIndex = 0;

			for (Category category : mListData) {
				int size = category.getItemCount();
				// 在当前分类中的索引值
				int categoryIndex = position - categroyFirstIndex;
				// item在当前分类内
				if (categoryIndex < size) {
					return category.getItem(categoryIndex);
				}

				// 索引移动到当前分类结尾，即下一个分类第一个元素索引
				categroyFirstIndex += size;
			}

			return null;
		}

		@Override
		public int getItemViewType(int position) {
			// 异常情况处理
			if (null == mListData || position < 0 || position > getCount()) {
				return TYPE_ITEM;
			}

			int categroyFirstIndex = 0;

			for (Category category : mListData) {
				int size = category.getItemCount();
				// 在当前分类中的索引值
				int categoryIndex = position - categroyFirstIndex;
				if (categoryIndex == 0) {
					return TYPE_CATEGORY_ITEM;
				}

				categroyFirstIndex += size;
			}

			return TYPE_ITEM;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			int itemViewType = getItemViewType(position);
			switch (itemViewType) {
			case TYPE_CATEGORY_ITEM:
				if (null == convertView) {
					convertView = mInflater.inflate(
							R.layout.activity_my_train_history_title, null);
				}

				TextView tv_train_history_month = (TextView) convertView
						.findViewById(R.id.tv_train_history_month);
				TextView tv_train_history_total_time = (TextView) convertView
						.findViewById(R.id.tv_train_history_total_time);
				String itemValue = (String) getItem(position);
				tv_train_history_month.setText(itemValue.substring(0,
						itemValue.lastIndexOf("/")));
				tv_train_history_total_time.setText(itemValue.substring(
						itemValue.lastIndexOf("/") + 1, itemValue.length())
						+ "min");
				break;

			case TYPE_ITEM:
				ViewHolder viewHolder = null;
				if (null == convertView) {

					convertView = mInflater.inflate(
							R.layout.activity_my_train_history_listitem, null);

					viewHolder = new ViewHolder();
					viewHolder.tv_train_history_item_title = (TextView) convertView
							.findViewById(R.id.tv_train_history_item_title);
					viewHolder.tv_train_history_item_time = (TextView) convertView
							.findViewById(R.id.tv_train_history_item_time);
					viewHolder.tv_train_history_item_score = (TextView) convertView
							.findViewById(R.id.tv_train_history_item_score);
					viewHolder.iv_train_history_item_img = (ImageView) convertView
							.findViewById(R.id.iv_train_history_item_img);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}

				// 绑定数据
				ImageLoader
						.getInstance()
						.displayImage(
								Config.BASE_URL
										+ ((SectionArray) getItem(position))
												.getSectionImage(),
								viewHolder.iv_train_history_item_img);
				viewHolder.tv_train_history_item_time
						.setText(((SectionArray) getItem(position))
								.getSectionTotalTime() + "min");
				viewHolder.tv_train_history_item_score
						.setText(((SectionArray) getItem(position))
								.getSectionTotalScore() + "分");
				viewHolder.tv_train_history_item_title
						.setText(((SectionArray) getItem(position))
								.getSectionName());
				break;
			}

			return convertView;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return getItemViewType(position) != TYPE_CATEGORY_ITEM;
		}

		private class ViewHolder {
			TextView tv_train_history_item_title, tv_train_history_item_time,
					tv_train_history_item_score;
			ImageView iv_train_history_item_img;
		}

	}

	public class Category {

		private String mCategoryName;
		private String mCategoryTotalTime;
		private ArrayList<SectionArray> mCategoryItem = new ArrayList<SectionArray>();

		public Category(String mCategroyName, String mCategoryTotalTime) {
			this.mCategoryName = mCategroyName;
			this.mCategoryTotalTime = mCategoryTotalTime;
		}

		public String getmCategoryName() {
			return mCategoryName + "/" + mCategoryTotalTime;
		}

		public void addItem(ArrayList<SectionArray> item) {
			if (item != null) {
				mCategoryItem.addAll(item);
			}
		}

		/**
		 * 获取Item内容
		 * 
		 * @param pPosition
		 * @return
		 */
		public Object getItem(int pPosition) {
			// Category排在第一位
			if (pPosition == 0) {
				return mCategoryName + "/" + mCategoryTotalTime;
			} else {
				return mCategoryItem.get(pPosition - 1);
			}
		}

		/**
		 * 当前类别Item总数。Category也需要占用一个Item
		 * 
		 * @return
		 */
		public int getItemCount() {
			return mCategoryItem.size() + 1;
		}

	}
}
