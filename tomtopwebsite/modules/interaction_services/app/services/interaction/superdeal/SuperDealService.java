package services.interaction.superdeal;

import interceptors.CacheResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;

import play.Logger;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.base.utils.DateFormatUtils;
import services.home.SuperDealsEnquiry;
import services.interaction.ISuperDealService;
import services.interaction.MemberBrowseHistoryService;
import services.order.IOrderService;
import services.product.CategoryEnquiryService;
import valueobjects.base.Page;
import valueobjects.interaction.SuperDealContext;
import valueobjects.product.ProductPartInformation;

import com.google.common.collect.Collections2;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

import context.WebContext;
import dao.interaction.IBrowseEnquiryDao;
import dao.interaction.ISuperDealDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.impl.ProductSalePriceEnquiryDao;
import dto.ProductSalePriceLite;
import dto.SystemParameter;
import dto.TopBrowseAndSaleCount;
import dto.interaction.ListingCount;
import dto.interaction.SuperDeal;
import dto.product.ProductBase;

public class SuperDealService implements ISuperDealService {

	@Inject
	IBrowseEnquiryDao browseEnquityDao;

	@Inject
	IOrderService orderService;

	@Inject
	SuperDealsEnquiry superDealsEnquiry;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	IProductBaseEnquiryDao productBaseEnquiryDao;

	@Inject
	ProductSalePriceEnquiryDao productSalePriceEnquiryDao;

	@Inject
	FoundationService foundationService;

	@Inject
	SystemParameterService systemParameterService;

	@Inject
	ISuperDealDao superDealDao;

	@Inject
	MemberBrowseHistoryService memberBrowseHistoryService;

