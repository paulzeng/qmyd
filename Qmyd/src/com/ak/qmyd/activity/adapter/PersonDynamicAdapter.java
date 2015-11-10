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
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.GusterInfoActivity;
import com.ak.qmyd.activity.PhotoPreviewActivity;
import com.ak.qmyd.activity.listener.ListItemClickHelp;
import com.ak.qmyd.bean.DongTaiContentBean;
import com.ak.qmyd.bean.ImagePath;
import com.ak.qmyd.bean.StaffInfo;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.ImageManager;
import com.ak.qmyd.view.CircleImageView;
import com.ak.qmyd.view.MyGridView;
import com.ak.qmyd.view.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonDynamicAdapter extends BaseAdapter {
	List<StaffInfo> data;
	Context c;
	private LayoutInflater inflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ListItemClickHelp callback;

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public PersonDynamicAdapter(Context context, List<StaffInfo> mDate,
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
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item_dynamic, null);
			holder.iv_head_img = (CircleImageView) convertView
					.findViewById(R.id.iv_item_dynamic_head_img);
			holder.tv_head_name = (TextView) convertView
					.findViewById(R.id.tv_item_dynamic_head_name);
			holder.tv_head_time = (TextView) convertView
					.findViewById(R.id.tv_item_dynamic_head_time);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_item_dynamic_content);
			holder.tv_dianzan = (TextView) convertView
					.findViewById(R.id.tv_item_dynamic_dianzan);
			holder.tv_reply = (TextView) convertView
					.findViewById(R.id.tv_item_dynamic_reply);
			holder.ll_dianzan = (LinearLayout) convertView
					.findViewById(R.id.ll_item_dynamic_dianzan);
			holder.iv_dianzan = (ImageView) convertView
					.findViewById(R.id.iv_dianzan);
			holder.ll_reply = (LinearLayout) convertView
					.findViewById(R.id.ll_item_dynamic_reply);

			holder.dynamic_grid_image = (MyGridView) convertView
					.findViewById(R.id.dynamic_item_dynamic_grid_image);
			holder.gridview_zan = (MyGridView) convertView
					.findViewById(R.id.item_dynamic_gridview_zan);
			holder.rl_zan = (RelativeLayout) convertView
					.findViewById(R.id.rl_zan);
			holder.lv_content_list = (MyListView) convertView
					.findViewById(R.id.lv_item_dynamic_content_list);

			holder.iv_delete = (ImageView) convertView
					.findViewById(R.id.iv_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (data.get(position).getIsDel().equals("1")) {
			holder.iv_delete.setVisibility(View.VISIBLE);
			final View view = convertView;
			final int p = position;
			final int one = holder.iv_delete.getId();
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callback.onClick(view, parent, p, one);
				}
			});
		} else {
			holder.iv_delete.setVisibility(View.GONE);
		}

		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + data.get(position).getThumbnailPath(),
				holder.iv_head_img);
		holder.tv_head_name.setText(data.get(position).getUserName());
		// holder.tv_head_time.setText(data.get(position).getCreateTime());
		if ((new SimpleDateFormat("MM-dd", Locale.getDefault())
				.format(new Date())).equals(data.get(position).getCreateTime()
				.substring(5, 10))) {
			holder.tv_head_time.setText("今天"
					+ data.get(position).getCreateTime().substring(10));
		} else {
			holder.tv_head_time.setText(data.get(position).getCreateTime()
					.substring(0, 10));
		}
		if (data.get(position).getContent().equals("null")) {
			holder.tv_content.setVisibility(View.GONE);
		} else {
			holder.tv_content.setVisibility(View.VISIBLE);
			holder.tv_content.setText(data.get(position).getContent());
		}
		if (data.get(position).getPraiseNum().equals("0")) {
			holder.iv_dianzan.setBackgroundResource(R.drawable.zan_g_);
		} else {
			holder.iv_dianzan.setBackgroundResource(R.drawable.zan_y);
		}
		holder.tv_dianzan.setText(data.get(position).getPraiseNum());

		final View view = convertView;
		final int p = position;
		final int two = holder.ll_dianzan.getId();
		final int three = holder.ll_reply.getId();
		holder.ll_dianzan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(view, parent, p, two);
			}
		});
		holder.tv_reply.setText(data.get(position).getReplayNum());
		holder.ll_reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(view, parent, p, three);
			}
		});
		holder.dynamic_grid_image
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						Intent intent = new Intent(c,
								PhotoPreviewActivity.class);

						intent.putExtra("photos", data.get(position)
								.getInfoPicList());
						DebugUtility.showLog("查看的图片：" + data.toString());

						intent.putExtra("position", arg2);

						c.startActivity(intent);
					}
				});
		if (data.get(position).getInfoPicList() != null) {
			holder.dynamic_grid_image.setAdapter(new ImageAdapter(data.get(
					position).getInfoPicList()));
		}
		if (data.get(position).getInfoCommentList() != null) {
			DebugUtility.showLog("###评论列表####"
					+ data.get(position).getInfoCommentList().toString());
			holder.lv_content_list.setVisibility(View.VISIBLE);
			holder.lv_content_list.setAdapter(new ListUserContentAdapter(data
					.get(position).getInfoCommentList()));
		} else {
			holder.lv_content_list.setVisibility(View.GONE);
		}
		if (data.get(position).getInfoPraiseList() != null) {
			holder.rl_zan.setVisibility(View.VISIBLE);
			holder.gridview_zan.setAdapter(new PraiseImageAdapter(data.get(
					position).getInfoPraiseList()));
			holder.gridview_zan
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(c,
									GusterInfoActivity.class);
							intent.putExtra("userid", data.get(position)
									.getInfoPraiseList().get(arg2).getUserId());
							c.startActivity(intent);
						}
					});
		} else {
			holder.rl_zan.setVisibility(View.GONE);
		}

		return convertView;
	}

	final class ViewHolder {
		public CircleImageView iv_head_img;
		public TextView tv_head_name, tv_head_time, tv_content, tv_dianzan,
				tv_reply;
		public MyGridView dynamic_grid_image, gridview_zan;
		private ImageView iv_delete, iv_dianzan;
		public MyListView lv_content_list;
		public LinearLayout ll_dianzan, ll_reply;
		public RelativeLayout rl_zan;
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
			ImageView imageview;
			if (convertView == null) {
				imageview = new ImageView(c);
				imageview.setLayoutParams(new GridView.LayoutParams(
						LayoutParams.MATCH_PARENT, 160));
				imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageview.setPadding(8, 8, 8, 8);
			} else {
				imageview = (ImageView) convertView;
			}
			// imageview.setImageResource(mThumbIds[position]);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + data.get(position).getImagePath(),
					imageview);
			return imageview;
		}

	}

	private class PraiseImageAdapter extends BaseAdapter {
		private List<StaffInfo.InfoPraise> data;

		public PraiseImageAdapter(List<StaffInfo.InfoPraise> data) {
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
				imageview.setLayoutParams(new GridView.LayoutParams(dpToPx(30),
						dpToPx(30)));
				imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageview.setPadding(0, 8, 0, 8);
			} else {
				imageview = (CircleImageView) convertView;
			}
			// imageview.setImageResource(mThumbIds[position]);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + data.get(position).getThumbnailPath(),
					imageview);
			return imageview;
		}

		public int dpToPx(int dpValue) {
			float scale = c.getResources().getDisplayMetrics().density;
			return Math.round(dpValue * scale);
		}

	}

	private class ListUserContentAdapter extends BaseAdapter {
		List<DongTaiContentBean> data;
		private LayoutInflater inflater;
		protected ImageLoader imageLoader = ImageLoader.getInstance();

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		public ListUserContentAdapter(List<DongTaiContentBean> mDate) {
			inflater = LayoutInflater.from(c);
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
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_dongtai_content,
						null);
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
					+ data.get(position).getThumbnailPath(),
					holder.imgViewHead, ImageManager.getViewsHeadOptions());
			holder.txtViewNickName.setText(data.get(position).getUserName());
			// holder.txtViewTime.setText(data.get(position).getCreateTime());
			if ((new SimpleDateFormat("MM-dd", Locale.getDefault())
					.format(new Date())).equals(data.get(position)
					.getCreateTime().substring(5, 10))) {
				holder.txtViewTime.setText("今天"
						+ data.get(position).getCreateTime().substring(10));
			} else {
				holder.txtViewTime.setText(data.get(position).getCreateTime()
						.substring(0, 10));
			}
			holder.txtViewContent.setText(data.get(position).getContent());
			holder.imgViewHead.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(c, GusterInfoActivity.class);
					intent.putExtra("userid", data.get(position).getUserId());
					c.startActivity(intent);
				}
			});
			return convertView;
		}

		final class ViewHolder {
			public CircleImageView imgViewHead;
			public TextView txtViewNickName, txtViewTime, txtViewContent;
		}
	}

}
