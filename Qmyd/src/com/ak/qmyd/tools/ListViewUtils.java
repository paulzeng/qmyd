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
 * @date 2015-5-20 ����3:12:41
 */
public class ListViewUtils {
	public static  String SceneList2String(List<AlertDetailEntity> SceneList)
            throws IOException {
      // ʵ����һ��ByteArrayOutputStream��������װ��ѹ������ֽ��ļ���
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      // Ȼ�󽫵õ����ַ�����װ�ص�ObjectOutputStream
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(
              byteArrayOutputStream);
      // writeObject ��������д���ض���Ķ����״̬���Ա���Ӧ�� readObject �������Ի�ԭ��
      objectOutputStream.writeObject(SceneList);
      // �����Base64.encode���ֽ��ļ�ת����Base64���뱣����String��
      String SceneListString = new String(Base64.encode(
              byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
      // �ر�objectOutputStream
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
	 * ���л�����
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
		Log.d("serial", "���л���ʱΪ:" + (endTime - startTime));
		return serStr;
	}
	
	/**
	 * �����л�����
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
		Log.d("serial", "�����л���ʱΪ:" + (endTime - startTime));
		return entity;
	}


}
