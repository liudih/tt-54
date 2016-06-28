package service.advertising.fragment;

import java.util.List;

import javax.inject.Inject;

import mapper.product.CategoryWebsiteMapper;
import mapper.product.ProductBaseMapper;
import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductUrlMapper;
import services.IAdvertisingService;
import services.advertising.AdvertisingService;
import services.base.FoundationService;
import valueobjects.product.AdItem;
import valueobjects.product.ProductAdertisingContext;

import com.google.common.collect.Lists;

import dto.advertising.Advertising;
import dto.advertising.ProductAdertisingContextExtended;
import dto.product.ProductBase;
import dto.product.ProductCategoryMapper;
import extensions.product.IProductAdvertisingProvider;

public class AdvertisingProvider implements IProductAdvertisingProvider,
		IAdvertisingService {

	@Inject
	AdvertisingService advertisingService;

	@Inject
	ProductUrlMapper productUrlMapper;

	@Inject
	ProductCategoryMapperMapper productCategoryMapper;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	CategoryWebsiteMapper categoryWebsiteMapper;

	@Inject
	FoundationService foundation;

	@Override
	public List<AdItem> getAdvertisings(ProductAdertisingContext context) {
		if (context.getAdvertisingType() == 1) {
			return this.convertValue(this.getProductAdvertising(context));
		} else {
			List<Advertising> advertisingList = advertisingService
					.getAdvertisingByContext(context);
			if (null != advertisingList && advertisingList.size() > 0) {
				return this.convertValue(advertisingList);

			}
			if (context.getLanguageId() == 1) {
				return null;
			}
			ProductAdertisingContext tcontext = new ProductAdertisingContext(
					context.getBusinessId(), context.getAdvertisingType(),
					context.getWebsiteId(), 1, context.getPositonId(),
					context.getDevice());
			advertisingList = advertisingService
					.getAdvertisingByContext(tcontext);
			return this.convertValue(advertisingList);
		}

	}

	private List<AdItem> convertValue(List<Advertising> advertisingList) {
		if (null == advertisingList) {
			return null;
		}
		return Lists.transform(advertisingList, ab -> {
//			 private String cbgimageurl;
//		    private String cbgcolor;
//		    private boolean bbgimgtile; //背景图是否平铺
//		    private boolean bhasbgimage;
			String hrefUrl = ab.getChrefurl();
			String title = ab.getCtitle();
			String imageUrl = ab.getCimageurl();
			
			 
			// 需要判断是图片库里面的图片，添加前缀，否则还是直接写的路径
				if (imageUrl.startsWith("advertising/image")) {
					imageUrl = "/img/" + imageUrl;
				}

				String advertisingStr = "<a href=\"" + hrefUrl
						+ "\"><img title=\"" + title + "\" src=\"" + imageUrl
						+ "\"></a>";
				return new AdItem(title, imageUrl, hrefUrl, advertisingStr,
						ab.getCbgimageurl(), ab.getCbgcolor(), ab.isBbgimgtile());
				

				// return advertisingStr;
			});
	}

	@Override
	public List<Advertising> getProductAdvertising(
			ProductAdertisingContext context) {
		// 根据传递的语言、站点查找广告，如果 没找到，传递默认的 英语查找
		// 首先判断类型 ，如果是product,根据产品 ID找到品类ID，一级一级往上找，如果没找到，则修改为默认语言去找
		List<Advertising> advertisingList = advertisingService
				.getAdvertisingByContext(context);
		if (null != advertisingList && advertisingList.size() > 0) {
			return advertisingList;
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
					return advertisingList;
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
		return advertisingList;
	}

	@Override
	public List<AdItem> getAdvertisingsExtended(
			ProductAdertisingContextExtended context) {

		ProductAdertisingContext ctx = new ProductAdertisingContext(
				context.getBusinessId(), context.getAdvertisingType(),
				foundation.getSiteID(context.getContext()),
				foundation.getLanguage(context.getContext()),
				context.getPositonId(), foundation.getDevice(context
						.getContext()));

		return this.getAdvertisings(ctx);
	}

}
