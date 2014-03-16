package com.magicallinone.app.dialogfragments;

import android.app.DialogFragment;
import android.os.Bundle;

import com.xtremelabs.imageutils.ImageLoader;

public class BaseDialogFragment extends DialogFragment {

	private ImageLoader mLoader;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoader = ImageLoader.buildImageLoaderForFragment(this);
	}
	
	protected ImageLoader getImageLoader(){
		return mLoader;
	}
	
	@Override
	public void onDestroy() {
		mLoader.destroy();
		super.onDestroy();
	}
	
}
