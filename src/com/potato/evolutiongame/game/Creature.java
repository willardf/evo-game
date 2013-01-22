package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.CardTag;

public class Creature implements Serializable {
	private static final long serialVersionUID = 4394858417541893022L;
	private Card[] bodyParts;
	
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
}
