package services.product;

import javax.inject.Inject;

public class ProductViewCountService {

	@Inject
	IProductUpdateService productUpdateService;
	
	public void insertViewCount(Integer siteID,String listingID){
		productUpdateService.incrementViewCount(siteID, listingID);
	}
}
