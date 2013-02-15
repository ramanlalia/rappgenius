package com.ramanlalia.rappgenius;

import java.util.List;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ramanlalia.rappgenius.RapGeniusAPI.Track;


public class SearchFragment extends SherlockFragment {
	private static final String TAG = "RAppGenius";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		return inflater.inflate(R.layout.search, container, false);
	}
	
	public void newSearch(String query) {
		InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getActivity().findViewById(R.id.search).getWindowToken(), 0);
		
		Log.v(TAG, "New Search: " + query);
		new SongQueryAsyncTask().execute(query);
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
			SearchFragment.this.getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
			
			ListView lw = (ListView) SearchFragment.this.getActivity().findViewById(R.id.search_results_list_view);
			if(lw != null) lw.setAdapter(new SearchResultsListAdapter(SearchFragment.this.getActivity(), tracks));
		}

		@Override
		protected List<Track> doInBackground(String... q) {
			return RapGeniusAPI.search(q[0]);
		}
	}
}
