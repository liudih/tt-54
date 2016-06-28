package services.mobile.personal;

import java.util.Date;
import java.util.List;

import mapper.interaction.ProductCollectMapper;
import mapper.product.ProductBaseMapper;

import org.apache.commons.lang3.StringUtils;

import services.mobile.MobileService;

import com.google.inject.Inject;

import dto.interaction.ProductCollect;
import dto.product.ProductBase;

public class MyWishService {

	@Inject
	ProductCollectMapper productCollectMapper;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	MobileService mobileService;

	public boolean addWishProduct(String gid, String email) {
		if (StringUtils.isNotBlank(gid) && StringUtils.isNotBlank(email)) {
			ProductBase product = productBaseMapper
					.getProductBaseByListingIdAndLanguage(gid,
							mobileService.getLanguageID());
			List<ProductCollect> collects = productCollectMapper
					.getCollectByMember(gid, email);
			if (product != null && collects != null && collects.size() < 1) {
				ProductCollect record = new ProductCollect();
				record.setCemail(email);
				record.setClistingid(gid);
				record.setDcreatedate(new Date());
				int result = productCollectMapper.insertSelective(record);
				if (result > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean deleteWishs(List<String> gids, String email) {
		if (StringUtils.isNotBlank(email) && gids != null && gids.size() > 0) {
			int result = productCollectMapper.delCollectByListingids(gids,
					email);
			if (result > 0) {
				return true;
			}
		}
		return false;
	}
}
