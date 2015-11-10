package com.ak.qmyd.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.DongTaiDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ViewFlowAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private final String[] images = { "assets://application_gallery.png",
			"assets://application_gallery.png",
			"assets://application_gallery.png" };
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Context mContext;

	public ViewFlowAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.video_gallery_default)
				.showImageForEmptyUri(R.drawable.video_gallery_default)
				.showImageOnFail(R.drawable.video_gallery_default)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(images[position % images.length],
				viewHolder.imageView, options);
		// viewHolder.imageView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(mContext,
		// DongTaiDetailActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// mContext.startActivity(intent);
		// }
		// });
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
	}

}
