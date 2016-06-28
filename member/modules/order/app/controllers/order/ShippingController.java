package controllers.order;

import java.util.List;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.cart.fragment.ProductStorageMapFragmentProvider;
import services.shipping.ShippingMethodService;
import services.shipping.ShippingServices;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.api.client.util.Lists;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;

import dto.Country;
import dto.Currency;
import dto.Storage;
import services.IStorageService;

public class ShippingController extends Controller {
	@Inject
	private ShippingServices shippingServices;
	@Inject
	private CountryService countryEnquiryService;
	@Inject
	private ProductStorageMapFragmentProvider provider;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private FoundationService foundation;
	@Inject
	private CurrencyService currencyService;
	@Inject
	IStorageService iStorageService;

	public Result getShipping(String country, String listing) {
		Country us = countryEnquiryService
				.getCountryByShortCountryName(country);
		List<String> listings = Lists.newArrayList();
		listings.add(listing);
		Storage t = shippingServices.getShippingStorage(1, us, listings);
		String storageid = t.getIid().toString();
		return ok(storageid);
	}

	public Result refreshInProduct(Integer storageID, String listingID,
			Integer qty) {
		//~转换成真实仓计算物流
		//~ 获取真实仓库
		int tstorid = storageID;
		List<dto.Storage> storagelist = iStorageService.getAllStorages();
		List<dto.Storage> newstoragelist = Lists.newArrayList(Collections2
				.filter(storagelist, c -> c.getIparentstorage() == tstorid));
		if (newstoragelist != null && newstoragelist.size() > 0) {
			storageID = newstoragelist.get(0).getIid();
		}
		
		ShippingMethodRequst req = provider.getShippingMethodRequst(storageID,
				listingID, qty);
		ShippingMethodInformations infos = shippingMethodService
				.getShippingMethodInformations(req);
		Currency cy = currencyService.getCurrencyByCode(foundation
				.getCurrency());
		return ok(views.html.cart.fragment.product_shipping.render(infos, cy));
	}
}
