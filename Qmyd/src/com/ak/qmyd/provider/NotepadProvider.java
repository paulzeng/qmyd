package com.ak.qmyd.provider;

import java.util.HashMap;

import com.ak.qmyd.bean.Notepad;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class NotepadProvider extends ContentProvider  {

public static final int NOTE_DIR=0;
	
	public static final int NOTE_ITEM=1;
	
	private static UriMatcher uriMatcher;
	
	private NoteDatabaseHelper mDbhelper;
	
	private static HashMap<String, String> sProjectionMap;
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Notepad.AUTHORITY, Notepad.TABLE_NOTE, NOTE_DIR);
		uriMatcher.addURI(Notepad.AUTHORITY, Notepad.TABLE_NOTE+"/#", NOTE_ITEM);
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(Notepad.NOTE_ID, Notepad.NOTE_ID);
		sProjectionMap.put(Notepad.NOTE_TIME, Notepad.NOTE_TIME);
		sProjectionMap.put(Notepad.NOTE_TITLE, Notepad.NOTE_TITLE);
		sProjectionMap.put(Notepad.NOTE_DAY, Notepad.NOTE_DAY);
		sProjectionMap.put(Notepad.NOTE_WAY, Notepad.NOTE_WAY);
		sProjectionMap.put(Notepad.NOTE_COUNT, Notepad.NOTE_COUNT);
	}
	
	public static final String CREATE_NOTE="create table "
			+ Notepad.TABLE_NOTE+"( "
			+ Notepad.NOTE_ID 
			+ " integer primary key autoincrement, "
			+ Notepad.NOTE_TIME +" text not null, "
			+ Notepad.NOTE_TITLE +" text not null, "
			+ Notepad.NOTE_DAY +" text not null, "
			+ Notepad.NOTE_WAY +" text not null, "
			+ Notepad.NOTE_COUNT + " text not null);";
	
	private static class NoteDatabaseHelper extends SQLiteOpenHelper{

		public NoteDatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, Notepad.DATABASE_NAME, null, Notepad.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_NOTE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists "+Notepad.TABLE_NOTE);
			onCreate(db);
		}
	}
	

	@Override
	public boolean onCreate() {
		mDbhelper = new NoteDatabaseHelper(getContext(),
				Notepad.DATABASE_NAME, null,
				Notepad.DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case NOTE_DIR:
			// 查询所有便签记录
			qb.setTables(Notepad.TABLE_NOTE);
			qb.setProjectionMap(sProjectionMap);
			break;
		case NOTE_ITEM:
			// 根据id查询便签记录
			qb.setTables(Notepad.TABLE_NOTE);
			qb.setProjectionMap(sProjectionMap);
			qb.appendWhere(Notepad.NOTE_ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("error: is unknow Uri" +uri);
		}
		SQLiteDatabase db = mDbhelper.getReadableDatabase();
		Cursor cursor =qb.query(db, projection, selection, selectionArgs, null, null, null);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case NOTE_DIR:
			return Notepad.CONTENT_TYPE;
		case NOTE_ITEM:
			return Notepad.CONTENT_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		//添加数据
		SQLiteDatabase db = mDbhelper.getWritableDatabase();
		long noteId =db.insert(Notepad.TABLE_NOTE, null, values);
		Uri uriReturn = Uri.parse("content://"+Notepad.AUTHORITY+"/alert/"+noteId);
		return uriReturn;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db =mDbhelper.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case NOTE_DIR:
			// 根据匹配的Uri返回码来执行相应删除操作
			count =db.delete(Notepad.TABLE_NOTE, selection, selectionArgs);
			break;
		case NOTE_ITEM:
			// 只删除对应id值
			String note_id = uri.getPathSegments().get(1);
			count = db.delete(Notepad.TABLE_NOTE, Notepad.NOTE_ID + "=" + note_id 
					+ (!TextUtils.isEmpty(selection) ? "AND(" + selection+')':""), selectionArgs);
			break;

		default:
			throw new  IllegalArgumentException("error: is unknow Uri "+uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mDbhelper.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case NOTE_DIR:
			count = db.update(Notepad.TABLE_NOTE, values, selection, selectionArgs);
			break;
		case NOTE_ITEM:
			String nodeId =uri.getPathSegments().get(1);
			count = db.update(Notepad.TABLE_NOTE, values, Notepad.NOTE_ID + "=" + nodeId +
					(!TextUtils.isEmpty(selection)?"AND(" + selection+')':""), selectionArgs);
			break;

		default:
			throw new IllegalAccessError("error: is unknow Uri " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
}
