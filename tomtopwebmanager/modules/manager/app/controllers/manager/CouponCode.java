package controllers.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dto.order.Order;
import entity.loyalty.CartCoupon;
import entity.loyalty.CouponRule;
import entity.loyalty.OrderCoupon;
import forms.loyalty.CouponCodeForm;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.utils.ExcelUtils;
import services.base.utils.StringUtils;
import services.loyalty.coupon.CouponCodeService;
import services.loyalty.coupon.CouponRuleService;
import services.loyalty.coupon.impl.CartCouponService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;

/**
 * 优惠券统计报表
 * 
 * @author guozy
 *
 */
public class CouponCode extends Controller {

	@Inject
	private CouponRuleService couponRuleService;

	@Inject
	private CartCouponService cartCouponService;

	@Inject
	private CouponCodeService couponCodeService;

	@Inject
	private IOrderStatusService iOrderStatusService;

	@Inject
	private IOrderEnquiryService iOIrderEnquiryService;

	/**
	 * 优惠券信息入口
	 * 
	 * @return
	 */
	@controllers.AdminRole(menuName = "Coupon")
	public Result getList(int p) {
		CouponCodeForm CouponCodeForm = new CouponCodeForm();
		CouponCodeForm.setPageNum(p);
		return ok(getCouponCodes(CouponCodeForm));
	}

	/**
	 * 查找数据信息
	 * 
	 * @return
	 */
	public Result search() {
		// 创建一个新的CouponMember实例，用来接受HTTP数据
		Form<CouponCodeForm> coupmemberForm = Form.form(CouponCodeForm.class)
				.bindFromRequest();
		return ok(getCouponCodes(coupmemberForm.get()));
	};

	/**
	 * 查询所有的数据信息
	 * 
	 * @param CouponCodes
	 * @return
	 */
	public Html getCouponCodes(CouponCodeForm couponCodeForm) {
		String couponCodeName = couponCodeForm.getCcode();
		couponCodeForm.setCcode(couponCodeName == null
				|| couponCodeName.equals("") ? null : couponCodeName);

		// 获取优惠券的所有信息
		List<CouponRule> rulesList = couponRuleService.getCouponRulesList();

		// 获取优惠券的所有信息，实现分页功能
		List<entity.loyalty.CouponCode> couponCodes = couponCodeService
				.getCouponCodes(couponCodeForm);
		// 获取CouponCode的数量
		Integer count = couponCodeService.getCount(couponCodeForm);

		Map<Integer, CartCoupon> couponCodeMap = new HashMap<Integer, CartCoupon>();
		Map<Integer, CouponRule> couponRuleMap = new HashMap<Integer, CouponRule>();
		Map<Integer, String> statusMap = new HashMap<Integer, String>();
		for (entity.loyalty.CouponCode code : couponCodes) {
			CartCoupon cart = cartCouponService.getCartCouponByCode(code
					.getCcode());
			couponCodeMap.put(code.getIid(), cart);
			CouponRule couponRule = couponRuleService.get(code
					.getIcouponruleid());
			couponRuleMap.put(code.getIid(), couponRule);
			String istatusName = "";
			if (cart != null) {
				Order order = iOIrderEnquiryService.getOrderById(cart
						.getOrderNumber());
				if (order != null) {
					istatusName = iOrderStatusService
							.getOrderStatusNameById(order.getIstatus());
				}
			}
			statusMap.put(code.getIid(), istatusName);
		}

		// 获取CouponCode页面数量
		Integer pageTotal = count / couponCodeForm.getPageSize()
				+ ((count % couponCodeForm.getPageSize() > 0) ? 1 : 0);

		return views.html.manager.coupon.coupon_statistics.render(
				couponCodeMap, couponRuleMap, statusMap, rulesList,
				couponCodeForm, couponCodes, count, pageTotal,
				couponCodeForm.getPageNum());
	};

	/**
	 * 导出CouponCodes的所有数据信息
	 * 
	 * @return
	 */
	public Result downloadCouponCodes(String ruleId, String code) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CouponCodeForm couponCodeForm = new CouponCodeForm();

		if (StringUtils.notEmpty(ruleId)) {
			couponCodeForm.setIcouponruleid(Integer.parseInt(ruleId));
		}
		if (StringUtils.notEmpty(code)) {
			couponCodeForm.setCcode(code);
		}
		// 获取优惠券的所有信息，实现分页功能
		List<entity.loyalty.CouponCode> couponCodes = couponCodeService
				.getCouponCodes(couponCodeForm);

		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> title = new ArrayList<Object>();
		title.add("Coupon Code");
		title.add("Coupon Rule");
		title.add("Coupon Money");
		title.add("Cart Number");
		title.add("Order Number");
		title.add("Order Status");
		title.add("Order Date");
		data.add(title);

		for (entity.loyalty.CouponCode couponCode : couponCodes) {
			ArrayList<Object> row = new ArrayList<Object>();

			CouponRule couponRule = couponRuleService.get(couponCode
					.getIcouponruleid());
			row.add(couponCode.getCcode());
			if (couponRule != null) {
				row.add(couponRule.getCname());
				row.add(couponRule.getFcouponamount());
			} else {
				row.add("");
				row.add("");
			}
			CartCoupon cart = cartCouponService.getCartCouponByCode(couponCode
					.getCcode());
			if (cart != null) {
				Order order = iOIrderEnquiryService.getOrderById(cart
						.getOrderNumber());
				String istatusName = null;
				if (order != null) {
					istatusName = iOrderStatusService
							.getOrderStatusNameById(order.getIstatus());
				}
				row.add(cart.getCcartid());
				row.add(cart.getOrderNumber());
				if (istatusName != null) {
					row.add(istatusName);
				} else {
					row.add("");
				}
				row.add(cart.getDusedate());
			} else {
				row.add("");
				row.add("");
				row.add("");
				row.add("");
			}

			data.add(row);
		}

		String filename = "CouponCode-list-" + sdf.format(new Date()) + ".xlsx";
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	};
}
