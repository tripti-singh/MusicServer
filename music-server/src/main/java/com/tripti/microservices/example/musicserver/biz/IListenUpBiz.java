package com.tripti.microservices.example.musicserver.biz;

import java.util.List;
import java.util.Properties;

public interface IListenUpBiz<T>{

	void setProperties(Properties props);
	
	public List<T> getAllUsers();
	
	public T getUserDetails(String username);
}
