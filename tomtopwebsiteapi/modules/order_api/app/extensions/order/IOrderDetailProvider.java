package extensions.order;

import java.util.List;
import java.util.Map;

import dto.order.OrderDetail;
import valueobjects.product.ProductLite;

public interface IOrderDetailProvider {
	/**
	 * IOrderDetailProvider的ID用来标识此类
	 *
	 * @return
	 */
	String getId();

	/**
	 * 传入商品信息，获取填充了信息的OrderDetail，iorderid字段为null
	 * 
	 * @param productMap
	 *            listingID为key，productlite为value
	 * @param qtyMap
	 *            listingID为key，qty为value
	 * @return
	 */
	List<OrderDetail> createOrderDetails(Map<String, ProductLite> productMap,
			Map<String, Integer> qtyMap, String currency, int siteID, int lang,
			String email);

}
