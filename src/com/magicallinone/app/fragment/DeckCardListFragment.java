package com.magicallinone.app.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.magicallinone.app.R;
import com.magicallinone.app.activities.SetsListActivity;
import com.magicallinone.app.datasets.CardsView;
import com.magicallinone.app.datasets.DeckCardTable;
import com.magicallinone.app.datasets.DeckListView;
import com.magicallinone.app.listeners.CardMenuListener;
import com.magicallinone.app.models.CardTag;
import com.magicallinone.app.providers.MagicContentProvider;
import com.magicallinone.app.services.ApiService;
import com.magicallinone.app.views.DeckListContextMenu;
import com.magicallinone.app.utils.ImageUtils;

import java.util.List;
import java.util.regex.MatchResult;

public class DeckCardListFragment extends BaseFragment implements ViewBinder,
		LoaderCallbacks<Cursor>, OnItemClickListener, CardMenuListener {

	private int mDeckId;
	private Button mAddCardButton;
	private View mCurrentMenuView;

	public static final class Arguments {
		public static final String DECK_ID = "deck_id";
	}

	public static final class Keys {
		public static final int SET_ID = 1;
		public static final int CARD_NUMBER = 2;
	}

    public static final int DECREMENT = -1;
    public static final int INCREMENT = 1;

	public static final String[] COLUMNS = { DeckListView.Columns.NAME, DeckListView.Columns.MANA_COST, DeckListView.Columns.QUANTITY, };
	public static final int[] VIEWS = { R.id.list_item_deck_card_name, R.id.list_item_deck_card_mana_cost_layout, R.id.list_item_deck_card_quantity, };

	private ProgressDialog mProgressDialog;
	private LoaderManager mLoaderManager;
	private ListView mListView;
	private TextView mEmptyView;
	private CursorLoader mCursorLoader;
	private SimpleCursorAdapter mAdapter;
	private DeckListContextMenu mMenu;
	private OnClickListener mOnAddClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SetsListActivity.newInstance(getActivity(), mDeckId);
		}
	};

	public static DeckCardListFragment newInstance(int deckId) {
		final DeckCardListFragment fragment = new DeckCardListFragment();

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
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_deck_card_list, container, false);

        final Activity activity = getActivity();

        mAdapter = new SimpleCursorAdapter(activity, R.layout.list_item_deck_card, null, COLUMNS, VIEWS, SimpleCursorAdapter.NO_SELECTION);
		mAdapter.setViewBinder(this);
		mListView = (ListView) view.findViewById(R.id.fragment_deck_card_list_list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mEmptyView = (TextView) view.findViewById(R.id.fragment_deck_card_list_no_items);
		mLoaderManager = getLoaderManager();

		mAddCardButton = (Button) view.findViewById(R.id.fragment_deck_card_list_add);
		mAddCardButton.setOnClickListener(mOnAddClickListener);

		mMenu = new DeckListContextMenu(activity, this);

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
	public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
		final String selection = DeckListView.Columns.DECK_ID + " = ?";
		final String[] selectionArgs = { String.valueOf(mDeckId), };
		final String orderBy = CardsView.Columns.NUMBER + " ASC";
        final Activity activity = getActivity();
        mCursorLoader = new CursorLoader(activity, MagicContentProvider.Uris.DECK_LIST_URI, null, selection, selectionArgs, orderBy);
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
		mProgressDialog.dismiss();
		if (mAdapter != null && cursor != null) {
            final Activity activity = getActivity();
            final ContentResolver contentResolver = activity.getContentResolver();
            cursor.setNotificationUri(contentResolver, MagicContentProvider.Uris.DECK_CARD_URI);
            mAdapter.swapCursor(cursor);
		}
		if (cursor != null && cursor.moveToFirst()) {
			showDeckCardsScreen();
		} else {
			showNoCardsScreen();
		}
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		if (mAdapter != null) {
			mAdapter.swapCursor(null);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void showNoCardsScreen() {
		mListView.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.VISIBLE);
	}

	private void showDeckCardsScreen() {
		mListView.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.GONE);
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		final TextView textView;
		final LinearLayout linearLayout;
        final Activity activity = getActivity();
        switch (view.getId()) {
		case R.id.list_item_deck_card_name:
			textView = (TextView) view;
			textView.setText(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_deck_card_mana_cost_layout:
			final List<MatchResult> manaSymbols = ImageUtils.getManaSymbols(cursor.getString(columnIndex));
			LayoutInflater inflater = activity.getLayoutInflater();
			linearLayout = (LinearLayout) view;
			linearLayout.removeAllViews();
			if (manaSymbols != null) {
				ImageUtils.addManaSymbols(activity, inflater,
						linearLayout, manaSymbols);
			}
			return true;
		case R.id.list_item_deck_card_quantity:
			textView = (TextView) view;
			textView.setTag(CardTag.createCardTag(cursor));
			textView.setText(activity.getString(R.string.x) + cursor.getString(columnIndex));
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mCurrentMenuView != view) {
			if (mCurrentMenuView != null) {
                removeMenuView(mCurrentMenuView);
            }
            final FrameLayout frameLayout = (FrameLayout) view;
            final View cardView = view.findViewById(R.id.list_item_deck_card_quantity);
            final CardTag cardTag = (CardTag) cardView.getTag();
            final View menu = mMenu.getMenu(cardTag);
            frameLayout.addView(menu);
			mCurrentMenuView = view;
		} else {
			removeMenuView(view);
			mCurrentMenuView = null;
		}
	}

	private void removeMenuView(View view) {
        final FrameLayout frameLayout = (FrameLayout) view;
        final View overlayMenu = view.findViewById(R.id.list_item_overlay_menu);
        frameLayout.removeView(overlayMenu);
	}

	@Override
	public void onDestroy() {
		mOnAddClickListener = null;
		super.onDestroy();
	}

	@Override
	public void onRemoveSelected() {
        final View quantityView = mCurrentMenuView.findViewById(R.id.list_item_deck_card_quantity);
        final CardTag cardTag = (CardTag) quantityView.getTag();
		removeCard(cardTag.getId());
	}

    private void changeCardCount(final int change) {
        final View quantityView = mCurrentMenuView.findViewById(R.id.list_item_deck_card_quantity);
        final CardTag cardTag = (CardTag) quantityView.getTag();
        final int quantity = cardTag.getQuantity() + change;
        final int id = cardTag.getId();
        updateCardCount(id, quantity);
        mMenu.toggleVisibilities(quantity);
    }

	@Override
	public void onMinusSelected() {
        changeCardCount(DECREMENT);
	}

	@Override
	public void onPlusSelected() {
        changeCardCount(INCREMENT);
	}

	@Override
	public void onInformationSelected() {
//		CardTag cardTag = (CardTag) mCurrentMenuView.findViewById(
//				R.id.list_item_deck_card_quantity).getTag();
	}

	private void updateCardCount(int cardId, int quantity) {
        final Activity activity = getActivity();
        final Intent intent = new Intent(activity, ApiService.class);
		intent.putExtra(ApiService.Extras.QUANTITY, quantity);
		intent.putExtra(ApiService.Extras.OPERATION, ApiService.Operations.UPDATE_COUNT);
		intent.putExtra(ApiService.Extras.QUERY, String.valueOf(cardId));
		intent.putExtra(ApiService.Extras.COLUMN, DeckCardTable.Columns.CARD_ID);
		activity.startService(intent);
	}

	private void removeCard(int cardId) {
        final Activity activity = getActivity();
        final Intent intent = new Intent(activity, ApiService.class);
		intent.putExtra(ApiService.Extras.OPERATION, ApiService.Operations.REMOVE_CARD);
		intent.putExtra(ApiService.Extras.QUERY, String.valueOf(cardId));
		intent.putExtra(ApiService.Extras.COLUMN, DeckCardTable.Columns.CARD_ID);
		activity.startService(intent);
	}
}
