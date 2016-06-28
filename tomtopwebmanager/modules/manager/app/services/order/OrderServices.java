package services.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import services.order.OrderCurrencyRateService;
import dto.order.OrderCurrencyRate;
import javax.inject.Inject;

import mapper.member.MemberRoleMapper;
import mapper.product.CategoryNameMapper;
import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductStorageMapMapper;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.Play;
import play.twirl.api.Html;
import services.ILanguageService;
import services.IStorageService;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.base.utils.Utils;
import services.member.IMemberBlackUserService;
import services.payment.IPaymentService;
import services.product.IProductEnquiryService;
import services.shipping.ShippingMethodService;
import valueobjects.order_api.DropShipOrderMessage;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderList;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dto.Currency;
import dto.ExportOrder;
import dto.ProductCategoryInfo;
import dto.Storage;
import dto.member.BlackUser;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.OrderPack;
import dto.order.OrderStatus;
import dto.product.ProductBase;
import dto.shipping.ShippingMethod;
import extensions.payment.IPaymentProvider;
import forms.order.OrderForm;
import forms.order.OrderTransactionForm;

public class OrderServices {
	@Inject
	IOrderService orderService;
	@Inject
	CurrencyService currencyService;
	@Inject
	IOrderEnquiryService orderEnquiryService;
	@Inject
	OrderPackService packService;
	@Inject
	ShippingMethodService shippingMethodService;
	@Inject
	IBillDetailService billDetailService;
	@Inject
	ILanguageService languageService;
	@Inject
	IPaymentService paymentService;
	@Inject
	IOrderStatusService statusService;
	@Inject
	ProductStorageMapMapper productStorageMapMapper;
	@Inject
	ProductCategoryMapperMapper productCategoryMapperMapper;
	@Inject
	CategoryNameMapper categoryNameMapper;
	@Inject
	private IOrderDetailService orderDetailService;
	@Inject
	private IProductEnquiryService productEnquiryService;
	@Inject
	private IStorageService storageService;
	@Inject
	private IOrderStatusService orderStatusService;
	@Inject
	OrderCurrencyRateService orderCurrencyRateService;
	@Inject
	FoundationService foundation;
	@Inject
	IMemberBlackUserService blackUserService;
	@Inject
	MemberRoleMapper memberRoleMapper;
	//每次查询订单个数
	public static final int limit = 10000;
	
