package com.rabbit.services.iservice.order;

import java.util.List;

import com.rabbit.dto.order.OrderPack;

public interface IOrderPackService {

	public abstract boolean saveOrUpdateOrderPack(OrderPack orderPack);

	public abstract boolean batchInsertOrderPack(List<OrderPack> orderPacks);

	public abstract boolean checkOrderPack(Integer orderId, String tackNum);

	public abstract boolean batachInsert(
			OrderPack[] orderPacks);

	public abstract boolean insertOrderPack(OrderPack orderPack);

	/**
	 * 只获取iorderid,ctrackingnumber，去除重复跟踪号
	 * 
	 * @param orderId
	 * @return
	 */
	public abstract List<OrderPack> getByOrderId(Integer orderId);
	
	/**
	 * 根据订单号和sku查询OrderPack表的数据信息
	 * @param orderId
	 * @param sku
	 * @return
	 */
	public abstract OrderPack getOrderPackByOrderIdAndSku(Integer orderId, String sku);

}