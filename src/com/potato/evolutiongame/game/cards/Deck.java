package com.potato.evolutiongame.game.cards;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;

import com.potato.evolutiongame.R;

public class Deck implements Serializable{
	private static final long serialVersionUID = -4513592919288316751L;
	private static ArrayList<Card> cards;
	private static Card emptyCard;
	private ArrayList<Card> deck;
	
	public Deck(boolean isEmpty)
	{
		deck = new ArrayList<Card>();
		if (!isEmpty)
		{
			deck.addAll(cards);
		}
	}
	public Deck(JSONArray arr)
	{
		deck = new ArrayList<Card>();
		for (int i = 0; i < arr.length(); ++i)
			deck.add(cards.get(arr.getInt(i)));
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
		if (c != null || c.getCardIdx() < 0) return; 
		deck.add(c);
	}
	public void placeDeck(Deck c)
	{
		while (c.count() > 0) deck.add(c.drawCard());
	}
	
	public int count(){ return deck.size(); }
	
	public static void Initialize(Context ctx) throws NotFoundException, IOException
	{	// Carnivore, Herbivore, Insectivore, Nectarivore, Flying, 
		// Cold, Hot, Dry, Wet, Aquatic, Rocky
		Resources res = ctx.getResources();
		emptyCard = new Card(-1, "Empty", 
				((BitmapDrawable)res.getDrawable(R.drawable.emptycard)).getBitmap(), 
				null, CardGroup.BodyPart, 0);
		
		cards = new ArrayList<Card>();
		addBodyCard(res, "Wings", R.drawable.wingscard, new CardTag[]{CardTag.Flying}, 1);
		addBodyCard(res, "Tail fin", R.drawable.tailfincard, new CardTag[]{CardTag.Aquatic}, 0);
		addBodyCard(res, "Hollow Fur", R.drawable.card1, new CardTag[]{CardTag.Cold}, 0);
		addBodyCard(res, "Flat Teeth", R.drawable.card1, new CardTag[]{CardTag.Herbivore}, 0);
		addBodyCard(res, "Canines", R.drawable.card1, new CardTag[]{CardTag.Carnivore}, 0);
		addBodyCard(res, "Cold blood", R.drawable.card1, new CardTag[]{CardTag.Hot}, 0);
		addBodyCard(res, "Split Hoof", R.drawable.card1, new CardTag[]{CardTag.Rocky}, 0);
		addBodyCard(res, "Air sac", R.drawable.card1, new CardTag[]{CardTag.Aquatic, CardTag.Flying}, 2);
		addBodyCard(res, "Poison Tentacles", R.drawable.poisontentaclescard, new CardTag[]{CardTag.Aquatic, CardTag.Prey}, 1);
		addBodyCard(res, "Suckered Tentacles", R.drawable.card1, new CardTag[]{CardTag.Wet}, 1);
		addBodyCard(res, "Regenerative Limbs", R.drawable.card1, new CardTag[]{CardTag.Prey, CardTag.Disease}, 0);
		addBodyCard(res, "Camouflage", R.drawable.card1, new CardTag[]{CardTag.Prey}, 0);
		
		addBodyCard(res, "pow", R.drawable.wingscard, new CardTag[]{CardTag.Flying}, 1);
		addBodyCard(res, "what", R.drawable.card1, new CardTag[]{CardTag.Aquatic}, 0);
		addBodyCard(res, "huh", R.drawable.tailfincard, new CardTag[]{CardTag.Cold}, 0);
		addBodyCard(res, "you", R.drawable.card1, new CardTag[]{CardTag.Flying}, 1);
		addBodyCard(res, "me", R.drawable.tailfincard, new CardTag[]{CardTag.Aquatic}, 0);
		addBodyCard(res, "bees", R.drawable.card1, new CardTag[]{CardTag.Cold}, 0);
	}
	private static void addBodyCard(Resources res, String title, int resId, CardTag[] tags, int size) throws NotFoundException, IOException
	{
		cards.add(new Card(cards.size(), title, 
				((BitmapDrawable)res.getDrawable(resId)).getBitmap(), 
				tags, CardGroup.BodyPart, size));
	}
	
	public static Card getCardInstance(int idx)
	{
		if (idx < 0)
			return emptyCard;
		return cards.get(idx);
	}
	
	public JSONArray getJSONArray() {
		JSONArray arr = new JSONArray();
		
		for (Card c : deck) arr.put(c.getCardIdx());
		
		return arr;
	}
}
