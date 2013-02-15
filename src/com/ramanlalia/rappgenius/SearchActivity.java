package com.ramanlalia.rappgenius;

import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.ramanlalia.rappgenius.RapGeniusAPI.Track;

public class SearchActivity extends SherlockActivity {
	
	private static final String TAG = "RAppGenius";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Log.v(TAG, "Search Activity");
		handleIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(getIntent());
	}

	private void handleIntent(Intent intent) {
		if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			new SongQueryAsyncTask().execute(query);
		}
	}
	
	private class SearchResultsListAdapter extends BaseAdapter {
		private List<Track> m_tracks;
		private LayoutInflater m_inflater;
		
		public SearchResultsListAdapter(Context context, List<Track> tracks) {
			m_tracks = tracks;
			m_inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return m_tracks.size();
		}

		@Override
		public Object getItem(int location) {
			return m_tracks.get(location);
		}

		@Override
		public long getItemId(int location) {
			return location;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	        if (convertView == null) {
	            convertView = m_inflater.inflate(R.layout.song, parent, false);
	        }
	        
	        TextView title = (TextView) convertView.findViewById(R.id.track_title);
	        TextView artist = (TextView) convertView.findViewById(R.id.track_artist);
	 
	        Track track = m_tracks.get(position);
	        title.setText(track.getName());
	        artist.setText(track.getArtist());
	        return convertView;
		}
		
	}
	
	private class SongQueryAsyncTask extends AsyncTask<String, Void, List<Track>> {

		@Override
		protected void onPostExecute(List<Track> tracks) {
			//ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, result);
			ListView lw = (ListView) SearchActivity.this.findViewById(R.id.search_results_list_view);
			lw.setAdapter(new SearchResultsListAdapter(SearchActivity.this, tracks));
		}

		@Override
		protected List<Track> doInBackground(String... q) {
			return RapGeniusAPI.search(q[0]);
		}
	}
}
