package com.ak.qmyd.activity;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;

@SuppressLint("NewApi")
public class SurfaceViewOpenActivity extends BaseActivity implements
		OnClickListener {

	private SurfaceView surface1;
	private Button btnGo;
	private MediaPlayer mediaPlayer;

	private int postion = 0;
	private boolean isFirst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFirst = getSharedPreferences("config", 0).getBoolean("isFirst", true);
		if (isFirst == false) {
			startActivity(HomeActivity.class, null);
			finish();
		} else {
			setContentView(R.layout.activity_surfaceview);
			SharedPreferences preferences = this.getSharedPreferences(
					"first_pref", Context.MODE_PRIVATE);
			if (!preferences.getBoolean("isFirst", true)) {
				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
			findViewById();
			initView();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goto:
			Intent intent = new Intent(this, ChooseColumnActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_form_left, R.anim.out_of_right);
			finish();
			break;
		default:
			break;
		}
	}

	public void play() throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {
		// mediaPlayer.reset();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		AssetFileDescriptor fd = this.getAssets().openFd("start.mp4");
		mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
				fd.getLength());
		mediaPlayer.setLooping(true);
		mediaPlayer.setDisplay(surface1.getHolder());
		// 通过异步的方式装载媒体资源
		mediaPlayer.prepareAsync();
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// 装载完毕回调
				mediaPlayer.start();
			}
		});
		// mediaPlayer.prepare();
		// mediaPlayer.start();
	}

	private class SurfaceViewLis implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if (postion == 0) {
				try {
					play();
					// mediaPlayer.seekTo(postion);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

		}

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		if (mediaPlayer.isPlaying()) {
			postion = mediaPlayer.getCurrentPosition();
			mediaPlayer.start();
		}
		super.onRestart();
	}

	@Override
	protected void onPause() {
		if (!mediaPlayer.isPlaying()) {
			postion = mediaPlayer.getCurrentPosition();
			mediaPlayer.stop();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying())
				mediaPlayer.stop();
			mediaPlayer.release();
		}

		super.onDestroy();
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		surface1 = (SurfaceView) findViewById(R.id.surface1);
		btnGo = (Button) findViewById(R.id.btn_goto);

	}

	protected void initView() {
		// TODO Auto-generated method stub
		mediaPlayer = new MediaPlayer();
		surface1.getHolder().setKeepScreenOn(true);
		surface1.getHolder().addCallback(new SurfaceViewLis());
		btnGo.setOnClickListener(this);
	}

}
