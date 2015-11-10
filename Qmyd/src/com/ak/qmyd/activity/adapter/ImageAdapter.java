package com.ak.qmyd.activity.adapter;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ak.qmyd.bean.ImagePath;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.ToastManager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<ImagePath> data;

	public ImageAdapter(Context c, ArrayList<ImagePath> data) {
		mContext = c;
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

		final ImageView imageview;
		if (convertView == null) {
			imageview = new ImageView(mContext);
			imageview.setLayoutParams(new GridView.LayoutParams(
					LayoutParams.MATCH_PARENT, 160));
			imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageview.setPadding(8, 8, 8, 8);
		} else {
			imageview = (ImageView) convertView;
		} 
		// imageview.setImageResource(mThumbIds[position]);
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + data.get(position).getImagePath(), imageview);
		return imageview;
	}

}
