package com.ak.qmyd.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ak.qmyd.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageGridviewAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> imageItem;
	Context c;
	private LayoutInflater inflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public ImageGridviewAdapter(Context context,
			ArrayList<HashMap<String, Object>> mDate) {
		inflater = LayoutInflater.from(context);
		this.c = context;
		this.imageItem = mDate;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.griditem_addpic, null);
			holder.imgView = (ImageView) convertView
					.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(
				imageItem.get(position).get("itemImage").toString(),
				holder.imgView);

		return convertView;
	}

	final class ViewHolder {
		public ImageView imgView;
	}
}
