package services.mobile.order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import services.ICurrencyService;
import services.interaction.review.IProductReviewsService;
import services.mobile.MobileService;
import services.order.IOrderCountService;
import services.order.IOrderEnquiryService;
import services.order.IOrderPackService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.IOrderUpdateService;
import services.order.exception.OrderException;
import utils.ImageUtils;
import utils.Page;
import utils.ValidataUtils;
import valueobjects.order_api.OrderConfirmationRequest;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.payment.PaymentContext;
import valuesobject.mobile.BaseResultType;

import com.google.common.collect.Lists;

import dto.Currency;
import dto.mobile.OdGoods;
import dto.mobile.OdItem;
import dto.mobile.OrderDetailInfo;
import dto.order.Order;
import dto.order.OrderPack;
import forms.order.MemberOrderForm;

public class OrderService {

	@Inject
	IOrderEnquiryService orderEnquiryService;

	@Inject
	IOrderStatusService orderStatusService;

	@Inject
	IOrderUpdateService updateService;

	@Inject
	IOrderCountService countService;

	@Inject
	IOrderService orderSvc;

	@Inject
	ICurrencyService currencyService;

	@Inject
	IOrderPackService orderPackService;

	@Inject
	IProductReviewsService productReviewsService;

	@Inject
	MobileService mobileService;

	/**
	 * 待付款
	 */
	public static final Integer PAYMENT_PENDING = 1;
	/**
	 * 收款成功
	 */
	public static final Integer PAYMENT_CONFIRMED = 2;
	/**
	 * 订单已取消
	 */
	public static final Integer ORDER_CANCELLED = 3;
	/**
	 * 订单正在处理中（从成功收款到到发货这段时间）
	 */
	public static final Integer PROCESSING = 4;
	/**
	 * 订单审核中，比如可能这个单的付款有些问题，我们暂时hold住，不发货
	 */
	public static final Integer ON_HOLD = 5;
	/**
	 * 订单已发货
	 */
	public static final Integer DISPATCHED = 6;
	/**
	 * 订单已完成
	 */
	public static final Integer COMPLETED = 7;
	/**
	 * 已退款
	 */
	public static final Integer REFUNDED = 8;
	/**
	 * 收款处理中
	 */
	public static final Integer PAYMENT_PROCESSING = 9;

