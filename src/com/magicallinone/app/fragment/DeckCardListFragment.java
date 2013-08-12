package com.magicallinone.app.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.magicallinone.app.R;
import com.magicallinone.app.activities.SetsListActivity;
import com.magicallinone.app.datasets.CardsView;
import com.magicallinone.app.datasets.DeckListView;
import com.magicallinone.app.managers.FontManager;
import com.magicallinone.app.providers.MagicContentProvider;
import com.magicallinone.app.utils.ImageUtils;

public class DeckCardListFragment extends BaseFragment implements ViewBinder,
		LoaderCallbacks<Cursor>, OnItemClickListener {

	private int mDeckId;
	private Button mAddCardButton;

	public static final class Arguments {
		public static final String DECK_ID = "deck_id";
	}

	public static final String[] COLUMNS = { DeckListView.Columns.NAME,
			DeckListView.Columns.MANA_COST, DeckListView.Columns.QUANTITY, };
	public static final int[] VIEWS = { R.id.list_item_deck_card_name,
			R.id.list_item_deck_card_mana_cost_layout,
			R.id.list_item_deck_card_quantity, };

	private ProgressDialog mProgressDialog;
	private LoaderManager mLoaderManager;
	private ListView mListView;
	private TextView mEmptyView;
	private CursorLoader mCursorLoader;
	private SimpleCursorAdapter mAdapter;
	private OnClickListener mOnAddClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SetsListActivity.newInstance(getActivity(), mDeckId);
		}
	};

	public static DeckCardListFragment newInstance(int deckId) {
		DeckCardListFragment fragment = new DeckCardListFragment();

		Bundle args = fragment.getArguments();
		if (args == null)
			args = new Bundle();

		args.putInt(Arguments.DECK_ID, deckId);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle("Loading Cards");
		mProgressDialog.setMessage("Stand By ...");
		mProgressDialog.show();

		mDeckId = getArguments().getInt(Arguments.DECK_ID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_deck_card_list,
				container, false);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.list_item_deck_card, null, COLUMNS, VIEWS,
				SimpleCursorAdapter.NO_SELECTION);
		mAdapter.setViewBinder(this);
		mListView = (ListView) view
				.findViewById(R.id.fragment_deck_card_list_list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mEmptyView = (TextView) view
				.findViewById(R.id.fragment_deck_card_list_no_items);
		mLoaderManager = getLoaderManager();

		mAddCardButton = (Button) view
				.findViewById(R.id.fragment_deck_card_list_add);
		mAddCardButton.setTypeface(FontManager.INSTANCE.getAppFont());
		mAddCardButton.setOnClickListener(mOnAddClickListener);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadCards();
	}

	private void loadCards() {
		mLoaderManager.initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String selection = DeckListView.Columns.DECK_ID + " = ?";
		String[] selectionArgs = { String.valueOf(mDeckId), };
		String orderBy = CardsView.Columns.NUMBER + " ASC";
		mCursorLoader = new CursorLoader(getActivity(),
				MagicContentProvider.Uris.DECK_LIST_URI, null, selection,
				selectionArgs, orderBy);
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mProgressDialog.dismiss();
		if (mAdapter != null && cursor != null) {
			cursor.setNotificationUri(getActivity().getContentResolver(),
					MagicContentProvider.Uris.DECK_CARD_URI);
			mAdapter.swapCursor(cursor);
		}
		if (cursor != null && cursor.moveToFirst()) {
			showDeckCardsScreen();
		} else {
			showNoCardsScreen();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (mAdapter != null) {
			mAdapter.swapCursor(null);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void showNoCardsScreen() {
		mListView.setVisibility(View.GONE);
		mEmptyView.setTypeface(FontManager.INSTANCE.getAppFont());
		mEmptyView.setVisibility(View.VISIBLE);
	}

	private void showDeckCardsScreen() {
		mListView.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.GONE);
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		TextView textView;
		LinearLayout linearLayout;
		switch (view.getId()) {
		case R.id.list_item_deck_card_name:
			textView = (TextView) view;
			textView.setTypeface(FontManager.INSTANCE.getAppFont(),
					Typeface.BOLD);
			textView.setTextSize(18);
			textView.setText(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_deck_card_mana_cost_layout:
			List<MatchResult> manaSymbols = new ArrayList<MatchResult>();
			ImageUtils.getManaSymbols(cursor.getString(columnIndex),
					manaSymbols);
			LayoutInflater inflater = getActivity().getLayoutInflater();
			linearLayout = (LinearLayout) view;
			linearLayout.removeAllViews();
			if (manaSymbols != null) {
				ImageUtils.addManaSymbols(getActivity(), inflater,
						linearLayout, manaSymbols);
			}
			return true;
		case R.id.list_item_deck_card_quantity:
			textView = (TextView) view;
			textView.setTypeface(FontManager.INSTANCE.getAppFont());
			textView.setTextSize(20);
			textView.setText(getActivity().getString(R.string.x) + cursor.getString(columnIndex));
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public void onDestroy() {
		mOnAddClickListener = null;
		super.onDestroy();
	}
}
