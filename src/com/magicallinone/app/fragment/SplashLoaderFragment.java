package com.magicallinone.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magicallinone.app.R;
import com.xtremelabs.imageutils.ImageLoader;

public class SplashLoaderFragment extends BaseFragment {

	public static SplashLoaderFragment newInstance() {
		final SplashLoaderFragment splashFragment = new SplashLoaderFragment();

		Bundle args = splashFragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		splashFragment.setArguments(args);
		return splashFragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_splash, container, false);
		final ImageView splashImage = (ImageView) view.findViewById(R.id.splash_image);
        final ImageLoader imageLoader = getImageLoader();
        imageLoader.loadImageFromResource(splashImage, R.raw.mtg_logo);
		return view;
	}
}
