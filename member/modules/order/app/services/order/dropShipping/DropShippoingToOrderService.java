package services.order.dropShipping;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.order.OrderMapper;
import play.Logger;
import play.libs.Json;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.utils.DoubleCalculateUtils;
import services.common.UUIDGenerator;
import services.member.address.AddressService;
import services.order.IBillDetailService;
import services.order.IFreightService;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderTaggingService;
import services.order.exception.ExType;
import services.order.exception.OrderException;
import services.product.ProductEnquiryService;
import services.shipping.IShippingMethodService;
import services.shipping.ShippingServices;
import valueobjects.order_api.ConfirmedOrder;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.product.Weight;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import dto.Country;
import dto.Currency;
import dto.ShippingMethodDetail;
import dto.Storage;
import dto.member.MemberAddress;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.dropShipping.DropShipping;
import dto.order.dropShipping.DropShippingOrder;
import dto.order.dropShipping.DropShippingOrderDetail;
import events.order.OrderConfirmationEvent;

public class DropShippoingToOrderService {
	@Inject
	private DropShippingOrderEnquiryService dsOrderEnquiry;
	@Inject
	private IOrderService orderService;
	@Inject
	private DropShippingOrderDetailEnquiryService dsDetailEnquiry;
	@Inject
	private CountryService countryEnquiry;
	@Inject
	private IFreightService freightService;
	@Inject
	private IShippingMethodService shippingMethodService;
	@Inject
	private ShippingServices shippingServices;
	@Inject
	private IBillDetailService billDetailService;
	@Inject
	private IOrderStatusService statusService;
	@Inject
	private EventBus eventBus;
	@Inject
	private DropShippingMapUpdateService dropShippingMapUpdate;
	@Inject
	private DropShippingMapEnquiryService dropShippingMapEnquiry;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private DropShippingUpdateService dropShippingUpdate;
	@Inject
	private ProductEnquiryService productEnquiry;
	@Inject
	private OrderTaggingService taggingService;
	@Inject
	private AddressService addressService;

	private String errorLog;
	
	@Inject
	OrderMapper orderMapper;

