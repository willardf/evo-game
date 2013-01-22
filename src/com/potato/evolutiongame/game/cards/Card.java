package com.potato.evolutiongame.game.cards;

import java.io.IOException;
import java.util.ArrayList;

import com.potato.evolutiongame.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class Card {
	
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
}
