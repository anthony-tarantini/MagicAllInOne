package com.magicallinone.app.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.magicallinone.app.R;
import com.magicallinone.app.listeners.CardMenuListener;
import com.magicallinone.app.models.CardTag;

public class DeckListContextMenu {

	private CardMenuListener mListener;
	private View mMenu;

	public DeckListContextMenu(Activity activity, CardMenuListener listener) {
		mListener = listener;
		mMenu = activity.getLayoutInflater().inflate(
				R.layout.list_item_overlay_menu, null, false);
		mMenu.findViewById(R.id.list_item_overlay_menu_remove)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onRemoveSelected();
					}
				});
		mMenu.findViewById(R.id.list_item_overlay_menu_minus)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onMinusSelected();
					}
				});
		mMenu.findViewById(R.id.list_item_overlay_menu_plus)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onPlusSelected();
					}
				});
		mMenu.findViewById(R.id.list_item_overlay_menu_information)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onInformationSelected();
					}
				});
	}

	public void setCardMenuListener(CardMenuListener listener) {
		mListener = listener;
	}

	public CardMenuListener getCardMenuListener() {
		return mListener;
	}

	public void resetCardMenuListener() {
		mListener = null;
	}

	public View getMenu(CardTag cardTag) {
		toggleMinusVisibility(cardTag.getQuantity());
		togglePlusVisibility(cardTag.getQuantity());
		return mMenu;
	}

	public void toggleVisibilities(int quantity) {
		toggleMinusVisibility(quantity);
		togglePlusVisibility(quantity);
	}

	private void toggleMinusVisibility(int quantity) {
		final int visibility = quantity < 2 ? View.INVISIBLE : View.VISIBLE;
		mMenu.findViewById(R.id.list_item_overlay_menu_minus).setVisibility(
				visibility);
	}

	private void togglePlusVisibility(int quantity) {
		final int visibility = quantity > 3 ? View.INVISIBLE : View.VISIBLE;
		mMenu.findViewById(R.id.list_item_overlay_menu_plus).setVisibility(
				visibility);
	}
}
