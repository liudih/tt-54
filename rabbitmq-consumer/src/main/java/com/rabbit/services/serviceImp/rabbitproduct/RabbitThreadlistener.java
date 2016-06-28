package com.rabbit.services.serviceImp.rabbitproduct;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RabbitThreadlistener implements ServletContextListener {

//	private static Logger log=Logger.getLogger(RabbitThreadlistener.class.getName());
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ApplicationContext ctx = 
			      WebApplicationContextUtils.
			         getWebApplicationContext(arg0.getServletContext());
//		MonitorThread bean = (MonitorThread)ctx.getBean("monitorThread");
//		RabbitQueueManager rabbitQueueManager = (RabbitQueueManager)ctx.getBean("rabbitQueueManager");
//		ExecutorService exec = Executors.newFixedThreadPool(2);
//	    for (int i = 0; i < 3; i++){
//	    		exec.execute(bean);	 
//	    }
	    
//	    try {
//	    	rabbitQueueManager.startQueueListener();
//		} catch (Exception e) {
//			log.error("RabbitThreadlistener contextInitialized error",e);
//		}
	}
}
