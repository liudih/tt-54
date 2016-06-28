package controllers.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.utils.ExcelUtils;
import services.base.FoundationService;
import services.member.login.LoginService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.OrderUpdateService;
import services.order.dropShipping.DSDTOTransformService;
import services.order.dropShipping.DropShippingEnquiryService;
import services.order.dropShipping.DropShippingMapEnquiryService;
import services.order.dropShipping.DropShippingOrderConvertService;
import services.order.dropShipping.DropShippingUpdateService;
import services.order.dropShipping.DropShippoingToOrderService;
import services.order.dropShipping.IDropshipService;
import services.payment.IPaymentService;
import services.product.ProductEnquiryService;
import services.product.inventory.InventoryEnquiryService;
import valueobjects.order_api.dropshipping.DropShippingRow;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.product.ProductLite;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import controllers.annotation.DropShippingAuthenticator;
import dao.product.impl.ProductBaseEnquiryDao;
import dto.DropShippingOrderDTO;
import dto.Country;
import dto.Currency;
import dto.order.Order;
import dto.product.ProductBase;
import enums.OrderLableEnum;
import extensions.order.IOrderSourceProvider;
import extensions.payment.IPaymentHTMLPlugIn;
import extensions.payment.IPaymentProvider;
import forms.order.DropShippingForm;
import forms.order.DropShippingOrderForm;
import forms.order.ReplaceOrder;

@DropShippingAuthenticator
public class DropShipping extends Controller {
	@Inject
	private ExcelUtils excelUtils;
	@Inject
	private FoundationService foundtion;
	@Inject
	private DropShippingOrderConvertService convertService;
	@Inject
	private LoginService loginService;
	@Inject
	private DSDTOTransformService dtoService;
	@Inject
	private CountryService countryEnquiry;
	@Inject
	private DropShippoingToOrderService dropShippoingToOrder;
	@Inject
	private Set<IOrderSourceProvider> sourceProviders;
	@Inject
	private DropShippingMapEnquiryService dropShippingMapEnquiry;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private OrderUpdateService orderUpdate;
	@Inject
	private IOrderStatusService statusService;
	@Inject
	private IPaymentService paymentService;
	@Inject
	private IDropshipService dropshipService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private DropShippingUpdateService dropShippingUpdate;
	@Inject
	private DropShippingEnquiryService dropShippingEnquiry;
	@Inject
	InventoryEnquiryService inventoryEnquiryService;
	@Inject
	ProductBaseEnquiryDao productBaseEnquiryDao;

	public Result uploadOrder() {
		DynamicForm form = Form.form().bindFromRequest();
		String description = form.get("description");
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart excel = body.getFile("excel");
		if (excel == null) {
			return badRequest("file is null!");
		}
		boolean b = ExcelUtils.isExcel2003(excel.getFilename());
		List<List<String>> dataList = excelUtils.read(excel.getFile(), b);
		String dropShippingID = null;
		if (null == dataList) {
			return badRequest("file type error!");
		} else {
			dropShippingID = convertService
					.convertExcelToDropShippingOrderAndInsert(dataList,
							description, foundtion.getSiteID(),
							loginService.getLoginEmail(),
							foundtion.getLanguage(), "USD");
		}
		if (null == dropShippingID) {
			return badRequest(convertService.getErrorMessage().toString());
		} else {
			return redirect(routes.DropShipping.confirmOrder(dropShippingID));
		}
	}

	public Result confirmOrder(String dropShippingID) {
		dto.order.dropShipping.DropShipping ds = dropShippingEnquiry
				.getByDropShippingID(dropShippingID);
		if (ds == null || ds.getBused() == true) {
			return badRequest("Can't found this DropShippingID, or it has been used. ID: "
					+ dropShippingID);
		}
		List<DropShippingOrderDTO> dtos = dtoService.getDropShippingOrderDTOs(
				dropShippingID, foundtion.getLanguage(), "USD",
				foundtion.getSiteID());
		if (dtos.isEmpty()) {
			return badRequest("error dropShippingID: " + dropShippingID);
		}

		Currency cy = currencyService.getCurrencyByCode("USD");
		return ok(views.html.order.dropShipping.drop_shipping_table.render(
				dtos, dropShippingID, cy));
	}

	public Result dropShipping() {
		List<Country> countrys = countryEnquiry.getAllCountries();
		return ok(views.html.order.dropShipping.drop_shipping.render(countrys));
	}

