package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.TrainDetailExpandableListChildEntity;
import com.ak.qmyd.bean.result.ChapterListResult;
import com.ak.qmyd.bean.result.ChapterListResult.SectionList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
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
import com.nostra13.universalimageloader.core.ImageLoader;

public class TrainChapterListAcitivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_chapter_list_return;
	private ListView lv_chapter_listview;
	private ArrayList<SectionList> sectionList;
	private myAdapter adapter;
	private TextView tv_chapter_list_title;
	private LayoutInflater mInflater;
	private DrawerLayout mDrawerLayout;
	private Map<String, ?> userInfoSp;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter_list);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		findView();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		if (NetManager.isNetworkConnected(this)) {
			initView();
			getData();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		adapter = new myAdapter(this);
		lv_chapter_listview.setAdapter(adapter);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl__train_chapter_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initData(String str) {
		tv_chapter_list_title.setText(str);
	}

	private void getData() {
		loadDialog.show();
		// rest/train/section/List/{hardId}/{sessionId}/{typeId}/{chapterId}
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
				: (String) userInfoSp.get("sessionId"));
		String url = Config.CHAPTER_LIST_URL + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ MyApplication.instance.getTypeId() + "/"
				+ getIntent().getStringExtra("chapterId");
		DebugUtility.showLog("训练章节列表url: " + url);
		StringRequest strRequest = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						DebugUtility.showLog("训练章节返回数据: " + response);
						Gson gson = new Gson();
						ChapterListResult clr = gson.fromJson(response,
								ChapterListResult.class);
						UIManager.toggleDialog(loadDialog);
						if (clr.getResultCode() == 1) {
							sectionList = clr.getSectionList();
							initData(clr.getChapterName());
							adapter.notifyDataSetChanged();
						} else {
							showToast(clr.getResultInfo());
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
		ib_chapter_list_return.setOnClickListener(this);
	}

	private void findView() {
		ib_chapter_list_return = (ImageButton) findViewById(R.id.ib_chapter_list_return);
		lv_chapter_listview = (ListView) findViewById(R.id.lv_chapter_listview);
		tv_chapter_list_title = (TextView) findViewById(R.id.tv_chapter_list_title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_chapter_list_return:
			finish();
//			Intent newIntent = new Intent(TrainChapterListAcitivity.this,
//					SynchronousTrainActivity2.class);
//			newIntent.putExtra("sectionId",
//					getIntent().getStringExtra("sectionId"));
//			startActivity(newIntent);
			break;

		default:
			break;
		}

	}

	class myAdapter extends BaseAdapter {

		private Context context;

		public myAdapter(Context context) {
			super();
			this.context = context;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return sectionList == null ? 0 : sectionList.size();
		}

		@Override
		public Object getItem(int position) {
			return sectionList.get(position);
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
				convertView = mInflater.inflate(
						R.layout.activity_train_expandablelistview_child_item,
						null);
				holder.iv_trainlistview_item_img = (ImageView) convertView
						.findViewById(R.id.iv_trainlistview_item_img);
				holder.tv_trainlistview_item_title = (TextView) convertView
						.findViewById(R.id.tv_trainlistview_item_title);
				holder.tv_trainlistview_item_time = (TextView) convertView
						.findViewById(R.id.tv_trainlistview_item_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent newIntent = new Intent(
							TrainChapterListAcitivity.this,
							SynchronousTrainActivity2.class);
					newIntent.putExtra("sectionId", sectionList.get(position)
							.getSectionId());
					newIntent.putExtra("sectionName", sectionList.get(position)
							.getSectionName());
					newIntent.putExtra("typeId",
							getIntent().getStringExtra("typeId"));
					startActivity(newIntent);
				}
			});
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ sectionList.get(position).getSectionImage(),
					holder.iv_trainlistview_item_img);
			holder.tv_trainlistview_item_title.setText(sectionList
					.get(position).getSectionName());
			holder.tv_trainlistview_item_time.setText(sectionList.get(position)
					.getSectionLength() + "min");
			// tv_chapter_list_title
			// .setText(sectionList.get(position).getChapterName());
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView iv_trainlistview_item_img;
		TextView tv_trainlistview_item_title, tv_trainlistview_item_time;
	}

	private static ArrayList<TrainDetailExpandableListChildEntity> insertData() {

		ArrayList<TrainDetailExpandableListChildEntity> list = new ArrayList<TrainDetailExpandableListChildEntity>();
		for (int i = 0; i < 10; i++) {
			TrainDetailExpandableListChildEntity data = new TrainDetailExpandableListChildEntity();
			data.setExpandableList_Child_img(R.drawable.ceshi7);
			data.setExpandableList_Child_title("侧边单手平衡");
			data.setExpandableList_Child_time("15min");
			data.setExpandableList_Child_count("8个");
			data.setExpandableList_Child_heat("150千卡");
			list.add(data);
		}
		return list;
	}
}
