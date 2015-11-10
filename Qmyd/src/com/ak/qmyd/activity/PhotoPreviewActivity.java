package com.ak.qmyd.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.ImagePath;
import com.ak.qmyd.bean.PicUrl;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.photoselector.ui.BasePhotoPreviewActivity;
import com.photoselector.ui.PhotoPreview;
import com.photoselector.util.AnimationUtil;

@SuppressLint("NewApi")
public class PhotoPreviewActivity extends BasePhotoPreviewActivity implements
		OnPageChangeListener, OnClickListener {

	private ViewPager mViewPager;
	private RelativeLayout layoutTop;
	private ImageButton btnBack;
	private TextView tvPercent, tv_save;
	protected List<ImagePath> photos;
	protected List<PicUrl> venues_photos;
	protected int current;
	private int from;
	private PhotoPreview photoPreview;
	private Bitmap bitmap;
	private String fileName;
	private String fname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photopreview);
		BitmapUtils.initImageLoader(getApplicationContext());
		layoutTop = (RelativeLayout) findViewById(R.id.layout_top_app);
		btnBack = (ImageButton) findViewById(R.id.btn_back_app);
		tvPercent = (TextView) findViewById(R.id.tv_percent_app);
		mViewPager = (ViewPager) findViewById(R.id.vp_base_app);
		mViewPager.setOnPageChangeListener(this);
		tv_save = (TextView) findViewById(R.id.tv_save);
		init(getIntent().getExtras());
		overridePendingTransition(R.anim.activity_alpha_action_in, 0);
		tv_save.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		imageView = new ImageView(this);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}

	@SuppressWarnings("unchecked")
	protected void init(Bundle extras) {
		if (extras == null)
			return;

		if (extras.containsKey("photos")) {
			photos = (List<ImagePath>) extras.getSerializable("photos");
			current = extras.getInt("position", 0);
			from = extras.getInt("from", 0);
			updatePercent(extras);
			bindData();
		}
		if (extras.containsKey("venues_photos")) {
			venues_photos = (List<PicUrl>) extras
					.getSerializable("venues_photos");
			current = extras.getInt("position", 0);
			from = extras.getInt("from", 0);
			updatePercent(extras);
			bindData();
		}
	}

	protected void bindData() {
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(current);
	}

	private PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public int getCount() {
			if (getIntent().getExtras().containsKey("venues_photos")) {
				if (venues_photos == null) {
					return 0;
				} else {
					return venues_photos.size();
				}
			} else {
				if (photos == null) {
					return 0;
				} else {
					return photos.size();
				}
			}
		}

		@Override
		public View instantiateItem(final ViewGroup container,
				final int position) {
			photoPreview = new PhotoPreview(getApplicationContext());
			((ViewPager) container).addView(photoPreview);
			if (from == 1) {
				// 从动态过来
				photoPreview.loadImage("file://"
						+ photos.get(position).getImagePath());
			} else {
				if (getIntent().getExtras().containsKey("venues_photos")) {
					photoPreview.loadImage(Config.BASE_URL
							+ venues_photos.get(position).getPicUrl());
					ImageLoader.getInstance().displayImage(
							Config.BASE_URL
									+ venues_photos.get(position).getPicUrl(),
							imageView);
				} else {
					photoPreview.loadImage(Config.BASE_URL
							+ photos.get(position).getImagePath());

				}
			}
			photoPreview.setOnClickListener(photoItemClickListener);
			photoPreview.setOnLongClickListener(photoItemLongClickListener);

			return photoPreview;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	};
	protected boolean isUp;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_app:
			finish();
			break;
		case R.id.tv_save:
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
					Locale.CHINA);
			mViewPager.setDrawingCacheEnabled(true);
			Bitmap bitmap = Bitmap.createBitmap(mViewPager
					.getDrawingCache(true));
			File file = Tools.getFileFromBytes(Tools.Bitmap2Bytes(bitmap),
					Environment.getExternalStorageDirectory().getPath()
							+ "/qmty" + sdf.format(new Date()) + ".png");
			mViewPager.destroyDrawingCache();
			// layoutTop.setVisibility(View.GONE);
			// fname = getImage(v);
			showToast("保存为: "
					+ Environment.getExternalStorageDirectory().getPath()
					+ "/qmty" + sdf.format(new Date()) + ".png");
			finish();
			break;
		default:
			break;
		}
	}

	private String getImage(View v) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.CHINA);
		String fname = "/sdcard/" + sdf.format(new Date()) + ".png";
		View view = v.getRootView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		if (bitmap != null) {
			try {
				FileOutputStream out = new FileOutputStream(fname);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				showToast("保存为: " + fname);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		}
		return fname;

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		current = arg0;
		updatePercent(getIntent().getExtras());
	}

	protected void updatePercent(Bundle extras) {
		if (extras.containsKey("venues_photos")) {
			tvPercent.setText((current + 1) + "/" + venues_photos.size());
		} else {
			tvPercent.setText((current + 1) + "/" + photos.size());
		}
	}

	private OnClickListener photoItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isUp) {
				new AnimationUtil(getApplicationContext(), R.anim.translate_up)
						.setInterpolator(new LinearInterpolator())
						.setFillAfter(true).startAnimation(layoutTop);
				isUp = true;
			} else {
				new AnimationUtil(getApplicationContext(),
						R.anim.translate_down_current)
						.setInterpolator(new LinearInterpolator())
						.setFillAfter(true).startAnimation(layoutTop);
				isUp = false;
			}
		}
	};

	private OnLongClickListener photoItemLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View view) {
			Toast.makeText(PhotoPreviewActivity.this, "长按图片",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	};
	private SimpleDateFormat sdf;
	private ImageView imageView;

	private Bitmap getInsertedImage(String imagePath) {
		if (imagePath == null || imagePath.equals("")) {
			return null;
		}
		Bitmap bm = null;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, opts);
		opts.inJustDecodeBounds = false;
		int sampleSize = 1;
		while (true) {
			if (opts.outHeight * opts.outWidth / sampleSize < 1281 * 901) {
				break;
			}
			sampleSize *= 2;
		}
		opts.inSampleSize = sampleSize;
		bm = BitmapFactory.decodeFile(imagePath, opts);
		return bm;
	}
}
