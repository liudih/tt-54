package services.product;

import java.util.List;

import valueobjects.base.Page;

public interface IHomePageDataEnquiry {

	// new arrivals
	public abstract Page<String> getNewArrialListingIdsByCategoryId(
			Integer categoryId, int siteId, int languageId);

	// hot sales
	public abstract List<String> getHotSalesListingIds(int siteId,
			int languageId, int page);

	// clearance sales
	public abstract List<String> getClearanceSalesListingIds(int siteId,
			int languageId, int page);

	// feature Items
	public abstract List<String> getFeaturedItemsListingIds(int siteId,
			int languageId, int page);

	/**
	 * new arrivals for mobile
	 * 
	 * @author xiaoch
	 */
	public abstract List<String> getNewArrivalsListingIds(int siteId,
			int languageId, int page);

	/**
	 * freeshipping for mobile
	 * 
	 * @author xiaoch
	 */
	public abstract List<String> getFreeshippingListingIds(int siteId,
			int languageId, int page);

}