package com.dobi.jiecon.data;

import java.util.HashMap;


public class AllAppManager {
	
	//String: app name; Object: contains all the info for this app
	private HashMap<String, AppManager> dataL1 = new HashMap<String, AppManager>();
	
	public void put(String key, AppManager value){
		dataL1.put(key, value);
	}
	public AppManager get(String key){
		return dataL1.get(key);		
	}	
}
