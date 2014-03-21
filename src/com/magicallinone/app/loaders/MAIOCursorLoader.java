package com.magicallinone.app.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * Created by tony on 2014-03-21.
 */
public class MAIOCursorLoader extends CursorLoader {

    private final ForceLoadContentObserver mForceLoadContentObserver;

    public MAIOCursorLoader(Context context) {
        this(context, null, null, null, null, null);
    }

    public MAIOCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
        mForceLoadContentObserver = new ForceLoadContentObserver();
    }

    public ForceLoadContentObserver getForceLoadContentObserver() {
        return mForceLoadContentObserver;
    }
}
