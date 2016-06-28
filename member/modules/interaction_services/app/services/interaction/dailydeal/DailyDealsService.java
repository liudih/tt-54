package services.interaction.dailydeal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mapper.product.CategoryLabelMapper;
import mapper.product.ProductBaseMapper;
import play.Logger;
import services.ILanguageService;
import services.base.utils.DateFormatUtils;
import services.base.utils.StringUtils;
import services.base.SystemParameterService;
import services.interaction.InteractionInformationService;
import services.price.PriceService;
import services.product.DailyDealUpdateService;
import services.product.ProductSalePriceService;
import services.search.IDailyDealEnquiryService;
import valueobjects.price.Price;
import valueobjects.product.ProductViewCount;
import valueobjects.product.sort.ProductComparator;
import valueobjects.product.sort.ProductSort;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import dao.interaction.ISuperDealDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductCategoryEnquiryDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductLabelUpdateDao;
import dao.product.IProductSalePriceEnquiryDao;
import dao.product.IProductSalePriceUpdateDao;
import dao.product.IProductViewCountEnquiryDao;
import dto.product.DailyDeal;
import dto.product.ProductBase;
import dto.product.ProductLabel;
import dto.product.ProductSalePrice;

public class DailyDealsService {

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	CategoryLabelMapper categoryLabelMapper;

	@Inject
	IProductLabelUpdateDao productLabelUpdateDao;

	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;

	@Inject
	IProductSalePriceUpdateDao productSalePriceUpdateDao;

	@Inject
	IProductBaseEnquiryDao productBaseEnquiryDao;

	@Inject
	IProductCategoryEnquiryDao productCategoryEnquiryDao;

	@Inject
	PriceService priceService;

	@Inject
	IProductViewCountEnquiryDao productViewCountEnquiryDao;

	@Inject
	InteractionInformationService interactionInformationService;

	@Inject
	IProductSalePriceEnquiryDao productSalePriceEnquiryDao;

	@Inject
	ProductSalePriceService productSalePriceService;

	@Inject
	SystemParameterService parameterService;

	@Inject
	ISuperDealDao superDealDao;

	@Inject
	DailyDealUpdateService dailyDealUpdateService;

	@Inject
	IDailyDealEnquiryService dailyDealEnquiryService;

	@Inject
	ILanguageService languageService;

	@Inject
	EventBus eventBus;

