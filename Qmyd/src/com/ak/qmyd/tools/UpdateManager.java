package com.ak.qmyd.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.view.NumberProgressBar;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager {
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD_FINISH = 2;
	private String mSavePath;
	private String mAppName;
	private int progress;
	private boolean cancelUpdate = false;

	private Context mContext;
	private NumberProgressBar mNumberProgressBar;
	private TextView tv_pro;
	private AlertDialog myDialog = null;
	private AlertDialog downDialog = null;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD:
				String pro = msg.obj.toString();
				tv_pro.setText("正在更新" + pro);
				mNumberProgressBar.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context, String appName) {
		this.mContext = context;
		mAppName = appName;
	}

	public boolean checkUpdate(int serviceCode) {
		if (isUpdate(serviceCode)) {
			showNoticeDialog("");
			return true;
		} else {
			Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	private boolean isUpdate(int serviceCode) {
		int versionCode = getVersionCode(mContext);
		if (serviceCode > versionCode) {
			return true;
		}
		return false;
	}

	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			String pkg = context.getPackageName();
			versionCode = context.getPackageManager().getPackageInfo(pkg, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public void showNoticeDialog(String desc) {
		Dialog dialog = UIManager.getCommWarnDialog(mContext, desc,
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						switch (view.getId()) {
						case R.id.warn_sure_bt:
							showDownloadDialog();
							break;
						case R.id.warn_cancle_bt:
							break;

						default:
							break;
						}
					}
				});
		dialog.show();
		dialog.setCancelable(false);
	}

	private void showDownloadDialog() {
		downDialog = new AlertDialog.Builder(mContext).create();
		downDialog.show();
		downDialog.getWindow().setContentView(R.layout.layout_download_dialog);
		tv_pro = (TextView) downDialog.getWindow().findViewById(
				R.id.download_content_tv);
		downDialog.getWindow().findViewById(R.id.download_cancle_bt)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						downDialog.dismiss();
						cancelUpdate = true;
					}
				});
		downDialog.setCancelable(false);
		mNumberProgressBar = (NumberProgressBar) (downDialog.getWindow()
				.findViewById(R.id.numberbar));
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					// String sdpath = Environment.getExternalStorageDirectory()
					// + "/";
					// mSavePath = sdpath + "download";
					mSavePath = "/sdcard/download";
					// String str = "http://192.168.10.50:8080/apk/qmyd.apk";
					URL url = new URL(Config.BASE_URL
							+ MyApplication.downloadUrl);
					// URL url = new URL(str);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "qmyd.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						Message msg = new Message();
						msg.obj = progress + "%";
						msg.what = DOWNLOAD;
						// 更新进度
						mHandler.sendMessage(msg);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				// ToastManager.show(mContext, "文件未找到");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			downDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, "qmyd.apk");
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
