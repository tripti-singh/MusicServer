package com.tripti.microservices.example.musicserver.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripti.microservices.example.musicserver.biz.IListenUpBiz;
import com.tripti.microservices.example.musicserver.pojo.AllFriendsData;
import com.tripti.microservices.example.musicserver.pojo.FriendsData;
import com.tripti.microservices.example.musicserver.pojo.PlaysData;
import com.tripti.microservices.example.musicserver.pojo.UserData;
import com.tripti.microservices.example.musicserver.constants.ListenUpConstants;

/**
 * Main Class that calls other services and constructs data
 * @author Tripti
 */
public class ListenUpBizImpl implements IListenUpBiz<UserData> {

	private static Properties props;
	private static String friendsApi;
	private static String friendsResource;
	private static String playsApi;
	private static String playsResource;
	
	static ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void setProperties(Properties prop){
		//One time mapping
		if(props == null){
			props=prop;
			friendsApi = props.getProperty(ListenUpConstants.FRIENDS_API);
			friendsResource = props.getProperty(ListenUpConstants.FRIENDS_RESOURCE);
			playsApi = props.getProperty(ListenUpConstants.PLAYS_API);
			playsResource = props.getProperty(ListenUpConstants.PLAYS_RESOURCE);
		}
	}
	@Override
	public List<UserData> getAllUsers() {
		List<UserData> userDataArr = new ArrayList<UserData>();
		try {
			URL friendsGenericUrl =new URL(friendsApi+"/"+friendsResource);
			String allFriendsData = readDataFromUrl(friendsGenericUrl);
			AllFriendsData friends = mapper.readValue(allFriendsData, AllFriendsData.class);
			//Loop over all the usernames
			for(FriendsData fd: friends.getFriends()){
				UserData ud = getUserDetails(fd.getUsername());
				userDataArr.add(ud);
			}
		} catch(Exception e) {
			throw new RuntimeException("Something went wrong",e);
		}
		return userDataArr;
	}

	@Override
	public UserData getUserDetails(String username) {
		UserData userData = new UserData();
		userData.setUsername(username);
		userData.setUri("/" + props.getProperty(ListenUpConstants.LISTENUP_USER_RESOURCE) + "/" + username);

		try {
			// Construct the 2 API calls
			URL friendsUrl = new URL(friendsApi + "/" + friendsResource + "/" + username);
			String friendsData = readDataFromUrl(friendsUrl);
			URL playsUrl = new URL(playsApi + "/" + playsResource + "/" + username);
			String playsData = readDataFromUrl(playsUrl);
			// Map response to POJOS
			FriendsData friends=null;
			PlaysData plays=null;
			if(friendsData!=null){
				friends = mapper.readValue(friendsData, FriendsData.class);
			}
			if(playsData!=null){
				plays = mapper.readValue(playsData, PlaysData.class);
			}
			// Map counts to UserData
			if (friends != null && friends.getFriends() != null) {
				userData.setFriends(friends.getFriends().size());
			}
			if (plays != null) {
				if (plays.getPlays()!=null && !plays.getPlays().isEmpty()) {
					userData.setPlays(plays.getPlays().size());
				}
				userData.setTracks(plays.getTracks());
			}
			return userData;
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong while fetching single user details",e);
		}
	}

	/**
	 * Function used to read Data from URL.
	 * @param url
	 * @return JSON received from the APIS in String format 
	 * @throws IOException
	 */
	private String readDataFromUrl(URL url) throws IOException{
		try {
		URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        StringBuilder inputLine = new StringBuilder();
        String input;
        while ((input = in.readLine()) != null) 
            inputLine.append(input);
        in.close();
        return inputLine.toString();
		} catch(FileNotFoundException fne){
			return null;
		}
	}
}
