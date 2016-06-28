package services.wholesale;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.FluentIterable;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;

import play.Logger;
import play.libs.Json;
import services.base.FoundationService;
import services.price.PriceService;
import services.product.ProductEnquiryService;
import valueobjects.price.Price;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.SingleProductSpec;
import valueobjects.wholesale.PriceContext;
import valueobjects.wholesale.WholesalePrice;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dto.product.ProductBase;
import entity.wholesale.WholeSaleDiscountLevel;

public class WholesalePriceService {

	@Inject
	PriceService priceService;
	@Inject
	ProductEnquiryService productEnquiryService;
	@Inject
	WholeSaleDiscountLevelEnquiryService wholeSaleDiscountLevelEnquiryService;
	@Inject
	FoundationService foundationService;

	public WholesalePrice getPrice(IProductSpec iProductSpec) {
		Price price = priceService.getPrice(iProductSpec);
		WholesalePrice wprice = new WholesalePrice();
		wprice.setPrice(price);
		return wprice;
	}

	public List<WholesalePrice> getPrice(List<PriceContext> contexts,
			Integer websiteId) {
		String currency = foundationService.getCurrency();
		Logger.debug("currency========{}",currency);
		List<String> skus = Lists.transform(contexts, p -> p.getSku());
		List<ProductBase> plist = productEnquiryService.getProductBaseBySkus(
				skus, websiteId);

		Multimap<String, ProductBase> pmultimap = Multimaps.index(plist,
				p -> p.getCsku());
		Map<String, PriceContext> qtymap = Maps.uniqueIndex(contexts,
				p -> p.getSku());
		List<IProductSpec> speclist = Lists.transform(
				plist,
				p -> new SingleProductSpec(p.getClistingid(), qtymap.get(
						p.getCsku()).getQty()));
		List<Price> pricelist = priceService.getPrice(speclist);
		Map<String, Price> priceMap = Maps.uniqueIndex(pricelist,
				p -> p.getListingId());

		double wsdiscount = getWholesaleDiscount(plist, websiteId, qtymap);
		List<WholesalePrice> priceslist = FluentIterable
				.from(pmultimap.keySet())
				.transform(
						p -> {
							return FluentIterable.from(pmultimap.get(p))
									//
									.transform(
											p1 -> priceMap.get(p1
													.getClistingid()))
									//
									.toSortedList(
											(o1, o2) -> Double.compare(
													o1.getUnitPrice(),
													o2.getUnitPrice()))//
									.get(0);
						})//
				.transform(p -> {
					WholesalePrice wprice = new WholesalePrice();
					wprice.setPrice(p);
					wprice.setWsBaseDiscount(wsdiscount);
					Double tprice = p.getUnitBasePrice() * (1 - wsdiscount);
					//保留2位小数
					DecimalFormat df = null; 
					if(currency!=null && "JPY".equals(currency)){
						df = new DecimalFormat("0");
					}else{
						df = new DecimalFormat("0.00");
					}
					tprice = new Double(df.format(tprice).toString());  
					Logger.debug("unitprice --> {} ,wsprice --> {}",p.getUnitPrice() ,tprice);
					wprice.setWsBasePrice(tprice);
					wprice.setWsDiscount(wsdiscount);
					wprice.setWsPrice(tprice);
					if (p.getUnitPrice() < tprice) {
						wprice.setWsDiscount(p.getDiscount());
						wprice.setWsPrice(new Double(df.format(p.getUnitPrice()).toString()));
					}
					return wprice;
				}).toList();
		
		return priceslist;
	}

	private Double getWholesaleDiscount(List<ProductBase> plist,
			Integer websiteId, Map<String, PriceContext> qtymap) {
		WholeSaleDiscountLevel wdl = wholeSaleDiscountLevelEnquiryService
				.getBySiteAndPrice(websiteId, this.getTotalPrice(plist, qtymap));
		if (null == wdl) {
			return 0d;
		} else {
			return wdl.getFmindiscount();
		}

	}

	private Double getTotalPrice(List<ProductBase> plist,
			Map<String, PriceContext> qtymap) {
		Double price = 0d;
		List<String> csku = new ArrayList<String>();
		for (ProductBase p : plist) {
			if (csku.contains(p.getCsku())) {
				continue;
			}
			csku.add(p.getCsku());
			price += (p.getFprice() * qtymap.get(p.getCsku()).getQty());
		}
		return price;
	}

}
