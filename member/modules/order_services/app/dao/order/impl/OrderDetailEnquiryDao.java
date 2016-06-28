package dao.order.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.api.client.util.Maps;

import valueobjects.order_api.cart.CartItem;
import mapper.order.DetailMapper;
import dao.order.IOrderDetailEnquiryDao;
import dto.TopBrowseAndSaleCount;

public class OrderDetailEnquiryDao implements IOrderDetailEnquiryDao {

	@Inject
	DetailMapper detailMapper;

	@Override
	public List<TopBrowseAndSaleCount> getTopSaleByTimeRange(Integer timeRange) {
		return detailMapper.getTopSaleByTimeRange(timeRange);
	}

	public List<CartItem> selectCartItemsByOrderNum(String orderNum){
		if(orderNum == null || orderNum.length() == 0){
			throw new NullPointerException("orderNum is null");
		}
		Map<String,Object> paras = Maps.newHashMap();
		paras.put("orderNum", orderNum);
		return detailMapper.selectCartItemsByOrderNum(paras);
	}
}
