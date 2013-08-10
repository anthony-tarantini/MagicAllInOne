package com.magicallinone.app.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.CardListFragment;
import com.magicallinone.app.services.ApiService;

public class CardListActivity extends BaseFragmentActivity {

	public static final class Extras {
		public static final String SET = "set";
	}

	public class UpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			mCardListFragment.loadSet();
			mProgressDialog.dismiss();
		}
	}

	private String mSetId;
	private IntentFilter mFilter;
	private UpdateReceiver mReceiver;
	private CardListFragment mCardListFragment;
	private ProgressDialog mProgressDialog;
	
	public static void newInstance(final Context context, final String set) {
		final Intent intent = new Intent(context, CardListActivity.class);
		intent.putExtra(Extras.SET, set);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_list);
		
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("Loading Cards");
		mProgressDialog.setMessage("Stand By ...");
		mProgressDialog.show();
		
		mSetId = getIntent().getExtras().getString(Extras.SET);
		
		mCardListFragment = CardListFragment.newInstance(mSetId);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.content_frame, mCardListFragment, CardListFragment.class.getCanonicalName());
		fragmentTransaction.commit();
		
		createReceiver();
		startApiService();
	}

	private void createReceiver() {
		mFilter = new IntentFilter(ApiService.Actions.SET_READ);
		mFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new UpdateReceiver();
	}

	private void startApiService() {
		Intent intent = new Intent(this, ApiService.class);
		intent.putExtra(ApiService.Extras.QUERY, mSetId);
		intent.putExtra(ApiService.Extras.OPERATION, ApiService.Operations.SINGLE_SET);
		startService(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		mReceiver = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		createReceiver();
		registerReceiver(mReceiver, mFilter);
	}
}
