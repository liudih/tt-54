package controllers.manager.coupon;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.loyalty.coupon.CouponRuleService;
import services.loyalty.coupon.CouponSkuService;
import services.manager.AdminUserService;
import valueobjects.base.Page;
import valueobjects.manager.CouponSkuObject;

import com.google.api.client.util.Maps;

import controllers.InterceptActon;
import dao.product.IProductBaseEnquiryDao;
import dto.product.ProductBase;
import entity.loyalty.CouponRule;
import entity.loyalty.CouponSku;

/**
 * coupon 和 sku 关联表
 * 
 * @author liuxin
 *
 */
@With(InterceptActon.class)
public class CouponSkuRelation extends Controller {

	@Inject
	private CouponRuleService couponRuleService;

	@Inject
	private CouponSkuService couponSkuService;

	@Inject
	IProductBaseEnquiryDao baseenquiryDao;

	public Result getAll(int p,String sku) {
		// 获取优惠券的所有信息
		List<CouponRule> rulesList = couponRuleService.getCouponRulesList();
		CouponSku cSku = new CouponSku();
		if(null!=sku&&!sku.equals("")){
			cSku.setCsku(sku);
		}
		Page<CouponSku> skuPage = couponSkuService.getAll(p, 10, cSku);
		Map<Integer, String> ruleMap = Maps.newHashMap();
		if (null != skuPage.getList() && skuPage.getList().size() > 0) {
			for (CouponSku cc : skuPage.getList()) {
				String ruName = couponRuleService.getRuleNameById(cc
						.getIruleid());
				if (!ruName.equals("")) {
					ruleMap.put(cc.getIid(), ruName);
				}
			}
		}
		return ok(views.html.manager.coupon.coupon_sku_relation.render(
				rulesList, skuPage, ruleMap,sku));
	}

	public Result addRelation() {
		Form<CouponSkuObject> cForm = Form.form(CouponSkuObject.class)
				.bindFromRequest();
		if (cForm.hasErrors()) {
			return redirect("");
		}
		CouponSkuObject skuObject = cForm.get();
		CouponSku couponSku = new CouponSku();
		couponSku.setCsku(skuObject.getCsku());
		couponSku.setIruleid(skuObject.getIruleid());
		couponSku.setIsEnabled(skuObject.getIsEnabled());
		couponSku.setCcreateuser(AdminUserService.getInstance()
				.getCuerrentUser().getCusername());
		couponSkuService.addRelation(couponSku);
		return redirect(routes.CouponSkuRelation.getAll(1,null));
	}

	public Result updateStatus(Integer id, Boolean status) {
		Boolean re = false;
		String user = AdminUserService.getInstance().getCuerrentUser()
				.getCusername();
		Date date = new Date();
		int result = couponSkuService.updateStatus(id, status, user, date);
		if (result > 0) {
			re = true;
		}
		return ok(Json.toJson(re));
	}

	public Result delRelation(Integer id) {
		Boolean result = false;
		int count = couponSkuService.deleteById(id);
		if (count > 0) {
			result = true;
		}
		return ok(Json.toJson(result));
	}

	public Result checkSku(String sku) {
		boolean result = false;
		ProductBase listing = baseenquiryDao.getProductByCskuAndIsActivity(sku,
				1);
		if (null == listing) {
			result = true;
		}
		return ok(Json.toJson(result));
	}

	public Result checkStatus(String sku) {
		CouponSku couponSku = couponSkuService.getBySku(sku);
		boolean result = false;
		if (null != couponSku) {
			result = true;
		}
		return ok(Json.toJson(result));
	}
}
