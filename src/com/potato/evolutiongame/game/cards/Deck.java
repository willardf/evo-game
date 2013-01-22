package com.potato.evolutiongame.game.cards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;

import com.potato.evolutiongame.R;

public class Deck {
	private static ArrayList<Card> cards;
	private ArrayList<Card> deck;
	
	public Deck(boolean isEmpty)
	{
		deck = new ArrayList<Card>();
		if (!isEmpty)
		{
			deck.addAll(cards);
		}
	}
	
	public void shuffle()
	{
		Random r = new Random();
		for (int i = 0; i < deck.size(); i++)
		{
			int swap = r.nextInt(deck.size());
			Card temp = deck.get(i);
			deck.set(i, deck.get(swap));
			deck.set(swap, temp);
		}
	}
	
	public Card drawCard()
	{
		Card c = deck.get(deck.size() - 1);
		deck.remove(deck.size() - 1);
		return c;
	}
	public void placeCard(Card c)
	{
		deck.add(c);
	}
	
	public static void Initialize(Context ctx) throws NotFoundException, IOException
	{
		Resources res = ctx.getResources();		
		cards = new ArrayList<Card>();
		addBodyCard(res, "Wings", R.drawable.card1, new CardTag[]{CardTag.Flying}, 1);
		addBodyCard(res, "Fins", R.drawable.card1, new CardTag[]{CardTag.Aquatic}, 0);
		addBodyCard(res, "Fur", R.drawable.card1, new CardTag[]{CardTag.Cold}, 0);
	}
	private static void addBodyCard(Resources res, String title, int resId, CardTag[] tags, int size) throws NotFoundException, IOException
	{
		cards.add(new Card(cards.size(), title, 
				((BitmapDrawable)res.getDrawable(resId)).getBitmap(), 
				tags, CardGroup.BodyPart, size));
	}
	
	public static Card getCardInstance(int idx)
	{
		return cards.get(idx);
	}
}
