package com.ak.qmyd.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import com.ak.qmyd.bean.AlertDetailEntity;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

/** 
 * @author JGB
 * @date 2015-5-20 下午3:12:41
 */
public class ListViewUtils {
	public static  String SceneList2String(List<AlertDetailEntity> SceneList)
            throws IOException {
      // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      // 然后将得到的字符数据装载到ObjectOutputStream
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(
              byteArrayOutputStream);
      // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
      objectOutputStream.writeObject(SceneList);
      // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
      String SceneListString = new String(Base64.encode(
              byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
      // 关闭objectOutputStream
      objectOutputStream.close();
      return SceneListString;
}
	public static List<AlertDetailEntity> String2SceneList(String SceneListString)
	          throws StreamCorruptedException, IOException,
	          ClassNotFoundException {
	      byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
	              Base64.DEFAULT);
	      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
	              mobileBytes);
	      ObjectInputStream objectInputStream = new ObjectInputStream(
	              byteArrayInputStream);
	      List<AlertDetailEntity> SceneList = (List<AlertDetailEntity>) objectInputStream
	              .readObject();
	      objectInputStream.close();
	      return SceneList;
	  }
	
	
	/**
	 * 序列化对象
	 * 
	 * @param person
	 * @return
	 * @throws IOException
	 */
	public static String serialize(List<AlertDetailEntity> entity) throws IOException {
		long startTime = System.currentTimeMillis();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(entity);
		String serStr = byteArrayOutputStream.toString("ISO-8859-1");
		serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		objectOutputStream.close();
		byteArrayOutputStream.close();
		Log.d("serial", "serialize str =" + serStr);
		long endTime = System.currentTimeMillis();
		Log.d("serial", "序列化耗时为:" + (endTime - startTime));
		return serStr;
	}
	
	/**
	 * 反序列化对象
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<AlertDetailEntity> deSerialization(String str) throws IOException,
			ClassNotFoundException {
		long startTime = System.currentTimeMillis();
		String redStr = java.net.URLDecoder.decode(str, "UTF-8");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				redStr.getBytes("ISO-8859-1"));
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		List<AlertDetailEntity> entity = (List<AlertDetailEntity>) objectInputStream.readObject();
		objectInputStream.close();
		byteArrayInputStream.close();
		long endTime = System.currentTimeMillis();
		Log.d("serial", "反序列化耗时为:" + (endTime - startTime));
		return entity;
	}


}
