package com.ak.qmyd.activity.adapter;

import java.util.List;
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
import com.ak.qmyd.activity.CircleDetailActivity;
import com.ak.qmyd.bean.QuanZiBean;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.view.CircleImageView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class QuanziListAdapter extends BaseAdapter {
	private static final String TAG = "DongTaiListAdapter";

	private final LayoutInflater mLayoutInflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private List<QuanZiBean> data;
	private final Random mRandom;
	private Context context;
	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

	public QuanziListAdapter(Context context, List<QuanZiBean> data) {
		this.context = context;
		mLayoutInflater = LayoutInflater.from(context);
		mRandom = new Random();
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		ViewHolder vh;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.list_item_quanzi,
					parent, false);
			vh = new ViewHolder();
			vh.imgContent = (DynamicHeightImageView) convertView
					.findViewById(R.id.iv_qz_content_img);
			vh.imgUserhead = (CircleImageView) convertView
					.findViewById(R.id.iv_qz_user_img);
			vh.tv_content = (TextView) convertView
					.findViewById(R.id.tv_qz_content);
			vh.tv_name = (TextView) convertView.findViewById(R.id.tv_qz_name);
			vh.ll_qz_user = (LinearLayout) convertView
					.findViewById(R.id.ll_qz_user);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		double positionHeight = getPositionRatio(position);
		Log.d(TAG, "getView position:" + position + " h:" + positionHeight);
		if (position % 2 == 0) {
			vh.imgContent.setHeightRatio(1.2);
		} else {
			vh.imgContent.setHeightRatio(1.4);
		}
		// vh.imgContent.setHeightRatio(positionHeight);
		vh.tv_content.setText(data.get(position).getInfoTitle());
		vh.tv_name.setText(data.get(position).getCircleName());
		// 异步加载图片
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + data.get(position).getThumbnailPath(),
				vh.imgUserhead);// 用户头像
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + data.get(position).getImagePath(),
				vh.imgContent);// 动态图片
		vh.ll_qz_user.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Intent newIntent = new Intent(context,
						CircleDetailActivity.class);
				newIntent.putExtra("circleId", data.get(position).getCircleId());
				context.startActivity(newIntent);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		private DynamicHeightImageView imgContent;
		private TextView tv_content, tv_name;
		private CircleImageView imgUserhead;
		private LinearLayout ll_qz_user;
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
