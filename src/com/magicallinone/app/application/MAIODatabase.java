package com.magicallinone.app.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.magicallinone.app.providers.DatabaseSet;
import com.magicallinone.app.providers.DatabaseTable;
import com.magicallinone.app.providers.DatabaseView;

import java.util.Collection;

public class MAIODatabase extends SQLiteOpenHelper {

    private final Collection<DatabaseSet> mSets;
	
	public MAIODatabase(final Context context, final Collection<DatabaseSet> sets) {
		super(context, MAIOApplication.DATABASE_NAME, null, MAIOApplication.DATABASE_VERSION);
		Log.d("MAIODatabase", "MAIODatabase");
        mSets = sets;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		Log.d("MAIODatabase", "onCreate");
		createTables(db);
        createViews(db);
	}

    private void createTables(final SQLiteDatabase db) {
        for (final DatabaseSet set : mSets) {
            if (set instanceof DatabaseTable) {
                set.onCreate(db);
            }
        }
    }

    private void createViews(final SQLiteDatabase db) {
        for (final DatabaseSet set : mSets) {
            if (set instanceof DatabaseView) {
                set.onCreate(db);
            }
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
