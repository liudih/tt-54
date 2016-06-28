package services.product;

import java.util.List;

import javax.inject.Inject;

import com.google.api.client.util.Lists;

import mapper.product.ProductBaseMapper;
import services.IFoundationService;
import context.WebContext;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductTranslateEnquiryDao;
import dto.product.ProductMessage;

public class ProductMessageService implements IProductMessageService {
	@Inject
	IFoundationService foundationService;

	@Inject
	IProductBaseEnquiryDao productBaseEnquiryDao;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	IProductTranslateEnquiryDao productTranslateEnquiryDao;

	@Inject
	IProductBadgeService productBadgeService;

	@Inject
	IEntityMapService entityMapService;

	@Override
	public List<ProductMessage> getProductMessageByListingIDs(
			List<String> listingIDs, WebContext webContext) {
		if (listingIDs == null || listingIDs.size() <= 0) {
			return Lists.newArrayList();
		}
		int languageid = foundationService.getLanguage(webContext);
		Integer websiteId = foundationService.getSiteID(webContext);
		List<ProductMessage> productMessages = productBaseMapper
				.getProductMessagesByListingID(listingIDs, languageid, websiteId);

		return productMessages;
	}

}
