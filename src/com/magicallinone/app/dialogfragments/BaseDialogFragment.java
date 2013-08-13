package com.magicallinone.app.dialogfragments;

import com.xtremelabs.imageutils.ImageLoader;

import android.app.DialogFragment;
import android.os.Bundle;

public class BaseDialogFragment extends DialogFragment {

	private ImageLoader mLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