	public Html getOrderList(OrderForm form) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		String rootPath = Play.application().configuration().getConfig("host")
				.getString("link");
		if (rootPath != null) {
			rootPath = rootPath.substring(0, rootPath.length() - 1);
		}
		if (!StringUtils.isEmpty(form.getStart())) {
			try {
				start = sdf.parse(form.getStart());
			} catch (ParseException e) {
				e.printStackTrace();
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			try {
				end = sdf.parse(form.getEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}
		List<dto.order.Order> orders = null;
		Logger.debug("code : "+form.getCode());
		if ("yes".equals(form.getIsOutTestUser())) {
			List<String> excUser = memberRoleMapper.getEmailByRoleId(2);// 传入权限id
			System.out.println(excUser.toString());
			orders = orderEnquiryService.searchOrdersExcludeUser(form, start,
					end, paymentStart, paymentEnd, excUser);
		} else {
			orders = orderEnquiryService.searchOrders(form, start, end,
					paymentStart, paymentEnd);
		}
		List<String> emails = null;
		if (null != orders && orders.size() > 0) {
			emails = Lists.transform(orders, i -> i.getCemail());
		}
		Map<String, Boolean> emailAndStatusMap = Maps.newHashMap();
		List<BlackUser> blackUserList = blackUserService.getBlackUser(
				foundation.getSiteID(), emails);
		if (null != blackUserList && blackUserList.size() > 0) {
			for (BlackUser blackUser : blackUserList) {
				boolean istatus = blackUser.getIstatus() == 0 ? true : false;
				emailAndStatusMap.put(blackUser.getCemail(), istatus);
			}
		}
		if (null == form.getSiteId() || orders.size() == 0) {
			return views.html.manager.order.order_table_list.render(rootPath,
					emailAndStatusMap, null, 0, 0, 0, null, null, null, null,
					null, null);
		}
		List<OrderList> allItems = Lists.newArrayList();
		List<String> currencys = Lists.transform(orders,
				list -> list.getCcurrency());
		List<Currency> curList = currencyService.getCurrencyByCodes(currencys);
		Map<String, Currency> currencyIndex = Maps.uniqueIndex(curList,
				i -> i.getCcode());
		Map<Integer, List<OrderPack>> packMap = Maps.newHashMap();
		Map<Integer, Double> orderWeight = Maps.newHashMap();
		Map<Integer, String> orderMessage = Maps.newHashMap();
		for (Order ord : orders) {
			OrderList o = new OrderList();
			o.setOrder(ord);
			List<OrderItem> items = orderService.getOrderDetailByOrder(ord);
			Double weight = 0.0;
			for (OrderItem orderItem : items) {
				if (null != orderItem.getWeight()) {
					weight += orderItem.getWeight() * orderItem.getIqty();
				}
			}
			List<BillDetail> extraBill = billDetailService.getExtraBill(ord
					.getIid());
			o.setOrderBillDetails(extraBill);
			o.setSymbol(currencyIndex.get(ord.getCcurrency()).getCsymbol());
			o.setOrderItems(items);
			packMap.put(ord.getIid(), packService.getByOrderId(ord.getIid()));
			allItems.add(o);
			orderWeight.put(ord.getIid(), weight);
			orderMessage.put(ord.getIid(), ord.getCmessage());
		}
		Integer orderCount = orderEnquiryService.getOrderTotalCount(form,
				start, end, paymentStart, paymentEnd);
		Integer pageTotal = orderCount / form.getPageSize()
				+ ((orderCount % form.getPageSize() > 0) ? 1 : 0);
		Map<String, IPaymentProvider> payments = paymentService.getMap();
		Map<Integer, OrderStatus> idMap = statusService.getIdMap();

		return views.html.manager.order.order_table_list.render(rootPath,
				emailAndStatusMap, allItems, orderCount, form.getPageNum(),
				pageTotal, packMap, shippingMethodService, payments, idMap,
				orderWeight, orderMessage);
	}

	public Html getOrderTransactionList(OrderTransactionForm form) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		if (!StringUtils.isEmpty(form.getStart())) {
			try {
				start = sdf.parse(form.getStart());
			} catch (ParseException e) {
				e.printStackTrace();
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			try {
				end = sdf.parse(form.getEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				end = null;
			}
		}

		List<dto.order.Order> orders = orderEnquiryService
				.searchOrdersTransaction(form, start, end);

		if (orders.size() == 0) {

		}
		Integer orderCount = orderEnquiryService
				.getOrdersTransactionTotalCount(form, start, end);
		Integer pageTotal = orderCount / form.getPageSize()
				+ ((orderCount % form.getPageSize() > 0) ? 1 : 0);

		return views.html.manager.order.order_transaction_table_list.render(
				orders, orderCount, form.getPageNum(), pageTotal);
	}

	/**
	 * 
	 * @Title: getExportOrders
	 * @Description: TODO(获取需要导出Excel的数据)
	 * @param @param orders
	 * @param @param siteId
	 * @param @return
	 * @return ArrayList<ArrayList<Object>>
	 * @throws
	 * @author yinfei
	 */
	public ArrayList<ArrayList<Object>> getExportOrders(
			List<dto.order.Order> orderList, String siteId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List<dto.order.Order> allOrders = new ArrayList<dto.order.Order>();
		// 最终要转化为Excel的集合
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		// Excel的标题
		ArrayList<Object> title = getExcelTitle(false);
		data.add(title);
		if (StringUtils.isEmpty(siteId)) {
			return data;
		}
		Map<Integer, OrderStatus> orderStatusMap = orderStatusService
				.getIdMap();
		Map<Integer, ShippingMethod> smMap = Maps.newHashMap();
		Map<String, OrderCurrencyRate> orcMap = Maps.newHashMap();
		int pageNo = 1;
		int count = orderList.size();
		int totalPage = 0;
		if (count % limit == 0) {
			totalPage = count / limit;
		} else {
			totalPage = (count / limit) + 1;
		}
		while (pageNo <= totalPage) {
			List<dto.order.Order> orders = new ArrayList<dto.order.Order>();
			int startIndex = (pageNo-1) * limit;
			int endIndex = pageNo * limit;
			if(pageNo == totalPage){
				endIndex = count;
			}
			for(int i=startIndex;i<endIndex;i++){
				orders.add(orderList.get(i));
			}
			List<Integer> smIds = FluentIterable.from(orders).filter(p -> p != null && p.getIshippingmethodid() != null)
					.transform(order -> order.getIshippingmethodid()).toList();

			if (smIds.size() > 0) {
				List<ShippingMethod> smList = shippingMethodService.getShippingMethodsByIds(smIds);
				for (ShippingMethod sm : smList) {
					smMap.put(sm.getIid(), sm);
				}
			}
			List<String> orderNumberList = FluentIterable.from(orders).transform(o -> o.getCordernumber()).toList();
			List<OrderCurrencyRate> orcList = orderCurrencyRateService.getRateByOrderNumbers(orderNumberList);
			for (OrderCurrencyRate ocr : orcList) {
				orcMap.put(ocr.getCordernumber(), ocr);
			}
			allOrders.addAll(orders);
			pageNo++;
		}
		OrderCurrencyRate ocr = null;
		// Excel的内容
		for (dto.order.Order order : allOrders) {
			ArrayList<Object> row = new ArrayList<Object>();
			row.add(order.getCordernumber());
			if (null != order.getCemail()) {
				String userEmail = Utils.getIncompleteEmail(order.getCemail());
				row.add(userEmail);
			} else {
				row.add(null);
			}
			row.add(order.getCcountry());
			row.add(order.getCcountrysn());
			row.add(order.getCprovince());
			row.add(order.getCcity());
			row.add(order.getCstreetaddress());
			row.add(order.getCpostalcode());
			row.add(order.getCtelephone());
			String name = order.getCfirstname() + " " + order.getClastname();
			row.add(name);
			row.add(order.getCpaymentid());
			if (null != order.getDpaymentdate()) {
				row.add(sdf.format(order.getDpaymentdate()));
			} else {
				row.add(null);
			}
			row.add(order.getCtransactionid());
			row.add(order.getCreceiveraccount());
			if (null != order.getIshippingmethodid()) {
				ShippingMethod shippingMethod = smMap.get(order
						.getIshippingmethodid());
				if (null != shippingMethod) {
					row.add(shippingMethod.getCcode());
				} else {
					row.add("shipping method is not found, shippingmethodid:"
							+ order.getIshippingmethodid());
				}
			} else {
				row.add(null);
			}
			row.add(order.getCcurrency());
			row.add(order.getFshippingprice());
			row.add(order.getFordersubtotal());
			row.add(order.getFextra());
			row.add(order.getFgrandtotal());
			if (null != order.getDcreatedate()) {
				row.add(sdf.format(order.getDcreatedate()));
			} else {
				row.add(null);
			}
			if (order.getIstatus() != null) {
				row.add(orderStatusMap.get(order.getIstatus()).getCname());
			} else {
				row.add(null);
			}
			if (order.getCcurrency() != "USD") {
				ocr = orcMap.get(order.getCordernumber());
				if (ocr != null) {
					long countPrice = Math.round((order.getFgrandtotal() / ocr
							.getFrate()) * 100);
					double dPrice = countPrice / 100.0;
					row.add(dPrice);
				} else {
					row.add(order.getFgrandtotal());
				}
			} else {
				row.add(order.getFgrandtotal());
			}
			data.add(row);
		}
		return data;
	}

	/**
	 * 
	 * @Title: getExportOrders
	 * @Description: TODO(获取需要导出Excel详情的数据)
	 * @param @param orders
	 * @param @param siteId
	 * @param @return
	 * @return ArrayList<ArrayList<Object>>
	 * @throws
	 * @author yinfei
	 */
	public ArrayList<ArrayList<Object>> getExportOrdersDetails(List<dto.order.Order> orderList, String siteId) {
		Logger.debug("orders size : "+orderList.size());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 需要导出的导出订单
		List<dto.ExportOrder> exportOrderList = new ArrayList<dto.ExportOrder>();
		// 最终要转化为Excel的集合
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		// Excel的标题
		ArrayList<Object> title = getExcelTitle(true);
		data.add(title);
		if (StringUtils.isEmpty(siteId)) {
			return data;
		}
		Integer langugeId = 1;
		Integer level_one = 1;
		Integer level_tow = 2;
		Integer level_three = 3;
		Map<Integer, ShippingMethod> smMap = Maps.newHashMap();
		Map<Integer, Storage> storageMap = Maps.newHashMap();
		Map<Integer, OrderStatus> orderStatusMap = orderStatusService.getIdMap();
		Map<Integer, dto.order.Order> orderMap = Maps.newHashMap();
		Map<String, OrderCurrencyRate> orcMap = Maps.newHashMap();
		Map<String, ProductBase> productbaseMap = Maps.newHashMap();
		Multimap<String, ProductCategoryInfo> threeLevelPciMap = ArrayListMultimap.create();
		Multimap<String, ProductCategoryInfo> twoLevelPciMap = ArrayListMultimap.create();
		Multimap<String, ProductCategoryInfo> oneLevelPciMap = ArrayListMultimap.create();
		int pageNo = 1;
		int count = orderList.size();
		int totalPage = 0;
		if (count % limit == 0) {
			totalPage = count / limit;
		} else {
			totalPage = (count / limit) + 1;
		}
		while (pageNo <= totalPage) {
			List<dto.order.Order> orders = new ArrayList<dto.order.Order>();
			int startIndex = (pageNo - 1) * limit;
			int endIndex = pageNo * limit;
			if (pageNo == totalPage) {
				endIndex = count;
			}
			for (int i = startIndex; i < endIndex; i++) {
				orders.add(orderList.get(i));
			}
			List<Integer> smIds = FluentIterable.from(orders).filter(p -> p != null && p.getIshippingmethodid() != null)
					.transform(order -> order.getIshippingmethodid()).toList();
			if (smIds.size() > 0) {
				List<ShippingMethod> smList = shippingMethodService.getShippingMethodsByIds(smIds);
				for (ShippingMethod sm : smList) {
					smMap.put(sm.getIid(), sm);
				}
			}
			List<Integer> orderidlist = FluentIterable.from(orders).transform(p -> p.getIid()).toList();
			for (dto.order.Order order : orders) {
				orderMap.put(order.getIid(), order);
			}
			List<String> orderNumberList = FluentIterable.from(orders).transform(o -> o.getCordernumber()).toList();
			List<OrderCurrencyRate> orcList = orderCurrencyRateService.getRateByOrderNumbers(orderNumberList);
			for (OrderCurrencyRate ocr : orcList) {
				orcMap.put(ocr.getCordernumber(), ocr);
			}
			Logger.debug("orderidlist size : " + orderidlist.size());
			if (null != orderidlist && orderidlist.size() > 0) {
				List<OrderDetail> odList = orderDetailService.getDetailByOrderIds(orderidlist);
				List<String> orderlistinglist = FluentIterable.from(odList).transform(p -> p.getClistingid()).toList();
				List<ProductBase> pbList = productEnquiryService.getProductBasesByListingIds(orderlistinglist);
				for (ProductBase pb : pbList) {
					productbaseMap.put(pb.getClistingid(), pb);
				}
				List<ProductCategoryInfo> pcategorylisttemp = productCategoryMapperMapper.getProductCategory(
						orderlistinglist, level_three, StringUtils.notEmpty(siteId) ? Integer.parseInt(siteId) : null,
						langugeId);
				List<ProductCategoryInfo> pcategorylist = productCategoryMapperMapper.getProductCategory(
						orderlistinglist, level_tow, StringUtils.notEmpty(siteId) ? Integer.parseInt(siteId) : null,
						langugeId);
				List<ProductCategoryInfo> oneLevelPcategorylist = productCategoryMapperMapper.getProductCategory(
						orderlistinglist, level_one, StringUtils.notEmpty(siteId) ? Integer.parseInt(siteId) : null,
						langugeId);
				Multimap<String, ProductCategoryInfo> pcitempMap = Multimaps.index(pcategorylisttemp,
						obj -> obj.getClistingid());
				threeLevelPciMap.putAll(pcitempMap);
				Multimap<String, ProductCategoryInfo> pciMap = Multimaps.index(pcategorylist,
						obj -> obj.getClistingid());
				twoLevelPciMap.putAll(pciMap);
				Multimap<String, ProductCategoryInfo> singleOneLevelPciMap = Multimaps.index(oneLevelPcategorylist,
						obj -> obj.getClistingid());
				oneLevelPciMap.putAll(singleOneLevelPciMap);
				List<Integer> storageIds = FluentIterable.from(orders)
						.filter(p -> p != null && p.getIstorageid() != null).transform(order -> order.getIstorageid())
						.toList();
				List<Storage> storageList = storageService.getStorageByStorageIds(storageIds);
				for (Storage s : storageList) {
					storageMap.put(s.getIid(), s);
				}
				List<dto.ExportOrder> singleExportOrderList = FluentIterable.from(odList).transform(orderDetail -> {
					dto.ExportOrder eo = new dto.ExportOrder();
					dto.order.Order order = orderMap.get(orderDetail.getIorderid());
					eo = transformOrderToExportOrder(order);
					eo.setSKU(orderDetail.getCsku());
					if (eo.getCcurrency().equals("USD")) {
						eo.setPrice(orderDetail.getFprice());
					} else {
						OrderCurrencyRate ocr = orcMap.get(order.getCordernumber());
						if (null != ocr) {
							eo.setPrice(orderDetail.getFprice() / ocr.getFrate());
						} else {
							eo.setPrice(orderDetail.getFprice() / currencyService.getRate(eo.getCcurrency()));
						}
					}
					eo.setQty(orderDetail.getIqty());
					ProductBase pbase = productbaseMap.get(orderDetail.getClistingid());
					if (null != pbase) {
						eo.setListingPrice(pbase.getFcostprice());
					}
					Collection<ProductCategoryInfo> cinfoList = threeLevelPciMap.get(orderDetail.getClistingid());
					String categoryName = "";
					for (ProductCategoryInfo pci : cinfoList) {
						categoryName += pci.getCpath() + ";";
					}
					if (categoryName == null || categoryName.equals("")) {
						Collection<ProductCategoryInfo> infoList = twoLevelPciMap.get(orderDetail.getClistingid());
						for (ProductCategoryInfo info : infoList) {
							categoryName += info.getCpath() + ";";
						}
					}
					if (categoryName == null || categoryName.equals("")) {
						Collection<ProductCategoryInfo> infoList = oneLevelPciMap.get(orderDetail.getClistingid());
						for (ProductCategoryInfo info : infoList) {
							categoryName += info.getCpath() + ";";
						}
					}
					eo.setCategory(categoryName);
					return eo;
				}).toSortedList((p1, p2) -> p1.getCordernumber().compareTo(p2.getCordernumber())).asList();
				exportOrderList.addAll(singleExportOrderList);
			}
			pageNo++;
		}
		Logger.debug("exportOrderList size : "+exportOrderList.size());
		// Excel的内容
		for (dto.ExportOrder order : exportOrderList) {
			ArrayList<Object> row = new ArrayList<Object>();
			row.add(order.getCordernumber());
			if (null != order.getCemail()) {
				String userEmail = Utils.getIncompleteEmail(order.getCemail());
				row.add(userEmail);
			} else {
				row.add(null);
			}
			row.add(order.getCcountry());
			row.add(order.getCcountrysn());
			row.add(order.getCprovince());
			row.add(order.getCcity());
			row.add(order.getCstreetaddress());
			row.add(order.getCpostalcode());
			row.add(order.getCtelephone());
			String name = order.getCfirstname() + " " + order.getClastname();
			row.add(name);
			row.add(order.getCpaymentid());
			if (null != order.getDpaymentdate()) {
				row.add(sdf.format(order.getDpaymentdate()));
			} else {
				row.add(null);
			}
			row.add(order.getCtransactionid());
			row.add(order.getCreceiveraccount());
			if (null != order.getIshippingmethodid()) {
				ShippingMethod shippingMethod = smMap.get(order.getIshippingmethodid());
				if (null != shippingMethod) {
					row.add(shippingMethod.getCcode());
				} else {
					row.add("shipping method is not found, shippingmethodid:" + order.getIshippingmethodid());
				}
			} else {
				row.add(null);
			}
			row.add("USD");
			if (order.getCcurrency().equals("USD")) {
				if (order.getFshippingprice() != null) {
					row.add(Math.round(order.getFshippingprice() * 100) / 100.0);
				} else {
					row.add("0.0");
				}
				row.add(Math.round(order.getFordersubtotal() * 100) / 100.0);
				row.add(Math.round(order.getFextra() * 100) / 100.0);
				row.add(Math.round(order.getFgrandtotal() * 100) / 100.0);
			} else {
				OrderCurrencyRate ocr = orcMap.get(order.getCordernumber());
				if (null != ocr) {
					if (order.getFshippingprice() != null) {
						row.add(Math.round((order.getFshippingprice() / ocr.getFrate()) * 100) / 100.0);
					} else {
						row.add("0.0");
					}
					row.add(Math.round((order.getFordersubtotal() / ocr.getFrate()) * 100) / 100.0);
					row.add(Math.round((order.getFextra() / ocr.getFrate()) * 100) / 100.0);
					row.add(Math.round((order.getFgrandtotal() / ocr.getFrate()) * 100) / 100.0);
				} else {
					if (order.getFshippingprice() != null) {
						row.add(Math.round(
								(order.getFshippingprice() / currencyService.getRate(order.getCcurrency())) * 100)
								/ 100.0);
					} else {
						row.add("0.0");
					}
					row.add(Math.round(
							(order.getFordersubtotal() / currencyService.getRate(order.getCcurrency())) * 100) / 100.0);
					row.add(Math.round((order.getFextra() / currencyService.getRate(order.getCcurrency())) * 100)
							/ 100.0);
					row.add(Math.round((order.getFgrandtotal() / currencyService.getRate(order.getCcurrency())) * 100)
							/ 100.0);
				}
			}
			if (null != order.getDcreatedate()) {
				row.add(sdf.format(order.getDcreatedate()));
			} else {
				row.add(null);
			}
			if (order.getIstatus() != null) {
				row.add(orderStatusMap.get(order.getIstatus()).getCname());
			} else {
				row.add(null);
			}
			if (null != order.getIstorageid()) {
				Storage storage = storageMap.get(order.getIstorageid());
				if (null != storage) {
					row.add(storage.getCstoragename());
				} else {
					row.add("storage is not found, storageid:" + order.getIstorageid());
				}
			} else {
				row.add(null);
			}
			row.add(order.getCategory());
			row.add(order.getSKU());
			row.add(order.getListingPrice());
			row.add(Math.round(order.getPrice() * 100) / 100.0);
			row.add(order.getQty());
			data.add(row);
		}
		Logger.debug("data size : "+data.size());
		return data;
	}

	/**
	 * 
	 * @Title: transformOrderToExportOrder
	 * @Description: TODO(将Order属性值赋给ExportOrder)
	 * @param @param order
	 * @param @return
	 * @return ExportOrder
	 * @throws
	 * @author yinfei
	 */
	private ExportOrder transformOrderToExportOrder(Order order) {
		if (null != order) {
			// 将Order属性赋值给ExportOrder
			dto.ExportOrder eo = new dto.ExportOrder();
			try {
				BeanUtils.copyProperties(eo, order);
			} catch (Exception e) {
				Logger.error("copyProperties error:", e);
			}
			return eo;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Title: getExcelTitle
	 * @Description: TODO(获取Excel的title信息)
	 * @param @return
	 * @return ArrayList<Object>
	 * @throws
	 * @author yinfei
	 */
	private ArrayList<Object> getExcelTitle(boolean isDetails) {
		ArrayList<Object> title = Lists.newArrayList();
		if (isDetails) {
			title = Lists.newArrayList("orderNumber", "email", "country",
					"countrysn", "province", "city", "streetaddress",
					"postalcode", "telephone", "name", "payment",
					"payment-Date", "transactionid", "receiveraccount",
					"shippingmethod", "currency", "shippingprice",
					"ordersubtotal", "extra", "grandtotal", "createdate",
					"status", "warehouse", "category", "SKU", "costPrice",
					"price", "qty");
		} else {
			title = Lists.newArrayList("orderNumber", "email", "country",
					"countrysn", "province", "city", "streetaddress",
					"postalcode", "telephone", "name", "payment",
					"payment-Date", "transactionid", "receiveraccount",
					"shippingmethod", "currency", "shippingprice",
					"ordersubtotal", "extra", "grandtotal", "createdate",
					"status", "price-usd");
		}
		return title;
	}

	/**
	 * 
	 * @Title: transformDSOToOrder
	 * @Description: TODO(将DropShipOrderMessage属性值赋给Order)
	 * @param @param dsorders
	 * @param @return
	 * @return List<dto.order.Order>
	 * @throws
	 * @author yinfei
	 */
	public List<dto.order.Order> transformDSOToOrder(
			List<DropShipOrderMessage> dsorders) {
		List<dto.order.Order> orders = FluentIterable.from(dsorders)
				.transform(dso -> {
					dto.order.Order order = new dto.order.Order();
					try {
						BeanUtils.copyProperties(order, dso);
					} catch (Exception e) {
						Logger.error("copyProperties error:", e);
					}
					return order;
				}).toList();
		return orders;
	}

	/**
	 * 
	 * @Title: getEnquiryOrders
	 * @Description: TODO(查询需要导出的订单列表)
	 * @param @param form
	 * @param @return
	 * @return List<dto.order.Order>
	 * @throws
	 * @author yinfei
	 */
	public List<dto.order.Order> getEnquiryOrders(OrderForm form) {
		List<dto.order.Order> orders = null;
		if ("yes".equals(form.getIsOutTestUser())) {
			List<String> excUser = memberRoleMapper.getEmailByRoleId(2);// 传入权限id
			orders = orderEnquiryService.getOrderBySearchFromExcludeUser(form,
					excUser);
		} else {
			orders = orderEnquiryService.getOrderBySearchFrom(form);
		}
		return orders;
	}

	public void removeDuplicate(List list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).equals(list.get(i))) {
					list.remove(j);
				}
			}
		}
	}
}
