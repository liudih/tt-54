package services.product;

import java.util.List;

import context.WebContext;

public interface IProductBaseEnquiryService {
	public List<String> getListingIdsBySpus(List<String> cspus, WebContext webContext);
	public String getListingsBySku(String sku,Integer websiteid);
}
