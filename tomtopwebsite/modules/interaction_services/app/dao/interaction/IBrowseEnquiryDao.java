package dao.interaction;

import java.util.List;

import dto.TopBrowseAndSaleCount;
import dto.interaction.ListingCount;

public interface IBrowseEnquiryDao {
	public List<String> getBrowseListingsByListing(String listing);

	public String getLastViewListingIdBySiteIdAndLtc(int siteId, String ltc);

	public List<String> getShowCltcByListing(String listing);

	public List<ListingCount> getListingAndCountByCltcs(List<String> cltcs,int n);

	public List<TopBrowseAndSaleCount> getTopBrowseByTimeRange(Integer timeRange,List<String> listingIds);
}
