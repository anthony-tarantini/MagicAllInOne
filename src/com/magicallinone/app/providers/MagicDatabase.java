package com.magicallinone.app.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.magicallinone.app.application.MagicApplication;
import com.magicallinone.app.datasets.CardTable;
import com.magicallinone.app.datasets.CardsView;
import com.magicallinone.app.datasets.DeckCardTable;
import com.magicallinone.app.datasets.DeckListView;
import com.magicallinone.app.datasets.DeckTable;
import com.magicallinone.app.datasets.SetCardTable;
import com.magicallinone.app.datasets.SetTable;

public class MagicDatabase extends SQLiteOpenHelper {
	public static final String DB_CREATE_SET = "CREATE TABLE "
			+ MagicContentProvider.Paths.SET + " ("
			+ SetTable.Columns.SET_ID
			+ " INTEGER PRIMARY KEY, "
			+ SetTable.Columns.NAME
			+ " TEXT, "
			+ SetTable.Columns.CODE
			+ " TEXT, "
			+ SetTable.Columns.RELEASE_DATE
			+ " TEXT, "
			+ SetTable.Columns.BORDER
			+ " TEXT, "
			+ SetTable.Columns.TYPE
			+ " TEXT, "
			+ SetTable.Columns.BLOCK
			+ " TEXT, " + "UNIQUE ("
			+ SetTable.Columns.CODE
			+ ") ON CONFLICT REPLACE)";

	public static final String DB_CREATE_CARD = "CREATE TABLE "
			+ MagicContentProvider.Paths.CARD + " ("
			+ CardTable.Columns.CARD_ID
			+ " INTEGER PRIMARY KEY, "
			+ CardTable.Columns.LAYOUT
			+ " TEXT, " 
			+ CardTable.Columns.NAME
			+ " TEXT, "
			+ CardTable.Columns.SPLIT_NAMES
			+ " TEXT, "
			+ CardTable.Columns.MANA_COST
			+ " TEXT, " 
			+ CardTable.Columns.CMC
			+ " TEXT, "
			+ CardTable.Columns.COLORS
			+ " TEXT, "
			+ CardTable.Columns.SUPERTYPES
			+ " TEXT, "
			+ CardTable.Columns.EXTRA_TYPES
			+ " TEXT, "
			+ CardTable.Columns.SUBTYPES
			+ " TEXT, "
			+ CardTable.Columns.RARITY
			+ " TEXT, " 
			+ CardTable.Columns.TEXT
			+ " TEXT, "
			+ CardTable.Columns.FLAVOUR
			+ " TEXT, "
			+ CardTable.Columns.NUMBER
			+ " INTEGER, " 
			+ CardTable.Columns.POWER
			+ " TEXT, "
			+ CardTable.Columns.TOUGHNESS
			+ " TEXT, "
			+ CardTable.Columns.LOYALTY
			+ " TEXT, "
			+ CardTable.Columns.MULTIVERSE_ID
			+ " INTEGER, "
			+ CardTable.Columns.VARIATIONS
			+ " TEXT, "
			+ CardTable.Columns.IMAGE_NAME
			+ " TEXT, "
			+ CardTable.Columns.WATERMARK
			+ " TEXT, "
			+ CardTable.Columns.BORDER + " TEXT"
			+ ", UNIQUE ("
			+ CardTable.Columns.MULTIVERSE_ID
			+ ") ON CONFLICT REPLACE)";

	public static final String DB_CREATE_SET_CARD = "CREATE TABLE "
			+ MagicContentProvider.Paths.SET_CARD + " ("
			+ SetCardTable.Columns.SET_CARD_ID
			+ " INTEGER PRIMARY KEY, "
			+ SetCardTable.Columns.SET_CODE
			+ " TEXT NOT NULL, "
			+ SetCardTable.Columns.CARD_ID
			+ " TEXT NOT NULL, " + " UNIQUE ("
			+ SetCardTable.Columns.SET_CODE
			+ ", "
			+ SetCardTable.Columns.CARD_ID
			+ ") ON CONFLICT REPLACE)";

	public static final String DB_CREATE_DECKS = "CREATE TABLE "
			+ MagicContentProvider.Paths.DECKS + " ("
			+ DeckTable.Columns.DECK_ID
			+ " INTEGER PRIMARY KEY, "
			+ DeckTable.Columns.NAME
			+ " TEXT NOT NULL, " 
			+ DeckTable.Columns.DESCRIPTION
			+ " TEXT, "
			+ DeckTable.Columns.SIZE 
			+ " INTEGER NOT NULL, "
			+ DeckTable.Columns.FORMAT
			+ " TEXT, " + " UNIQUE ("
			+ DeckTable.Columns.DECK_ID
			+ ") ON CONFLICT REPLACE)"; 
	
