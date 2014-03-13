package com.magicallinone.app.datasets;

import com.magicallinone.app.providers.DatabaseView;

public class DeckListView extends DatabaseView {
	
	public static final String VIEW_NAME = "deck_list_view";
	
	public static final class Columns{
		public static final String CARD_ID = "card_id";
		public static final String DECK_ID = "deck_id";
		public static final String NAME = "name";
		public static final String MANA_COST = "mana_cost";
		public static final String RULES_TEXT = "rules_text";
		public static final String FLAVOUR_TEXT = "flavour_text";
		public static final String SET_ID = "set_id";
		public static final String NUMBER = "number";
		public static final String WATERMARK = "watermark";
		public static final String QUANTITY = "quantity";
		public static final String TYPE = "type";
	}
	
	@Override
	protected String getSelectString() {
		return	"SELECT "+ DeckCardTable.TABLE_NAME
				+ "." + DeckCardTable.Columns.CARD_ID
				+ " AS " + DeckListView.Columns.CARD_ID
				+ ", " + DeckCardTable.TABLE_NAME 
				+ "." + DeckCardTable.Columns.DECK_ID 
				+ " AS " + DeckListView.Columns.DECK_ID 
				+ ", " + CardTable.TABLE_NAME 
				+ "." + CardTable.Columns.NAME
				+ " AS " + DeckListView.Columns.NAME
				+ ", " + SetCardTable.TABLE_NAME 
				+ "." + SetCardTable.Columns.SET_CODE
				+ " AS " + DeckListView.Columns.SET_ID
				+ ", " + CardTable.TABLE_NAME
				+ "." + CardTable.Columns.MANA_COST
				+ " AS " + DeckListView.Columns.MANA_COST
				+ ", " + CardTable.TABLE_NAME 
				+ "." + CardTable.Columns.TEXT
				+ " AS " + DeckListView.Columns.RULES_TEXT
				+ ", " + CardTable.TABLE_NAME
				+ "." + CardTable.Columns.TYPE 
				+ " AS " + DeckListView.Columns.TYPE
				+ ", " + CardTable.TABLE_NAME 
				+ "." + CardTable.Columns.FLAVOUR
				+ " AS " + DeckListView.Columns.FLAVOUR_TEXT
				+ ", " + CardTable.TABLE_NAME 
				+ "." + CardTable.Columns.NUMBER
				+ " AS " + DeckListView.Columns.NUMBER
				+ ", " + CardTable.TABLE_NAME 
				+ "." + CardTable.Columns.WATERMARK
				+ " AS " + DeckListView.Columns.WATERMARK
				+ ", " + DeckCardTable.TABLE_NAME 
				+ "." + DeckCardTable.Columns.QUANTITY
				+ " AS " + DeckListView.Columns.QUANTITY
				+ " FROM " + CardTable.TABLE_NAME 
				+ " " + CardTable.TABLE_NAME 
				+ ", " + DeckCardTable.TABLE_NAME 
				+ " " + DeckCardTable.TABLE_NAME
				+ ", " + SetCardTable.TABLE_NAME
				+ " " + SetCardTable.TABLE_NAME
				+ " WHERE " + CardTable.TABLE_NAME 
				+ "." + CardTable.Columns.MULTIVERSE_ID
				+ " = " + DeckCardTable.TABLE_NAME + "." + DeckCardTable.Columns.CARD_ID
				+ " AND " + SetCardTable.TABLE_NAME + "." + SetCardTable.Columns.CARD_ID 
				+ " = " + CardTable.TABLE_NAME + "." + CardTable.Columns.MULTIVERSE_ID;
	}

	@Override
	public String getName() {
		return VIEW_NAME;
	}
}
