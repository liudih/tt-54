package controllers.manager.coupon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.base.CurrencyService;
import services.loyalty.coupon.CouponRuleService;
import services.loyalty.coupon.CouponTypeService;
import services.product.CategoryEnquiryService;
import services.product.IProductLabelTypeService;
import valueobjects.base.Page;
import valueobjects.product.category.CategoryComposite;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import controllers.InterceptActon;
import dto.Currency;
import entity.loyalty.CouponRule;
import entity.loyalty.CouponRuleProductFilter;
import entity.loyalty.CouponType;
import dto.product.ProductLabelType;
import enums.loyalty.coupon.manager.CouponRuleSelect;
import forms.loyalty.CouponRuleForm;
import enums.loyalty.coupon.manager.CouponRuleBack;

/**
 * coupon code使用规则，给后台管理调用
 * 
 * @author xiaoch
 *
 */
@With(InterceptActon.class)
public class Rule extends Controller {

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	CouponRuleService couponRuleService;
	// 获取币种
	@Inject
	CurrencyService currencyService;

	@Inject
	CouponTypeService couponTypeService;

	@Inject
	IProductLabelTypeService plts;

	public Result getAll(int page, int pageSize) {

		Form<CouponRuleForm> ruleForm = Form.form(CouponRuleForm.class)
				.bindFromRequest();
		if (ruleForm.hasErrors()) {
			return ok(views.html.manager.affiliate.error.render("error"));
		}
		CouponRuleForm condition = ruleForm.get();

		Page<CouponRule> list = couponRuleService.getAll(page, pageSize,
				condition);
		Page<CouponRuleForm> result = list.map(self -> {
			CouponRuleForm form = new CouponRuleForm();
			if (null != self) {
				BeanUtils.copyProperties(self, form);
				if (!StringUtils.isEmpty(self.getCcurrency())) {
					form.setCurrencyName(currencyService.getCurrencyById(
							self.getCcurrency()).getCcode());
				}
				form.setTypeName(couponTypeService.getTypeNameById(self
						.getItype()));
			}
			return form;
		});
		// 获取币种
		List<Currency> currencies = currencyService.getAllCurrencies();
		// 获取购物券类型
		List<CouponType> couponTypes = couponTypeService.getAll();
		// 获取日期类型
		CouponRuleSelect.TimeType[] timeTypes = CouponRuleSelect.TimeType
				.values();
		List<CouponRuleSelect.TimeType> timeTypeList = Arrays.asList(timeTypes);
		// 获取产品类型
		List<ProductLabelType> productTypesList = plts.selectAll();

		return ok(views.html.manager.coupon.couponrule_index.render(result,
				currencies, couponTypes, timeTypeList, productTypesList));
	}

	public Result add() {
		play.data.Form<CouponRuleForm> sourceForm = Form.form(
				CouponRuleForm.class).bindFromRequest();

		if (sourceForm.hasErrors()) {
			return ok(views.html.manager.affiliate.error.render("error"));
		}
		boolean flag = false;
		// 新增数据前先检查数据库是否有重名的规则
		CouponRuleForm form = sourceForm.get();
		String name = form.getCname();

		boolean isExisted = this.couponRuleService.isExisted(name);
		if (!isExisted) {
			Map<String, String[]> map = request().body().asFormUrlEncoded();
			// 获取站点id
			String[] checkedSource = map.get("cwebsiteid");
			String loginTerm = "";
			if (null != checkedSource && checkedSource.length > 0) {
				for (int i = 0; i < checkedSource.length; i++) {
					if (i == 0) {
						loginTerm = checkedSource[i];
					} else {
						loginTerm += "," + checkedSource[i];
					}
				}
			}
			// 获取允许使用的终端类型
			String[] checkedUseTerminal = map.get("cuseterminal");
			String useTerminal = "";
			if (!StringUtils.isEmpty(checkedUseTerminal)) {
				for (int i = 0; i < checkedUseTerminal.length; i++) {
					if (i == 0) {
						useTerminal = checkedUseTerminal[i];
					} else {
						useTerminal += "," + checkedUseTerminal[i];
					}
				}
			}

			CouponRuleForm ruleForm = sourceForm.get();
			CouponRule rule = new CouponRule();
			if (null != ruleForm) {
				BeanUtils.copyProperties(ruleForm, rule);
			}
			// 获取上边经过处理的前台数据
			rule.setCwebsiteid(loginTerm);
			rule.setCuseterminal(useTerminal);
			flag = couponRuleService.add(rule);
		} else {
			Logger.debug("数据库已经存在{}名称的规则,不能重复添加同名的规则", name);
		}

		if (flag) {
			return redirect(controllers.manager.coupon.routes.Rule
					.getAll(1, 15));
		}
		return ok(views.html.manager.error.render("error"));
	}

