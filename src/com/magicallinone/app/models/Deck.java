package com.magicallinone.app.models;

public class Deck {
	public int deck_id;
	public String name;
	public String description;
	public int size;
	public String format;
	
	public Deck (String name, String description, int size, String format){
		this.name = name;
		this.description = description;
		this.size = size;
		this.format = format;
	}
}
