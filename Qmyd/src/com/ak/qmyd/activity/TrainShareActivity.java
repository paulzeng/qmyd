package com.ak.qmyd.activity;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.tools.NetManager;

/**
 * @author JGB
 * @date
 */
public class TrainShareActivity extends BaseFragmentActivity implements
		OnClickListener ,PlatformActionListener{
	private ImageView iv_share_wechat, iv_share_qzone, iv_share_sina,
			iv_share_close;
	private DrawerLayout mDrawerLayout;
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch ((int)msg.what) {
			case 0x110:
				showToast("分享出错！请检查是否安装了客户端");
				break;
			case 0x111:
				showToast("分享成功！");
				break;
			case 0x112:
				showToast("分享取消！");
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		ShareSDK.initSDK(this);
		findView();
		if(NetManager.isNetworkConnected(this)){
			initView();
		}else{
			showToast("网络不可用，请检查网络设置");
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_train_share_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initView() {
		iv_share_wechat.setOnClickListener(this);
		iv_share_qzone.setOnClickListener(this);
		iv_share_sina.setOnClickListener(this);
		iv_share_close.setOnClickListener(this);
	}

	private void findView() {
		iv_share_wechat = (ImageView) findViewById(R.id.iv_share_wechat);
		iv_share_qzone = (ImageView) findViewById(R.id.iv_share_qzone);
		iv_share_sina = (ImageView) findViewById(R.id.iv_share_sina);
		iv_share_close = (ImageView) findViewById(R.id.iv_share_close);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_share_wechat:
			ShareParams sp2 = new ShareParams();
			sp2.setText(getIntent().getStringExtra("sectionName")+"获得了"+getIntent().getStringExtra("trainScore")+"分,可喜可贺！");
			sp2.setImagePath(getIntent().getStringExtra("fname"));
			Platform wechat = ShareSDK.getPlatform(this, WechatMoments.NAME);
			wechat.setPlatformActionListener(new PlatformActionListener() {
				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					sendMyMessage(0x110);
				}

				@Override
				public void onComplete(Platform arg0, int arg1,
						HashMap<String, Object> arg2) {
					sendMyMessage(0x111);
				}

				@Override
				public void onCancel(Platform arg0, int arg1) {
					sendMyMessage(0x112);
				}
			}); // 设置分享事件回调
			// 执行图文分享
			wechat.share(sp2);
			break;
		case R.id.iv_share_qzone:
			String fname = getIntent().getStringExtra("fname");
			ShareParams sp = new ShareParams();
			sp.setTitle("全民体育");
			sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
			sp.setText(getIntent().getStringExtra("sectionName")+"获得了"+getIntent().getStringExtra("trainScore")+"分,可喜可贺！");
			sp.setImagePath(fname);

			Platform qzone = ShareSDK.getPlatform(QZone.NAME);
			qzone.setPlatformActionListener(new PlatformActionListener() {

				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					sendMyMessage(0x110);
				}

				@Override
				public void onComplete(Platform arg0, int arg1,
						HashMap<String, Object> arg2) {
					sendMyMessage(0x111);
				}

				@Override
				public void onCancel(Platform arg0, int arg1) {
					sendMyMessage(0x112);
				}
			}); // 设置分享事件回调
			// 执行图文分享
			qzone.share(sp);
			break;
		case R.id.iv_share_sina:
			ShareParams sp1 = new ShareParams();
			sp1.setText(getIntent().getStringExtra("sectionName")+"获得了"+getIntent().getStringExtra("trainScore")+"分,可喜可贺！");
			sp1.setImagePath(getIntent().getStringExtra("fname"));
			Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			weibo.setPlatformActionListener(new PlatformActionListener() {
				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					sendMyMessage(0x110);
				}

				@Override
				public void onComplete(Platform arg0, int arg1,
						HashMap<String, Object> arg2) {
					sendMyMessage(0x111);

				}

				@Override
				public void onCancel(Platform arg0, int arg1) {
					sendMyMessage(0x112);

				}
			}); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp1);
			break;
		case R.id.iv_share_close:
			finish();
			skipActivity(SynchronousTrainActivity2.class, getIntent()
					.getStringExtra("sectionId"));
			break;
		default:
			break;
		}

	}

	protected void sendMyMessage(int i) {
		Message msg = Message.obtain();
		msg.what = i;
		myHandler.sendMessage(msg);
	}

	private String getImage(View v) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.CHINA);
		    String fname = "/sdcard/"+sdf.format(new Date())+ ".png";
		    View view = v.getRootView();
		    view.setDrawingCacheEnabled(true);
		    view.buildDrawingCache();
		    Bitmap bitmap = view.getDrawingCache();
		    if(bitmap != null)
 {      
		    try{ FileOutputStream out = new FileOutputStream(fname);
		        bitmap.compress(Bitmap.CompressFormat.PNG,100,
 out);
		      }catch(Exception
 e) {
		        e.printStackTrace();
		      }
		    }else{
		      }
		return fname;
	}

	

	private void skipActivity(Class clazz, String str) {
		Intent newIntent = new Intent(TrainShareActivity.this, clazz);
		newIntent.putExtra("sectionId", str);
		newIntent.putExtra("typeId", getIntent().getStringExtra("typeId"));
		newIntent.putExtra("trainID", getIntent().getStringExtra("trainID"));
		startActivity(newIntent);

	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		sendMyMessage(0x112);
		
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		sendMyMessage(0x111);
		
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		sendMyMessage(0x110);
		
	}
}
