package com.ak.qmyd.adapt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.CircleContentNoteDetailActivity;
import com.ak.qmyd.activity.GusterInfoActivity;
import com.ak.qmyd.bean.result.CircleDetailResult.CircleDetailList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/** 
 * @author JGB
 * @date 2015-6-25 上午11:31:02
 */
public class MyCircleDetailListAdapter extends BaseAdapter{


	private Context context;
	private ArrayList<CircleDetailList> totalCircleDetailList;
	
	public MyCircleDetailListAdapter(Context context,ArrayList<CircleDetailList> totalCircleDetailList) {
		this.context = context;
		this.totalCircleDetailList = totalCircleDetailList;
	}

	@Override
	public int getCount() {
		return totalCircleDetailList == null ? 0 : totalCircleDetailList.size();
	}

	@Override
	public Object getItem(int position) {
		return totalCircleDetailList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.activity_circle_detail_listview_item,
							null);
			holder.tv_circle_list_item_title = (TextView) convertView
					.findViewById(R.id.tv_circle_list_item_title);
			holder.tv_circle_list_item_name = (TextView) convertView
					.findViewById(R.id.tv_circle_list_item_name);
			holder.tv_circle_list_item_comment = (TextView) convertView
					.findViewById(R.id.tv_circle_list_item_comment);
			holder.tv_circle_list_item_time = (TextView) convertView
					.findViewById(R.id.tv_circle_list_item_time);
			holder.iv_circle_list_item_img = (CircleImageView) convertView
					.findViewById(R.id.iv_circle_list_item_img);
			holder.iv_circle_list_item_is_good = (ImageView) convertView
					.findViewById(R.id.iv_circle_list_item_is_good);
			holder.rl_circle_list_item_user = (RelativeLayout) convertView
					.findViewById(R.id.rl_circle_list_item_user);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.rl_circle_list_item_user
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent newIntent = new Intent(
								context,
								GusterInfoActivity.class);
						newIntent.putExtra("userid",
								totalCircleDetailList.get(position).getUserId());
						context.startActivity(newIntent);
					}
				});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(context,
						CircleContentNoteDetailActivity.class);
				newIntent.putExtra("circleId",
						((Activity) context).getIntent().getStringExtra("circleId"));
				newIntent.putExtra("infoId", totalCircleDetailList.get(position)
						.getCircleInfoId());
				context.startActivity(newIntent);
			}
		});
		holder.tv_circle_list_item_title.setText(totalCircleDetailList.get(
				position).getInfoTitle());
		holder.tv_circle_list_item_name.setText(totalCircleDetailList.get(
				position).getUserName());
		holder.tv_circle_list_item_comment.setText(totalCircleDetailList.get(
				position).getReplayNum()
				+ "评论");
		if ((new SimpleDateFormat("MM-dd", Locale.getDefault())
				.format(new Date())).equals(totalCircleDetailList.get(position)
				.getCreateTime().substring(5, 10))) {
			holder.tv_circle_list_item_time.setText("今天"
					+ totalCircleDetailList.get(position).getCreateTime()
							.substring(10));
		} else {
			holder.tv_circle_list_item_time.setText(totalCircleDetailList
					.get(position).getCreateTime().substring(0, 10));
		}
		ImageLoader.getInstance()
				.displayImage(
						Config.BASE_URL
								+ totalCircleDetailList.get(position)
										.getThumbnailPath(),
						holder.iv_circle_list_item_img);
		if (totalCircleDetailList.get(position).getInfoType().equals("1")) {
			holder.iv_circle_list_item_is_good.setVisibility(View.VISIBLE);
		} else {
			holder.iv_circle_list_item_is_good.setVisibility(View.GONE);
		}
		return convertView;
	}
	static class ViewHolder {
		TextView tv_circle_list_item_title, tv_circle_list_item_name,
				tv_circle_list_item_comment, tv_circle_list_item_time;
		ImageView iv_circle_list_item_is_good;
		CircleImageView iv_circle_list_item_img;
		RelativeLayout rl_circle_list_item_user;
	}
}
