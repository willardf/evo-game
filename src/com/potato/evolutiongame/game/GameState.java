package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import com.potato.evolutiongame.Communicator;
import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.Deck;

public class GameState implements Serializable, JSONString {
	private static final long serialVersionUID = 2575600538286270740L;
	private static final int DEFAULT_GOAL_POPULATION = 20;
	
	public boolean isOnline;
	private long gid;
	private String oppName;
	
	private Player[] players;
	private int playerTurn;
	private Environment environ;
	private Deck deck, discard;
	private boolean yourTurn;
	private int popGoal;

	public GameState() {
		this(DEFAULT_GOAL_POPULATION);
	}
	public GameState(int goal)
	{
		popGoal = goal;
		
		players = new Player[2];
		players[0] = new Player();
		players[1] = new Player();
		oppName = "Computer";
		gid = -1;
		
		environ = new Environment();
		deck = new Deck(false);
		deck.shuffle();
		discard = new Deck(true);
		for (int i = 0; i < Player.MAX_CARDS_HAND; i++)
		{
			drawCard(0);
			drawCard(1);
		}
	}
	public GameState(JSONObject j)
	{
		
		JSONArray ply = j.getJSONArray("players");
		players = new Player[ply.length()];
		for (int i = 0; i < ply.length(); ++i)
			players[i] = new Player(ply.getJSONObject(i));
		

		playerTurn = j.getInt("playerTurn");
		deck = new Deck(j.getJSONArray("deck"));
		discard = new Deck(j.getJSONArray("discard"));
		
		popGoal = j.getInt("popGoal");
		gid = j.getLong("gid");
		oppName = j.getString("oppName");
		
		yourTurn = j.getBoolean("p1turn");
		environ = new Environment(j.getJSONObject("environ"));
	}

	public void drawCard(int player)
	{
		if (deck.count() == 0)
		{
			deck.placeDeck(discard);
			deck.shuffle();
		}
		try {
			players[player].addCard(deck.drawCard());
		} catch (InvalidCardException e) {
			// Totes not possible right now
		}
	}
	public void discardFromHand(int selectedCard) throws IndexOutOfBoundsException, CommunicationException {
		Card c = players[playerTurn].takeCard(selectedCard);
		discard.placeCard(c);
		if (isOnline) Communicator.discardCard(gid, selectedCard);
	}
	public void playCard(int idx) throws IndexOutOfBoundsException, CommunicationException
	{
		Card c = players[playerTurn].takeCard(idx);
		Card t = null;
		switch (c.getGroup())
		{
			case BodyPart:
				t = players[playerTurn].playBodyCard(c, 0);
				break;
			case Action:
				discard.placeCard(c);
				break;
			case Rainfall:
			case Temperature:
			case Catastrophy:
			case Feature:
				t = environ.playCard(c);
				break;
		}
		if (t != null) discard.placeCard(t);
		
		drawCard(playerTurn);
		
		// Increment turn
		playerTurn = (playerTurn + 1) % 2;
		yourTurn = !yourTurn;
		
		if (isOnline) Communicator.playCard(gid, idx);
	}
	
	public static GameState fromJSONString(String in) throws Exception
	{
		GameState output;
		JSONObject o = new JSONObject(in);
		output = new GameState(o);
		return output;
	}
	public boolean isYourTurn() { return yourTurn; }
	public ArrayList<Card> getMyPlayerHand()
	{
		int idx = yourTurn ? playerTurn : ((playerTurn + 1) % 2);
		return players[idx].getHand();
	}
	@Override
	public String toJSONString() {
		JSONObject j = new JSONObject();
		
		JSONArray ply = new JSONArray();
		for (Player p : players) ply.put(p.getJSONObject());
		j.put("players", ply);

		j.put("playerTurn", playerTurn);
		j.put("deck", deck.getJSONArray());
		j.put("discard", discard.getJSONArray());
		
		j.put("popGoal", popGoal);
		
		j.put("p1turn", yourTurn);
		j.put("environ", environ.getJSONObject());
		
		return j.toString();
	}
	public String getOppName() {
		return oppName;
	}
	public long getGid() {
		return gid;
	}
}
