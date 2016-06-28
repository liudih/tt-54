package com.tomtop.product.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.models.bo.CollectCountBo;
import com.tomtop.product.models.bo.ListingIdNumBo;
import com.tomtop.product.models.bo.ProductBasePriceInfoBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewInfoBo;
import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;
import com.tomtop.product.models.dto.ProductBadge;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.services.ICategoryService;
import com.tomtop.product.services.IProductCollectService;
import com.tomtop.product.services.IProductService;
import com.tomtop.product.services.price.IPriceService;
import com.tomtop.product.services.price.impl.SingleProductSpec;

/**
 * 产品服务工具类
 * 
 * @author liulj
 *
 */
@Component("productBaseUtils")
public class ProductBaseUtils {

	@Autowired
	private IPriceService priceService;

	@Resource(name = "productService")
	private IProductService productService;

	@Resource(name = "priceInterceptUtils")
	private PriceInterceptUtils priceIntercept;

	@Resource(name = "productCollectService")
	private IProductCollectService collectService;

	@Resource(name = "categoryService")
	private ICategoryService categoryService;

	/**
	 * 获取子类目
	 * 
	 * @param cates
	 * @param parents
	 * @param lang
	 * @param client
	 * @return
	 */
	public List<CategoryWebsiteWithNameDto> getChildCategorys(
			List<CategoryWebsiteWithNameDto> cates, int lang, int client) {
		List<CategoryWebsiteWithNameDto> dto = categoryService
				.getMultiChildCategoriesByDisplay(
						Lists.transform(cates, p -> p.getIcategoryid()), lang,
						client, true);
		if (dto != null && dto.size() > 0) {
			cates.addAll(dto);
			dto = categoryService.getMultiChildCategoriesByDisplay(
					Lists.transform(dto, p -> p.getIcategoryid()), lang,
					client, true);
			if (dto != null && dto.size() > 0) {
				cates.addAll(dto);
				dto = categoryService.getMultiChildCategoriesByDisplay(
						Lists.transform(cates, p -> p.getIcategoryid()), lang,
						client, true);
				if (dto != null && dto.size() > 0) {
					cates.addAll(dto);
				}
			}
		}
		return cates;
	}

