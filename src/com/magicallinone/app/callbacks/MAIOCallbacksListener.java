package com.magicallinone.app.callbacks;

import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by tony on 2014-03-21.
 */
public interface MAIOCallbacksListener {
    public void onLoadFinished(final Uri uri, final Cursor cursor);
    public void onLoaderReset(final Loader<Cursor> loader);
}