	public static final String DB_CREATE_DECK_CARD = "CREATE TABLE "
			+ MagicContentProvider.Paths.DECK_CARD + " ("
			+ DeckCardTable.Columns.DECK_CARD_ID
			+ " INTEGER PRIMARY KEY, "
			+ DeckCardTable.Columns.DECK_ID
			+ " INTEGER NOT NULL, "
			+ DeckCardTable.Columns.CARD_ID
			+ " INTEGER NOT NULL, "
			+ DeckCardTable.Columns.QUANTITY
			+ " INTEGER NOT NULL,  UNIQUE ("
			+ DeckCardTable.Columns.DECK_ID 
			+ ", "
			+ DeckCardTable.Columns.CARD_ID
			+ ") ON CONFLICT REPLACE)";
			
	public static final String DB_CREATE_CARDS_VIEW = "CREATE VIEW " 
			+ MagicContentProvider.Paths.CARDS + " AS "
			+ "SELECT " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.MULTIVERSE_ID 
			+ " AS " + CardsView.Columns.CARD_ID
			+ ", " + MagicContentProvider.Paths.SET_CARD 
			+ "." + SetCardTable.Columns.SET_CODE 
			+ " AS " + CardsView.Columns.SET_ID 
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.NAME
			+ " AS " + CardsView.Columns.NAME
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.MANA_COST
			+ " AS " + CardsView.Columns.MANA_COST
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.TEXT
			+ " AS " + CardsView.Columns.RULES_TEXT
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.FLAVOUR
			+ " AS " + CardsView.Columns.FLAVOUR_TEXT
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.NUMBER
			+ " AS " + CardsView.Columns.NUMBER
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.WATERMARK
			+ " AS " + CardsView.Columns.WATERMARK
			+ " FROM " + MagicContentProvider.Paths.CARD + " card" 
			+ ", " + MagicContentProvider.Paths.SET_CARD + " set_card"
			+ " WHERE " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.MULTIVERSE_ID
			+ " = " + SetCardTable.Columns.CARD_ID;
	
	public static final String DB_CREATE_DECK_LIST_VIEW = "CREATE VIEW " 
			+ MagicContentProvider.Paths.DECK_LIST + " AS "
			+ "SELECT ("+ MagicContentProvider.Paths.DECK_CARD 
			+ "." + DeckCardTable.Columns.DECK_CARD_ID
			+ " + " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.CARD_ID 
			+ ") AS " + DeckListView.Columns.CARD_ID
			+ ", " + MagicContentProvider.Paths.DECK_CARD 
			+ "." + DeckCardTable.Columns.DECK_ID 
			+ " AS " + DeckListView.Columns.DECK_ID 
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.NAME
			+ " AS " + DeckListView.Columns.NAME
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.MANA_COST
			+ " AS " + DeckListView.Columns.MANA_COST
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.TEXT
			+ " AS " + DeckListView.Columns.RULES_TEXT
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.FLAVOUR
			+ " AS " + DeckListView.Columns.FLAVOUR_TEXT
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.NUMBER
			+ " AS " + DeckListView.Columns.NUMBER
			+ ", " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.WATERMARK
			+ " AS " + DeckListView.Columns.WATERMARK
			+ ", " + MagicContentProvider.Paths.DECK_CARD 
			+ "." + DeckCardTable.Columns.QUANTITY
			+ " AS " + DeckListView.Columns.QUANTITY
			+ " FROM " + MagicContentProvider.Paths.CARD 
			+ " " + MagicContentProvider.Paths.CARD 
			+ ", " + MagicContentProvider.Paths.DECK_CARD 
			+ " " + MagicContentProvider.Paths.DECK_CARD
			+ " WHERE " + MagicContentProvider.Paths.CARD 
			+ "." + CardTable.Columns.MULTIVERSE_ID
			+ " = " + MagicContentProvider.Paths.DECK_CARD + "." + DeckCardTable.Columns.CARD_ID;
	
	public MagicDatabase(final Context context) {
		super(context, MagicApplication.DATABASE_NAME, null, MagicApplication.DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(DB_CREATE_SET);
		Log.d("DATABASE VIEW", DB_CREATE_DECK_LIST_VIEW);
		db.execSQL(DB_CREATE_CARD);
		db.execSQL(DB_CREATE_DECKS);
		db.execSQL(DB_CREATE_SET_CARD);
		db.execSQL(DB_CREATE_DECK_CARD);
		db.execSQL(DB_CREATE_CARDS_VIEW);
		db.execSQL(DB_CREATE_DECK_LIST_VIEW);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		if (oldVersion < newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + MagicContentProvider.Paths.SET);
			db.execSQL("DROP TABLE IF EXISTS " + MagicContentProvider.Paths.CARD);
			db.execSQL("DROP TABLE IF EXISTS " + MagicContentProvider.Paths.DECKS);
			db.execSQL("DROP TABLE IF EXISTS " + MagicContentProvider.Paths.SET_CARD);
			db.execSQL("DROP TABLE IF EXISTS " + MagicContentProvider.Paths.DECK_CARD);
			db.execSQL("DROP VIEW IF EXISTS " + MagicContentProvider.Paths.CARDS);
			db.execSQL("DROP VIEW IF EXISTS " + MagicContentProvider.Paths.DECK_LIST);
			onCreate(db);
		}
	}
}
