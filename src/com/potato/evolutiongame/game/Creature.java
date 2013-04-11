package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.potato.evolutiongame.game.cards.PlayerCard;
import com.potato.evolutiongame.game.cards.CardTag;
import com.potato.evolutiongame.game.cards.PlayerDeck;

public class Creature implements Serializable {
	private static final long serialVersionUID = 4394858417541893022L;
	private PlayerCard[] bodyParts;
	
	public Creature(int numParts){
		bodyParts = new PlayerCard[numParts];
	}
	
	public Creature(JSONObject o) {
		JSONArray parts = o.getJSONArray("bodyparts");
		bodyParts = new PlayerCard[parts.length()];
		for (int i = 0; i < parts.length(); ++i)
			bodyParts[i] = PlayerDeck.getCardInstance(parts.getInt(i));
	}
	
	public int getSize()
	{
		int output = 0;
		for(PlayerCard c : bodyParts)
		{
			if (c == null) continue;
			output += c.getSize();
		}
		return output;
	}
	public CardTag[] getTags()
	{
		ArrayList<CardTag> output = new ArrayList<CardTag>();
		for(PlayerCard c : bodyParts)
		{
			if (c == null) continue;
			CardTag[] tags = c.getTags();
			for (CardTag t : tags)
			{
				if (!output.contains(t))
				{
					output.add(t);
				}
			}
		}
		CardTag[] out = new CardTag[output.size()];
		output.toArray(out);
		return out;
	}
	public JSONObject getJSONObject()
	{
		JSONObject o = new JSONObject();
		
		JSONArray parts = new JSONArray();
		for (PlayerCard c : bodyParts)
			parts.put(c != null ? c.getCardIdx() : -1);
		o.put("bodyparts", parts);
		
		return o;
	}

	public PlayerCard setBodyPart(PlayerCard c, int index) {
		PlayerCard output = bodyParts[index];
		bodyParts[index] = c;
		return output;
	}
	public ArrayList<PlayerCard> getCards()
	{
		ArrayList<PlayerCard> l = new ArrayList<PlayerCard>();
		for (PlayerCard b : bodyParts)
			l.add(b);
		return l;
	}
}
