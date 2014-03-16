package com.magicallinone.app.fragment;

import android.app.LoaderManager;
import android.database.Cursor;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter.ViewBinder;

public abstract class BaseLoaderFragment extends BaseFragment implements ViewBinder, LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener {
	
}
