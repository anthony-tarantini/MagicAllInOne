package com.magicallinone.app.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.SetsListFragment;

public class SetsListActivity extends BaseFragmentActivity {

	public static final class Extras {
		public static final String DECK_ID = "deck_id";
	}

	public static void newInstance(Context context, int deckId) {
		Intent intent = new Intent(context, SetsListActivity.class);
		intent.putExtra(Extras.DECK_ID, deckId);
		context.startActivity(intent);
	}

	private int mDeckId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sets_list);

		Intent intent = getIntent();
		if (intent != null) {
			mDeckId = intent.getIntExtra(Extras.DECK_ID, -1);
		}

		final FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.content_frame,
				SetsListFragment.newInstance(mDeckId),
				SetsListFragment.class.getCanonicalName());
		fragmentTransaction.commit();

		ActionBar supportActionBar = getActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		supportActionBar.setHomeButtonEnabled(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == RequestCodes.ADD_CARD_SET_REQUEST
				&& resultCode == RESULT_OK)
			finish();
	}
}
