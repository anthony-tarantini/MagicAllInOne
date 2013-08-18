package com.magicallinone.app.ui;

import com.magicallinone.app.managers.FontManager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class BebasNeueEditText extends EditText {

	public BebasNeueEditText(Context context) {
		super(context);
		init();
	}

	public BebasNeueEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BebasNeueEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setTypeface(FontManager.INSTANCE.getAppFont());
	}
}
