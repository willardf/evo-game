package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import com.potato.evolutiongame.Communicator;
import com.potato.evolutiongame.game.ai.EvoGameAI;
import com.potato.evolutiongame.game.ai.RandomAgent;
import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.Deck;

public class GameState implements Serializable, JSONString {
	public interface BodyPartListener {
		public void onBodyPartCallback(int toPlay, GameState.BodyPartCallback callback);
	}
	public class BodyPartCallback {
		public final void BodyPartCallBack(int toPlay, int toReplace) throws CommunicationException, Exception{
			if (toPlay == -1 || toReplace == -1) return;
			Card c = players[playerTurn].takeHandCard(toPlay);
			Card t = players[playerTurn].playBodyCard(c, toReplace);
			discard.placeCard(t);
			finishTurn();
			if (gid != -1) Communicator.playBodyCard(oppName, toPlay, toReplace);
			else if (playerTurn != 0)
			{
				myTurn = true;
				if (ai == null) throw new Exception("Uninitialized AI.");
				ai.makeMove(GameState.this);
				myTurn = true;
			}
		}
	}
	ArrayList<BodyPartListener> bodypartListeners = new ArrayList<BodyPartListener>();
	public interface RefreshListener {
		public void onRefresh();
	}
	ArrayList<RefreshListener> refreshListeners = new ArrayList<RefreshListener>();
	
	private static final long serialVersionUID = 2575600538286270740L;
	private static final int DEFAULT_GOAL_POPULATION = 20;
	
	private long gid = -1;
	private String oppName;
	
	private Player[] players;
	private int playerTurn;
	private Environment environ;
	private Deck deck, discard;
	private boolean myTurn;
	private int popGoal;
	
	private EvoGameAI ai;
	
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
		myTurn = true;
		ai = new RandomAgent();
		
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
		
		myTurn = j.getBoolean("myTurn");
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
			players[player].addHandCard(deck.drawCard());
		} catch (InvalidCardException e) {
			// Totes not possible right now
		}
	}
	public void discardFromHand(int selectedCard) throws IndexOutOfBoundsException, CommunicationException {
		Card c = players[playerTurn].takeHandCard(selectedCard);
		discard.placeCard(c);
		if (gid != -1) Communicator.discardCard(oppName, selectedCard);
	}
	public void playCard(int idx) 
			throws IndexOutOfBoundsException, CommunicationException, NotMyTurnException
	{
		if (!myTurn) throw new NotMyTurnException();
		
		Card c = players[playerTurn].getHandCard(idx);
		Card t = null;
		switch (c.getGroup())
		{
			case BodyPart:
				for (BodyPartListener b : bodypartListeners)
					b.onBodyPartCallback(idx, new BodyPartCallback());
				return;
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
		
		finishTurn();
	}
	private void finishTurn()
	{
		drawCard(playerTurn);
		
		// Increment turn
		playerTurn = (playerTurn + 1) % 2;
		myTurn = !myTurn;
		for (RefreshListener r : refreshListeners) r.onRefresh();
	}
	
	public static GameState fromJSONString(String in) throws Exception
	{
		GameState output;
		JSONObject o = new JSONObject(in);
		output = new GameState(o);
		return output;
	}
	public boolean isYourTurn() { return myTurn; }
	public ArrayList<Card> getMyPlayerHand()
	{
		int idx = myTurn ? playerTurn : ((playerTurn + 1) % 2);
		return players[idx].getHand();
	}
	public Creature getMyPlayerCreature() {
		int idx = myTurn ? playerTurn : ((playerTurn + 1) % 2);
		return players[idx].getCreature();
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
		
		j.put("myTurn", myTurn);
		j.put("environ", environ.getJSONObject());
		
		return j.toString();
	}
	public String getOppName() {
		return oppName;
	}
	public long getGid() {
		return gid;
	}
	public void setOnBodyPartListener(BodyPartListener l) {
		bodypartListeners.add(l);
	}
	public void setOnRefreshListener(RefreshListener l) {
		refreshListeners.add(l);
	}
	public boolean isMyTurn()
	{
		return myTurn;
	}
	public void setAI(EvoGameAI a)
	{
		ai = a;
	}
}
