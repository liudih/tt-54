package controllers.wholesale;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.CountryService;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.base.utils.StringUtils;
import services.member.address.AddressService;
import services.member.login.ILoginService;
import services.order.OrderTaggingService;
import services.order.ProductToOrderService;
import services.product.CategoryEnquiryService;
import services.product.ProductEnquiryService;
import services.wholesale.WholeSaleBaseEnquiryService;
import services.wholesale.WholeSaleBaseUpdateService;
import services.wholesale.WholeSaleCategoryEnquiryService;
import services.wholesale.WholeSaleCategoryUpdateService;
import services.wholesale.WholeSaleOrderProductUpdateService;
import services.wholesale.WholeSaleProductEnquiryService;
import services.wholesale.WholeSaleProductUpdateService;
import valueobjects.order_api.SaveOrderRequest;
import authenticators.member.MemberLoginAuthenticator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dao.wholesale.IWholeSaleDiscountLevelEnquiryDao;
import dto.Country;
import dto.member.MemberAddress;
import dto.order.Order;
import entity.wholesale.WholeSaleBase;
import entity.wholesale.WholeSaleCategory;
import entity.wholesale.WholeSaleDiscountLevel;
import entity.wholesale.WholeSaleProduct;
import extensions.order.IOrderSourceProvider;
import form.wholesale.WholeSaleBaseForm;
import form.wholesale.WholesaleOrderForm;

public class WholeSale extends Controller {

	@Inject
	private ILoginService loginService;
	@Inject
	private WholeSaleBaseEnquiryService wholeSaleBaseEnquiryService;
	@Inject
	private FoundationService foundationService;
	@Inject
	private WholeSaleBaseUpdateService wholeSaleBaseUpdateService;
	@Inject
	private WholeSaleCategoryUpdateService wholeSaleCategoryUpdateService;
	@Inject
	private WholeSaleCategoryEnquiryService wholeSaleCategoryEnquiryService;
	@Inject
	private CategoryEnquiryService categoryEnquiryService;
	@Inject
	private SystemParameterService parameterService;
	@Inject
	private IWholeSaleDiscountLevelEnquiryDao discountEnquiry;
	@Inject
	private CountryService countryEnquiryService;
	@Inject
	private WholeSaleProductEnquiryService productEnquiryService;
	@Inject
	private WholeSaleProductEnquiryService wholeSaleEnquiryService;
	@Inject
	private WholeSaleProductUpdateService wholeSaleUpdateService;
	@Inject
	private ProductEnquiryService productEnquiry;
	@Inject
	private ProductToOrderService productToOrder;
	@Inject
	private Set<IOrderSourceProvider> sourceProviders;
	@Inject
	private WholeSaleOrderProductUpdateService orderProductUpdate;
	@Inject
	private OrderTaggingService taggingService;
	@Inject
	private AddressService addressService;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result join() {
		String memberEmail = loginService.getLoginData().getEmail();
		int siteId = foundationService.getSiteID();
		int languageId = foundationService.getLanguage();
		WholeSaleBase wholeSaleBase = wholeSaleBaseEnquiryService
				.getWholeSaleBaseByEmail(memberEmail, siteId);
		if (null == wholeSaleBase) {
			dto.SystemParameter wholeSaleEmailParameter = parameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "WholeSaleMail");
			String wholeSaleEmail = wholeSaleEmailParameter != null ? wholeSaleEmailParameter
					.getCparametervalue() : "";
			dto.SystemParameter wholeSaleSkypeParameter = parameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(siteId,
							languageId, "WholeSaleSkype");
			String wholeSaleSkype = wholeSaleSkypeParameter != null ? wholeSaleSkypeParameter
					.getCparametervalue() : "";
			return ok(views.html.wholesale.member.wholesale_home.render(
					wholeSaleEmail, wholeSaleSkype));
		} else {
			return applyWholeSale();
		}
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result applyWholeSale() {
		String memberEmail = loginService.getLoginData().getEmail();
		int siteID = foundationService.getSiteID();
		int langId = foundationService.getLanguage();
		WholeSaleBase wholeSaleBase = wholeSaleBaseEnquiryService
				.getWholeSaleBaseByEmail(memberEmail, siteID);
		WholeSaleBaseForm wholeSaleBaseForm = new WholeSaleBaseForm();
		wholeSaleBaseForm.setCemail(memberEmail);
		Map<Integer, String> rootCategories = getRootCategories(langId, siteID);
		if (null == wholeSaleBase) {
			Country country = foundationService.getCountryObj();
			if (null != country) {
				wholeSaleBaseForm.setCcountrysn(country.getCshortname());
				wholeSaleBaseForm.setCcountryname(country.getCname());
			}
			Form<WholeSaleBaseForm> formWholesaleBase = Form.form(
					WholeSaleBaseForm.class).fill(wholeSaleBaseForm);
			return ok(views.html.wholesale.member.wholesale_join.render(
					formWholesaleBase, rootCategories, null));
		} else if (wholeSaleBase.getIstatus() == 2) {
			Country country = countryEnquiryService
					.getCountryByShortCountryName(wholeSaleBase.getCcountrysn());
			BeanUtils.copyProperties(wholeSaleBase, wholeSaleBaseForm);
			if (country != null) {
				wholeSaleBaseForm.setCcountryname(country.getCname());
			}
			List<Integer> icategroyIds = wholeSaleCategoryEnquiryService
					.getCategoryIdByWholeSaleId(wholeSaleBase.getIid());
			Form<WholeSaleBaseForm> formWholesaleBase = Form.form(
					WholeSaleBaseForm.class).fill(wholeSaleBaseForm);
			return ok(views.html.wholesale.member.wholesale_join.render(
					formWholesaleBase, rootCategories, icategroyIds));
		}

		return joinSuccess();
	}

