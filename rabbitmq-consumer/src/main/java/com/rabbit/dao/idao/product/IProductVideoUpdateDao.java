package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductUpdateDao;
import com.rabbit.dto.product.ProductVideo;

public interface IProductVideoUpdateDao extends IProductUpdateDao {

	public int addProductVideoList(List<ProductVideo> list);
	
	public int deleteByListingId(String listingId);
}
