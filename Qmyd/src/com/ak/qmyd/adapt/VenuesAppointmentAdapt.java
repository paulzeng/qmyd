package com.ak.qmyd.adapt;

import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.Discount;
import com.ak.qmyd.bean.VenuesListInformation;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.ImageLoad;
import com.ak.qmyd.tools.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class VenuesAppointmentAdapt extends BaseAdapter {

	Context context;
	
	List<VenuesListInformation> venuesList;
	
	LayoutInflater layoutInflater;
	
	ImageLoader imageLoader;
	
	public VenuesAppointmentAdapt(Context context, List<VenuesListInformation> venuesList) {
		super();
	
		this.context = context;
		this.venuesList = venuesList;
		layoutInflater=LayoutInflater.from(context);
		ImageLoad.initImageLoader(context);
		imageLoader=ImageLoader.getInstance();
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return venuesList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return venuesList.get(arg0);
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
			arg1=layoutInflater.inflate(R.layout.venues_appointment_listview, null);
			viewHold=new ViewHold();
            viewHold.textView_name=(TextView)arg1.findViewById(R.id.textView_venues_name);
            viewHold.textView_address=(TextView)arg1.findViewById(R.id.textView_venues_address);
            viewHold.textView_distance=(TextView)arg1.findViewById(R.id.textView_venues_distance);
	        viewHold.imageView_venues=(ImageView)arg1.findViewById(R.id.imageView_venues);  
	        viewHold.linear_textView=(LinearLayout)arg1.findViewById(R.id.linear_textView);
            arg1.setTag(viewHold);
		}else{
			viewHold=(ViewHold)arg1.getTag();
		}
		viewHold.linear_textView.removeAllViews();	
	    VenuesListInformation venuesInformation = venuesList.get(arg0);
	    viewHold.textView_name.setText(venuesInformation.getVenuesName());
	    viewHold.textView_address.setText(venuesInformation.getVenuesAddr());
	    
	    
	    int positon = venuesInformation.getVenuesDistance().indexOf('.');
	    
	    String distance=venuesInformation.getVenuesDistance().substring(0, 3)+"Km";
	    
	    viewHold.textView_distance.setText(distance);
	  
	    imageLoader.displayImage(Config.BASE_URL+venuesInformation.getVenuesThumbnail(),
	    		viewHold.imageView_venues);
	    
	    List<Discount> discount = venuesInformation.getActivityList();
	    //动态的添加优惠一栏
	    
	    int px = Tools.dip2px(context, 20);
	    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(px, 
	    		 px);
	     lp.rightMargin=5;
	     
	 
	    if(discount!=null&&discount.size()!=0){
	    	  for(int i=0;i<discount.size();i++){
	  	    	TextView textView=new TextView(context);
	  	    	textView.setText(discount.get(i).getActivityName());
	  	    	textView.setTextColor(context.getResources().getColor(R.color.white));
	  	    	textView.setBackgroundColor(context.getResources().getColor(R.color.orange_color));
	  	    	textView.setPadding(2, 2, 2, 2);
	  	    	textView.setGravity(Gravity.CENTER);
	  	    	textView.setLayoutParams(lp);
	  	    	viewHold.linear_textView.addView(textView);
	  	    }
	    	
	    }
	
		return arg1;
	}
	
	class ViewHold{
		TextView textView_name;
		TextView textView_address;
		TextView textView_distance;
		ImageView imageView_venues;
		LinearLayout linear_textView;	
	}

}
