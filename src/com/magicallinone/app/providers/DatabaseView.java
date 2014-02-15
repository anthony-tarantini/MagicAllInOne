package com.magicallinone.app.providers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class DatabaseView extends DatabaseSet {
	private static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS %s AS SELECT * FROM ( %s );";
	private static final String DROP_VIEW = "DROP VIEW IF EXISTS %s;";

	abstract protected String getSelectString();

	@Override
	public void onCreate(final SQLiteDatabase db) {		
		final String query = String.format(CREATE_VIEW, getName(), getSelectString());
		db.execSQL(query);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db) {
		final String query = String.format(DROP_VIEW, getName());
		db.execSQL(query);
		onCreate(db);
	}

	@Override
	public void drop(final SQLiteDatabase db) {
		final String query = String.format(DROP_VIEW, getName());
		db.execSQL(query);
	}
	@Override
	public int delete(SQLiteDatabase database, Uri uri, String selection,
			String[] selectionArgs) {
		return 0;
	}

	@Override
	public int delete(SQLiteDatabase database, String selection,
			String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(SQLiteDatabase database, Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(SQLiteDatabase database, ContentValues values,
			String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int bulkInsert(SQLiteDatabase database, Uri uri,
			ContentValues[] values) {
		return 0;
	}

	@Override
	public int bulkInsert(SQLiteDatabase database, ContentValues[] values) {
		return 0;
	}

	@Override
	public long insert(SQLiteDatabase database, Uri uri, ContentValues values) {
		return 0;
	}

	@Override
	public long insert(SQLiteDatabase database, ContentValues values) {
		return 0;
	}
	
	
}
