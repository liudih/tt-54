package services.product;

import interceptors.CacheResult;

import java.util.List;

import javax.inject.Inject;

import services.base.FoundationService;
import context.WebContext;
import dao.product.IProductSellingPointsEnquiryDao;
import dto.product.ProductSellingPoints;

public class ProductSellingPointsService implements
		IProductSellingPointsService {

	@Inject
	FoundationService foundationService;

	@Inject
	IProductSellingPointsEnquiryDao sellingPointsDao;

	List<ProductSellingPoints> getProductSellingPointsByListingIdAndLanguage(
			String listingID, int lang) {
		return sellingPointsDao.getProductSellingPointsByListingIdAndLanguage(
				listingID, lang);
	};

	@Override
	public List<ProductSellingPoints> getProductSellingPointsByListingIdAndLanguage(
			String listingID, WebContext context) {
		int lang = foundationService.getLanguage(context);
		return this.getProductSellingPointsByListingIdAndLanguage(listingID,
				lang);
	};

	@CacheResult("product.badges")
	List<ProductSellingPoints> getProductSellingPointsByListingIds(
			List<String> listingids, Integer lang) {
		return sellingPointsDao.getProductSellingPointsByListingIds(listingids,
				lang);
	};

	@Override
	public List<ProductSellingPoints> getProductSellingPointsByListingIds(
			List<String> listingids, WebContext context) {
		int lang = foundationService.getLanguage(context);
		return this.getProductSellingPointsByListingIds(listingids, lang);
	};

	@Override
	public List<ProductSellingPoints> getProductSellingPointsByListingId(
			String listingID) {
		return sellingPointsDao.getProductSellingPointsByListingId(listingID);
	};

	@Override
	public List<ProductSellingPoints> getProductSellingPointsByListingIdsOnly(
			List<String> listingIDs) {
		return sellingPointsDao
				.getProductSellingPointsByListingIdsOnly(listingIDs);
	};
}
