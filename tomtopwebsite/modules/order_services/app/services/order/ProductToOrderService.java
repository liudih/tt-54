package services.order;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductStorageMapMapper;
import play.Logger;
import services.base.CountryService;
import services.base.WebsiteService;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.member.address.AddressService;
import services.order.exception.ExType;
import services.order.exception.OrderException;
import services.product.ProductEnquiryService;
import services.shipping.IShippingMethodService;
import services.shipping.ShippingServices;
import valueobjects.member.MemberInSession;
import valueobjects.order_api.SaveOrderRequest;
import valueobjects.product.ProductLite;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.Country;
import dto.ShippingMethodDetail;
import dto.Storage;
import dto.Website;
import dto.member.MemberAddress;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.product.ProductStorageMap;
import extensions.order.IOrderDetailProvider;

public class ProductToOrderService implements IProductToOrderService {
	@Inject
	private IOrderService orderService;
	@Inject
	private CountryService countryService;
	@Inject
	private ProductEnquiryService productEnquiry;
	@Inject
	private ShippingServices shippingServices;
	@Inject
	private IFreightService freightService;
	@Inject
	private IShippingMethodService shippingMethodService;
	@Inject
	private AddressService addressService;
	@Inject
	private IBillDetailService billDetailService;
	@Inject
	private OrderDetailProviderService orderDetailProviderService;
	@Inject
	private WebsiteService websiteService;
	@Inject
	private IOrderStatusService statusService;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private OrderUpdateService orderUpdate;
	@Inject
	private ProductStorageMapMapper productStorageMapMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IProductToOrderService#saveOrder(valueobjects.order_api
	 * .SaveOrderRequest)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Order saveOrder(SaveOrderRequest req) {
		MemberAddress billAddress = addressService.getDefaultOrderAddress(req
				.getMis().getEmail());
		List<String> listingIDs = Lists.newArrayList(req.getListingQtyMap()
				.keySet());
		Order order = createOrder(req);
		Map<String, Object> detailMap = createOrderDetails(req, order,
				listingIDs);
		List<OrderDetail> orderDetails = (List<OrderDetail>) detailMap
				.get("detailList");
		List<BillDetail> billDetails = (List<BillDetail>) detailMap
				.get("billList");
		Double subtotal = (Double) detailMap.get("subtotal");
		order.setFordersubtotal(subtotal);
		setShippingMethod(req, billDetails, order, subtotal, listingIDs);
		setOrderNumber(order,
				req.getAddress() == null ? billAddress : req.getAddress(), req
						.getMis().getMemberId());
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(
				order.getFordersubtotal());
		if (order.getFshippingprice() == null) {
			order.setFgrandtotal(dcu.add(order.getFextra()).doubleValue());
		} else {
			order.setFgrandtotal(dcu.add(order.getFextra())
					.add(order.getFshippingprice()).doubleValue());
		}
		
		//设置仓库id
		List<ProductStorageMap> stlist = productStorageMapMapper.getProductStorages(listingIDs);
		boolean issameStorage = true;
		if(stlist.size()>1){
			Integer firststo = stlist.get(0).getIstorageid();
			for(int i=1;i<stlist.size();i++){
				if(stlist.get(i).getIstorageid()!=firststo){
					issameStorage = false;
					break;
				}
			}
		}
		order.setIstorageid((issameStorage ? stlist.get(0).getIstorageid() : 1));
		orderService.insertOrder(order);
		insertDetails(orderDetails, billDetails, order);
		return order;
	}

	private void insertDetails(List<OrderDetail> orderDetails,
			List<BillDetail> billDetails, Order order) {
		orderDetails.forEach(e -> e.setIorderid(order.getIid()));
		billDetails.forEach(e -> e.setIorderid(order.getIid()));
		orderService.insertDetail(orderDetails);
		billDetailService.batchInsert(billDetails);
	}

	private void setOrderNumber(Order order, MemberAddress billAddress,
			int memberID) {
		String countryShortName = order.getCcountrysn();
		if (countryShortName == null && billAddress != null
				&& billAddress.getIcountry() != null) {
			Country country = countryService.getCountryByCountryId(billAddress
					.getIcountry());
			countryShortName = country.getCshortname();
		} else if (countryShortName == null) {
			Website web = websiteService.getWebsite(order.getIwebsiteid());
			Integer countryID = web.getIdefaultshippingcountry();
			if (countryID != null) {
				Country country = countryService
						.getCountryByCountryId(countryID);
				countryShortName = country != null ? country.getCshortname()
						: "";
			} else {
				countryShortName = "";
			}
		}
		order.setCordernumber(orderService.getOrderCid(countryShortName,
				memberID));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IProductToOrderService#updateShippingMethod(java.lang.
	 * String, int, int, int)
	 */
	@Override
	public boolean updateShippingMethod(String orderID, int shippingID,
			int siteID, int languageID) {
		Order order = orderEnquiry.getOrderById(orderID);
		if (order == null || StringUtils.isEmpty(order.getCcountrysn())) {
			return false;
		}
		List<OrderDetail> details = orderEnquiry
				.getOrderDetails(order.getIid());
		List<String> listingIDs = Lists.transform(details,
				d -> d.getClistingid());
		Map<String, Integer> qtyMap = Maps.newHashMap();
		for (OrderDetail d : details) {
			qtyMap.put(d.getClistingid(), d.getIqty());
		}
		Country country = countryService.getCountryByShortCountryName(order
				.getCcountrysn());
		Storage shippingStorage = shippingServices.getShippingStorage(siteID,
				country, listingIDs);
		Double weight = freightService.getTotalWeight(qtyMap, false);
		Double shippingWeight = freightService.getTotalWeight(qtyMap, true);
		ShippingMethodDetail shippingMethod = shippingMethodService
				.getShippingMethodDetail(shippingID, languageID);
		Double freight = orderService.getDoubleFreight(weight, shippingWeight,
				shippingMethod, country, order.getFordersubtotal(),
				order.getCcurrency(), siteID, listingIDs);
		if (!orderService.checkShippingMethodCorrect(shippingStorage.getIid(),
				country.getCshortname(), shippingID, weight,
				order.getFordersubtotal(), listingIDs, order.getCcurrency(),
				languageID)) {
			Logger.error(
					"OrderService checkShippingMethodCorrect error: "
							+ "storageId: {}, country: {}, shippingMethodId: {}, weight: {}",
					shippingStorage.getIid(), country.getCshortname(),
					shippingID, weight);
			throw new OrderException(ExType.InvalidShippingMethod);
		} else {
			order.setIshippingmethodid(shippingID);
			order.setFshippingprice(freight);
			order.setIstorageid(shippingStorage.getIid());
			order.setCshippingcode(shippingMethod.getCcode());
			order.setFgrandtotal(order.getFextra() + order.getFordersubtotal()
					+ order.getFshippingprice());
			BillDetail billDetail = orderService.getShippingBill(order);
			billDetailService.insert(billDetail);
			orderUpdate.updateShippingMethod(order);
			return true;
		}
	}

	private void setShippingMethod(SaveOrderRequest req,
			List<BillDetail> billDetails, Order order, Double subtotal,
			List<String> listingIDs) {
		if (!req.getIsNeedShippingMethod()) {
			order.setFshippingprice(0.0);
		}
		if (req.getShippingMethodID() == null || order.getCcountrysn() == null) {
			return;
		}
		Country country = countryService.getCountryByShortCountryName(order
				.getCcountrysn());
		if (country == null) {
			return;
		}
		Storage shippingStorage = shippingServices.getShippingStorage(
				req.getWebsiteID(), country, listingIDs);
		Double weight = freightService.getTotalWeight(req.getListingQtyMap(),
				false);
		Double shippingWeight = freightService.getTotalWeight(
				req.getListingQtyMap(), true);
		ShippingMethodDetail shippingMethod = shippingMethodService
				.getShippingMethodDetail(req.getShippingMethodID(),
						req.getLanguageID());
		Double freight = orderService.getDoubleFreight(weight, shippingWeight,
				shippingMethod, country, subtotal, req.getCurrency(),
				req.getWebsiteID(), listingIDs);
		if (!orderService.checkShippingMethodCorrect(shippingStorage.getIid(),
				country.getCshortname(), req.getShippingMethodID(), weight,
				subtotal, listingIDs, req.getCurrency(), req.getLanguageID())) {
			Logger.error(
					"OrderService checkShippingMethodCorrect error: "
							+ "storageId: {}, country: {}, shippingMethodId: {}, weight: {}",
					shippingStorage.getIid(), country.getCshortname(),
					req.getShippingMethodID(), weight);
			throw new OrderException(ExType.InvalidShippingMethod);
		} else {
			order.setIshippingmethodid(req.getShippingMethodID());
			order.setFshippingprice(freight);
			order.setIstorageid(shippingStorage.getIid());
			order.setCshippingcode(shippingMethod.getCcode());
			BillDetail billDetail = orderService.getShippingBill(order);
			billDetails.add(billDetail);
		}
	}

	private Map<String, Object> createOrderDetails(SaveOrderRequest req,
			Order order, List<String> listingIDs) {
		Map<String, Integer> listingIDQtyMap = req.getListingQtyMap();

		List<ProductLite> productLites = productEnquiry
				.getProductLiteByListingIDs(listingIDs, req.getWebsiteID(),
						req.getLanguageID());
		Map<String, ProductLite> productMap = Maps.uniqueIndex(productLites,
				p -> p.getListingId());
		Map<String, Object> map = Maps.newHashMap();
		Double subtotal = 0.0;
		List<BillDetail> billList = Lists.newArrayList();
		IOrderDetailProvider provider = orderDetailProviderService
				.getProvider(req.getDetailProviderID());
		List<OrderDetail> detailList = provider.createOrderDetails(productMap,
				listingIDQtyMap, req.getCurrency(), req.getWebsiteID(),
				req.getLanguageID(), req.getMis().getEmail());
		for (OrderDetail detail : detailList) {
			orderService.parseBill(billList, detail);
			subtotal += detail.getFtotalprices();
		}
		map.put("detailList", detailList);
		map.put("billList", billList);
		map.put("subtotal", subtotal);
		return map;
	}

	private Order createOrder(SaveOrderRequest req) {
		Order order = new Order();
		MemberInSession mis = req.getMis();
		MemberAddress address = req.getAddress();
		Integer status = statusService
				.getIdByName(IOrderStatusService.PAYMENT_PENDING);
		order.setIstatus(status);
		order.setCcurrency(req.getCurrency());
		order.setCmemberemail(mis.getEmail());
		order.setCemail(mis.getEmail());
		order.setIwebsiteid(req.getWebsiteID());
		order.setCorigin(req.getOrigin());
		order.setCip(req.getIp());
		order.setCvhost(req.getVhost());
		order.setCmessage(req.getMessage());
		order.setFextra(0.0);
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
		return order;
	}
}