	public Set<String> getSuperDealsListingIds(SuperDealContext context) {
		Integer siteId = context.getSiteId();
		Integer languageId = context.getLanguageId();
		Integer browseTimeRange = context.getBrowseTimeRange();
		Integer saleTimeRange = context.getSaleTimeRange();
		Integer browseLimit = context.getBrowseLimit();
		Integer saleLimit = context.getSaleLimit();
		List<Double> priceRange = context.getPriceRange();
		List<Double> discountRange = context.getDiscountRange();

		// 获取搜素引擎符合条件的listingId
		List<String> searchEngineListingIds = getListingIdsOnSearchEngine(
				siteId, languageId, priceRange, discountRange);
		Set<String> searchEngineListingIdsSet = Sets
				.newHashSet(searchEngineListingIds);
		Logger.debug("searchEngineListingIds===size================"
				+ searchEngineListingIds.size());
		// 获取历史记录
		List<TopBrowseAndSaleCount> browseCount = getTopBrowseByTimeRange(
				browseTimeRange, searchEngineListingIds);

		List<String> browseListingIds = Lists.transform(browseCount,
				i -> i.getListingid());
		Map<String, TopBrowseAndSaleCount> filterBrowseCountMap = Maps
				.uniqueIndex(browseCount, i -> i.getListingid());
		Logger.debug("browseCount==size=================" + browseCount.size());

		// 获取销售记录
		List<TopBrowseAndSaleCount> saleCount = getTopSaleByTimeRange(saleTimeRange);
		List<TopBrowseAndSaleCount> filterSaleCount = Lists
				.newArrayList(Collections2.filter(saleCount,
						i -> (searchEngineListingIdsSet.contains(i
								.getListingid()))));
		Map<String, TopBrowseAndSaleCount> filterSaleCountMap = Maps
				.uniqueIndex(filterSaleCount, i -> i.getListingid());
		List<String> saleListingIds = Lists.transform(filterSaleCount,
				i -> i.getListingid());
		Logger.debug("saleCount====size===============" + saleCount.size());

		Set<String> qualifiedListingIds = Sets.newHashSet();
		qualifiedListingIds.addAll(browseListingIds);
		qualifiedListingIds.addAll(saleListingIds);

		// 根据产品线分组
		List<TopBrowseAndSaleCount> newBrowseCount = Lists.newArrayList();
		List<TopBrowseAndSaleCount> newSaleCount = Lists.newArrayList();
		for (String listingId : qualifiedListingIds) {
			Integer rootId = categoryEnquiryService
					.getRootCategoryIdByListingId(listingId, languageId);

			if (browseListingIds.contains(listingId)) {
				TopBrowseAndSaleCount browseCount2 = filterBrowseCountMap
						.get(listingId);
				newBrowseCount.add(new TopBrowseAndSaleCount(listingId, null,
						rootId, browseCount2.getBrowsecount(), browseCount2
								.getSku()));
			}
			if (saleListingIds.contains(listingId)) {
				TopBrowseAndSaleCount saleCount2 = filterSaleCountMap
						.get(listingId);
				newSaleCount.add(new TopBrowseAndSaleCount(listingId,
						saleCount2.getSalecount(), rootId, null, null));
			}
		}

		Set<Integer> browseRootId = Sets.newHashSet(Collections2.transform(
				newBrowseCount, i -> i.getRootCategoryId()));
		browseRootId = Sets.newHashSet(Collections2.filter(browseRootId,
				i -> null != i));
		Set<Integer> saleRootId = Sets.newHashSet(Collections2.transform(
				newSaleCount, i -> i.getRootCategoryId()));
		saleRootId = Sets.newHashSet(Collections2.filter(saleRootId,
				i -> null != i));
		Map<Integer, List<TopBrowseAndSaleCount>> browseRootIdCountMap = Maps
				.newHashMap();

		if (null != browseRootId && browseRootId.size() > 0) {
			browseRootIdCountMap = Maps.asMap(
					browseRootId,
					i -> {
						List<TopBrowseAndSaleCount> temp = Lists
								.newArrayList(Collections2.filter(
										newBrowseCount,
										j -> (j.getRootCategoryId() == i)));
						temp.sort(new Comparator<TopBrowseAndSaleCount>() {
							@Override
							public int compare(TopBrowseAndSaleCount o1,
									TopBrowseAndSaleCount o2) {
								return o2.getBrowsecount().compareTo(
										o1.getBrowsecount());
							}
						});
						temp.stream().limit(browseLimit);
						return temp;
					});
		}

		Map<Integer, List<TopBrowseAndSaleCount>> saleRootIdCountMap = Maps
				.newHashMap();
		if (null != saleRootId && saleRootId.size() > 0) {
			saleRootIdCountMap = Maps.asMap(
					saleRootId,
					i -> {
						List<TopBrowseAndSaleCount> temp = Lists
								.newArrayList(Collections2.filter(newSaleCount,
										j -> (j.getRootCategoryId() == i)));
						temp.sort(new Comparator<TopBrowseAndSaleCount>() {
							@Override
							public int compare(TopBrowseAndSaleCount o1,
									TopBrowseAndSaleCount o2) {
								return o2.getBrowsecount().compareTo(
										o1.getBrowsecount());
							}
						});
						temp.stream().limit(saleLimit);
						return temp;
					});
		}
		Set<String> listingSet = Sets.newHashSet();
		if (null != browseRootIdCountMap && browseRootIdCountMap.size() > 0) {
			for (List<TopBrowseAndSaleCount> list : browseRootIdCountMap
					.values()) {
				listingSet.addAll(Lists.newArrayList(Lists.transform(list,
						i -> i.getListingid())));
			}
		}

		if (null != saleRootIdCountMap && saleRootIdCountMap.size() > 0) {
			for (List<TopBrowseAndSaleCount> list : saleRootIdCountMap.values()) {
				List<String> listingIdss = Lists.transform(list,
						i -> i.getListingid());
				listingSet.addAll(listingIdss);
			}
		}

		if (null != listingSet && listingSet.size() > 0) {
			Logger.debug("final get listingId size====================="
					+ listingSet.size());
		}

		return listingSet;
	}

	/**
	 * getTopBrowseListingIds
	 * 
	 * @param timeRange
	 * @return 返回在timeRange范围内，浏览最多的产品的listingId
	 */
	private List<TopBrowseAndSaleCount> getTopBrowseByTimeRange(
			Integer timeRange, List<String> listingIds) {
		if (listingIds == null || listingIds.size() == 0) {
			return Lists.newArrayList();
		}
		int total = listingIds.size();
		int batchSize = 200;
		int totalPages = total / batchSize;
		Set<Integer> pageset = ContiguousSet.create(
				Range.closed(0, totalPages), DiscreteDomain.integers());
		Stream<List<TopBrowseAndSaleCount>> dbresultlist = pageset
				.parallelStream()
				.map(page -> {
					int fromindex = (batchSize * (page));
					int toindex = fromindex + batchSize;
					if (toindex > listingIds.size())
						toindex = listingIds.size();
					Logger.debug("supperdeal page-> {}  from {}  to {}", page,
							fromindex, toindex);
					List<String> newlist = listingIds.subList(fromindex,
							toindex);
					List<TopBrowseAndSaleCount> listorderbyBrowse = browseEnquityDao
							.getTopBrowseByTimeRange(timeRange, newlist);
					return listorderbyBrowse;
				});
		List<TopBrowseAndSaleCount> resultlist = Lists.newArrayList();
		dbresultlist.forEach(p -> {
			if (p != null) {
				resultlist.addAll(p);
			}
		});
		Logger.debug("get super deal getTopBrowseListingIds rows - > {}",
				resultlist.size());
		return resultlist;
	}

