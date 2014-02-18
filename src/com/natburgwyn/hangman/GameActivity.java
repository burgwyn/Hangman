package com.natburgwyn.hangman;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends FragmentActivity implements IGameEndListener, IGuessListener {
	
	public static final String GAME_ID = "gameId";
	
	public static final int GAME_REQUEST = 0;
	
	private static final String GUESS_DIALOG_FRAGMENT = "guessDialogFragment";
	private static final String GAME_END_DIALOG_FRAGMENT = "gameEndDialogFragment";
	
	private TextView txtWord;
	private TextView txtGuesses;
	private ImageView imgGameBoard;
	
	private GameManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		txtWord = (TextView)this.findViewById(R.id.txtWord);
		txtGuesses = (TextView)this.findViewById(R.id.txtGuesses);
		imgGameBoard = (ImageView)this.findViewById(R.id.imgGameBoard);
		
		Intent i = getIntent();
		
		if (i.hasExtra(GAME_ID)){
			long gameId = i.getLongExtra(GAME_ID, 0);
			
			if (gameId == 0){
				manager = new GameManager(this);
			} else {
				manager = new GameManager(this, gameId);
			}
			
		} else {
			manager = new GameManager(this);
		}
		
		txtWord.setText(manager.getDisplayWord());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}

	public void btnGuess_onClick(View view) {
		DialogFragment df = new GuessDialogFragment();
		df.show(getSupportFragmentManager(), GUESS_DIALOG_FRAGMENT);
	}
	
	public void handleGuess(String guess){
	
		try {
			manager.processGuess(guess,true);
			
			//is the game over?
    		if(manager.isGameOver()){
    			Toast.makeText(this, this.getString(R.string.strLoser) + ": " + manager.getWord(), Toast.LENGTH_LONG).show();
    			manager.loseGame();

    			Intent i = new Intent();
    			i.putExtra(GAME_ID, manager.getGameId());
    			setResult(RESULT_OK,i);
    			this.finish();
    		} else if (manager.isMatch()){
    			Toast.makeText(this, this.getString(R.string.strWinner), Toast.LENGTH_LONG).show();
    			manager.winGame();
    			Intent i = new Intent();
    			i.putExtra(GAME_ID, manager.getGameId());
    			setResult(RESULT_OK,i);
    			this.finish();
    		}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		txtWord.setText(manager.getDisplayWord());
		txtGuesses.setText(manager.getLettersInList(manager.getIncorrectGuessLetters()));
		
		//update game image
		switch(manager.getIncorrectGuesses()){
		
			case 0:
				imgGameBoard.setImageResource(R.drawable.hangman_0);
				imgGameBoard.setContentDescription(getString(R.string.imgGameBoard_contentDescription_0));
				break;
			
			case 1:
				imgGameBoard.setImageResource(R.drawable.hangman_1);
				imgGameBoard.setContentDescription(getString(R.string.imgGameBoard_contentDescription_1));
				break;
				
			case 2:
				imgGameBoard.setImageResource(R.drawable.hangman_2);
				imgGameBoard.setContentDescription(getString(R.string.imgGameBoard_contentDescription_2));
				break;
				
			case 3:
				imgGameBoard.setImageResource(R.drawable.hangman_3);
				imgGameBoard.setContentDescription(getString(R.string.imgGameBoard_contentDescription_3));
				break;
				
			case 4:
				imgGameBoard.setImageResource(R.drawable.hangman_4);
				imgGameBoard.setContentDescription(getString(R.string.imgGameBoard_contentDescription_4));
				break;
				
			case 5:
				imgGameBoard.setImageResource(R.drawable.hangman_5);
				imgGameBoard.setContentDescription(getString(R.string.imgGameBoard_contentDescription_5));
				break;
				
			case 6:
				imgGameBoard.setImageResource(R.drawable.hangman_6);
				imgGameBoard.setContentDescription(getString(R.string.imgGameBoard_contentDescription_6));
				break;
		}
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings_game_end:
            	DialogFragment df = new GameEndDialogFragment();
        		df.show(getSupportFragmentManager(), GAME_END_DIALOG_FRAGMENT);
                break;
        }
        return true;
    }

	@Override
	public void handleGameEnd() {
		Toast.makeText(this, this.getString(R.string.strForfeit), Toast.LENGTH_LONG).show();
		manager.quitGame();	
		Intent i = new Intent();
		i.putExtra(GAME_ID, manager.getGameId());
		setResult(RESULT_OK,i);
		this.finish();
	}
}
