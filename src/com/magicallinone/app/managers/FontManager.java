package com.magicallinone.app.managers;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.magicallinone.app.application.MagicApplication;

public enum FontManager {

	INSTANCE;

	private Typeface mAppFont;

	private FontManager(){
		AssetManager assetManager = MagicApplication.getContext().getResources().getAssets();
		 mAppFont = Typeface.createFromAsset(assetManager, "fonts/BebasNeue.otf");
	}

	public Typeface getAppFont(){
		return mAppFont;
	}
}
