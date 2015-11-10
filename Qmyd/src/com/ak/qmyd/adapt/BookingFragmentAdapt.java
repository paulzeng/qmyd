package com.ak.qmyd.adapt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.ConstUtil;
import com.ak.qmyd.bean.MyOrderList;
import com.ak.qmyd.bean.OrderDetail;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.ImageLoad;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BookingFragmentAdapt extends BaseAdapter {

	private List<MyOrderList> venuesOrderList;

	private Context context;

	private ImageLoader imageLoader;

	private RequestQueue mRequestQueue;

	private boolean isClick = false;

	private BookingFagmentAdapt2 adapt;

	public int orderId;

	boolean[][] selected;

	private Map<Integer, Boolean> map;

	private Map<Integer, Boolean> clickMap;
	
	private String hardId,sessionId,userId;
	
	private boolean[] select=new boolean[20];

	public BookingFragmentAdapt(List<MyOrderList> venuesOrderList,
			Context context,String userId) {
		super();
		this.venuesOrderList = venuesOrderList;
		this.context = context;
		this.userId=userId;
		ImageLoad.initImageLoader(context);
		imageLoader = ImageLoader.getInstance();
		mRequestQueue = Volley.newRequestQueue(context);
		clickMap = new HashMap<Integer, Boolean>();
		hardId=MyApplication.instance.getHardId();
		sessionId=context.getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("sessionId", null);
		iniData();
	}

	private void iniData() {

		int length = 0;

		for (int i = 0; i < venuesOrderList.size(); i++) {
			length = Math.max(venuesOrderList.get(i).getOrderDetailList()
					.size(), length);
		}
		selected = new boolean[venuesOrderList.size()][20];

		for (int i = 0; i < venuesOrderList.size(); i++) {

			for (int k = 0; k < length; k++) {
				selected[i][k] = false;
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

	
		return venuesOrderList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		
		return venuesOrderList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
	    loadSelect();
		return arg0;
	}
	
	
   /**
    * 当加载更多重新刷新数据时用。必须重新设置selected数组长度
    * */
	private void loadSelect() {		
		List<Map<String, Integer>> blist=new ArrayList<Map<String,Integer>>();
		Map<String, Integer> map;
		for(int i=0;i<selected.length;i++){
			for(int k=0;k<selected[i].length;k++){
				if(selected[i][k]){
					map=new HashMap<String, Integer>();
					map.put("i", i);
					map.put("k", k);
					blist.add(map);
				}
			}		
		}	
		
		iniData();
		for(int i=0;i<blist.size();i++){
		    Integer first = blist.get(i).get("i");
		    Integer second = blist.get(i).get("k");
		    selected[first][second]=true;
		}		
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		final ViewHold viewHold;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.booking_fragment_listview_item, null);
			viewHold = new ViewHold();
			viewHold.textView_order_number = (TextView) arg1
					.findViewById(R.id.textView_order_number);
			viewHold.textView_venues_name = (TextView) arg1
					.findViewById(R.id.textView_venues_name);
			viewHold.textView_venues_address = (TextView) arg1
					.findViewById(R.id.textView_venues_address);
			viewHold.imageView_venues = (ImageView) arg1
					.findViewById(R.id.imageView_venues);
			viewHold.myListView = (ListView) arg1.findViewById(R.id.myListView);
			viewHold.button_cancel = (Button) arg1
					.findViewById(R.id.button_cancel);
			arg1.setTag(viewHold);

		} else {
			viewHold = (ViewHold) arg1.getTag();
		}

		if (clickMap.get(arg0) != null) {
			isClick = clickMap.get(arg0);
		} else {
			isClick = false;
			clickMap.put(arg0, isClick);
		}

		viewHold.textView_order_number.setText(venuesOrderList.get(arg0)
				.getOrderCode());
		viewHold.textView_venues_name.setText(venuesOrderList.get(arg0)
				.getVenuesName());
		viewHold.textView_venues_address.setText(venuesOrderList.get(arg0)
				.getVenuesAddr());
		imageLoader.displayImage(Config.BASE_URL
				+ venuesOrderList.get(arg0).getVenuesThumbnail(),
				viewHold.imageView_venues);

		//select = new boolean[selected[arg0].length];
		for (int i = 0; i < selected[arg0].length; i++) {
			select[i] = selected[arg0][i];
		}
		
		final int position = arg0;

		viewHold.myListView.setAdapter(new BookingFagmentAdapt2(venuesOrderList
				.get(arg0).getOrderDetailList(), context, isClick,
				venuesOrderList.get(arg0).getOrderId(), select));
		
		viewHold.button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isClick = !(isClick);
				if (!isClick) {

					orderId = venuesOrderList.get(position).getOrderId();
					Map<Integer, Boolean> nMap = GetMap(position);

					List<String> timeId = new ArrayList<String>();
					List<OrderDetail> mmList = venuesOrderList.get(position)
							.getOrderDetailList();
					for (int i = 0; i < mmList.size(); i++) {
						if (nMap.get(i)) {
							timeId.add(mmList.get(i).getOrderDetailId() + "");
						}
					}
					if (timeId.size() != 0) {
						httpPost(timeId);
					} else {
						viewHold.button_cancel.setText("取消");
						boolean[] select = new boolean[selected[position].length];
						for (int i = 0; i < selected[position].length; i++) {
							select[i] = selected[position][i];
						}
						clickMap.put(position, isClick);
						adapt = new BookingFagmentAdapt2(venuesOrderList.get(
								position).getOrderDetailList(), context,
								isClick, venuesOrderList.get(position)
										.getOrderId(), select);
						viewHold.myListView.setAdapter(adapt);
					}
				} else {

					viewHold.button_cancel.setText("确定");
					//select = new boolean[selected[position].length];
					for (int i = 0; i < selected[position].length; i++) {
						select[i] = selected[position][i];
					}
					clickMap.put(position, isClick);
					adapt = new BookingFagmentAdapt2(venuesOrderList.get(
							position).getOrderDetailList(), context, isClick,
							venuesOrderList.get(position).getOrderId(), select);
					viewHold.myListView.setAdapter(adapt);

					viewHold.myListView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									com.ak.qmyd.adapt.BookingFagmentAdapt2.ViewHold hold = (com.ak.qmyd.adapt.BookingFagmentAdapt2.ViewHold) arg1
											.getTag();
									if (venuesOrderList.get(position)
											.getOrderDetailList().get(arg2)
											.getReserveTypeId() == 1
											&& clickMap.get(position)) {
										hold.imageView_select.toggle();
									
										selected[position][arg2] = hold.imageView_select
												.isChecked();									
									}
								}
							});
				}
			}
		});
		return arg1;
	}

	static class ViewHold {
		TextView textView_order_number, textView_venues_name,
				textView_venues_address;
		ImageView imageView_venues;
		ListView myListView;
		Button button_cancel;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, Boolean> GetMap(int position) {

		Map<Integer, Boolean> lmap = new HashMap<Integer, Boolean>();
		for (int i = 0; i < selected[position].length; i++) {
			lmap.put(i, selected[position][i]);
		}
		return lmap;
	}

	private void httpPost(final List<String> timeId) {
		StringRequest request = new StringRequest(Method.POST,
				Config.ORDER_BOOKING_POST_URL, new Listener<String>() {

					@Override
					public void onResponse(String response) {

						try {
							parseJson(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("hardId", hardId);
				map.put("sessionId", sessionId);
				map.put("orderId", orderId + "");
				map.put("userId", userId);

				String cancleTimeSlot = "";
				for (int i = 0; i < timeId.size(); i++) {
					cancleTimeSlot += timeId.get(i);
					if (i == timeId.size() - 1) {
						continue;
					}
					cancleTimeSlot = cancleTimeSlot + ",";
				}
				map.put("cancleTimeSlot", cancleTimeSlot);
				return map;
			}
		};
		mRequestQueue.add(request);

	}

	private void parseJson(String response) throws JSONException {
		JSONObject object = new JSONObject(response);
		int resultCode = object.getInt("resultCode");
		if (resultCode == 1) {
			Intent intent = new Intent();
			intent.setAction(ConstUtil.BOOKFRAGMENT_ACTION);
			context.sendBroadcast(intent);
		}
	}
}
