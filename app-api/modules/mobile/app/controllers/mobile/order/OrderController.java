package controllers.mobile.order;

import interceptor.auth.LoginAuth;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.With;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.order.OrderService;
import services.order.exception.OrderException;
import utils.CommonDefn;
import utils.MsgUtils;
import utils.Page;
import utils.ValidataUtils;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BasePageJson;
import valuesobject.mobile.BaseResultType;

import com.google.common.collect.Lists;

import controllers.mobile.TokenController;
import dto.mobile.OdItem;
import dto.mobile.OrderDetailInfo;
import forms.mobile.AddOrderForm;
import forms.order.MemberOrderForm;

@With(LoginAuth.class)
public class OrderController extends TokenController {

	@Inject
	private OrderService orderService;

	@Inject
	LoginService loginService;

	@Inject
	MobileService mobileService;

	private final static String purl = "h5/payod";

	/**
	 * 获取用户订单列表(包括订单商品资料)
	 * 
	 * @param status
	 *            订单状态
	 * @param page
	 * @param pageSize
	 * 
	 */
	public Result getOrdersList(Integer status, Integer page, Integer pageSize) {
		BasePageJson<OdItem> result = new BasePageJson<OdItem>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			Integer siteId = mobileService.getWebSiteID();
			Integer langId = mobileService.getLanguageID();
			MemberOrderForm form = new MemberOrderForm();
			if (status != null && status > 0) {
				form.setStatus(status);
				form.setPageNum(page);
				form.setPageSize(pageSize);
			} else {
				form.setPageNum(page);
				form.setPageSize(pageSize);
			}
			Page<OdItem> odlist = orderService.getOrdersList(email, form,
					siteId, true, langId);
			if (null == odlist) {
				return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
						MsgUtils.msg(BaseResultType.NODATA))));
			}
			result.setList(odlist.getList());
			result.setTotal(odlist.getTotal());
			result.setP(odlist.getP());
			result.setSize(odlist.getSize());
			result.setRe(BaseResultType.SUCCESS);
			result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
		} catch (Exception e) {
			HashMap<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("OrderController getOrdersList Exception", e);
			e.printStackTrace();
			return ok(Json.toJson(objMap));
		}
		return ok(Json.toJson(result));
	}

	/**
	 * 获取用户订单详情
	 * 
	 * @param oid
	 */
	public Result getOrderDetail(String oid) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			Integer langID = mobileService.getLanguageID();
			OrderDetailInfo odtl = orderService.getOrderDetail(oid, langID);

			if (null == odtl) {
				return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
						MsgUtils.msg(BaseResultType.NODATA))));
			}
			objMap.put("odtl", odtl);
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("OrderController getOrderDetail Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));
	}

	/**
	 * 添加订单
	 * 
	 * @param cartId
	 *            购物车ID
	 * @param addressId
	 *            用户地址ID
	 * @param shippingMethodId
	 *            邮寄方式的ID
	 * @param message
	 * 
	 */
	public Result addOrder() {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		Integer siteId = mobileService.getWebSiteID();
		Integer langID = mobileService.getLanguageID();
		String currency = mobileService.getCurrency();
		String origin = mobileService.getAppName();
		String ip = mobileService.getMobileContext().getIp();
		String vhost = mobileService.getMobileContext().getHost();
		Form<AddOrderForm> form = Form.form(AddOrderForm.class)
				.bindFromRequest();
		AddOrderForm ao = form.get();
		if (null == ao) {
			return ok(Json.toJson(new BaseJson(BaseResultType.ERROR, MsgUtils
					.msg(BaseResultType.NODATA))));
		}
		if (ValidataUtils.validateLength(ao.getMsg(), 500) == false) {
			objMap.put("re",
					BaseResultType.MESSAGE_FORMATE_LENGTH_OVER_ERROR_CODE);
			objMap.put("msg",
					MsgUtils.msg(BaseResultType.MESSAGE_LENGTH_OVER_ERROR_MSG));
			return ok(Json.toJson(objMap));
		}
		if (ValidataUtils.validateNull(ao.getCartid()) == false) {
			objMap.put("re", BaseResultType.ORDER_ADD_CART_IS_NULL_ERROR_CODE);
			objMap.put("msg", MsgUtils
					.msg(BaseResultType.ORDER_ADD_CART_IS_NULL_ERROR_MSG));
			return ok(Json.toJson(objMap));
		}
		if (ao.getAddrid() == 0) {
			objMap.put("re", BaseResultType.ORDER_ADDRESS_IS_NULL_ERROR_CODE);
			objMap.put("msg", MsgUtils
					.msg(BaseResultType.ORDER_ADDRESS_IS_NULL_ERROR_MSG));
			return ok(Json.toJson(objMap));
		}
		if (ao.getShipid() == 0) {
			objMap.put("re", BaseResultType.ORDER_SHIPPING_IS_NULL_ERROR_CODE);
			objMap.put("msg", MsgUtils
					.msg(BaseResultType.ORDER_SHIPPING_IS_NULL_ERROR_MSG));
			return ok(Json.toJson(objMap));
		}
		try {
			String orderNum = orderService.addOrders(ao.getCartid(), siteId,
					ao.getAddrid(), ao.getShipid(), origin, ao.getMsg(), ip,
					langID, currency, vhost);
			if (null == orderNum || "".equals(orderNum)) {
				objMap.put("re", BaseResultType.FAILURE);
				objMap.put("msg", MsgUtils.msg(BaseResultType.OPERATE_FAIL));
				return ok(Json.toJson(objMap));
			}
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", orderNum);
			objMap.put("purl", purl + "?oid=" + orderNum);

		} catch (OrderException e) {
			objMap.put("re", BaseResultType.ADD_EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.ADDORDERERROR)
					+ " : " + e.toString());
			Logger.error("OrderController addOrder Exception", e);
		}
		return ok(Json.toJson(objMap));
	}

	/**
	 * 删除订单
	 * 
	 * @param iids
	 * @return
	 */
	public Result delteOrder(String iids) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			if (StringUtils.isNotBlank(iids)) {
				String[] orders = iids.split("\\,");
				List<Integer> idList = Lists.newArrayList();
				for (String i : orders) {
					int iid = Integer.parseInt(i);
					idList.add(iid);
				}
				boolean b = orderService.deleteOrders(idList, email);
				if (b != false) {
					objMap.put("re", BaseResultType.UPDATE_EXCEPTION);
					objMap.put("msg", MsgUtils.msg(BaseResultType.UPDATEERROR));
					return ok(Json.toJson(objMap));
				}
			}
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("OrderController delteOrder Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));
	}

	/**
	 * 移除订单
	 * 
	 * @param iids
	 * @return
	 */
	public Result removeOrder(String iids) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			if (StringUtils.isNotBlank(iids)) {
				String[] orders = iids.split("\\,");
				List<Integer> idList = Lists.newArrayList();
				for (String i : orders) {
					int iid = Integer.parseInt(i);
					idList.add(iid);
				}
				boolean b = orderService.removeOrders(idList, email);
				if (b != false) {
					objMap.put("re", BaseResultType.UPDATE_EXCEPTION);
					objMap.put("msg", MsgUtils.msg(BaseResultType.UPDATEERROR));
					return ok(Json.toJson(objMap));
				}
			}
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", "");
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("OrderController removeOrder Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));

	}

	/**
	 * 还原订单
	 * 
	 * @param iids
	 * @return
	 */
	public Result restoreOrder(String iids) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			if (StringUtils.isNotBlank(iids)) {
				String[] orders = iids.split("\\,");
				List<Integer> idList = Lists.newArrayList();
				for (String i : orders) {
					int iid = Integer.parseInt(i);
					idList.add(iid);
				}
				boolean b = orderService.restoreOrders(idList, email);
				if (b != false) {
					objMap.put("re", BaseResultType.UPDATE_EXCEPTION);
					objMap.put("msg", MsgUtils.msg(BaseResultType.UPDATEERROR));
					return ok(Json.toJson(objMap));
				}
			}
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", "");
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("OrderController restoreOrder Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));
	}

	/**
	 * 确认订单
	 * 
	 * @param oiid
	 * @return
	 */
	public Result comfirOrder(Integer oiid) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			if (oiid == null) {
				objMap.put("re", BaseResultType.ERROR);
				objMap.put("msg", "oiid is null");
				return ok(Json.toJson(objMap));
			}
			objMap = orderService.completedOrder(oiid, email);
			Integer re = (Integer) objMap.get("re");
			if (re == BaseResultType.UPDATE_ORDER_STATUS_NOT_DISPATCHED_ERROR_CODE) {
				objMap.put(
						"re",
						BaseResultType.UPDATE_ORDER_STATUS_NOT_DISPATCHED_ERROR_CODE);
				objMap.put(
						"msg",
						MsgUtils.msg(BaseResultType.UPDATE_ORDER_STATUS_NOT_DISPATCHED_ERROR_MSG));
				return ok(Json.toJson(objMap));
			}
			if (re == BaseResultType.COMFIR_ORDER_ERROR_CODE) {
				objMap.put("re", BaseResultType.COMFIR_ORDER_ERROR_CODE);
				objMap.put("msg",
						MsgUtils.msg(BaseResultType.COMFIR_ORDER_ERROR_MSG));
				return ok(Json.toJson(objMap));
			}
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("OrderController comfirOrder Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));

	}
}
