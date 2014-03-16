package com.magicallinone.app.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.magicallinone.app.R;
import com.magicallinone.app.activities.BaseFragmentActivity.RequestCodes;
import com.magicallinone.app.activities.CardListActivity;
import com.magicallinone.app.activities.SetsListActivity;
import com.magicallinone.app.datasets.SetTable;
import com.magicallinone.app.providers.MagicContentProvider;
import com.magicallinone.app.utils.ImageUtils;

public class SetsListLoaderFragment extends BaseLoaderFragment {

	private LoaderManager mLoaderManager;
	private SimpleCursorAdapter mAdapter;
	private CursorLoader mCursorLoader;
	private ListView mListView;
	private int mDeckId;

	private static final String[] COLUMNS = { SetTable.Columns.CODE, SetTable.Columns.CODE, };
	private static final int[] VIEWS = { R.id.list_item_set_symbol, R.id.list_item_set_logo, };
	
	public static final class Arguments {
		public static final String DECK_ID = "deck_id";
	}

	public static SetsListLoaderFragment newInstance(){
		return newInstance(-1);
	}
	
	public static SetsListLoaderFragment newInstance(final int deckId) {
		final SetsListLoaderFragment setsListFragment = new SetsListLoaderFragment();
		Bundle args = setsListFragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		args.putInt(Arguments.DECK_ID, deckId);
		setsListFragment.setArguments(args);
		return setsListFragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoaderManager = getLoaderManager();
		mDeckId = getArguments().getInt(Arguments.DECK_ID);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_sets_list, container, false);

		mListView = (ListView) view.findViewById(R.id.set_list);
        final Activity activity = getActivity();
        mAdapter = new SimpleCursorAdapter(activity, R.layout.list_item_set, null, COLUMNS, VIEWS, 0);
		mAdapter.setViewBinder(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mLoaderManager.initLoader(1, null, this);

		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
        final Activity activity = getActivity();
        mCursorLoader = new CursorLoader(activity, MagicContentProvider.Uris.SET_URI, null, null, null, SetTable.Columns.RELEASE_DATE + " DESC");
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> loader, Cursor cursor) {
		if (mAdapter != null && cursor != null)
			mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		if (mAdapter != null)
			mAdapter.swapCursor(null);
	}

	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        final View clickedItem = view.findViewById(R.id.list_item_set_symbol);
        final String tag = (String) clickedItem.getTag();
        final Activity activity = getActivity();
        if (activity instanceof SetsListActivity) {
            CardListActivity.newInstanceForResult(activity, tag, mDeckId, RequestCodes.ADD_CARD_SET_REQUEST);
        } else {
            CardListActivity.newInstance(activity, tag);
        }
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
        final String text = cursor.getString(columnIndex);
        String folder = null;
        switch (view.getId()) {
		case R.id.list_item_set_logo:
            folder = ImageUtils.Folders.LOGOS;
            break;
		case R.id.list_item_set_symbol:
            folder = ImageUtils.Folders.SET_SYMBOL;
			break;
		}

        if (folder != null && !folder.isEmpty()) {
            final ImageView imageView = (ImageView) view;
            final Activity activity = getActivity();
			view.setTag(text);
			imageView.setImageDrawable(ImageUtils.getDrawable(activity, folder, text));
            return true;
        }
		return false;
	}
}
