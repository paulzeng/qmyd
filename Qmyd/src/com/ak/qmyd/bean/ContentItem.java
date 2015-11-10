package com.ak.qmyd.bean;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ak.qmyd.R;
import com.ak.qmyd.tools.ImageLoad;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ContentItem implements ListItem {

	private Bitmap bitmap;
	
	private String url;
	
	private ImageLoader imageLoad;
		
	public ContentItem(Context context){
	   ImageLoad.initImageLoader(context);
	   imageLoad=ImageLoader.getInstance();		
	}

	public ContentItem(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public ContentItem(String url) {
		this.url = url;
	}

	@Override
	public int getLayout() {
		// TODO Auto-generated method stub
		return R.layout.project_contentitem_listview;
	}

	@Override
	public boolean isClickable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getView(Context context, View convertView,
			LayoutInflater inflater) {
		convertView = inflater.inflate(getLayout(), null);
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.ContentItemimageView1);
		iv.setImageBitmap(bitmap);
		//传入URL地址成的图片在imageview显示
		//imageLoad.displayImage(uri, imageView);
		return convertView;
	}


	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "";
	}

}
