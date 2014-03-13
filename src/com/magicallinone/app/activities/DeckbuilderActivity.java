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

	public static void newInstance(final Context context, final Deck deck) {
		final Intent intent = new Intent(context, DeckbuilderActivity.class);
		intent.putExtra(Extras.DECK, deck);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deckbuilder);

		mDeck = getIntent().getParcelableExtra(Extras.DECK);

		final FragmentManager fragmentManager = getFragmentManager();
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final DeckInformationFragment deckInformationFragment = DeckInformationFragment.newInstance(mDeck);
        final DeckCardListFragment deckCardListFragment = DeckCardListFragment.newInstance(mDeck.deck_id);

		fragmentTransaction.add(R.id.activity_deckbuilder_information, deckInformationFragment, DeckInformationFragment.class.getCanonicalName());
        fragmentTransaction.add(R.id.activity_deckbuilder_list, deckCardListFragment, DeckCardListFragment.class.getCanonicalName());
		fragmentTransaction.commit();

		final ActionBar supportActionBar = getActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		supportActionBar.setHomeButtonEnabled(true);
	}
}
