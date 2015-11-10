package com.ak.qmyd.adapt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.BookDtail;
import com.ak.qmyd.bean.VenuesBook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VenuesBookAdapt extends BaseAdapter {

	Context context;

	List<BookDtail> reserveDetialList;

	int tieck = 0;

	int click_book = 0;

	int remain_book;

	List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();

	Map<String, Integer> map;

	private boolean flag;
	
	
	public  List<Map<String, Integer>> saveList;

	public VenuesBookAdapt(Context context, List<BookDtail> reserveDetialList,
			boolean flag) {
		super();
		this.context = context;
		this.reserveDetialList = reserveDetialList;
		this.flag = flag;
		iniList();
	}

	private void iniList() {
		saveList=new ArrayList<Map<String,Integer>>();	
		Map<String, Integer> map;
		for(int i=0;i<reserveDetialList.size();i++){
		    map=new HashMap<String, Integer>();
		    map.put("ticketNumber", 0);
		    saveList.add(map);
		}	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return reserveDetialList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return reserveDetialList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final ViewHold viewHold;
		if (arg1 == null) {
			viewHold = new ViewHold();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.venuesbook_listview_item, null);
			viewHold.imageView_book_add = (ImageView) arg1
					.findViewById(R.id.imageView_book_add);
			viewHold.imageView_book_reduce = (ImageView) arg1
					.findViewById(R.id.imageView_book_reduce);
			viewHold.textView_book_show = (TextView) arg1
					.findViewById(R.id.textView_book_show);
			viewHold.textView_book_time = (TextView) arg1
					.findViewById(R.id.textView_book_time);
			viewHold.textView_book_money = (TextView) arg1
					.findViewById(R.id.textView_book_money);
			viewHold.textView_book_remaining = (TextView) arg1
					.findViewById(R.id.textView_book_remaining);
			arg1.setTag(viewHold);
		} else {
			viewHold = (ViewHold) arg1.getTag();
		}
		viewHold.textView_book_time.setText(reserveDetialList.get(arg0)
				.getReserveTimeSlot());
		viewHold.textView_book_money.setText(reserveDetialList.get(arg0)
				.getReserveMoney());
		viewHold.textView_book_remaining.setText(reserveDetialList.get(arg0)
				.getReserveSurplusNum());
		
		 Integer m = saveList.get(arg0).get("ticketNumber");
		 int x = reserveDetialList.get(arg0).getVenuesLimit();
		 int y = Integer.parseInt(reserveDetialList.get(arg0).getReserveSurplusNum());
		 viewHold.textView_book_show.setText(m+"");
		 //给按纽初始化设置颜色
		if(m==0){
			if(x==0||y==0){
				viewHold.imageView_book_add.setImageResource(R.drawable.add_g);
				viewHold.imageView_book_reduce.setImageResource(R.drawable.reduce_g);
			}else{
				viewHold.imageView_book_add.setImageResource(R.drawable.add_y);
				viewHold.imageView_book_reduce.setImageResource(R.drawable.reduce_g);
			}		
		}else if(m>0){		
			viewHold.imageView_book_reduce.setImageResource(R.drawable.reduce_y);
			if(m<x&&m<y){
				viewHold.imageView_book_add.setImageResource(R.drawable.add_y);
			}else{
				viewHold.imageView_book_add.setImageResource(R.drawable.add_g);
			}
		}

		final int position = arg0;

		// 点击增加按纽，集合数据重新设置
		viewHold.imageView_book_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
		
				 Integer m = saveList.get(position).get("ticketNumber");
				 int x = reserveDetialList.get(position).getVenuesLimit();
				 int y = Integer.parseInt(reserveDetialList.get(position).getReserveSurplusNum());
				
			if(x>m&&y>m){			
			       m++;
			       viewHold.textView_book_show.setText(m + "");
			       Map<String, Integer> map=new HashMap<String, Integer>();
			       map.put("ticketNumber", m);
			       saveList.set(position, map);	
			   	if (m == x||m==y) {
					// 改变button的颜色状态
					viewHold.imageView_book_add
							.setImageResource(R.drawable.add_g);
				}
				if (m >= 1) {
					viewHold.imageView_book_reduce
							.setImageResource(R.drawable.reduce_y);
				}
			}
			}
		});

		// 点击减少按纽，将list集合数据重新设置
		viewHold.imageView_book_reduce
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						Integer m = saveList.get(position).get("ticketNumber");	
						int x = reserveDetialList.get(position).getVenuesLimit();
						int y = Integer.parseInt(reserveDetialList.get(position).getReserveSurplusNum());
						
					if(m>0){
						m--;
						viewHold.textView_book_show.setText(m + "");
						Map<String, Integer> map=new HashMap<String, Integer>();
					    map.put("ticketNumber", m);
					    saveList.set(position, map);
					    if(m<x&&m<y){
					    	viewHold.imageView_book_add
							.setImageResource(R.drawable.add_y);
					    }				
					}
					if(m==0){
						viewHold.imageView_book_reduce
						.setImageResource(R.drawable.reduce_g);
						
					}
					}
				});
		return arg1;
	}

	static class ViewHold {
		ImageView imageView_book_reduce, imageView_book_add;

		TextView textView_book_show, textView_book_time, textView_book_money,
				textView_book_remaining;

	}

}
