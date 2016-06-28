package services.mobile.home;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import mapper.product.ProductCategoryMapperMapper;

import org.apache.commons.lang3.StringUtils;

import services.advertising.AdvertisingService;
import valueobjects.product.ProductAdertisingContext;

import com.google.common.collect.Lists;

import dto.advertising.Advertising;
import dto.mobile.AdvertisingBaseInfo;
import dto.product.ProductBase;
import dto.product.ProductCategoryMapper;

public class AdService {

	@Inject
	AdvertisingService advertisingService;

	@Inject
	ProductCategoryMapperMapper productCategoryMapper;

	@Inject
	ProductBaseMapper productBaseMapper;

	public List<AdvertisingBaseInfo> getAdvertising(
			ProductAdertisingContext context) {
		// 根据传递的语言、站点查找广告，如果 没找到，传递默认的 英语查找
		// 首先判断类型 ，如果是product,根据产品 ID找到品类ID，一级一级往上找，如果没找到，则修改为默认语言去找
		List<Advertising> advertisingList = advertisingService
				.getAdvertisingByContext(context);
		if (null != advertisingList && advertisingList.size() > 0) {
			return convertList(advertisingList);
		}

		String sku = context.getBusinessId();
		List<ProductBase> pbases = productBaseMapper
				.getProductBaseBySkuAndWebsiteId(sku, context.getWebsiteId());
		if (null == pbases || pbases.size() <= 0) {
			return null;
		}
		// 递归三次根据产品品类去找相应的广告，去找 如果没找到，则修改为默认语言、产品类型、产品SKU去找
		// ~ 品类相同，取其中一个品类的广告就可以了
		ProductBase pbase = pbases.get(0);
		List<ProductCategoryMapper> productCategoryList = productCategoryMapper
				.selectByListingId(pbase.getClistingid());
		if (productCategoryList != null && productCategoryList.size() > 0) {
			// for (ProductCategoryMapper pca : productCategoryList) {
			for (int i = productCategoryList.size() - 1; i >= 0; i--) {
				ProductAdertisingContext tcontext = new ProductAdertisingContext(
						String.valueOf(productCategoryList.get(i)
								.getIcategory()), 2, context.getWebsiteId(),
						context.getLanguageId(), context.getPositonId(),
						context.getDevice());
				advertisingList = advertisingService
						.getAdvertisingByContext(tcontext);
				if (null != advertisingList && advertisingList.size() > 0) {
					return convertList(advertisingList);
				}
			}

		}
		if (context.getLanguageId() == 1) {
			return null;
		}
		ProductAdertisingContext tcontext = new ProductAdertisingContext(
				context.getBusinessId(), context.getAdvertisingType(),
				context.getWebsiteId(), 1, context.getPositonId(),
				context.getDevice());
		advertisingList = advertisingService.getAdvertisingByContext(tcontext);
		return convertList(advertisingList);
	}

	private List<AdvertisingBaseInfo> convertList(
			List<Advertising> advertisingList) {
		return Lists.transform(advertisingList,
				(advertising) -> convertAdvertising(advertising));
	}

	private AdvertisingBaseInfo convertAdvertising(Advertising advertising) {
		AdvertisingBaseInfo advertisingBaseInfo = new AdvertisingBaseInfo();
		advertisingBaseInfo.setImgurl(advertising.getCimageurl());
		advertisingBaseInfo.setSit(advertising.getIposition());
		String chrefurl = advertising.getChrefurl();
		String itype = "0";
		String skip = "";
		if (StringUtils.isNotBlank(chrefurl)) {
			itype = chrefurl.substring(0, 1);
			skip = chrefurl.substring(2);
		}
		advertisingBaseInfo.setType(itype);
		advertisingBaseInfo.setSkip(skip);
		return advertisingBaseInfo;
	}
}
