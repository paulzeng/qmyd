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

public class AddFriendsAdapt extends BaseAdapter {

	List<Map<String, String>> list;
	
	Context context;
	
	LayoutInflater layoutInflater;	
	
	public AddFriendsAdapt(List<Map<String, String>> list, Context context) {
		super();
		this.list = list;
		this.context = context;
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
			converView=layoutInflater.inflate(R.layout.fans_listview_item, null);
			viewHold.imageView_user=(ImageView)converView.findViewById(R.id.imageView_user);
			viewHold.textView_user_name=(TextView)converView.findViewById(R.id.textView_user_name);
			viewHold.textView_user_description=(TextView)converView.findViewById(R.id.textView_user_description);
			viewHold.btn_cancel_focus=(TextView)converView.findViewById(R.id.btn_cancel_focus);		
		    converView.setTag(viewHold);
		}else{
			viewHold=(ViewHold)converView.getTag();
		}
		return converView;
	}

	class ViewHold{
		ImageView imageView_user;
		TextView textView_user_name;
		TextView textView_user_description;
		TextView btn_cancel_focus;
	}
}