	/**
	 * 获取产品的信息和price
	 * 
	 * @param listings
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	public List<ProductBasePriceInfoBo> getProductBasePriceByListings(
			List<String> listings, String currency, int client, int lang) {
		// TODO Auto-generated method stub
		if (listings != null && listings.size() > 0) {
			listings = listings.stream().distinct()
					.collect(Collectors.toList());
			List<ProductBadge> products = productService
					.getProductBadgeListByListingIds(listings, lang, client, 1);
			if (products != null && products.size() > 0) {
				Map<String, Price> prices = Maps.uniqueIndex(priceService
						.getPrice(Lists.transform(listings,
								p -> new SingleProductSpec(p, 1)),
								new PriceCalculationContext(currency)), p -> p
						.getListingId());
				return Lists.transform(
						products,
						p -> {
							ProductBasePriceInfoBo bo = BeanUtils.mapFromClass(
									p, ProductBasePriceInfoBo.class);
							setObjPrice(bo, prices.get(p.getListingId()),
									currency);
							return bo;
						});
			}
		}
		return Lists.newArrayList();
	}

	/**
	 * 获取产品的信息和price
	 * 
	 * @param listings
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	public List<ProductBasePriceInfoBo> getProductBasePriceByListingsNum(
			List<ListingIdNumBo> listings, String currency, int client, int lang) {
		// TODO Auto-generated method stub
		if (listings != null && listings.size() > 0
				&& StringUtils.isNotBlank(currency)) {

			List<ProductBadge> products = productService
					.getProductBadgeListByListingIds(
							Lists.transform(listings, p -> p.getListingId()),
							lang, client, 1);
			if (products != null && products.size() > 0) {
				Map<String, Price> prices = Maps.uniqueIndex(priceService
						.getPrice(Lists.transform(
								listings,
								p -> new SingleProductSpec(p.getListingId(), p
										.getNum())),
								new PriceCalculationContext(currency)), p -> p
						.getListingId());
				return Lists.transform(
						products,
						p -> {
							ProductBasePriceInfoBo bo = BeanUtils.mapFromClass(
									p, ProductBasePriceInfoBo.class);
							setObjPrice(bo, prices.get(p.getListingId()),
									currency);
							return bo;
						});
			}

		}
		return Lists.newArrayList();
	}

	/**
	 * 获取产品的信息和price
	 * 
	 * @param listings
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	public ProductBasePriceInfoBo getProductBasePriceByListing(String listing,
			String currency, int client, int lang) {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(listing) && StringUtils.isNotBlank(currency)) {
			ProductBadge product = productService.getProductBadgeByListingId(
					listing, lang, client);
			if (product != null) {
				ProductBasePriceInfoBo bo = BeanUtils.mapFromClass(product,
						ProductBasePriceInfoBo.class);
				setObjPrice(bo, priceService.getPrice(new SingleProductSpec(
						listing, 1), new PriceCalculationContext(currency)),
						currency);
				return bo;
			}
		}
		return null;
	}

	public List<ProductBasePriceReviewCollectInfoBo> getProductBasePriceReviewCollectByListings(
			List<String> listings, String currency, int client, int lang) {
		if (listings != null && listings.size() > 0
				&& StringUtils.isNotBlank(currency)) {
			listings = listings.stream().distinct()
					.collect(Collectors.toList());
			List<ProductBadge> products = productService
					.getProductBadgeListByListingIds(listings, lang, client, 1);
			if (products != null && products.size() > 0) {
				Map<String, Price> prices = Maps.uniqueIndex(priceService
						.getPrice(Lists.transform(listings,
								p -> new SingleProductSpec(p, 1)),
								new PriceCalculationContext(currency)), p -> p
						.getListingId());
				Map<String, Integer> collects = collectService
						.getCollectCountByListingIds(listings)
						.stream()
						.collect(
								Collectors.toMap(CollectCountBo::getListingId,
										CollectCountBo::getCollectCount));
				return Lists
						.transform(
								products,
								p -> {
									ProductBasePriceReviewCollectInfoBo bo = BeanUtils
											.mapFromClass(
													p,
													ProductBasePriceReviewCollectInfoBo.class);
									bo.setCollectNum(collects.get(p
											.getListingId()));
									setObjPrice(bo,
											prices.get(p.getListingId()),
											currency);
									return bo;
								});
			}

		}
		return Lists.newArrayList();
	}

	/**
	 * 返回产品的信息和价格和评论星级
	 * 
	 * @param listings
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	public List<ProductBasePriceReviewInfoBo> getProductBasePriceReviewByListings(
			List<String> listings, String currency, int client, int lang) {
		// TODO Auto-generated method stub
		if (listings != null && listings.size() > 0
				&& StringUtils.isNotBlank(currency)) {
			listings = listings.stream().distinct()
					.collect(Collectors.toList());
			List<ProductBadge> products = productService
					.findDetailProductIdInfo(listings, lang, client);
			if (products != null && products.size() > 0) {
				Map<String, Price> prices = Maps.uniqueIndex(priceService
						.getPrice(Lists.transform(listings,
								p -> new SingleProductSpec(p, 1)),
								new PriceCalculationContext(currency)), p -> p
						.getListingId());
				return Lists
						.transform(
								products,
								p -> {
									ProductBasePriceReviewInfoBo bo = BeanUtils
											.mapFromClass(
													p,
													ProductBasePriceReviewInfoBo.class);
									setObjPrice(bo,
											prices.get(p.getListingId()),
											currency);
									return bo;
								});
			}
		}
		return Lists.newArrayList();
	}

	/**
	 * 设置对象的价格
	 * 
	 * @param bo
	 * @param price
	 */
	public void setObjPrice(ProductBasePriceInfoBo bo, Price price,
			String currency) {
		if (price != null) {
			bo.setNowprice(priceIntercept.money(price.getPrice(), currency));
			bo.setOrigprice(priceIntercept.money(price.getUnitBasePrice(),
					currency));
			bo.setSymbol(price.getSymbol());
		}
	}
}