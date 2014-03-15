package com.magicallinone.app.datasets;

import android.content.ContentValues;

import com.magicallinone.app.models.Deck;
import com.magicallinone.app.providers.DatabaseTable;

import java.util.Map;

public class DeckTable extends DatabaseTable {
	
	public static final String TABLE_NAME = "deck_table";
	
	public static class Columns extends DatabaseTable.Columns{
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String FORMAT = "format";
		public static final String SIZE = "size";
	}

	public static ContentValues getContentValues(Deck deck) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Columns.NAME, deck.name);
		contentValues.put(Columns.DESCRIPTION, deck.description);
		contentValues.put(Columns.FORMAT, deck.format);
		contentValues.put(Columns.SIZE, deck.size);
		return contentValues;
	}

	@Override
	protected Map<String, String> getColumnTypes() {
		Map<String, String> columnTypes = super.getColumnTypes();
		columnTypes.put(Columns.NAME, "TEXT");
		columnTypes.put(Columns.DESCRIPTION, "TEXT");
		columnTypes.put(Columns.FORMAT, "TEXT");
		columnTypes.put(Columns.SIZE, "INTEGER");
		return columnTypes;
	}

	@Override
	protected String getConstraint() {
		return "UNIQUE (" + Columns.NAME + ") ON CONFLICT REPLACE";
	}

	@Override
	public String getName() {
		return TABLE_NAME;
	}
}
