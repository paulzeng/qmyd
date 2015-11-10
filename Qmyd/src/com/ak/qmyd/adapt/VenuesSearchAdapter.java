package com.ak.qmyd.adapt;

import java.util.List;

import com.ak.qmyd.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VenuesSearchAdapter extends BaseAdapter {

	private List<String> list;
	
	private Context context;
	
	public VenuesSearchAdapter(List<String> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
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
		if(arg1==null){
			arg1=LayoutInflater.from(context).inflate(R.layout.venuessearch_listview_item, null);
		}
		TextView text=(TextView)arg1.findViewById(R.id.textView_search);
		text.setText(list.get(arg0));	
		
		
		return arg1;
	}

}
