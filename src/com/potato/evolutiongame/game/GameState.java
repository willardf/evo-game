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
import com.potato.evolutiongame.game.cards.EnvironmentCard;
import com.potato.evolutiongame.game.cards.EnvironmentDeck;
import com.potato.evolutiongame.game.cards.PlayerCard;
import com.potato.evolutiongame.game.cards.PlayerDeck;

public class GameState implements Serializable, JSONString {
	public interface BodyPartListener {
		public void onBodyPartCallback(int toPlay, GameState.BodyPartCallback callback);
	}
	public class BodyPartCallback {
		public final void BodyPartCallBack(int toPlay, int toReplace) throws CommunicationException, Exception{
			if (toPlay == -1 || toReplace == -1) return;
			PlayerCard c = getCurrentPlayer().takeHandCard(toPlay);
			PlayerCard t = getCurrentPlayer().playBodyCard(c, toReplace);
			playerDiscard.placeCard(t);
			finishTurn();
			if (gid != -1) Communicator.playBodyCard(oppName, toPlay, toReplace);
		}
	}
	ArrayList<BodyPartListener> bodypartListeners = new ArrayList<BodyPartListener>();
	public interface RefreshListener {
		public void onRefresh();
	}
	ArrayList<RefreshListener> refreshListeners = new ArrayList<RefreshListener>();
	
	private static final long serialVersionUID = 2575600538286270740L;
	private static final int DEFAULT_GOAL_POPULATION = 20;
	private static final int SIZE_BREADTH = 3;
	
	private long gid = -1;
	private String oppName;
	
	private Player[] players;
	private int playerTurn;
	private Environment environ;
	private PlayerDeck playerDeck, playerDiscard;
	private EnvironmentDeck enviroDeck, enviroDiscard;
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
		enviroDeck = new EnvironmentDeck(false);
		enviroDeck.shuffle();
		enviroDiscard = new EnvironmentDeck(true);
		
		playerDeck = new PlayerDeck(false);
		playerDeck.shuffle();
		playerDiscard = new PlayerDeck(true);
		for (int i = 0; i < Player.MAX_CARDS_HAND; i++)
		{
			drawPlayerCard(0);
			drawPlayerCard(1);
		}
	}
	public GameState(JSONObject j)
	{
		JSONArray ply = j.getJSONArray("players");
		players = new Player[ply.length()];
		for (int i = 0; i < ply.length(); ++i)
			players[i] = new Player(ply.getJSONObject(i));
		

		playerTurn = j.getInt("playerTurn");
		playerDeck = new PlayerDeck(j.getJSONArray("deck"));
		playerDiscard = new PlayerDeck(j.getJSONArray("discard"));
		
		enviroDeck = new EnvironmentDeck(j.getJSONArray("edeck"));
		enviroDiscard = new EnvironmentDeck(j.getJSONArray("ediscard"));
		
		popGoal = j.getInt("popGoal");
		gid = j.getLong("gid");
		oppName = j.getString("oppName");
		
		myTurn = j.getBoolean("myTurn");
		environ = new Environment(j.getJSONObject("environ"));
	}

	public void drawPlayerCard(int player)
	{
		if (playerDeck.count() == 0)
		{
			playerDeck.placeDeck(playerDiscard);
			playerDeck.shuffle();
		}
		try {
			players[player].addHandCard(playerDeck.drawCard());
		} catch (InvalidCardException e) {
			// Totes not possible right now
		}
	}
	public void drawEnvironmentCard()
	{
		if (enviroDeck.count() == 0)
		{
			enviroDeck.placeDeck(enviroDiscard);
			enviroDeck.shuffle();
		}
		Card c = environ.playCard(enviroDeck.drawCard());
		discardCard(c);
	}
	
	public void discardFromHand(int selectedCard) throws IndexOutOfBoundsException, CommunicationException {
		PlayerCard c = getCurrentPlayer().takeHandCard(selectedCard);
		playerDiscard.placeCard(c);
		if (gid != -1) Communicator.discardCard(oppName, selectedCard);
	}
	public void playCard(int idx) 
			throws IndexOutOfBoundsException, CommunicationException, 
				NotMyTurnException, Exception
	{
		if (!myTurn) throw new NotMyTurnException();
		
		Card c = getCurrentPlayer().getHandCard(idx);
		Card t = null;
		switch (c.getGroup())
		{
			case BodyPart:
				for (BodyPartListener b : bodypartListeners)
					b.onBodyPartCallback(idx, new BodyPartCallback());
				return;
			case Action:
				discardFromHand(idx);
				finishTurn();
				break;
			case Catastrophy:
			case Feature:
				t = environ.playCard(c);
				break;
		}
		discardCard(t);
	}
	
	private void discardCard(Card c) {
		if (c == null) return;
		if (c instanceof EnvironmentCard)
		{
			enviroDiscard.placeCard((EnvironmentCard)c);
		}
		else playerDiscard.placeCard((PlayerCard)c);
		
	}
	private void finishTurn() throws Exception
	{
		drawPlayerCard(playerTurn);

		int change = environ.calculatePopulationChange(getCurrentPlayer());
		getCurrentPlayer().changePopulation(change);
		
		// Increment turn
		playerTurn = (playerTurn + 1) % 2;
		if (gid != -1) myTurn = !myTurn;
		
		if (gid == -1 && playerTurn != 0)
		{
			myTurn = true;
			if (ai == null) throw new Exception("Uninitialized AI.");
			ai.makeMove(GameState.this);
			myTurn = true;

			drawEnvironmentCard();
		}
		
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
	public Player getCurrentPlayer() {
		int idx = myTurn ? playerTurn : ((playerTurn + 1) % 2);
		return players[idx];
	}
	public ArrayList<PlayerCard> getCurrentPlayerHand()
	{
		return getCurrentPlayer().getHand();
	}
	public Creature getCurrentPlayerCreature() {
		return getCurrentPlayer().getCreature();
	}
	
	@Override
	public String toJSONString() {
		JSONObject j = new JSONObject();
		
		JSONArray ply = new JSONArray();
		for (Player p : players) ply.put(p.getJSONObject());
		j.put("players", ply);

		j.put("playerTurn", playerTurn);
		j.put("deck", playerDeck.getJSONArray());
		j.put("discard", playerDiscard.getJSONArray());
		
		j.put("edeck", enviroDeck.getJSONArray());
		j.put("ediscard", enviroDiscard.getJSONArray());
		
		j.put("popGoal", popGoal);
		
		j.put("myTurn", myTurn);
		j.put("environ", environ.getJSONObject());
		
		return j.toString();
	}
	public Environment getEnvironment()
	{
		return environ;
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
	public int getGoalPopulation() {
		return popGoal;
	}
	public String getSizeText(int size) {
		int slot = size / SIZE_BREADTH;
		switch (slot)
		{
			case 0: return "Small";
			case 1: return "Medium";
			default: return "Large";
		}
	}
}
