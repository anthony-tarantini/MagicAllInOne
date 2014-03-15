package com.magicallinone.app.views;

import com.magicallinone.app.managers.FontManager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class BebasNeueTextView extends TextView {

	public BebasNeueTextView(Context context) {
		super(context);
		init();
	}

	public BebasNeueTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BebasNeueTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setTypeface(FontManager.INSTANCE.getAppFont());
	}

}
