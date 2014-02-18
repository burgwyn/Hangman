package com.natburgwyn.hangman;

import java.util.ArrayList;
import java.util.Date;

public class Game {
	
	private long id;
	private String word;
	private int length;
	private Date createDate;
	private Date lastEditDate;
	private Date endDate;
	private ArrayList<String> guesses;
	
	public Game(){
		guesses = new ArrayList<String>();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getLastEditDate() {
		return lastEditDate;
	}
	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public ArrayList<String> getGuesses(){
		return this.guesses;
	}
	
	public void addGuess(String guess){
		this.guesses.add(guess);
	}

}
