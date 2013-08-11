package com.magicallinone.app.datasets;

import android.content.ContentValues;

import com.magicallinone.app.models.Deck;

public class DeckTable {
	public static class Columns {
		public static final String DECK_ID = "_id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String SIZE = "size";
		public static final String FORMAT = "format";
	}

	public static ContentValues getContentValues(Deck deck) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Columns.NAME, deck.name);
		contentValues.put(Columns.DESCRIPTION, deck.description);
		contentValues.put(Columns.SIZE, deck.size);
		contentValues.put(Columns.FORMAT, deck.format);
		return contentValues;
	}
}
