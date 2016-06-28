package services.order.dropShipping;

import javax.inject.Inject;

import dao.order.IDropShippingEnquiryDao;
import dto.order.dropShipping.DropShipping;

public class DropShippingEnquiryService {
	@Inject
	private IDropShippingEnquiryDao dropShippingEnquiry;

	public DropShipping getByID(int id) {
		return dropShippingEnquiry.getByID(id);
	}

	public DropShipping getByDropShippingID(String id) {
		return dropShippingEnquiry.getByDropShippingID(id);
	}

	public Double getSumPriceByEmail(String email, int site) {
		Double sum = dropShippingEnquiry.getSumPriceByEmail(email, site);
		return sum == null ? 0 : sum;
	}
}
