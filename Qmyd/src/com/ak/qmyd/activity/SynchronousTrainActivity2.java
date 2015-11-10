package com.ak.qmyd.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.SynchronousTrainResult;
import com.ak.qmyd.bean.result.SynchronousTrainResult.SectionObject;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.dialog.MenuDialog;
import com.ak.qmyd.dialog.MenuDialog.OnActionSheetSelected;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.TimeUtils;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SynchronousTrainActivity2 extends BaseFragmentActivity implements
		OnClickListener {
	protected static final int CURRENT_PROGRESS = 0;
	protected static final int VIDEO_PATH = 1;
	protected static final int TIME_PROGRESS = 2;
	protected static final int COMPLETE = 3;
	private VideoView vv_sync_video;
	private Button bt_sync_start, bt_sync_stop, bt_sync_end, bt_sync_start_bt;
	private File f;
	private LinearLayout ll_start_train, ll_sync_details, ll_sync_images;
	private TextView tv_sync_title, tv_sync_time, tv_sync_count, tv_sync_heat,
			tv_sync_total_time;
	private TextView tv_sync_current_time;
	private int type = 1;
	private ProgressBar pb_sync_progrebar, pb_sync_down_progressbar;
	private boolean isPlaying = false;
	private RelativeLayout rl_sync_progressbar_btn, rl_sync_progressbar_btn1,
			ll_start_train_btns;
	private boolean isLoading = true;
	private FrameLayout fl_sync_videoview;
	private ImageView iv_sync_videoview_img, iv_sync_train_clock,
			iv_sync_train_list, iv_sync_train_history;
	private ImageButton ib_sync_return;
	private int currentTime;
	private SharedPreferences sp;
	private SectionObject sectionObject;
	private int sign;
	private String chapterId;
	private String chapterName;
	private String startTime;
	private MenuDialog dialog;
	private String endTime;
	private String typeName;
	private int current;
	private int myTime = 0;
	private int myCurrentTime;
	private boolean ishasTime = false;
	private long recordingTime = 0;// 记录下来的总时间
	private Message msg;
	private double startTime1;
	private String sectionLength;
	private int intervalTime;
	private DrawerLayout mDrawerLayout;
	private String str;
	private SharedPreferences.Editor editor;
	private Map<String, ?> userInfoSp;
	private double endTime2;
	private int temp;
	private MediaController mediaController;
	private int myCount = 0;
	private boolean isComplete = false;
	private Dialog loadDialog;
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CURRENT_PROGRESS:
				currentTime = (Integer) msg.obj;
				if (vv_sync_video.isPlaying()) {
					pb_sync_progrebar.setProgress(currentTime);
					tv_sync_current_time.setText(TimeUtils
							.MSformat(vv_sync_video.getDuration() * myCount
									+ vv_sync_video.getCurrentPosition()));
				}
				break;
			case TIME_PROGRESS:
				myCurrentTime = (Integer) msg.obj;
				if (vv_sync_video.isPlaying()) {
					tv_sync_current_time.setText(TimeUtils
							.MSformat(vv_sync_video.getDuration() * myCount
									+ vv_sync_video.getCurrentPosition()));
				}
				break;
			case 0x123:
				bt_sync_start.setClickable(false);
				break;
			case 0x321:
				bt_sync_start.setText("开始训练");
				bt_sync_start.setClickable(true);
				bt_sync_start
						.setBackgroundResource(R.drawable.aa_button_gray_pressed);
				changBgView();
				sign = (Integer) msg.obj;
				break;
			case COMPLETE:
				isComplete = true;
				tv_sync_current_time.setText(TimeUtils.MSformat(vv_sync_video
						.getDuration()
						* myCount
						+ vv_sync_video.getCurrentPosition()));
				popDialog();
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronous_train);
		loadDialog = UIManager.getLoadingDialog(this);
		chapterName = getIntent().getStringExtra("chapterName");
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		editor = sp.edit();
		findView();
		getData();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_sysnc_train_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mBatInfoReceiver, filter);
	}

	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				vv_sync_video.pause();
				type = 2;
				bt_sync_stop.setText("继续播放");
			}
		}
	};

	@SuppressLint("NewApi")
	private void TestFile(String videoUrl) {
		f = new File(Environment.getExternalStorageDirectory().getPath()
				+ videoUrl.substring(videoUrl.lastIndexOf("/"),
						videoUrl.length()));

		if (getIntent().getStringExtra("pause") != null
				&& getIntent().getStringExtra("pause").equals("pause")) {
			str = getSharedPreferences("config", 0).getString("currentTime",
					"0");
			if (Integer.parseInt(str) > 0) {
				rl_sync_progressbar_btn.setVisibility(View.GONE);
				changView();
				startCurrentVideo();
			}
		} else {
			if (f.exists()) {
				vv_sync_video.setVideoPath(f.getAbsolutePath());
				vv_sync_video.setMediaController(mediaController);
				isLoading = false;
				bt_sync_start.setText("开始训练");
				// type = 2;
			} else if (!f.exists()) {
				isLoading = true;
				// type = 2;
				bt_sync_start.setText("下载");
			}
		}
	}

	private void startCurrentVideo() {
		vv_sync_video.setVideoPath(f.getAbsolutePath());
		mediaController = new MediaController(this);
		vv_sync_video.setMediaController(mediaController);
		vv_sync_video.requestFocus();
		vv_sync_video.seekTo(Integer.parseInt(str));
		vv_sync_video.start();
		iv_sync_videoview_img.setVisibility(View.GONE);
		fl_sync_videoview.setVisibility(View.VISIBLE);

		upDateTime();
		vv_sync_video.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				pb_sync_progrebar.setMax(vv_sync_video.getDuration());
				tv_sync_total_time.setText(TimeUtils.MSformat(vv_sync_video
						.getDuration()));
			}
		});
		upDateProgressBar();
		vv_sync_video
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						Message msg = Message.obtain();
						msg.what = COMPLETE;
						myHandler.sendMessage(msg);
					}
				});
	}

	private void getData() {
		loadDialog.show();
		if (NetManager.isNetworkConnected(this)) {
			// rest/train/synchronization/{hardId}/{sessionId}/{typeId}/{sectionId}/{userId}
			RequestQueue queue = Volley.newRequestQueue(this);
			// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
			// 也可以继承Request自定义 (Request是泛型)
			String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
					: (String) userInfoSp.get("sessionId"));
			String userId = ((String) userInfoSp.get("userId") == null ? "0"
					: (String) userInfoSp.get("userId"));
			String url = Config.SYNCHRONOUS_TRAIN_URL
					+ "/"
					+ MyApplication.instance.getHardId()
					+ "/"
					+ sessionId
					+ "/"
					+ getSharedPreferences("config", 0)
							.getString("typeId", "0") + "/"
					+ getIntent().getStringExtra("sectionId") + "/" + userId;//
			DebugUtility.showLog("同步训练url: " + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {

						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("同步训练返回数据: " + response);
							Gson gson = new Gson();
							SynchronousTrainResult str = gson.fromJson(
									response, SynchronousTrainResult.class);
							UIManager.toggleDialog(loadDialog);
							if (str.getResultCode() == 1) {
								sectionObject = str.getSectionObject();
								typeName = str.getTypeName();
								initData();
								TestFile(sectionObject.getSectionVideoPath());
								chapterId = sectionObject.getChapterId();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
							UIManager.toggleDialog(loadDialog);
						}
					});
			queue.add(strRequest);// 加入到通讯队列中
		} else {
			showToast("网络不可用，请检查网络设置");
		}
	}

	private void initView() {
		bt_sync_start.setOnClickListener(this);
		bt_sync_stop.setOnClickListener(this);
		bt_sync_end.setOnClickListener(this);
		iv_sync_train_clock.setOnClickListener(this);
		iv_sync_train_list.setOnClickListener(this);
		iv_sync_train_history.setOnClickListener(this);
		ib_sync_return.setOnClickListener(this);
	}

	protected void changView() {
		ll_start_train.setVisibility(View.VISIBLE);
		ll_start_train_btns.setVisibility(View.VISIBLE);
		rl_sync_progressbar_btn.setVisibility(View.GONE);
		ll_sync_details.setVisibility(View.GONE);
		ll_sync_images.setVisibility(View.GONE);
	}

	protected void changBgView() {
		ll_start_train.setVisibility(View.GONE);
		ll_start_train_btns.setVisibility(View.GONE);
		rl_sync_progressbar_btn.setVisibility(View.VISIBLE);
		ll_sync_details.setVisibility(View.VISIBLE);
		ll_sync_images.setVisibility(View.VISIBLE);
	}

	private void initData() {
		ImageLoader.getInstance().displayImage(
				Config.BASE_URL + sectionObject.getSectionImage(),
				iv_sync_videoview_img);
		tv_sync_title.setText(sectionObject.getSectionName());
		tv_sync_time.setText(sectionObject.getSectionLength() + "min");
		tv_sync_count.setText(sectionObject.getSectionActionNum() + "个");
		tv_sync_heat.setText(sectionObject.getSectionCalorie() + "千卡");
		Message msg = Message.obtain();
		msg.what = VIDEO_PATH;
		msg.obj = Config.BASE_URL + sectionObject.getSectionVideoPath();
		myHandler.sendMessage(msg);
	}

	private void findView() {
		rl_sync_progressbar_btn = (RelativeLayout) findViewById(R.id.rl_sync_progressbar_btn);
		ll_sync_details = (LinearLayout) findViewById(R.id.ll_sync_details);
		ll_sync_images = (LinearLayout) findViewById(R.id.ll_sync_images);
		ll_start_train = (LinearLayout) findViewById(R.id.ll_start_train);
		ll_start_train_btns = (RelativeLayout) findViewById(R.id.ll_start_train_btns);
		vv_sync_video = (VideoView) findViewById(R.id.vv_sync_video);
		bt_sync_start = (Button) findViewById(R.id.bt_sync_start);
		bt_sync_stop = (Button) findViewById(R.id.bt_sync_stop);
		bt_sync_end = (Button) findViewById(R.id.bt_sync_end);
		tv_sync_title = (TextView) findViewById(R.id.tv_sync_title);
		tv_sync_time = (TextView) findViewById(R.id.tv_sync_time);
		tv_sync_count = (TextView) findViewById(R.id.tv_sync_count);
		tv_sync_heat = (TextView) findViewById(R.id.tv_sync_heat);
		tv_sync_total_time = (TextView) findViewById(R.id.tv_sync_total_time);
		tv_sync_current_time = (TextView) findViewById(R.id.tv_sync_current_time);
		pb_sync_progrebar = (ProgressBar) findViewById(R.id.pb_sync_progrebar);
		pb_sync_down_progressbar = (ProgressBar) findViewById(R.id.pb_sync_down_progressbar);
		iv_sync_videoview_img = (ImageView) findViewById(R.id.iv_sync_videoview_img);
		fl_sync_videoview = (FrameLayout) findViewById(R.id.fl_sync_videoview);

		iv_sync_train_clock = (ImageView) findViewById(R.id.iv_sync_train_clock);
		iv_sync_train_list = (ImageView) findViewById(R.id.iv_sync_train_list);
		iv_sync_train_history = (ImageView) findViewById(R.id.iv_sync_train_history);
		ib_sync_return = (ImageButton) findViewById(R.id.ib_sync_return);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_sync_start:
			upDateTime();
			startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault()).format(new Date());
			editor.putString("startTime", startTime);
			editor.commit();
			startTime1 = System.currentTimeMillis();
			if (sign != 1) {
				if (isLoading) {
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						isLoading = false;
						String videoPath = Config.BASE_URL
								+ sectionObject.getSectionVideoPath();
						String filePath = Environment
								.getExternalStorageDirectory().getPath()
								+ videoPath.substring(
										videoPath.lastIndexOf("/"),
										videoPath.length());
						DownLoadFileThreadTask task = new DownLoadFileThreadTask(
								videoPath, filePath, pb_sync_down_progressbar);
						new Thread(task).start();
						Message msg1 = Message.obtain();
						msg1.what = 0x123;
						myHandler.sendMessage(msg1);
					} else {
						Toast.makeText(getApplicationContext(), "SD卡不可用", 1)
								.show();
					}
				} else {
					rl_sync_progressbar_btn.setVisibility(View.GONE);
					changView();
					startVideo();
				}
			} else {
				rl_sync_progressbar_btn.setVisibility(View.GONE);
				changView();
				startMyVideo();
			}
			break;
		case R.id.bt_sync_stop:
			isComplete = false;
			if (type == 1) {
				vv_sync_video.pause();
				type = 2;
				bt_sync_stop.setText("继续播放");
			} else if (type == 2) {
				vv_sync_video.start();
				bt_sync_stop.setText("暂停");
				type = 1;
			}
			break;
		case R.id.bt_sync_end:
			vv_sync_video.pause();
			endTime2 = System.currentTimeMillis();
			popDialog();
			break;
		case R.id.iv_sync_train_clock:
			skipAcitivity(SynchronousTrainActivity2.this,
					TrainAlertListActivity.class);
			break;
		case R.id.iv_sync_train_list:
			skipAcitivity(SynchronousTrainActivity2.this,
					TrainChapterListAcitivity.class);
			break;
		case R.id.iv_sync_train_history:
			if (!MyApplication.instance.userInfoSp.getAll().isEmpty()) {
				skipAcitivity(SynchronousTrainActivity2.this,
						TrainHistoryMainActivity.class);
			} else {
				startActivity(LoginActivity.class, null);
			}
			break;
		case R.id.ib_sync_return:
			finish();
			break;
		}
	}

	private void popDialog() {
		MenuDialog.showSheet(this, new OnActionSheetSelected() {

			@Override
			public void onClick(int which) {
				switch (which) {
				case 0:
					endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
							Locale.getDefault()).format(new Date());
					double endTime1 = System.currentTimeMillis();// interval
					intervalTime = (int) Math
							.ceil((endTime1 - startTime1 - (endTime1 - endTime2)) / 1000);
					temp = ((int) Double.parseDouble(tv_sync_current_time
							.getText().toString().split(":")[0]) * 60 + (int) Double
							.parseDouble(tv_sync_current_time.getText()
									.toString().split(":")[1]));
					sectionLength = formatTime(temp);
					if (!MyApplication.instance.userInfoSp.getAll().isEmpty()) {
						skipAcitivity(SynchronousTrainActivity2.this,
								EndTrainPlanActivity.class);
					} else {
						startActivity(LoginActivity.class, null);
					}
					finish();
					break;

				case 1:
					if (isComplete) {
						myCount++;
					}
					isComplete = false;
					if (type == 2) {
						bt_sync_stop.setText("暂停");
						type = 1;
						vv_sync_video.start();
					} else if (type == 1) {
						type = 2;
						vv_sync_video.start();
					}
					break;
				default:
					break;
				}

			}
		}, null);
	}

	private void startMyVideo() {
		vv_sync_video.setVideoPath(f.getAbsolutePath());
		vv_sync_video.setMediaController(mediaController);
		vv_sync_video.requestFocus();
		vv_sync_video.start();
		iv_sync_videoview_img.setVisibility(View.GONE);
		fl_sync_videoview.setVisibility(View.VISIBLE);
		upDateTime();
		upDateProgressBar();
		vv_sync_video.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				return true;
			}
		});
		vv_sync_video.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				pb_sync_progrebar.setMax(vv_sync_video.getDuration());
				myTime++;
				tv_sync_total_time.setText(TimeUtils.MSformat(myTime));
			}
		});
		vv_sync_video
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						Message msg = Message.obtain();
						msg.what = COMPLETE;
						myHandler.sendMessage(msg);
					}
				});
	}

	private void startVideo() {
		vv_sync_video.setFocusable(true);
		vv_sync_video.setFocusableInTouchMode(true);
		vv_sync_video.requestFocus();
		vv_sync_video.start();
		iv_sync_videoview_img.setVisibility(View.GONE);
		fl_sync_videoview.setVisibility(View.VISIBLE);
		upDateTime();
		upDateProgressBar();
		vv_sync_video.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				pb_sync_progrebar.setMax(vv_sync_video.getDuration());
				tv_sync_total_time.setText(TimeUtils.MSformat(vv_sync_video
						.getDuration()));
			}
		});
		vv_sync_video
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						Message msg = Message.obtain();
						msg.what = COMPLETE;
						myHandler.sendMessage(msg);
					}
				});
	}

	private void skipAcitivity(Context context, Class clazz) {
		Intent newIntent = new Intent(context, clazz);
		newIntent.putExtra("sectionId", sectionObject.getSectionId());
		newIntent.putExtra("sectionLength", sectionLength);
		newIntent.putExtra(
				"startTime",
				getSharedPreferences("config", 0).getString(
						"startTime",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
								.getDefault()).format(new Date())));
		newIntent.putExtra("endTime", endTime);
		newIntent.putExtra("chapterId", chapterId);
		newIntent.putExtra("intervalTime", temp + "");
		newIntent.putExtra("sectionName",
				getIntent().getStringExtra("sectionName"));
		newIntent.putExtra("typeId", getIntent().getStringExtra("typeId"));
		newIntent.putExtra("trainID", getIntent().getStringExtra("trainID"));
		newIntent.putExtra("source", "syncTrain");
		startActivity(newIntent);
	}

	private void upDateProgressBar() {
		// 开始线程，更新进度条的刻度
		new Thread() {

			@Override
			public void run() {
				try {
					isPlaying = true;
					while (isPlaying) {
						current = vv_sync_video.getCurrentPosition();
						Message msg = Message.obtain();
						msg.what = CURRENT_PROGRESS;
						msg.obj = current;
						myHandler.sendMessage(msg);
						sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void upDateTime() {
		// 开始线程，更新时间
		new Thread() {

			@Override
			public void run() {
				try {
					isPlaying = true;
					while (isPlaying) {
						myTime++;
						Message msg = Message.obtain();
						msg.what = TIME_PROGRESS;
						msg.obj = myTime;
						myHandler.sendMessage(msg);
						sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private String formatTime(int it) {
		if (it < 10) {
			return "0";
		} else {
			return it + "";
		}
	}

	/**
	 * 在子线程里的任务中下载视频
	 * 
	 * @author Administrator
	 * 
	 */
	private class DownLoadFileThreadTask implements Runnable {
		private String path;// 服务器文件的路径
		private String filepath;// 本地文件的路径
		private ProgressBar pb_sync_down_progressbar;

		public DownLoadFileThreadTask(String path, String filepath,
				ProgressBar pb_sync_down_progressbar) {
			super();
			this.path = path;
			this.filepath = filepath;
			this.pb_sync_down_progressbar = pb_sync_down_progressbar;
		}

		@Override
		public void run() {
			try {
				File file = getFile(path, filepath, pb_sync_down_progressbar);
				if (file.exists()) {
					Message msg = Message.obtain();
					msg.what = 0x321;
					msg.obj = 1;
					myHandler.sendMessageDelayed(msg, 2000);
				} else {
					showToast("该视频不存在！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param path
	 *            服务器文件的路径
	 * @param filepath
	 *            本地文件的路径
	 * @return 本地文件对象
	 * @throws Exception
	 */
	public File getFile(String path, String filepath, ProgressBar pb)
			throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(20000);
		conn.setDoInput(true); // 允许输入流，即允许下载
		if (conn.getResponseCode() == 200) {
			int total = conn.getContentLength();
			pb.setMax(total);
			InputStream is = conn.getInputStream();
			File file = new File(filepath);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			int process = 0;
			// 如果没有读到文件的末尾，循环读取
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				process += len;
				pb.setProgress(process);
			}
			fos.flush();
			fos.close();
			is.close();
			return file;

		}
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}