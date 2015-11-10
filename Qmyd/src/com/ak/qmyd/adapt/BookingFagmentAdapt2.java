package com.ak.qmyd.adapt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.OrderDetail;
import com.ak.qmyd.config.Config;
import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Contacts.SettingsColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class BookingFagmentAdapt2 extends BaseAdapter  {

	private  List<OrderDetail> mlist;
	 
	private Context context;
	
	private boolean isClick;
	
	private RequestQueue mRequestQueue;
	
	public  static Map<Integer, Boolean> map;
	
	private int orderId;
	
	private boolean[] selected;

	@SuppressLint("UseSparseArrays")
	public BookingFagmentAdapt2(List<OrderDetail> mlist, Context context,boolean isClick,int orderId
		,boolean[] selected) {
		super();
		this.mlist = mlist;
		this.context = context;
		this.isClick=isClick;
		this.orderId=orderId;
		this.selected=selected;
		mRequestQueue=Volley.newRequestQueue(context);
		map=new HashMap<Integer, Boolean>();
	}
	


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		final ViewHold viewHold;
		if(arg1==null){
			arg1=LayoutInflater.from(context).inflate(R.layout.book_fragment_listview_item2, null);
			viewHold=new ViewHold();
			viewHold.textView_data=(TextView)arg1.findViewById(R.id.textView_data);
			viewHold.textView_time=(TextView)arg1.findViewById(R.id.textView_time);
			viewHold.textView_state=(TextView)arg1.findViewById(R.id.textView_state);
			viewHold.textView_money=(TextView)arg1.findViewById(R.id.textView_money);
			viewHold.imageView_select=(CheckBox)arg1.findViewById(R.id.imageView_select);
			arg1.setTag(viewHold);;
		}else{
			viewHold=(ViewHold)arg1.getTag();
		}
		
		int index = mlist.get(arg0).getReserveDate().indexOf('-', 0);
		String str = mlist.get(arg0).getReserveDate().substring(index+1, mlist.get(arg0).getReserveDate().length());		
		viewHold.textView_data.setText(str);
		viewHold.textView_time.setText(mlist.get(arg0).getReserveTimeSlot());
		viewHold.textView_money.setText(mlist.get(arg0).getReserveMoney());
		
		if(isClick){
			//点击取消按钮
			
			if(mlist.get(arg0).getReserveTypeId()==1){
				viewHold.imageView_select.setVisibility(View.VISIBLE);
				viewHold.imageView_select.setChecked(false);
				viewHold.textView_state.setVisibility(View.GONE);
				viewHold.imageView_select.setChecked(selected[arg0]);
			
			}else{
				viewHold.imageView_select.setVisibility(View.GONE);
				viewHold.textView_state.setVisibility(View.VISIBLE);
				viewHold.textView_state.setText(mlist.get(arg0).getReserveType());
				
			}
		}else{
			//点击已确定按钮
			viewHold.imageView_select.setVisibility(View.GONE);
			viewHold.textView_state.setVisibility(View.VISIBLE);
			viewHold.textView_state.setText(mlist.get(arg0).getReserveType());
			
		}

		return arg1;
	}

	static class ViewHold{
		TextView textView_data,textView_time,textView_state,textView_money;
		CheckBox imageView_select;
	}
}
