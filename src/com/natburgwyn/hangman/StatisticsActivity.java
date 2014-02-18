package com.natburgwyn.hangman;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.support.v4.widget.SimpleCursorAdapter;

public class StatisticsActivity extends ListActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		
		final DBHelper dh = new DBHelper(this);
		
		new AsyncTask<Void,Void,Void>(){
			@Override
			protected Void doInBackground(Void... params){
				dh.calculateStatistics();
				return null;
			}
		}.execute();
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_statistics_item, dh.getStatistics(), new String[]{"Label","Value"}, new int[] {R.id.txtLabel, R.id.txtValue});
		
		//ListView list = (ListView)this.findViewById(R.id.list);
		//list.setAdapter(adapter);
		
		this.setListAdapter(adapter);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_statistics, menu);
		return true;
	}

}
