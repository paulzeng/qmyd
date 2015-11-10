package com.ak.qmyd.activity.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.view.CircleImageView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FindListAdapter extends BaseAdapter {
	private static final String TAG = "DongTaiListAdapter";

	private final LayoutInflater mLayoutInflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private List<String> data;
	private final Random mRandom;

	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

	public FindListAdapter(Context context, List<String> data) {
		mLayoutInflater = LayoutInflater.from(context);
		mRandom = new Random();
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		ViewHolder vh;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.list_item_find,
					parent, false);
			vh = new ViewHolder();
			vh.imgContent = (DynamicHeightImageView) convertView
					.findViewById(R.id.iv_find_content_img);
			vh.imgUserhead = (CircleImageView) convertView
					.findViewById(R.id.iv_find_user_img);
			vh.tv_content = (TextView) convertView
					.findViewById(R.id.tv_find_content);
			vh.tv_name = (TextView) convertView.findViewById(R.id.tv_find_name);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		double positionHeight = getPositionRatio(position);
		Log.d(TAG, "getView position:" + position + " h:" + positionHeight);
		if (position % 2 == 0) {
			vh.imgContent.setHeightRatio(1.2);
		} else {
			vh.imgContent.setHeightRatio(1.5);
		}
		// vh.imgContent.setHeightRatio(positionHeight);
		vh.tv_content.setText(data.get(position));
		vh.tv_name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {

			}
		});

		return convertView;
	}

	static class ViewHolder {
		DynamicHeightImageView imgContent;
		TextView tv_content, tv_name;
		CircleImageView imgUserhead;
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
