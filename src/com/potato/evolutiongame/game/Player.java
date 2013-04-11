package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.potato.evolutiongame.game.cards.PlayerCard;
import com.potato.evolutiongame.game.cards.PlayerDeck;

public class Player implements Serializable{
	private static final long serialVersionUID = -7645297394213829127L;
	public static final int MAX_CARDS_HAND = 5;
	public static final int STARTING_POPULATION = 10;
	
	private int population;
	private Creature board;
	private ArrayList<PlayerCard> hand;
	
	public Player()
	{
		population = STARTING_POPULATION;
		hand = new ArrayList<PlayerCard>();
		// TODO: Real starting creatures
		board = new Creature(3);
	}
	public Player(JSONObject o) {
		JSONArray parts = o.getJSONArray("hand");
		hand = new ArrayList<PlayerCard>();
		for (int i = 0; i < parts.length(); ++i)
			hand.add(PlayerDeck.getCardInstance(parts.getInt(i)));
		
		board = new Creature(o.getJSONObject("creature"));
		population = o.getInt("population");
	}
	
	/**
	 * Sets card c to index i of this player's creature
	 * @param c The card to set.
	 * @param index The index to set card c.
	 * @return Displaced card, if any.
	 */
	public PlayerCard playBodyCard(PlayerCard c, int i)
	{
		return board.setBodyPart(c, i);
	}
	
	/**
	 * Removes and returns card from this player's hand.
	 * @param idx Index in hand
	 * @return Card at idx
	 * @throws IndexOutOfBoundsException When idx is < 0 or > length.
	 */
	public PlayerCard takeHandCard(int idx) throws IndexOutOfBoundsException
	{
		if (idx < 0 || idx >= hand.size()) throw new IndexOutOfBoundsException();
		return hand.remove(idx);
	}
	public void addHandCard(PlayerCard c) throws InvalidCardException
	{
		if (hand.size() == MAX_CARDS_HAND) throw new InvalidCardException();
		hand.add(c);
	}
	public ArrayList<PlayerCard> getHand()
	{
		return hand;
	}
	public JSONObject getJSONObject()
	{
		JSONObject o = new JSONObject();
		
		JSONArray parts = new JSONArray();
		for (PlayerCard c : hand) parts.put(c.getCardIdx());
		o.put("hand", parts);
		
		o.put("population", population);
		o.put("creature", board.getJSONObject());
		
		return o;
	}
	public PlayerCard getHandCard(int idx) {
		return hand.get(idx);
	}
	public Creature getCreature() {
		return board;
	}
	public int getPopulation() {
		return population;
	}
	public void changePopulation(int change) {
		population += change;
	}
}
