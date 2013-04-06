package com.potato.evolutiongame.game.ai;

import com.potato.evolutiongame.game.GameState;
import com.potato.evolutiongame.game.cards.Card;

public interface EvoGameAI {
	/**
	 * Call to make AI choose a card to play.
	 * @return Index of card chosen
	 */
	public int chooseCard(GameState g);
	/**
	 * Call to make AI choose creature's body part to be replaced.
	 * @param c The card replacing the body part
	 * @return Index of body part chosen
	 */
	public int chooseBodyPart(GameState g, Card c);
	public void makeMove(GameState gameState) throws Exception;
}
