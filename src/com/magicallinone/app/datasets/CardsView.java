package com.magicallinone.app.datasets;

import com.magicallinone.app.providers.DatabaseView;

public class CardsView extends DatabaseView {
	
	public static final String VIEW_NAME = "cards_view";
	
	public static final class Columns{
		public static final String CARD_ID = "_id";
		public static final String SET_ID = "set_id";
		public static final String NAME = "name";
		public static final String MANA_COST = "mana_cost";
		public static final String RULES_TEXT = "rules_text";
		public static final String FLAVOUR_TEXT = "flavour_text";
		public static final String NUMBER = "number";
		public static final String WATERMARK = "watermark";
	}

	@Override
	protected String getSelectString() {
		return "SELECT "
                + CardTable.TABLE_NAME + "." + CardTable.Columns.MULTIVERSE_ID + " AS " + CardsView.Columns.CARD_ID
				+ ", "
                + SetCardTable.TABLE_NAME + "." + SetCardTable.Columns.SET_CODE + " AS " + CardsView.Columns.SET_ID
				+ ", "
                + CardTable.TABLE_NAME + "." + CardTable.Columns.NAME + " AS " + CardsView.Columns.NAME
				+ ", "
                + CardTable.TABLE_NAME + "." + CardTable.Columns.MANA_COST + " AS " + CardsView.Columns.MANA_COST
				+ ", "
                + CardTable.TABLE_NAME + "." + CardTable.Columns.TEXT + " AS " + CardsView.Columns.RULES_TEXT
				+ ", "
                + CardTable.TABLE_NAME + "." + CardTable.Columns.FLAVOUR + " AS " + CardsView.Columns.FLAVOUR_TEXT
				+ ", "
                + CardTable.TABLE_NAME + "." + CardTable.Columns.NUMBER + " AS " + CardsView.Columns.NUMBER
				+ ", "
                + CardTable.TABLE_NAME + "." + CardTable.Columns.WATERMARK + " AS " + CardsView.Columns.WATERMARK
				+ " FROM " + CardTable.TABLE_NAME + ", " + SetCardTable.TABLE_NAME
				+ " WHERE " + CardTable.TABLE_NAME + "." + CardTable.Columns.MULTIVERSE_ID + " = " + SetCardTable.Columns.CARD_ID;
	}

	@Override
	public String getName() {
		return VIEW_NAME;
	}
}
