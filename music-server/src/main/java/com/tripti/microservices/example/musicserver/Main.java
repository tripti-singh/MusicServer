package com.tripti.microservices.example.musicserver;

import static spark.Spark.get;
import static spark.Spark.port;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripti.microservices.example.musicserver.pojo.UserData;
import com.tripti.microservices.example.musicserver.biz.IListenUpBiz;
import com.tripti.microservices.example.musicserver.constants.ListenUpConstants;

public class Main {
	static String propertiesFilename = "config.properties";
	
	static IListenUpBiz<UserData> listenUpBiz;
	
    public static void main(String[] args) {
        port(8005); 
        //For wiring biz implementation
        init();
        get("/users/:name", (request, response) -> {
        	String username = request.params("name");
        	return new ObjectMapper().writeValueAsString(listenUpBiz.getUserDetails(username));
        	}
        );
        get("/users", (request, response) -> {
        		return new ObjectMapper().writeValueAsString(listenUpBiz.getAllUsers());
        	}
        );
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void init(){
    	//Initialize Properties file and Biz object
    	Properties prop = new Properties();
    	InputStream input = null;
    	try {
    		input = Main.class.getResourceAsStream(propertiesFilename);
    		prop.load(input);
    		listenUpBiz = (IListenUpBiz) Class.forName(prop.getProperty(ListenUpConstants.BIZ_IMPL),
    				false,IListenUpBiz.class.getClassLoader()).newInstance();
    		listenUpBiz.setProperties(prop);
    	} catch(IOException e){
    		throw new RuntimeException("Could not load properties file!!!");
    	} catch(ClassNotFoundException | IllegalAccessException | InstantiationException iae){
    		throw new RuntimeException("Couldn't initialize the Biz Implementation");
    	}
    }
}
