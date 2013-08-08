package com.magicallinone.app.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.SplashFragment;
import com.magicallinone.app.services.ApiService;

public class SplashActivity extends BaseFragmentActivity {

	private IntentFilter mFilter;
	private SplashReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		final FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.content_frame,
				SplashFragment.newInstance(),
				SplashFragment.class.getCanonicalName());
		fragmentTransaction.commit();

		createReceiver();
		startApiService();
	}

	private void createReceiver() {
		mFilter = new IntentFilter(ApiService.Actions.SETS_LOAD);
		mFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new SplashReceiver();
	}

	public class SplashReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					MainActivity.newInstance(SplashActivity.this);
					SplashActivity.this.finish();
					overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
				}
			}, 2000);
		}
	}

	private void startApiService() {
		Intent intent = new Intent(this, ApiService.class);
		intent.putExtra(ApiService.Extras.QUERY, "SETS");
		intent.putExtra(ApiService.Extras.OPERATION,
				ApiService.Operations.SET_LIST);
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
