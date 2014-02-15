package com.magicallinone.app.application;

import java.util.Collection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.magicallinone.app.providers.DatabaseSet;

public class MagicDatabase extends SQLiteOpenHelper {
	private Collection<DatabaseSet> mSets;
	
	public MagicDatabase(final Context context, final Collection<DatabaseSet> sets) {
		super(context, MagicApplication.DATABASE_NAME, null, MagicApplication.DATABASE_VERSION);
		Log.d("MagicDatabase", "MagicDatabase");
		mSets = sets;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		Log.d("MagicDatabase", "onCreate");
		for (final DatabaseSet set : mSets) {
			set.onCreate(db);
		}
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		for (final DatabaseSet set : mSets) {
			set.onUpgrade(db);
		}
	}
}
