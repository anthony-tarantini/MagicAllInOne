package com.magicallinone.app.models;

import android.database.Cursor;

import com.magicallinone.app.datasets.DeckListView;

public class CardTag {
	private String setId;
	private int number;
	private int id;
	private int quantity;
	
	public static CardTag createCardTag(Cursor cursor){
		CardTag cardTag = new CardTag();
		cardTag.setSetId(cursor.getString(cursor.getColumnIndex(DeckListView.Columns.SET_ID)));
		cardTag.setNumber(cursor.getInt(cursor.getColumnIndex(DeckListView.Columns.NUMBER)));
		cardTag.setId(cursor.getInt(cursor.getColumnIndex(DeckListView.Columns.CARD_ID)));
		cardTag.setQuantity(cursor.getInt(cursor.getColumnIndex(DeckListView.Columns.QUANTITY)));
		return cardTag;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
