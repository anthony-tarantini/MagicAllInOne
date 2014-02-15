package com.magicallinone.app.datasets;

import java.util.Map;

import com.magicallinone.app.providers.DatabaseTable;

public class DeckCardTable extends DatabaseTable {
	
	public static final String TABLE_NAME = "deck_card";
	
	public class Columns extends DatabaseTable.Columns {
		public static final String DECK_ID = "deck_id";
		public static final String CARD_ID = "card_id";
		public static final String QUANTITY = "quantity";
	}

	@Override
	protected Map<String, String> getColumnTypes() {
		Map<String, String> columnTypes = super.getColumnTypes();
		columnTypes.put(Columns.DECK_ID, "INTEGER");
		columnTypes.put(Columns.CARD_ID, "INTEGER");
		columnTypes.put(Columns.QUANTITY, "INTEGER");
		return columnTypes;
	}

	@Override
	protected String getConstraint() {
		return "UNIQUE (" + Columns.DECK_ID + ", " + Columns.CARD_ID + ") ON CONFLICT REPLACE";
	}

	@Override
	public String getName() {
		return TABLE_NAME;
	}
	
}
