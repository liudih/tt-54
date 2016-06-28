package services.product;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import context.WebContext;
import valueobjects.product.ProductBadge;

public interface IProductBadgeService {

	public abstract List<ProductBadge> getProductBadgesByListingIDs(
			List<String> listingIDs, int languageID, int site, String currency,
			Date date);

	public abstract List<ProductBadge> getProductBadgesByListingIDs(
			List<String> listingids, int languageID, int site, String currency,
			Date date, boolean showPoint, boolean showIcon);

	public abstract List<ProductBadge> getDBProductBadgesByListingIDs(
			Collection<String> listingIDs, int languageID);

	public abstract ProductBadge getByListing(String listingid, int languageid,
			String currency);

	public abstract List<ProductBadge> getProductBadgesByListingIDs(
			List<String> listingIDs, Date date, WebContext context);

	public abstract List<ProductBadge> getProductBadgesByListingIDs(
			List<String> listingids, Date date, boolean showPoint,
			boolean showIcon, WebContext context);

	public abstract List<ProductBadge> getDBProductBadgesByListingIDs(
			Collection<String> listingIDs, WebContext context);

	public abstract ProductBadge getByListing(String listingid,
			WebContext context);

	List<ProductBadge> getNewProductBadgesByListingIds(List<String> listingids,
			int languageID, int site, String currency, Date date);

}