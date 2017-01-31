package com.tripti.microservices.example.musicserver.pojo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaysData {
	private List<String> plays;
	private String uri;
	public List<String> getPlays() {
		return plays;
	}
	public void setPlays(List<String> plays) {
		this.plays = plays;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getTracks(){
		Set<String> tracks = new HashSet<String>(plays);
		if(tracks==null)
			return 0;
		return tracks.size();
	}
}
