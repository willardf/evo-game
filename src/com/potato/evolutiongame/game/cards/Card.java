package com.potato.evolutiongame.game.cards;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;

public class Card implements Serializable{
	private static final long serialVersionUID = -2291865297562975886L;
	private int cardIdx;
	private String name;
	private Bitmap image;
	private CardTag[] tags;
	private CardGroup group;
	private int size;
	
	public Card(int idx, String cardTitle, Bitmap i, CardTag[] t, CardGroup c, int s) throws IOException
	{ 
		cardIdx = idx;
		name = cardTitle;
	    image = i;
	    tags = t;
	    group = c;
	    size = s;
	}
	
	public int getSize()
	{
		return size;
	}
	public int getCardIdx() {
		return cardIdx;
	}
	public String getName() {
		return name;
	}
	public Bitmap getImage() {
		return image;
	}
	public CardTag[] getTags() {
		return tags;
	}
	public CardGroup getGroup() {
		return group;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(cardIdx);
    }
	
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    	cardIdx = in.readInt();
    	Card c = Deck.getCardInstance(cardIdx);
    	size = c.getSize();
    	name = c.getName();
    	image = c.getImage();
    	tags = c.getTags();
    	group = c.getGroup();
    }
}
