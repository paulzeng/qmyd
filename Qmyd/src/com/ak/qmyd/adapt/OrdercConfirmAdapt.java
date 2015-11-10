package com.ak.qmyd.adapt;

import java.util.List;
import java.util.Map;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.OrderList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrdercConfirmAdapt extends BaseAdapter {

	Context context;
	
	 List<OrderList>  list;
	
	public OrdercConfirmAdapt(Context context, List<OrderList>  list) {
		super();
		this.context = context;
		this.list = list;
	}

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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHold viewHold;
		if(arg1==null){
			arg1=LayoutInflater.from(context).inflate(R.layout.orderconfirm_listview_item, null);
			viewHold=new ViewHold();
			viewHold.textView_data=(TextView)arg1.findViewById(R.id.textView_data);
			viewHold.textView_time=(TextView)arg1.findViewById(R.id.textView_time);
			viewHold.textView_money=(TextView)arg1.findViewById(R.id.textView_money);
			viewHold.textView_number=(TextView)arg1.findViewById(R.id.textView_number);
			arg1.setTag(viewHold);
		}else{
			viewHold=(ViewHold)arg1.getTag();
		}
		viewHold.textView_data.setText(list.get(arg0).getVenuesDate());
		viewHold.textView_time.setText(list.get(arg0).getReserveTimeSlot());
		viewHold.textView_money.setText(list.get(arg0).getReserveMoney());
		viewHold.textView_number.setText("x"+list.get(arg0).getBookticket());	
		return arg1;
	}
	
	static class ViewHold{
		TextView textView_data,textView_time,textView_money,textView_number;		
	}

}
