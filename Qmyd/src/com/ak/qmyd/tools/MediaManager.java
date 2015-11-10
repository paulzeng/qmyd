package com.ak.qmyd.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.ak.qmyd.R;
import com.ak.qmyd.config.Config;

public class MediaManager {
	private static Map<String, String> FORMAT_TO_CONTENTTYPE = new HashMap<String, String>();
	public static final String AUDIO = "audio";
	public static final String VIDEO = "video";
	public static final String PHOTO = "photo";

	static {
		FORMAT_TO_CONTENTTYPE.put("mp3", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("mid", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("midi", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("asf", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("wm", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("wma", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("wmd", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("amr", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("wav", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("3gpp", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("mod", AUDIO);
		FORMAT_TO_CONTENTTYPE.put("mpc", AUDIO);

		FORMAT_TO_CONTENTTYPE.put("fla", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("flv", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("wav", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("wmv", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("avi", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("rm", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("rmvb", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("3gp", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("mp4", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("mov", VIDEO);

		// flash
		FORMAT_TO_CONTENTTYPE.put("swf", VIDEO);
		FORMAT_TO_CONTENTTYPE.put("null", VIDEO);

		FORMAT_TO_CONTENTTYPE.put("jpg", PHOTO);
		FORMAT_TO_CONTENTTYPE.put("jpeg", PHOTO);
		FORMAT_TO_CONTENTTYPE.put("png", PHOTO);
		FORMAT_TO_CONTENTTYPE.put("bmp", PHOTO);
		FORMAT_TO_CONTENTTYPE.put("gif", PHOTO);
	}

	public static final int REQUEST_PHOTO_CROP = 0;
	public static final int REQUEST_PHOTO_CROP_RESULT = 1;

	public static final String SD_STORAGE_DIR_NAME = "qmyd";
	public static final String SAVE_PHONE_NAME_TEMP = "qmyd_camera_";

	public static final String SAVE_PHONE_NAME_CROP = "qmyd_crop_";

	private static Uri cameraPhotoUri;

	public static File cropPhotoFile;

	public static void getPhotoFromCamera(Activity activity) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			ToastManager.show(activity,
					activity.getResources().getString(R.string.toast_no_sd));
			return;
		}
		String savePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File savedir = new File(savePath, SD_STORAGE_DIR_NAME);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		File out = new File(savedir.getAbsolutePath(), SAVE_PHONE_NAME_TEMP
				+ timeStamp);
		if (out.exists()) {
			out.delete();
		}
		cameraPhotoUri = Uri.fromFile(out);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri);
		activity.startActivityForResult(intent, REQUEST_PHOTO_CROP);
	}

	public static void getPhotoFromAlbum(Activity activity) {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			activity.startActivityForResult(Intent.createChooser(intent,
					activity.getResources().getString(R.string.choose_phone)),
					REQUEST_PHOTO_CROP);
		} catch (Exception e) {
			ToastManager.show(activity,
					activity.getResources().getString(R.string.toast_no_album));
		}
	}

	public static void onActivityResult(final Activity activity,
			final Handler handler, final int requestCode, int resultCode,
			final Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			ToastManager.show(
					activity,
					activity.getResources().getString(
							R.string.toast_photo_empty));
			return;
		}

		if (requestCode == REQUEST_PHOTO_CROP) {
			Uri cropUri = null;
			if (data == null) {
				cropUri = cameraPhotoUri;
			} else {
				cropUri = data.getData();
				if (cropUri == null) {
					ToastManager.show(activity, activity.getResources()
							.getString(R.string.toast_photo_empty));
					return;
				}
				if (!isPhotoFormat(cropUri.toString())) {
					ToastManager.show(activity, activity.getResources()
							.getString(R.string.toast_un_image));
					return;
				}
			}
			cropPhoto(cropUri, activity);
		} else {
			Message msg = handler.obtainMessage(Config.SHOW_PHOTO,
					cropPhotoFile);
			handler.sendMessage(msg);

		}

	}

	public static boolean isPhotoFormat(String uri) {
		boolean flag = true;

		if (uri.contains("file")) {
			String attFormat = uri.substring(uri.lastIndexOf('.') + 1);
			flag = PHOTO.equals(getContentType(attFormat));
		}
		return flag;
	}

	public static void cropPhoto(Uri uri, Activity activity) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		int wh = UIManager.dpToPx(activity, (int) activity.getResources()
				.getDimension(R.dimen.up_head_size));
		intent.putExtra("outputX", wh);
		intent.putExtra("outputY", wh);

		intent.putExtra("return-data", false);

		getCropPhotoFile();

		Uri cropPhotoUri = Uri.fromFile(cropPhotoFile);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, cropPhotoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		intent.putExtra("circleCrop", true);

		activity.startActivityForResult(intent, REQUEST_PHOTO_CROP_RESULT);
	}

	private static void getCropPhotoFile() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String savePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			File savedir = new File(savePath, SD_STORAGE_DIR_NAME);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());

			cropPhotoFile = new File(savedir.getAbsolutePath(),
					SAVE_PHONE_NAME_CROP + timeStamp + ".jpg");
		}

	}

	public static String getContentType(String attFormat) {
		String contentType = FORMAT_TO_CONTENTTYPE.get("null");

		if (attFormat != null) {
			contentType = (String) FORMAT_TO_CONTENTTYPE.get(attFormat
					.toLowerCase());
		}
		return contentType;
	}

	public static void onActivityTypeResult(final Activity activity,
			final Handler handler, final int requestCode, int resultCode,
			final Intent data) {

		if (resultCode != 100) {
			ToastManager.show(activity, "请选择圈子类型");
			return;
		}else{
			Message msg = handler.obtainMessage(100,data);
			handler.sendMessage(msg);
		}
	}
}