	/**
	 * @param timeRange
	 * @return 返回在timeRange范围内各产品线销售排名前limit的listingIds
	 */
	public List<TopBrowseAndSaleCount> getTopSaleByTimeRange(Integer timeRange) {
		return orderService.getTopSaleByTimeRange(timeRange);
	}

	/**
	 * getListingIdsOnSearchEngine（找出符合super deal条件的listingIds）
	 */
	public List<String> getListingIdsOnSearchEngine(Integer siteId,
			Integer language, List<Double> priceRange,
			List<Double> discountRange) {
		return superDealsEnquiry.getAllQualifiedListingIds(siteId, language,
				priceRange, discountRange);
	}

	public void addNewSuperDealsByBatch(Set<String> listingIds,
			Integer languageId, Integer websiteId) {
		if (null != listingIds && listingIds.size() > 0) {
			for (String listingId : listingIds) {
				Integer rootId = categoryEnquiryService
						.getRootCategoryIdByListingId(listingId, languageId);
				String sku = productBaseEnquiryDao
						.getProductSkuByListingId(listingId);
				SuperDeal superDeal = new SuperDeal();
				superDeal.setClistingid(listingId);
				superDeal.setIcategoryrootid(rootId);
				superDeal.setCsku(sku);
				superDeal.setBshow(false);
				superDeal.setCcreateuser("Auto");
				superDeal.setIwebsiteid(websiteId);
				superDealDao.addNewSuperDeal(superDeal);
			}
		}
	}

	public void handleSuperDeal(Integer siteId, Integer languageId) {
		SuperDealContext context = getSuperDealContext(siteId, languageId);
		Set<String> listingIds = getSuperDealsListingIds(context);

		Logger.debug("handle superDeal============================="
				+ listingIds.size());
		if (null != listingIds && listingIds.size() > 0) {
			superDealDao.deleteOldAutoGetSuperDealsByCreateUser("Auto");
			addNewSuperDealsByBatch(listingIds, languageId, siteId);
		}
	}

	public List<SuperDeal> getSuperDealByPageAndRootCategoryId(Integer page,
			Integer perPage, Integer rootCategoryId, String sku,
			Integer websiteId) {
		return superDealDao.getSuperDealByPageAndRootCategoryId(page, perPage,
				rootCategoryId, sku, websiteId);
	}

	public Integer getSuperDealCount(Integer rootCategoryId, String listingId,
			Integer websiteId) {
		return superDealDao.getSuperDealCount(rootCategoryId, listingId,
				websiteId);
	}

	public SuperDeal getSuperDealById(Integer id) {
		return superDealDao.getSuperDealById(id);
	}

	public boolean deleteSuperDealByIid(Integer id) {
		return superDealDao.deleteSuperDealById(id);
	}

	public ProductPartInformation getProductPartlInfoBySku(String sku,
			Integer siteId, Integer status, Boolean bvisible,
			Integer languageId, Boolean bactivity) {
		ProductPartInformation partInformation = new ProductPartInformation();
		ProductBase base = productBaseEnquiryDao.getProductListingId(sku,
				siteId, status, bvisible, bactivity);
		if (null != base) {
			String listingId = base.getClistingid();
			Integer rootCategoryId = categoryEnquiryService
					.getRootCategoryIdByListingId(listingId, languageId);
			if (null != rootCategoryId) {
				String rootCategoryName = categoryEnquiryService
						.getRootCategoryNameByRootIdAndLanguageId(
								rootCategoryId, languageId);
				if (null != rootCategoryName) {
					partInformation.setRootCategoryName(rootCategoryName);
				}
				partInformation.setRootCategoryId(rootCategoryId);
			}
			ProductSalePriceLite salePrice = productSalePriceEnquiryDao
					.getProductSalePriceLite(listingId);
			if (null != salePrice) {
				String endDateString = DateFormatUtils
						.getDateTimeYYYYMMDD(salePrice.getEndDate());
				String beginDateString = DateFormatUtils
						.getDateTimeYYYYMMDD(salePrice.getBeginDate());
				partInformation.setBeginDate(beginDateString);
				partInformation.setEndDate(endDateString);
				partInformation.setPrice(salePrice.getPrice());
				partInformation.setSalePrice(salePrice.getSalePrice());
			}
			partInformation.setListingId(listingId);
			partInformation.setSku(sku);
			partInformation.setCostPrice(base.getFcostprice());
			return partInformation;
		} else {
			return null;
		}
	}

