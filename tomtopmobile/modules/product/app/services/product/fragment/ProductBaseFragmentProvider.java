package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Http.Context;
import services.ILanguageService;
import services.base.FoundationService;
import services.price.IPriceService;
import services.product.IProductEnquiryService;
import services.product.IProductExplainService;
import services.product.IProductFragmentProvider;
import services.product.IProductInterceptUrlService;
import services.product.IProductUrlService;
import valueobjects.price.Price;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import context.ContextUtils;
import context.WebContext;
import dto.product.ProductBase;
import dto.product.ProductExplain;
import dto.product.ProductInterceptUrl;
import dto.product.ProductUrl;

public class ProductBaseFragmentProvider implements IProductFragmentProvider {

	public static final String NAME = "base";
	
	public static final Integer STRLEN=800;

	@Inject
	FoundationService foundationService;

	@Inject
	ILanguageService languageService;

	@Inject
	IPriceService priceService;

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	IProductExplainService productExplainService;

	@Inject
	IProductInterceptUrlService interceptUrlService;
	
	@Inject
	IProductUrlService productUrlService;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		
		ProductBase base = productEnquiryService.getProductByListingIdAndLanguageWithdoutDesc(
				context.getListingID(), context.getLang());
		
		List<ProductExplain> productExplains = productExplainService
				.getProductExplainsBySiteAndLan(foundationService.getSiteID(),
						context.getLang());
		String currency = foundationService.getCurrency();
		Price price = priceService.getPrice(context.getListingID(),currency);
		
		StringBuffer desc=new StringBuffer("");
		StringBuffer descinfo;
		
		for (int begin = 0; true;) {
			descinfo=new StringBuffer();
			descinfo.append(productEnquiryService.getProductDescByListingIdAndLanguagePart(context.getListingID(),context.getLang(),begin,STRLEN));
			     if(productEnquiryService.getProductDescByListingIdAndLanguagePart(context.getListingID(),context.getLang(),begin,STRLEN)==null){
			    	 break;
			     }
			     desc.append(descinfo);
			begin+=800;
		}
		processingContext.put("meta-title", base.getCmetatitle());
		processingContext.put("meta-description", base.getCmetadescription());
		processingContext.put("meta-keywords", base.getCmetakeyword());
		processingContext.put("title", base.getCtitle());
		
		processingContext.put("description",desc.toString());
		processingContext.put("keywords", base.getCkeyword());
		
		
		if (productExplains == null) {
			productExplains = productExplainService
					.getProductExplainsBySiteAndLan(
							foundationService.getSiteID(), 1);
		}
		for (ProductExplain productExplain : productExplains) {
			processingContext.put(productExplain.getCtype(),
					productExplain.getCcontent());
		}
		processingContext.put("price", base.getFprice());
		processingContext.put("sale-price", price);
		processingContext.put("listingid", context.getListingID());
		processingContext.put("sku", context.getSku());
		Integer langid = foundationService.getLanguage();
		String url = "";

		ProductUrl purl = this.productUrlService.getProductUrlsByListingId(
				context.getListingID(), webContext);
		if (null != purl) {
			url = purl.getCurl();
		}
		
		ProductInterceptUrl interceptUrl = interceptUrlService
				.getProductBySkuAndLanguage(context.getSku(), webContext);
		if (null != interceptUrl) {
			processingContext.put("isActivity", false);
		}
		return new valueobjects.product.ProductBase(base, price);
	}
}
