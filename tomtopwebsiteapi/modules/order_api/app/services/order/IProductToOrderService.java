package services.order;

import valueobjects.order_api.SaveOrderRequest;
import dto.order.Order;

public interface IProductToOrderService {

	/**
	 * 
	 * @param listingQtyMap
	 *            ListingID为key，qty为value
	 * @param mis
	 *            用户信息，可用LoginService.getLoginData获得
	 * @param siteID
	 *            站点ID
	 * @param lang
	 *            LanguageID
	 * @param cy
	 *            货币代码e.g.(USD)
	 * @return
	 */
	public abstract Order saveOrder(SaveOrderRequest req);

	public abstract boolean updateShippingMethod(String orderID,
			int shippingID, int siteID, int languageID);

}