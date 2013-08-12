package com.magicallinone.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.xtremelabs.imageutils.ImageLoader;

public class BaseFragmentActivity extends FragmentActivity {
	
	public static final class RequestCodes{
		public static final int ADD_CARD_SET_REQUEST = 1;
	}
	
	private ImageLoader mLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoader = ImageLoader.buildImageLoaderForActivity(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected ImageLoader getImageLoader(){
		return mLoader;
	}
	
	@Override
	protected void onDestroy() {
		mLoader.destroy();
		super.onDestroy();
	}
}
