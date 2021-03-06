package com.potato.evolutiongame.game.ai;

import java.util.Random;

import com.potato.evolutiongame.game.GameState;
import com.potato.evolutiongame.game.GameState.BodyPartCallback;
import com.potato.evolutiongame.game.cards.PlayerCard;

public class RandomAgent implements EvoGameAI {
	@Override
	public int chooseCard(GameState g) {
		int len = g.getCurrentPlayerHand().size();
		return new Random().nextInt(len);
	}

	@Override
	public int chooseBodyPart(GameState g, PlayerCard c) {
		int len = g.getCurrentPlayerCreature().getCards().size();
		return new Random().nextInt(len);
	}

	@Override
	public void makeMove(GameState gameState) throws Exception {
		BodyPartCallback b = gameState.new BodyPartCallback();
		b.BodyPartCallBack(chooseCard(gameState), chooseBodyPart(gameState, null));
	}
}
