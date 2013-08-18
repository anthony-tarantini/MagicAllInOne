package com.magicallinone.app.fragment;

import android.app.LoaderManager.LoaderCallbacks;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.magicallinone.app.R;
import com.magicallinone.app.models.Deck;

public class DeckInformationFragment extends BaseFragment implements
		ViewBinder, OnItemClickListener, LoaderCallbacks<Cursor> {

	private static final String[] COLUMN_NAMES = {};
	private static final int[] VIEW_IDS = {
			R.id.fragment_deck_information_title,
			R.id.fragment_deck_information_description,
			R.id.fragment_deck_information_format,
			R.id.fragment_deck_information_image, };

	private Deck mDeck;
	private SimpleCursorAdapter mAdapter;

	public static final class Arguments {
		public static final String DECK = "deck";
	}

	public static DeckInformationFragment newInstance(Deck deck) {
		DeckInformationFragment fragment = new DeckInformationFragment();

		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		args.putParcelable(Arguments.DECK, deck);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDeck = getArguments().getParcelable(Arguments.DECK);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.fragment_deck_information, null, COLUMN_NAMES,
				VIEW_IDS, SimpleCursorAdapter.NO_SELECTION);
		mAdapter.setViewBinder(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_deck_information,
				container, false);

		TextView textView = (TextView) view
				.findViewById(R.id.fragment_deck_information_title);
		textView.setText(mDeck.name);

		textView = (TextView) view
				.findViewById(R.id.fragment_deck_information_description);
		textView.setText(mDeck.description);

		textView = (TextView) view
				.findViewById(R.id.fragment_deck_information_format);
		textView.setText(getActivity().getString(R.string.format)
				+ mDeck.format);

		textView = (TextView) view
				.findViewById(R.id.fragment_deck_information_size);

		textView.setText(getActivity().getString(R.string.size) + mDeck.size);

		ImageView imageView = (ImageView) view
				.findViewById(R.id.fragment_deck_information_image);
		getImageLoader().loadImageFromResource(imageView,
				R.drawable.ic_app_icon);

		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public boolean setViewValue(View arg0, Cursor arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}
}
