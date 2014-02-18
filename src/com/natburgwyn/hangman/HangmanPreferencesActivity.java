package com.natburgwyn.hangman;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class HangmanPreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_hangman_preferences, menu);
		return true;
	}
*/
}
