package services.order;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.cart.CartBaseMapper;
import play.Logger;
import play.libs.F;
import play.libs.F.Tuple;
import play.libs.F.Tuple3;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.utils.DoubleCalculateUtils;
import services.cart.CartLifecycleService;
import services.common.UUIDGenerator;
import services.member.address.AddressService;
import services.order.exception.ExType;
import services.order.exception.OrderException;
import services.product.ProductLabelService;
import services.search.criteria.ProductLabelType;
import services.shipping.ShippingMethodService;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.OrderSplitRequest;
import valueobjects.order_api.OrderSubmitInfo;
import valueobjects.order_api.ShippingIDWithOrder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import dto.Country;
import dto.ShippingMethodDetail;
import dto.member.MemberAddress;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.PreparatoryDetail;
import dto.order.PreparatoryOrder;
import events.order.OrderConfirmationEvent;
import facades.cart.Cart;

public class OrderSplitService {
	@Inject
	private PreparatoryOrderService preparatoryOrderService;
	@Inject
	private PreparatoryDetailService preparatoryDetailService;
	@Inject
	private CartLifecycleService cartLifecycleService;
	@Inject
	private OrderService orderService;
	@Inject
	private AddressService addressService;
	@Inject
	private CountryService countryService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private FreightService freightService;
	@Inject
	private BillDetailService billDetailService;
	@Inject
	private OrderStatusService statusService;
	@Inject
	private CartBaseMapper cartBaseMapper;
	@Inject
	private EventBus eventBus;
	@Inject
	private ProductLabelService productLabelService;

	public List<Order> split(OrderSubmitInfo info, OrderSplitRequest request) {
		// 一些基础条件的验证和失败的处理
		if (info == null) {
			return null;
		}
		List<ShippingIDWithOrder> shippingIDs = info.getShippingIDs();
		if (shippingIDs == null || shippingIDs.isEmpty()) {
			return null;
		}
		Cart cart = cartLifecycleService
				.getCart(info.getCartID(), request.getSiteId(),
						request.getLangID(), request.getCurrency());
		MemberAddress shippingAddress = addressService
				.getMemberAddressById(info.getAddressID());
		if (shippingAddress == null || shippingAddress.getIcountry() == null) {
			return null;
		}
		// 准备一些必要的信息
		Country country = countryService.getCountryByCountryId(shippingAddress
				.getIcountry());
		// 准备额外优惠的信息
		Tuple<Map<String, ExtraLineView>, Map<String, ExtraSaveInfo>> extras = orderService
				.transformExtras(cart);
		Map<String, ExtraSaveInfo> orderInfo = extras._2;
		Map<String, ExtraSaveInfo> failedItems = Maps.filterValues(orderInfo,
				es -> !es.isSuccessful());
		if (failedItems.size() > 0) {
			Logger.error("Failed Extras Items: {}", failedItems);
		}
		// 计算优惠总额
		Double extraTotal = orderService.calculateExtrasAmount(extras._1);
		// 生成Order和相应的OrderDetail、BillDetail，中间有一些必要信息的验证
		List<Tuple3<Order, List<OrderDetail>, List<BillDetail>>> tuple3s = Lists
				.newArrayList();
		for (ShippingIDWithOrder s : shippingIDs) {
			tuple3s.add(createOrder(s, request, cart, shippingAddress, country,
					info));
		}
		List<Order> orders = Lists.transform(tuple3s, t -> t._1);
		List<List<OrderDetail>> detailsList = Lists.transform(tuple3s,
				t -> t._2);
		List<List<BillDetail>> billsList = Lists.transform(tuple3s, t -> t._3);
		// 将优惠的金额按各个订单的金额加权平均，分配给多个订单
		orders = calculateDiscount(extraTotal, orders);
		if (orders.size() != billsList.size()
				&& detailsList.size() != billsList.size()) {
			throw new OrderException(ExType.DetailSizeError);
		}
		// 将订单及相关的信息插入数据库，中间有一些必要信息的验证
		for (int i = 0; i < orders.size(); i++) {
			boolean isSaveExtras = i == 0 ? true : false;
			insertOrder(orders.get(i), detailsList.get(i), billsList.get(i),
					orderInfo, isSaveExtras);
		}
		// 修改购物车的状态
		cartBaseMapper.updateCartStatusByCartId(cart.getId());
		return orders;
	}

