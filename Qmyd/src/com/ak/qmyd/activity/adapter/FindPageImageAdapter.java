package com.ak.qmyd.activity.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.DongTaiDetailActivity;
import com.ak.qmyd.activity.GusterInfoActivity;
import com.ak.qmyd.bean.FindUser;
import com.ak.qmyd.config.Config;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author JGB
 * @date 2015-4-23 下午12:41:28
 */
public class FindPageImageAdapter extends PagerAdapter {

	private List<FindUser> images;
	private Activity mActivity;
	private LayoutInflater inflater;

	public FindPageImageAdapter(List<FindUser> images, Activity mActivity) {
		inflater = mActivity.getLayoutInflater();
		this.images = images;
		this.mActivity = mActivity;
	}

	@Override
	public int getCount() {
		return images == null ? 0 : images.size();
	}

	@Override
	public Object instantiateItem(View view, final int position) {
		final View imageLayout = inflater.inflate(R.layout.item_viewpager_user,
				null);
		final ImageView imageView = (ImageView) imageLayout
				.findViewById(R.id.find_banner_image);
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + images.get(position).getImagePath(),
				imageView);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity,
						DongTaiDetailActivity.class);
				intent.putExtra("id", images.get(position).getMyInfoId());
				intent.putExtra("staffId", images.get(position).getUserId());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(intent);
			}
		});
		
		final ImageView iv_user_img = (ImageView) imageLayout
				.findViewById(R.id.iv_user_img);
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + images.get(position).getThumbnailPath(),
				iv_user_img);
		iv_user_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, GusterInfoActivity.class);
				intent.putExtra("userid", images.get(position).getUserId());
				mActivity.startActivity(intent);
			}
		});
		
		final TextView tv_user_name = (TextView) imageLayout
				.findViewById(R.id.tv_user_name);
		tv_user_name.setText(images.get(position).getUserName());
		
		((ViewPager) view).addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	// PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		// view.removeView(images.get(position % images.size()));
	}

}
