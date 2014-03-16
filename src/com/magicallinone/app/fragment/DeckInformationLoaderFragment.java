package com.magicallinone.app.fragment;

import android.app.Activity;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.magicallinone.app.R;
import com.magicallinone.app.models.Deck;
import com.xtremelabs.imageutils.ImageLoader;

public class DeckInformationLoaderFragment extends BaseLoaderFragment {

	private static final String[] COLUMN_NAMES = {};
	private static final int[] VIEW_IDS = {R.id.fragment_deck_information_title, R.id.fragment_deck_information_description, R.id.fragment_deck_information_format, R.id.fragment_deck_information_image, };

	private Deck mDeck;
	private SimpleCursorAdapter mAdapter;

	public static final class Arguments {
		public static final String DECK = "deck";
	}

	public static DeckInformationLoaderFragment newInstance(final Deck deck) {
		final DeckInformationLoaderFragment fragment = new DeckInformationLoaderFragment();

		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		args.putParcelable(Arguments.DECK, deck);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDeck = getArguments().getParcelable(Arguments.DECK);

        final Activity activity = getActivity();
        mAdapter = new SimpleCursorAdapter(activity, R.layout.fragment_deck_information, null, COLUMN_NAMES, VIEW_IDS, SimpleCursorAdapter.NO_SELECTION);
		mAdapter.setViewBinder(this);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_deck_information, container, false);

        final Activity activity = getActivity();

		final TextView information = (TextView) view.findViewById(R.id.fragment_deck_information_title);
        information.setText(mDeck.name);

		final TextView description = (TextView) view.findViewById(R.id.fragment_deck_information_description);
        description.setText(mDeck.description);

		final TextView format = (TextView) view.findViewById(R.id.fragment_deck_information_format);
        final String formatText= activity.getString(R.string.format) + mDeck.format;
        format.setText(formatText);

		final TextView size = (TextView) view.findViewById(R.id.fragment_deck_information_size);
        final String sizeText = activity.getString(R.string.size) + mDeck.size;
        size.setText(sizeText);

		final ImageView imageView = (ImageView) view.findViewById(R.id.fragment_deck_information_image);
        final ImageLoader imageLoader = getImageLoader();
		imageLoader.loadImageFromResource(imageView, R.drawable.ic_app_icon);

		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
		return null;
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
	}

	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
}
