package com.ak.qmyd.activity;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.tools.MyListView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AddFriendsActivity extends BaseActivity {
	
	MyListView listView;
	
	RequestQueue mRequestQueue;
	
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friends);
		context=getApplicationContext();
		mRequestQueue=Volley.newRequestQueue(context);
		findView();
	}
	private void findView() {
		listView=(MyListView)findViewById(R.id.listView1);
		listView.setAdapter(new myadapt());
	}

	/**
	 * 网络POST请求得到推荐好友
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
	 * 模拟的数据,有接口后删除
	 * */	
	class myadapt extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg1==null){
				arg1=LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.addfriends_listview_item, null);
			}
			return arg1;
		}
		
	}
}
