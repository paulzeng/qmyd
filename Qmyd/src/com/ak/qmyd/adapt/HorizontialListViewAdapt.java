package com.ak.qmyd.adapt;

import java.util.ArrayList;
import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.VenuesBookActivity;
import com.ak.qmyd.bean.VenuesBook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HorizontialListViewAdapt extends BaseAdapter {

	private Context context;
	
	private List<VenuesBook>  venuesReserveList;
	
	public List<View> list;

	private int m=0;
	
	public HorizontialListViewAdapt(Context context,
			List<VenuesBook> venuesReserveList) {
		super();
		this.context = context;
		this.venuesReserveList = venuesReserveList;
		list=new ArrayList<View>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return venuesReserveList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return venuesReserveList.get(arg0);
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
		   arg1=LayoutInflater.from(context).inflate(R.layout.horizon_listview_item, null);
		   viewHold=new ViewHold();
		   viewHold.textView_day=(TextView)arg1.findViewById(R.id.textView_day);
		   viewHold.textView_data=(TextView)arg1.findViewById(R.id.textView_data);
		   viewHold.imageView_book_data=(ImageView)arg1.findViewById(R.id.imageView_book_data);
		   viewHold.rela_venues_book=(RelativeLayout)arg1.findViewById(R.id.rela_venues_book);
		   arg1.setTag(viewHold);
	    }else{
		   viewHold=(ViewHold)arg1.getTag();
	   } 
		
		//如果arg0等于position就变成选中状态，其他的变为不选中状态
		if(VenuesBookActivity.position==arg0){
			viewHold.textView_day.setTextColor(context.getResources().getColor(R.color.orange_color));
			viewHold.textView_data.setTextColor(context.getResources().getColor(R.color.orange_color));
			viewHold.imageView_book_data.setImageResource(R.drawable.circle_y);
		}else{
			viewHold.textView_day.setTextColor(context.getResources().getColor(R.color.common_black_dark));
			viewHold.textView_data.setTextColor(context.getResources().getColor(R.color.common_black_light));
			viewHold.imageView_book_data.setImageResource(R.drawable.circle_g);
		}	
		viewHold.textView_day.setText(venuesReserveList.get(arg0).getVenuesWeek());
		
		int index = venuesReserveList.get(arg0).getVenuesDate().indexOf('-', 0);	
		String str = venuesReserveList.get(arg0).getVenuesDate().substring(index+1,venuesReserveList.get(arg0).getVenuesDate().length());	
		viewHold.textView_data.setText(str);		
		viewHold.imageView_book_data.setFocusable(false);
		
		
		//venuesReserveList有多少元素，就添加多少个item视图给list;
	    if(m<venuesReserveList.size()){
 
	    	if(m<=arg0){
	    		m++;
	    	  list.add(arg1);	
	    	}
		  
	    }
		return arg1;
	}
	
	public   class ViewHold{
		 public	 TextView textView_day,textView_data;
	     public	 ImageView imageView_book_data;
		 public	 RelativeLayout rela_venues_book;		 
		 }

	
}	
	
