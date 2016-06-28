package services.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import play.libs.F.Tuple3;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.CreateOrderRequest;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.OrderConfirmationRequest;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.payment.PaymentContext;
import context.WebContext;
import dto.Country;
import dto.ShippingMethodDetail;
import dto.TopBrowseAndSaleCount;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.OrderReportForm;
import facades.cart.Cart;

public interface IOrderService {

	public abstract String confirmOrder(WebContext context, String cartID,
			int shippingMethodID, int addressID, String origin, String message);

	/**
	 *
	 * @param orderConfirmation
	 * @return order ID
	 */
	public abstract String confirmOrder(
			OrderConfirmationRequest orderConfirmation);

	public abstract BillDetail getShippingBill(Order order);

	public abstract boolean insertDetail(List<OrderDetail> details);

	public abstract boolean insertOrder(Order order);

	public abstract boolean validOrder(String cartId);

	/**
	 * 验证取得的运送方式的ID当前订单是否可使用
	 *
	 * @param storageId
	 * @param country
	 *            国家的英文缩写
	 * @param cunrrency
	 *            TODO
	 * @param langID
	 *            TODO
	 * @return
	 * @author luojiaheng
	 */
	public abstract boolean checkShippingMethodCorrect(Integer storageId,
			String country, Integer id, Double weight, Double subTotal,
			List<String> listingIds, String cunrrency, int langID);

	public abstract Tuple3<Cart, Order, Map<String, ExtraSaveInfo>> createOrderInstance(
			OrderConfirmationRequest request, Integer statusId);

	public abstract String getOrderCid(Order order, int memberID);

	public abstract String getOrderCid(String countryShortName, int memberID);

	public abstract Double getDoubleFreight(Double weight,
			Double shippingWeight, ShippingMethodDetail shippingMethod,
			Country country, double baseTotal, String currency, int websiteId,
			List<String> listingIDs);

	public abstract PaymentContext getPaymentContext(String corderId, int langID);

	public abstract BillDetail parseBill(List<BillDetail> bills,
			OrderDetail orderDetail);

	public abstract List<OrderItem> getOrderDetailByOrder(Order order);

	public abstract List<OrderItem> getOrderDetailByOrder(Order order,
			int langID);

	public abstract void initListingid();

	public abstract List<TopBrowseAndSaleCount> getTopSaleByTimeRange(
			Integer timeRange);

	public abstract List<String> getAllMemberEmails(Date startDate,
			Date endDate, Integer pageNum, Integer pageSize);

	/**
	 * 检查订单是否已经支付(订单状态为 ：付款处理中 和 收款成功 视为已经支付)
	 * 
	 * @author lijun
	 * @param orderNum
	 * @return true : 已经支付过 false : 未支付过
	 */
	public boolean isAlreadyPaid(String orderNum);

	/**
	 * 用户未登陆情况下生成订单(订单信息中不会包括地址信息)
	 * 
	 * @author lijun
	 * @param context
	 * @return orderNum
	 */
	public String createOrder(WebContext context);

	/**
	 * 反序列化order成cartitems
	 * 
	 * @author lijun
	 * @param orderNum
	 *            订单号
	 * @return List<CartItem>
	 */
	public List<CartItem> deserializeOrder(String orderNum);

	/**
	 * 更新ship地址和邮费 这个方法只会是paypal调到主站时用户修改了邮寄地址和邮寄方式
	 * 
	 * @author lijun
	 * @return
	 */
	public boolean updateShipAddressAndShipPrice(Order order);

	/**
	 * 重新计算已有订单用新的邮寄方式的邮费
	 * 用户未登陆的情况下直接去paypal付款,待paypal返回到我们网站上待用户去选择邮寄方式的时候要重新计算邮费
	 * 
	 * @author lijun
	 * @param orderNum
	 * @param shipMethodId
	 * @param shipToCountryCode
	 *            邮寄地址国家的code
	 */
	public double getFreight(String orderNum, String shipMethodId,
			String shipToCountryCode);

	/**
	 * 生成普通订单号(V2.0)
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 * @return
	 */
	public String createGeneralOrderNumberV2(String shipToCountryCode);

	/**
	 * 生成游客订单
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 * @return
	 */
	public String createGuestOrderNumberV2(String shipToCountryCode);

	/**
	 * 生成代理订单号
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 * @return
	 */
	public String createAgentOrderNumberV2(String shipToCountryCode);

	/**
	 * 创建订单
	 * 
	 * @author lijun
	 * @param request
	 * @return
	 */
	public Order createOrderInstance(CreateOrderRequest request);

	/**
	 * 保存优惠信息
	 * 
	 * @author lijun
	 * @param order
	 * @param prefers
	 * @return
	 */
	public boolean saveOrderExtras(Order order, List<LoyaltyPrefer> prefers);

	/**
	 * 创建subtotal订单
	 * 
	 * @param request
	 * @return
	 */
	public Order createOrderInstanceForSubtotal(CreateOrderRequest request);

	/**
	 * 后台订单统计功能
	 */
	public List<OrderReportForm> getOrderReport(Date startdate, Date enddate,
			String type, int iwebsiteid, String cvhost);

	/**
	 * 生成订单号
	 */
	public String generateOrderNum();

	/**
	 * 根据站点查询host
	 */
	public List<String> getHostBySite(int site);
}