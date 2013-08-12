package com.magicallinone.app.fragment;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.magicallinone.app.R;
import com.magicallinone.app.activities.DeckbuilderActivity;
import com.magicallinone.app.datasets.DeckTable;
import com.magicallinone.app.managers.FontManager;
import com.magicallinone.app.models.Deck;
import com.magicallinone.app.providers.MagicContentProvider;
import com.magicallinone.app.services.ApiService;

public class DeckListFragment extends BaseFragment implements ViewBinder,
		LoaderCallbacks<Cursor>, OnItemClickListener {

	private static final String[] COLUMNS = { DeckTable.Columns.DECK_ID,
			DeckTable.Columns.NAME, DeckTable.Columns.DESCRIPTION,
			DeckTable.Columns.FORMAT, DeckTable.Columns.SIZE, };
	private static final int[] VIEWS = { R.id.list_item_deck_image,
			R.id.list_item_deck_title, R.id.list_item_deck_description,
			R.id.list_item_deck_format, R.id.list_item_deck_size, };

	private ProgressDialog mProgressDialog;
	private LoaderManager mLoaderManager;
	private ListView mListView;
	private TextView mEmptyView;
	private CursorLoader mCursorLoader;
	private SimpleCursorAdapter mAdapter;
	private AlertDialog mAlertDialog;

	public static DeckListFragment newInstance() {
		DeckListFragment fragment = new DeckListFragment();

		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_decklist, container,
				false);

		mLoaderManager = getLoaderManager();
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.list_item_deck, null, COLUMNS, VIEWS, 0);
		mAdapter.setViewBinder(this);

		mListView = (ListView) view
				.findViewById(R.id.fragment_decklist_list_view);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mEmptyView = (TextView) view
				.findViewById(R.id.fragment_decklist_no_items);

		loadDecklist();

		return view;
	}

	private void loadDecklist() {
		mLoaderManager.initLoader(0, null, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Deck deck = new Deck(
				(String) view.findViewById(R.id.list_item_deck_title).getTag(),
				(String) view.findViewById(R.id.list_item_deck_description)
						.getTag(),
				((Integer) view.findViewById(R.id.list_item_deck_size).getTag())
						.intValue(), (String) view.findViewById(
						R.id.list_item_deck_format).getTag(), ((Integer) view
						.findViewById(R.id.list_item_deck_image).getTag())
						.intValue());
		DeckbuilderActivity.newInstance(getActivity(), deck);
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		TextView textView;
		switch (view.getId()) {
		case R.id.list_item_deck_image:
			getImageLoader().loadImageFromResource((ImageView) view,
					R.drawable.ic_app_icon);
			view.setTag(cursor.getInt(columnIndex));
			return true;
		case R.id.list_item_deck_title:
		case R.id.list_item_deck_description:
			textView = (TextView) view;
			textView.setTypeface(FontManager.INSTANCE.getAppFont());
			textView.setText(cursor.getString(columnIndex));
			view.setTag(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_deck_format:
			textView = (TextView) view;
			textView.setTypeface(FontManager.INSTANCE.getAppFont());
			textView.setText(getActivity().getString(R.string.format)
					+ cursor.getString(columnIndex));
			view.setTag(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_deck_size:
			textView = (TextView) view;
			textView.setTypeface(FontManager.INSTANCE.getAppFont());
			textView.setText(getActivity().getString(R.string.size)
					+ cursor.getString(columnIndex));
			view.setTag(cursor.getInt(columnIndex));
			return true;
		}
		return false;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle("Loading Decks");
		mProgressDialog.setMessage("Stand By ...");
		mProgressDialog.show();

		mCursorLoader = new CursorLoader(getActivity(),
				MagicContentProvider.Uris.DECKS_URI, null, null, null, null);
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mProgressDialog.dismiss();
		if (mAdapter != null && cursor != null) {
			cursor.setNotificationUri(getActivity().getContentResolver(),
					MagicContentProvider.Uris.DECKS_URI);
			mAdapter.swapCursor(cursor);
		}
		if (cursor != null && cursor.moveToFirst()) {
			showDeckListScreen();
		} else {
			showNoDeckScreen();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (mAdapter != null) {
			mAdapter.swapCursor(null);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void showNoDeckScreen() {
		mListView.setVisibility(View.GONE);
		mEmptyView.setTypeface(FontManager.INSTANCE.getAppFont());
		mEmptyView.setVisibility(View.VISIBLE);
	}

	private void showDeckListScreen() {
		mListView.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.GONE);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.deck_list_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			showNewDeckDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showNewDeckDialog() {
		LayoutInflater inflater = getActivity().getLayoutInflater();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = inflater.inflate(R.layout.dialog_new_deck, null);
		final EditText titleEditText = ((EditText) view
				.findViewById(R.id.dialog_new_deck_title));
		titleEditText.setTypeface(FontManager.INSTANCE.getAppFont());
		final EditText descriptionEditText = ((EditText) view
				.findViewById(R.id.dialog_new_deck_description));
		descriptionEditText.setTypeface(FontManager.INSTANCE.getAppFont());
		final EditText formatEditText = ((EditText) view
				.findViewById(R.id.dialog_new_deck_format));
		formatEditText.setTypeface(FontManager.INSTANCE.getAppFont());
		Button cancelButton = (Button) view
				.findViewById(R.id.dialog_new_deck_cancel);
		cancelButton.setTypeface(FontManager.INSTANCE.getAppFont());
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAlertDialog != null) {
					mAlertDialog.dismiss();
				}
			}
		});
		Button submitButton = (Button) view
				.findViewById(R.id.dialog_new_deck_submit);
		submitButton.setTypeface(FontManager.INSTANCE.getAppFont());
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAlertDialog.dismiss();
				createNewDeck(titleEditText.getText().toString(),
						descriptionEditText.getText().toString(),
						formatEditText.getText().toString());
			}
		});
		builder.setView(view);
		mAlertDialog = builder.create();
		mAlertDialog.show();
	}

	protected void createNewDeck(String title, String description, String format) {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.putExtra(ApiService.Extras.OPERATION,
				ApiService.Operations.ADD_DECK);
		intent.putExtra(ApiService.Extras.TITLE, title);
		intent.putExtra(ApiService.Extras.DESCRIPTION, description);
		intent.putExtra(ApiService.Extras.FORMAT, format);
		getActivity().startService(intent);
	}
}