	private Order insertOrder(Order order, List<OrderDetail> details,
			List<BillDetail> bills, Map<String, ExtraSaveInfo> extraSaveInfos,
			boolean isSaveExtras) {
		if (orderService.insertOrder(order)) {
			Integer orderId = order.getIid();
			bills.add(orderService.getShippingBill(order));
			details.forEach(d -> d.setIorderid(orderId));
			if (details.size() != 0 && orderService.insertDetail(details)) {
				if (isSaveExtras) {
					if (!orderService.saveOrderExtras(order, extraSaveInfos)) {
						throw new OrderException(ExType.ExtrasFailed);
					}
				}
				bills.forEach(b -> b.setIorderid(orderId));
				if (billDetailService.batchInsert(bills)) {
					// everything ok, update order status
					statusService.changeOrdeStatus(orderId,
							IOrderStatusService.PAYMENT_PENDING);
					eventBus.post(new OrderConfirmationEvent(order.getCemail(),
							order.getIid(), null));
					return order;
				} else {
					throw new OrderException(ExType.BillDetailFailed);
				}
			} else {
				throw new OrderException(ExType.OrderDetailFailed);
			}
		} else {
			throw new OrderException(ExType.Unknown);
		}
	}

	private List<Order> calculateDiscount(Double extraTotal, List<Order> orders) {
		double total = 0d;
		double temp = extraTotal;
		for (Order order : orders) {
			total += order.getFordersubtotal() + order.getFshippingprice();
		}
		for (int i = 0; i < orders.size(); i++) {
			DoubleCalculateUtils dcu = new DoubleCalculateUtils(extraTotal);
			Order order = orders.get(i);
			double discount = 0d;
			if (i == orders.size() - 1) {
				discount = temp;
			} else {
				discount = dcu.multiply(order.getFordersubtotal() / total)
						.doubleValue();
			}
			temp = new DoubleCalculateUtils(temp).subtract(discount)
					.doubleValue();
			order.setFextra(discount);
			order.setFgrandtotal(new DoubleCalculateUtils(order
					.getFordersubtotal()).add(order.getFshippingprice())
					.subtract(order.getFextra()).doubleValue());
		}
		return orders;
	}

