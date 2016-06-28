package services.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import javax.inject.Inject;

import mapper.base.CountryMapper;
import mapper.cart.CartBaseMapper;
import mapper.order.DetailMapper;
import mapper.order.OrderMapper;

import org.apache.ibatis.session.ExecutorType;
import org.mybatis.guice.transactional.Transactional;
import org.springframework.util.Assert;

import play.Logger;
import play.libs.F;
import play.libs.F.Tuple;
import play.libs.F.Tuple3;
import play.libs.Json;
import play.mvc.Http.Context;
import services.IStorageService;
import services.IVhostService;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.geoip.GeoIPService;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.base.utils.Utils;
import services.cart.ICartLifecycleService;
import services.common.UUIDGenerator;
import services.member.IMemberEnquiryService;
import services.member.address.AddressService;
import services.member.login.LoginService;
import services.order.exception.ExType;
import services.order.exception.OrderException;
import services.product.EntityMapService;
import services.product.ProductEnquiryService;
import services.product.ProductLabelService;
import services.search.criteria.ProductLabelType;
import services.shipping.IShippingMethodService;
import services.shipping.ShippingMethodService;
import services.shipping.ShippingServices;
import valueobjects.base.LoginContext;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.ConfirmedOrder;
import valueobjects.order_api.CreateOrderRequest;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.OrderConfirmationRequest;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.CartGetRequest;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.ExtraLine;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import valueobjects.price.Price;
import valueobjects.price.PriceBuilder;
import valueobjects.product.ProductLite;
import valueobjects.product.Weight;

import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import context.ContextUtils;
import context.WebContext;
import dao.order.IOrderDetailEnquiryDao;
import dto.Country;
import dto.Currency;
import dto.ShippingMethodDetail;
import dto.Storage;
import dto.TopBrowseAndSaleCount;
import dto.member.MemberAddress;
import dto.member.MemberBase;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.OrderReportForm;
import dto.product.SimpleProductBase;
import dto.shipping.ShippingMethod;
import events.order.OrderConfirmationEvent;
import extensions.order.IOrderExtrasProvider;
import facades.cart.Cart;

/**
 * @Description: 订单相关服务
 * @author luojiaheng
 * @date 2015年1月20日 上午11:23:29
 */
public class OrderService implements IOrderService {
	@Inject
	private ICartLifecycleService cartService;
	@Inject
	private FoundationService foundation;
	@Inject
	private LoginService loginService;
	@Inject
	private IFreightService freightService;
	@Inject
	private ShippingServices shippingServices;
	@Inject
	private AddressService addressservice;
	@Inject
	private CountryService countryService;
	@Inject
	private OrderMapper orderMapper;
	@Inject
	private DetailMapper detailMapper;
	@Inject
	private EventBus eventBus;
	@Inject
	private Set<IOrderExtrasProvider> extrasProviders;
	@Inject
	private IBillDetailService billDetailService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private IOrderStatusService statusService;
	@Inject
	private CartBaseMapper cartBaseMapper;
	@Inject
	private ProductEnquiryService productEnquiryService;
	@Inject
	private ProductLabelService productLabelService;
	@Inject
	private IMemberEnquiryService memberEnquiryService;
	@Inject
	private EntityMapService entityMapService;
	@Inject
	private AddressService addressService;

	@Inject
	IOrderDetailEnquiryDao orderDetailDao;

	@Inject
	CountryMapper countryMapper;

	@Inject
	IVhostService vhostService;

	@Inject
	GeoIPService ipService;

	// add by lijun
	@Inject
	ICheckoutService checkoutService;
	
	@Inject
	CheckoutNewService checkoutNewService;
	@Inject
	IStorageService storageService;
	@Inject
	ShippingMethodService shippingMethodService;
	
	@Inject
	ShippingApiServices shippingApiServices;

	static final String[] SYMBOLS = { "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
			"Z" };

