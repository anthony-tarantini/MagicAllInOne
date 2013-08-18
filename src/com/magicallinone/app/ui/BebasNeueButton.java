package com.magicallinone.app.ui;

import com.magicallinone.app.managers.FontManager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class BebasNeueButton extends Button {

	public BebasNeueButton(Context context) {
		super(context);
		init();
	}

	public BebasNeueButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BebasNeueButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setTypeface(FontManager.INSTANCE.getAppFont());
	}
}