	public Boolean addNewSuperDeal(dto.interaction.SuperDeal superDeal) {
		Integer result = superDealDao.addNewSuperDeal(superDeal);
		return result > 0 ? true : false;
	}

	public SuperDealContext getSuperDealContext(Integer siteId,
			Integer languageId) {
		Double priceLower = Double.valueOf(systemParameterService
				.getSystemParameter(siteId, languageId,
						"SuperDealPriceRangeLower"));
		Double priceHigher = Double.valueOf(systemParameterService
				.getSystemParameter(siteId, languageId,
						"SuperDealPriceRangeHigher"));
		List<Double> priceRange = Lists.newArrayList(priceLower, priceHigher);
		Integer browseTimeRange = Integer.valueOf(systemParameterService
				.getSystemParameter(siteId, languageId,
						"SuperDealBrowseTimeRange"));
		Integer browseLimitPerLine = Integer.valueOf(systemParameterService
				.getSystemParameter(siteId, languageId,
						"SuperDealBrowseLimitPerLine"));
		Integer saleTimeRange = Integer.valueOf(systemParameterService
				.getSystemParameter(siteId, languageId,
						"SuperDealSaleTimeRange"));
		Integer saleLimitPerLine = Integer.valueOf(systemParameterService
				.getSystemParameter(siteId, languageId,
						"SuperDealSaleLimitPerLine"));
		Double discountRangeLower = Double.valueOf(systemParameterService
				.getSystemParameter(siteId, languageId,
						"SuperDealDiscountRangeLower"));
		Double discountRangeHigher = Double.valueOf(systemParameterService
				.getSystemParameter(siteId, languageId,
						"SuperDealDiscountRangeHigher"));
		List<Double> discountRange = Lists.newArrayList(discountRangeLower,
				discountRangeHigher);
		SuperDealContext context = new SuperDealContext(siteId, languageId,
				browseLimitPerLine, saleLimitPerLine, browseTimeRange,
				saleTimeRange, priceRange, discountRange);
		return context;
	}

