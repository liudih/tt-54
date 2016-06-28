package com.rabbit.services;

/**
 * Created by Thinkpad on 2015/8/4  22:05.
 */
public class ConsumerHandler {
    public void handleMessage(String text) {
    	try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Received--------------------------: " + text);
    }
}