	/**
	 * @param websiteId
	 * 
	 * @return
	 */
	public boolean updateDailyDeals(Integer websiteId) {
		try {
			// 取消当前dailydeals商品
			List<DailyDeal> dailyDeals = dailyDealEnquiryService
					.getListingIdByWebsiteId(websiteId, 3, 0);
			for (DailyDeal dailyDeal : dailyDeals) {
				dailyDealUpdateService.updateDailyDealBvalid(
						dailyDeal.getIid(), false);
			}
			Logger.info("update done!");

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	public Map<String, String> getDailyDeals(Integer websiteId, String ccy,
			boolean type) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		try {
			Logger.info("start dailydeals");
			// 产品筛选逻辑:
			// a. 6个月<=上架时间<=12个月
			// b. 包含类目:Table PC & Cellphone,
			// apparel& Jewelry,
			// cameras & photo accessories,
			// health&Beauty
			// c. 不包含产品：supper deal产品，特殊产品(dodocool，限价品，清仓品)*/
			// 包含品类暂时写死,不能通过名称或路径获取,因为名称可能被修改
			List<Integer> rootIds = new ArrayList<Integer>();
			String dailyDealContainsCategory = parameterService
					.getSystemParameter(websiteId, null,
							"DailyDealContainsCategory");
			if (!StringUtils.isEmpty(dailyDealContainsCategory)) {
				String[] categoryIds = dailyDealContainsCategory.split(";");
				for (String categoryId : categoryIds) {
					rootIds.add(Integer.parseInt(categoryId));
				}
			} else {
				String msg = "not find categoryId";
				Logger.debug(msg);
				resultMap.put("error", msg);
				return resultMap;
			}

//			List<String> allListingIdsByRootId = productCategoryEnquiryDao
//					.getAllListingIdsByRootIds(rootIds);
//			if (allListingIdsByRootId.size() <= 0) {
//				String msg = "The categoryid range can not find the data";
//				Logger.debug(msg);
//				resultMap.put("error", msg);
//				return resultMap;
//			}

			Integer langId = languageService.getDefaultLanguage().getIid();
			int endDay = -parameterService.getSystemParameterAsInt(websiteId,
					langId, "DailyDealCreateDateOfMonthLower", 6);
			int startDay = -parameterService.getSystemParameterAsInt(websiteId,
					langId, "DailyDealCreateDateOfMonthHight", 12);
			Date startDate = DateFormatUtils.getNowBeforeByDay(Calendar.MONTH,
					startDay);
			Date endDate = DateFormatUtils.getNowBeforeByDay(Calendar.MONTH,
					endDay);
			List<String> listingIdsByDate = productBaseEnquiryDao
					.getLisingIdsByDate(rootIds, startDate,
							endDate);
			if (listingIdsByDate.size() <= 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String msg = "the date range can not find the data, dateRange:"
						+ sdf.format(startDate) + " to " + sdf.format(endDate);
				Logger.debug(msg);
				resultMap.put("error", msg);
				return resultMap;
			}
			List<String> allSuperDealsListingIds = superDealDao
					.getAllSuperDealsListingIds(websiteId);
			listingIdsByDate.removeAll(allSuperDealsListingIds);

			String dailyDealNotContainsProductLabal = parameterService
					.getSystemParameter(websiteId, null,
							"DailyDealNotContainsProductLabal");
			if (!StringUtils.isEmpty(dailyDealNotContainsProductLabal)) {
				String[] productLabelTypes = dailyDealNotContainsProductLabal
						.split(";");
				for (String productLabelType : productLabelTypes) {
					List<ProductLabel> productLabels = productLabelEnquiryDao
							.getProductLabelByTypeAndWebsite(websiteId,
									productLabelType);
					List<String> productLabelListingIds = Lists.transform(
							productLabels, obj -> {
								return obj.getClistingid();
							});
					listingIdsByDate.removeAll(productLabelListingIds);
				}
			}

			Calendar calendar = Calendar.getInstance();
			int dayTotal = 1;
			if (type) {
				long time1 = calendar.getTimeInMillis();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.roll(Calendar.DAY_OF_MONTH, -1);
				long time2 = calendar.getTimeInMillis();
				dayTotal = (int) ((time2 - time1) / (1000 * 3600 * 24));
			} else {
				calendar.set(calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH) + 1, 1);
				calendar.roll(Calendar.DATE, false);
				dayTotal = calendar.get(Calendar.DATE);
			}

			// d. 排序规则：按折扣从少到多排序；按评论数、浏览量从高到低排序；
			int total = dayTotal * 3;
			if (listingIdsByDate.size() < total) {
				String msg = "Can't find enough data, please replace the conditions!";
				Logger.debug(msg);

				resultMap.put("error", msg);
			}
			List<String> productSorts = sortListingId(listingIdsByDate, ccy)
					.subList(0, dayTotal * 3);
			// 生成dailyDeals
			boolean insertDailyDeals = insertDailyDeals(productSorts,
					websiteId, type);

			if (insertDailyDeals) {
				resultMap.put("success", productSorts.toString());
			} else {
				resultMap.put("false", productSorts.toString());
			}

			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();

			resultMap.put("error", e.toString());
			return resultMap;
		}

	}

	/**
	 * 
	 * @param listingIds
	 * @param ccy
	 * 
	 * @return
	 */
	public List<String> sortListingId(List<String> listingIds, String ccy) {
		// d. 排序规则：按折扣从少到多排序；按评论数、浏览量从高到低排序；
		HashMap<String, Integer> commentMap = getCommentCount(listingIds);
		Map<String, Integer> reviewMap = getProductViewCount(listingIds);
		List<ProductSort> productSorts = Lists.transform(
				listingIds,
				listingId -> {
					Price price = priceService.getPrice(listingId, ccy);
					Double saleCount = price != null
							&& price.getDiscount() != null ? price
							.getDiscount() : 0.0;
					Integer commentCount = commentMap != null
							&& commentMap.get(listingId) != null ? commentMap
							.get(listingId) : 0;
					Integer viewCount = reviewMap != null
							&& reviewMap.get(listingId) != null ? reviewMap
							.get(listingId) : 0;

					return new ProductSort(listingId, viewCount, commentCount,
							saleCount);
				});
		ProductComparator comparator = new ProductComparator();
		List<String> sortListingIds = productSorts.parallelStream()
				.sorted(comparator).map(ProductSort::getListingId)
				.collect(Collectors.toList());

		return sortListingIds;
	}

