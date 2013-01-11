package com.potato.evolutiongame.game;

import java.io.Serializable;

public class GameState implements Serializable {
	private static final long serialVersionUID = 2575600538286270740L;

	public static GameState parseGameState(String in) throws Exception
	{
		String[] split = in.split(",");
		//if (split.length != 5) throw new Exception();
		return new GameState();
	}
}
