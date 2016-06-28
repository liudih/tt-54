package services.order;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mapper.order.OrderPackMapper;

import org.mybatis.guice.transactional.Transactional;

import dto.order.OrderPack;

public class OrderPackService implements IOrderPackService {
	@Inject
	OrderPackMapper orderPackMapper;
	@Inject
	IOrderEnquiryService orderEnquiryService;

	/* (non-Javadoc)
	 * @see services.order.IOrderPackService#saveOrUpdateOrderPack(dto.order.OrderPack)
	 */
	@Override
	public boolean saveOrUpdateOrderPack(OrderPack orderPack) {
		if (orderPack.getIid() != null) {
			return orderPackMapper.updateByPrimaryKeySelective(orderPack) > 0;
		} else {
			return orderPackMapper.insert(orderPack) > 0;
		}
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderPackService#batchInsertOrderPack(java.util.List)
	 */
	@Override
	public boolean batchInsertOrderPack(List<OrderPack> orderPacks) {
		List<OrderPack> orderPackList = new ArrayList<OrderPack>();
		for (OrderPack orderPack : orderPacks) {
			boolean checkOrderPack = checkOrderPack(orderPack.getIorderid(),
					orderPack.getCtrackingnumber());
			if (checkOrderPack) {
				continue;
			}
			orderPackList.add(orderPack);
		}
		if (orderPackList.size() <= 0) {
			return true;
		}
		return orderPackMapper.batchInsert(orderPackList) > 0;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderPackService#checkOrderPack(java.lang.Integer, java.lang.String)
	 */
	@Override
	public boolean checkOrderPack(Integer orderId, String tackNum) {
		return orderPackMapper.ckeckOrderPackByOrderIdAndTrackNum(orderId,
				tackNum) > 0;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderPackService#batachInsert(com.website.dto.order.OrderPack[])
	 */
	@Override
	@Transactional
	public boolean batachInsert(com.website.dto.order.OrderPack[] orderPacks) {
		List<OrderPack> orderPackList = new ArrayList<OrderPack>();
		for (com.website.dto.order.OrderPack orderPack : orderPacks) {
			OrderPack oPack = new OrderPack();
			oPack.setCshippingtype(orderPack.getCshippingtype());
			oPack.setCsku(orderPack.getCsku());
			oPack.setCtrackingnumber(orderPack.getCtrackingnumber());
			oPack.setDshippingdate(orderPack.getDshippingdate());
			oPack.setFshippingprice(orderPack.getFshippingprice());
			oPack.setIorderid(orderPack.getIorderid());
			oPack.setIqty(orderPack.getIqty());
			orderPackList.add(oPack);
		}

		return batchInsertOrderPack(orderPackList);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderPackService#insertOrderPack(dto.order.OrderPack)
	 */
	@Override
	public boolean insertOrderPack(OrderPack orderPack) {
		boolean checkOrderPack = checkOrderPack(orderPack.getIorderid(),
				orderPack.getCtrackingnumber());
		if (checkOrderPack) {
			return true;
		}

		return saveOrUpdateOrderPack(orderPack);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderPackService#getByOrderId(java.lang.Integer)
	 */
	@Override
	public List<OrderPack> getByOrderId(Integer orderId) {
		return orderPackMapper.getOrderPacksByOrderId(orderId);
	}

	@Override
	public OrderPack getOrderPackByOrderIdAndSku(Integer orderId, String sku) {
		// TODO Auto-generated method stub
		return orderPackMapper.getOrderPackByOrderIdAndSku(orderId, sku);
	}
}
