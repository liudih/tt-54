package services.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import valueobjects.order_api.DropShipOrderMessage;
import valueobjects.order_api.OrderCommission;
import valueobjects.order_api.OrderValue;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.OrderIdStatusId;
import dto.order.OrderWithDtail;
import forms.order.MemberOrderForm;
import forms.order.OrderForm;
import forms.order.OrderTransactionForm;

public interface IOrderEnquiryService {

	public abstract Order getOrderById(Integer id);

	public abstract Order getOrderById(String orderNumber);

	public abstract Integer getOrderDetailSumByOrderId(Integer id);

	// need recode
	public abstract List<OrderDetail> getOrderDetails(Integer orderId);

	public abstract List<OrderDetail> getOrderDetails(String orderId);

	public abstract Integer countByEmail(String email, Integer siteId);

	public abstract Integer countByEmailAndStatus(String email, String status,
			Integer siteId, Integer isShow, boolean isNormal);

	public abstract Integer countByEmailAndStatusAndTag(String email,
			String status, Integer siteId, Integer isShow, String tag);

	public abstract List<Order> searchOrders(String email,
			MemberOrderForm form, Integer siteId, boolean isNormal);

	public abstract List<Order> searchOrdersByTag(String email,
			MemberOrderForm form, Integer siteId, String tag);

	public abstract Integer searchOrderCount(String email,
			MemberOrderForm form, Integer siteId, boolean isNormal);

	public abstract Integer searchOrderCountByTag(String email,
			MemberOrderForm form, Integer siteId, String tag);

	public abstract List<Order> searchOrders(OrderForm orderForm, Date start,
			Date end, Date paymentStart, Date paymentEnd);

	public abstract Integer searchOrderCount(MemberOrderForm form, Date start,
			Date end);

	public abstract Collection<com.website.dto.order.Order> getOrders(
			MemberOrderForm form, Date start, Date end);

	public abstract com.website.dto.order.Order getOrder(Order data);

	public abstract com.website.dto.order.Order getOrderByOrderId(
			Integer orderId);

	public abstract Integer getOrderCountByDateAndStatus(Date start, Date end,
			ArrayList<Integer> status);

	public abstract List<Order> getOrderBySearchFrom(OrderForm form);

	public abstract Integer getOrderStatusByOrderId(Integer orderId);

	public abstract Integer getOrderIdByOrderNumber(String orderNumber);

	public abstract OrderIdStatusId getOrderIdStatusByOrderNumber(
			String orderNumber);

	public abstract List<Order> getOrderForCommission(String aid, int status);

	public abstract List<OrderCommission> getOrderCommissions(
			List<Integer> ids, String searchname);

	public abstract List<Order> getOrderByAidAndDate(String startdate,
			String enddate, Integer website);

	public abstract List<Order> getOrdersByDateRange(String origin,
			Date begindate, Date enddate);

	public abstract List<OrderWithDtail> getOrdersWithDetail(
			Map<String, Object> queryParamMap);

	public abstract List<OrderWithDtail> getOrdersWithDetailByIds(
			List<Integer> ids, int page, int pageSize);

	public abstract List<OrderWithDtail> getOrdersWithDetailByLimit(
			Map<String, Object> paramMap);

	public abstract int getOrdersWithDetailCount(Map<String, Object> paramMap);

	public abstract Double getMemberGrandtotal(String email);

	public abstract List<DropShipOrderMessage> searchDropShipOrders(
			OrderForm form);

	public abstract Integer searchDropShipOrderCount(OrderForm form);

	public abstract List<Order> searchOrdersByLabel(OrderForm orderForm,
			String label);

	public abstract Integer searchCountByLabel(OrderForm orderForm, String label);

	public abstract List<Order> searchOrdersByLabel(OrderForm form, Date start,
			Date end, Date paymentStart, Date paymentEnd, String label);

	public abstract Integer searchCountByLabel(OrderForm form, Date start,
			Date end, Date paymentStart, Date paymentEnd, String label);

	public abstract double getOrginOtal(int id);

	public abstract OrderValue getOrderValue(String orderID);

	public abstract OrderValue getOrderValue(Integer orderID);

	public abstract List<DropShipOrderMessage> searchDropShipOrderList(
			OrderForm orderForm);

	public abstract List<dto.order.Order> searchOrderListByLabel(
			OrderForm orderForm, String label);

	public abstract Integer getOrderTotalCount(OrderForm orderForm, Date start,
			Date end, Date paymentStart, Date paymentEnd);

	public List<Order> searchOrdersTransaction(OrderTransactionForm form,
			Date start, Date end);

	public Integer getOrdersTransactionTotalCount(OrderTransactionForm form,
			Date start, Date end);

	public Integer updateOrderTransactionStatus(Integer iid,
			String transactionid);

	public List<Order> searchOrdersExcludeUser(OrderForm form, Date start,
			Date end, Date paymentStart, Date paymentEnd, List<String> excUser);

	/**
	 * 
	 * @Title: getOrderBySearchFromExcludeUser
	 * @Description: TODO(查询需要导出的订单列表(排除测试用户))
	 * @param @param form
	 * @param @param excUser
	 * @param @return
	 * @return List<Order>
	 * @throws
	 * @author yinfei
	 */
	public abstract List<Order> getOrderBySearchFromExcludeUser(OrderForm form,
			List<String> excUser);

	/**
	 * 
	 * @Title: getOrderByMemberAndPayDate
	 * @Description: TODO(通过用户和支付开始时间查询结算金额大于指定金额的订单)
	 * @param @param memberID
	 * @param @param website
	 * @param @param beginTime
	 * @param @param money
	 * @param @return
	 * @return List<Order>
	 * @throws
	 * @author yinfei
	 */
	public abstract List<Order> getOrderByMemberAndPayDate(String memberID,
			int website, Date beginTime, Double money);
}