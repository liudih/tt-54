package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductUpdateDao;
import com.rabbit.dto.product.ProductUrl;

public interface IProductUrlUpdateDao extends IProductUpdateDao {
	public int addProductUrl(ProductUrl record);

	public int addProductUrlList(List<ProductUrl> list);

	public int deleteByListingId(String listingId);
}