	public Result submitOrder() {
		Form<DropShippingForm> form = Form.form(DropShippingForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		String currency = "USD";
		DropShippingForm dropShippingForm = form.get();
		DropShippingRow row = new DropShippingRow();
		try {
			BeanUtils.copyProperties(row, dropShippingForm);
			Country country = countryEnquiry.getCountryByShortCountryName(row
					.getCountrysn());
			if (country == null) {
				row.setErrorLog("we can't found country by this counry code: "
						+ row.getCountrysn() + ". ");
			} else {
				row.setCountry(country.getCname());
			}
		} catch (Exception e) {
			Logger.error("DropShipping submitOrder error: ", e);
			return badRequest("DropShipping submitOrder error: " + e);
		}
		String dropShippingID = null;
		List<String> skuList = dropshipService.getDropShipSkusByEmailAndSite(
				loginService.getLoginEmail(), foundtion.getSiteID());
		skuList.add(convertService.getDropShippingSku(foundtion.getSiteID(),
				foundtion.getLanguage()));
		dropShippingID = convertService.convertAndInsert(
				Lists.newArrayList(row), null, foundtion.getSiteID(),
				loginService.getLoginEmail(), foundtion.getLanguage(),
				currency, skuList);
		if (null == dropShippingID) {
			return badRequest(convertService.getErrorMessage().toString());
		} else {
			convertService.insertDropShipping(dropShippingID, currency,
					loginService.getLoginEmail(), foundtion.getSiteID());
			return redirect(routes.DropShipping.confirmOrder(dropShippingID));
		}
	}

	public Result placeOrder() {
		Form<DropShippingOrderForm> form = Form.form(
				DropShippingOrderForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		DropShippingOrderForm orderForm = form.get();
		dto.order.dropShipping.DropShipping ds = dropShippingEnquiry
				.getByDropShippingID(orderForm.getDropShippingID());
		if (ds == null || ds.getBused() == true) {
			return badRequest("Can't found this DropShippingID, or it has been used. ID: "
					+ orderForm.getDropShippingID());
		}
		String[] ids = orderForm.getIds().split(",");
		String[] shippingMethodIDs = orderForm.getShippingMethodIDs()
				.split(",");
		if (shippingMethodIDs.length != ids.length) {
			return badRequest("error orders, shippingMethodIDs.length != ids.length");
		} else {
			Optional<String> source = FluentIterable.from(sourceProviders)
					.transform(sp -> sp.getSource(Context.current()))
					.filter(x -> x != null).first();
			String origin = source.orNull();
			String ip = request().remoteAddress();
			Integer memberID = loginService.getLoginData().getMemberId();
			Map<Integer, Integer> idMap = Maps.newHashMap();
			for (int i = 0; i < ids.length; i++) {
				Integer id = Integer.valueOf(ids[i]);
				Integer shippingMehodID = Integer.valueOf(shippingMethodIDs[i]);
				idMap.put(id, shippingMehodID);
			}
			List<Order> orderList = dropShippoingToOrder
					.converToOrderAndInsert(idMap, origin, ip,
							foundtion.getVhost(), memberID,
							foundtion.getLanguage());
			if (orderList == null) {
				return badRequest(dropShippoingToOrder.getErrorLog());
			}
			dropShippingUpdate.setUsedByDropShippingID(orderForm
					.getDropShippingID());
			return redirect(routes.DropShipping.choosePayment(orderForm
					.getDropShippingID()));
		}
	}

	public Result choosePayment(String dropShippingID) {
		List<String> orderNumbers = dropShippingMapEnquiry
				.getOrderNumbersByID(dropShippingID);
		Integer status = statusService
				.getIdByName(IOrderStatusService.PAYMENT_PENDING);
		String email = loginService.getLoginEmail();
		for (String orderNumber : orderNumbers) {
			Integer orderID = orderEnquiry.getOrderIdByOrderNumber(orderNumber);
			if (orderID != null && status != null) {
				ReplaceOrder form = new ReplaceOrder();
				form.setCorderId(orderNumber);
				form.setOrderId(orderID);
				form.setPaymentId("paypal");
				orderUpdate.replaceOrder(form, status, email);
			}
		}
		return redirect(routes.DropShipping.payment(dropShippingID));
	}

	public Result payment(String dropShippingID) {
		String url = "/paypal/ec-dropshipping?dropShippingID=" + dropShippingID;
		//controllers.paypal.routes.ExpressCheckoutNvp.setExpressCheckoutForOrder(order.getCordernumber())
		return redirect(url);
		
		/*String email = loginService.getLoginEmail();
		PaymentContext paymentContext = dropShippoingToOrder.getPaymentContext(
				dropShippingID, email);
		paymentContext.setOrderLable(OrderLableEnum.DROP_SHIPPING.getName());
		Order order = paymentContext.getOrder().getOrder();
		if (order == null) {
			return badRequest();
		}
		
		
		
		String paymentId = order.getCpaymentid();
		IPaymentProvider paymentProvider = paymentService
				.getPaymentById(paymentId);
		List<IPaymentHTMLPlugIn> plugIns = Lists.newArrayList();
		return ok(views.html.order.payment.do_payment.render(paymentContext,
				paymentProvider, plugIns));*/
	}
	
	public Result checkStockAndStatus(String sku, Integer qty){
		int lang = foundtion.getLanguage();
		int site = foundtion.getSiteID();
		Map<String,Object> mjson = new HashMap<String,Object>();
		ProductBase pb = productBaseEnquiryDao.getProductByCskuAndIsActivity(sku, site);
		if(pb==null){
			mjson.put("result", "sku is not found");
			return ok(Json.toJson(mjson));
		}
		Integer status = pb.getIstatus();
		if(status==null || status!=1){
			mjson.put("result", "sold out!");
			return ok(Json.toJson(mjson));
		}
		String listingid = pb.getClistingid();
		boolean isInv = inventoryEnquiryService.checkInventory(listingid, qty);
		if(!isInv){
			mjson.put("result", "out of stock!");
			return ok(Json.toJson(mjson));
		}
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}
}
