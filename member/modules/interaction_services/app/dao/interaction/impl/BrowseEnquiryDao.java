package dao.interaction.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import mapper.interaction.ProductBrowseMapper;

import com.google.inject.Inject;

import dao.interaction.IBrowseEnquiryDao;
import dto.TopBrowseAndSaleCount;
import dto.interaction.ListingCount;

public class BrowseEnquiryDao implements IBrowseEnquiryDao {

	@Inject
	ProductBrowseMapper productBrowseMapper;

	@Override
	public List<String> getBrowseListingsByListing(String listing) {
		return productBrowseMapper.getBrowseListingsByListing(listing);
	}

	@Override
	public String getLastViewListingIdBySiteIdAndLtc(int siteId, String ltc) {
		return productBrowseMapper.getLastBrowseListingIdBySiteIdAndLtc(siteId,
				ltc);
	}

	@Override
	public List<String> getShowCltcByListing(String listing) {
		return productBrowseMapper.getShowCltcByListing(listing);
	}

	@Override
	public List<ListingCount> getListingAndCountByCltcs(List<String> cltcs,int n) {
		return productBrowseMapper.getListingAndCountByCltcs(cltcs,n);
	}

	@Override
	public List<TopBrowseAndSaleCount> getTopBrowseByTimeRange(Integer timeRange, List<String> listingIds) {
		return productBrowseMapper.getTopBrowseByTimeRange(timeRange,listingIds);
	}
}
