package com.ak.qmyd.adapt;

import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.adapt.FinishFragmentAdapt2.ViewHold;
import com.ak.qmyd.bean.OrderDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ExpiredFragmentAdapt2 extends BaseAdapter {

	private  List<OrderDetail> mlist;
	 
	private Context context;

	public ExpiredFragmentAdapt2(List<OrderDetail> mlist, Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
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
		ViewHold viewHold;
		if(arg1==null){
			arg1=LayoutInflater.from(context).inflate(R.layout.book_fragment_listview_item2, null);
			viewHold=new ViewHold();
			viewHold.textView_data=(TextView)arg1.findViewById(R.id.textView_data);
			viewHold.textView_time=(TextView)arg1.findViewById(R.id.textView_time);
			viewHold.textView_state=(TextView)arg1.findViewById(R.id.textView_state);
			viewHold.imageView_select=(CheckBox)arg1.findViewById(R.id.imageView_select);
			viewHold.textView_fuhao=(TextView)arg1.findViewById(R.id.textView_fuhao);
			arg1.setTag(viewHold);
		}else{
			viewHold=(ViewHold)arg1.getTag();		
		}
		
		int index = mlist.get(arg0).getReserveDate().indexOf('-', 0);
		String str = mlist.get(arg0).getReserveDate().substring(index+1, mlist.get(arg0).getReserveDate().length());		
		viewHold.textView_data.setText(str);
	
		viewHold.textView_time.setText(mlist.get(arg0).getReserveTimeSlot());
		viewHold.imageView_select.setVisibility(View.GONE);
		viewHold.textView_fuhao.setVisibility(View.GONE);
		//viewHold.textView_state.setVisibility(View.VISIBLE);
		//viewHold.textView_state.setText(mlist.get(arg0).getReserveType());
		
		return arg1;
	}

	static class ViewHold{
		TextView textView_data,textView_time,textView_state,textView_fuhao;
		CheckBox imageView_select;
	}
}