	public Boolean changeSuperDealSearchCondition(SuperDealContext context) {
		Integer siteId = context.getSiteId();
		Integer languageId = context.getLanguageId();
		Boolean result = true;
		try {
			SystemParameter systemParameter1 = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "SuperDealPriceRangeLower");
			systemParameter1.setCparametervalue(context.getPriceRange().get(0)
					.toString());
			systemParameterService.alterSysParameter(systemParameter1);
			SystemParameter systemParameter2 = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "SuperDealPriceRangeHigher");
			systemParameter2.setCparametervalue(context.getPriceRange().get(1)
					.toString());
			systemParameterService.alterSysParameter(systemParameter2);
			SystemParameter systemParameter3 = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "SuperDealBrowseTimeRange");
			systemParameter3.setCparametervalue(context.getBrowseTimeRange()
					.toString());
			systemParameterService.alterSysParameter(systemParameter3);
			SystemParameter systemParameter4 = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "SuperDealBrowseLimitPerLine");
			systemParameter4.setCparametervalue(context.getBrowseLimit()
					.toString());
			systemParameterService.alterSysParameter(systemParameter4);

			SystemParameter systemParameter5 = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "SuperDealSaleTimeRange");
			systemParameter5.setCparametervalue(context.getSaleTimeRange()
					.toString());
			systemParameterService.alterSysParameter(systemParameter5);

			SystemParameter systemParameter6 = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "SuperDealSaleLimitPerLine");
			systemParameter6.setCparametervalue(context.getSaleLimit()
					.toString());
			systemParameterService.alterSysParameter(systemParameter6);

			SystemParameter systemParameter7 = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "SuperDealDiscountRangeLower");
			systemParameter7.setCparametervalue(context.getDiscountRange()
					.get(0).toString());
			systemParameterService.alterSysParameter(systemParameter7);

			SystemParameter systemParameter8 = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "SuperDealDiscountRangeHigher");
			systemParameter8.setCparametervalue(context.getDiscountRange()
					.get(1).toString());
			systemParameterService.alterSysParameter(systemParameter8);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	@CacheResult("product.badges")
	public List<String> getSuperDealListingIds(Integer limit, Integer websiteId) {
		Set<String> listingSet = Sets.newHashSet();
		List<String> listingIdsAuto = Lists.newArrayList();
		List<String> listingIdsNotAuto = superDealDao.getSuperDealListingIds(
				false, limit, websiteId);
		if (null != listingIdsNotAuto && listingIdsNotAuto.size() > 0) {
			listingSet.addAll(listingIdsNotAuto);
			if (listingIdsNotAuto.size() < limit) {
				Integer left = limit - listingIdsNotAuto.size();
				listingIdsAuto = superDealDao.getSuperDealListingIds(true,
						left, websiteId);
				if (null != listingIdsAuto && listingIdsAuto.size() > 0) {
					listingSet.addAll(listingIdsAuto);
				}
			}
		} else {
			listingIdsAuto = superDealDao.getSuperDealListingIds(true, limit,
					websiteId);
			listingSet.addAll(listingIdsAuto);
		}
		listingSet = Sets.newHashSet(Collections2.filter(listingSet,
				i -> null != i));
		return Lists.newArrayList(listingSet);
	}

	public Boolean updateSuperDealBshow(Integer id, Boolean bshow) {
		return superDealDao.updateSuperDealBshow(id, bshow);
	}

	/**
	 * @param siteId
	 * @param lang
	 * @param allListingIds
	 * @return 每类显示一个产品（前提保证搜索引擎中每个类别都有数据）
	 */
	public Set<String> getOneListingIdEveryRootCategory(Integer siteId,
			Integer lang, Set<String> allListingIds) {
		List<Integer> rootCategoryIdList = categoryEnquiryService
				.getAllRootCategoryIdBySite(siteId);
		if (null != rootCategoryIdList && rootCategoryIdList.size() > 0) {
			Integer pag = 0;
			Integer per = 1;
			for (Integer rootId : rootCategoryIdList) {
				Page<String> pageListing = superDealsEnquiry
						.getSuperDealsListingIdPage(siteId, lang, pag, per,
								rootId);
				if (null != pageListing.getList()
						&& pageListing.getList().size() > 0) {
					String listingId = pageListing.getList().get(0);
					if (!allListingIds.contains(listingId)) {
						allListingIds.add(listingId);
					}
				}
			}
		}
		return allListingIds;
	}

	/**
	 * @param cookieID
	 * @return 判断cookie中是否有值，存在则推荐该用户浏览历史最后一个产品所在品类的产品
	 */
	public List<String> getListingsByLastViewRootCateogryId(String cookieID,
			Integer perPage, Integer siteId, Integer lang) {
		List<String> listingIds = Lists.newArrayList();
		if (cookieID != null) {
			String lastViewListingId = memberBrowseHistoryService
					.getLastViewListingIdBySiteIdAndLtc(siteId, cookieID);
			if (null != lastViewListingId) {
				Integer rootCategoryId = categoryEnquiryService
						.getRootCategoryIdBySiteIdAndListingId(siteId,
								lastViewListingId);
				if (null != rootCategoryId) {
					Page<String> pageListing = superDealsEnquiry
							.getSuperDealsListingIdPage(siteId, lang, 0,
									perPage, rootCategoryId);
					listingIds.addAll(pageListing.getList());
				}
			}
		}
		return listingIds;
	}

	@Override
	public List<String> getListingIdsByPage(Integer pageNum, Integer pageSize,
			WebContext context) {
		int websiteId = foundationService.getSiteID(context);
		return superDealDao.getListingIds(websiteId, pageNum, pageSize);
	}

	public Page<String> getSDPageBySiteId(int siteId, Integer rootCategoryId, int page, Integer pageSize) {
		int startIndex = (page-1)*pageSize;
		List<String> plListings =  superDealDao.getSDPageBySiteId(siteId,rootCategoryId,pageSize,startIndex);
		int count = superDealDao.getSDCountBySiteId(siteId,rootCategoryId);
		return new Page<String>(plListings, count, page, pageSize);
	}
}
