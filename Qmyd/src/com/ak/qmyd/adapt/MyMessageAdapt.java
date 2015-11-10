package com.ak.qmyd.adapt;

import java.util.List;
import java.util.Map;

import com.ak.qmyd.R;
import com.ak.qmyd.adapt.FansAdapt.ViewHold;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMessageAdapt extends BaseAdapter {
	
	Context context;
	
	List<Map<String, String>> list;
	
	LayoutInflater layoutInflater;
	
	public MyMessageAdapt(Context context, List<Map<String, String>> list) {
		super();
		this.context = context;
		this.list = list;
		layoutInflater=LayoutInflater.from(context);
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
	public View getView(int position, View converView, ViewGroup parent) {
		ViewHold viewHold;
		if(converView==null){
			viewHold=new ViewHold();
			converView=layoutInflater.inflate(R.layout.mymessage_listview_item, null);
			viewHold.imageView_user=(ImageView)converView.findViewById(R.id.imageView_user);
			viewHold.textView_user_name=(TextView)converView.findViewById(R.id.textView_user_name);
			viewHold.textViewMessage=(TextView)converView.findViewById(R.id.textViewMessage);
			viewHold.textViewTime=(TextView)converView.findViewById(R.id.textViewTime);		
		    converView.setTag(viewHold);
		}else{
			viewHold=(ViewHold)converView.getTag();
		}
		
		
		return converView;
	}
	class ViewHold{
		ImageView imageView_user;
		TextView textView_user_name;
		TextView textViewMessage;
		TextView textViewTime;
	}

}
