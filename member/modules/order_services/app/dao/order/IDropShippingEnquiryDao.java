package dao.order;

import dao.IOrderEnquiryDao;
import dto.order.dropShipping.DropShipping;

public interface IDropShippingEnquiryDao extends IOrderEnquiryDao {
	DropShipping getByID(int id);

	DropShipping getByDropShippingID(String id);

	Double getSumPriceByEmail(String email, int site);
}
