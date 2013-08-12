package com.magicallinone.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Deck implements Parcelable {
	public int deck_id;
	public String name;
	public String description;
	public int size;
	public String format;
	
	public Deck(String name, String description, int size, String format) {
		this(name, description, size, format, -1);
	}
	
	public Deck(String name, String description, int size, String format, int id){
		this.name = name;
		this.description = description;
		this.size = size;
		this.format = format;
		this.deck_id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.name, this.description,
				this.format });
		dest.writeIntArray(new int[] { this.deck_id, this.size });
	}

	public static final Parcelable.Creator<Deck> CREATOR = new Parcelable.Creator<Deck>() {
		public Deck createFromParcel(Parcel src) {
			return new Deck(src);
		}

		public Deck[] newArray(int size) {
			return new Deck[size];
		}
	};

	private Deck(Parcel src){
		String[] stringData = new String[3];
		int[] intData = new int[2];
		src.readStringArray(stringData);
		src.readIntArray(intData);
		
		this.name = stringData[0];
		this.description = stringData[1];
		this.format = stringData[2];
		this.deck_id = intData[0];
		this.size = intData[1];
	}
}
