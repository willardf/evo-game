package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.CardTag;
import com.potato.evolutiongame.game.cards.Deck;

public class Creature implements Serializable {
	private static final long serialVersionUID = 4394858417541893022L;
	private Card[] bodyParts;
	
	public Creature(int numParts){
		bodyParts = new Card[numParts];
	}
	
	public Creature(JSONObject o) {
		JSONArray parts = o.getJSONArray("bodyparts");
		bodyParts = new Card[parts.length()];
		for (int i = 0; i < parts.length(); ++i)
			bodyParts[i] = Deck.getCardInstance(parts.getInt(i));
	}
	
	public int getSize()
	{
		int output = 0;
		for(Card c : bodyParts)
		{
			output += c.getSize();
		}
		return output;
	}
	public CardTag[] getTags()
	{
		ArrayList<CardTag> output = new ArrayList<CardTag>();
		for(Card c : bodyParts)
		{
			CardTag[] tags = c.getTags();
			for (CardTag t : tags)
			{
				if (!output.contains(t))
				{
					output.add(t);
				}
			}
		}
		return (CardTag[])output.toArray();
	}
	public JSONObject getJSONObject()
	{
		JSONObject o = new JSONObject();
		
		JSONArray parts = new JSONArray();
		for (Card c : bodyParts)
			parts.put(c != null ? c.getCardIdx() : -1);
		o.put("bodyparts", parts);
		
		return o;
	}

	public Card setBodyPart(Card c, int index) {
		Card output = bodyParts[index];
		bodyParts[index] = c;
		return output;
	}
}
