package com.potato.evolutiongame.game.cards;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;

public class PlayerCard extends Card implements Serializable{
	private static final long serialVersionUID = -2291865297562975886L;
	private int size;
	
	public PlayerCard(int idx, String cardTitle, Bitmap i, CardTag[] t, CardGroup c, int s) throws IOException
	{ 
		cardIdx = idx;
		name = cardTitle;
	    image = i;
	    tags = t;
	    group = c;
	    size = s;
	}
	
	public int getSize(){
		return size;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(cardIdx);
    }
	
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    	cardIdx = in.readInt();
    	PlayerCard c = PlayerDeck.getCardInstance(cardIdx);
    	size = c.getSize();
    	name = c.getName();
    	image = c.getImage();
    	tags = c.getTags();
    	group = c.getGroup();
    }
}
