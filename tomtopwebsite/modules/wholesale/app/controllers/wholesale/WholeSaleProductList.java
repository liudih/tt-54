package controllers.wholesale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.member.address.AddressService;
import services.member.login.ILoginService;
import services.product.ProductEnquiryService;
import services.product.inventory.InventoryEnquiryService;
import services.wholesale.ProductCartLifecycleService;
import services.wholesale.WholeSaleProductEnquiryService;
import authenticators.member.MemberLoginAuthenticator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import dao.product.impl.ProductBaseEnquiryDao;
import dto.Country;
import dto.Currency;
import dto.member.MemberAddress;
import dto.product.ProductBase;
import entity.wholesale.WholeSaleProduct;
import facades.wholesale.WholeSaleProductCart;

public class WholeSaleProductList extends Controller {

	@Inject
	private ILoginService loginService;
	@Inject
	private AddressService addressService;
	@Inject
	private FoundationService foundationService;
	@Inject
	private SystemParameterService parameterService;
	@Inject
	private ProductEnquiryService productEnquiryService;
	@Inject
	private WholeSaleProductEnquiryService enquiryService;
	@Inject
	private ProductCartLifecycleService productCartLifecycleService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private CountryService countryService;
	@Inject
	InventoryEnquiryService inventoryEnquiryService;
	@Inject
	ProductBaseEnquiryDao productBaseEnquiryDao;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result list() {
		return ok(views.html.wholesale.member.wholesale_product_list
				.render(getWholeSaleProductList()));
	}

