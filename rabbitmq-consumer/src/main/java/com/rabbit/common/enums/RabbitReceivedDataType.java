package com.rabbit.common.enums;

import java.util.List;


public enum RabbitReceivedDataType {
	
	/********************  Order  ******changeOrderStatusToRefunded**/	
	RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_DISPATCHED("RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_DISPATCHED",null),
	RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_COMPLETED("RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_COMPLETED",null),
	RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_ONHOLD("RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_ONHOLD",null),
	RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_PROCESSING("RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_PROCESSING",null),
	RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_REFUNDED("RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_REFUNDED",null),
	
	/*********************************************  SHIPPING  *******************************************************************/
	RABBIT_SHIPPING_PUSH_TYPE("RABBIT_SHIPPING_PUSH_TYPE",null);
	private String key;
	private List<String> params; 

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	private RabbitReceivedDataType(String key,List<String> params ) {
		this.key = key;
		this.params=params;
	}
}
