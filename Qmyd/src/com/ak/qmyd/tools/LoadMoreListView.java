package com.ak.qmyd.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.ak.qmyd.R;

public class LoadMoreListView extends ListView implements OnScrollListener{

	Context context;
	
	View footView;
	
	private LoadMore mloadMore;
	
	public LoadMoreListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
	}
	
	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
	}

	private void init() {
		footView=LayoutInflater.from(context).inflate(R.layout.listview_footview, null);
		this.addFooterView(footView);
		this.setOnScrollListener(this);
		
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if(arg1+arg2==arg3){		
			mloadMore.loadMore();
		}		
	}

	
	public void setLoadMore(LoadMore mloadMore){		
		this.mloadMore=mloadMore;
	}
	
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	 public interface LoadMore{
		public void loadMore();
	}
	
}
