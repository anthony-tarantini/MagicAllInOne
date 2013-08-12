package com.magicallinone.app.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.DeckCardListFragment;
import com.magicallinone.app.fragment.DeckInformationFragment;
import com.magicallinone.app.models.Deck;

public class DeckbuilderActivity extends BaseFragmentActivity {

	public static final class Extras {
		public static final String DECK = "deck";
	}

	private Deck mDeck;

	public static void newInstance(Context context, Deck deck) {
		Intent intent = new Intent(context, DeckbuilderActivity.class);
		intent.putExtra(Extras.DECK, deck);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deckbuilder);

		mDeck = getIntent().getParcelableExtra(Extras.DECK);

		final FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.activity_deckbuilder_information,
				DeckInformationFragment.newInstance(mDeck),
				DeckInformationFragment.class.getCanonicalName());
		fragmentTransaction.add(R.id.activity_deckbuilder_list,
				DeckCardListFragment.newInstance(mDeck.deck_id),
				DeckCardListFragment.class.getCanonicalName());
		fragmentTransaction.commit();

		ActionBar supportActionBar = getActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		supportActionBar.setHomeButtonEnabled(true);
	}
}
