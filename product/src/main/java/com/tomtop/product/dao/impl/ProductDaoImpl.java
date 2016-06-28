package com.tomtop.product.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IProductDao;
import com.tomtop.product.mappers.product.ProductWebsiteMapper;
import com.tomtop.product.models.dto.ProductBadge;

@Repository("productDao")
public class ProductDaoImpl implements IProductDao {

	@Autowired
	ProductWebsiteMapper productMapper;

	@Override
	public String getListingId(String csku, int websiteid) {
		return this.productMapper.getListingId(csku, websiteid);
	}

	@Override
	public ProductBadge getProductBadgeByListingId(String listingId,
			int languageid, int iwebsiteid) {
		// TODO Auto-generated method stub
		return productMapper.getProductBadgeByListingId(listingId, languageid,
				iwebsiteid);
	}

	@Override
	public List<ProductBadge> getProductBadgeListByListingIds(
			List<String> listingId, int languageid, int iwebsiteid, int istatus) {
		// TODO Auto-generated method stub
		return listingId != null && listingId.size() > 0 ? productMapper
				.getProductBadgeListByListingIds(listingId, languageid,
						iwebsiteid, istatus) : Arrays.asList();
	}

}
