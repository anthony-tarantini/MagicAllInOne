package com.magicallinone.app.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.CardListFragment;

public class CardListActivity extends BaseFragmentActivity {

	public static final class Extras {
		public static final String SET = "set";
	}

	private String mSetId;
	
	public static void newInstance(final Context context, final String set) {
		final Intent intent = new Intent(context, CardListActivity.class);
		intent.putExtra(Extras.SET, set);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_list);
		
		mSetId = getIntent().getExtras().getString(Extras.SET);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.content_frame, CardListFragment.newInstance(mSetId), CardListFragment.class.getCanonicalName());
		fragmentTransaction.commit();
	}
}
