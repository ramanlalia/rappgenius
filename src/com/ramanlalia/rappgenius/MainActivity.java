package com.ramanlalia.rappgenius;


import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

public class MainActivity extends SherlockFragmentActivity {
	
	private static final String TAG = "RAppGenius";
	private SearchFragment m_searchFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_main);
		m_searchFragment = new SearchFragment();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		
		menu.findItem(R.id.search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, m_searchFragment).commit();
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				Log.v(TAG, "Removing SearchFragment...");
				getSupportFragmentManager().beginTransaction().remove(m_searchFragment).commit();
				return true;
			}
		});
		
        searchView.setQueryHint("Song/artist/album...");
        searchView.setOnQueryTextListener(new OnSongQueryTextListener());

		return true;
	}
	
	private class OnSongQueryTextListener implements SearchView.OnQueryTextListener {

		@Override
		public boolean onQueryTextSubmit(String query) {
			findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
			m_searchFragment.newSearch(query);		
			return true;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	
}
