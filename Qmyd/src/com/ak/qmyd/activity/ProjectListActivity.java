package com.ak.qmyd.activity;

//import android.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.ContentItem;
import com.ak.qmyd.bean.LabelItem;
import com.ak.qmyd.bean.ListItem;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ProjectListActivity extends Activity {

	String[] label = new String[] { "热门活动", "最新活动" };

	List<ListItem> list = new ArrayList<ListItem>();

	ListView listView;

	Context mContext;

	LayoutInflater mInflater;

	RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_activities);
		mContext = getApplicationContext();
		mRequestQueue = Volley.newRequestQueue(mContext);
		mInflater = LayoutInflater.from(mContext);
		listView = (ListView) findViewById(R.id.listView1);
		addshuzu();
		listView.setAdapter(new myAdapt());
		onclick();
	}

	/**
	 * listview的点击事件
	 * */

	private void onclick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String labelItemTitle = null;
				int position = 0;
				for(int i=0;i<arg2;i++){
					ListItem item=list.get(i);
					String title = item.getTitle();
					if(!title.equals("")){
						labelItemTitle=title;
						position=i;
					}	
				}
				Intent intent=new Intent(ProjectListActivity.this,ProjectDetailActivity.class);
				startActivity(intent);
//			Toast.makeText(mContext, "你点击的是："+labelItemTitle+"下的第"+(arg2-position)+"个", 
//					Toast.LENGTH_SHORT).show();	
			}
		});

	}
	


	/**
	 * 网络POST请求得到JSON数据存到list集合里
	 * */
	private void httpGetJson() {
		StringRequest request = new StringRequest(Request.Method.POST,
				"http://xxx.xxx", new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.i("ceshi", "得到的json数据" + response);
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				}) {
			// POST请求的的参数
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				return super.getParams();
			}
		};
		mRequestQueue.add(request);
	}
	
	/**
	 * 解析JSON,将分类的title给LabelItem,图片的地址ContentItem
	 * 在集合里保存
	 * */
	private void analysisJson(){
		
	}
	
	

	/**
	 * 模拟数据，有接口后删除
	 * */
	private void addshuzu() {
    List<Map<String, String>> mlist;
		for (int i = 0; i < label.length; i++) {
			LabelItem labelItem = new LabelItem(label[i]);
			list.add(labelItem);
			for (int j = 0; j < 3; j++) {				
				ContentItem contentItem = new ContentItem(
						BitmapFactory.decodeResource(this.getResources(),
								R.drawable.ceshi));
				list.add(contentItem);
			}
		}

	}

	class myAdapt extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return list.get(position).isClickable();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub		
			return list.get(arg0).getView(mContext, arg1, mInflater);
		}

	}

}
