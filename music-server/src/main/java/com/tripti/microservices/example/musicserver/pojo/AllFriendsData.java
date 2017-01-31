package com.tripti.microservices.example.musicserver.pojo;

import java.util.List;

public class AllFriendsData {
	private List<FriendsData> friends;
	
	private String uri;
	
	public List<FriendsData> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendsData> friends) {
		this.friends = friends;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
