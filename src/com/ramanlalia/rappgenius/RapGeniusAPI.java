package com.ramanlalia.rappgenius;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class RapGeniusAPI {
	
	private static final String TAG = "RAppGenius";
	
	public static List<Track> search(String query) {
		String queryUrl = "http://rapgenius.com/search?hide_unexplained_songs=false&q=" + query.replace(" ", "+");
		Log.v(TAG, "Fetching: " + queryUrl);
		
		List<Track> tracks = new ArrayList<Track>();
		List<String> songs = new ArrayList<String>();			
	    String responseString = null;
	    InputStream is = null;
	    
	    try {	    	
	        URL url = new URL(queryUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000);
	        conn.setConnectTimeout(15000);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d(TAG, "The response is: " + response);
	        is = conn.getInputStream();

	        BufferedReader r = new BufferedReader(new InputStreamReader(is));
	        StringBuilder textResponse = new StringBuilder();
	        String line;
	        while ((line = r.readLine()) != null) {
	            textResponse.append(line);
	        }
	        
	        responseString = textResponse.toString();
	    }  catch(IOException e) {
			Log.e(TAG, "Could not fetch page.");
			Log.e(TAG, e.getMessage());
	    } finally {
	        if (is != null) {
	            try {
					is.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
	        } 
	    }   
	    
	    Document doc = Jsoup.parse(responseString);
	    Elements songElements = doc.select("li.search_result a span.title_with_artists");
	    
		if(songElements.isEmpty()) Log.v(TAG, "Songs List is empty!");
		
		if(doc != null && !songElements.isEmpty()) { 				
			for(Element song: songElements) {
				songs.add(song.text());
			}
			
			Log.v(TAG, "Fetched " + songs.size() + " songs: ");
			
			for(String song: songs) {
				Log.v(TAG, song);				
			
				String songArtist = song.split(" – ")[0];
				String songName = song.split(" – ")[1];
				
				Log.v(TAG, "Parsed Song Name: " + songName);
				Log.v(TAG, "Parsed Song Artist:" + songArtist);
				
				tracks.add(new Track(songName, songArtist));
			}
			
			return tracks;
		}
		
		return null;
	}
	
	public static class Track {
		private String m_name;
		private String m_artist;
		
		public Track(String name, String artist) {
			m_name = name;
			m_artist = artist;
		}
		
		public String getName() {
			return m_name;
		}
		
		public void setName(String name) {
			m_name = name;
		}
		
		public String getArtist() {
			return m_artist;
		}
		
		public void setArtist(String artist) {
			m_artist = artist;
		}
	}
}
