package com.magicallinone.app.views;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.magicallinone.app.R;
import com.magicallinone.app.listeners.CardMenuListener;
import com.magicallinone.app.models.CardTag;

public class DeckListContextMenu {
    public final static int MAX_CARDS = 4;
    public final static int MIN_CARDS = 1;

	private CardMenuListener mListener;
	private View mMenu;

	public DeckListContextMenu(Activity activity, CardMenuListener listener) {
		mListener = listener;
		mMenu = activity.getLayoutInflater().inflate(R.layout.list_item_overlay_menu, null, false);

        final View overlayRemove = mMenu.findViewById(R.id.list_item_overlay_menu_remove);
        final View overlayMinus = mMenu.findViewById(R.id.list_item_overlay_menu_minus);
        final View overlayPlus = mMenu.findViewById(R.id.list_item_overlay_menu_plus);
        final View overlayInformation = mMenu.findViewById(R.id.list_item_overlay_menu_information);

		overlayRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRemoveSelected();
            }
        });
        overlayMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMinusSelected();
            }
        });
        overlayPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlusSelected();
            }
        });
        overlayInformation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {mListener.onInformationSelected();
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

	public View getMenu(final CardTag cardTag) {
        final int quantity = cardTag.getQuantity();
		toggleMinusVisibility(quantity);
		togglePlusVisibility(quantity);
		return mMenu;
	}

	public void toggleVisibilities(final int quantity) {
		toggleMinusVisibility(quantity);
		togglePlusVisibility(quantity);
	}

	private void toggleMinusVisibility(final int quantity) {
        final boolean lowestQuantity = quantity <= MIN_CARDS;
		final int visibility = lowestQuantity ? View.INVISIBLE : View.VISIBLE;
        final View overlayMinus = mMenu.findViewById(R.id.list_item_overlay_menu_minus);
        overlayMinus.setVisibility(visibility);
	}

	private void togglePlusVisibility(final int quantity) {
        final boolean highestQuantity = quantity >= MAX_CARDS;
		final int visibility = highestQuantity ? View.INVISIBLE : View.VISIBLE;
        final View overlayPlus = mMenu.findViewById(R.id.list_item_overlay_menu_plus);
        overlayPlus.setVisibility(visibility);
	}
}
