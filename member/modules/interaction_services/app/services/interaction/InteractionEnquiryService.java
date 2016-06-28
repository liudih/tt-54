package services.interaction;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import mapper.interaction.ProductBrowseMapper;
import play.Logger;
import play.libs.Json;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.product.IProductUpdateService;
import services.product.ProductEnquiryService;
import services.search.criteria.ProductLabelType;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import dao.interaction.IBrowseEnquiryDao;
import dao.product.impl.ProductRecommendEnquiryDao;
import dto.interaction.ListingCount;
import dto.product.ProductLabel;
import events.search.ProductIndexingRequestEvent;

public class InteractionEnquiryService {
	@Inject
	ProductBrowseMapper productBrowseMapper;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	FoundationService foundationService;

	@Inject
	IProductUpdateService productUpdateService;

	@Inject
	IBrowseEnquiryDao browseEnquityDao;

	@Inject
	SystemParameterService systemParameterService;

	@Inject
	ProductRecommendEnquiryDao productRecommendEnquiryDao;

	@Inject
	EventBus eventBus;

	private List<ListingCount> getListingidfor(List<String> listingIds) {
		if(listingIds==null || listingIds.size()==0){
			return Lists.newArrayList();
		}
		int total = listingIds.size();
		int batchSize = 200;
		int totalPages = total / batchSize ;
		Set<Integer> pageset = ContiguousSet.create(
				Range.closed(0, totalPages), DiscreteDomain.integers());
		Stream<List<ListingCount>> dbresultlist = pageset.parallelStream().map(
				page -> {
					int fromindex = (batchSize * (page));
					int toindex = fromindex + batchSize;
					if (toindex > listingIds.size())
						toindex = listingIds.size();
					Logger.debug("featrue page-> {}  from {}  to {}", page,
							fromindex, toindex);
					List<String> newlist = listingIds.subList(fromindex,
							toindex);
					List<ListingCount> listorderbyBrowse = productBrowseMapper
							.getTop100Browse(newlist);
					return listorderbyBrowse;
				});
		List<ListingCount> resultlist = Lists.newArrayList();
		dbresultlist.forEach(p -> {
			resultlist.addAll(p);
		});
		List<ListingCount> resultsortlist = com.google.common.collect.FluentIterable
				.from(resultlist).toSortedList(
						(p1, p2) -> p2.getCount().compareTo(p1.getCount()));
		int subcount = 100;
		if (resultsortlist.size() > subcount) {
			resultsortlist = resultsortlist.subList(0, subcount);
		}
		Logger.debug("get getfreatrue Listingidfor rows - > {}",
				resultlist.size());
		return resultsortlist;
	}

	public void selectFeatured() {
		try {
			List<String> listinglist = productEnquiryService.getSaleListings();
			List<ListingCount> resultsortlist = getListingidfor(listinglist);
			for (ListingCount listingid : resultsortlist) {
				ProductLabel productLabel = new ProductLabel();
				productLabel.setCtype(ProductLabelType.Featured.toString());
				productLabel.setClistingid(listingid.getClistingid());
				productLabel.setCcreateuser("auto");
				productLabel.setIwebsiteid(foundationService.getSiteID());
				productUpdateService.insertProductLabel(productLabel);
			}
			eventBus.post(new ProductIndexingRequestEvent(Lists.transform(
					resultsortlist, p -> p.getClistingid())));
		} catch (Exception ex) {
			Logger.error("featrue error", ex);
		}
	}

	public void getRecommendByBrowse() {
		String dropShipingListing = systemParameterService.getSystemParameter(
				1, 1, "DropShipping", "0092bcc8-d914-1004-874c-d371c9ab96c0");
		List<String> listings = productEnquiryService.getLisingsByStatus(1);
		int deleteCount = productRecommendEnquiryDao
				.deleteAllRecommendProduct();
		Logger.debug("======delete  recomend product count is {} ", deleteCount);
		int count = 0;
		for (String listing : listings) {
			List<String> recommendListings = browseEnquityDao
					.getBrowseListingsByListing(listing);
			for (String recommendListing : recommendListings) {
				if (!dropShipingListing.equals(recommendListing)) {
					int result = productUpdateService.insertProductRecommend(
							listing, recommendListing, "Auto");
					if (result > 0) {
						count++;
					}
				}
			}
		}
		Logger.debug("======insert recomend product count is {} ", count);
	}
}
