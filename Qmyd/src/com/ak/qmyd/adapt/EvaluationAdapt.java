package com.ak.qmyd.adapt;

import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.AppraiseDetail;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.ImageLoad;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EvaluationAdapt extends BaseAdapter {

	List<AppraiseDetail> mList;
	
	Context context;
	
	ImageLoader imageLoader;

	public EvaluationAdapt(List<AppraiseDetail> mList, Context context) {
		super();
		this.mList = mList;
		this.context = context;
		ImageLoad.initImageLoader(context);
		imageLoader=ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
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
			arg1=LayoutInflater.from(context).inflate(R.layout.evaluation_listview_item, null);
			viewHold=new ViewHold();
			viewHold.imageView_user=(ImageView)arg1.findViewById(R.id.imageView_user);
			viewHold.textView_name=(TextView)arg1.findViewById(R.id.textView_name);
			viewHold.textView_data=(TextView)arg1.findViewById(R.id.textView_data);
			viewHold.textView_content=(TextView)arg1.findViewById(R.id.textView_content);
			arg1.setTag(viewHold);
		}else{
			viewHold=(ViewHold)arg1.getTag();
		}
		
		viewHold.textView_name.setText(mList.get(arg0).getUserName());
		viewHold.textView_data.setText(mList.get(arg0).getAppraiseTime());
		viewHold.textView_content.setText(mList.get(arg0).getAppraiseCountent());
		//imageLoader.displayImage(httpUrl+mList.get(arg0).getUserThumbnail(), viewHold.imageView_user);
		Bitmap bitmap = imageLoader.loadImageSync(Config.BASE_URL+mList.get(arg0).getUserThumbnail());
		if(bitmap!=null){
			viewHold.imageView_user.setImageBitmap(BitmapUtils.createCircleImage(bitmap));
		}else{
			viewHold.imageView_user.setImageResource(R.drawable.ic_launcher);
		}
		
		return arg1;
	}

	static class ViewHold{	
		ImageView imageView_user;
		TextView  textView_name,textView_data,textView_content;		
	}
}
