package services.product;

import interceptors.CacheResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import mapper.product.ProductBaseMapper;
import mapper.product.ProductInterceptUrlMapper;
import mapper.product.ProductTranslateMapper;
import play.Logger;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.price.PriceService;
import services.product.ProductSellingPointsService;
import valueobjects.price.Price;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductBadgePartContext;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import context.WebContext;
import dto.product.ProductInterceptUrl;
import dto.product.ProductSellingPoints;

@Singleton
public class ProductBadgeService implements IProductBadgeService {

	@Inject
	ProductBaseMapper productBaseMapper;
	@Inject
	PriceService priceService;
	@Inject
	Set<IProductBadgePartProvider> productBadgeExtraInfoProviders;
	@Inject
	ProductEnquiryService productEnquiryService;
	@Inject
	Set<IProductAttrIconProvider> IProductAttrIconProviders;
	@Inject
	Set<IProductAttrPartProvider> productAttrPartProvider;
	@Inject
	ProductTranslateMapper productTranslateMapper;
	@Inject
	ProductInterceptUrlMapper interceptUrlMapper;
	@Inject
	ProductSellingPointsService productSellingPointsService;

	@Inject
	FoundationService foundationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductBadgeService#getProductBadgesByListingIDs(java
	 * .util.List, int, int, java.lang.String, java.util.Date)
	 */
	@Override
	// @CacheResult("product.badges")
	public List<ProductBadge> getProductBadgesByListingIDs(
			List<String> listingIDs, int languageID, int site, String currency,
			Date date) {
//		if (listingIDs != null && listingIDs.size() > 0) {
//			List<ProductBadge> badge = getDBProductBadgesByListingIDs(
//					Sets.newHashSet(listingIDs), languageID);
//			Map<String, ProductBadge> badgeMap = Maps.newHashMap();
//
//			for (ProductBadge b : badge) {
//				badgeMap.put(b.getListingId(), b);
//			}
//
//			// unique product specs
//			List<IProductSpec> specs = FluentIterable.from(badgeMap.keySet())
//					.transform(id -> ProductSpecBuilder.build(id).get())
//					.toList();
//
//			// price
//			List<Price> prices = priceService.getPrice(specs, date, currency);
//			Map<String, Price> priceMap = Maps.uniqueIndex(
//					FluentIterable.from(prices).filter(p -> p != null), p -> p
//							.getSpec().getListingID());
//
//			// extension contents
//			// listingID -> List[Html]
//			ListMultimap<String, Html> combined = LinkedListMultimap.create();
//			ProductBadgePartContext context = new ProductBadgePartContext(
//					listingIDs, languageID, site, currency);
//			for (IProductBadgePartProvider s : Ordering
//					.natural()
//					.onResultOf(
//							(IProductBadgePartProvider b) -> b
//									.getDisplayOrder())
//					.sortedCopy(productBadgeExtraInfoProviders)) {
//				combined.putAll(Multimaps.forMap(s.getFragment(context)));
//			}
//
//			List<ProductBadge> relist = Lists.transform(listingIDs,
//					(String id) -> {
//						ProductBadge b = badgeMap.get(id);
//						if (b != null) {
//							b.setPrice(priceMap.get(id));
//							b.setExtended(combined.get(id));
//						}
//						return b;
//					});
//
//			return com.google.common.collect.FluentIterable.from(relist)
//					.filter(p -> p != null).toList();
//
//		}
//		return Lists.newArrayList();
		return getProductBadgesByListingIDsPrivate(listingIDs, languageID,site, currency,
				 date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductBadgeService#getProductBadgesByListingIDs(java
	 * .util.List, int, int, java.lang.String, java.util.Date, boolean, boolean)
	 */
	@Override
	public List<ProductBadge> getProductBadgesByListingIDs(
			List<String> listingids, int languageID, int site, String currency,
			Date date, boolean showPoint, boolean showIcon) {
		return getProductBadgesByListingIDsPrivate(listingids, languageID, site, currency, date, showPoint, showIcon);
	}

	/*
	 * 新的产品页面的产品徽章获取
	 */
	@Override
	@CacheResult("product.badges")
	public List<ProductBadge> getNewProductBadgesByListingIds(
			List<String> listingids, int languageID, int site, String currency,
			Date date) {
		if (listingids == null || listingids.size() == 0) {
			return Lists.newArrayList();
		}
		List<ProductBadge> badges = getProductBadgesByListingIDs(listingids,
				languageID, site, currency, date);

		// html属性
		Map<String, Map<String, Html>> icons = productAttrPartProvider.size() > 0 ? productAttrPartProvider
				.iterator().next().getHtml(listingids)
				: null;
		for (ProductBadge b : badges) {
			if (b != null && icons != null)
				b.getHtmlmap().putAll(icons.get(b.getListingId()));
		}
		return badges;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductBadgeService#getDBProductBadgesByListingIDs(
	 * java.util.Collection, int)
	 */
	@Override
	@CacheResult("product.badges")
	public List<ProductBadge> getDBProductBadgesByListingIDs(
			Collection<String> listingIDs, int languageID) {
		List<ProductBadge> mainBadges = new ArrayList<ProductBadge>();
		List<ProductInterceptUrl> interceptUrlList = interceptUrlMapper
				.getAllListingid();
		if (!interceptUrlList.isEmpty() && interceptUrlList != null) {
			for (ProductInterceptUrl url : interceptUrlList) {
				if (listingIDs.contains(url.getClistingid())) {
					Logger.debug(
							"listingid=================================={}",
							url.getClistingid());
					List<ProductBadge> productBadgeInterCept = productBaseMapper
							.getProductBadgeByListingIds(url.getClistingid(),
									languageID);
					for (ProductBadge productBadge : productBadgeInterCept) {
						mainBadges.add(productBadge);
					}
				}
			}
		}
		List<ProductBadge> productBadgelists = productBaseMapper
				.getProductBadgeByListingIDs(listingIDs, languageID);
		for (ProductBadge productBadge : productBadgelists) {
			mainBadges.add(productBadge);
		}

		return mainBadges;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductBadgeService#getByListing(java.lang.String,
	 * int)
	 */
	@Override
	@CacheResult("product.badges")
	public ProductBadge getByListing(String listingid, int languageid,
			String currency) {
		ProductBadge badge = productBaseMapper.getProductBadgeByListingID(
				listingid, languageid);
		if (badge != null) {
			Price price = priceService.getPrice(listingid, currency);
			badge.setPrice(price);
			return badge;
		}
		return null;
	}

	@Override
	public List<ProductBadge> getProductBadgesByListingIDs(
			List<String> listingIDs, Date date, WebContext ctx) {

		return this.getProductBadgesByListingIDs(listingIDs,
				this.foundationService.getLanguage(ctx),
				this.foundationService.getSiteID(ctx),
				this.foundationService.getCurrency(ctx), date);
	}

	@Override
	public List<ProductBadge> getProductBadgesByListingIDs(
			List<String> listingids, Date date, boolean showPoint,
			boolean showIcon, WebContext ctx) {

		return this.getProductBadgesByListingIDs(listingids,
				this.foundationService.getLanguage(ctx),
				this.foundationService.getSiteID(ctx),
				this.foundationService.getCurrency(ctx), date, showPoint,
				showIcon);
	}

	@Override
	public List<ProductBadge> getDBProductBadgesByListingIDs(
			Collection<String> listingIDs, WebContext ctx) {
		return this.getDBProductBadgesByListingIDs(listingIDs,
				this.foundationService.getLanguage(ctx));
	}

	@Override
	public ProductBadge getByListing(String listingid, WebContext ctx) {
		return this.getByListing(listingid,
				this.foundationService.getLanguage(ctx),
				this.foundationService.getCurrency(ctx));
	}
	
	/**
	 * 原getProductBadgesByListingIDs方法
	 * @param listingIDs
	 * @param languageID
	 * @param site
	 * @param currency
	 * @param date
	 * @return
	 */
	@CacheResult("product.badges")
	private List<ProductBadge> getProductBadgesByListingIDsPrivate(
			List<String> listingIDs, int languageID, int site, String currency,
			Date date) {
		if (listingIDs != null && listingIDs.size() > 0) {
			List<ProductBadge> badge = getDBProductBadgesByListingIDs(
					Sets.newHashSet(listingIDs), languageID);
			Map<String, ProductBadge> badgeMap = Maps.newHashMap();

			for (ProductBadge b : badge) {
				badgeMap.put(b.getListingId(), b);
			}

			// unique product specs
			List<IProductSpec> specs = FluentIterable.from(badgeMap.keySet())
					.transform(id -> ProductSpecBuilder.build(id).get())
					.toList();

			// price
			List<Price> prices = priceService.getPrice(specs, date, currency);
			Map<String, Price> priceMap = Maps.uniqueIndex(
					FluentIterable.from(prices).filter(p -> p != null), p -> p
							.getSpec().getListingID());

			// extension contents
			// listingID -> List[Html]
			ListMultimap<String, Html> combined = LinkedListMultimap.create();
			ProductBadgePartContext context = new ProductBadgePartContext(
					listingIDs, languageID, site, currency);
			for (IProductBadgePartProvider s : Ordering
					.natural()
					.onResultOf(
							(IProductBadgePartProvider b) -> b
									.getDisplayOrder())
					.sortedCopy(productBadgeExtraInfoProviders)) {
				combined.putAll(Multimaps.forMap(s.getFragment(context)));
			}

			List<ProductBadge> relist = Lists.transform(listingIDs,
					(String id) -> {
						ProductBadge b = badgeMap.get(id);
						if (b != null) {
							b.setPrice(priceMap.get(id));
							b.setExtended(combined.get(id));
						}
						return b;
					});

			return com.google.common.collect.FluentIterable.from(relist)
					.filter(p -> p != null).toList();

		}
		return Lists.newArrayList();
	}
	
	@CacheResult("product.badges")
	private List<ProductBadge> getProductBadgesByListingIDsPrivate(
			List<String> listingids, int languageID, int site, String currency,
			Date date, boolean showPoint, boolean showIcon) {
		if (listingids == null || listingids.size() == 0) {
			return Lists.newArrayList();
		}
		List<ProductBadge> badges = getProductBadgesByListingIDs(listingids,
				languageID, site, currency, date);
		if (showPoint) {
			// 为每个产品存入卖点
			List<ProductSellingPoints> sellingPoints = productSellingPointsService
					.getProductSellingPointsByListingIds(listingids, languageID);
			Multimap<String, ProductSellingPoints> spMap = Multimaps.index(
					sellingPoints, sps -> sps.getClistingid());
			if (spMap != null && spMap.size() > 0) {
				for (ProductBadge b : badges) {
					if (b != null)
						b.setSellingPoints(Lists.newArrayList(spMap.get(b
								.getListingId())));
				}
			}
		}
		if (showIcon) {
			Map<String, Html> icons = IProductAttrIconProviders.size() > 0 ? IProductAttrIconProviders
					.iterator().next().getHtml(listingids)
					: null;
			for (ProductBadge b : badges) {
				if (b != null && icons != null)
					b.getExtended().add(icons.get(b.getListingId()));
			}
		}
		return badges;
	}
}