	public Result get(int id) {
		CouponRule couponRule = couponRuleService.get(id);
		CouponRuleForm form = new CouponRuleForm();
		if (null != couponRule) {
			BeanUtils.copyProperties(couponRule, form);
		}
		form.setCurrencyName(currencyService.getCurrencyById(
				couponRule.getCcurrency()).getCcode());
		form.setTypeName(couponTypeService.getTypeNameById(couponRule
				.getItype()));
		// 获取币种
		List<Currency> currencies = currencyService.getAllCurrencies();
		// 获取购物券类型
		List<CouponType> couponTypes = couponTypeService.getAll();
		// 获取产品类型
		List<ProductLabelType> productTypesList = plts.selectAll();
		// 获取日期类型
		CouponRuleSelect.TimeType[] timeTypes = CouponRuleSelect.TimeType
				.values();
		List<CouponRuleSelect.TimeType> timeTypeList = Arrays.asList(timeTypes);

		// 获取站点选中项
		List<Integer> checks = null;
		if (!org.apache.commons.lang3.StringUtils.isEmpty(couponRule
				.getCwebsiteid())) {
			String[] loginTerCheck = org.apache.commons.lang3.StringUtils
					.split(couponRule.getCwebsiteid(), ",");
			checks = new ArrayList<>(loginTerCheck.length);
			for (int i = 0; i < loginTerCheck.length; i++) {
				checks.add(Integer.parseInt(loginTerCheck[i]));
			}
		}
		// 获取产品label
		List<String> producttypeCheck = null;
		if (!org.apache.commons.lang3.StringUtils.isEmpty(couponRule
				.getCproducttype())) {
			String[] productCheck = org.apache.commons.lang3.StringUtils.split(
					couponRule.getCproducttype(), ",");
			producttypeCheck = Arrays.asList(productCheck);
		}
		// 获取允许使用的终端类型
		List<String> useTerminalCheck = null;
		if (!org.apache.commons.lang3.StringUtils.isEmpty(couponRule
				.getCuseterminal())) {
			String[] terminalCheck = org.apache.commons.lang3.StringUtils
					.split(couponRule.getCuseterminal(), ",");
			useTerminalCheck = Arrays.asList(terminalCheck);
		}
		return ok(views.html.manager.coupon.couponrule_edit.render(form,
				currencies, couponTypes, checks, timeTypeList,
				productTypesList, producttypeCheck, useTerminalCheck));
	}

