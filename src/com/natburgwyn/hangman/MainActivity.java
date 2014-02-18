package com.natburgwyn.hangman;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity implements IGameEndListener {
	
	private static final String TAG = "MainActivity";
	
	private static final int GAME_REQUEST = 0;
	private static final String GAME_END_DIALOG_FRAGMENT = "gameEndDialogFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		manageScreen();

	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		manageScreen();
	}
	
	private void manageScreen(){
		
		final DBHelper dh = new DBHelper(this);
		
		new AsyncTask<Void,Void, Long>(){
		      @Override
		      protected Long doInBackground(Void... params) {
		    	//get game id
		  		return dh.getGame();
		      }
		      
		      @Override
		      protected void onPostExecute(Long gameId) {
		    	 
		    	  loadScreen(gameId);
		      }
		    }.execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void btnNewGame_onClick(View view){
		Intent i = new Intent(MainActivity.this, GameActivity.class);
		i.putExtra(GameActivity.GAME_ID, (long)0);
		startActivityForResult(i, GAME_REQUEST);
	}
	
	public void btnResumeGame_onClick(View view){
		Intent i = new Intent(MainActivity.this, GameActivity.class);
		i.putExtra(GameActivity.GAME_ID, Long.valueOf(view.getTag().toString()));
		startActivityForResult(i, GAME_REQUEST);
	}
	
	public void btnEndGame_onClick(View view){
		DialogFragment df = new GuessDialogFragment();
		df.show(getSupportFragmentManager(), GAME_END_DIALOG_FRAGMENT);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.menu_statistics:
	        	Intent i = new Intent(MainActivity.this, StatisticsActivity.class);
	        	startActivity(i);
	        	break;
            case R.id.menu_settings:
            	Intent settingsIntent = new Intent(MainActivity.this, HangmanPreferencesActivity.class);
            	startActivity(settingsIntent);
                break;
        }
        return true;
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, String.valueOf(requestCode));
        switch(requestCode) {
        case GAME_REQUEST:
            if (resultCode == RESULT_OK) {
                loadScreen(0);
            } 
            break;
        }
    }
	
	private void loadScreen(long gameId){
		if (gameId == 0){
			findViewById(R.id.btnResumeGame).setVisibility(View.INVISIBLE);
			findViewById(R.id.btnEndGame).setVisibility(View.INVISIBLE);
			findViewById(R.id.btnNewGame).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.btnNewGame).setVisibility(View.INVISIBLE);
			findViewById(R.id.btnResumeGame).setVisibility(View.VISIBLE);
			findViewById(R.id.btnEndGame).setVisibility(View.VISIBLE);
			
			findViewById(R.id.btnResumeGame).setTag(gameId);
			findViewById(R.id.btnEndGame).setTag(gameId);
		}
	}
	
	@Override
	public void handleGameEnd() {
		//manager.end();
		
		
		
	}
}
