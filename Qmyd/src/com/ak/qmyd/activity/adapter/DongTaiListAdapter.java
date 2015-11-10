package com.ak.qmyd.activity.adapter;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.GusterInfoActivity;
import com.ak.qmyd.bean.DongTaiBean;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.view.CircleImageView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DongTaiListAdapter extends BaseAdapter {
	private static final String TAG = "DongTaiListAdapter";

	private final LayoutInflater mLayoutInflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ArrayList<DongTaiBean> data;
	private final Random mRandom;
	Context context;

	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

	public DongTaiListAdapter(Context context, ArrayList<DongTaiBean> data) {
		mLayoutInflater = LayoutInflater.from(context);
		mRandom = new Random();
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		ViewHolder vh;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.list_item_dongtai,
					parent, false);
			vh = new ViewHolder();
			vh.imgContent = (DynamicHeightImageView) convertView
					.findViewById(R.id.iv_content_img);
			vh.imgUserhead = (CircleImageView) convertView
					.findViewById(R.id.iv_user_img);
			vh.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			vh.ll_user = (LinearLayout) convertView.findViewById(R.id.ll_user);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		double positionHeight = getPositionRatio(position);
		Log.d(TAG, "getView position:" + position + " h:" + positionHeight);
		if (position % 2 == 0) {
			vh.imgContent.setHeightRatio(1);
		} else {
			vh.imgContent.setHeightRatio(1.5);
		}
		// vh.imgContent.setHeightRatio(positionHeight);
		if (data.get(position).getContent().equals("null")) {
			vh.tv_content.setVisibility(View.GONE);
		} else {
			vh.tv_content.setText(data.get(position).getContent());
		}
		vh.tv_name.setText(data.get(position).getUserName());
		// 异步加载图片
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + data.get(position).getThumbnailPath(),
				vh.imgUserhead);// 用户头像
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + data.get(position).getImagePath(),
				vh.imgContent);// 动态图片
		vh.ll_user.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Intent intent = new Intent(context, GusterInfoActivity.class);
				intent.putExtra("userid", data.get(position).getUserId());
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		DynamicHeightImageView imgContent;
		TextView tv_content, tv_name;
		CircleImageView imgUserhead;
		LinearLayout ll_user;
	}

	private double getPositionRatio(final int position) {
		double ratio = sPositionHeightRatios.get(position, 0.0);
		if (ratio == 0) {
			ratio = getRandomHeightRatio();
			sPositionHeightRatios.append(position, ratio);
			Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
		}
		return ratio;
	}

	private double getRandomHeightRatio() {
		return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
													// the width
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
