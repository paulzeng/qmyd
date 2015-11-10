package com.ak.qmyd.adapt;

import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.adapt.BookingFragmentAdapt.ViewHold;
import com.ak.qmyd.bean.MyOrderList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.ImageLoad;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumptionAdapt extends BaseAdapter {

	private List<MyOrderList> venuesOrderList;

	private Context context;
	
	private ImageLoader imageLoader;
	
	public ConsumptionAdapt(List<MyOrderList> venuesOrderList, Context context) {
		super();
		this.venuesOrderList = venuesOrderList;
		this.context = context;
		ImageLoad.initImageLoader(context);
		imageLoader=ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return venuesOrderList == null ? 0:venuesOrderList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return venuesOrderList.get(arg0);
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
			arg1=LayoutInflater.from(context).inflate(R.layout.booking_fragment_listview_item, null);
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
		}else{
			viewHold=(ViewHold)arg1.getTag();
		}

		viewHold.button_cancel.setVisibility(View.GONE);
		viewHold.textView_order_number.setText(venuesOrderList.get(arg0)
				.getOrderCode());
		viewHold.textView_venues_name.setText(venuesOrderList.get(arg0)
				.getVenuesName());
		viewHold.textView_venues_address.setText(venuesOrderList.get(arg0)
				.getVenuesAddr());
		imageLoader.displayImage(Config.BASE_URL
				+ venuesOrderList.get(arg0).getVenuesThumbnail(),
				viewHold.imageView_venues);
		
		viewHold.myListView.setAdapter(new ConsumptionAdapt2(venuesOrderList.get(arg0).getOrderDetailList(),
				context));
		
		return arg1;
	}
	
	static class ViewHold {
		TextView textView_order_number, textView_venues_name,
				textView_venues_address;
		ImageView imageView_venues;
		ListView myListView;
		Button button_cancel;
	}

}
