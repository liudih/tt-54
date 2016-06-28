package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.CategoryBaseMapper;
import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductCategoryRankMapper;
import services.product.IProductBadgeService;
import services.product.IProductFragmentProvider;
import valueobjects.product.CategoryRankProduct;
import valueobjects.product.CategoryRankProductList;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductContext;

import com.google.common.collect.Lists;

public class ProductCategoryRankFragmentProvider implements
		IProductFragmentProvider {

	public static final String NAME = "rank";

	@Inject
	ProductCategoryRankMapper productCategoryRankMapper;

	@Inject
	ProductCategoryMapperMapper productCategoryMapperMapper;

	@Inject
	CategoryBaseMapper categoryBaseMapper;

	@Inject
	IProductBadgeService badgeService;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		Integer categoryBaseId = categoryBaseMapper
				.getLowestCategoryBase(context.getListingID());
		if (null == categoryBaseId) {
			return new CategoryRankProductList(Lists.newArrayList());
		}
		List<CategoryRankProduct> products = productCategoryRankMapper.select(
				categoryBaseId, 5);
		List<String> listingIds = Lists.transform(products,
				p -> p.getListingId());
		List<ProductBadge> badges = badgeService.getProductBadgesByListingIDs(
				listingIds, context.getLang(), context.getSiteID(),
				context.getCurrency(),null);
		return new CategoryRankProductList(badges);
	}

}
