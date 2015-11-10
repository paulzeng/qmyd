package com.ak.qmyd.adapt;

import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.VenuesAppointmentActivity;
import com.ak.qmyd.bean.AreaList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BusinessCircleAdapt extends BaseAdapter {

	Context context;
	
	List<AreaList>  mlist;
	
	LayoutInflater layoutInflater;
	
	public BusinessCircleAdapt(Context context, List<AreaList> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
		layoutInflater=LayoutInflater.from(context);
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
		if(arg1==null){
			arg1=layoutInflater.inflate(R.layout.businesscircle_gridview_item, null);
		}
		TextView textView=(TextView)arg1.findViewById(R.id.btn_city);
		textView.setText(mlist.get(arg0).getAreaName());
		
		//判断是否是已经选中的城市区域
		if(arg0==VenuesAppointmentActivity.position){		
			textView.setBackgroundResource(R.drawable.button_select_city);
		}
		
		return arg1;
	}

	

	
}
