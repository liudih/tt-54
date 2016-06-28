package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductVideoMapper;
import dao.product.IProductVideoEnquiryDao;
import dto.product.ProductVideo;

public class ProductVideoEnquiryDao implements IProductVideoEnquiryDao {

	@Inject
	ProductVideoMapper productVideoMapper;

	@Override
	public List<ProductVideo> getVideosBylistId(String clistingid) {

		return this.productVideoMapper.getVideosBylistId(clistingid);
	}

	@Override
	public List<ProductVideo> getVideoBylistingIds(List<String> listingIds) {

		return this.productVideoMapper.getVideoBylistingIds(listingIds);
	}

}
