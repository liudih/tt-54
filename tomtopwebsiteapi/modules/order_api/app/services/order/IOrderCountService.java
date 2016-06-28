package services.order;

import valueobjects.order_api.OrderCount;

public interface IOrderCountService {

	public abstract OrderCount getCountByEmail(String email, Integer siteId,
			Integer isShow, boolean isNormal);

	public abstract OrderCount getCountByEmailAndTag(String email,
			Integer siteId, Integer isShow, String tag);

	public abstract OrderCount getDropShippingOrderCountByEmail(String email,
			Integer siteId, Integer isShow);

}