package services.dodocool.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import play.Logger;
import play.mvc.Http.Context;
import services.base.utils.StringUtils;
import services.product.IEntityMapService;
import services.product.IProductBaseEnquiryService;
import services.product.IProductEnquiryService;
import services.product.IProductMessageService;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.product.ProductAttributeMessage;
import dto.product.ProductBase;
import dto.product.ProductMessage;
import extensions.InjectorInstance;

public class ProductMessageService {
	@Inject
	IProductMessageService productMessageService;

	@Inject
	IProductBaseEnquiryService productBaseEnquiryService;

	@Inject
	IEntityMapService entityService;

	@Inject
	ProductAmazonUrlService productAmazonUrlService;

	@Inject
	IProductEnquiryService productEnquiryService;

	public List<ProductMessage> getProductMessages(List<String> listingids,
			WebContext webContext) {
		if (null == listingids || 0 >= listingids.size()) {
			return null;
		}
		Logger.debug("listingids listingids :{}", listingids);
		List<ProductMessage> productMessages = productMessageService
				.getProductMessageByListingIDs(listingids, webContext);
		Logger.debug("productMessages:{}", productMessages.size());
		if (null == productMessages || 0 >= productMessages.size()) {
			return null;
		}
		List<String> spus = productMessages.stream()
				.filter(p -> p.getParentsku() != null)
				.filter(p -> !StringUtils.isEmpty(p.getParentsku()))
				.map(ProductMessage::getParentsku).collect(Collectors.toList());
		Logger.debug("productMessages1:{}", productMessages.size());
		if (null != spus && 0 < spus.size()) {
			Logger.debug("spus:{}", spus);
			List<String> listingIdsBySpus = productBaseEnquiryService
					.getListingIdsBySpus(spus,
							ContextUtils.getWebContext(Context.current()));
			Logger.debug("listingIdsBySpus:{}", listingIdsBySpus);
			if (null == listingIdsBySpus || 0 >= listingIdsBySpus.size()) {
				return null;
			}
			List<ProductMessage> productMessagesBySpus = productMessageService
					.getProductMessageByListingIDs(listingIdsBySpus, webContext);
			Logger.debug("productMessagesBySpus:{}", productMessagesBySpus);
			List<String> listingIds = productMessagesBySpus.stream()
					.filter(p -> p.getParentsku() != null)
					.map(ProductMessage::getListingid)
					.collect(Collectors.toList());
			ImmutableListMultimap<String, ProductMessage> attributeMap = Multimaps
					.index(productMessagesBySpus, a -> a.getListingid());
			List<ProductAttributeMessage> productAttributeMessages = entityService
					.getProductAttributeMessages(listingIds, webContext,
							"color");
			Map<String, List<ProductAttributeMessage>> attributes = productAttributeMessages
					.stream()
					.collect(
							Collectors
									.groupingBy(ProductAttributeMessage::getCparentsku));
			for (ProductMessage productMessage : productMessages) {
				String parentsku = productMessage.getParentsku();
				List<ProductAttributeMessage> list = attributes.get(parentsku);
				if (null != list && 0 <= list.size()) {
					Map<String, ProductMessage> attribute = Maps.newHashMap();
					for (ProductAttributeMessage productAttributeMessage : list) {
						String valueName = productAttributeMessage
								.getCvaluename();
						String clistingid = productAttributeMessage
								.getClistingid();
						ProductMessage productMessage2 = attributeMap.get(
								clistingid).get(0);
						attribute.put(valueName, productMessage2);
					}
					productMessage.setAttribute(attribute);
				}
			}
		}
		Logger.debug("productMessages2:{}", productMessages.size());
		return productMessages;
	}

	public String getDefaultThirdPlatUrl(String listingid) {
		ProductBase productBase = productEnquiryService
				.getBaseByListingId(listingid);
		String sku = productBase.getCsku();
		Map<String, String> productAmazonUrl = productAmazonUrlService
				.getProductAmazonUrl("amazon", sku);
		String amzonUrl = "";
		if (null != productAmazonUrl && !productAmazonUrl.isEmpty()) {
			if (productAmazonUrl.containsKey("United States Of America")) {
				amzonUrl = productAmazonUrl.get("United States Of America");
			} else {
				amzonUrl = productAmazonUrl.get(0);
			}
		}
		return amzonUrl;
	}

	public static ProductMessageService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				ProductMessageService.class);
	}

	public List<ProductMessage> getSimpleProductMessages(
			List<String> listingids, WebContext webContext) {
		if (null == listingids || 0 >= listingids.size()) {
			return null;
		}
		List<ProductMessage> productMessages = productMessageService
				.getProductMessageByListingIDs(listingids, webContext);

		return productMessages;
	}
}
