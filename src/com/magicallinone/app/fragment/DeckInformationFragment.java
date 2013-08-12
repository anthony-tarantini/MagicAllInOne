package com.magicallinone.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magicallinone.app.R;
import com.magicallinone.app.managers.FontManager;
import com.magicallinone.app.models.Deck;

public class DeckInformationFragment extends BaseFragment {

	private Deck mDeck;

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_deck_information,
				container, false);

		TextView textView = (TextView) view
				.findViewById(R.id.fragment_deck_information_title);
		textView.setTypeface(FontManager.INSTANCE.getAppFont());
		textView.setText(mDeck.name);

		textView = (TextView) view
				.findViewById(R.id.fragment_deck_information_description);
		textView.setTypeface(FontManager.INSTANCE.getAppFont());
		textView.setText(mDeck.description);

		textView = (TextView) view
				.findViewById(R.id.fragment_deck_information_format);
		textView.setTypeface(FontManager.INSTANCE.getAppFont());
		textView.setText(getActivity().getString(R.string.format)
				+ mDeck.format);

		textView = (TextView) view
				.findViewById(R.id.fragment_deck_information_size);
		textView.setTypeface(FontManager.INSTANCE.getAppFont());
		textView.setText(getActivity().getString(R.string.size) + mDeck.size);

		ImageView imageView = (ImageView) view
				.findViewById(R.id.fragment_deck_information_image);
		getImageLoader().loadImageFromResource(imageView, R.drawable.ic_app_icon);

		return view;
	}
}
