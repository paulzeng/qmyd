package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import cn.sharesdk.framework.authorize.g;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.TrainListDetailResult;
import com.ak.qmyd.bean.result.TrainListDetailResult.ChapterList;
import com.ak.qmyd.bean.result.TrainListDetailResult.Train;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CustomExpandableListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TrainingListDatailActivity extends BaseFragmentActivity implements
		OnClickListener {
	private CustomExpandableListView expandableListView;
	private LayoutInflater mGroupInflater; // 用于加载group的布局xml
	private LayoutInflater mChildInflater; // 用于加载listitem的布局xml
	private ImageView iv_traindetail_img;
	private TextView tv_traindetail_name, tv_traindetail_content,
			tv_traindetail_type, tv_traindetail_chapter;
	private ImageButton ib_traindetail_return, ib_traindetail_rightbtn;
	private ArrayList<ChapterList> chapterList;
	private Train train;
	private DrawerLayout mDrawerLayout;
	private myBaseExpandableListAdapter adapterBaseExpandableListAdapter;
	private Map<String, ?> userInfoSp;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training_detail);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		if (NetManager.isNetworkConnected(this)) {
			getData();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		findView();
		MyApplication.instance.setTypeId(getIntent().getStringExtra("typeId"));
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_train_detail_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		iv_traindetail_img.setFocusable(true);
		iv_traindetail_img.setFocusableInTouchMode(true);
		iv_traindetail_img.requestFocus();
		expandableListView.setCacheColorHint(Color.rgb(0, 0, 0));
		adapterBaseExpandableListAdapter = new myBaseExpandableListAdapter(this);
		expandableListView.setAdapter(adapterBaseExpandableListAdapter);
		expandableListView.expandGroup(0);
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						for (int i = 0; i < expandableListView.getCount(); i++) {
							if (groupPosition != i) {
								expandableListView.collapseGroup(i);
								expandableListView
										.setDivider(new ColorDrawable(Color
												.parseColor("#F0F0F0")));
								expandableListView.setDividerHeight(10);
							}
						}
					}
				});
		setListViewHeightBasedOnChildren(expandableListView);

	}

	private void getData() {
		loadDialog.show();
		// rest/train/Details/{hardId}/{sessionId}/{typeId}/{trainId}
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
				: (String) userInfoSp.get("sessionId"));
		String url = Config.TRAIN_DETAIL_URL + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ getIntent().getStringExtra("typeId") + "/"
				+ getIntent().getStringExtra("trainID");// 
		StringRequest strRequest = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// 通讯成功的 返回数据
						Gson gson = new Gson();
						TrainListDetailResult tldr = gson.fromJson(response,
								TrainListDetailResult.class);
						UIManager.toggleDialog(loadDialog);
						if (tldr.getResultCode() == 1) {
							chapterList = tldr.getChapterList();
							train = tldr.getTrain();
							initView();
							adapterBaseExpandableListAdapter
									.notifyDataSetChanged();
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

	public static void setListViewHeightBasedOnChildren(
			ExpandableListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private void initView() {
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + train.getTrainImageDetail(),
				iv_traindetail_img);
		tv_traindetail_name.setText(train.getTrainName());
		tv_traindetail_content.setText(train.getDescription());
		tv_traindetail_type.setText("难度：" + train.getTrainDifficulty());
		tv_traindetail_chapter.setText("总章节：" + train.getTotalChapter());
		ib_traindetail_return.setOnClickListener(this);
		ib_traindetail_rightbtn.setOnClickListener(this);
	}

	private void findView() {
		expandableListView = (CustomExpandableListView) findViewById(R.id.elv_traindetail_expandablelist);
		iv_traindetail_img = (ImageView) findViewById(R.id.iv_traindetail_img);
		tv_traindetail_name = (TextView) findViewById(R.id.tv_traindetail_name);
		tv_traindetail_content = (TextView) findViewById(R.id.tv_traindetail_content);
		tv_traindetail_type = (TextView) findViewById(R.id.tv_traindetail_type);
		tv_traindetail_chapter = (TextView) findViewById(R.id.tv_traindetail_chapter);
		ib_traindetail_return = (ImageButton) findViewById(R.id.ib_traindetail_return);
		ib_traindetail_rightbtn = (ImageButton) findViewById(R.id.ib_traindetail_rightbtn);
	}

	private class myBaseExpandableListAdapter extends BaseExpandableListAdapter {
		private Context context;

		public myBaseExpandableListAdapter(Context context) {
			super();
			this.context = context;
			mChildInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mGroupInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getGroupCount() {
			return chapterList == null ? 0 : chapterList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return chapterList == null ? 0 : (chapterList.get(groupPosition)
					.getSections() == null ? 0 : chapterList.get(groupPosition)
					.getSections().size());
		}

		@Override
		public Object getGroup(int groupPosition) {
			return chapterList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return chapterList.get(groupPosition).getSections();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return Long.parseLong(chapterList.get(groupPosition).getSections()
					.get(childPosition).getSectionId());
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		ViewGroundHolder groundholder = null; 

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) { 

				convertView = mGroupInflater.inflate(
						R.layout.activity_train_expandablelistview_ground_item,
						null);
				groundholder = new ViewGroundHolder();
				groundholder.tv_expandableList_ground_title = (TextView) convertView
						.findViewById(R.id.tv_expandableList_ground_title);
				groundholder.iv_expandableList_ground_img = (ImageView) convertView
						.findViewById(R.id.iv_expandableList_ground_img);
				convertView.setTag(groundholder);
			} else { 
				groundholder = (ViewGroundHolder) convertView.getTag();
			}
			if (isExpanded) {
				groundholder.iv_expandableList_ground_img
						.setBackgroundResource(R.drawable.delta_xia);
				expandableListView.setDivider(new ColorDrawable(Color
						.parseColor("#BABABA")));
				expandableListView.setDividerHeight(0);
			} else if (!isExpanded){
				groundholder.iv_expandableList_ground_img
						.setBackgroundResource(R.drawable.delta_r);
				expandableListView.setDivider(new ColorDrawable(Color
						.parseColor("#F0F0F0")));
				expandableListView.setCacheColorHint(Color.rgb(255, 255, 255));
				expandableListView.setDividerHeight(10);
			}
			groundholder.tv_expandableList_ground_title.setText(chapterList
					.get(groupPosition).getChapterName());
			notifyDataSetChanged();
			return convertView;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ViewChildHolder childholder = null;
			if (convertView == null) {
				convertView = mChildInflater.inflate(
						R.layout.activity_train_expandablelistview_child_item,
						null);
				childholder = new ViewChildHolder();
				childholder.iv_trainlistview_item_img = (ImageView) convertView
						.findViewById(R.id.iv_trainlistview_item_img);
				childholder.tv_trainlistview_item_title = (TextView) convertView
						.findViewById(R.id.tv_trainlistview_item_title);
				childholder.tv_trainlistview_item_time = (TextView) convertView
						.findViewById(R.id.tv_trainlistview_item_time);
				convertView.setTag(childholder);
			} else {
				childholder = (ViewChildHolder) convertView.getTag();
			}
			if(childPosition >= 0){
				expandableListView.setDivider(new ColorDrawable(Color
						.parseColor("#BABABA")));
				expandableListView.setDividerHeight(1);
			}else{
				expandableListView.setDivider(new ColorDrawable(Color
						.parseColor("#BABABA")));
				expandableListView.setDividerHeight(0);
			}
			if(isLastChild){
				expandableListView.setDivider(new ColorDrawable(Color
						.parseColor("#F0F0F0")));
				expandableListView.setCacheColorHint(Color.rgb(255, 255, 255));
				expandableListView.setDividerHeight(10);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					skipActivity(SynchronousTrainActivity2.class);
				}

				private void skipActivity(Class clazz) {
					Intent newItemIntent = new Intent(
							TrainingListDatailActivity.this, clazz);
					newItemIntent.putExtra("typeId",
							MyApplication.instance.getTypeId());
					newItemIntent.putExtra("trainID", getIntent()
							.getStringExtra("trainID"));
					newItemIntent.putExtra("chapterName",
							chapterList.get(groupPosition).getChapterName());
					newItemIntent.putExtra(
							"sectionName",
							chapterList.get(groupPosition).getSections()
									.get(childPosition).getSectionName());
					newItemIntent.putExtra(
							"sectionId",
							chapterList.get(groupPosition).getSections()
									.get(childPosition).getSectionId());
					startActivity(newItemIntent);
				}
			});
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ chapterList.get(groupPosition).getSections()
									.get(childPosition).getSectionImage(),
					childholder.iv_trainlistview_item_img);
			childholder.tv_trainlistview_item_title.setText(chapterList
					.get(groupPosition).getSections().get(childPosition)
					.getSectionName());
			childholder.tv_trainlistview_item_time.setText(chapterList
					.get(groupPosition).getSections().get(childPosition)
					.getSectionLength()
					+ "min");
			return convertView;
		}

	}


	static class ViewGroundHolder {
		TextView tv_expandableList_ground_title;
		ImageView iv_expandableList_ground_img;
	}

	static class ViewChildHolder {
		ImageView iv_trainlistview_item_img;
		TextView tv_trainlistview_item_title, tv_trainlistview_item_time;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_traindetail_return:
			finish();
			break;
		case R.id.ib_traindetail_rightbtn:
			skipActivity(TrainSkillActivity.class);
			break;

		default:
			break;
		}

	}

	private void skipActivity(Class clazz) {
		Intent it = new Intent(TrainingListDatailActivity.this, clazz);
		it.putExtra("typeId", MyApplication.instance.getTypeId());
		startActivity(it);
	}
}
