package com.potato.evolutiongame.game.test;

import com.potato.evolutiongame.game.CommunicationException;
import com.potato.evolutiongame.game.GameState;
import com.potato.evolutiongame.game.cards.Card;

import junit.framework.TestCase;

public class GameStateTest extends TestCase {

	GameState sut;
	protected void setUp() throws Exception {
		super.setUp();
		sut = new GameState();
	}

	public void testDrawPlayerCard() throws IndexOutOfBoundsException, CommunicationException {
		testDiscardFromHand(); //Make room for card
		int lastCard = sut.getCurrentPlayerHand().size();
		//assertFalse(c.getCardIdx() == sut.getCurrentPlayerHand().get(0).getCardIdx());
	}

	public void testDrawEnvironmentCard() {
		fail("Not yet implemented");
	}

	public void testDiscardFromHand() throws IndexOutOfBoundsException, CommunicationException {
		Card c = sut.getCurrentPlayerHand().get(0);
		sut.discardFromHand(0);
		assertFalse(c.getCardIdx() == sut.getCurrentPlayerHand().get(0).getCardIdx());
	}

	public void testPlayCard() {
		fail("Not yet implemented");
	}

}
