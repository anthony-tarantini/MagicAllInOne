package com.magicallinone.app.fragment;

import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.magicallinone.app.callbacks.MAIOCallbacksListener;

public abstract class BaseLoaderFragment extends BaseFragment implements ViewBinder, MAIOCallbacksListener, OnItemClickListener {
    abstract void loadFinished(final Uri uri, final Cursor cursor);
    abstract void loadReset(final Loader<Cursor> loader);

    @Override
    public void onLoadFinished(final Uri uri, final Cursor cursor) {
        loadFinished(uri, cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loadReset(loader);
    }
}
