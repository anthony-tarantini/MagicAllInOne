package com.magicallinone.app.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.magicallinone.app.application.MagicApplication;
import com.magicallinone.app.datasets.CardTable;
import com.magicallinone.app.datasets.CardsView;
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

	public static final String DB_CREATE_CARDS_VIEW = "CREATE VIEW " 
			+ MagicContentProvider.Paths.CARDS + " AS "
			+ "SELECT (set_card." + SetCardTable.Columns.SET_CARD_ID
			+ " + card." + CardTable.Columns.CARD_ID 
			+ ") AS " + CardsView.Columns.CARD_ID
			+ ", set_card." + SetCardTable.Columns.SET_CODE 
			+ " AS " + CardsView.Columns.SET_ID 
			+ ", card." + CardTable.Columns.NAME
			+ " AS " + CardsView.Columns.NAME
			+ ", card." + CardTable.Columns.MANA_COST
			+ " AS " + CardsView.Columns.MANA_COST
			+ ", card." + CardTable.Columns.TEXT
			+ " AS " + CardsView.Columns.RULES_TEXT
			+ ", card." + CardTable.Columns.FLAVOUR
			+ " AS " + CardsView.Columns.FLAVOUR_TEXT
			+ ", card." + CardTable.Columns.NUMBER
			+ " AS " + CardsView.Columns.NUMBER
			+ ", card." + CardTable.Columns.WATERMARK
			+ " AS " + CardsView.Columns.WATERMARK
			+ " FROM " + MagicContentProvider.Paths.CARD + " card" 
			+ ", " + MagicContentProvider.Paths.SET_CARD + " set_card"
			+ " WHERE card." + CardTable.Columns.MULTIVERSE_ID
			+ " = " + SetCardTable.Columns.CARD_ID;
	
	public MagicDatabase(final Context context) {
		super(context, MagicApplication.DATABASE_NAME, null, MagicApplication.DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(DB_CREATE_SET);
		db.execSQL(DB_CREATE_CARD);
		db.execSQL(DB_CREATE_SET_CARD);
		db.execSQL(DB_CREATE_CARDS_VIEW);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		if (oldVersion < newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + MagicContentProvider.Paths.SET);
			db.execSQL("DROP TABLE IF EXISTS " + MagicContentProvider.Paths.CARD);
			db.execSQL("DROP TABLE IF EXISTS " + MagicContentProvider.Paths.SET_CARD);
			db.execSQL("DROP VIEW IF EXISTS " + MagicContentProvider.Paths.CARDS);
			onCreate(db);
		}
	}
}
