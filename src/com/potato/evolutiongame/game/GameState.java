package com.potato.evolutiongame.game;

import java.io.Serializable;

import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.Deck;

public class GameState implements Serializable {
	private static final long serialVersionUID = 2575600538286270740L;

	private Player[] players;
	private int playerCnt;
	private int playerTurn;
	private Environment e;
	private Deck deck, discard;
	private boolean yourTurn;
	
	public GameState(int numPlayers)
	{
		playerCnt = numPlayers;
		players = new Player[playerCnt];
		e = new Environment();
		deck = new Deck(false);
		deck.shuffle();
		discard = new Deck(true);
	}
		
	public void playCard(int idx) throws IndexOutOfBoundsException
	{
		
		Card c = players[playerTurn].takeCard(idx);
		discard.placeCard(c);
		
		e.playCard(c);
		players[playerTurn].playCard(c);
		
		// Increment turn
		playerTurn = (playerTurn + 1) % playerCnt;
		yourTurn = !yourTurn;
	}
	
	public static GameState parseGameState(String in) throws Exception
	{
		GameState output = new GameState(2);
		String[] split = in.split(",");
		//if (split.length != 5) throw new Exception();
		return output;
	}
	
	public void setYourTurn(boolean y) { yourTurn = y; }
	public boolean isYourTurn() { return yourTurn; }
}
