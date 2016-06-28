package services.mobile.personal;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import services.interaction.ICollectService;
import services.mobile.MobileService;
import services.product.IProductEnquiryService;
import dto.interaction.ProductCollect;
import dto.product.ProductBase;

public class MyWishService {

	@Inject
	ICollectService collectService;

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	MobileService mobileService;

	public boolean addWishProduct(String gid, String email) {
		if (StringUtils.isNotBlank(gid) && StringUtils.isNotBlank(email)) {
			ProductBase product = productEnquiryService
					.getProductByListingIdAndLanguageWithdoutDesc(gid,
							mobileService.getLanguageID());
			List<ProductCollect> collects = collectService.getCollectByMember(
					gid, email);
			if (product != null && collects != null && collects.size() < 1) {
				return collectService.addCollect(gid, email);
			}
		}
		return false;
	}

	public boolean deleteWishs(List<String> gids, String email) {
		if (StringUtils.isNotBlank(email) && gids != null && gids.size() > 0) {
			StringBuffer sbuffer = new StringBuffer(gids.get(0));
			for (int i = 1; i < gids.size(); i++) {
				sbuffer.append(",").append(gids.get(i));
			}
			return collectService.delCollectByListingids(sbuffer.toString(),
					email);
		}
		return false;
	}
}
