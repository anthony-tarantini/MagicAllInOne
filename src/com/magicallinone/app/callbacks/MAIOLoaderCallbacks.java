package com.magicallinone.app.callbacks;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Loader.ForceLoadContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.magicallinone.app.loaders.MAIOCursorLoader;

/**
 * Created by tony on 2014-03-21.
 */
public abstract class MAIOLoaderCallbacks implements LoaderCallbacks<Cursor> {
    public abstract Uri getUri();
    public abstract int getLoaderId();

    private final MAIOCallbacksListener mMaioCallbacksListener;
    private final Context mContext;
    private final LoaderManager mLoaderManager;
    private ForceLoadContentObserver mForceLoadContentObserver;

    public MAIOLoaderCallbacks(final Context context, final LoaderManager loaderManager, final MAIOCallbacksListener maioCallbacksListener) {
        mLoaderManager = loaderManager;
        mMaioCallbacksListener = maioCallbacksListener;
        mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final MAIOCursorLoader maioCursorLoader = new MAIOCursorLoader(mContext);
        maioCursorLoader.setUri(getUri());
        mForceLoadContentObserver = (ForceLoadContentObserver) maioCursorLoader.getForceLoadContentObserver();
        mContext.getContentResolver().registerContentObserver(getUri(), false, mForceLoadContentObserver);
        return maioCursorLoader;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        cursor.moveToFirst();
        mMaioCallbacksListener.onLoadFinished(getUri(), cursor);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
        mMaioCallbacksListener.onLoaderReset(cursorLoader);
    }

    public void onStart(final Context context) {
        final int loaderId = getLoaderId();
        final Loader<?> loader = mLoaderManager.getLoader(loaderId);
        if (loader == null) {
            mLoaderManager.initLoader(loaderId, null, this);
        } else {
            mLoaderManager.restartLoader(loaderId, null, this);
        }
    }

    public void onStop(final Context context) {
        if(mForceLoadContentObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mForceLoadContentObserver);
        }
    }

}