	@SuppressWarnings("static-access")
	private Tuple3<Order, List<OrderDetail>, List<BillDetail>> createOrder(
			ShippingIDWithOrder s, OrderSplitRequest request, Cart cart,
			MemberAddress address, Country country, OrderSubmitInfo info) {
		if (country == null) {
			throw new OrderException(ExType.CountryIsEmpty);
		}
		PreparatoryOrder pOrder = preparatoryOrderService.getByID(s
				.getOrderID());
		List<PreparatoryDetail> pDetails = preparatoryDetailService
				.getByOrderID(s.getOrderID());
		List<OrderDetail> details = Lists.newArrayList();
		for (PreparatoryDetail pDetail : pDetails) {
			details.add(createDetail(pDetail, request.getCurrency(),
					pOrder.getCcurrency()));
		}
		Tuple3<Double, Double, List<String>> orderInfoTuple = getTotalWeight(pDetails);
		Double subtotal = orderInfoTuple._1;
		Double weight = orderInfoTuple._2;
		List<String> listingIDs = orderInfoTuple._3;
		boolean checkShipping = orderService.checkShippingMethodCorrect(
				pOrder.getIstorageid(), country.getCshortname(),
				s.getShippingID(), weight, subtotal, listingIDs,
				request.getCurrency(), request.getLangID());
		if (!checkShipping) {
			throw new OrderException(ExType.InvalidShippingMethod);
		}
		ShippingMethodDetail shippingMethod = shippingMethodService
				.getShippingMethodDetail(s.getShippingID(), request.getLangID());
		Map<String, Integer> listingIDMap = getListingIDMap(pDetails);
		Double shippingWeight = freightService.getTotalWeight(listingIDMap,
				true);
		Double shippingPrice = freightService.getFinalFreight(shippingMethod,
				weight, shippingWeight, request.getCurrency(), subtotal,
				this.hasAllFreeShipping(listingIDs));
		List<BillDetail> bills = Lists.newArrayList();
		details.forEach(d -> orderService.parseBill(bills, d));
		String cid = orderService.getOrderCid(country.getCshortname(),
				request.getMemberID());
		Order order = new Order();
		order.setCordernumber(cid);
		order.setCcartid(cart.getId());
		order.setCcountry(country.getCname());
		order.setCcountrysn(country.getCshortname());
		order.setCcity(address.getCcity());
		order.setCfirstname(address.getCfirstname());
		order.setCmiddlename(address.getCmiddlename());
		order.setClastname(address.getClastname());
		order.setCpostalcode(address.getCpostalcode());
		order.setCprovince(address.getCprovince());
		order.setCtelephone(address.getCtelephone());
		order.setCstreetaddress(address.getCstreetaddress());
		order.setCcurrency(request.getCurrency());
		order.setCemail(pOrder.getCmemberemail());
		order.setCmemberemail(pOrder.getCmemberemail());
		order.setCip(request.getIp());
		order.setCmessage(info.getMessage());
		order.setCorigin(request.getOrigin());
		order.setCpostalcode(shippingMethod.getCcode());
		order.setCvhost(request.getVhost());
		order.setFordersubtotal(subtotal);
		order.setFshippingprice(shippingPrice);
		order.setIshippingmethodid(shippingMethod.getIid());
		order.setIstorageid(shippingMethod.getIstorageid());
		order.setIwebsiteid(request.getSiteId());
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return F.Tuple3(order, details, bills);
	}

	/**
	 * 所有免邮
	 * 
	 * @return
	 */
	private boolean hasAllFreeShipping(List<String> listingids) {
		// ~ 所有免邮
		List<String> allfp = productLabelService.getListByListingIdsAndType(
				listingids, ProductLabelType.AllFreeShipping.toString());
		return (allfp != null && allfp.size() > 0);
	}

	private Map<String, Integer> getListingIDMap(
			List<PreparatoryDetail> pDetails) {
		Map<String, Integer> map = Maps.newHashMap();
		for (PreparatoryDetail preparatoryDetail : pDetails) {
			map.put(preparatoryDetail.getClistingid(),
					preparatoryDetail.getIqty());
		}
		return map;
	}

	private Tuple3<Double, Double, List<String>> getTotalWeight(
			List<PreparatoryDetail> pDetails) {
		double weight = 0.0;
		double subtotal = 0.0;
		List<String> listingIDs = Lists.newArrayList();
		for (PreparatoryDetail detail : pDetails) {
			weight += detail.getFweight() * detail.getIqty();
			subtotal += detail.getFtotalprices();
			listingIDs.add(detail.getClistingid());
		}
		return F.Tuple3(subtotal, weight, listingIDs);
	}

	private OrderDetail createDetail(PreparatoryDetail pDetail,
			String targetCCY, String originalCCY) {
		OrderDetail detail = new OrderDetail();
		detail.setCid(UUIDGenerator.createAsString());
		detail.setClistingid(pDetail.getClistingid());
		detail.setCparentid(pDetail.getCparentid());
		detail.setCsku(pDetail.getCsku());
		detail.setCtitle(pDetail.getCtitle());
		detail.setForiginalprice(currencyService.exchange(
				pDetail.getForiginalprice(), originalCCY, targetCCY));
		detail.setFprice(currencyService.exchange(pDetail.getFprice(),
				originalCCY, targetCCY));
		detail.setFtotalprices(currencyService.exchange(
				pDetail.getFtotalprices(), originalCCY, targetCCY));
		detail.setFweight(pDetail.getFweight());
		detail.setIqty(pDetail.getIqty());
		return detail;
	}
}