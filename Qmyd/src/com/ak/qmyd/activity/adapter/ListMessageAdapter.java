package com.ak.qmyd.activity.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.PushInfo;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListMessageAdapter extends BaseAdapter {

	List<PushInfo> data;
	Context c;
	private LayoutInflater inflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public ListMessageAdapter(Context context, List<PushInfo> mDate) {
		inflater = LayoutInflater.from(context);
		
		this.c = context;
		this.data = mDate;

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.item_listview_message, null);
			holder.imgViewHead = (CircleImageView) convertView
					.findViewById(R.id.imgViewHead);
			holder.imgCircle = (ImageView) convertView
					.findViewById(R.id.imgCircle);
			holder.txtViewNickName = (TextView) convertView
					.findViewById(R.id.txtViewNickName);
			holder.txtViewContent = (TextView) convertView
					.findViewById(R.id.txtContent);
			holder.txtViewDate = (TextView) convertView
					.findViewById(R.id.txtViewDate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// “Ï≤Ωº”‘ÿÕº∆¨
		ImageLoader.getInstance().displayImage(Config.BASE_URL + data.get(position).getImg(), holder.imgViewHead);
		// ImageManager.getInstance().displayImage(
		// Config.BASE_URL + data.get(position).getImg(),
		// holder.imgViewHead, ImageManager.getViewsHeadOptions());
		holder.txtViewNickName.setText(data.get(position).getName());
		holder.txtViewContent.setText(data.get(position).getPushInfo());
		holder.txtViewDate.setText(data.get(position).getTime());
		if (data.get(position).getStatus().equals("1")) {
			holder.imgCircle.setVisibility(View.VISIBLE);
		} else {
			holder.imgCircle.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	final class ViewHolder {
		public CircleImageView imgViewHead;
		public ImageView imgCircle;
		public TextView txtViewNickName;
		public TextView txtViewContent;
		public TextView txtViewDate;
	}

}