	public Map<Integer, String> getRootCategories(Integer langId, Integer siteID) {
		List<valueobjects.product.category.CategoryComposite> rootCategorie = categoryEnquiryService
				.getRootCategories(langId, siteID);
		Map<Integer, String> collect = rootCategorie.stream().collect(
				Collectors.toMap(a -> a.getSelf().getIcategoryid(), a -> a
						.getSelf().getCname()));
		return collect;
	}

	public Result updateWholeSaleBase() {
		Form<WholeSaleBaseForm> wholeSaleBaseForm = Form.form(
				WholeSaleBaseForm.class).bindFromRequest();
		int siteID = foundationService.getSiteID();
		int langId = foundationService.getLanguage();
		if (wholeSaleBaseForm.hasErrors()) {
			Map<Integer, String> rootCategories = getRootCategories(langId,
					siteID);
			return ok(views.html.wholesale.member.wholesale_join.render(
					wholeSaleBaseForm, rootCategories, null));
		}
		WholeSaleBaseForm wholeSaleBaseForm2 = wholeSaleBaseForm.get();
		WholeSaleBase wholeSaleBase = new WholeSaleBase();
		BeanUtils.copyProperties(wholeSaleBaseForm2, wholeSaleBase);
		boolean flag = false;
		wholeSaleBase.setIstatus(0);
		wholeSaleBase.setIwebsiteid(siteID);
		wholeSaleBase.setIlanguageid(langId);
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		String[] icategroyIds = map.get("icategroyIds");
		WholeSaleBase checkWholeSaleBase = wholeSaleBaseEnquiryService
				.getWholeSaleBaseByEmail(wholeSaleBase.getCemail(), siteID);
		if (null != checkWholeSaleBase) {
			wholeSaleBase.setIid(checkWholeSaleBase.getIid());
		}
		if (null != wholeSaleBase.getIid()) {
			flag = wholeSaleBaseUpdateService
					.updateWholeSaleBase(wholeSaleBase);
		} else {
			flag = wholeSaleBaseUpdateService.addWholeSaleBase(wholeSaleBase);
		}
		if (flag) {
			wholeSaleCategoryUpdateService.deleteByWholeSaleId(wholeSaleBase
					.getIid());
			for (String categoryId : icategroyIds) {
				WholeSaleCategory wholeSaleCategory = new WholeSaleCategory();
				wholeSaleCategory.setIcategoryid(Integer.parseInt(categoryId));
				wholeSaleCategory.setIwholesaleid(wholeSaleBase.getIid());
				wholeSaleCategoryUpdateService
						.addWholeSaleCategory(wholeSaleCategory);
			}
			return redirect(controllers.wholesale.routes.WholeSale
					.joinSuccess());
		} else {
			Map<Integer, String> rootCategories = getRootCategories(langId,
					siteID);
			return ok(views.html.wholesale.member.wholesale_join.render(
					wholeSaleBaseForm, rootCategories, null));
		}
	}

