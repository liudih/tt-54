package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import play.Logger;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.ILanguageService;
import services.base.FoundationService;
import services.price.PriceService;
import services.product.IProductFragmentProvider;
import services.search.criteria.ProductLabelType;
import valueobjects.price.Price;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductExplainEnquiryDao;
import dao.product.IProductInterceptUrlEnquiryDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductUrlEnquiryDao;
import dto.product.ProductBase;
import dto.product.ProductExplain;
import dto.product.ProductInterceptUrl;
import dto.product.ProductLabel;
import dto.product.ProductUrl;

public class ProductBaseFragmentProvider implements IProductFragmentProvider {

	public static final String NAME = "base";

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	ILanguageService languageService;

	@Inject
	PriceService priceService;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	@Inject
	IProductExplainEnquiryDao productExplainEnquityDao;

	@Inject
	IProductInterceptUrlEnquiryDao interceptUrlEnquiryDao;

	@Inject
	IProductLabelEnquiryDao productlabeldao;

	@Inject
	IProductBaseEnquiryDao baseenquiry;

	@Inject
	ProductBaseFragmentCacheService productBaseFragmentCacheService;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		long starttime = System.currentTimeMillis();
		ProductBase base = productBaseFragmentCacheService.getProductBase(
				context.getListingID(), context.getLang());
		List<ProductExplain> productExplains = productBaseFragmentCacheService
				.getProductExplainList(foundationService.getSiteID(),
						context.getLang());
		Price price = priceService.getPrice(context.getListingID());
		// add free shipping label
		List<ProductLabel> plabelLabels = productlabeldao
				.getProductLabel(context.getListingID());

		for (ProductLabel l : plabelLabels) {
			if (!l.getCtype().equals(ProductLabelType.Clearstocks.toString())
					&& l.getCtype().equals(
							ProductLabelType.FreeShipping.toString())) {
				processingContext.put("isfree", true);
			}
		}
		// add In Stock or Out Sold
		ProductBase pBase = baseenquiry.getStatusByListingIdAndsiteId(
				context.getListingID(), context.getSiteID());
		if(pBase==null){
			throw new RuntimeException("Not ProductBase Found!");
		}
		if (pBase.getIstatus() == 1) {
			if (pBase.getIqty() <= 0) {
				processingContext.put("status", 0);
			} else {
				processingContext.put("status", 1);
			}
		} else if (pBase.getIstatus() == 2 || pBase.getIstatus() == 3) {
			processingContext.put("status", 2);
		} else if (pBase.getIqty() <= 0) {
			processingContext.put("status", 0);
		} else {
			return null;
		}
		processingContext.put("meta-title", base.getCmetatitle());
		processingContext.put("meta-description", base.getCmetadescription());
		processingContext.put("meta-keywords", base.getCmetakeyword());
		processingContext.put("title", base.getCtitle());
		processingContext.put("description", base.getCdescription());
		processingContext.put("keywords", base.getCkeyword());
		if (productExplains == null) {
			productExplains = productExplainEnquityDao
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

		ProductUrl purl = this.productUrlEnquityDao.getProductUrlsByListingId(
				context.getListingID(), foundationService.getLanguage());
		if (null != purl) {
			url = purl.getCurl();
		}
		ProductInterceptUrl interceptUrl = interceptUrlEnquiryDao
				.getProductBySkuAndLanguage(context.getSku(),
						foundationService.getLanguage());
		if (null != interceptUrl) {
			processingContext.put("isActivity", false);
		}
		// facebook等分享的一些属性
		if (langid != null) {
			services.base.HtmlUtils.misc().addHeadOnce(
					new Html("<meta property=\"og:locale\" content=\""
							+ languageService.getLanguage(langid).getCname()
							+ "\"/>"));
			services.base.HtmlUtils.misc().addHeadOnce(
					new Html("<meta property=\"og:locale\" content=\""
							+ languageService.getLanguage(langid).getCname()
							+ "\"/>"));
		}
		if (url != null) {
			services.base.HtmlUtils.misc().addHeadOnce(
					new Html(
							"<meta name=\"facebooknamesp\" property=\"og:url\" content=\""
									+ controllers.product.routes.Product.view(
											url).absoluteURL(
											Context.current().request())
									+ "\"/>"));
		}
		Logger.debug("-->time-->ProductBaseFragmentProvider-->{}",
				System.currentTimeMillis() - starttime);
		return new valueobjects.product.ProductBase(base, price);
	}

}
