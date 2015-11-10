package com.ak.qmyd.bean;

import android.net.Uri;

public class Notepad {

	public static final String[] projection={
		Notepad.NOTE_ID,
		Notepad.NOTE_TIME,
		Notepad.NOTE_DAY,
		Notepad.NOTE_WAY,
		Notepad.NOTE_COUNT
	};

	/**���ݿ���*/
	public static final String DATABASE_NAME="alert.db";
	/**�汾��*/
	public static final int DATABASE_VERSION=1;
	/**��ǩ��*/
	public static final String TABLE_NOTE="alert";
	
	// columns
	public static final String NOTE_ID="_id";  //ID
	public static final String NOTE_TIME = "time";
	public static final String NOTE_TITLE = "title";
	public static final String NOTE_DAY = "day";
	public static final String NOTE_WAY = "way";
	public static final String NOTE_COUNT = "count";
//	public static final String NOTE_CONTENT="content"; // ��ǩ����
//	public static final String NOTE_CREATE_DATE="createtime"; //����ʱ��
	
	/**��Ȩ����*/
	public static final String AUTHORITY="com.ak.qmyd.provider";
	/**����CONTENT_URI*/
	public static final Uri CONTENT_URI =Uri.parse("content://" + AUTHORITY+"/alert");
	/**������������*/
	public static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd.com.ak.qmyd.provider.alert";
	public static final String CONTENT_ITEM_TYPE="vnd.android.cursor.item/vnd.com.ak.qmyd.provider.alert";
}
