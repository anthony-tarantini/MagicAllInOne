package com.magicallinone.app.datasets;

import android.content.ContentValues;

import com.magicallinone.app.models.Card;

public class CardTable {
	public static class Columns {
		public static final String CARD_ID = "_id";
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
		contentValues.put(Columns.COLORS, getCardColours(card.colors));
		return contentValues;
	}

	private static String getCardColours(String[] colours){
		String cardColours = "";
		for (String colour : colours)
			cardColours += colour.charAt(0);
		return cardColours.toLowerCase();
	}
}
