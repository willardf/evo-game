package com.potato.evolutiongame.game;

public class GameEntry {
	private String opponent;
	private boolean yourTurn;
	private long opponentId;
	private long id;
	
	public GameEntry(long gid, String op, long opId, boolean turn)
	{
		id = gid;
		opponent = op;
		opponentId = opId;
		yourTurn = turn;
	}
	public static GameEntry parseGameEntry(String in) throws Exception
	{
		String[] split = in.split(",");
		if (split.length != 4) throw new Exception("Invalid Game Entry");
		
		long gid = Long.parseLong(split[0]);
		long opId = Long.parseLong(split[2]);
		boolean turn = Boolean.parseBoolean(split[3]);
		
		return new GameEntry(gid, split[1], opId, turn);
	}
	
	@Override
	public String toString()
	{
		return opponent;
	}
	
	public String getOpponent() {
		return opponent;
	}
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	public boolean isYourTurn() {
		return yourTurn;
	}
	public void setYourTurn(boolean yourTurn) {
		this.yourTurn = yourTurn;
	}
	public long getOpponentId() {
		return opponentId;
	}
	public void setOpponentId(long opponentId) {
		this.opponentId = opponentId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
