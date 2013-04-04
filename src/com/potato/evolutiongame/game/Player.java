package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.Deck;

public class Player implements Serializable{
	private static final long serialVersionUID = -7645297394213829127L;
	public static final int MAX_CARDS_HAND = 5;
	public static final int STARTING_POPULATION = 10;
	
	private int population;
	private Creature board;
	private ArrayList<Card> hand;
	
	public Player()
	{
		population = STARTING_POPULATION;
		hand = new ArrayList<Card>();
		// TODO: Real starting creatures
		board = new Creature(3);
	}
	public Player(JSONObject o) {
		JSONArray parts = o.getJSONArray("hand");
		hand = new ArrayList<Card>();
		for (int i = 0; i < parts.length(); ++i)
			hand.add(Deck.getCardInstance(parts.getInt(i)));
		
		board = new Creature(o.getJSONObject("creature"));
		population = o.getInt("population");
	}
	// Returns displaced card, if any
	public Card playBodyCard(Card c, int index)
	{
		return board.setBodyPart(c, index);
	}
	public Card takeCard(int idx) throws IndexOutOfBoundsException
	{
		if (idx < 0 || idx >= hand.size()) throw new IndexOutOfBoundsException();
		Card c = hand.get(idx);
		hand.remove(idx);
		return c;
	}
	public void addCard(Card c) throws InvalidCardException
	{
		if (hand.size() == MAX_CARDS_HAND) throw new InvalidCardException();
		hand.add(c);
	}
	public ArrayList<Card> getHand()
	{
		return hand;
	}
	public JSONObject getJSONObject()
	{
		JSONObject o = new JSONObject();
		
		JSONArray parts = new JSONArray();
		for (Card c : hand) parts.put(c.getCardIdx());
		o.put("hand", parts);
		
		o.put("population", population);
		o.put("creature", board.getJSONObject());
		
		return o;
	}
}
