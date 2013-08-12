package com.magicallinone.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magicallinone.app.R;

public class SplashFragment extends BaseFragment {

	public static SplashFragment newInstance() {
		SplashFragment splashFragment = new SplashFragment();

		Bundle args = splashFragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		splashFragment.setArguments(args);
		return splashFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_splash, container, false);
		ImageView splashImage = (ImageView) view.findViewById(R.id.splash_image);
		getImageLoader().loadImageFromResource(splashImage, R.raw.mtg_logo);
		return view;
	}
}
