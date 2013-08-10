package com.magicallinone.app.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.magicallinone.app.R;
import com.magicallinone.app.activities.CardListActivity;
import com.magicallinone.app.datasets.SetTable;
import com.magicallinone.app.providers.MagicContentProvider;
import com.magicallinone.app.utils.ImageUtils;

public class SetsListFragment extends BaseFragment implements
		OnItemClickListener, ViewBinder, LoaderManager.LoaderCallbacks<Cursor> {

	private LoaderManager mLoaderManager;
	private SimpleCursorAdapter mAdapter;
	private CursorLoader mCursorLoader;
	private ListView mListView;

	private static final String[] COLUMNS = { SetTable.Columns.CODE,
			SetTable.Columns.CODE, };
	private static final int[] VIEWS = { R.id.list_item_set_symbol,
			R.id.list_item_set_logo, };

	public static SetsListFragment newInstance() {
		SetsListFragment setsListFragment = new SetsListFragment();
		Bundle args = setsListFragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		setsListFragment.setArguments(args);
		return setsListFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoaderManager = getLoaderManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sets_list, container,
				false);

		mListView = (ListView) view.findViewById(R.id.set_list);
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.list_item_set, null, COLUMNS, VIEWS, 0);
		mAdapter.setViewBinder(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mLoaderManager.initLoader(1, null, this);

		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		mCursorLoader = new CursorLoader(getActivity(),
				MagicContentProvider.Uris.SET_URI, null, null, null,
				SetTable.Columns.RELEASE_DATE + " DESC");
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (mAdapter != null && cursor != null)
			mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (mAdapter != null)
			mAdapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		String tag = (String) view.findViewById(R.id.list_item_set_symbol)
				.getTag();
		CardListActivity.newInstance(getActivity(), tag);
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		ImageView imageView;
		switch (view.getId()) {
		case R.id.list_item_set_logo:
			imageView = (ImageView) view;
			view.setTag(cursor.getString(columnIndex));
			imageView.setImageDrawable(ImageUtils.getDrawable(getActivity(),
					ImageUtils.Folders.LOGOS, cursor.getString(columnIndex)));
			return true;
		case R.id.list_item_set_symbol:
			imageView = (ImageView) view;
			view.setTag(cursor.getString(columnIndex));
			imageView.setImageDrawable(ImageUtils.getDrawable(getActivity(),
					ImageUtils.Folders.SET_SYMBOL, cursor.getString(columnIndex)));
			return true;
		}
		return false;
	}
}
