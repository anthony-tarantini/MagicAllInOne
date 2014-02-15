package com.magicallinone.app.datasets;

import java.util.Map;

import android.content.ContentValues;

import com.magicallinone.app.models.SetCard;
import com.magicallinone.app.providers.DatabaseTable;

public class SetCardTable extends DatabaseTable {
	
	public static final String TABLE_NAME = "set_card_table";
	
	public static final class Columns extends DatabaseTable.Columns{
		public static final String SET_CODE = "set_code";
		public static final String CARD_ID = "card_id";
	}
	
	public static ContentValues getContentValues(SetCard setCard){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Columns.SET_CODE, setCard.set_code);
		contentValues.put(Columns.CARD_ID, setCard.card_id);
		return contentValues;
	}

	@Override
	protected Map<String, String> getColumnTypes() {
		Map<String, String> columnTypes = super.getColumnTypes();
		columnTypes.put(Columns.SET_CODE, "TEXT");
		columnTypes.put(Columns.CARD_ID, "TEXT");
		return columnTypes;
	}
	
	@Override
	protected String getConstraint() {
		return "UNIQUE (" + Columns.SET_CODE + ", " + Columns.CARD_ID + ") ON CONFLICT REPLACE";
	}

	@Override
	public String getName() {
		return TABLE_NAME;
	}
}
