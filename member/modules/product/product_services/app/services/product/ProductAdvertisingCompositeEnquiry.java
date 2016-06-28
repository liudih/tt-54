package services.product;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import valueobjects.product.AdItem;
import valueobjects.product.ProductAdertisingContext;
import extensions.product.IProductAdvertisingProvider;

public class ProductAdvertisingCompositeEnquiry {

	@Inject
	Set<IProductAdvertisingProvider> productAdvertisingProviders;

	/**
	 * 返回多个广告 默认显示
	 * 
	 * @param context
	 * @return list(a and img html)
	 */
/*	public List<String> getAdvertisings(ProductAdertisingContext context) {
		if (productAdvertisingProviders != null
				&& productAdvertisingProviders.size() > 0) {
			List<AdItem> list = this.getAdvertisingsItem(context);
			if (list == null) {
				return null;
			}
			List<String> relist = Lists.transform(list,
					obj -> obj.getDefaultShow());
			return relist;
		}
		return null;
	}*/

	/**
	 * 返回多个广告 可以支配显示
	 * 
	 * @param context
	 * @return list(a and img html)
	 */
	public List<AdItem> getAdvertisings(ProductAdertisingContext context) {
		if (productAdvertisingProviders != null
				&& productAdvertisingProviders.size() > 0) {
			return ((IProductAdvertisingProvider) productAdvertisingProviders
					.toArray()[0]).getAdvertisings(context);
		}
		return null;
	}

}
