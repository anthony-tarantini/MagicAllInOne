package com.magicallinone.app.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MagicContentProvider extends ContentProvider {

	private MagicDatabase mMagicDB;

	public static final String AUTHORITY = "com.magicallinone.app.providers";
	public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	public static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.magicallinone.app";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/vnd.magicallinone.app";

	public static final class Uris {
		public static final Uri SET_URI = Uri.parse(BASE_URI + "/" + Paths.SET);
		public static final Uri CARD_URI = Uri.parse(BASE_URI + "/" + Paths.CARD);
		public static final Uri SET_CARD_URI = Uri.parse(BASE_URI + "/" + Paths.SET_CARD);
		public static final Uri CARDS_URI = Uri.parse(BASE_URI + "/" + Paths.CARDS);
	}

	public static final class Paths {
		public static final String SET = "magic_set";
		public static final String CARD = "card";
		public static final String SET_CARD = "set_card";
		public static final String CARDS = "cards";
	}

	public static final class Codes {
		public static final int SET = 1;
		public static final int SET_STAR = 2;
		public static final int CARD = 3;
		public static final int CARD_STAR = 4;
		public static final int SET_CARD = 5;
		public static final int SET_CARD_STAR = 6;
		public static final int CARDS = 7;
		public static final int CARDS_STAR = 8;
	}

	@Override
	public boolean onCreate() {
		mMagicDB = new MagicDatabase(getContext());
		URI_MATCHER.addURI(AUTHORITY, Paths.SET, Codes.SET);
		URI_MATCHER.addURI(AUTHORITY, Paths.SET + "/*", Codes.SET_STAR);
		URI_MATCHER.addURI(AUTHORITY, Paths.CARD, Codes.CARD);
		URI_MATCHER.addURI(AUTHORITY, Paths.CARD + "/*", Codes.CARD_STAR);
		URI_MATCHER.addURI(AUTHORITY, Paths.SET_CARD, Codes.SET_CARD);
		URI_MATCHER.addURI(AUTHORITY, Paths.SET_CARD + "/*", Codes.SET_CARD_STAR);
		URI_MATCHER.addURI(AUTHORITY, Paths.CARDS, Codes.CARDS);
		URI_MATCHER.addURI(AUTHORITY, Paths.CARDS + "/*", Codes.CARDS_STAR);
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase insertDB = mMagicDB.getWritableDatabase();
		int token = URI_MATCHER.match(uri);
		long id;
		switch (token) {
		case Codes.SET:
			id = insertDB.insert(Paths.SET, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uris.SET_URI.buildUpon().appendPath(String.valueOf(id)).build();
		case Codes.CARD:
			id = insertDB.insert(Paths.CARD, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uris.CARD_URI.buildUpon().appendPath(String.valueOf(id)).build();
		case Codes.SET_CARD:
			id = insertDB.insert(Paths.SET_CARD, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uris.SET_CARD_URI.buildUpon().appendPath(String.valueOf(id)).build();
		default:
			throw new UnsupportedOperationException("URI: " + uri + " not supported.");
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase queryDB = mMagicDB.getReadableDatabase();
		final int match = URI_MATCHER.match(uri);
		SQLiteQueryBuilder builder;
		switch (match) {
		case Codes.SET:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.SET);
			return builder.query(queryDB, projection, selection, selectionArgs, null, null, sortOrder);
		case Codes.CARD:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.CARD);
			return builder.query(queryDB, projection, selection, selectionArgs, null, null, sortOrder);
		case Codes.SET_CARD:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.SET_CARD);
			return builder.query(queryDB, projection, selection, selectionArgs, null, null, sortOrder);
		case Codes.CARDS:
		case Codes.CARDS_STAR:
			builder = new SQLiteQueryBuilder();
			builder.setTables(Paths.CARDS);
			return builder.query(queryDB, projection, selection, selectionArgs, null, null, sortOrder);
		default:
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}
}
