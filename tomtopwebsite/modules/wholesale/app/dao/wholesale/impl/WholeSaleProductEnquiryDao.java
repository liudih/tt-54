package dao.wholesale.impl;

import java.util.List;

import mapper.wholesale.WholeSaleProductMapper;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleProductEnquiryDao;
import entity.wholesale.WholeSaleProduct;

public class WholeSaleProductEnquiryDao implements IWholeSaleProductEnquiryDao {
	@Inject
	WholeSaleProductMapper mapper;

	@Override
	public List<WholeSaleProduct> getWholeSaleProductsByEmail(String email,
			Integer websiteId) {
		return mapper.getWholeSaleProductsByEmail(email, websiteId);
	}

	@Override
	public WholeSaleProduct getWholeSaleProductsByEmailAndSkuAndWebsite(
			String email, Integer websiteId, String sku) {
		return mapper.getWholeSaleProductsByEmailAndSkuAndWebsite(email,
				websiteId, sku);
	}

	@Override
	public List<WholeSaleProduct> getByIDs(List<Integer> ids) {
		return mapper.getByIDs(ids);
	}
}
