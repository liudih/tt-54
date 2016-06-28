package services.product;

import java.util.List;
import java.util.Map;

import context.WebContext;
import dto.product.ProductAttributeMessage;
import dto.product.ProductBase;
import dto.product.ProductEntityMap;

public interface IEntityMapService {

	public abstract List<ProductEntityMap> getEntityMapsByListingId(
			String listingId);

	public abstract List<ProductEntityMap> getProductEntityMapByListingId(
			String listingId, Integer lang);

	public abstract List<ProductBase> getProductsWithSameParentSku(
			String cparentsku);

	public abstract Map<String, String> getAttributeMap(String listingID,
			Integer languageID);

	public abstract List<ProductEntityMap> getProductEntityMapWithSameParentSkuByListingId(
			String listingID, Integer langid, String keyname, Integer websiteid);

	public abstract List<ProductAttributeMessage> getProductAttributeMessages(
			List<String> listingIds, WebContext webContext, String keyname);

	Map<String, String> getAttributeMap(String listingID, WebContext context);

	List<ProductEntityMap> getProductEntityMapWithSameParentSkuByListingId(
			String listingID, String keyname, WebContext context);

	List<ProductEntityMap> getProductEntityMapByListingId(String listingId,
			WebContext context);

	List<ProductEntityMap> getProductEntityMapListByListingIds(
			List<String> listingIds, WebContext context);

}