package com.ak.qmyd.activity.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.CircleDetailActivity;
import com.ak.qmyd.bean.result.CircleListResult.HotCircleList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/** 
 * @author JGB
 * @date 2015-6-24 ÏÂÎç5:13:56
 */
public class HotCircleListAdapter extends BaseAdapter{


	private Context context;
    private ArrayList<HotCircleList> hotCircleList;
	
	public HotCircleListAdapter(Context context,ArrayList<HotCircleList> hotCircleList) {
		this.context = context;
		this.hotCircleList = hotCircleList;
	}

	@Override
	public int getCount() {
		return hotCircleList == null ? 0 : hotCircleList.size();
	}

	@Override
	public Object getItem(int position) {
		return hotCircleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		ListViewHolder listHolder = null;
		if (convertView == null) {
			listHolder = new ListViewHolder();
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.activity_circle_listview_item, null);
			listHolder.iv_circle_item_img = (CircleImageView) convertView
					.findViewById(R.id.iv_circle_item_img);
			listHolder.iv_circle_item_title = (TextView) convertView
					.findViewById(R.id.iv_circle_item_title);
			listHolder.iv_circle_item_content = (TextView) convertView
					.findViewById(R.id.iv_circle_item_content);
			convertView.setTag(listHolder);
		} else {
			listHolder = (ListViewHolder) convertView.getTag();
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(context,
						CircleDetailActivity.class);
				newIntent.putExtra("circleId", hotCircleList.get(position)
						.getCircleId());
				context.startActivity(newIntent);
			}
		});
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL
						+ hotCircleList.get(position).getThunmnailPath(),
				listHolder.iv_circle_item_img);
		listHolder.iv_circle_item_title.setText(hotCircleList.get(position)
				.getCircleName());
		listHolder.iv_circle_item_content.setText(hotCircleList.get(
				position).getCircleNotice());
		return convertView;
	}
	static class ListViewHolder {
		CircleImageView iv_circle_item_img;
		TextView iv_circle_item_title, iv_circle_item_content;
	}
}
