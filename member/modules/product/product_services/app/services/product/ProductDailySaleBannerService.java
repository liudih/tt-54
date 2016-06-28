package services.product;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import services.base.utils.DateFormatUtils;
import services.common.UUIDGenerator;
import services.price.PriceService;
import services.search.ISearchService;
import services.search.SearchContextFactory;
import services.search.filter.DiscountDateFilter;
import services.search.sort.DiscountSortOrder;
import valueobjects.base.Page;
import valueobjects.price.Price;
import valueobjects.product.ProductBadge;

import com.google.common.collect.Range;
import com.google.common.collect.Sets;

import valueobjects.search.SearchContext;

public class ProductDailySaleBannerService {

	@Inject
	ProductBaseMapper productBaseMapper;
	@Inject
	PriceService priceService;
	@Inject
	SearchContextFactory searchFactory;
	@Inject
	ISearchService genericSearch;
	@Inject
	IProductBadgeService productBadgeService;

	public ProductBadge getProductSalMaxSalePriceByDay(int site, int lang,
			String ccy) {

		List<Date> dlist = DateFormatUtils.getNowDayRange(0);
		DiscountDateFilter c = new DiscountDateFilter(Range.closed(
				dlist.get(0), dlist.get(1)));
		SearchContext context = new SearchContext();
		context.setId(UUIDGenerator.createAsString());
		context.getFilter().add(c);
		context.getSort().add(new DiscountSortOrder(false));
		context.setPageSize(1);
		Page<String> listingids = genericSearch.search(context, site, lang);
		if (listingids.getList().size() == 0) {
			return null;
		}
		String clistingid = listingids.getList().get(0);

		List<ProductBadge> badge = productBadgeService.getDBProductBadgesByListingIDs(Sets.newHashSet(clistingid), lang);
		Price p = priceService.getPrice(clistingid, ccy);
		ProductBadge b = null;
		if (badge.size() > 0) {
			b = badge.get(0);
			b.setPrice(p);
		}
		return b;
	}
}
