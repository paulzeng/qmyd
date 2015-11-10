package com.photoselector.ui;

/**
 * 
 * @author Aizaz AZ
 *
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ak.qmyd.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.photoselector.model.PhotoModel;
import com.polites.GestureImageView;

public class PhotoPreview extends LinearLayout implements OnClickListener,
		OnLongClickListener {

	private ProgressBar pbLoading;
	private GestureImageView ivContent;
	private OnClickListener l;
	private OnLongClickListener longClick; 

	public PhotoPreview(Context context) {
		super(context); 
		LayoutInflater.from(context).inflate(R.layout.view_photopreview, this,
				true);

		pbLoading = (ProgressBar) findViewById(R.id.pb_loading_vpp);
		ivContent = (GestureImageView) findViewById(R.id.iv_content_vpp);
		ivContent.setOnClickListener(this);
		ivContent.setOnLongClickListener(this); 
	}

	public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
		this(context);
	}

	public PhotoPreview(Context context, AttributeSet attrs) {
		this(context);
	}

	public void loadImage(PhotoModel photoModel) {
		loadImage("file://" + photoModel.getOriginalPath());
	}

	public void loadImage(String path) {
		ImageLoader.getInstance().loadImage(path,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						ivContent.setImageBitmap(loadedImage);
						pbLoading.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						ivContent.setImageDrawable(getResources().getDrawable(
								R.drawable.ic_picture_loadfailed));
						pbLoading.setVisibility(View.GONE);
					}
				});
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_content_vpp && l != null)
			l.onClick(ivContent);
	}
	
	@Override
	public void setOnLongClickListener(OnLongClickListener longClick) {
		this.longClick = longClick; 
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.iv_content_vpp && longClick != null)
			longClick.onLongClick(ivContent);
		return false;
	};

	 
}
