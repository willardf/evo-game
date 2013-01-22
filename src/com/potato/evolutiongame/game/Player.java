package com.potato.evolutiongame.game;

import java.util.ArrayList;

import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.CardGroup;

public class Player {
	public static final int MAX_CARDS_HAND = 5;
	
	private Creature board;
	private ArrayList<Card> hand;
	
	public Player()
	{
		hand = new ArrayList<Card>();
		board = new Creature();
	}
	// Returns displaced card, if any
	public Card playCard(Card c)
	{
		Card output = null;
		if (c.getGroup() == CardGroup.BodyPart)
		{
			
		}
		return output;
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
		
	}
	public ArrayList<Card> getHand()
	{
		return hand;
	}
}
