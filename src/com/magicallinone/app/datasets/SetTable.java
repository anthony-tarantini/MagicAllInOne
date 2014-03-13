package com.magicallinone.app.datasets;

import java.util.Map;

import android.content.ContentValues;

import com.magicallinone.app.models.Set;
import com.magicallinone.app.providers.DatabaseTable;

public class SetTable extends DatabaseTable {
	
	public static final String TABLE_NAME = "set_table";
	
	public static class Columns extends DatabaseTable.Columns {
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

	@Override
	protected Map<String, String> getColumnTypes() {
		Map<String, String> columnTypes = super.getColumnTypes();
		columnTypes.put(Columns.NAME, "TEXT");
		columnTypes.put(Columns.CODE, "TEXT");
		columnTypes.put(Columns.RELEASE_DATE, "TEXT");
		columnTypes.put(Columns.BORDER, "TEXT");
		columnTypes.put(Columns.TYPE, "TEXT");
		columnTypes.put(Columns.BLOCK, "TEXT");
		return columnTypes;
	}
	
	@Override
	protected String getConstraint() {
		return "UNIQUE (" + Columns.CODE + ") ON CONFLICT REPLACE";
	}

	@Override
	public String getName() {
		return TABLE_NAME;
	}
}
