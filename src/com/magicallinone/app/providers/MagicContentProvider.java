package com.magicallinone.app.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.SparseArray;

import com.magicallinone.app.application.MagicDatabase;
import com.magicallinone.app.datasets.CardTable;
import com.magicallinone.app.datasets.CardsView;
import com.magicallinone.app.datasets.DeckCardTable;
import com.magicallinone.app.datasets.DeckListView;
import com.magicallinone.app.datasets.DeckTable;
import com.magicallinone.app.datasets.SetCardTable;
import com.magicallinone.app.datasets.SetTable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class MagicContentProvider extends ContentProvider {
	
	private static SQLiteDatabase sDatabase;

	public static final String AUTHORITY = "com.magicallinone.app.providers";
	public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.magicallinone.app";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/vnd.magicallinone.app";

	private final SparseArray<DatabaseSet> mMappings = new SparseArray<DatabaseSet>();
	private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	protected SQLiteDatabase getDatabase() {
		if (sDatabase == null) {
			createDatabase();
		}
		return sDatabase;
	}
	
	private void createDatabase() {
		if (sDatabase == null) {
			final MagicDatabase database = new MagicDatabase(getContext(), getSets()); 
			sDatabase = database.getWritableDatabase();
		}
	}
	
	protected void destroyDatabase() {
		if (sDatabase != null) {
			sDatabase.close();
			sDatabase = null;
		}
	}
	
	private Collection<DatabaseSet> getSets() {
		final int size = mMappings.size();
		final Set<DatabaseSet> sets = new LinkedHashSet<DatabaseSet>(size);
		for (int i = 0; i < size; i++) {
            sets.add(mMappings.get(mMappings.keyAt(i)));
        }
		return sets;
	}
	
	public static final class Uris {
		public static final Uri SET_URI = Uri.parse(BASE_URI + "/" + Paths.SET_TABLE);
		public static final Uri CARD_URI = Uri.parse(BASE_URI + "/" + Paths.CARD_TABLE);
		public static final Uri SET_CARD_URI = Uri.parse(BASE_URI + "/" + Paths.SET_CARD_TABLE);
		public static final Uri CARDS_URI = Uri.parse(BASE_URI + "/" + Paths.CARDS_VIEW);
		public static final Uri DECKS_URI = Uri.parse(BASE_URI + "/" + Paths.DECK_TABLE);
		public static final Uri DECK_CARD_URI = Uri.parse(BASE_URI + "/" + Paths.DECK_CARD_TABLE);
		public static final Uri DECK_LIST_URI = Uri.parse(BASE_URI + "/" + Paths.DECKS_VIEW);
	}

	private static final class Tables {
		private static final String SET = SetTable.TABLE_NAME;
		private static final String CARD = CardTable.TABLE_NAME;
		private static final String SET_CARD = SetCardTable.TABLE_NAME;
		private static final String DECK = DeckTable.TABLE_NAME;
		private static final String DECK_CARD = DeckCardTable.TABLE_NAME;
	}
	
	private static final class Views {
		private static final String CARDS = CardsView.VIEW_NAME; 
		private static final String DECKS = DeckListView.VIEW_NAME;
	}
	
	private static final class Paths {
		public static final String SET_TABLE = Tables.SET;
		public static final String CARD_TABLE = Tables.CARD;
		public static final String SET_CARD_TABLE = Tables.SET_CARD;
		public static final String DECK_TABLE = Tables.DECK;
		public static final String DECK_CARD_TABLE = Tables.DECK_CARD;
		public static final String CARDS_VIEW = Views.CARDS;
		public static final String DECKS_VIEW = Views.DECKS;
	}

	private static final class Codes {
		public static final int SET = 1;
		public static final int SET_STAR = 2;
		public static final int CARD = 3;
		public static final int CARD_STAR = 4;
		public static final int SET_CARD = 5;
		public static final int SET_CARD_STAR = 6;
		public static final int CARDS = 7;
		public static final int CARDS_STAR = 8;
		public static final int DECKS = 9;
		public static final int DECKS_STAR = 10;
		public static final int DECK_CARD = 11;
		public static final int DECK_CARD_STAR = 12;
		public static final int DECK_LIST = 13;
		public static final int DECK_LIST_STAR = 14;
	}

	@Override
	public boolean onCreate() {
		mUriMatcher.addURI(AUTHORITY, Paths.SET_TABLE, Codes.SET);
		mMappings.append(Codes.SET, new SetTable());
		mUriMatcher.addURI(AUTHORITY, Paths.SET_TABLE + "/*", Codes.SET_STAR);
		mMappings.append(Codes.SET_STAR, new SetTable());

		mUriMatcher.addURI(AUTHORITY, Paths.CARD_TABLE, Codes.CARD);
		mMappings.append(Codes.CARD, new CardTable());
		mUriMatcher.addURI(AUTHORITY, Paths.CARD_TABLE + "/*", Codes.CARD_STAR);
		mMappings.append(Codes.CARD_STAR, new CardTable());

		mUriMatcher.addURI(AUTHORITY, Paths.SET_CARD_TABLE, Codes.SET_CARD);
		mMappings.append(Codes.SET_CARD, new SetCardTable());
		mUriMatcher.addURI(AUTHORITY, Paths.SET_CARD_TABLE + "/*", Codes.SET_CARD_STAR);
		mMappings.append(Codes.SET_CARD_STAR, new SetCardTable());

		mUriMatcher.addURI(AUTHORITY, Paths.DECK_TABLE, Codes.DECKS);
		mMappings.append(Codes.DECKS, new DeckTable());
		mUriMatcher.addURI(AUTHORITY, Paths.DECK_TABLE + "/*", Codes.DECKS_STAR);
		mMappings.append(Codes.DECKS_STAR, new DeckTable());

		mUriMatcher.addURI(AUTHORITY, Paths.DECK_CARD_TABLE, Codes.DECK_CARD);
		mMappings.append(Codes.DECK_CARD, new DeckCardTable());
		mUriMatcher.addURI(AUTHORITY, Paths.DECK_CARD_TABLE + "/*", Codes.DECK_CARD_STAR);
		mMappings.append(Codes.DECK_CARD_STAR, new DeckCardTable());

		mUriMatcher.addURI(AUTHORITY, Paths.CARDS_VIEW, Codes.CARDS);
		mMappings.append(Codes.CARDS, new CardsView());
		mUriMatcher.addURI(AUTHORITY, Paths.CARDS_VIEW + "/*", Codes.CARDS_STAR);
		mMappings.append(Codes.CARDS_STAR, new CardsView());

		mUriMatcher.addURI(AUTHORITY, Paths.DECKS_VIEW, Codes.DECK_LIST);
		mMappings.append(Codes.DECK_LIST, new DeckListView());
		mUriMatcher.addURI(AUTHORITY, Paths.DECKS_VIEW + "/*", Codes.DECK_LIST_STAR);
		mMappings.append(Codes.DECK_LIST_STAR, new DeckListView());

        createDatabase();
		return true;
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final SQLiteDatabase insertDB = sDatabase;
		final int match = mUriMatcher.match(uri);

        final DatabaseSet databaseSet = mMappings.get(match);
        final long id = databaseSet.insert(insertDB, uri, values);

        final String idValue = String.valueOf(id);
        final Uri uriToReturn = uri.buildUpon().appendPath(idValue).build();
        return uriToReturn;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		final SQLiteDatabase queryDB = sDatabase;
		final int match = mUriMatcher.match(uri);

        final DatabaseSet databaseSet = mMappings.get(match);
        final Cursor cursor = databaseSet.query(queryDB, uri, projection, selection, selectionArgs, sortOrder);

        return cursor;
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		final SQLiteDatabase deleteDB = sDatabase;
		final int match = mUriMatcher.match(uri);

        final DatabaseSet databaseSet = mMappings.get(match);
        final int removedRows = databaseSet.delete(deleteDB, uri, selection, selectionArgs);

		return removedRows;
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String whereClause, final String[] whereArgs) {
		final SQLiteDatabase updateDB = sDatabase;
		final int match = mUriMatcher.match(uri);

        final DatabaseSet databaseSet = mMappings.get(match);
        final int updatedRows = databaseSet.update(updateDB, uri, values, whereClause, whereArgs);

        return updatedRows;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}
}
