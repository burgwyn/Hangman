package com.natburgwyn.hangman;

import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String TAG = "DBHelper";

	private static final String DB_NAME = "hangman.db";
	private static final int DB_VERSION = 1;

	private static final String CREATE_TABLE_GAME = "create table Game (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Word text, Result INTEGER NULL, CreateDate DATETIME, LastEditDate DATETIME, CompleteDate DATETIME NULL);";
	private static final String CREATE_TABLE_TURN = "CREATE TABLE Turn (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, GameID INTEGER NOT NULL, Guess TEXT NOT NULL, GuessDate DATETIME);";
	private static final String CREATE_TABLE_STATISTICS = "CREATE TABLE statistics (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Code TEXT NOT NULL UNIQUE , Label TEXT NOT NULL UNIQUE , Value TEXT);";

	private static final String TABLE_GAME = "Game";
	private static final String TABLE_STATISTICS = "Statistics";
	private static final String TABLE_TURN = "Turn";

	private static final String COLUMN_ID = "ID";
	private static final String COLUMN_CREATEDATE = "CreateDate";
	private static final String COLUMN_LASTEDITDATE = "LastEditDate";
	private static final String COLUMN_COMPLETEDATE = "CompleteDate";
	private static final String COLUMN_WORD = "Word";
	private static final String COLUMN_RESULT = "Result";

	private static final String COLUMN_GUESSDATE = "GuessDate";
	private static final String COLUMN_GAMEID = "GameID";
	private static final String COLUMN_GUESS = "Guess";

	//private static final String COLUMN_CODE = "Code";
	private static final String COLUMN_LABEL = "Label";
	private static final String COLUMN_VALUE = "Value";

	public static final int GAME_RESULT_WIN = 1;
	public static final int GAME_RESULT_LOSE = 2;
	public static final int GAME_RESULT_QUIT = 3;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try{

			db.execSQL(CREATE_TABLE_GAME);
			db.execSQL(CREATE_TABLE_TURN);	
			db.execSQL(CREATE_TABLE_STATISTICS);

			db.execSQL("INSERT INTO statistics (Code, Label, Value) VALUES ('GAME', 'Games', '0');");
			db.execSQL("INSERT INTO statistics (Code, Label, Value) VALUES ('WIN', 'Wins', '0');");
			db.execSQL("INSERT INTO statistics (Code, Label, Value) VALUES ('LOSE', 'Losses', '0');");
			db.execSQL("INSERT INTO statistics (Code, Label, Value) VALUES ('QUIT', 'Forfeits', '0');");

		}catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TURN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
		onCreate(db);
	}

	public long insertGame(String word){

		SQLiteDatabase db = this.getWritableDatabase();
		long value = 0;

		try {

			ContentValues values = new ContentValues();
			values.put(COLUMN_WORD, word);
			values.put(COLUMN_CREATEDATE, new Date().toGMTString());
			values.put(COLUMN_LASTEDITDATE, new Date().toGMTString());

			value = db.insert(TABLE_GAME, null, values);
			Log.i(TAG, String.valueOf(value));
		}catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
		}
		finally{
			db.close();
		}

		return value;
	}


	public void insertTurn(long gameId, String guess){

		SQLiteDatabase db = this.getWritableDatabase();
		try{
			ContentValues values = new ContentValues();
			values.put(COLUMN_GUESS, guess);
			values.put(COLUMN_GAMEID, gameId);
			values.put(COLUMN_GUESSDATE, new Date().toGMTString());

			ContentValues valuesGame = new ContentValues();
			valuesGame.put(COLUMN_LASTEDITDATE, new Date().toGMTString());

			db.beginTransaction();

			db.insert(TABLE_TURN, null, values);
			Log.i("insert", "turn");
			db.update(TABLE_GAME, valuesGame, COLUMN_ID + " = " + String.valueOf(gameId), null);
			Log.i("update", "game");
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
		}
		finally{
			db.close();
		}
	}

	public Game getGame(long gameId){

		Game g = new Game();

		SQLiteDatabase db = this.getReadableDatabase();

		try{
			Cursor cGame = db.query(TABLE_GAME, new String[] {COLUMN_WORD}, COLUMN_ID + " = " + String.valueOf(gameId), null, null, null, null);
			cGame.moveToFirst();
			if (cGame.getCount() > 0){

				g.setWord( cGame.getString(cGame.getColumnIndexOrThrow(COLUMN_WORD)) );
				cGame.close();

				Cursor cTurn = db.query(TABLE_TURN, new String[] {COLUMN_GUESS}, COLUMN_GAMEID + " = " + String.valueOf(gameId), null, null, null, null);
				cTurn.moveToFirst();
				Log.i("guess", String.valueOf( cTurn.getCount()) );
				while(cTurn.moveToNext()){
					Log.i("guess",cTurn.getString(cTurn.getColumnIndex(COLUMN_GUESS)));
					g.addGuess(cTurn.getString(cTurn.getColumnIndex(COLUMN_GUESS)));
				}

				cTurn.close();
			}
		}catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
		}
		finally{
			db.close();
		}

		return g;
	}

	private void endGame(long gameId, int result){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_COMPLETEDATE, new Date().toGMTString());
		values.put(COLUMN_LASTEDITDATE, new Date().toGMTString());
		values.put(COLUMN_RESULT, result);

		db.update(TABLE_GAME, values, COLUMN_ID + " = " + String.valueOf(gameId), null);

		db.close();
		Log.i(TAG, "endGame " + String.valueOf(gameId));
	}

	public Cursor getStatistics(){

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c =  db.query(TABLE_STATISTICS, new String[] {"rowid _id", COLUMN_LABEL, COLUMN_VALUE}, null, null, null, null, null);

		return c;
	}

	public long getGame(){

		long id = 0;

		SQLiteDatabase db = this.getReadableDatabase();

		try{

			Cursor c =  db.query(TABLE_GAME, new String[] {COLUMN_ID}, COLUMN_RESULT + " IS NULL AND " + COLUMN_COMPLETEDATE + " IS NULL", null, null, null, null);

			if (c.moveToFirst()){
				Log.i(TAG, "getGame " + String.valueOf(c.getColumnIndex(COLUMN_ID)));
				id = c.getLong(c.getColumnIndex(COLUMN_ID));
			}

			c.close();
			
		}
		catch (Exception ex)
		{
			Log.e(TAG, ex.getLocalizedMessage());
			}
		finally{
			db.close();
		}

		return id;
	}

	public void calculateStatistics(){
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			db.beginTransaction();

			db.execSQL("UPDATE statistics SET Value = (SELECT COUNT(ID) AS Val FROM Game) WHERE Code = 'GAME';");
			db.execSQL("UPDATE statistics SET Value = (SELECT COUNT(ID) AS Val FROM Game WHERE Result = 1) WHERE Code = 'WIN';");
			db.execSQL("UPDATE statistics SET Value = (SELECT COUNT(ID) AS Val FROM Game WHERE Result = 2) WHERE Code = 'LOSE';");
			db.execSQL("UPDATE statistics SET Value = (SELECT COUNT(ID) AS Val FROM Game WHERE Result = 3) WHERE Code = 'QUIT';");
			Log.d(TAG, "calculateStatistics");
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch(Exception ex){
			Log.e(TAG, "calculateStatistics");
		}
		finally{
			db.close();
		}
	}

	public void loseGame(long gameId) {
		this.endGame(gameId, GAME_RESULT_LOSE);
	}

	public void winGame(long gameId) {
		this.endGame(gameId, GAME_RESULT_WIN);
	}

	public void quitGame(long gameId) {
		this.endGame(gameId, GAME_RESULT_QUIT);
	}
}
