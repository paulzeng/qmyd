package com.ak.qmyd.activity.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.GusterInfoActivity;
import com.ak.qmyd.bean.DongTaiContentBean;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.ImageManager;
import com.ak.qmyd.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListDongTaiContentAdapter extends BaseAdapter {
	List<DongTaiContentBean> data;
	Context c;
	private LayoutInflater inflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public ListDongTaiContentAdapter(Context context,
			List<DongTaiContentBean> mDate) {
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
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_dongtai_content, null);
			holder.imgViewHead = (CircleImageView) convertView
					.findViewById(R.id.dongtai_user_img);
			holder.txtViewNickName = (TextView) convertView
					.findViewById(R.id.tv_dongtai_name);
			holder.txtViewTime = (TextView) convertView
					.findViewById(R.id.tv_dongtai_time);
			holder.txtViewContent = (TextView) convertView
					.findViewById(R.id.tv_dongtai_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		imageLoader.displayImage(Config.BASE_URL
				+ data.get(position).getThumbnailPath(), holder.imgViewHead,
				ImageManager.getViewsHeadOptions());
		holder.imgViewHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(c, GusterInfoActivity.class);
				intent.putExtra("userid", data.get(position).getUserId());
				c.startActivity(intent);
			}
		});
		holder.txtViewNickName.setText(data.get(position).getUserName());
		if ((new SimpleDateFormat("MM-dd", Locale.getDefault())
				.format(new Date())).equals(data.get(position)
				.getCreateTime().substring(5, 10))) {
			holder.txtViewTime.setText("½ñÌì"
					+ data.get(position).getCreateTime()
							.substring(10));
		} else {
			holder.txtViewTime.setText(data
					.get(position).getCreateTime().substring(0, 10));
		} 
		holder.txtViewContent.setText(data.get(position).getContent());

		return convertView;
	}

	final class ViewHolder {
		public CircleImageView imgViewHead;
		public TextView txtViewNickName, txtViewTime, txtViewContent;
	}
}