	/**
	 * 查询用户订单列表(包括订单商品资料)
	 * 
	 * @param email
	 * @param form
	 * @param siteId
	 * @param isNormal
	 * 
	 * @return Page<OdItem>
	 */
	public Page<OdItem> getOrdersList(String email, MemberOrderForm form,
			int siteId, boolean isNormal, int langid) {
		int pageCount = orderEnquiryService.searchOrderCount(email, form,
				siteId, isNormal);
		if (pageCount > 0) {
			List<Order> orders = orderEnquiryService.searchOrders(email, form,
					siteId, true);
			List<OdItem> allItems = Lists.newArrayList();
			Currency cu = null;
			OdItem oi = null;
			for (Order ord : orders) {
				oi = new OdItem();
				oi.setIid(ord.getIid());
				oi.setOid(ord.getCordernumber());
				oi.setOdtotal(ord.getFgrandtotal());
				List<OrderPack> opack = orderPackService.getByOrderId(ord
						.getIid());
				if (opack != null && opack.size() > 0) {
					String shipid = "";
					for (OrderPack op : opack) {
						if ("".equals(shipid)) {
							shipid = op.getCtrackingnumber();
						} else {
							shipid = "," + op.getCtrackingnumber();
						}
					}
					oi.setShipid(shipid);
				}
				oi.setStatus(ord.getIstatus());
				oi.setPay(ord.getCpaymentid());
				oi.setCdate(ord.getDcreatedate().getTime());
				if (null == ord.getCcurrency()) {
					ord.setCcurrency("USD");
				}
				cu = currencyService.getCurrencyByCode(ord.getCcurrency());
				if (cu != null) {
					oi.setCsymbol(cu.getCsymbol());
				}
				List<OrderItem> items = orderSvc.getOrderDetailByOrder(ord,
						langid);
				List<OdGoods> ogList = Lists.newArrayList();
				for (OrderItem oitem : items) {
					OdGoods og = new OdGoods();
					og.setGid(oitem.getClistingid());
					og.setTitle(oitem.getCtitle());
					og.setImgurl(ImageUtils.getWebPath(oitem.getCimageurl(),
							265, 265, mobileService.getMobileContext()));
					og.setQty(oitem.getIqty());
					og.setSku(oitem.getSku());
					og.setSale(oitem.getUnitPrice());
					og.setPcs(oitem.getOriginalPrice());
					String attr = "";
					Map<String, String> map = oitem.getAttributeMap();
					Iterator<Entry<String, String>> iter = map.entrySet()
							.iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map.Entry entry = (Map.Entry) iter.next();
						String key = (String) entry.getKey();
						String val = (String) entry.getValue();
						attr = key + " " + val;
					}
					og.setAtts(attr);

					ogList.add(og);
				}
				oi.setOglist(ogList);
				allItems.add(oi);
			}
			return new Page<OdItem>(allItems, pageCount, form.getPageNum(),
					form.getPageSize());
		}
		return null;
	}

	/**
	 * 获取用户订单状态分类数量
	 * 
	 * @param email
	 * @return OrderCount
	 * 
	 *         public OrderClassCount getOrderCount(String email, Integer
	 *         siteId, Integer isShow, boolean isNormal) {
	 * 
	 *         OrderClassCount occ = new OrderClassCount(); Integer all =
	 *         orderMapper.getCountByEmailAndStatus(email, null, siteId, isShow,
	 *         isNormal); Integer pending =
	 *         orderMapper.getCountByEmailAndStatus(email, PAYMENT_PENDING,
	 *         siteId, isShow, isNormal); Integer confirmed =
	 *         orderMapper.getCountByEmailAndStatus(email, PAYMENT_CONFIRMED,
	 *         siteId, isShow, isNormal); Integer cancelled =
	 *         orderMapper.getCountByEmailAndStatus(email, ORDER_CANCELLED,
	 *         siteId, isShow, isNormal); Integer processing =
	 *         orderMapper.getCountByEmailAndStatus(email, PROCESSING, siteId,
	 *         isShow, isNormal); Integer onHold =
	 *         orderMapper.getCountByEmailAndStatus(email, ON_HOLD, siteId,
	 *         isShow, isNormal); Integer dispatched =
	 *         orderMapper.getCountByEmailAndStatus(email, DISPATCHED, siteId,
	 *         isShow, isNormal); Integer completed =
	 *         orderMapper.getCountByEmailAndStatus(email, COMPLETED, siteId,
	 *         isShow, isNormal); Integer refunded =
	 *         orderMapper.getCountByEmailAndStatus(email, REFUNDED, siteId,
	 *         isShow, isNormal); Integer payin =
	 *         orderMapper.getCountByEmailAndStatus(email, PAYMENT_PROCESSING,
	 *         siteId, isShow, isNormal); Integer recycle =
	 *         orderMapper.getCountByEmailAndStatus(email, null, siteId, 2,
	 *         isNormal);
	 * 
	 *         occ.setAll(ValidataUtils.validataInt(all));
	 *         occ.setPending(ValidataUtils.validataInt(pending));
	 *         occ.setConfirmed(ValidataUtils.validataInt(confirmed));
	 *         occ.setCancelled(ValidataUtils.validataInt(cancelled));
	 *         occ.setOrdering(ValidataUtils.validataInt(processing));
	 *         occ.setOnHold(ValidataUtils.validataInt(onHold));
	 *         occ.setDispatched(ValidataUtils.validataInt(dispatched));
	 *         occ.setCompleted(ValidataUtils.validataInt(completed));
	 *         occ.setRefunded(ValidataUtils.validataInt(refunded));
	 *         occ.setPayin(ValidataUtils.validataInt(payin));
	 *         occ.setRecycle(ValidataUtils.validataInt(recycle));
	 * 
	 *         return occ; }
	 */

	/**
	 * 添加订单
	 * 
	 * @param cartId
	 * @param siteId
	 * @param addressId
	 * @param shippingMethodId
	 * @param origin
	 * @param message
	 * @param ip
	 * @param landId
	 * @param currency
	 * @param vhost
	 * @return String
	 */
	public String addOrders(String cartId, int siteId, int addressId,
			int shippingMethodId, String origin, String message, String ip,
			int langID, String currency, String vhost) throws OrderException {
		String orderNum = "";
		OrderConfirmationRequest ocf = new OrderConfirmationRequest(cartId,
				siteId, addressId, shippingMethodId, origin, message, ip,
				langID, currency, vhost);
		orderNum = orderSvc.confirmOrder(ocf);
		return orderNum;

	}

	/**
	 * 查询订单详情
	 * 
	 * @param orderNum
	 * @return
	 */
	public OrderDetailInfo getOrderDetail(String orderId, int langId) {
		PaymentContext context = orderSvc.getPaymentContext(orderId, langId);
		if (null == context) {
			return null;
		}
		Order order = context.getOrder().getOrder();
		if (null == order) {
			return null;
		}
		List<OrderItem> items = orderSvc.getOrderDetailByOrder(order, langId);
		if (null == items) {
			return null;
		}
		List<OdGoods> ogList = Lists.newArrayList();
		for (OrderItem oitem : items) {
			OdGoods og = new OdGoods();
			og.setImgurl(ImageUtils.getWebPath(oitem.getCimageurl(), 265, 265,
					mobileService.getMobileContext()));
			og.setGid(oitem.getClistingid());
			og.setTitle(oitem.getCtitle());
			og.setQty(oitem.getIqty());
			og.setSku(oitem.getSku());
			og.setSale(oitem.getUnitPrice());
			og.setPcs(oitem.getOriginalPrice());
			Double score = productReviewsService.getAverageScore(oitem
					.getClistingid());
			score = ValidataUtils.validataDouble(score);
			Integer count = productReviewsService.getReviewCount(oitem
					.getClistingid());
			count = ValidataUtils.validataInt(count);
			og.setScore(score);
			og.setCount(count);
			String attr = "";
			Map<String, String> map = oitem.getAttributeMap();
			Iterator<Entry<String, String>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				String val = (String) entry.getValue();
				attr = ValidataUtils.validataStr(key) + " "
						+ ValidataUtils.validataStr(val);
			}
			og.setAtts(attr);
			ogList.add(og);
		}
		OrderDetailInfo odinfo = new OrderDetailInfo();
		odinfo.setOglist(ogList);
		odinfo.setIid(order.getIid());
		odinfo.setOdtotal(order.getFgrandtotal());
		odinfo.setStatus(order.getIstatus());
		odinfo.setPay(ValidataUtils.validataStr(order.getCpaymentid()));
		odinfo.setCdate(order.getDcreatedate().getTime());
		odinfo.setOid(order.getCordernumber());
		String addr = ValidataUtils.validataStr(order.getCcountry()) + " "
				+ ValidataUtils.validataStr(order.getCprovince()) + " "
				+ ValidataUtils.validataStr(order.getCcity()) + " "
				+ ValidataUtils.validataStr(order.getCstreetaddress()) + " "
				+ ValidataUtils.validataStr(order.getCpostalcode());
		odinfo.setAddr(addr);
		odinfo.setTel(ValidataUtils.validataStr(order.getCtelephone()));
		odinfo.setFname(ValidataUtils.validataStr(order.getCfirstname()) + " "
				+ ValidataUtils.validataStr(order.getClastname()));
		odinfo.setShippcs(ValidataUtils.validataDouble(order
				.getFshippingprice()));
		odinfo.setOdsubtotal(ValidataUtils.validataDouble(order
				.getFordersubtotal()));
		odinfo.setExtra(ValidataUtils.validataDouble(order.getFextra()));
		odinfo.setPaydate(ValidataUtils.ValidataDate(order.getDpaymentdate()));
		List<OrderPack> opack = orderPackService.getByOrderId(order.getIid());
		if (opack != null && opack.size() > 0) {
			String shipid = "";
			for (OrderPack op : opack) {
				if ("".equals(shipid)) {
					shipid = op.getCtrackingnumber();
				} else {
					shipid = "," + op.getCtrackingnumber();
				}
			}
			odinfo.setShipid(shipid);
		}
		odinfo.setShipdescr(ValidataUtils.validataStr(context
				.getShippingMethod().getCname())
				+ " "
				+ ValidataUtils.validataStr(context.getShippingMethod()
						.getCcontent()));
		String csy = "";
		if (null == context.getCurrency()) {
			csy = "US$";
		} else {
			csy = context.getCurrency().getCsymbol();
		}
		odinfo.setCsymbol(csy);
		return odinfo;
	}

	/**
	 * 确认订单
	 * 
	 * @param oiid
	 *            (订单主键ID)
	 * @param email
	 */

	public HashMap<String, Object> completedOrder(int oiid, String email) {
		boolean b = false;
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		com.website.dto.order.Order o = orderEnquiryService
				.getOrderByOrderId(oiid);
		if (null == o) {
			objMap.put("re", BaseResultType.ORDER_NOT_FIND_ERROR_CODE);
			objMap.put("re", BaseResultType.ORDER_NOT_FIND_ERROR_MSG);
		}
		if (o.getStatus() != DISPATCHED) {
			objMap.put(
					"re",
					BaseResultType.UPDATE_ORDER_STATUS_NOT_DISPATCHED_ERROR_CODE);// 状态不能确认
			objMap.put("re",
					BaseResultType.UPDATE_ORDER_STATUS_NOT_DISPATCHED_ERROR_MSG);
			return objMap;
		}
		b = orderStatusService.changeOrdeStatusValidEmailAndStatus(oiid,
				IOrderStatusService.COMPLETED, email,
				IOrderStatusService.DISPATCHED);
		if (b) {
			objMap.put("re", 1);
		} else {
			objMap.put("re", BaseResultType.COMFIR_ORDER_ERROR_CODE);// 确认失败
		}

		return objMap;
	}

	/**
	 * 删除订单
	 * 
	 * @param idList
	 * @param email
	 */
	public boolean deleteOrders(List<Integer> idList, String email) {
		return updateService.updateShowValidEmail(
				IOrderUpdateService.SHOW_TYPE_FALSE, idList, email);
	}

	/**
	 * 移出订单
	 * 
	 * @param idList
	 * @param email
	 */
	public boolean removeOrders(List<Integer> idList, String email) {
		return updateService.updateShowValidEmail(
				IOrderUpdateService.SHOW_TYPE_RECYCLE, idList, email);
	}

	/**
	 * 还原订单（将移除订单还原）
	 * 
	 * @param idList
	 * @param email
	 */
	public boolean restoreOrders(List<Integer> idList, String email) {
		return updateService.updateShowValidEmail(
				IOrderUpdateService.SHOW_TYPE_TURE, idList, email);
	}

}
