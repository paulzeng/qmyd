package com.ak.qmyd.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ak.qmyd.bean.NoteBean;
import com.ak.qmyd.bean.PushInfo;
import com.ak.qmyd.tools.DebugUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DBManager {
	private QmydDBHandler helper;
	private static DBManager mDBManager;

	public DBManager(Context context) {
		this.helper = new QmydDBHandler(context);
	}

	public static synchronized DBManager getInstance(Context cxt) {
		if (mDBManager == null) {
			mDBManager = new DBManager(cxt);
		}
		return mDBManager;
	}

	/**
	 * 插入一条记录
	 * 
	 * @param note
	 */
	public void inseartNote(NoteBean note) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		if (sqliteDatabase == null) {
			return;
		}
		sqliteDatabase
				.execSQL(
						"insert into t_alter (_time,_title,_day,_way,_count,_week,_timetype,_index,_isstart) values (?,?,?,?,?,?,?,?,?)",
						new Object[] { note.getTime(), note.getTitle(),
								note.getDay(), note.getWay(), note.getCount(),
								note.getWeek(), note.getTimeType(),
								note.getIndex(), note.getIsstart() });
		sqliteDatabase.close();
	}

	/**
	 * 根据id获取相应note
	 * 
	 * @param id
	 * @return
	 */
	public NoteBean getNoteByID(int id) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		NoteBean note = null;

		if (sqliteDatabase == null) {
			return null;
		}

		Cursor cursor = sqliteDatabase.rawQuery(
				"select * from t_alter where _id = " + id, null);

		while (cursor != null && cursor.moveToNext()) {
			String _title = cursor.getString(cursor.getColumnIndex("_title"));
			String _time = cursor.getString(cursor.getColumnIndex("_time"));
			String _day = cursor.getString(cursor.getColumnIndex("_day"));
			String _way = cursor.getString(cursor.getColumnIndex("_way"));
			String _count = cursor.getString(cursor.getColumnIndex("_count"));
			String _week = cursor.getString(cursor.getColumnIndex("_week"));
			String _timetype = cursor.getString(cursor
					.getColumnIndex("_timetype"));
			String _index = cursor.getString(cursor.getColumnIndex("_index"));
			String _isstart = cursor.getString(cursor
					.getColumnIndex("_isstart"));
			note = new NoteBean(id, _time, _title, _day, _way, _count, _week,
					_timetype, _index, _isstart);
		}
		try {
			if (cursor != null) {
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqliteDatabase.close();
		return note;
	}

	/**
	 * 根据id更新相应note update 表名 set 字段名=值 where 条件子句。如：update person set name=‘传智‘
	 * where id=10
	 */
	public void upDateNote(int id, NoteBean note) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		if (sqliteDatabase == null) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put("_time", note.getTime());
		values.put("_title", note.getTitle());
		values.put("_day", note.getDay());
		values.put("_way", note.getWay());
		values.put("_count", note.getCount());
		values.put("_week", note.getWeek());
		values.put("_timetype", note.getTimeType());
		values.put("_index", note.getIndex());
		values.put("_isstart", note.getIsstart());
		sqliteDatabase.update("t_alter", values, "_id = ?",
				new String[] { String.valueOf(id) });
		sqliteDatabase.close();
	}

	/**
	 * 更新一条note数据
	 * 
	 * @param id
	 * @param isstart
	 */
	public void upDateNoteById(int id, String isstart) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		if (sqliteDatabase == null) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put("_isstart", isstart);
		sqliteDatabase.update("t_alter", values, "_id = ?",
				new String[] { String.valueOf(id) });
		sqliteDatabase.close();
	}

	/**
	 * 删除一条note数据
	 */
	public void deleteNoteById(int id) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		if (sqliteDatabase == null) {
			return;
		}
		ContentValues cv = new ContentValues();
		String[] args = { String.valueOf(id) };
		sqliteDatabase.delete("t_alter", "_id = ?", args);
		sqliteDatabase.close();
	}

	/**
	 * 获取所有note信息
	 * 
	 * @return
	 */
	public List<NoteBean> getNoteList() {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		if (sqliteDatabase == null) {
			return null;
		}
		List<NoteBean> notes = new ArrayList<NoteBean>();
		Cursor cursor = sqliteDatabase.query("t_alter", null, null, null, null,
				null, null, null);
		while (cursor != null && cursor.moveToNext()) {
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String _title = cursor.getString(cursor.getColumnIndex("_title"));
			String _time = cursor.getString(cursor.getColumnIndex("_time"));
			String _day = cursor.getString(cursor.getColumnIndex("_day"));
			String _way = cursor.getString(cursor.getColumnIndex("_way"));
			String _count = cursor.getString(cursor.getColumnIndex("_count"));
			String _week = cursor.getString(cursor.getColumnIndex("_week"));
			String _timetype = cursor.getString(cursor
					.getColumnIndex("_timetype"));
			String _index = cursor.getString(cursor.getColumnIndex("_index"));
			String _isstart = cursor.getString(cursor
					.getColumnIndex("_isstart"));
			NoteBean note = new NoteBean(_id, _time, _title, _day, _way,
					_count, _week, _timetype, _index, _isstart);
			notes.add(note);
		}
		DebugUtility.showLog("查询所有数据:"+notes.toString());
		try {
			if (cursor != null) {
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return notes;
	}

	/**
	 * 插入一条消息
	 * 
	 * @param message
	 */
	public void inseartMessage(String userid, PushInfo message) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		if (sqliteDatabase == null) {
			return;
		}
		sqliteDatabase.execSQL(
				"insert into t_message (_pushinfo) values (?,?)", new Object[] {
						userid, message.toString() });
		sqliteDatabase.close();
	}

	/**
	 * 更新一条消息
	 * 
	 * @param message
	 */
	public void updateMessage(int _id, PushInfo message) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		if (sqliteDatabase == null) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put("_pushinfo", message.toString());
		sqliteDatabase.update("t_message", values, "_id = ?",
				new String[] { String.valueOf(_id) });
		DebugUtility.showLog("更新消息成功");
		sqliteDatabase.close();
	}

	/**
	 * 增加一个列表的消息
	 * 
	 * @param messageList
	 */
	public void inseartMessageList(String userid, List<PushInfo> pushinfoList) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		for (PushInfo message : pushinfoList) {
			String info = message.toString().replace("\u003d", "=");
			// ContentValues cv = new
			// ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
			// cv.put("password","iLovePopMusic"); //添加密码
			// sqliteDatabase.insert("user",null,cv);//执行插入操作
			sqliteDatabase.execSQL(
					"insert into t_message (_userid,_pushinfo) values (?,?)",
					new Object[] { userid, info });
		}
		sqliteDatabase.close();
	}

	/**
	 * 获取所有信息
	 * 
	 * @return
	 */
	public List<PushInfo> getMessageList(String userid) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		if (sqliteDatabase == null) {
			return null;
		}
		List<PushInfo> _pushinfos = new ArrayList<PushInfo>();
		// Cursor cursor = sqliteDatabase.query("t_message", null, null, null,
		// null, null, null, null);
		Cursor cursor = sqliteDatabase.rawQuery(
				"select * from t_message where _userid=?",
				new String[] { userid });
		while (cursor != null && cursor.moveToNext()) {
			String _pushinfo = cursor.getString(cursor
					.getColumnIndex("_pushinfo"));
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			PushInfo pushinfo = gson.fromJson(_pushinfo, PushInfo.class);
			pushinfo.set_id(_id);
			_pushinfos.add(pushinfo);
		}
		try {
			if (cursor != null) {
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqliteDatabase.close();
		return _pushinfos;
	}

	// 查询数据库是否有未读的消息
	public boolean hasNotRead(String userid) {
		List<PushInfo> messages = getMessageList(userid);
		for (PushInfo message : messages) {
			if (message.getStatus().equals("1")) {
				return true;
			}
		}
		return false;
	}
}
