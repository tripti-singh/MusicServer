package com.tripti.microservices.example.musicserver.pojo;

import java.util.List;

public class FriendsData {
	private String username;
	private List<String> friends;
	private String uri;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<String> getFriends() {
		return friends;
	}
	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
