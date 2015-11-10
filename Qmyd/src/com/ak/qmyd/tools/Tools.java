package com.ak.qmyd.tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.os.Environment;
import android.text.GetChars;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class Tools {

	public static String stringFilter(String str) throws PatternSyntaxException {
		// ֻ������ĸ�����ֺͺ��ּ������ַ�
		String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5/\\.,��������������������������������������������������������������һ��;'����������!������@��!~`#$%^&*()+=-��_\\|:*?<>|\"\n\t]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	/**
     * �������л�ȡͼƬ��Ϣ����������ʽ����
     * @return
     * @throws IOException 
     */
    public static InputStream getImageViewInputStream(String path) throws IOException{
        
        InputStream inputStream = null;
            URL url = new URL(path);
            if(url != null){
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setConnectTimeout(3000); //�������ӳ�ʱ��ʱ��
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                //������Ӧ�Ĵ���
                int response_code = httpURLConnection.getResponseCode();
                if(response_code == 200){
                    inputStream = httpURLConnection.getInputStream();
                }
            }
        
        return inputStream;
    }
	
    public static byte[] getImageViewArray(String path){
        byte[] data = null;
        InputStream inputStream = null;
        
        //����Ҫ�ر��������ֱ�� д�뵽�ڴ���
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            URL url = new URL(path);
            if(url != null){
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setConnectTimeout(3000); //�������ӳ�ʱ��ʱ��
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                //������Ӧ�Ĵ���
                int response_code = httpURLConnection.getResponseCode();
                
                int len = 0;
                byte[] b_data = new byte[1024];
                if(response_code == 200){
                    inputStream = httpURLConnection.getInputStream();
                    while((len = inputStream.read(b_data)) != -1) {
                        outputStream.write(b_data, 0, len);
                    }
                    data = outputStream.toByteArray();
                }
            }
        } catch (Exception e) {
        }finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return data;
        
    }
    
	/**
	 * ��ȡ��Ļ�Ŀ��
	 * */
	public static int getWidth(Context context) {

		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		return width;

	}

	/**
	 * @param ͼƬ����
	 * @param bitmap
	 *            ����
	 * @param w
	 *            Ҫ���ŵĿ��
	 * @param h
	 *            Ҫ���ŵĸ߶�
	 * @return newBmp �� Bitmap����
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newBmp;
	}

	/**
	 * @param ���ֽ�����ת��ΪImageView�ɵ��õ�Bitmap����
	 * @param bytes
	 * @param opts
	 * @return Bitmap
	 */
	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	/**
	 * ��BitmapתByte
	 * 
	 * @Author
	 * @EditTime
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	

	/**
	 * ���ֽ����鱣��Ϊһ���ļ�
	 * 
	 * @Author JGB
	 * @EditTime
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * ���������л������
	 */
	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();

	}

	/**
	 * ��ȡ��Ļ�ĸ߶�
	 */
	public static int getHeight(Context context) {

		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;
		return height;

	}

	/**
	 * dipתΪ px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px תΪ dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ƴ���ַ�get�����URL
	 * */
	public static String joinUrlByParam(String... param) {
		String result = "";
		int lenght = param.length;
		for (int i = 0; i < lenght; i++) {
			result = result + "/";
			result = result + param[i];
		}
		return result;
	}

	/**
	 * �ж�Ԥ��ʱ����Ƿ�����
	 * */

	public static boolean isTimeLink(String... param) {

		int leng = param.length;
		String[][] timeArray = new String[param.length][2];

		for (int i = 0; i < leng; i++) {
			String[] time = param[i].split("-");
			timeArray[i][0] = time[0].trim().substring(0, 2);
			timeArray[i][1] = time[1].trim().substring(0, 2);
		}

		String[][] newTimeArray = sortingList(timeArray);

		for (int i = 0; i < newTimeArray.length - 1; i++) {
			if (!(newTimeArray[i][1].equals(newTimeArray[i + 1][0]))) {
				return false;
			}
		}

		return true;
	}

	private static String[][] sortingList(String[][] timeArray) {

		for (int i = 0; i < timeArray.length - 1; i++) {

			int m = Integer.parseInt(timeArray[i][0]);

			for (int k = i + 1; k < timeArray.length; k++) {

				int n = Integer.parseInt(timeArray[k][0]);
				String str;
				if (m > n) {
					str = timeArray[i][0];
					timeArray[i][0] = timeArray[k][0];
					timeArray[k][0] = str;
					m = n;

					str = timeArray[i][1];
					timeArray[i][1] = timeArray[k][1];
					timeArray[k][1] = str;

				}

			}

		}
		return timeArray;
	}

	/**
	 * �ж��Ƿ���GPS
	 * */

	public static boolean gpsIsOpen(Context context) {

		LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean network = manager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}

	/**
	 * listviewΪ��ʱ����ʾ
	 * */
	public static void setEmptyView(ListView listView, Context context) {
		TextView emptyView = new TextView(context);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		emptyView.setText("������Ϣ");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
	}

	/**
	 * ��֤�ֻ���ʽ
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
		 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
		 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��4��5��7��8������λ�õĿ���Ϊ0-9
		 */
		String telRegex = "[1][34578]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public static void saveFile(Bitmap bm, String fileName, String path)
			throws IOException {
		String subForder = Environment.getExternalStorageDirectory().getPath()
				+ path;
		File foder = new File(subForder);
		if (!foder.exists()) {
			foder.mkdirs();
		}
		File myCaptureFile = new File(subForder, fileName);
		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	/**
	 * 
	 * �ж�һ���Ƿ�������
	 */
	public static boolean isLeapYear(int year) {
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return true;
		}
		return false;
	}

	/**
	 * ��̬����ListView�ĸ߶�
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * ��̬����ExpandableListView�ĸ߶�
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(
			ExpandableListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
}
