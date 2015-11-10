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
	 * ����һ����¼
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
	 * ����id��ȡ��Ӧnote
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
	 * ����id������Ӧnote update ���� set �ֶ���=ֵ where �����Ӿ䡣�磺update person set name=�����ǡ�
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
	 * ����һ��note����
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
	 * ɾ��һ��note����
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
	 * ��ȡ����note��Ϣ
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
		DebugUtility.showLog("��ѯ��������:"+notes.toString());
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
	 * ����һ����Ϣ
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
	 * ����һ����Ϣ
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
		DebugUtility.showLog("������Ϣ�ɹ�");
		sqliteDatabase.close();
	}

	/**
	 * ����һ���б����Ϣ
	 * 
	 * @param messageList
	 */
	public void inseartMessageList(String userid, List<PushInfo> pushinfoList) {
		SQLiteDatabase sqliteDatabase = helper.getWritableDatabase();
		for (PushInfo message : pushinfoList) {
			String info = message.toString().replace("\u003d", "=");
			// ContentValues cv = new
			// ContentValues();//ʵ����һ��ContentValues����װ�ش����������cv.put("username","Jack Johnson");//����û���
			// cv.put("password","iLovePopMusic"); //�������
			// sqliteDatabase.insert("user",null,cv);//ִ�в������
			sqliteDatabase.execSQL(
					"insert into t_message (_userid,_pushinfo) values (?,?)",
					new Object[] { userid, info });
		}
		sqliteDatabase.close();
	}

	/**
	 * ��ȡ������Ϣ
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

	// ��ѯ���ݿ��Ƿ���δ������Ϣ
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
