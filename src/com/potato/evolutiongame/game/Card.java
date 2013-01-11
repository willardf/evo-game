package com.potato.evolutiongame.game;

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
	private String cardText;
	private static ArrayList<Card> cards; 
	public static void Initialize(Context ctx) throws NotFoundException, IOException
	{
		Resources res = ctx.getResources();
		cards = new ArrayList<Card>();
		cards.add(new Card("card1", ((BitmapDrawable)res.getDrawable(R.drawable.card1)).getBitmap(), "text"));
	}
	public static Card getCard(int idx)
	{
		return cards.get(idx);
	}
	public Card(String cardTitle, Bitmap i, String text) throws IOException
	{
		cardIdx = cards.size();
		name = cardTitle;
	    image = i;
	    cardText = text;
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
	public String getCardText() {
		return cardText;
	}
	
}
