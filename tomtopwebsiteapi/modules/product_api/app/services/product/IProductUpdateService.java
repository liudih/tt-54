package services.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mybatis.guice.transactional.Isolation;
import org.mybatis.guice.transactional.Transactional;

import com.website.dto.product.AttributeItem;
import com.website.dto.product.ImageItem;
import com.website.dto.product.Product;
import com.website.dto.product.PromotionPrice;
import com.website.dto.product.TranslateItem;

import dto.product.ProductBase;
import dto.product.ProductBundleSale;
import dto.product.ProductCategoryMapper;
import dto.product.ProductLabel;
import dto.product.ProductSalePrice;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;

public interface IProductUpdateService {

	public abstract String createProduct(Product pitem, String username)
			throws Exception;

	public abstract void delete(String listingId, String psku, String sku);

	public abstract Boolean saveProductBase(Product pitem, String username,
			String listingid);

	public abstract Boolean saveProductUrl(Product pitem, String username,
			String listingid);

	public abstract List<ProductTranslate> getLanguageidByListingid(
			String clistingid);

	public abstract List<ProductTranslate> getTitleByClistingids(
			List<String> clistingids);

	public abstract List<ProductUrl> getUrlByClistingIds(
			List<String> clistingids);

	public abstract void insertBundle(ProductBundleSale productBundleSale);

	public abstract void deleteAutoBundle();

	public abstract void alterAutoBundleSaleVisible();

	public abstract boolean saveOrUpdateProductCategory(
			ProductCategoryMapper productCategoryMapper);

	public abstract List<Integer> getProductCategoryMapperByListingId(
			String listingId);

	public abstract boolean updateProductCategoryWithSomeListingId(
			ArrayList<ProductCategoryMapper> productCategoryMappers,
			String listingId);

	public abstract boolean updateByListingIdSelective(ProductBase product);

	public abstract String saveProductPromotions(
			List<com.website.dto.product.PromotionPrice> ProductPromotionlist,
			String user);

	public abstract boolean saveOrUpdateProductSalePrice(
			ProductSalePrice productSalePrice);

	public abstract int updateCostPrice(String sku, int websiteid,
			double costprice);

	public abstract String saveProductCategory(String sku, String categoryPath,
			int websiteid);

	public abstract void insertProductLabel(ProductLabel productLabel);

	public abstract void deleteProductLabel(int websiteId, String type);

	public abstract int updatePrice(String sku, int website, Double price,
			Double cost, Double freight, Boolean freeShipping);

	public abstract int updateStatus(String sku, int website, Integer status);

	public abstract int addUrl(String sku, int website, String url,
			Integer langid);

	public abstract String addProductMultiAttribute(String sku,
			String parentsku, Integer websiteId,
			List<AttributeItem> multiAttributes, String username);

	public abstract String addSellpoints(String sku, Integer websiteId,
			Integer languageId, List<String> points, String userName);

	public abstract String addProductImages(String sku, Integer websiteId,
			List<ImageItem> imgUrl);

	public abstract String updateImages(String sku, Integer websiteId,
			List<ImageItem> imgUrl);

	public abstract String addProductLable(String sku, Integer websiteId,
			List<String> types);

	public abstract boolean updateStorageMapper(List<Integer> storagesAdd,
			List<Integer> storagesDel, String listingId, String sku,
			String createuser);

	public abstract boolean updateQty(int qty, String listingId, int websiteid);

	public abstract void updateQtyInventory(String listingid, Integer qty,
			int websiteid);

	public abstract String addTransate(String sku, int websiteId,
			List<com.website.dto.product.TranslateItem> tranItems);

	public abstract String updateTransate(String sku, int websiteId,
			List<com.website.dto.product.TranslateItem> tranItems);

	public abstract String updateStorages(String sku, int websiteId,
			List<Integer> storages);

	public abstract String updateFregiht(String sku, Integer websiteId,
			Double freight, Boolean freeShipping);

	public abstract String deleteProductLabel(String sku, Integer websiteId,
			List<String> types);

	public abstract String updateProductQty(String sku, Integer websiteId,
			Integer qty);

	/**
	 * 通过listingId来查询表数据
	 * 
	 * @table t_product_saleprice
	 * @author lijun
	 * @param listingId
	 */
	public abstract List<ProductSalePrice> getProductSalePriceAfterCurrentDate(
			Map<String, Object> paras);

	/**
	 * 插入数据
	 * 
	 * @table t_product_saleprice
	 * @author lijun
	 * @param record
	 * @return int 1=插入成功 0=插入失败
	 */
	public abstract int insertProductSalePrice(ProductSalePrice record);

	public abstract int setProductStatusBySku(String sku);

	/**
	 * Update View Count for DB
	 * 
	 * @param listingID
	 */
	public abstract void incrementViewCount(int siteID, String listingID);

	public abstract int insertProductRecommend(String clistinid,
			String crecommendlisting, String ccreateuser);

	/**
	 * 删除多属性产品一个属性(所有sku的属性值会清除)
	 * 
	 * @param parentSku
	 * @param websiteId
	 * @param key
	 * @return
	 */
	public abstract String deleteMultiProductAttribute(String parentSku,
			Integer websiteId, String key, Integer lang);

	public abstract String deleteProductAttribute(String clistingid,
			String key, Integer lang);

	public abstract String addProductAttribute(String sku, Integer websiteId,
			String listingid, List<AttributeItem> aitem, String username);

	public abstract int deleteProductCurrentSalePrice(String listingId);

	public abstract int deleteProductSalePriceByDate(String listingId,
			Date start, Date end);

	public abstract int deleteProductSellingPoints(String listingId,
			int languageId);
	
	public abstract boolean deleteProductImageByListingId(String listingId, Integer websiteId);

	/**
	 * 
	 * @Title: addProductCategoryMapper
	 * @Description: TODO(增加产品与品类映射)
	 * @param @param listingId
	 * @param @param categoryIds
	 * @param @return
	 * @return String
	 * @throws 
	 * @author yinfei
	 */
	public abstract String addProductCategoryMapper(List<String> listingId,
			List<Integer> categoryIds);
}