	@SuppressWarnings({ "rawtypes", "static-access" })
	public List<Order> converToOrderAndInsert(Map<Integer, Integer> idMap,
			String orgin, String ip, String vhost, int memberID, int lang) {
		List<Integer> idList = Lists.newArrayList(idMap.keySet());
		List<DropShippingOrder> dsOrders = dsOrderEnquiry.getListByIDs(idList);
		for (DropShippingOrder dsOrder : dsOrders) {
			if (dsOrder.getCerrorlog() != null) {
				errorLog = "There is a wrong order: "
						+ dsOrder.getCuserorderid() + ", error log: "
						+ dsOrder.getCerrorlog();
				return null;
			}
		}
		if (dsOrders.isEmpty()) {
			errorLog = "haven't found any dropshipping order with id: "
					+ Json.toJson(idList);
			return null;
		}
		List<Order> orderList = Lists.newArrayList();
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(0);
		String dropShippingID = dsOrders.get(0).getCdropshippingid();
		for (DropShippingOrder dsOrder : dsOrders) {
			Order order = new Order();
			saveFieldToOrder(order, dsOrder, orgin, ip, vhost);
			order.setCordernumber(orderService.getOrderCid(order, memberID));
			List<DropShippingOrderDetail> details = dsDetailEnquiry
					.getByDropShippingOrderID(dsOrder.getIid());
			List<String> listingIDs = Lists.transform(details,
					d -> d.getClistingid());
			checkShippingMethod(order, dsOrder, listingIDs,
					idMap.get(dsOrder.getIid()), lang, details);
			Integer orderID = null;
			if (orderService.insertOrder(order)) {
				orderID = order.getIid();
			} else {
				throw new OrderException(ExType.Unknown);
			}
			taggingService.tag(orderID, Lists.newArrayList("dropshipping"));
			Map<String, List> map = createDetail(order, orderID, details);
			insertOrderInfo(map, order, orderID);
			orderList.add(order);
			dropShippingMapUpdate.updateOrderNumber(order.getCordernumber(),
					dsOrder.getIid());
			dcu = dcu.add(order.getFgrandtotal());
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		updateDropShippingTotal(dcu.doubleValue(), dropShippingID);
		return orderList;
	}

	private void updateDropShippingTotal(double doubleValue,
			String dropShippingID) {
		DropShipping ds = new DropShipping();
		ds.setCdropshippingid(dropShippingID);
		ds.setFtotalprice(doubleValue);
		dropShippingUpdate.updateByDropShippingID(ds);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertOrderInfo(Map<String, List> map, Order order,
			Integer orderID) {
		List<OrderDetail> details = map.get("detailList");
		List<BillDetail> bills = map.get("billList");
		if (details.size() != 0 && orderService.insertDetail(details)) {
			if (billDetailService.batchInsert(bills)) {
				// everything ok, update order status
				statusService.changeOrdeStatus(orderID,
						IOrderStatusService.PAYMENT_PENDING);
				Logger.debug("Start sending ------>OrderConfirmationEvent<------events,orderNum={},class==DropShippoingToOrderService,method=insertOrderInfo",order.getCordernumber());
				eventBus.post(new OrderConfirmationEvent(order.getCemail(),
						order.getIid(), null));
			} else {
				throw new OrderException(ExType.BillDetailFailed);
			}
		} else {
			throw new OrderException(ExType.OrderDetailFailed);
		}
	}

	@SuppressWarnings("rawtypes")
	private Map<String, List> createDetail(Order order, Integer orderID,
			List<DropShippingOrderDetail> details) {
		Map<String, List> map = Maps.newHashMap();
		List<String> listingIDs = Lists.transform(details,
				d -> d.getClistingid());
		List<OrderDetail> detailList = Lists.newArrayList();
		List<BillDetail> billList = Lists.newArrayList();
		List<Weight> weights = productEnquiry.getWeightList(listingIDs);
		Map<String, Weight> weightMap = Maps.uniqueIndex(weights,
				w -> w.getListingId());
		for (DropShippingOrderDetail dsDetail : details) {
			OrderDetail detail = new OrderDetail();
			detail.setCid(UUIDGenerator.createAsString());
			detail.setClistingid(dsDetail.getClistingid());
			detail.setCsku(dsDetail.getCsku());
			detail.setCtitle(dsDetail.getCtitle());
			detail.setForiginalprice(dsDetail.getForiginalprice());
			detail.setFprice(dsDetail.getFprice());
			detail.setFtotalprices(dsDetail.getFtotalprice());
			detail.setIorderid(orderID);
			detail.setIqty(dsDetail.getIqty());
			detail.setFweight(weightMap.get(dsDetail.getClistingid())
					.getWeight());
			orderService.parseBill(billList, detail);
			detailList.add(detail);
		}
		BillDetail billDetail = orderService.getShippingBill(order);
		billList.add(billDetail);
		map.put("detailList", detailList);
		map.put("billList", billList);
		return map;
	}

	private void checkShippingMethod(Order order, DropShippingOrder dsOrder,
			List<String> listingIDs, int shippingMethodID, int lang,
			List<DropShippingOrderDetail> details) {
		Map<String, Integer> qtyMap = Maps.newHashMap();
		for (DropShippingOrderDetail dsDetail : details) {
			qtyMap.put(dsDetail.getClistingid(), dsDetail.getIqty());
		}
		Country country = countryEnquiry.getCountryByShortCountryName(dsOrder
				.getCcountrysn());
		Double weight = freightService.getTotalWeight(qtyMap, false);
		Double shippingWeight = freightService.getTotalWeight(qtyMap, true);
		ShippingMethodDetail shippingMethod = shippingMethodService
				.getShippingMethodDetail(shippingMethodID, lang);
		Double freight = orderService.getDoubleFreight(weight, shippingWeight,
				shippingMethod, country, dsOrder.getFtotal(),
				dsOrder.getCcurrency(), order.getIwebsiteid(), listingIDs);
		Storage shippingStorage = shippingServices.getShippingStorage(
				dsOrder.getIwebsiteid(), country, listingIDs);
		if (!orderService.checkShippingMethodCorrect(shippingStorage.getIid(),
				country.getCshortname(), shippingMethodID, weight,
				dsOrder.getFtotal(), listingIDs, dsOrder.getCcurrency(), lang)) {
			Logger.error(
					"OrderService checkShippingMethodCorrect error: "
							+ "storageId: {}, country: {}, shippingMethodId: {}, weight: {}",
					shippingStorage.getIid(), country.getCshortname(),
					shippingMethodID, weight);
			throw new OrderException(ExType.InvalidShippingMethod);
		}
		double grandTotal = new DoubleCalculateUtils(freight).add(
				order.getFordersubtotal()).doubleValue();
		order.setFshippingprice(freight);
		order.setIshippingmethodid(shippingMethod.getIid());
		order.setCshippingcode(shippingMethod.getCcode());
		order.setIstorageid(shippingStorage.getIid());
		order.setFgrandtotal(grandTotal);
	}

	private void saveFieldToOrder(Order order, DropShippingOrder dsOrder,
			String orgin, String ip, String vhost) {
		order.setCcity(dsOrder.getCcity());
		order.setCcountry(dsOrder.getCcountry());
		order.setCcountrysn(dsOrder.getCcountrysn());
		order.setCcurrency(dsOrder.getCcurrency());
		order.setCemail(dsOrder.getCuseremail());
		order.setCfirstname(dsOrder.getCfirstname());
		order.setCmemberemail(dsOrder.getCuseremail());
		order.setCmessage(dsOrder.getCcnote());
		order.setCpostalcode(dsOrder.getCpostalcode());
		order.setCprovince(dsOrder.getCprovince());
		order.setCstreetaddress(dsOrder.getCstreetaddress());
		order.setCtelephone(dsOrder.getCtelephone());
		order.setIwebsiteid(dsOrder.getIwebsiteid());
		order.setFordersubtotal(dsOrder.getFtotal());
		order.setFextra(0.0);
		order.setCip(ip);
		order.setCorigin(orgin);
		order.setCvhost(vhost);
	}

	public PaymentContext getPaymentContext(String dropShippingID, String email) {
		List<String> orderNumbers = dropShippingMapEnquiry
				.getOrderNumbersByID(dropShippingID);
		List<OrderDetail> details = Lists.newArrayList();
		Order order = null;
		DoubleCalculateUtils total = new DoubleCalculateUtils(0.0);//总价
		DoubleCalculateUtils subtotal = new DoubleCalculateUtils(0.0);//产品总价
		DoubleCalculateUtils shippingTotal = new DoubleCalculateUtils(0.0);//邮费总价
		MemberAddress address = addressService.getDefaultOrderAddress(email);
		for (int i = 0; i < orderNumbers.size(); i++) {
			String orderNumber = orderNumbers.get(i);
			Order o = orderEnquiry.getOrderById(orderNumber);
			if (o != null) {
				List<OrderDetail> des = orderMapper.getOrderDetailByOrderId(o.getIid());
				for(OrderDetail od : des){
					od.setCtitle("Drop Shipping Order Item " + (i + 1));
				}
//				OrderDetail detail = new OrderDetail();
//				detail.setIorderid(o.getIid());
//				detail.setCid(o.getCordernumber());
//				detail.setCtitle("Drop Shipping Order Item " + (i + 1));
//				detail.setCsku(o.getCordernumber());
//				detail.setIqty(1);
//				detail.setFtotalprices(o.getFgrandtotal());
//				detail.setFprice(o.getFgrandtotal());
				if(des.size()>0){
					details.addAll(des);
				}
				total = total.add(o.getFgrandtotal());
				subtotal = subtotal.add(o.getFordersubtotal());
				shippingTotal = shippingTotal.add(o.getFshippingprice());
			}
			if (i == 0 && o!=null) {
				order = new Order();
				order.setCordernumber(dropShippingID);
				order.setCpaymentid(o.getCpaymentid());
				order.setCfirstname(o.getCfirstname());
				order.setCcurrency(o.getCcurrency());
				order.setIwebsiteid(o.getIwebsiteid());
//				if (address != null && address.getIcountry() != null) {
//					Country country = countryEnquiry
//							.getCountryByCountryId(address.getIcountry());
//					order.setCcountry(country.getCname());
//					order.setCcountrysn(country.getCshortname());
//				} else {
//					order.setCcountry(o.getCcountry());
//					order.setCcountrysn(o.getCcountrysn());
//				}
			}
		}
		Currency currency = null;
		if (order != null) {
			currency = currencyService.getCurrencyByCode(order.getCcurrency());
			order.setFgrandtotal(total.doubleValue());
			order.setCemail(email);
			//add by lijun
			order.setFordersubtotal(subtotal.doubleValue());
			order.setFshippingprice(shippingTotal.doubleValue());
		}
		ConfirmedOrder co = new ConfirmedOrder(order, details);
		PaymentContext context = new PaymentContext(co, null, currency);
		return context;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

}
