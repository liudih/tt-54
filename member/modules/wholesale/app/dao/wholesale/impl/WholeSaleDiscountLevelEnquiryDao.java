package dao.wholesale.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.wholesale.WholeSaleDiscountLevelMapper;
import dao.wholesale.IWholeSaleDiscountLevelEnquiryDao;
import entity.wholesale.WholeSaleDiscountLevel;

public class WholeSaleDiscountLevelEnquiryDao implements
		IWholeSaleDiscountLevelEnquiryDao {
	@Inject
	WholeSaleDiscountLevelMapper mapper;

	@Override
	public WholeSaleDiscountLevel getByID(int id) {
		return mapper.getByID(id);
	}

	@Override
	public WholeSaleDiscountLevel getBySiteAndPrice(int siteID, double price) {
		return mapper.getBySiteAndPrice(siteID, price);
	}

	@Override
	public List<WholeSaleDiscountLevel> getWholeSaleDiscountLevelByWebSiteId(
			int WebSiteId) {
		return mapper.getWholeSaleDiscountLevelByWebSiteId(WebSiteId);
	}

	@Override
	public List<WholeSaleDiscountLevel> getBySite(int siteID) {
		return mapper.getBySite(siteID);
	}

}
