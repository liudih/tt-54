package dao.order;

import java.util.List;

import valueobjects.order_api.cart.CartItem;
import dao.IOrderEnquiryDao;
import dto.TopBrowseAndSaleCount;

public interface IOrderDetailEnquiryDao extends IOrderEnquiryDao {

	public List<TopBrowseAndSaleCount> getTopSaleByTimeRange(Integer timeRange);

	/**
	 * 反序列化order成caritems
	 * 
	 * @author lijun
	 * @param orderNum
	 * @return
	 */
	public List<CartItem> selectCartItemsByOrderNum(String orderNum);

}