	public Result del(int id) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		Integer[] ids = { id };
		boolean flag = couponRuleService.del(ids);
		if (flag) {
			mjson.put("result", "success");
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	public Result edit() {
		play.data.Form<CouponRuleForm> sourceForm = Form.form(
				CouponRuleForm.class).bindFromRequest();
		if (sourceForm.hasErrors()) {
			return ok(views.html.manager.error
					.render("Form verification failed"));
		}
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		// 获取站点id
		String[] checkedSource = map.get("cwebsiteid");
		String loginTerm = "";
		if (null != checkedSource && checkedSource.length > 0) {
			for (int i = 0; i < checkedSource.length; i++) {
				if (i == 0) {
					loginTerm = checkedSource[i];
				} else {
					loginTerm += "," + checkedSource[i];
				}
			}
		}
		// 获取允许使用的终端类型
		String[] checkedUseTerminal = map.get("cuseterminal");
		String useTerminal = "";
		if (!StringUtils.isEmpty(checkedUseTerminal)) {
			for (int i = 0; i < checkedUseTerminal.length; i++) {
				if (i == 0) {
					useTerminal = checkedUseTerminal[i];
				} else {
					useTerminal += "," + checkedUseTerminal[i];
				}
			}
		}
		CouponRuleForm couponRuleForm = sourceForm.get();
		CouponRule couponRule = new CouponRule();
		if (null != couponRuleForm) {
			BeanUtils.copyProperties(couponRuleForm, couponRule);
		}
		couponRule.setCwebsiteid(loginTerm);
		couponRule.setCuseterminal(useTerminal);
		boolean flag;
		// 如果该rule的状态为on时则这次修改相当于新增一条rule,然后把原来的rule设置成delete
		int ruleId = couponRule.getIid();
		CouponRule original = couponRuleService.getCouponRuleByRuleId(ruleId);
		int state = original.getIstatus();
		if (CouponRuleBack.Status.ON.getStatusid() == state) {
			couponRuleService.add(couponRule);
			couponRuleService.ChangeStatusDelete(original.getIid());
			flag = true;
		} else {
			flag = couponRuleService.edit(couponRule);
		}

		if (flag) {
			return redirect(controllers.manager.coupon.routes.Rule
					.getAll(1, 15));
		} else {
			return ok(views.html.manager.error.render("edit error"));
		}
	}

	/**
	 * 获取所有商品品类
	 * 
	 * @return
	 */
	public Result getProductCategory(int id) {
		CouponRule couponRule = couponRuleService.get(id);
		int siteid = 1;
		String sites = couponRule.getCwebsiteid();
		JSONObject result = new JSONObject();
		if(!StringUtils.isEmpty(sites)){
			if(sites.indexOf(",") == -1){
				siteid = Integer.parseInt(sites);
			}else{
				String[] strs = sites.split(",");
				siteid = Integer.parseInt(strs[0]);
			}
			Logger.debug("website : " + siteid);
			int languageid = 1;
			List<CategoryComposite> allCategory = categoryEnquiryService
					.getAllSimpleCategories(languageid, siteid);
			Logger.debug("allCategory size : " + allCategory.size());
			JSONArray jsonArray = getTree(allCategory);
			result.put("result", "success");
			result.put("data", jsonArray);
		}else{
			result.put("result", "fail");
			result.put("data", null);
		}
		return ok(result.toString()).as("application/json");
	}

	/**
	 * 对商品的所有品类数据进行转换，使满足前台树的展现
	 * 
	 * @param list
	 * @return
	 */
	private JSONArray getTree(List<CategoryComposite> list) {
		JSONArray result = new JSONArray();
		for (CategoryComposite node : list) {
			JSONObject json = new JSONObject();
			json.put("id", node.getSelf().getIcategoryid());
			json.put("name", node.getSelf().getCname());
			if (node.getChildren() != null && node.getChildren().size() > 0) {
				JSONArray children = getTree(node.getChildren());
				json.put("children", children);
			}
			result.add(json);
		}
		return result;
	}

	/**
	 * 把品类过滤树的选中的品类保存
	 * 
	 * @param ids
	 * @param ruleId
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result addTreeCheck() {
		
		JsonNode json = request().body().asJson();
		String ids = json.get("ids").asText();
		Integer ruleId = json.get("ruleid").asInt();
		
		boolean flag = false;
		couponRuleService.delTreeCheckByRuleId(ruleId);
		String[] checkids = org.apache.commons.lang3.StringUtils
				.split(ids, ",");
		if (null != checkids && checkids.length > 0) {
			List<CouponRuleProductFilter> list = new ArrayList<>(
					checkids.length);
			for (int i = 0; i < checkids.length; i++) {
				CouponRuleProductFilter crp = new CouponRuleProductFilter();
				crp.setIcategoryid(Integer.parseInt(checkids[i]));
				crp.setIruleid(ruleId);
				list.add(crp);
			}
			flag = couponRuleService.addTreeCheck(list);
		}
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (flag) {
			mjson.put("result", "success");
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	/**
	 * 获取指定规则的品类过滤树的check
	 * 
	 * @param ruleId
	 *            规则id
	 * @return 品类id
	 */
	public Result getTreeCheckByRuleId(Integer ruleId) {
		List<Integer> checkIds = couponRuleService.getTreeCheckByRuleId(ruleId);
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "success");
		mjson.put("data", checkIds);
		return ok(Json.toJson(mjson));
	}

	public Result ChangeStatusOn(Integer id) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (null != id) {
			boolean flag = couponRuleService.ChangeStatusOn(id);
			if (flag) {
				mjson.put("result", "success");
			} else {
				mjson.put("result", "error");
			}
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	public Result ChangeStatusOff(Integer id) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (null != id) {
			boolean flag = couponRuleService.ChangeStatusOff(id);
			if (flag) {
				mjson.put("result", "success");
			} else {
				mjson.put("result", "error");
			}
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	/**
	 * 重新打开rule
	 * 
	 * @author lijun
	 * @param ruleId
	 * @return
	 */
	public Result reopen(int ruleId) {
		this.couponRuleService.ChangeStatusOn(ruleId);
		return redirect(controllers.manager.coupon.routes.Rule.getAll(1, 15));
	}
}
