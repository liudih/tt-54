package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductImageMapper;
import dao.product.IProductImageDao;
import dto.product.ProductImage;

public class ProductImageDao implements IProductImageDao {

	@Inject
	ProductImageMapper productImageMapper;

	@Override
	public List<ProductImage> getProductImageByListingIds(
			List<String> listingids) {
		return productImageMapper.getImageUrlByClistingid(listingids);
	}

}