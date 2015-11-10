package com.ak.qmyd.activity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.listener.ListItemClickHelp;
import com.ak.qmyd.bean.Focus;
import com.ak.qmyd.bean.ImagePath;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.ImageManager;
import com.ak.qmyd.view.CircleImageView;
import com.ak.qmyd.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListFocusAdapter extends BaseAdapter {

	List<Focus> data;
	Context c;
	private LayoutInflater inflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ListItemClickHelp callback;

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public ListFocusAdapter(Context context, List<Focus> mDate,
			ListItemClickHelp callback) {
		inflater = LayoutInflater.from(context);
		this.c = context;
		this.data = mDate;
		this.callback = callback;
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
	public View getView(int position, View convertView, final ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_listview_focus, null);
			holder.imgViewHead = (CircleImageView) convertView
					.findViewById(R.id.imgViewFocusHead);
			holder.txtViewNickName = (TextView) convertView
					.findViewById(R.id.txtViewFocusNickName);
			holder.txtViewStatus = (TextView) convertView
					.findViewById(R.id.txtViewFocusStatus);
			holder.gridview_sport = (MyGridView) convertView
					.findViewById(R.id.gridview_sport);
			holder.iv_sport_more = (ImageView) convertView
					.findViewById(R.id.iv_sport_more);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + data.get(position).getThumbnailPath(),
				holder.imgViewHead, ImageManager.getViewsHeadOptions());
		holder.txtViewNickName.setText(data.get(position).getUserName());
		final View view = convertView;
		final int p = position;
		final int one = holder.txtViewStatus.getId();
		holder.txtViewStatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(view, parent, p, one);
			}
		});

		holder.gridview_sport.setColumnWidth(dpToPx(20));
		if (data.get(position).getSportsTypeList().size() > 5) {
			List<ImagePath> mNewImagePathDate = data.get(position)
					.getSportsTypeList().subList(0, 5);
			holder.gridview_sport
					.setAdapter(new ImageAdapter(mNewImagePathDate));
			holder.iv_sport_more.setVisibility(View.VISIBLE);
		} else {
			holder.gridview_sport.setAdapter(new ImageAdapter(data
					.get(position).getSportsTypeList()));
			holder.iv_sport_more.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ImageAdapter extends BaseAdapter {
		private List<ImagePath> data;

		public ImageAdapter(List<ImagePath> data) {
			this.data = data;
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
			// TODO Auto-generated method stub
			CircleImageView imageview;
			if (convertView == null) {
				imageview = new CircleImageView(c);
				imageview.setLayoutParams(new GridView.LayoutParams(dpToPx(20),
						dpToPx(20)));
				imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageview.setPadding(5, 5, 5, 5);
			} else {
				imageview = (CircleImageView) convertView;
			}
			// imageview.setImageResource(mThumbIds[position]);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + data.get(position).getImagePath(),
					imageview);
			return imageview;
		}
	}

	public int dpToPx(int dpValue) {
		float scale = c.getResources().getDisplayMetrics().density;
		return Math.round(dpValue * scale);
	}

	final class ViewHolder {
		public CircleImageView imgViewHead;
		public ImageView iv_sport_more;
		public TextView txtViewNickName, txtViewStatus;
		public MyGridView gridview_sport;
	}

}
