package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.WebsiteService;
import services.interaction.superdeal.SuperDealService;
import services.product.CategoryEnquiryService;
import session.ISessionService;
import valueobjects.base.Page;
import valueobjects.interaction.SuperDealContext;
import valueobjects.product.ProductPartInformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.Website;
import dto.product.CategoryName;
import forms.SuperDealForm;

public class SuperDeal extends Controller {

	@Inject
	SuperDealService superDealService;

	@Inject
	FoundationService foundationService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	ISessionService sessionService;

	@Inject
	WebsiteService websiteService;

	final static int NOT_ERROR = 0;

	final static int REQUIRED_ERROR = 1;

	final static int SERVER_ERROR = 2;

	@controllers.AdminRole(menuName = "SuperDeal")
	public Result getSuperDeals(Integer page, Integer perPage,
			Integer rootCategoryId, String sku) {
		Integer siteId = foundationService.getSiteID();
		Integer languageId = foundationService.getLanguage();
		Integer total = superDealService.getSuperDealCount(rootCategoryId, sku,
				siteId);
		List<dto.interaction.SuperDeal> superDeals = superDealService
				.getSuperDealByPageAndRootCategoryId(page, perPage,
						rootCategoryId, sku, siteId);
		List<Integer> rootIds = categoryEnquiryService
				.getAllRootCategoryIdBySite(siteId);
		Map<Integer, String> rootCategoryNameMap = Maps.newHashMap();
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> websiteMap = websites.stream().collect(
				Collectors.toMap(a -> a.getIid(), a -> a.getCurl()));
		if (null != rootIds && rootIds.size() > 0) {
			for (Integer rootId : rootIds) {
				CategoryName categoryName = categoryEnquiryService
						.getCategoryNameByCategoryIdAndLanguage(rootId,
								languageId);
				if (null != categoryName) {
					rootCategoryNameMap.put(rootId, categoryName.getCname());
				}
			}
		}
		SuperDealContext context = superDealService.getSuperDealContext(siteId,
				languageId);
		Page<dto.interaction.SuperDeal> superDealPage = new Page<dto.interaction.SuperDeal>(
				superDeals, total, page, perPage);

		return ok(views.html.manager.superdeal.manager_super_deal.render(
				superDealPage, rootCategoryNameMap, rootCategoryId, context,
				sku, websiteMap));
	}

	public Result addSuperDeals() {
		Form<SuperDealForm> superDealForm = Form.form(SuperDealForm.class)
				.bindFromRequest();
		SuperDealForm eForm = superDealForm.get();
		dto.interaction.SuperDeal superDeal = new dto.interaction.SuperDeal();
		BeanUtils.copyProperties(eForm, superDeal);
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		superDeal.setCcreateuser(user.getCcreateuser());
		superDeal.setBshow(true);
		Boolean result = superDealService.addNewSuperDeal(superDeal);
		Integer lastPage = (eForm.getTotalPage() + 1) / eForm.getPageSize();
		if (lastPage < 1) {
			lastPage = 1;
		}
		if (result) {
			return redirect(controllers.manager.routes.SuperDeal.getSuperDeals(
					lastPage, eForm.getPageSize(), -1, superDeal.getCsku()));
		}
		return badRequest();
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result deleteSuperDeals() {
		JsonNode jsonNode = request().body().asJson();
		Integer id = jsonNode.get("id").asInt();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dto.interaction.SuperDeal superDeal = superDealService
				.getSuperDealById(id);
		if (null != superDeal) {
			boolean result = superDealService.deleteSuperDealByIid(id);
			if (result) {
				resultMap.put("errorCode", NOT_ERROR);
				return ok(Json.toJson(resultMap));
			}
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result getSuperDealSkuInformation() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer languageId = foundationService.getLanguage();
		Integer status = 1;
		Boolean bvisible = true;
		Boolean bactivity = false;
		JsonNode jsonNode = request().body().asJson();
		String sku = jsonNode.get("sku").asText();
		Integer siteId = jsonNode.get("websiteId").asInt();
		ProductPartInformation skuInformation = superDealService
				.getProductPartlInfoBySku(sku, siteId, status, bvisible,
						languageId, bactivity);
		if (null != skuInformation) {
			resultMap.put("errorCode", NOT_ERROR);
			resultMap.put("returnData", Json.toJson(skuInformation));
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result changeSuperDealSearchCondition() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonNode jsonNode = request().body().asJson();
		double priceLower = jsonNode.get("SuperDealPriceRangeLower").asDouble();
		double priceHiger = jsonNode.get("SuperDealPriceRangeHigher")
				.asDouble();
		double discountLower = jsonNode.get("SuperDealDiscountRangeLower")
				.asDouble();
		double discountHiger = jsonNode.get("SuperDealDiscountRangeHigher")
				.asDouble();
		Integer browseTimeRange = jsonNode.get("SuperDealBrowseTimeRange")
				.asInt();
		Integer browseLimit = jsonNode.get("SuperDealBrowseLimitPerLine")
				.asInt();
		Integer saleTimeRange = jsonNode.get("SuperDealSaleTimeRange").asInt();
		Integer saleLimit = jsonNode.get("SuperDealSaleLimitPerLine").asInt();

		List<Double> priceRange = Lists.newArrayList(priceLower, priceHiger);
		List<Double> discountRange = Lists.newArrayList(discountLower,
				discountHiger);

		SuperDealContext context = new SuperDealContext(
				foundationService.getSiteID(), foundationService.getLanguage(),
				browseLimit, saleLimit, browseTimeRange, saleTimeRange,
				priceRange, discountRange);

		Boolean result = superDealService
				.changeSuperDealSearchCondition(context);
		if (result) {
			resultMap.put("errorCode", NOT_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result superdealInit() {
		try {
			superDealService.handleSuperDeal(1, 1);
			return ok("init super deal data ok !");
		} catch (Exception e) {
			Logger.error("init supperdeal error ", e);
			return badRequest("init supperdeal error ");
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result updateSuperDealBshow() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonNode jsonNode = request().body().asJson();
		String bshowString = jsonNode.get("bshow").asText();
		Integer id = jsonNode.get("id").asInt();
		Boolean bshow = false;
		if (null != bshowString) {
			if ("yes".equals(bshowString)) {
				bshow = true;
			}
		}
		Boolean result = superDealService.updateSuperDealBshow(id, bshow);
		if (result) {
			resultMap.put("errorCode", NOT_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}
}
