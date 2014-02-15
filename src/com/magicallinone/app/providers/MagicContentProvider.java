package com.magicallinone.app.providers;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

import com.magicallinone.app.application.MagicDatabase;
import com.magicallinone.app.datasets.CardTable;
import com.magicallinone.app.datasets.CardsView;
import com.magicallinone.app.datasets.DeckCardTable;
import com.magicallinone.app.datasets.DeckListView;
import com.magicallinone.app.datasets.DeckTable;
import com.magicallinone.app.datasets.SetCardTable;
import com.magicallinone.app.datasets.SetTable;

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
			Log.d("TEST", "getDatabase");
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
		for (int i = 0; i < size; i++)
			sets.add(mMappings.get(mMappings.keyAt(i)));
		return sets;
	}
	
	public static final class Uris {
		public static final Uri SET_URI = Uri.parse(BASE_URI + "/" + Paths.SET_TABLE);
		public static final Uri CARD_URI = Uri.parse(BASE_URI + "/"
				+ Paths.CARD_TABLE);
		public static final Uri SET_CARD_URI = Uri.parse(BASE_URI + "/"
				+ Paths.SET_CARD_TABLE);
		public static final Uri CARDS_URI = Uri.parse(BASE_URI + "/"
				+ Paths.CARDS_VIEW);
		public static final Uri DECKS_URI = Uri.parse(BASE_URI + "/"
				+ Paths.DECK_TABLE);
		public static final Uri DECK_CARD_URI = Uri.parse(BASE_URI + "/"
				+ Paths.DECK_CARD_TABLE);
		public static final Uri DECK_LIST_URI = Uri.parse(BASE_URI + "/"
				+ Paths.DECKS_VIEW);
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
		createDatabase();
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
		mUriMatcher.addURI(AUTHORITY, Paths.CARDS_VIEW, Codes.CARDS);
		mMappings.append(Codes.CARDS, new CardsView());
		mUriMatcher.addURI(AUTHORITY, Paths.CARDS_VIEW + "/*", Codes.CARDS_STAR);
		mMappings.append(Codes.CARDS_STAR, new CardsView());
		mUriMatcher.addURI(AUTHORITY, Paths.DECK_TABLE, Codes.DECKS);
		mMappings.append(Codes.DECKS, new DeckTable());
		mUriMatcher.addURI(AUTHORITY, Paths.DECK_TABLE + "/*", Codes.DECKS_STAR);
		mMappings.append(Codes.DECKS_STAR, new DeckTable());
		mUriMatcher.addURI(AUTHORITY, Paths.DECK_CARD_TABLE, Codes.DECK_CARD);
		mMappings.append(Codes.DECK_CARD, new DeckCardTable());
		mUriMatcher.addURI(AUTHORITY, Paths.DECK_CARD_TABLE + "/*", Codes.DECK_CARD_STAR);
		mMappings.append(Codes.DECK_CARD_STAR, new DeckCardTable());
		mUriMatcher.addURI(AUTHORITY, Paths.DECKS_VIEW, Codes.DECK_LIST);
		mMappings.append(Codes.DECKS, new DeckListView());
		mUriMatcher.addURI(AUTHORITY, Paths.DECKS_VIEW + "/*", Codes.DECK_LIST_STAR);
		mMappings.append(Codes.DECKS_STAR, new DeckListView());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase insertDB = sDatabase;
		int token = mUriMatcher.match(uri);
		long id;
		switch (token) {
		case Codes.SET:
			id = insertDB.insert(Paths.SET_TABLE, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uris.SET_URI.buildUpon().appendPath(String.valueOf(id))
					.build();
		case Codes.CARD:
			id = insertDB.insert(Paths.CARD_TABLE, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uris.CARD_URI.buildUpon().appendPath(String.valueOf(id))
					.build();
		case Codes.SET_CARD:
			id = insertDB.insert(Paths.SET_CARD_TABLE, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uris.SET_CARD_URI.buildUpon().appendPath(String.valueOf(id))
					.build();
		case Codes.DECKS:
			id = insertDB.insert(Paths.DECK_TABLE, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uris.DECKS_URI.buildUpon().appendPath(String.valueOf(id))
					.build();
		case Codes.DECK_CARD:
			id = insertDB.insert(Paths.DECK_CARD_TABLE, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uris.DECK_CARD_URI.buildUpon()
					.appendPath(String.valueOf(id)).build();
		default:
			throw new UnsupportedOperationException("URI: " + uri
					+ " not supported.");
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase queryDB = sDatabase;
		final int match = mUriMatcher.match(uri);
		SQLiteQueryBuilder builder;
		switch (match) {
		case Codes.SET:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.SET_TABLE);
			return builder.query(queryDB, projection, selection, selectionArgs,
					null, null, sortOrder);
		case Codes.CARD:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.CARD_TABLE);
			return builder.query(queryDB, projection, selection, selectionArgs,
					null, null, sortOrder);
		case Codes.SET_CARD:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.SET_CARD_TABLE);
			return builder.query(queryDB, projection, selection, selectionArgs,
					null, null, sortOrder);
		case Codes.CARDS:
		case Codes.CARDS_STAR:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.CARDS_VIEW);
			return builder.query(queryDB, projection, selection, selectionArgs,
					null, null, sortOrder);
		case Codes.DECKS:
		case Codes.DECKS_STAR:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.DECK_TABLE);
			return builder.query(queryDB, projection, selection, selectionArgs,
					null, null, sortOrder);
		case Codes.DECK_CARD:
		case Codes.DECK_CARD_STAR:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.DECK_CARD_TABLE);
			return builder.query(queryDB, projection, selection, selectionArgs,
					null, null, sortOrder);
		case Codes.DECK_LIST:
		case Codes.DECK_LIST_STAR:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.DECKS_VIEW);
			return builder.query(queryDB, projection, selection, selectionArgs,
					null, null, sortOrder);
		default:
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase deleteDB = sDatabase;
		final int match = mUriMatcher.match(uri);
		int count = 0;
		switch (match) {
		case Codes.DECK_CARD:
			count = deleteDB.delete(Paths.DECK_CARD_TABLE, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			getContext().getContentResolver().notifyChange(Uris.DECK_LIST_URI, null);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		SQLiteDatabase updateDB = sDatabase;
		final int match = mUriMatcher.match(uri);
		int count = 0;
		switch (match) {
		case Codes.SET:
			count = updateDB.update(Paths.SET_TABLE, values, whereClause, whereArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			break;
		case Codes.CARD:
			count = updateDB.update(Paths.CARD_TABLE, values, whereClause, whereArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			break;
		case Codes.SET_CARD:
			count = updateDB.update(Paths.SET_CARD_TABLE, values, whereClause, whereArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			break;
		case Codes.DECKS:
			count = updateDB.update(Paths.DECK_TABLE, values, whereClause, whereArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			break;
		case Codes.DECK_CARD:
			count = updateDB.update(Paths.DECK_CARD_TABLE, values, whereClause, whereArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			getContext().getContentResolver().notifyChange(Uris.DECK_LIST_URI, null);
			break;
		}
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	public Uri getUri(String path) {
		return Uri.parse(BASE_URI + "/" + path);
	}
}