	/**
	 * 获取listingIds的浏览量
	 * 
	 * @param listingIds
	 * @return
	 */
	private Map<String, Integer> getProductViewCount(List<String> listingIds) {
		List<ProductViewCount> viewCountListByListingIds = productViewCountEnquiryDao
				.getViewCountListByListingIds(listingIds);
		Map<String, Integer> commentMap = Maps.newHashMap();
		if (null != viewCountListByListingIds) {
			commentMap = viewCountListByListingIds.stream().collect(
					Collectors.toMap(a -> a.getClistingid(),
							a -> a.getIviewcount()));
		}

		return commentMap;
	}

	/**
	 * 获取listingIds的评论数
	 * 
	 * @param listingIds
	 * @return
	 */
	private HashMap<String, Integer> getCommentCount(List<String> listingIds) {
		HashMap<String, Integer> commentMap = Maps.newHashMap();
		for (String listingId : listingIds) {
			Integer productCommentCount = interactionInformationService
					.getProductCommentCount(listingId);
			commentMap.put(listingId,
					productCommentCount != null ? productCommentCount : 0);
		}
		return commentMap;
	}

	/**
	 * 生成dailyDeals
	 * 
	 * @param listingIds
	 * @param websiteId
	 */
	private boolean insertDailyDeals(List<String> listingIds,
			Integer websiteId, boolean type) {
		try {
			// 计算下个月的dailyDeals
			Calendar startDate = Calendar.getInstance();// 获取当前日期
			startDate.set(Calendar.HOUR_OF_DAY, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.SECOND, 0);

			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.HOUR_OF_DAY, 23);
			endDate.set(Calendar.MINUTE, 59);
			endDate.set(Calendar.SECOND, 59);

			Calendar createDate = Calendar.getInstance();// 获取当前日期
			createDate.set(Calendar.HOUR_OF_DAY, 8);
			createDate.set(Calendar.MINUTE, 8);
			createDate.set(Calendar.SECOND, 0);
			if (!type) {
				startDate.add(Calendar.MONTH, 1);
				startDate.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				endDate.add(Calendar.MONTH, 1);
				endDate.set(Calendar.DAY_OF_MONTH, 1);
				createDate.add(Calendar.MONTH, 1);
				createDate.set(Calendar.DAY_OF_MONTH, 1);
			}
			Logger.info("start insert dailydeals");
			for (int i = 0; i < listingIds.size(); i++) {
				if (i != 0 && i % 3 == 0) { // 每天三个dailyDeals
					startDate.add(Calendar.DAY_OF_MONTH, 1);
					endDate.add(Calendar.DAY_OF_MONTH, 1);
					createDate.add(Calendar.DAY_OF_MONTH, 1);
				}
				// 生成productLabel
				String listingId = listingIds.get(i);
				ProductBase productBase = productBaseEnquiryDao
						.getProductBaseByListingId(listingId);
				DailyDeal dailyDeal = new DailyDeal();
				dailyDeal.setBvalid(true);
				dailyDeal.setCcreateuser("auto-DailyDeals");
				dailyDeal.setClistingid(listingId);
				dailyDeal.setCsku(productBase.getCsku());
				dailyDeal.setDcreatedate(createDate.getTime());
				dailyDeal.setIwebsiteid(websiteId);

				dailyDealUpdateService.insert(dailyDeal);
				// 生成折扣及折扣日期
				ProductSalePrice productSalePrice = new ProductSalePrice();
				Double salePrice = productBase.getFprice() * 0.5;
				productSalePrice.setClistingid(listingId);
				productSalePrice.setDcreatedate(new Date());
				productSalePrice.setFsaleprice(salePrice);
				productSalePrice.setCsku(productBase.getCsku());
				productSalePrice.setDbegindate(startDate.getTime());
				productSalePrice.setDenddate(endDate.getTime());
				productSalePrice.setCcreateuser("auto-DailyDeals");
				productSalePriceService
						.insertProductSalePrice(productSalePrice);
			}
			Logger.info("insert dailydeals end, listingids : "
					+ listingIds.toString());

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}
}
