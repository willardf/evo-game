package com.potato.evolutiongame.game.cards;

import android.graphics.Bitmap;

public abstract class Card {
	protected int cardIdx;
	protected CardTag[] tags;
	protected Bitmap image;
	protected String name;
	protected CardGroup group;
	
	public int getCardIdx() { 
		return cardIdx; 
	}
	public CardTag[] getTags() {
		return tags;
	}
	public Bitmap getImage() {
		return image;
	}
	public String getName() {
		return name;
	}
	public CardGroup getGroup() {
		return group;
	}
}
