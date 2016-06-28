package com.rabbit.services;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.stereotype.Service;
@Service
public class CachingConnectionFactoryManager {

	private  static String rabbitUser="admin";
    private  static String rabbitPwd="admin";
    //private  static String rabbitHost="192.168.0.110";
    private  static String rabbitHost="192.168.220.176";

    //private static  CachingConnectionFactoryManager cachingConnectionFactoryManager;
    private static CachingConnectionFactory cachingConnectionFactory;
	public static CachingConnectionFactory getCachingConnectionFactory() {
		if(cachingConnectionFactory==null){
			cachingConnectionFactory=new CachingConnectionFactory(rabbitHost);
			cachingConnectionFactory.setUsername(rabbitUser);
			cachingConnectionFactory.setPassword(rabbitPwd);
		}
		return cachingConnectionFactory;
	}
	/*public static void setCachingConnectionFactory(
			CachingConnectionFactory cachingConnectionFactory) {
		CachingConnectionFactoryManager.cachingConnectionFactory = cachingConnectionFactory;
	}*/
	private CachingConnectionFactoryManager(){
		
	}
	
	/*public static CachingConnectionFactoryManager getInstence(){
		if(cachingConnectionFactoryManager==null){
			cachingConnectionFactoryManager=new CachingConnectionFactoryManager();
		}
		return cachingConnectionFactoryManager;
	}*/
}
