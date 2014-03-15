package com.magicallinone.app.datasets;

import android.content.ContentValues;

import com.magicallinone.app.models.Card;
import com.magicallinone.app.providers.DatabaseTable;
import com.magicallinone.app.utils.DBUtils;

import java.util.Map;

public class CardTable extends DatabaseTable {
	
	public static final String TABLE_NAME = "card_table";
	
	public static class Columns extends DatabaseTable.Columns{
		public static final String LAYOUT = "layout";
		public static final String NAME = "name";
		public static final String SPLIT_NAMES = "split_names";
		public static final String MANA_COST = "mana_cost";
		public static final String CMC = "cmc";
		public static final String COLORS = "colors";
		public static final String TYPE = "type";
		public static final String SUPERTYPES = "supertypes";
		public static final String EXTRA_TYPES = "extra_types";
		public static final String SUBTYPES = "subtypes";
		public static final String RARITY = "rarity";
		public static final String TEXT = "text";
		public static final String FLAVOUR = "flavour";
		public static final String NUMBER = "number";
		public static final String POWER = "power";
		public static final String TOUGHNESS = "toughness";
		public static final String LOYALTY = "loyalty";
		public static final String MULTIVERSE_ID = "multiverse_id";
		public static final String VARIATIONS = "variations";
		public static final String IMAGE_NAME = "image_name";
		public static final String WATERMARK = "watermark";
		public static final String BORDER = "border";
	}
	
	public static ContentValues getContentValues(Card card){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Columns.NAME, card.name);
		contentValues.put(Columns.MANA_COST, card.manaCost);
		contentValues.put(Columns.TEXT, card.text);
		contentValues.put(Columns.FLAVOUR, card.flavor);
		contentValues.put(Columns.NUMBER, card.number);
		contentValues.put(Columns.MULTIVERSE_ID, card.multiverseid);
		contentValues.put(Columns.WATERMARK, card.watermark);
		contentValues.put(Columns.COLORS, DBUtils.getCardColours(card.colors));
		contentValues.put(Columns.EXTRA_TYPES, DBUtils.getType(card.supertypes, card.types));
		contentValues.put(Columns.TYPE, card.type);
		return contentValues;
	}

	@Override
	public String getName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> getColumnTypes() {
		Map<String, String> columnTypes = super.getColumnTypes();
		columnTypes.put(Columns.NAME, "TEXT");
		columnTypes.put(Columns.MANA_COST, "TEXT");
		columnTypes.put(Columns.TEXT, "TEXT");
		columnTypes.put(Columns.FLAVOUR, "TEXT");
		columnTypes.put(Columns.NUMBER, "TEXT");
		columnTypes.put(Columns.MULTIVERSE_ID, "INTEGER");
		columnTypes.put(Columns.WATERMARK, "TEXT");
		columnTypes.put(Columns.COLORS, "TEXT");
		columnTypes.put(Columns.EXTRA_TYPES, "TEXT");
		columnTypes.put(Columns.TYPE, "TEXT");
		return columnTypes;
	}

	@Override
	protected String getConstraint() {
		return "UNIQUE (" + Columns.MULTIVERSE_ID + ") ON CONFLICT REPLACE";
	}
}
