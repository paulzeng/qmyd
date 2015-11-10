package com.ak.qmyd.adapt;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ak.qmyd.activity.PhotoPreviewActivity;
import com.ak.qmyd.bean.PicUrl;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class VenuesDetailViewPager extends PagerAdapter {

	private List<ImageView> imageviewList;
	private Context context;
    private String response;
    private ArrayList<PicUrl> data;
    
	public VenuesDetailViewPager(List<ImageView> imageviewList, Context context,String response) {
		super();
		this.imageviewList = imageviewList;
		this.context = context;
		this.response = response;
		try {
			JSONObject jsonObj;
			JSONObject staffDongTaiObj;
			jsonObj = new JSONObject(response);
			String staffDongTai = JsonManager.getJsonItem(jsonObj,
					"getVenuesDetail").toString();
			staffDongTaiObj = new JSONObject(staffDongTai);
			String picList = JsonManager.getJsonItem(staffDongTaiObj,
					"picList").toString();
			data = new Gson().fromJson(picList,
					new TypeToken<List<PicUrl>>() {
					}.getType());
			DebugUtility.showLog("图片集合: " + data.toString());
			DebugUtility.showLog("场馆之后返回数据: " + response);
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageviewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(imageviewList.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		container.addView(imageviewList.get(position));
		imageviewList.get(position).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PhotoPreviewActivity.class);
				intent.putExtra("venues_photos", data);
				intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
		return imageviewList.get(position);
	}

}
