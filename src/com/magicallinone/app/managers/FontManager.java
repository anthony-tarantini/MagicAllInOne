package com.magicallinone.app.managers;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.magicallinone.app.application.MAIOApplication;

public enum FontManager {

	INSTANCE;

	private Typeface mAppFont;

	private FontManager(){
		final AssetManager assetManager = MAIOApplication.getContext().getResources().getAssets();
        mAppFont = Typeface.createFromAsset(assetManager, "fonts/BebasNeue.otf");
	}

	public Typeface getAppFont(){
		return mAppFont;
	}
}
