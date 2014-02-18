package com.natburgwyn.hangman;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class GameManager {

	private static final String TAG = "GameManager";
	private Context context;
	private Game game;

	private String word;
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getGuesses() {
		return guesses;
	}

	public void setGuesses(int guesses) {
		this.guesses = guesses;
	}

	public int getIncorrectGuesses() {
		return incorrectGuesses;
	}

	public void setIncorrectGuesses(int incorectGuesses) {
		this.incorrectGuesses = incorectGuesses;
	}

	private int guesses;
	private int correctGuesses;
	private int incorrectGuesses;
	private ArrayList<String> guessLetters;
	private ArrayList<String> correctGuessLetters;
	private ArrayList<String> incorrectGuessLetters;
	private long gameId;
	private String displayWord;

	public GameManager(Context context){

		this.context = context;
		this.game = new Game();
		
		this.setGuesses(0);
		this.setCorrectGuesses(0);
		this.setIncorrectGuesses(0);
		this.setGuessLetters(new ArrayList<String>());
		this.setCorrectGuessLetters(new ArrayList<String>());
		this.setIncorrectGuessLetters(new ArrayList<String>());

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		this.setWord(getWord(Integer.valueOf(prefs.getString("length", "4"))));

		String display = new String();

		for(int i =0; i < this.getWord().length(); i++){
			display +=  "*";
		}

		this.setDisplayWord(display);

		final DBHelper dh = new DBHelper(context);
		
		new AsyncTask<String,Void,Long>(){
			@Override
			protected Long doInBackground(String... params){
				return dh.insertGame(params[0]);
			}
			@Override
			protected void onPostExecute(Long l){
				setGameId(l);
			}
		}.execute(getWord());
		
		Log.i(TAG, String.valueOf(this.getGameId()));
	}

	public GameManager(Context context, long gameId){

		this.context = context;
		this.game = new Game();

		this.setGuesses(0);
		this.setIncorrectGuesses(0);
		this.setCorrectGuesses(0);
		this.setGuessLetters(new ArrayList<String>());
		this.setCorrectGuessLetters(new ArrayList<String>());
		this.setIncorrectGuessLetters(new ArrayList<String>());
		
		final DBHelper dh = new DBHelper(context);
		
		new AsyncTask<Long,Void,Game>(){
			@Override
			protected Game doInBackground(Long... arg) {
				return dh.getGame(arg[0]);
			}
			@Override
            protected void onPostExecute(Game g){
				game = g;
				Log.i(TAG, game.getWord());

				setWord(game.getWord());

				Iterator<String> i = game.getGuesses().iterator();

				while(i.hasNext()){

					try {
						String guess = i.next();
						Log.i(TAG, guess);
						processGuess(guess, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				String display = new String();

				for(int j =0; j < getWord().length(); j++){
					display +=  "*";
				}

				setDisplayWord(display);
			}
		}.execute(gameId);

		/*
		Log.i(TAG, game.getWord());

		this.setWord(game.getWord());

		Iterator<String> i = game.getGuesses().iterator();

		while(i.hasNext()){

			try {
				String guess = i.next();
				Log.i(TAG, guess);
				this.processGuess(guess, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String display = new String();

		for(int j =0; j < this.getWord().length(); j++){
			display +=  "*";
		}

		this.setDisplayWord(display);
		*/
	}

	public void processGuess(String guess, boolean save) throws Exception {

		if (guessLetters.contains(guess)){
			throw new Exception();
		}else{

			if (save){
				final DBHelper dh = new DBHelper(context);
				Log.i("gameId", String.valueOf(this.getGameId()));

				Turn turn = new Turn();
				turn.setGameId(this.getGameId());
				turn.setGuess(guess);
				
				new AsyncTask<Turn,Void,Void>(){
				      @Override
				      protected Void doInBackground(Turn... params) {
				    	  Turn t = (Turn)params[0];
				    	  dh.insertTurn(t.getGameId(), t.getGuess());
				    	  return null;
				     }
				 }.execute(turn);

			}

			guessLetters.add(guess);
			this.setGuesses(this.getGuesses() +1);

			if (word.contains(guess)) {

				this.setCorrectGuesses(this.getCorrectGuesses() + 1);
				this.correctGuessLetters.add(guess);

				this.setDisplayWord(this.getWord().replaceAll("[^" + this.getLettersInList(this.getCorrectGuessLetters()) + "]", "*"));

			} else {

				this.setIncorrectGuesses(this.getIncorrectGuesses() + 1);
				incorrectGuessLetters.add(guess);
			}
		}
	}

	public String getLettersInList(ArrayList<String> list){
		
		String letters = "";

		for(int i =0; i < list.size(); i++){
			letters += list.get(i);
		}

		return letters;
	}

	private String getWord(int length){

		String result ="";

		switch(length){

		case 4:
			result = getRandomString(context.getResources().getStringArray(R.array.words_4));
			break;			
		case 5:
			result = getRandomString(context.getResources().getStringArray(R.array.words_5));
			break;
		case 6:
			result = getRandomString(context.getResources().getStringArray(R.array.words_6));
			break;
		case 7:
			result = getRandomString(context.getResources().getStringArray(R.array.words_7));
			break;
		case 8:
			result = getRandomString(context.getResources().getStringArray(R.array.words_8));
			break;
		}

		return result;
	}

	public boolean isGameOver(){
		return this.getIncorrectGuesses() >= 6;
	}

	public boolean isMatch(){
		return this.getWord().equalsIgnoreCase(this.getDisplayWord());
	}

	private String getRandomString(String[] array){
		String result ="";

		Random random = new Random();
		result = array[random.nextInt(array.length)];

		return result;
	}

	public String getDisplayWord() {
		return displayWord;
	}

	public void setDisplayWord(String displayWord) {
		this.displayWord = displayWord;
	}

	public ArrayList<String> getGuessLetters() {
		return guessLetters;
	}

	public void setGuessLetters(ArrayList<String> guessLetters) {
		this.guessLetters = guessLetters;
	}

	public ArrayList<String> getIncorrectGuessLetters() {
		return incorrectGuessLetters;
	}

	public void setIncorrectGuessLetters(ArrayList<String> incorrectGuessLetters) {
		this.incorrectGuessLetters = incorrectGuessLetters;
	}

	public int getCorrectGuesses() {
		return correctGuesses;
	}

	public void setCorrectGuesses(int correctGuesses) {
		this.correctGuesses = correctGuesses;
	}

	public ArrayList<String> getCorrectGuessLetters() {
		return correctGuessLetters;
	}

	public void setCorrectGuessLetters(ArrayList<String> correctGuessLetters) {
		this.correctGuessLetters = correctGuessLetters;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public void quitGame(){
		new AsyncTask<Long,Void,Void>(){
			@Override
			protected Void doInBackground(Long... params) {
				DBHelper dh = new DBHelper(context);
				dh.quitGame(params[0]);
				return null;
			}
		}.execute(this.getGameId());
	}

	public void winGame(){
		new AsyncTask<Long,Void,Void>(){
			@Override
			protected Void doInBackground(Long... params) {
				DBHelper dh = new DBHelper(context);
				dh.winGame(params[0]);
				return null;
			}
		}.execute(this.getGameId());
	}

	public void loseGame(){
		new AsyncTask<Long,Void,Void>(){
			@Override
			protected Void doInBackground(Long... params) {
				DBHelper dh = new DBHelper(context);
				dh.loseGame(params[0]);
				return null;
			}
		}.execute(this.getGameId());
	}

}
