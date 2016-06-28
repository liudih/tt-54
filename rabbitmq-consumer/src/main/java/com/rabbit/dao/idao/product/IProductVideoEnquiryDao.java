package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductEnquiryDao;
import com.rabbit.dto.product.ProductVideo;

public interface IProductVideoEnquiryDao extends IProductEnquiryDao {

	public List<ProductVideo> getVideosBylistId(String clistingid);

	public List<ProductVideo> getVideoBylistingIds(List<String> listingIds);
}
