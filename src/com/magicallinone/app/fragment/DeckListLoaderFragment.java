package com.magicallinone.app.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.magicallinone.app.R;
import com.magicallinone.app.activities.DeckbuilderActivity;
import com.magicallinone.app.datasets.DeckTable;
import com.magicallinone.app.models.Deck;
import com.magicallinone.app.providers.MAIOContentProvider;
import com.magicallinone.app.services.ApiService;

public class DeckListLoaderFragment extends BaseLoaderFragment {

	private static final String[] COLUMNS = { DeckTable.Columns._ID, DeckTable.Columns.NAME, DeckTable.Columns.DESCRIPTION, DeckTable.Columns.FORMAT, DeckTable.Columns.SIZE, };
	private static final int[] VIEWS = { R.id.list_item_deck_image, R.id.list_item_deck_title, R.id.list_item_deck_description, R.id.list_item_deck_format, R.id.list_item_deck_size, };

	private ProgressDialog mProgressDialog;
	private LoaderManager mLoaderManager;
	private ListView mListView;
	private TextView mEmptyView;
	private CursorLoader mCursorLoader;
	private SimpleCursorAdapter mAdapter;
	private AlertDialog mAlertDialog;

	public static DeckListLoaderFragment newInstance() {
		final DeckListLoaderFragment fragment = new DeckListLoaderFragment();

		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle("Loading Decks");
		mProgressDialog.setMessage("Stand By ...");
		mProgressDialog.show();
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_decklist, container, false);

		mLoaderManager = getLoaderManager();
        final Activity activity = getActivity();
        mAdapter = new SimpleCursorAdapter(activity, R.layout.list_item_deck, null, COLUMNS, VIEWS, 0);
		mAdapter.setViewBinder(this);

		mListView = (ListView) view.findViewById(R.id.fragment_decklist_list_view);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mEmptyView = (TextView) view.findViewById(R.id.fragment_decklist_no_items);

		loadDecklist();

		return view;
	}

	private void loadDecklist() {
		mLoaderManager.initLoader(0, null, this);
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final View deckTitleView = view.findViewById(R.id.list_item_deck_title);
        final View deckDescriptionView = view.findViewById(R.id.list_item_deck_description);
        final View deckSizeView = view.findViewById(R.id.list_item_deck_size);
        final View deckFormatView = view.findViewById(R.id.list_item_deck_format);
        final View deckImageView = view.findViewById(R.id.list_item_deck_image);

        final String deckTitle = (String) deckTitleView.getTag();
        final String deckDescription = (String) deckDescriptionView.getTag();
        final String deckFormat = (String) deckFormatView.getTag();
        final int deckSize = ((Integer) deckSizeView.getTag()).intValue();
        final int deckImage = ((Integer) deckImageView.getTag()).intValue();

        final Deck deck = new Deck(deckTitle, deckDescription, deckSize, deckFormat, deckImage);

		DeckbuilderActivity.newInstance(getActivity(), deck);
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		final TextView textView;
        final ImageView imageView;
        final String text;
        final Activity activity = getActivity();
        switch (view.getId()) {
		case R.id.list_item_deck_image:
            imageView = (ImageView) view;
			getImageLoader().loadImageFromResource(imageView, R.drawable.ic_app_icon);
			view.setTag(cursor.getInt(columnIndex));
			return true;
		case R.id.list_item_deck_title:
		case R.id.list_item_deck_description:
			textView = (TextView) view;
			textView.setText(cursor.getString(columnIndex));
			view.setTag(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_deck_format:
			textView = (TextView) view;
            text = activity.getString(R.string.format) + cursor.getString(columnIndex);
            textView.setText(text);
			view.setTag(cursor.getString(columnIndex));
			return true;
		case R.id.list_item_deck_size:
			textView = (TextView) view;
            text = activity.getString(R.string.size) + cursor.getString(columnIndex);
            textView.setText(text);
			view.setTag(cursor.getInt(columnIndex));
			return true;
		}
		return false;
	}

	@Override
	public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final Activity activity = getActivity();
        mCursorLoader = new CursorLoader(activity, MAIOContentProvider.Uris.DECKS_URI, null, null, null, null);
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
		mProgressDialog.dismiss();
		if (mAdapter != null && cursor != null) {
            final Activity activity = getActivity();
            final ContentResolver contentResolver = activity.getContentResolver();
            cursor.setNotificationUri(contentResolver, MAIOContentProvider.Uris.DECKS_URI);
			mAdapter.swapCursor(cursor);
		}
		if (cursor != null && cursor.moveToFirst()) {
			showDeckListScreen();
		} else {
			showNoDeckScreen();
		}
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		if (mAdapter != null) {
			mAdapter.swapCursor(null);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void showNoDeckScreen() {
		mListView.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.VISIBLE);
	}

	private void showDeckListScreen() {
		mListView.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.GONE);
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.deck_list_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			showNewDeckDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showNewDeckDialog() {
		final LayoutInflater inflater = getActivity().getLayoutInflater();

		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final View view = inflater.inflate(R.layout.dialog_new_deck, null);
		final EditText titleEditText = ((EditText) view.findViewById(R.id.dialog_new_deck_title));
		final EditText descriptionEditText = ((EditText) view.findViewById(R.id.dialog_new_deck_description));
		final EditText formatEditText = ((EditText) view.findViewById(R.id.dialog_new_deck_format));
		final Button cancelButton = (Button) view.findViewById(R.id.dialog_new_deck_cancel);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAlertDialog != null) {
					mAlertDialog.dismiss();
				}
			}
		});
		final Button submitButton = (Button) view.findViewById(R.id.dialog_new_deck_submit);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                final String title = titleEditText.getText().toString();
                final String description = descriptionEditText.getText().toString();
                final String format = formatEditText.getText().toString();
				mAlertDialog.dismiss();
                createNewDeck(title, description, format);
			}
		});
		builder.setView(view);
		mAlertDialog = builder.create();
		mAlertDialog.show();
	}

	protected void createNewDeck(final String title, final String description, final String format) {
        final Activity activity = getActivity();
        final Intent intent = new Intent(activity, ApiService.class);
		intent.putExtra(ApiService.Extras.OPERATION, ApiService.Operations.ADD_DECK);
		intent.putExtra(ApiService.Extras.TITLE, title);
		intent.putExtra(ApiService.Extras.DESCRIPTION, description);
		intent.putExtra(ApiService.Extras.FORMAT, format);
		activity.startService(intent);
	}
}