	public Html getWholeSaleProductList() {
		String memberEmail = loginService.getLoginData().getEmail();
		int siteId = foundationService.getSiteID();
		int languageId = foundationService.getLanguage();
		String currency = foundationService.getCurrency();
		WholeSaleProductCart wholeSaleProductCart = productCartLifecycleService
				.creatWholeSaleProductCart(memberEmail, siteId, languageId,
						currency);
		wholeSaleProductCart.initialize();
		List<Integer> ids = wholeSaleProductCart.getSelectWholeSaleProductIds();
		Currency cu = currencyService.getCurrencyByCode(currency);
		Integer minQty = this.getMiniWholeNumber();
		List<MemberAddress> addresses = addressService
				.getMemberShippingAddressByEmail(memberEmail);
		List<MemberAddress> billAddresses = addressService
				.getOrderAddressByEmail(memberEmail);
		Map<Integer, Country> countryMap = Maps.uniqueIndex(
				countryService.getReallyAllCountries(), c -> c.getIid());
		return views.html.wholesale.member.wholesale_product_message.render(
				wholeSaleProductCart, ids, cu, minQty, addresses, countryMap,
				billAddresses);
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

	public Result addProduct() {
		Form<WholeSaleProduct> wholeSaleProductFrom = Form.form(
				WholeSaleProduct.class).bindFromRequest();
		WholeSaleProduct wholeSaleProduct = wholeSaleProductFrom.get();
		int siteId = foundationService.getSiteID();
		String memberEmail = loginService.getLoginData().getEmail();
		Integer state = 1;
		String sku = wholeSaleProduct.getCsku().toUpperCase();
		ProductBase p = productEnquiryService.getProductBySku(sku, siteId,
				state);
		Map<String, String> resultMap = new HashMap<String, String>();
		if (null == p) {
			resultMap.put("error", "error sku!");
			return ok(Json.toJson(resultMap));
		}
		if (1 != p.getIstatus()) {
			resultMap.put("error", "sorry,this product is out of stock!");
			return ok(Json.toJson(resultMap));
		}
		int languageId = foundationService.getLanguage();
		Integer qty = this.getMiniWholeNumber();
		wholeSaleProduct.setCemail(memberEmail);
		wholeSaleProduct.setIwebsiteid(siteId);
		wholeSaleProduct.setCsku(sku);
		if (qty > wholeSaleProduct.getIqty()) {
			wholeSaleProduct.setIqty(qty);
		}
		String currency = foundationService.getCurrency();
		WholeSaleProductCart wholeSaleProductCart = productCartLifecycleService
				.creatWholeSaleProductCart(memberEmail, siteId, languageId,
						currency);
		boolean addWholeSaleProduct = wholeSaleProductCart
				.addWholeSaleProduct(wholeSaleProduct);
		if (addWholeSaleProduct) {
			resultMap.put("result", "success");
			return ok(Json.toJson(resultMap));
		} else {
			resultMap.put("result", "fair");
			return ok(Json.toJson(resultMap));
		}
	}

	@SuppressWarnings("unchecked")
	@BodyParser.Of(BodyParser.Json.class)
	public Result chooseProduct() {
		JsonNode asJson = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.convertValue(asJson, Map.class);
		List<Integer> wproductIds = (List<Integer>) result.get("wproductIds");
		List<WholeSaleProduct> wholeSaleProducts = null;
		if (!wproductIds.isEmpty()) {
			wholeSaleProducts = enquiryService.getByIDs(wproductIds);
		}

		String memberEmail = loginService.getLoginData().getEmail();
		int siteId = foundationService.getSiteID();
		int languageId = foundationService.getLanguage();
		String currency = foundationService.getCurrency();
		WholeSaleProductCart wholeSaleProductCart = productCartLifecycleService
				.creatWholeSaleProductCart(memberEmail, siteId, languageId,
						currency);
		wholeSaleProductCart.setSelectWholeSaleProducts(wholeSaleProducts);
		Currency cu = currencyService.getCurrencyByCode(currency);
		Integer minQty = this.getMiniWholeNumber();
		List<MemberAddress> addresses = addressService
				.getMemberShippingAddressByEmail(memberEmail);
		List<MemberAddress> billAddresses = addressService
				.getOrderAddressByEmail(memberEmail);
		Map<Integer, Country> countryMap = Maps.uniqueIndex(
				countryService.getReallyAllCountries(), c -> c.getIid());
		return ok(views.html.wholesale.member.wholesale_product_message.render(
				wholeSaleProductCart, wproductIds, cu, minQty, addresses,
				countryMap, billAddresses));
	}

	public Result updateProductQty(Integer iid, Integer qty) {
		String memberEmail = loginService.getLoginData().getEmail();
		int siteId = foundationService.getSiteID();
		int languageId = foundationService.getLanguage();
		String currency = foundationService.getCurrency();
		WholeSaleProductCart wholeSaleProductCart = productCartLifecycleService
				.creatWholeSaleProductCart(memberEmail, siteId, languageId,
						currency);
		boolean flag = wholeSaleProductCart.updateWholeSaleProductQty(iid, qty);

		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", flag);

		return ok(Json.toJson(result));
	}

	public Result deleteProduct(Integer iid) {
		String memberEmail = loginService.getLoginData().getEmail();
		int siteId = foundationService.getSiteID();
		int languageId = foundationService.getLanguage();
		String currency = foundationService.getCurrency();
		WholeSaleProductCart wholeSaleProductCart = productCartLifecycleService
				.creatWholeSaleProductCart(memberEmail, siteId, languageId,
						currency);
		boolean flag = wholeSaleProductCart.deleteWholeSaleProduct(iid);

		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", flag);

		return ok(Json.toJson(result));
	}

	@SuppressWarnings("unchecked")
	@BodyParser.Of(BodyParser.Json.class)
	public Result deleteAllProduct() {
		JsonNode asJson = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.convertValue(asJson, Map.class);
		List<Integer> wproductIds = (List<Integer>) result.get("wproductIds");
		String memberEmail = loginService.getLoginData().getEmail();
		int siteId = foundationService.getSiteID();
		int languageId = foundationService.getLanguage();
		String currency = foundationService.getCurrency();
		WholeSaleProductCart wholeSaleProductCart = productCartLifecycleService
				.creatWholeSaleProductCart(memberEmail, siteId, languageId,
						currency);
		if (!wproductIds.isEmpty()) {
			wholeSaleProductCart.deleteAllWholeSaleProduct(wproductIds);
		}
		return ok(getWholeSaleProductList());
	}
	
	public Result checkStockAndStatus(){
		int site = foundationService.getSiteID();
		Map<String,Object> mjson = new HashMap<String,Object>();
		String email = "";
		boolean islogin = foundationService.getLoginContext().isLogin();
		if(islogin){
			email = foundationService.getLoginContext().getMemberID();
		}else{
			mjson.put("result", "not login");
			return ok(Json.toJson(mjson));
		}
		List<WholeSaleProduct> wplist = enquiryService.getWholeSaleProductsByEmail(email, site);
		for(WholeSaleProduct wp : wplist){
			String sku = wp.getCsku();
			Integer qty = wp.getIqty();
			ProductBase pb = productBaseEnquiryDao.getProductByCskuAndIsActivity(sku, site);
			if(pb==null){
				mjson.put("result", sku+" sku is not found");
				return ok(Json.toJson(mjson));
			}
			Integer status = pb.getIstatus();
			if(status==null || status!=1){
				mjson.put("result", sku+" sold out!");
				return ok(Json.toJson(mjson));
			}
			String listingid = pb.getClistingid();
			boolean isInv = inventoryEnquiryService.checkInventory(listingid, qty);
			if(!isInv){
				mjson.put("result", sku+" out of stock!");
				return ok(Json.toJson(mjson));
			}
		}
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}
	
	
}
