package com.natburgwyn.hangman;

import java.util.Date;

public class Turn {
	
	private long id;
	private long gameId;
	private String guess;
	private Date createDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getGameId() {
		return gameId;
	}
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}
	public String getGuess() {
		return guess;
	}
	public void setGuess(String guess) {
		this.guess = guess;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
