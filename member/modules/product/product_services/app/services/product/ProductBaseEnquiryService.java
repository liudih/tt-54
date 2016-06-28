package services.product;

import java.util.List;

import javax.inject.Inject;

import services.base.FoundationService;
import context.WebContext;
import dao.product.IProductBaseEnquiryDao;

public class ProductBaseEnquiryService implements IProductBaseEnquiryService {
	@Inject
	IProductBaseEnquiryDao dao;
	
	@Inject
	FoundationService foundationService;

	@Override
	public List<String> getListingIdsBySpus(List<String> cspus,
			WebContext webContext) {
		Integer websiteId = foundationService.getSiteID(webContext);
		return dao.getListingIdsBySpus(cspus, websiteId);
	}

	@Override
	public String getListingsBySku(String sku, Integer websiteid) {
		// TODO Auto-generated method stub
		return dao.getListingsBySku(sku, websiteid);
	}

}
