package services.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import mapper.product.ProductBundleSaleMapper;
import mapper.product.ProductEntityMapMapper;
import play.Logger;
import play.api.libs.iteratee.internal;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.price.PriceService;
import valueobjects.price.BundlePrice;
import valueobjects.price.Price;
import valueobjects.product.ProductLite;
import valueobjects.product.spec.ProductSpecBuilder;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import context.WebContext;
import dao.product.IProductUrlEnquiryDao;
import dao.product.impl.ProductActivityRelationEnquiryDao;
import dto.product.ProductBase;
import dto.product.ProductEntityMap;
import dto.product.ProductMultiattributeEntity;
import dto.product.ProductUrlWithSmallImage;

public class ProductMultiatributeService implements
		IProductMultiatributeService {
	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	ProductEntityMapMapper productEntityMapMapper;

	@Inject
	ProductBundleSaleMapper productBundleSaleMapper;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	PriceService priceService;

	@Inject
	FoundationService foundation;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	@Inject
	ProductActivityRelationEnquiryDao productActivityRelationEnquiryDao;

	@Override
	public Map<String, List<ProductMultiattributeEntity>> getProductMultiatribute(
			String clistingid, String mainclistingid, String type) {
		List<ProductBase> related = null;
		if ("buyonegetone".equals(type)) {
			related = productActivityRelationEnquiryDao
					.getProductsWithSameParentSkuByListingId(clistingid,
							foundation.getSiteID());
		} else {
			related = productBaseMapper
					.getProductsWithSameParentSkuByListingId(clistingid,
							foundation.getSiteID());
		}
		Logger.debug("related Attributes: {}", related);
		if (related.isEmpty()) {
			return null;
		}

		List<String> relatedListingIDs = Lists.transform(related,
				p -> p.getClistingid());
		if (!relatedListingIDs.contains(clistingid)) {
			return null;
		}
		Map<String, Price> priceMap1 = null;
		if (!StringUtils.isEmpty(mainclistingid)) {
			BundlePrice p = (BundlePrice) priceService
					.getPrice(ProductSpecBuilder.build(mainclistingid)
							.bundleWith(relatedListingIDs).get());
			priceMap1 = Maps.uniqueIndex(p.getBreakdown(), obj -> {
				return obj.getListingId();
			});
		}
		Map<String, Price> priceMap = priceMap1;
		// 找到所有产品图片
		List<ProductUrlWithSmallImage> relatedProductUrls = this.productUrlEnquityDao
				.getProductUrlsByListingIds(relatedListingIDs,
						foundation.getLanguage());
		Map<String, ProductUrlWithSmallImage> productByListingID = Maps
				.newHashMap();
		for (ProductUrlWithSmallImage b : relatedProductUrls) {
			productByListingID.put(b.getClistingid(), b);
		}
		// 找到产品的相关信息
		List<ProductLite> productLites = productEnquiryService
				.getProductLiteByListingIDs(relatedListingIDs,
						foundation.getSiteID(), foundation.getLanguage());
		Map<String, ProductLite> productLitesByListingID = Maps.newHashMap();
		for (ProductLite b : productLites) {
			productLitesByListingID.put(b.getListingId(), b);
		}
		// 找到产品的多属性值
		List<ProductEntityMap> attributes = productEntityMapMapper
				.getProductEntityMapListByListingIds(relatedListingIDs,
						foundation.getLanguage(),foundation.getSiteID());

		List<ProductEntityMap> sourceMap = productEntityMapMapper
				.getProductEntityMapByListingId(clistingid,
						foundation.getLanguage());
		final Map<String, List<ProductEntityMap>> attributesByListingID = Maps
				.toMap(relatedListingIDs, rid -> Lists
						.newArrayList(Collections2.filter(attributes,
								a -> rid.equals(a.getClistingid()))));

		List<ProductEntityMap> filterProduct = filterProduct(sourceMap,
				attributesByListingID);
		// 组装信息
		String currency = foundation.getCurrency();
		Collection<ProductMultiattributeEntity> pmblist = Collections2
				.transform(
						filterProduct,
						e -> {
							ProductLite productLite = productLitesByListingID
									.get(e.getClistingid());
							ProductUrlWithSmallImage productUrlWithSmallImage = productByListingID
									.get(e.getClistingid());
							Price price = null;
							if (!StringUtils.isEmpty(mainclistingid)) {
								price = priceMap.get(e.getClistingid());
							}
							return new ProductMultiattributeEntity(e
									.getClistingid(), e.getCsku(), e
									.getCkeyname(), e.getCvaluename(), e
									.isBshow() != null ? e.isBshow() : false, e
									.isBshowimg(), productLite.getTitle(),
									productLite.getUrl(), price != null ? price
											.getPrice() : null,
									productUrlWithSmallImage.getCimageurl(),
									currency, price != null ? price.getSymbol()
											: null);
						});
		Set<String> keys = Sets.newLinkedHashSet(Lists.transform(filterProduct,
				a -> a.getCkeyname()));

		Map<String, List<ProductMultiattributeEntity>> attributesByKey = Maps
				.asMap(keys,
						k -> {
							List<ProductMultiattributeEntity> tlist = Lists
									.newArrayList(Collections2.filter(pmblist,
											a -> k.equals(a.getCkey())));
							Collections
									.sort(tlist,
											new Comparator<ProductMultiattributeEntity>() {
												public int compare(
														ProductMultiattributeEntity arg0,
														ProductMultiattributeEntity arg1) {
													return arg0
															.getCvalue()
															.compareTo(
																	arg1.getCvalue());
												}
											});
							return tlist;
						});
		return attributesByKey;
	}

	@Override
	public Map<String, List<ProductMultiattributeEntity>> getProductMultiatribute(
			String clistingid, String mainclistingid, String type,
			WebContext context) {
		int siteId = foundation.getSiteID(context);
		int languageid = foundation.getLanguage(context);

		List<ProductBase> related = null;
		if ("buyonegetone".equals(type)) {
			related = productActivityRelationEnquiryDao
					.getProductsWithSameParentSkuByListingId(clistingid, siteId);
		} else {
			related = productBaseMapper
					.getProductsWithSameParentSkuByListingId(clistingid, siteId);
		}
		Logger.debug("related Attributes: {}", related);
		if (related.isEmpty()) {
			return null;
		}

		List<String> relatedListingIDs = Lists.transform(related,
				p -> p.getClistingid());
		if (!relatedListingIDs.contains(clistingid)) {
			return null;
		}
		Map<String, Price> priceMap1 = null;
		if (!StringUtils.isEmpty(mainclistingid)) {
			BundlePrice p = (BundlePrice) priceService
					.getPrice(ProductSpecBuilder.build(mainclistingid)
							.bundleWith(relatedListingIDs).get());
			priceMap1 = Maps.uniqueIndex(p.getBreakdown(), obj -> {
				return obj.getListingId();
			});
		}
		Map<String, Price> priceMap = priceMap1;
		// 找到所有产品图片
		List<ProductUrlWithSmallImage> relatedProductUrls = this.productUrlEnquityDao
				.getProductUrlsByListingIds(relatedListingIDs, languageid);
		Map<String, ProductUrlWithSmallImage> productByListingID = Maps
				.newHashMap();
		for (ProductUrlWithSmallImage b : relatedProductUrls) {
			productByListingID.put(b.getClistingid(), b);
		}
		// 找到产品的相关信息
		List<ProductLite> productLites = productEnquiryService
				.getProductLiteByListingIDs(relatedListingIDs, siteId,
						languageid);
		Map<String, ProductLite> productLitesByListingID = Maps.newHashMap();
		for (ProductLite b : productLites) {
			productLitesByListingID.put(b.getListingId(), b);
		}
		// 找到产品的多属性值
		List<ProductEntityMap> attributes = productEntityMapMapper
				.getProductEntityMapListByListingIds(relatedListingIDs,
						languageid,siteId);

		List<ProductEntityMap> sourceMap = productEntityMapMapper
				.getProductEntityMapByListingId(clistingid, languageid);
		final Map<String, List<ProductEntityMap>> attributesByListingID = Maps
				.toMap(relatedListingIDs, rid -> Lists
						.newArrayList(Collections2.filter(attributes,
								a -> rid.equals(a.getClistingid()))));

		List<ProductEntityMap> filterProduct = filterProduct(sourceMap,
				attributesByListingID);
		// 组装信息
		String currency = foundation.getCurrency(context);
		Collection<ProductMultiattributeEntity> pmblist = Collections2
				.transform(
						filterProduct,
						e -> {
							ProductLite productLite = productLitesByListingID
									.get(e.getClistingid());
							ProductUrlWithSmallImage productUrlWithSmallImage = productByListingID
									.get(e.getClistingid());
							Price price = null;
							if (!StringUtils.isEmpty(mainclistingid)) {
								price = priceMap.get(e.getClistingid());
							} else {
								price = priceService.getPrice(e.getClistingid());
							}
							ProductMultiattributeEntity pm = new ProductMultiattributeEntity(
									e.getClistingid(), e.getCsku(), e
											.getCkeyname(), e.getCvaluename(),
									e.isBshow() != null ? e.isBshow() : false,
									e.isBshowimg(), productLite.getTitle(),
									productLite.getUrl(), price != null ? price
											.getUnitPrice() : null,
									productUrlWithSmallImage.getCimageurl(),
									currency, price != null ? price.getSymbol()
											: null);
							if (price != null) {
								pm.setBasePrice(price.getUnitBasePrice());
								pm.setIsDiscounted(price.isDiscounted());
								pm.setDiscount(price.getDiscount());
								pm.setValidTo(price.getValidTo());
							}
							return pm;
						});
		Set<String> keys = Sets.newLinkedHashSet(Lists.transform(filterProduct,
				a -> a.getCkeyname()));

		Map<String, List<ProductMultiattributeEntity>> attributesByKey = Maps
				.asMap(keys,
						k -> {
							List<ProductMultiattributeEntity> tlist = Lists
									.newArrayList(Collections2.filter(pmblist,
											a -> k.equals(a.getCkey())));
							Collections
									.sort(tlist,
											new Comparator<ProductMultiattributeEntity>() {
												public int compare(
														ProductMultiattributeEntity arg0,
														ProductMultiattributeEntity arg1) {
													return arg0
															.getCvalue()
															.compareTo(
																	arg1.getCvalue());
												}
											});
							return tlist;
						});
		return attributesByKey;
	}

	private List<ProductEntityMap> filterProduct(
			List<ProductEntityMap> sourceMap,
			Map<String, List<ProductEntityMap>> attributeMap) {
		List<ProductEntityMap> nowList = new ArrayList<ProductEntityMap>();
		Iterator<Entry<String, List<ProductEntityMap>>> iterator = attributeMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<ProductEntityMap>> next = iterator.next();
			List<ProductEntityMap> productList = next.getValue();
			if (productList.size() == sourceMap.size()) {
				nowList.addAll(productList);
			}
		}

		return nowList;

	}
}
