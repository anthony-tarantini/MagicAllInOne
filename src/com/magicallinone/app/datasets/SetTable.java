package com.magicallinone.app.datasets;

import android.content.ContentValues;

import com.magicallinone.app.models.Set;

public class SetTable {
	public static class Columns{
		public static final String SET_ID = "_id";
		public static final String NAME = "name";
		public static final String CODE = "code";
		public static final String RELEASE_DATE = "release_date";
		public static final String BORDER = "border";
		public static final String TYPE = "type";
		public static final String BLOCK = "block";
	}
	
	public static ContentValues getContentValues(Set set){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Columns.NAME, set.name.replace("'", ""));
		contentValues.put(Columns.CODE, set.code);
		contentValues.put(Columns.RELEASE_DATE, set.releaseDate);
		contentValues.put(Columns.BORDER, set.border);
		contentValues.put(Columns.TYPE, set.type);
		contentValues.put(Columns.BLOCK, set.block);
		return contentValues;
	}
}
