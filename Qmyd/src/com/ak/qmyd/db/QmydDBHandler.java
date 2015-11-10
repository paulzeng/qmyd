package com.ak.qmyd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QmydDBHandler extends SQLiteOpenHelper {
	private static final String name = "db_qmyd";  
	private static final int version = 1; // Êý¾Ý¿â°æ±¾ 
	public QmydDBHandler(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE IF NOT EXISTS t_alter (_id integer primary key autoincrement,_time varchar(20),_title varchar(20),_day varchar(20),_way varchar(20),_count varchar(20),_week varchar(20),_timetype varchar(20),_index varchar(20),_isstart varchar(20))");
			db.execSQL("CREATE TABLE IF NOT EXISTS t_message (_id integer primary key autoincrement,_userid varchar(20),_pushinfo varchar(20))");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	

}
