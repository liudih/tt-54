package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dto.product.ProductLabel;

public interface IProductLabelServices {


	List<String> getListByListingIdsAndType(List<String> listingIds, String type);


	List<ProductLabel> getProductLabel(String clistingid);
}
