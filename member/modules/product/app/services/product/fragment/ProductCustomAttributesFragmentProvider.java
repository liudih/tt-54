package services.product.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import mapper.product.ProductEntityMapMapper;
import mapper.product.ProductImageMapper;
import services.base.FoundationService;
import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductAttributeTag;
import valueobjects.product.ProductContext;
import valueobjects.product.ProductCustomAttributes;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import dao.product.IProductUrlEnquiryDao;
import dto.product.ProductBase;
import dto.product.ProductEntityMap;
import dto.product.ProductUrlWithSmallImage;

public class ProductCustomAttributesFragmentProvider implements
		IProductFragmentProvider {

	public static final String NAME = "entity-map";

	@Inject
	ProductEntityMapMapper productEntityMapMapper;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	ProductImageMapper productImageMapper;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	@Inject
	FoundationService foundation;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		final List<ProductBase> trelated = productBaseMapper
				.getProductsWithSameParentSkuMatchingAttributes(
						context.getListingID(), context.getSiteID());

		if (trelated.size() == 0) {
			return null;
		}

		Collection<ProductBase> relalist = Collections2.filter(trelated,
				obj -> obj.getIstatus() == 1);
		if (relalist.size() == 0) {
			return null;
		}
		final List<ProductBase> related = Lists.newArrayList(relalist);
		final List<String> relatedListingIDs = Lists.transform(related,
				p -> p.getClistingid());

		List<ProductUrlWithSmallImage> relatedProductUrls = this.productUrlEnquityDao
				.getProductUrlsByListingIds(relatedListingIDs,
						foundation.getDefaultLanguage());

		final Map<String, ProductUrlWithSmallImage> productByListingID = Maps
				.newHashMap();
		for (ProductUrlWithSmallImage b : relatedProductUrls) {
			productByListingID.put(b.getClistingid(), b);
		}

		List<ProductEntityMap> attributesList = productEntityMapMapper
				.getProductEntityMapListByListingIds(relatedListingIDs,
						context.getLang(), context.getSiteID());

		if (null == attributesList || attributesList.size() == 0) {
			attributesList = productEntityMapMapper
					.getProductEntityMapListByListingIds(relatedListingIDs,
							foundation.getDefaultLanguage(),
							context.getSiteID());
		}

		final List<ProductEntityMap> attributes = Lists
				.newArrayList(attributesList);

		final Map<String, List<ProductEntityMap>> attributesByListingID = Maps
				.toMap(relatedListingIDs, rid -> Lists
						.newArrayList(Collections2.filter(attributes,
								a -> rid.equals(a.getClistingid()))));

		Set<String> keys = Sets.newLinkedHashSet(Lists.transform(attributes,
				a -> a.getCkeyname()));

		List<ProductEntityMap> sourceMap = productEntityMapMapper
				.getProductEntityMapByListingId(context.getListingID(),
						context.getLang());
		if (null == sourceMap || sourceMap.size() == 0) {
			sourceMap = productEntityMapMapper.getProductEntityMapByListingId(
					context.getListingID(), foundation.getDefaultLanguage());
		}
		processingContext.put("product_entity_map", sourceMap);

		Map<String, String> sourceAttributes = Maps.newHashMap();
		for (ProductEntityMap m : sourceMap) {
			sourceAttributes.put(m.getCkeyname(), m.getCvaluename());
		}

		List<ProductEntityMap> attributeList = filterProduct(sourceMap,
				attributesByListingID);
		Map<String, Map<String, String>> changeModel = changeModel(attributes);
		Map<String, Map<String, String>> filterProduct = filterProduct(
				context.getListingID(), changeModel);
		if (filterProduct == null) {
			return null;
		}
		Map<String, List<ProductEntityMap>> nomatchAttribtue = Maps.asMap(keys,
				k -> Lists.newArrayList(Collections2.filter(
						attributeList,
						a -> (filterProduct.containsKey(k)
								&& k.equals(a.getCkeyname()) && !filterProduct
								.get(k).containsKey(a.getCvaluename())))));
		Map<String, Map<String, ProductAttributeTag>> nomatchAttribtueByKey = Maps
				.newHashMap();
		Iterator<Entry<String, List<ProductEntityMap>>> iterator = nomatchAttribtue
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<ProductEntityMap>> attributeMap = iterator
					.next();
			String key = attributeMap.getKey();
			List<ProductEntityMap> productList = attributeMap.getValue();
			for (ProductEntityMap productEntityMap : productList) {
				Map<String, ProductAttributeTag> nomatchTag = new HashMap<String, ProductAttributeTag>();
				String cvaluename = productEntityMap.getCvaluename();
				ProductUrlWithSmallImage url = productByListingID
						.get(productEntityMap.getClistingid());
				ProductAttributeTag productAttributeTag = new ProductAttributeTag(
						productEntityMap.getClistingid(),
						productEntityMap.getCkeyname(),
						productEntityMap.getCvaluename(),
						url != null ? url.getCurl() : productUrlEnquityDao
								.getUrlByListingIdAndLanugage(
										productEntityMap.getClistingid(),
										foundation.getDefaultLanguage()),
						url != null ? url.getCimageurl() : null,
						productEntityMap.isBshowimg(), false);
				nomatchTag.put(cvaluename, productAttributeTag);
				nomatchAttribtueByKey.put(key, nomatchTag);

				break;
			}
		}

		Map<String, List<ProductEntityMap>> attributesByKey = Maps.asMap(keys,
				k -> Lists.newArrayList(Collections2.filter(
						attributeList,
						a -> context.getListingID().equals(a.getClistingid())
								&& k.equals(a.getCkeyname())
								&& a.getCvaluename().equals(
										sourceAttributes.get(k))
								|| (filterProduct.containsKey(k) && a
										.getClistingid().equals(
												filterProduct.get(k).get(
														a.getCvaluename()))))));

		Map<String, List<ProductAttributeTag>> tags = Maps
				.transformValues(
						attributesByKey,
						list -> Lists.transform(
								list,
								e -> {
									ProductUrlWithSmallImage url = productByListingID
											.get(e.getClistingid());
									return new ProductAttributeTag(
											e.getClistingid(),
											e.getCkeyname(),
											e.getCvaluename(),
											url != null ? url.getCurl()
													: productUrlEnquityDao
															.getUrlByListingIdAndLanugage(
																	e.getClistingid(),
																	foundation
																			.getDefaultLanguage()),
											url != null ? url.getCimageurl()
													: null, e.isBshowimg(),
											true);
								}));

		Map<String, List<ProductAttributeTag>> productAttributeTags = new TreeMap<String, List<ProductAttributeTag>>();
		Iterator<Entry<String, List<ProductAttributeTag>>> iterator2 = tags
				.entrySet().iterator();
		while (iterator2.hasNext()) {
			List<ProductAttributeTag> list = new ArrayList<ProductAttributeTag>();
			Entry<String, List<ProductAttributeTag>> next = iterator2.next();
			String key = next.getKey();
			List<ProductAttributeTag> value = next.getValue();
			list.addAll(value);
			if (nomatchAttribtueByKey.containsKey(key)) {
				Iterator<Entry<String, ProductAttributeTag>> iterator3 = nomatchAttribtueByKey
						.get(key).entrySet().iterator();
				while (iterator3.hasNext()) {
					ProductAttributeTag value2 = iterator3.next().getValue();
					if (null != value2) {
						list.add(value2);
					}
				}
			}
			Collections.sort(list, new Comparator<ProductAttributeTag>() {
				public int compare(ProductAttributeTag arg0,
						ProductAttributeTag arg1) {
					return arg0.getValue().compareTo(arg1.getValue());
				}
			});
			productAttributeTags.put(key, list);
		}

		return new ProductCustomAttributes(context.getListingID(),
				productAttributeTags);
	}

	/**
	 * 过滤属性值个数不一样的商品
	 * 
	 * @param sourceMap
	 *            当前商品
	 * @param attributeMap
	 * @return
	 */
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

	private Map<String, Map<String, String>> changeModel(
			List<ProductEntityMap> attributeMap) {
		Map<String, Map<String, String>> productMap = new LinkedHashMap<String, Map<String, String>>();
		for (ProductEntityMap productEntityMap : attributeMap) {
			Map<String, String> attribueMap = Maps.newLinkedHashMap();
			String listingid = productEntityMap.getClistingid();
			if (productMap.containsKey(listingid)) {
				attribueMap = productMap.get(listingid);
			}
			String key = productEntityMap.getCkeyname();
			String value = productEntityMap.getCvaluename();
			attribueMap.put(key, value);
			productMap.put(listingid, attribueMap);
		}

		return productMap;
	}

	// 过滤
	private Map<String, Map<String, String>> filterProduct(String listingid,
			Map<String, Map<String, String>> productMap) {
		Map<String, String> sourceMap = productMap.get(listingid);
		if (sourceMap == null) {
			return null;
		}
		Iterator<Entry<String, String>> sourceIterator = sourceMap.entrySet()
				.iterator();
		Map<String, Map<String, String>> matchMap = new LinkedHashMap<String, Map<String, String>>();
		while (sourceIterator.hasNext()) {
			Entry<String, String> sourceAttribute = sourceIterator.next();
			String key = sourceAttribute.getKey();
			Iterator<Entry<String, Map<String, String>>> iterator = productMap
					.entrySet().iterator();
			Map<String, String> valueMap = new LinkedHashMap<String, String>();
			while (iterator.hasNext()) {
				Entry<String, Map<String, String>> next = iterator.next();
				String clistingid = next.getKey();
				Map<String, String> value2 = next.getValue();
				if (sourceMap.size() != value2.size()) {
					continue;
				}
				if (compareProductAttribute(sourceMap, key, value2)
						|| clistingid.equals(listingid)) {
					String valueName = value2.get(key);
					valueMap.put(valueName, clistingid);
				}
			}
			matchMap.put(key, valueMap);
		}

		return matchMap;
	}

	// 只允许有一个值不匹配
	private boolean compareProductAttribute(
			Map<String, String> sourceAttributes, String notMatchKey,
			Map<String, String> compareProduct) {
		Integer matchTotal = 0;
		Iterator<Entry<String, String>> iterator = compareProduct.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> sourceAttribute = iterator.next();
			String key = sourceAttribute.getKey();
			String value = sourceAttribute.getValue();
			if (key.equals(notMatchKey)) {
				continue;
			}
			if (sourceAttributes != null && sourceAttributes.get(key) != null
					&& sourceAttributes.get(key).equals(value)) {
				matchTotal++;
			}
		}
		if (matchTotal + 1 == sourceAttributes.size()) {
			if (sourceAttributes != null
					&& sourceAttributes.get(notMatchKey) != null
					&& sourceAttributes.get(notMatchKey).equals(
							compareProduct.get(notMatchKey))) {
				return false;
			}
			return true;
		}

		return false;
	}
}
