package services.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import play.Logger;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.product.IEntityMapService;
import services.product.IProductBaseEnquiryService;
import services.product.IProductMessageService;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import context.WebContext;
import dto.product.ProductAttributeMessage;
import dto.product.ProductMessage;

public class ProductMessageService {
	@Inject
	IProductMessageService productMessageService;

	@Inject
	IProductBaseEnquiryService productBaseEnquiryService;

	@Inject
	IEntityMapService entityService;

	@Inject
	FoundationService foundation;

	public List<ProductMessage> getProductMessages(List<String> listingids,
			WebContext webContext) {
		Logger.debug("listingids:{}", listingids.size());
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
		Logger.debug("spus:{}", spus);
		if (null != spus && 0 < spus.size()) {
			List<String> listingIdsBySpus = productBaseEnquiryService
					.getListingIdsBySpus(spus, this.foundation.getWebContext());
			Logger.debug("listingIdsBySpus:{}", listingIdsBySpus);
			List<ProductMessage> productMessagesBySpus = productMessageService
					.getProductMessageByListingIDs(listingIdsBySpus, webContext);
			List<String> listingIds = productMessagesBySpus.stream()
					.filter(p -> p.getParentsku() != null)
					.map(ProductMessage::getListingid)
					.collect(Collectors.toList());
			Logger.debug("listingIds:{}", listingIds);
			ImmutableListMultimap<String, ProductMessage> attributeMap = Multimaps
					.index(productMessagesBySpus, a -> a.getListingid());
			List<ProductAttributeMessage> productAttributeMessages = entityService
					.getProductAttributeMessages(listingIds, webContext,
							"color");
			Logger.debug("productAttributeMessages:{}",
					productAttributeMessages);
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

		return productMessages;
	}

}