	public Result joinSuccess() {
		int siteID = foundationService.getSiteID();
		List<WholeSaleDiscountLevel> discounts = discountEnquiry
				.getWholeSaleDiscountLevelByWebSiteId(siteID);
		return ok(views.html.wholesale.member.wholesale_comfirm
				.render(discounts));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result addWholesaleProduct() {
		String email = loginService.getLoginEmail();
		JsonNode json = request().body().asJson();
		String sku = json.get("csku").asText();
		String qty = json.get("qty").asText();
		Integer websiteid = foundationService.getSiteID();

		boolean isExist = wholeSaleBaseEnquiryService
				.checkWholeSaleBaseByEmail(email, websiteid);
		if (!isExist) {
			return ok("failure");
		}

		int iqty = Integer.parseInt(qty);
		if (null != sku && !"".equals(sku)) {

			int miniNumber = this.getMiniWholeNumber();
			WholeSaleProduct wholeSaleProduct = this.wholeSaleEnquiryService
					.getWholeSaleProductsByEmailAndSkuAndWebsite(email,
							websiteid, sku);
			if (null == wholeSaleProduct) {
				WholeSaleProduct wsp = new WholeSaleProduct();
				wsp.setCsku(sku);
				wsp.setCemail(email);
				if (iqty < miniNumber) {
					wsp.setIqty(miniNumber);
				} else {
					wsp.setIqty(iqty);
				}
				wsp.setIwebsiteid(websiteid);
				boolean result = this.wholeSaleUpdateService
						.addWholeSaleProduct(wsp);
				if (result) {
					return ok("success");
				}
			} else {
				boolean result = this.wholeSaleUpdateService.updateQtyByIid(
						wholeSaleProduct.getIid(), Integer.parseInt(qty)
								+ wholeSaleProduct.getIqty());
				if (result) {
					return ok("success");
				}
			}

		}
		return ok("failure");
	}

	private int getMiniWholeNumber() {
		String miniWholesaleNumber = parameterService.getSystemParameter(
				foundationService.getSiteID(), foundationService.getLanguage(),
				"WholeSaleProductMinQty");
		if (null == miniWholesaleNumber || "".equals(miniWholesaleNumber)) {
			miniWholesaleNumber = "5";
		}
		return Integer.parseInt(miniWholesaleNumber);
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result wholesaleValidate() {
		Integer websiteid = foundationService.getSiteID();
		try {
			JsonNode json = request().body().asJson();
			if (json.get("csku") != null) {
				String sku = json.get("csku").asText();
				String email = loginService.getLoginEmail();
				if (null != email) {
					boolean isExist = wholeSaleBaseEnquiryService
							.checkWholeSaleBaseByEmail(email, websiteid);
					if (isExist) {
						WholeSaleProduct wholeSaleProduct = this.wholeSaleEnquiryService
								.getWholeSaleProductsByEmailAndSkuAndWebsite(
										email, websiteid, sku);
						if (wholeSaleProduct == null) {
							return ok("show");
						}
						return ok("showAndYes");
					}
				}
			}
		} catch (Exception ex) {
			Logger.error("WholeSale.wholesaleValidate error: ", ex);
		}
		return ok("noshow");
	}

	public Result placeOrder() {
		Form<WholesaleOrderForm> form = Form.form(WholesaleOrderForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		WholesaleOrderForm orderForm = form.get();
		if (orderForm.getIsShipping() && orderForm.getAddressID() == null) {
			return badRequest("form error: addressID is null");
		}
		List<Integer> productIDs = getProductIDs(orderForm.getProductIDs());
		List<WholeSaleProduct> products = productEnquiryService
				.getByIDs(productIDs);
		if (products == null || products.isEmpty()) {
			return badRequest("This product is not exist");
		}
		Map<String, Integer> listingIDQtyMap = getListingIDQtyMap(products);
		MemberAddress address = null;
		if (orderForm.getIsShipping()) {
			address = addressService.getMemberAddressById(orderForm
					.getAddressID());
		}
		SaveOrderRequest req = new SaveOrderRequest();
		req.setCurrency(foundationService.getCurrency());
		req.setLanguageID(foundationService.getLanguage());
		req.setListingQtyMap(listingIDQtyMap);
		req.setMis(loginService.getLoginData());
		req.setWebsiteID(foundationService.getSiteID());
		req.setIp(request().remoteAddress());
		req.setDetailProviderID("wholesale");
		req.setAddress(address);
		req.setIsNeedShippingMethod(orderForm.getIsShipping());
		Optional<String> source = FluentIterable.from(sourceProviders)
				.transform(sp -> sp.getSource(Context.current()))
				.filter(x -> x != null).first();
		String origin = source.orNull();
		req.setOrigin(origin);
		req.setVhost(foundationService.getVhost());
		Order order = productToOrder.saveOrder(req);
		taggingService.tag(order.getIid(), Lists.newArrayList("wholesale"));
		saveProductToOrder(products, order);
		return redirect(controllers.order.routes.OrderProcessing.selectOrder(
				order.getCordernumber(), orderForm.getIsShipping()));
	}

	private void saveProductToOrder(List<WholeSaleProduct> products, Order order) {
		orderProductUpdate.saveWholeSaleProduct(products, order.getIid());
		List<Integer> ids = Lists.transform(products, p -> p.getIid());
		wholeSaleUpdateService.batchDeleteByIid(ids, order.getCmemberemail());
	}

	private Map<String, Integer> getListingIDQtyMap(
			List<WholeSaleProduct> products) {
		int siteID = foundationService.getSiteID();
		Map<String, Integer> listingIDQtyMap = Maps.newHashMap();
		for (WholeSaleProduct wholeSaleProduct : products) {
			String listingID = productEnquiry.getListingsBySku(
					wholeSaleProduct.getCsku(), siteID);
			if (listingIDQtyMap.get(wholeSaleProduct.getCsku()) != null) {
				int qty = listingIDQtyMap.get(listingID)
						+ wholeSaleProduct.getIqty();
				listingIDQtyMap.put(listingID, qty);
			} else {
				listingIDQtyMap.put(listingID, wholeSaleProduct.getIqty());
			}
		}
		return listingIDQtyMap;
	}

	private List<Integer> getProductIDs(String ids) {
		try {
			if (StringUtils.notEmpty(ids)) {
				String[] arr = ids.split(",");
				List<Integer> list = Lists.newArrayList();
				for (String id : arr) {
					list.add(Integer.valueOf(id));
				}
				return list;
			}
		} catch (NumberFormatException e) {
			return Lists.newArrayList();
		}
		return Lists.newArrayList();
	}
}
