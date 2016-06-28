package com.tomtop.es.entity;

/**
 * 排序对象
 * @author ztiny
 *
 */
public class OrderEntity implements Comparable<OrderEntity>{

	/**属性名称*/
	String propetyName;
	/**排序顺序*/
	int order;
	/**排序类型*/
	String type;
	
	
	public OrderEntity(){}
	
	public OrderEntity(String propetyName,int order,String type){
		this.propetyName = propetyName;
		this.order = order ;
		this.type = type;
	}
	
	public String getPropetyName() {
		return propetyName;
	}
	public void setPropetyName(String propetyName) {
		this.propetyName = propetyName;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int compareTo(OrderEntity o) {
		if(order!=o.getOrder()){
			return order-o.getOrder();
		}
		return order;	
	}
	
	
}