	@Override
	public String confirmOrder(WebContext context, String cartID,
			int shippingMethodID, int addressID, String origin, String message) {
		String cy = foundation.getCurrency(context);
		int siteID = foundation.getSiteID(context);
		int langID = foundation.getLanguage(context);
		String vhost = context.getVhost();
		String ip = context.getRemoteAddress();
		OrderConfirmationRequest req = new OrderConfirmationRequest(cartID,
				siteID, addressID, shippingMethodID, origin, message, ip,
				langID, cy, vhost);
		return confirmOrder(req);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#confirmOrder(valueobjects.order_api.
	 * OrderConfirmationRequest)
	 */
	@Override
	@Transactional(executorType = ExecutorType.BATCH, rethrowExceptionsAs = Exception.class)
	public String confirmOrder(OrderConfirmationRequest orderConfirmation) {
		// modify by lijun
		Tuple3<Cart, Order, Map<String, ExtraSaveInfo>> cartorder = null;
		if (orderConfirmation.getNoAddress()) {
			// 非会员用户快捷支付是不需要ship地址的(通过paypal取到地址)
			cartorder = createOrderInstance(orderConfirmation);
		} else {
			cartorder = createOrderInstance(orderConfirmation, null);
		}

		Cart cart = cartorder._1;
		Order order = cartorder._2;
		Map<String, ExtraSaveInfo> extraSaveInfos = cartorder._3;

		if (!validOrder(cart.getId())) {
			throw new OrderException(ExType.CartNotReadyForCheckout);
		}

		// insert order first with NULL status
		if (insertOrder(order)) {
			Integer orderId = order.getIid();
			List<BillDetail> bills = new ArrayList<BillDetail>();
			bills.add(getShippingBill(order));
			List<OrderDetail> details = createDetails(orderId, cart, bills);
			if (details.size() != 0 && insertDetail(details)) {
				if (saveOrderExtras(order, extraSaveInfos)) {
					if (billDetailService.batchInsert(bills)) {
						// everything ok, update order status
						statusService.changeOrdeStatus(orderId,
								IOrderStatusService.PAYMENT_PENDING);
						cartBaseMapper.updateCartStatusByCartId(order
								.getCcartid());
						Logger.debug(
								"Start sending ------>OrderConfirmationEvent<------events,orderNum={},class==OrderService,method=confirmOrder",
								order.getCordernumber());
						eventBus.post(new OrderConfirmationEvent(order
								.getCemail(), order.getIid(), cart.getId()));
						return order.getCordernumber();
					} else {
						throw new OrderException(ExType.BillDetailFailed);
					}
				} else {
					throw new OrderException(ExType.ExtrasFailed);
				}
			} else {
				throw new OrderException(ExType.OrderDetailFailed);
			}
		} else {
			throw new OrderException(ExType.Unknown);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#getShippingBill(dto.order.Order)
	 */
	@Override
	public BillDetail getShippingBill(Order order) {
		BillDetail billDetail = new BillDetail();
		billDetail.setCmsg(order.getIshippingmethodid() == null ? null : order
				.getIshippingmethodid().toString());
		billDetail.setCtype(IBillDetailService.TYPE_SHIPPING_METHOD);
		billDetail.setForiginalprice(order.getFshippingprice());
		billDetail.setFpresentprice(order.getFshippingprice());
		billDetail.setFtotalprice(order.getFshippingprice());
		billDetail.setIorderid(order.getIid());
		billDetail.setIqty(1);
		return billDetail;
	}

	/**
	 * 保存订单的额外优惠信息
	 *
	 * @param cart
	 * @param orderId
	 * @author luojiaheng
	 */
	public boolean saveOrderExtras(Order order,
			Map<String, ExtraSaveInfo> extraSaveInfos) {
		if (extraSaveInfos == null) {
			return true;
		}
		List<IOrderExtrasProvider> successful = Lists.newLinkedList();
		List<IOrderExtrasProvider> failure = Lists.newLinkedList();

		for (IOrderExtrasProvider provider : extrasProviders) {
			ExtraSaveInfo info = extraSaveInfos.get(provider.getId());
			if (null != info) {
				if (provider.saveOrderExtras(order, info)) {
					successful.add(provider);
				} else {
					Logger.debug("Failure saving order extras: {}, Payload={}",
							provider.getId(), info.getCartLine().getPayload());
					failure.add(provider);
					break;
				}
			}
		}

		if (failure.isEmpty()) {
			return true;
		} else {
			// rollback all successful extras
			for (IOrderExtrasProvider provider : successful) {
				ExtraSaveInfo info = extraSaveInfos.get(provider.getId());
				if (info != null) {
					provider.undoSaveOrderExtras(order, info);
				}
			}
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#insertDetail(java.util.List)
	 */
	@Override
	public boolean insertDetail(List<OrderDetail> details) {
		if (details == null || details.isEmpty()) {
			return true;
		}
		int i = 0;
		i = detailMapper.batchInsert(details);
		if (details.size() == i) {
			Logger.debug("OrderService Save Order Details insertDetail true!");
			return true;
		}
		Logger.debug("OrderService Save Order Details insertDetail false!");
		return false;
	}

	private List<OrderDetail> createDetails(Integer orderId, Cart cart,
			List<BillDetail> bills) {
		List<CartItem> items = cart.getAllItems();
		List<OrderDetail> listTotal = Lists.newArrayList();
		List<Weight> weights = productEnquiryService.getWeightList(cart
				.getListingIDs());
		Map<String, Weight> weightMap = Maps.uniqueIndex(weights,
				w -> w.getListingId());
		List<OrderDetail> list = Lists
				.transform(
						items,
						e -> {
							if (null == e) {
								Logger.debug(
										"OrderService Save Order Details createDetails item is null! cartId: {}",
										cart.getId());
								return null;
							}
							if (e instanceof SingleCartItem) {
								Price price = e.getPrice();
								Weight weight = weightMap.get(e.getClistingid());
								if (null == price || weight == null) {
									Logger.debug(
											"OrderService Save Order Details createDetails price is null! itemId: {}, cartId: {}",
											e.getCid(), cart.getId());
									return null;
								}
								OrderDetail detail = new OrderDetail();
								detail.setCid(UUIDGenerator.createAsString());
								detail.setCtitle(e.getCtitle());
								detail.setClistingid(e.getClistingid());
								detail.setFprice(price.getUnitPrice());
								detail.setIqty(e.getIqty());
								detail.setFtotalprices(price.getPrice());
								detail.setIorderid(orderId);
								detail.setCsku(e.getSku());
								detail.setForiginalprice(price
										.getUnitBasePrice());
								detail.setFweight(weight.getWeight());
								parseBill(bills, detail);
								return detail;
							} else if (e instanceof BundleCartItem) {
								List<SingleCartItem> childitems = ((BundleCartItem) e)
										.getChildList();
								Integer pty = e.getIqty();
								String id = UUIDGenerator.createAsString();
								String listingId = e.getClistingid();
								List<OrderDetail> temp = Lists.transform(
										childitems,
										i -> {
											if (null == i) {
												Logger.debug(
														"OrderService Save Order Details createDetails item is null! cartId: {}",
														cart.getId());
												return null;
											}
											Price price = i.getPrice();
											Weight weight = weightMap.get(e
													.getClistingid());
											if (null == price || weight == null) {
												Logger.debug(
														"OrderService Save Order Details createDetails price is null! itemId: {}, cartId: {}",
														i.getCid(),
														cart.getId());
												return null;
											}
											OrderDetail detail = new OrderDetail();
											detail.setCtitle(i.getCtitle());
											detail.setClistingid(i
													.getClistingid());
											if (detail.getClistingid().equals(
													listingId)) {
												detail.setCid(id);
											} else {
												detail.setCid(UUIDGenerator
														.createAsString());
												detail.setCparentid(id);
											}
											detail.setFprice(price
													.getUnitPrice());
											detail.setIqty(i.getIqty() * pty);
											detail.setFtotalprices(detail
													.getFprice()
													* detail.getIqty());
											detail.setIorderid(orderId);
											detail.setCsku(i.getSku());
											detail.setForiginalprice(price
													.getUnitBasePrice());
											detail.setFweight(weight
													.getWeight());
											parseBill(bills, detail);
											return detail;
										});
								listTotal.addAll(temp);
							}
							return null;
						});
		listTotal.addAll(list);
		Collection<OrderDetail> collection = Collections2.filter(listTotal,
				e -> e != null);
		return Lists.newArrayList(collection);
	}

	/**
	 * @author lijun
	 * @param orderId
	 * @param cart
	 * @param bills
	 * @return
	 */
	private List<OrderDetail> createDetails(Integer orderId,
			List<valueobjects.cart.CartItem> items, List<BillDetail> bills) {

		List<String> listingIds = Lists.newLinkedList();

		for (valueobjects.cart.CartItem item : items) {
			if (item instanceof valueobjects.cart.SingleCartItem) {
				listingIds.add(item.getClistingid());
			} else if (item instanceof valueobjects.cart.BundleCartItem) {
				List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
						.getChildList();
				List<String> list = Lists.transform(childs,
						child -> child.getClistingid());
				listingIds.addAll(list);
			} else {
				listingIds.add(item.getClistingid());
			}
		}

		List<OrderDetail> listTotal = Lists.newArrayList();
		List<Weight> weights = productEnquiryService.getWeightList(listingIds);
		Map<String, Weight> weightMap = Maps.uniqueIndex(weights,
				w -> w.getListingId());
		FluentIterable
				.from(items)
				.forEach(
						e -> {
							if (e == null) {
								return;
							}
							if (e instanceof valueobjects.cart.SingleCartItem) {
								valueobjects.cart.Price price = e.getPrice();
								Weight weight = weightMap.get(e.getClistingid());
								if (null == price || weight == null) {
									Logger.debug("OrderService Save Order Details createDetails price is null!");
									return;
								}
								OrderDetail detail = new OrderDetail();
								detail.setCid(UUIDGenerator.createAsString());
								detail.setCtitle(e.getCtitle());
								detail.setClistingid(e.getClistingid());
								detail.setFprice(price.getUnitPrice());
								detail.setIqty(e.getIqty());
								detail.setFtotalprices(price.getPrice());
								detail.setIorderid(orderId);
								detail.setCsku(e.getSku());
								detail.setForiginalprice(price
										.getUnitBasePrice());
								detail.setFweight(weight.getWeight());
								parseBill(bills, detail);
								listTotal.add(detail);
							} else if (e instanceof valueobjects.cart.BundleCartItem) {
								List<valueobjects.cart.SingleCartItem> childitems = ((valueobjects.cart.BundleCartItem) e)
										.getChildList();
								if (childitems == null
										|| childitems.size() == 0) {
									throw new NullPointerException(
											"childitems is null");
								}
								Integer pty = e.getIqty();
								String id = UUIDGenerator.createAsString();
								// String listingId = e.getClistingid();
								String listingId = childitems.get(0)
										.getClistingid();
								FluentIterable
										.from(childitems)
										.forEach(
												i -> {
													if (null == i) {
														Logger.debug("OrderService Save Order Details createDetails item is null!");
														return;
													}
													valueobjects.cart.Price price = i
															.getPrice();
													Weight weight = weightMap.get(i
															.getClistingid());
													if (null == price
															|| weight == null) {
														Logger.debug("OrderService Save Order Details createDetails price is null!");
														return;
													}
													OrderDetail detail = new OrderDetail();
													detail.setCtitle(i
															.getCtitle());
													detail.setClistingid(i
															.getClistingid());
													if (detail.getClistingid()
															.equals(listingId)) {
														detail.setCid(id);
													} else {
														detail.setCid(UUIDGenerator
																.createAsString());
														detail.setCparentid(id);
													}
													detail.setFprice(price
															.getUnitPrice());
													detail.setIqty(i.getIqty());
													detail.setFtotalprices(detail
															.getFprice()
															* detail.getIqty());
													detail.setIorderid(orderId);
													detail.setCsku(i.getSku());
													detail.setForiginalprice(price
															.getUnitBasePrice());
													detail.setFweight(weight
															.getWeight());
													parseBill(bills, detail);
													listTotal.add(detail);
												});
							} else {
								valueobjects.cart.Price price = e.getPrice();
								Weight weight = weightMap.get(e.getClistingid());
								if (null == price || weight == null) {
									Logger.debug("OrderService Save Order Details createDetails price is null!");
									return;
								}
								OrderDetail detail = new OrderDetail();
								detail.setCid(UUIDGenerator.createAsString());
								detail.setCtitle(e.getCtitle());
								detail.setClistingid(e.getClistingid());
								detail.setFprice(price.getUnitPrice());
								detail.setIqty(e.getIqty());
								detail.setFtotalprices(price.getPrice());
								detail.setIorderid(orderId);
								detail.setCsku(e.getSku());
								detail.setForiginalprice(price
										.getUnitBasePrice());
								detail.setFweight(weight.getWeight());
								parseBill(bills, detail);
								listTotal.add(detail);
							}
						});

		return listTotal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#insertOrder(dto.order.Order)
	 */
	@Override
	public boolean insertOrder(Order order) {
		int i = orderMapper.insert(order);
		if (1 == i) {
			Logger.debug("OrderService Save Order insertOrder true!");
			return true;
		}
		Logger.debug("OrderService Save Order insertOrder false!");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#validOrder(java.lang.String)
	 */
	@Override
	public boolean validOrder(String cartId) {
		int i = orderMapper.checkOrder(cartId);
		if (i > 0) {
			Logger.debug(
					"OrderService Save Order validOrder false! cartId: {}",
					cartId);
			return false;
		}
		Logger.debug("OrderService Save Order validOrder true! cartId: {}",
				cartId);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderService#checkShippingMethodCorrect(java.lang.Integer
	 * , java.lang.String, java.lang.Integer, java.lang.Double,
	 * java.lang.Double, java.util.List)
	 */
	@Override
	public boolean checkShippingMethodCorrect(Integer storageId,
			String country, Integer id, Double weight, Double subTotal,
			List<String> listingIds, String cunrrency, int langID) {
		Double usdTotal = currencyService.exchange(subTotal, cunrrency, "USD");
		Boolean isSpecial = productLabelService.getListByListingIdsAndType(
				listingIds, ProductLabelType.Special.toString()).size() > 0 ? true
				: false;
		Logger.debug(
				"checkShippingMethodCorrect storageId: {}, country: {}, shippingMethodID: {}"
						+ ", weight: {}, subTotal: {}, listingIds: {}, isSpecial: {}",
				storageId, country, id, weight, subTotal,
				Json.toJson(listingIds), isSpecial);
		List<ShippingMethodDetail> shippingMethods = shippingMethodService
				.getShippingMethods(storageId, country, weight, langID,
						usdTotal, isSpecial);
		List<Integer> ids = Lists.transform(shippingMethods, s -> s.getIid());
		Logger.debug(
				"checkShippingMethodCorrect shippingMethodId: {}, IDs: {}", id,
				Json.toJson(ids));
		for (ShippingMethod shippingMethod : shippingMethods) {
			if (isSpecial) {
				if (shippingMethod.getIid().equals(id)
						&& shippingMethod.getBisspecial()) {
					return true;
				}
			} else {
				if (shippingMethod.getIid().equals(id)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 新版校验 物流
	 * @param storageId
	 * @param country
	 * @param shipCode
	 * @param subTotal
	 * @param items
	 * @param cunrrency
	 * @param langID
	 * @return
	 */
	public valueobjects.order.ShippingMethod checkShippingMethodCorrect(int storageId,
			String country, String shipCode, Double subTotal,
			List<valueobjects.cart.CartItem> items, String cunrrency, int langID) {
		Assert.hasLength(shipCode, "ship code is null");
		Assert.hasLength(country, "country code is null");
		Assert.hasLength(cunrrency, "cunrrency code is null");
		Assert.notEmpty(items, "items is null");
		List<valueobjects.order.ShippingMethod> shipMethods = shippingApiServices.getShipMethod(
				country, storageId, langID, items, cunrrency, subTotal);

		if (shipMethods != null) {
			ImmutableList<valueobjects.order.ShippingMethod> hits = FluentIterable
					.from(shipMethods)
					.filter(m -> {
						if(m!=null && m.getCode()!=null && m.getCode().equals(shipCode)){
							return true;
						}else{
							return false;
						}
					}).toList();
			if (hits.size() > 0) {
				return hits.get(0);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderService#createOrderInstance(valueobjects.order_api
	 * .OrderConfirmationRequest, java.lang.Integer)
	 */
	@Override
	public Tuple3<Cart, Order, Map<String, ExtraSaveInfo>> createOrderInstance(
			OrderConfirmationRequest request, Integer statusId) {
		Cart cart = cartService
				.getCart(request.getCartId(), request.getSiteId(),
						request.getLangID(), request.getCurrency());
		if (null == cart) {
			throw new RuntimeException("Cart ID not found: "
					+ request.getCartId());
		}
		if (!cart.readyForCheckout()) {
			throw new OrderException(ExType.CartNotReadyForCheckout);
		}

		Order order = new Order();

		List<String> listingIds = cart.getListingIDs();
		Double weight = freightService.getTotalWeight(cart);
		MemberAddress adderess = addressservice.getMemberAddressById(request
				.getAddressId());
		Country country = countryService.getCountryByCountryId(adderess
				.getIcountry());
		Double shippingWeight = freightService.getTotalWeight(cart, true);
		ShippingMethodDetail shippingMethod = shippingMethodService
				.getShippingMethodDetail(request.getShippingMethodId(),
						request.getLangID());

		Double freight = getDoubleFreight(weight, shippingWeight,
				shippingMethod, country, cart.getBaseTotal(),
				request.getCurrency(), request.getSiteId(), listingIds);

		String provinceName = adderess.getCprovince();
		Storage shippingStorage = shippingServices.getShippingStorage(
				request.getSiteId(), country, listingIds);

		if (!checkShippingMethodCorrect(shippingStorage.getIid(),
				country.getCshortname(), request.getShippingMethodId(), weight,
				cart.getTotal(), listingIds, request.getCurrency(),
				request.getLangID())) {
			Logger.error(
					"OrderService checkShippingMethodCorrect error: "
							+ "storageId: {}, country: {}, shippingMethodId: {}, weight: {}",
					shippingStorage.getIid(), country.getCshortname(),
					request.getShippingMethodId(), weight);
			throw new OrderException(ExType.InvalidShippingMethod);
		}

		Tuple<Map<String, ExtraLineView>, Map<String, ExtraSaveInfo>> extras = transformExtras(cart);
		Map<String, ExtraSaveInfo> orderInfo = extras._2;
		Map<String, ExtraSaveInfo> failedItems = Maps.filterValues(orderInfo,
				es -> !es.isSuccessful());
		if (failedItems.size() > 0) {
			Logger.error("Failed Extras Items: {}", failedItems);
		}

		Double extraTotal = calculateExtrasAmount(extras._1);
		double tfreight = freight == null ? 0d : freight;
		double textraTotal = extraTotal == null ? 0d : extraTotal;
		Double finalTotal = getFinalTotal(cart, tfreight, textraTotal);

		order.setIstatus(statusId);
		order.setCcartid(cart.getId());
		order.setCcountrysn(country.getCshortname());
		order.setCcountry(country.getCname());
		order.setCcity(adderess.getCcity());
		order.setCcurrency(request.getCurrency());
		order.setCemail(cart.getOwner().getEmail());
		order.setCpostalcode(adderess.getCpostalcode());
		order.setCprovince(provinceName);
		order.setCfirstname(adderess.getCfirstname());
		order.setCmiddlename(adderess.getCmiddlename());
		order.setClastname(adderess.getClastname());
		order.setCstreetaddress(adderess.getCstreetaddress());
		order.setCtelephone(adderess.getCtelephone());
		order.setFextra(extraTotal);
		order.setFgrandtotal(finalTotal);
		order.setFordersubtotal(cart.getTotal());
		order.setFshippingprice(freight);
		order.setIshippingmethodid(request.getShippingMethodId());
		order.setIstorageid(shippingStorage.getIid());
		order.setIwebsiteid(request.getSiteId());
		order.setCorigin(request.getOrigin());
		order.setCmemberemail(adderess.getCmemberemail());
		order.setCmessage(request.getMessage());
		order.setCip(request.getIp());
		order.setCshippingcode(shippingMethod.getCcode());
		order.setCvhost(request.getVhost());
		//新订单号
		String ordernum = this.createGeneralOrderNumberV2(null);
		order.setCordernumber(ordernum);
//		order.setCordernumber(getOrderCid(order));
		return F.Tuple3(cart, order, orderInfo);
	}
	
	/**
	 * @author lijun
	 * @param request
	 * @return
	 */
	@Transactional(executorType = ExecutorType.BATCH, rethrowExceptionsAs = Exception.class)
	public Order createOrderInstance(CreateOrderRequest request) {
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx.getMemberID();
		Order order = new Order();
		List<valueobjects.cart.CartItem> items = request.getItems();

		List<String> listingIds = Lists.newLinkedList();

		double subTotal = checkoutService.subToatl(items);

		for (valueobjects.cart.CartItem item : items) {
			if (item instanceof valueobjects.cart.SingleCartItem) {
				listingIds.add(item.getClistingid());
			} else if (item instanceof valueobjects.cart.BundleCartItem) {
				List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
						.getChildList();
				List<String> list = Lists.transform(childs,
						child -> child.getClistingid());
				listingIds.addAll(list);
			} else {
				listingIds.add(item.getClistingid());
			}
		}
		MemberAddress adderess = null;
		if (request.getAddressId() != null) {
			adderess = addressservice.getMemberAddressById(request
					.getAddressId());
		}

		Country country = countryService.getCountryByCountryId(adderess
				.getIcountry());
		if (request.getShipCode() == null) {
			throw new RuntimeException("shipping method code is null");
		}
		// 验证shipcode 是否正确
		valueobjects.order.ShippingMethod hit = this.checkShippingMethodCorrect(
				request.getStorage(), country.getCshortname(),
				request.getShipCode(), subTotal, items,
				request.getCurrency(), request.getLangID());
		if (hit == null) {
			throw new RuntimeException(
					"shipping method code is not correct");
		}
		Double freight = hit.getPrice();

		String provinceName = adderess.getCprovince();
		Storage shippingStorage;
		if (request.getStorage() == null) {
			shippingStorage = shippingServices.getShippingStorage(
					request.getSiteId(), country, listingIds);
		} else {
			shippingStorage = this.storageService
					.getStorageForStorageId(request.getStorage());
		}

		Double extraTotal = 0.0;
		List<LoyaltyPrefer> prefers = request.getPrefer();
		if (prefers != null) {
			for (LoyaltyPrefer p : prefers) {
				extraTotal = extraTotal + p.getValue();
			}
		}

		Double finalTotal = checkoutNewService.sum(items, freight, country,
				prefers, request.getCurrency());

		String finalTotalStr = Utils.money(finalTotal, request.getCurrency());
		finalTotalStr = finalTotalStr.replaceAll(",", "");

		finalTotal = Double.parseDouble(finalTotalStr);

		order.setCcountrysn(country.getCshortname());
		order.setCcountry(country.getCname());
		order.setCcity(adderess.getCcity());
		order.setCcurrency(request.getCurrency());
		order.setCemail(email);
		order.setCpostalcode(adderess.getCpostalcode());
		order.setCprovince(provinceName);
		order.setCfirstname(adderess.getCfirstname());
		order.setCmiddlename(adderess.getCmiddlename());
		order.setClastname(adderess.getClastname());
		order.setCstreetaddress(adderess.getCstreetaddress());
		order.setCtelephone(adderess.getCtelephone());
		order.setFextra(extraTotal);
		order.setFgrandtotal(finalTotal);
		order.setFordersubtotal(subTotal);
		order.setFshippingprice(freight);
		order.setIstorageid(shippingStorage.getIid());
		order.setIwebsiteid(request.getSiteId());
		order.setCorigin(request.getOrigin());
		order.setCmemberemail(adderess.getCmemberemail());
		order.setCmessage(request.getMessage());
		order.setCip(request.getIp());
		order.setCvhost(request.getVhost());
		String orderNum = this.createGeneralOrderNumberV2(null);
		order.setCordernumber(orderNum);
		order.setCpaymenttype(request.getCpaymenttype());	//支付类型
		
		order.setIshippingmethodid(request.getShipMethodId());
		order.setCshippingcode(request.getShipCode());

		// insert order first with NULL status
		if (insertOrder(order)) {
			Integer orderId = order.getIid();
			List<BillDetail> bills = new ArrayList<BillDetail>();
			bills.add(getShippingBill(order));
			List<OrderDetail> details = createDetails(orderId, items, bills);
			if (details.size() != 0 && insertDetail(details)) {
				if (saveOrderExtras(order, prefers)) {
					if (billDetailService.batchInsert(bills)) {
						// everything ok, update order status
						statusService.changeOrdeStatus(orderId,
								IOrderStatusService.PAYMENT_PENDING);
						Logger.debug(
								"Start sending ------>OrderConfirmationEvent<------events,orderNum={},class==OrderService,method=createOrderInstance",
								order.getCordernumber());
						eventBus.post(new OrderConfirmationEvent(order
								.getCemail(), order.getIid(), null));
						return order;
					} else {
						throw new OrderException(ExType.BillDetailFailed);
					}
				} else {
					throw new OrderException(ExType.ExtrasFailed);
				}
			} else {
				throw new OrderException(ExType.OrderDetailFailed);
			}
		} else {
			throw new OrderException(ExType.Unknown);
		}
	}

	

	private String getOrderCid(Order order) {
		MemberBase member = memberEnquiryService.getMemberByMemberEmail(
				order.getCmemberemail(),
				ContextUtils.getWebContext(Context.current()));
		Integer memberID = member != null ? member.getIid() : loginService
				.getLoginData().getMemberId();
		return getOrderCid(order, memberID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#getOrderCid(dto.order.Order, int)
	 */
	@Override
	public String getOrderCid(Order order, int memberID) {
		return getOrderCid(order.getCcountrysn(), memberID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#getOrderCid(java.lang.String, int)
	 */
	@Override
	public String getOrderCid(String countryShortName, int memberID) {
		Date date = new Date();
		TimeZone tz = TimeZone.getTimeZone("GMT+00:00");
		String mmdd = format(date, "MMdd", tz);
		String yy = format(date, "yy", tz);
		String hhmmss = format(date, "HHmmss", tz);
		String cid = countryShortName + "-" + mmdd + memberID + yy + "-"
				+ hhmmss;
		return cid;
	}

	private String format(Date date, String pattern, TimeZone timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(timeZone);
		return sdf.format(date);
	}

	public Double calculateExtrasAmount(Map<String, ExtraLineView> extrasInfo) {
		double amount = 0.0;
		for (ExtraLineView i : extrasInfo.values()) {
			amount += i.getMoney();
		}
		return PriceBuilder.round_helf_up(amount, 2);
	}

	private Double getFinalTotal(Cart cart, Double freight, Double extraTotal) {
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(cart.getTotal());
		dcu = dcu.add(freight).add(extraTotal);
		double result = dcu.doubleValue();
		return result < 0.0 ? 0.0 : result;
	}

	/**
	 * 该方法获得的结果为正数，用以记录额外支付的金额
	 *
	 * @param cart
	 * @return
	 * @author luojiaheng
	 */
	public Tuple<Map<String, ExtraLineView>, Map<String, ExtraSaveInfo>> transformExtras(
			Cart cart) {
		Map<String, IOrderExtrasProvider> providers = Maps.uniqueIndex(
				extrasProviders, p -> p.getId());
		Map<String, ExtraLine> extraLinesMap = cart.getExtraLines();
		Collection<ExtraLine> extraLines = extraLinesMap.values();
		Map<String, ExtraSaveInfo> extraSaveInfo = Maps.newHashMap();
		for (ExtraLine line : extraLines) {
			IOrderExtrasProvider provider = providers.get(line.getPluginId());
			if (provider != null) {
				extraSaveInfo.put(line.getPluginId(),
						provider.prepareOrderInstance(cart, line));
			} else {
				Logger.warn("IOrderExtrasProvider {} not found, removed?",
						line.getPluginId());
				return null;
			}
		}
		Logger.debug("ExtraSaveInfo: {}", extraSaveInfo);
		Map<String, ExtraLineView> moneyValue = Maps.transformValues(
				extraLinesMap,
				line -> {
					IOrderExtrasProvider provider = providers.get(line
							.getPluginId());
					if (provider != null) {

						return provider.extralineView(cart, line);
					} else {
						Logger.warn(
								"IOrderExtrasProvider {} not found, removed?",
								line.getPluginId());
						return null;
					}
				});
		return F.Tuple(moneyValue, extraSaveInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#getDoubleFreight(java.lang.Double,
	 * java.lang.Double, dto.ShippingMethodDetail, dto.Country, double)
	 */
	@Override
	public Double getDoubleFreight(Double weight, Double shippingWeight,
			ShippingMethodDetail shippingMethod, Country country,
			double baseTotal, String currency, int websiteId,
			List<String> listingIDs) {
		boolean hasAllFreeShipping = hasAllFreeShipping(listingIDs);
		Double freight = freightService.getFinalFreight(shippingMethod, weight,
				shippingWeight, currency, baseTotal, hasAllFreeShipping);
		if (null == freight) {
			return null;
		}
				
		String shippingContext = shippingMethod.getCcontent();
		ShippingMethodInformation smi = new ShippingMethodInformation(
				shippingMethod, shippingContext, freight);
		ShippingMethodRequst requst = new ShippingMethodRequst(
				shippingMethod.getIstorageid(), country.getCshortname(),
				weight, shippingWeight, null, baseTotal, listingIDs,
				shippingMethod.getBisspecial(), currency, websiteId,
				hasAllFreeShipping);
		List<ShippingMethodInformation> smiList = Lists.newArrayList(smi);
		smiList = shippingMethodService.processingInPlugin(smiList, requst);
		return smiList.isEmpty() ? null : smiList.get(0).getFreight();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#getPaymentContext(java.lang.String)
	 */
	@Override
	public PaymentContext getPaymentContext(String corderId, int langID) {
		Order order = orderMapper.getOrderByOrderNumber(corderId);
		if (null == order) {
			return null;
		}
		List<OrderDetail> details = detailMapper.getOrderDetailByOrderId(order
				.getIid());
		ShippingMethodDetail shippingMethod = null;
		if (order.getIshippingmethodid() != null) {
			shippingMethod = shippingMethodService.getShippingMethodDetail(
					order.getIshippingmethodid(), langID);
			if (shippingMethod == null && order.getCshippingcode() != null) {
				shippingMethodService.getShippingMethodDetailByCode(
						order.getCshippingcode(), langID);
			}
		}
		if (StringUtils.isEmpty(order.getCcountrysn())) {
			setShippingAddress(order);
		}
		ConfirmedOrder confirmedOrder = new ConfirmedOrder(order, details);
		Currency currency = currencyService.getCurrencyByCode(order
				.getCcurrency());
		return new PaymentContext(confirmedOrder, shippingMethod, currency);
	}

	private void setShippingAddress(Order order) {
		MemberAddress address = addressService.getDefaultOrderAddress(order
				.getCmemberemail());
		if (address != null) {
			Country country = countryService.getCountryByCountryId(address
					.getIcountry());
			if (country != null) {
				order.setCcountry(country.getCname());
				order.setCcountrysn(country.getCshortname());
			}
			order.setCcity(address.getCcity());
			order.setCfirstname(address.getCfirstname());
			order.setCmiddlename(address.getCmiddlename());
			order.setClastname(address.getClastname());
			order.setCpostalcode(address.getCpostalcode());
			order.setCprovince(address.getCprovince());
			order.setCtelephone(address.getCtelephone());
			order.setCstreetaddress(address.getCstreetaddress());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#parseBill(java.util.List,
	 * dto.order.OrderDetail)
	 */
	@Override
	public BillDetail parseBill(List<BillDetail> bills, OrderDetail orderDetail) {
		BillDetail detail = new BillDetail();
		detail.setCmsg(orderDetail.getCtitle());
		detail.setIqty(orderDetail.getIqty());
		detail.setCtype(IBillDetailService.TYPE_PRODUCT);
		detail.setForiginalprice(orderDetail.getForiginalprice());
		detail.setFpresentprice(orderDetail.getFprice());
		detail.setFtotalprice(orderDetail.getFtotalprices());
		detail.setIorderid(orderDetail.getIorderid());
		bills.add(detail);
		return detail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#getOrderDetailByOrder(dto.order.Order)
	 */
	@Override
	public List<OrderItem> getOrderDetailByOrder(Order order) {
		int langID = foundation.getLanguage();
		return getOrderDetailByOrder(order, langID);
	}

	@Override
	public List<OrderItem> getOrderDetailByOrder(Order order, int langID) {
		Integer statusId = statusService
				.getIdByName(IOrderStatusService.COMPLETED);
		boolean isCompleted = order.getIstatus() == statusId ? true : false;

		List<OrderDetail> dlist = detailMapper.getOrderDetailByOrderId(order
				.getIid());
		// products
		List<String> ids = Lists.transform(dlist, d -> d.getClistingid());
		List<ProductLite> products = productEnquiryService
				.getProductLiteByListingIDs(ids, order.getIwebsiteid(), langID);
		Map<String, ProductLite> productMap = Maps.uniqueIndex(products,
				p -> p.getListingId());
		ProductLite product = new ProductLite();
		List<OrderItem> resultItems = Lists.newArrayList();
		for (int i = 0; i < dlist.size(); i++) {
			product = productMap.get(dlist.get(i).getClistingid());
			OrderItem ci = new OrderItem();
			ci.setCid(dlist.get(i).getCid());
			ci.setOrderid(dlist.get(i).getIorderid());
			ci.setClistingid(dlist.get(i).getClistingid());
			ci.setDcreatedate(dlist.get(i).getDcreatedate());
			ci.setIqty(dlist.get(i).getIqty());
			ci.setSku(dlist.get(i).getCsku());
			ci.setWeight(dlist.get(i).getFweight());
			ci.setReview((dlist.get(i).getCommentid() == null && isCompleted));
			Map<String, String> attributeMap = entityMapService
					.getAttributeMap(dlist.get(i).getClistingid(), langID);
			ci.setAttributeMap(attributeMap);
			if (product != null) {
				ci.setCtitle(product.getTitle());
				ci.setCurl(product.getUrl());
				ci.setCimageurl(product.getImageUrl());
			} else {
				ci.setCtitle(dlist.get(i).getCtitle());
			}
			ci.setBismain(dlist.get(i).getCparentid() == null ? true : false);
			ci.setUnitPrice(dlist.get(i).getFprice());
			ci.setTotalPrice(dlist.get(i).getFtotalprices());
			ci.setCparentId(dlist.get(i).getCparentid());
			ci.setOriginalPrice(dlist.get(i).getForiginalprice());
			ci.setChildList(Lists.newArrayList());
			resultItems.add(ci);
		}
		Map<String, OrderItem> cimaps = Maps.uniqueIndex(resultItems,
				p -> p.getCid());
		for (Map.Entry<String, OrderItem> entry : cimaps.entrySet()) {
			if (entry.getValue().getCparentId() != null
					&& cimaps.get(entry.getValue().getCparentId()) != null) {
				cimaps.get(entry.getValue().getCparentId()).getChildList()
						.add(entry.getValue());
				cimaps.get(entry.getValue().getCparentId()).setTotalPrice(
						cimaps.get(entry.getValue().getCparentId())
								.getTotalPrice()
								+ entry.getValue().getTotalPrice());
			}
		}
		List<OrderItem> resultItems2 = Lists.newArrayList();
		for (Map.Entry<String, OrderItem> entry : cimaps.entrySet()) {
			if (entry.getValue().getCparentId() == null) {
				resultItems2.add(entry.getValue());
			}
		}
		return resultItems2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#initListingid()
	 */
	@Override
	public void initListingid() {
		List<SimpleProductBase> list = productEnquiryService
				.findAllSimpleProductBase();
		for (SimpleProductBase pb : list) {
			detailMapper.initOldListingidForSku(pb);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderService#getTopSaleByTimeRange(java.lang.Integer)
	 */
	@Override
	public List<TopBrowseAndSaleCount> getTopSaleByTimeRange(Integer timeRange) {
		return detailMapper.getTopSaleByTimeRange(timeRange);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#getAllMemberEmails(java.util.Date,
	 * java.util.Date, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<String> getAllMemberEmails(Date startDate, Date endDate,
			Integer pageNum, Integer pageSize) {
		return orderMapper.getAllMemberEmails(startDate, endDate, pageNum,
				pageSize);
	}

	@Override
	public boolean isAlreadyPaid(String orderNum) {
		Order order = orderMapper.getOrderByOrderNumber(orderNum);
		if (order != null) {
			if (order.getIstatus() != null) {
				Integer status = order.getIstatus();
				String statusNam = statusService.getOrderStatusNameById(status);
				if (IOrderStatusService.PAYMENT_CONFIRMED.equals(statusNam)
						|| IOrderStatusService.PAYMENT_PROCESSING
								.equals(statusNam)) {
					return true;
				}
			}

			if (order.getIpaymentstatus() != null) {
				if (order.getIpaymentstatus() == 1
						|| order.getIpaymentstatus() == 2) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 为未登陆用户生成订单
	 * 
	 * @author lijun
	 * @param cartId
	 * @return
	 */
	private Tuple3<Cart, Order, Map<String, ExtraSaveInfo>> createOrderInstance(
			OrderConfirmationRequest request) {
		// 检查币种是否设置,没有币种就生成不了订单
		String currency = request.getCurrency();
		if (currency == null || currency.length() == 0) {
			throw new NullPointerException("currency is null");
		}
		Cart cart = cartService
				.getCart(request.getCartId(), request.getSiteId(),
						request.getLangID(), request.getCurrency());
		if (null == cart) {
			throw new RuntimeException("Cart ID not found: "
					+ request.getCartId());
		}

		List<String> listingIds = cart.getListingIDs();
		Order order = new Order();

		Tuple<Map<String, ExtraLineView>, Map<String, ExtraSaveInfo>> extras = transformExtras(cart);
		Map<String, ExtraSaveInfo> orderInfo = extras._2;
		Map<String, ExtraSaveInfo> failedItems = Maps.filterValues(orderInfo,
				es -> !es.isSuccessful());
		if (failedItems.size() > 0) {
			Logger.error("Failed Extras Items: {}", failedItems);
		}

		// 去取仓库
		Storage shippingStorage = shippingServices.getShippingStorage(
				request.getSiteId(), null, listingIds);

		Double extraTotal = calculateExtrasAmount(extras._1);
		double tfreight = 0d;
		double textraTotal = extraTotal == null ? 0d : extraTotal;
		Double finalTotal = getFinalTotal(cart, tfreight, textraTotal);

		order.setIstatus(null);
		order.setCcartid(cart.getId());
		order.setCcurrency(request.getCurrency());
		order.setCemail(cart.getOwner().getEmail());
		order.setFextra(extraTotal);
		order.setFgrandtotal(finalTotal);
		order.setFordersubtotal(cart.getTotal());
		order.setIwebsiteid(request.getSiteId());
		order.setCorigin(request.getOrigin());
		order.setCmessage(request.getMessage());
		order.setCip(request.getIp());
		order.setCvhost(request.getVhost());
		// String shipToCountryCode =
		// this.ipService.getCountryCode(request.getIp());
		String orderNum = this.createGuestOrderNumberV2(null);
		order.setCordernumber(orderNum);
		order.setIstorageid(shippingStorage.getIid());
		return F.Tuple3(cart, order, orderInfo);

	}

	/**
	 * 用户未登陆情况下生成订单
	 * 
	 * @author lijun
	 * @param orderConfirmation
	 */
	@Transactional(executorType = ExecutorType.BATCH, rethrowExceptionsAs = Exception.class)
	private String createOrder(OrderConfirmationRequest orderConfirmation) {

		Tuple3<Cart, Order, Map<String, ExtraSaveInfo>> cartorder = createOrderInstance(orderConfirmation);

		Cart cart = cartorder._1;
		Order order = cartorder._2;
		Map<String, ExtraSaveInfo> extraSaveInfos = cartorder._3;

		if (!validOrder(cart.getId())) {
			throw new OrderException(ExType.CartNotReadyForCheckout);
		}

		// insert order first with NULL status
		if (insertOrder(order)) {
			Integer orderId = order.getIid();
			List<BillDetail> bills = new ArrayList<BillDetail>();
			bills.add(getShippingBill(order));
			List<OrderDetail> details = createDetails(orderId, cart, bills);
			if (details.size() != 0 && insertDetail(details)) {
				if (saveOrderExtras(order, extraSaveInfos)) {
					if (billDetailService.batchInsert(bills)) {
						// everything ok, update order status
						statusService.changeOrdeStatus(orderId,
								IOrderStatusService.PAYMENT_PENDING);
						cartBaseMapper.updateCartStatusByCartId(order
								.getCcartid());
						Logger.debug(
								"Start sending ------>OrderConfirmationEvent<------events,orderNum={},class==OrderService,method=createOrder",
								order.getCordernumber());
						eventBus.post(new OrderConfirmationEvent(order
								.getCemail(), order.getIid(), cart.getId()));
						return order.getCordernumber();
					} else {
						throw new OrderException(ExType.BillDetailFailed);
					}
				} else {
					throw new OrderException(ExType.ExtrasFailed);
				}
			} else {
				throw new OrderException(ExType.OrderDetailFailed);
			}
		} else {
			throw new OrderException(ExType.Unknown);
		}

	}

	@Override
	public String createOrder(WebContext context) {
		if (context == null) {
			throw new NullPointerException("WebContext is null");
		}
		String ltc = context.getLtc();
		if (ltc == null || ltc.length() == 0) {
			throw new NullPointerException("can not get ltc");
		}

		// 检查币种是否设置
		String cy = context.getCurrencyCode();
		if (cy == null || cy.length() == 0) {
			throw new NullPointerException("currencyCode is null");
		}

		// String cy = foundation.getCurrency(context);
		int siteID = foundation.getSiteID(context);
		int langID = foundation.getLanguage(context);
		String vhost = context.getVhost();
		String ip = context.getRemoteAddress();
		// 获取登录信息
		LoginContext loginCtx = foundation.getLoginContext(context);
		String email = null;
		if (loginCtx != null && loginCtx.isLogin()) {
			email = loginCtx.getMemberID();
		}
		CartGetRequest cartRequest = new CartGetRequest(email, ltc, siteID,
				langID, cy);
		Cart cart = cartService.getOrCreateCart(cartRequest);
		// 检查该购物车是否已经生成订单了,如果已经生成订单了 则不能再生成订单了
		if (cart == null || cart.isAlreadyTransformOrder()) {
			throw new OrderException(ExType.IncompleteInformation);
		}
		String cartId = cart.getId();
		OrderConfirmationRequest req = new OrderConfirmationRequest(cartId,
				siteID, 0, 0, null, null, ip, langID, cy, vhost);
		req.setNoAddress(true);

		return confirmOrder(req);
	}

	@Override
	public List<CartItem> deserializeOrder(String orderNum) {

		return null;
	}

	@Override
	public boolean updateShipAddressAndShipPrice(Order order) {
		Map<String, Object> paras = Maps.newHashMap();
		paras.put("email", order.getCemail());
		paras.put("memberEmail", order.getCmemberemail());
		paras.put("countryCode", order.getCcountrysn());
		paras.put("country", order.getCcountry());
		paras.put("street", order.getCstreetaddress());
		paras.put("city", order.getCcity());
		paras.put("province", order.getCprovince());
		paras.put("zipCode", order.getCpostalcode());
		paras.put("firstName", order.getCfirstname());
		paras.put("shippingMethodId", order.getIshippingmethodid());
		paras.put("shippingPrice", order.getFshippingprice());
		paras.put("telephone", order.getCtelephone());
		paras.put("orderNum", order.getCordernumber());
		paras.put("message", order.getCmessage());
		paras.put("grandtotal", order.getFgrandtotal());
		paras.put("lastName", order.getClastname());
		paras.put("cshippingcode", order.getCshippingcode());

		int affectColumns = this.orderMapper.update(paras);
		return affectColumns > 0 ? true : false;
	}

	@Override
	public double getFreight(String orderNum, String shipMethodId,
			String shipToCountryCode) {
		if (orderNum == null || orderNum.length() == 0) {
			throw new NullPointerException("orderNum is null");
		}
		if (shipMethodId == null || shipMethodId.length() == 0) {
			throw new NullPointerException("shipMethodId is null");
		}
		if (shipToCountryCode == null || shipToCountryCode.length() == 0) {
			throw new NullPointerException("shipToCountryCode is null");
		}

		Order order = orderMapper.getOrderByOrderNumber(orderNum);
		// 订单基本总价
		double baseTotal = order.getFordersubtotal();

		List<valueobjects.order_api.cart.CartItem> items = orderDetailDao
				.selectCartItemsByOrderNum(orderNum);
		Logger.debug("order items size:{}", items.size());
		List<String> listingId = Lists.newLinkedList();
		FluentIterable
				.from(items)
				.forEach(
						item -> {
							if (item instanceof valueobjects.order_api.cart.SingleCartItem) {
								listingId.add(item.getClistingid());
							} else if (item instanceof valueobjects.order_api.cart.BundleCartItem) {
								List<SingleCartItem> childs = ((valueobjects.order_api.cart.BundleCartItem) item)
										.getChildList();
								List<String> clisting = Lists.transform(childs,
										c -> c.getClistingid());
								listingId.addAll(clisting);
							} else {
								listingId.add(item.getClistingid());
							}
						});

		// 币种
		String currencyCode = order.getCcurrency();

		Double weight = freightService.getTotalWeight(items);
		Double shippingWeight = freightService.getTotalShipWeight(items);

		Country ct = countryMapper.getCountryByCountryName(shipToCountryCode);
		Integer ship = Integer.parseInt(shipMethodId);
		ShippingMethodDetail shippingMethod = shippingMethodService
				.getShippingMethodDetail(ship, null);
		int site = this.foundation.getSiteID();
		Logger.debug("*************************");
		Logger.debug("weight:{}", weight);
		Logger.debug("shippingWeight:{}", shippingWeight);
		Logger.debug("shippingMethod:{}", shippingMethod.getIid());
		Logger.debug("Country:{}", ct.getCname());
		Logger.debug("baseTotal:{}", baseTotal);
		Logger.debug("currencyCode:{}", currencyCode);
		Logger.debug("site:{}", site);
		Logger.debug("listingId:{}", listingId.toString());
		Logger.debug("*************************");
		Double freight = getDoubleFreight(weight, shippingWeight,
				shippingMethod, ct, baseTotal, currencyCode, site, listingId);

		return freight;
	}

	/**
	 * 2位国家代码+1位订单来源+1位订单类型+5位日期YYMDD+5位时分秒标识+6位随机码
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 * @param type
	 * @return
	 */
	private String createOrderNumberV2(String shipToCountryCode, String type) {
		String vhost = this.foundation.getVhost();
		// 订单来源
		String placeholder = vhostService.getCorderplaceholder(vhost);
		if (placeholder == null || placeholder.length() == 0) {
			placeholder = "A";
		}

		StringBuilder number = new StringBuilder();
		if (shipToCountryCode != null && shipToCountryCode.length() > 0) {
			number.append(shipToCountryCode);
			number.append("-");
		}

		number.append(placeholder);
		number.append(type);
		// 系统时间
		SimpleDateFormat formater = new SimpleDateFormat("yy-M-dd-HH-mm-ss");
		Date date = new Date();
		String dateStr = formater.format(date);
		String[] dates = dateStr.split("-");
		// 年
		number.append(dates[0]);
		// 月
		/*
		 * M: I 规则>1到12个月对应 {A,B,C,D,E,F,G,H,I,J,K,L}
		 */
		int month = Integer.parseInt(dates[1]) + 64;
		number.append((char) month);
		// 日
		number.append(dates[2]);
		// hh 时(一位)：0(即24点)点对应X, 24小时制对应字母
		int hhInt = Integer.parseInt(dates[3]);
		int hh = (hhInt == 0 ? 88 : hhInt + 64);
		number.append((char) hh);
		// 分秒
		number.append(dates[4]);
		number.append(dates[5]);
		number.append("-");
		// 6位随机码
		String randomString = this.randomString(6);
		number.append(randomString);
		return number.toString();
	}

	private String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			sb.append(SYMBOLS[random.nextInt(SYMBOLS.length)]);
		}
		return sb.toString();
	}

	@Override
	public String createGeneralOrderNumberV2(String shipToCountryCode) {
		return this.createOrderNumberV2(shipToCountryCode, "N");
	}

	@Override
	public String createGuestOrderNumberV2(String shipToCountryCode) {
		return this.createOrderNumberV2(shipToCountryCode, "G");
	}

	@Override
	public String createAgentOrderNumberV2(String shipToCountryCode) {
		return this.createOrderNumberV2(shipToCountryCode, "D");
	}

	@Override
	public boolean saveOrderExtras(Order order, List<LoyaltyPrefer> prefers) {
		if (order == null || prefers == null) {
			return true;
		}

		for (LoyaltyPrefer p : prefers) {
			try {

				BillDetail bill = new BillDetail();
				bill.setCmsg(p.getCode());
				bill.setCtype(p.getPreferType());
				bill.setFtotalprice(p.getValue());
				bill.setIorderid(order.getIid());
				bill.setIqty(1);
				billDetailService.insert(bill);
			} catch (Exception e) {
				Logger.debug(
						"Extras[type:{} code:{} insert to t_order_bill_detail error",
						p.getPreferType(), p.getCode(), e);
				continue;
			}
		}
		return true;
	}

	@Override
	@Transactional(executorType = ExecutorType.BATCH, rethrowExceptionsAs = Exception.class)
	public Order createOrderInstanceForSubtotal(CreateOrderRequest request) {
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx.getMemberID();
		Order order = new Order();
		List<valueobjects.cart.CartItem> items = request.getItems();

		List<String> listingIds = Lists.newLinkedList();

		double subTotal = checkoutService.subToatl(items);

		for (valueobjects.cart.CartItem item : items) {
			if (item instanceof valueobjects.cart.SingleCartItem) {
				listingIds.add(item.getClistingid());
			} else if (item instanceof valueobjects.cart.BundleCartItem) {
				List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
						.getChildList();
				List<String> list = Lists.transform(childs,
						child -> child.getClistingid());
				listingIds.addAll(list);
			} else {
				listingIds.add(item.getClistingid());
			}
		}
//		Storage shippingStorage;
//		if (request.getStorage() == null) {
//			shippingStorage = shippingServices.getShippingStorage(
//					request.getSiteId(), null, listingIds);
//		} else {
//			shippingStorage = this.storageService
//					.getStorageForStorageId(request.getStorage());
//		}

		Double extraTotal = 0.0;
		List<LoyaltyPrefer> prefers = request.getPrefer();
		if (prefers != null) {
			for (LoyaltyPrefer p : prefers) {
				extraTotal = extraTotal + p.getValue();
			}
		}
		Integer storageid = 1;
		if(request.getStorage()!=null){
			storageid = request.getStorage();
		}
		order.setIstorageid(storageid);
		order.setCcurrency(request.getCurrency());
		order.setCemail(email);
		order.setFextra(extraTotal);
		order.setFordersubtotal(subTotal);
		order.setFgrandtotal(subTotal + extraTotal);
		order.setIwebsiteid(request.getSiteId());
		order.setCorigin(request.getOrigin());
		order.setCmemberemail(email);
		order.setCmessage(request.getMessage());
		order.setCip(request.getIp());
		order.setCvhost(request.getVhost());
		String orderNum = this.createGeneralOrderNumberV2(null);
		order.setCordernumber(orderNum);
		order.setCpaymenttype(request.getCpaymenttype());	//支付类型

		// insert order first with NULL status
		if (insertOrder(order)) {
			Integer orderId = order.getIid();
			List<BillDetail> bills = new ArrayList<BillDetail>();
			bills.add(getShippingBill(order));
			List<OrderDetail> details = createDetails(orderId, items, bills);
			if (details.size() != 0 && insertDetail(details)) {
				if (saveOrderExtras(order, prefers)) {
					if (billDetailService.batchInsert(bills)) {
						// everything ok, update order status
						statusService.changeOrdeStatus(orderId,
								IOrderStatusService.PAYMENT_PENDING);
						Logger.debug(
								"Start sending ------>OrderConfirmationEvent<------events,orderNum={},class==OrderService,method=createOrderInstanceForSubtotal",
								order.getCordernumber());
						eventBus.post(new OrderConfirmationEvent(order
								.getCemail(), order.getIid(), null));
						return order;
					} else {
						throw new OrderException(ExType.BillDetailFailed);
					}
				} else {
					throw new OrderException(ExType.ExtrasFailed);
				}
			} else {
				throw new OrderException(ExType.OrderDetailFailed);
			}
		} else {
			throw new OrderException(ExType.Unknown);
		}
	}

	@Override
	public List<OrderReportForm> getOrderReport(Date startdate, Date enddate,
			String type, int iwebsiteid,String cvhost) {
		return orderMapper.getOrderReport(startdate, enddate, type, iwebsiteid,cvhost);
	}
	
	/**
	 * 生成订单号
	 */
	@Override
	public String generateOrderNum(){
		return this.createGeneralOrderNumberV2(null);
	}
	
	/***********NEW************/
	
	public List<valueobjects.cart.CartItem> transToNewCartItems(List<valueobjects.order_api.cart.CartItem> olist){
		List<valueobjects.cart.CartItem> nlist = Lists.newArrayList();
		for(valueobjects.order_api.cart.CartItem c : olist){
			valueobjects.cart.CartItem ci = new valueobjects.cart.CartItem();
			ci.setSku(c.getSku());
			ci.setClistingid(c.getClistingid());
			ci.setIqty(c.getIqty());
			nlist.add(ci);
		}
		return nlist;
	}
	
	@Override
	public List<String> getHostBySite(int site){
		return vhostService.getHostBySite(site);
	}
}


