package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductViewCountMapper;
import com.rabbit.dao.idao.product.IProductViewCountEnquiryDao;
import com.rabbit.dto.valueobjects.product.ProductViewCount;
@Component
public class ProductViewCountEnquiryDao implements IProductViewCountEnquiryDao {

	@Autowired
	ProductViewCountMapper productViewCountMapper;
	
	@Override
	public List<ProductViewCount> getViewCountListByListingIds(List<String> listingIds) {
		 
		return this.productViewCountMapper.getViewCountListByListingIds(listingIds);
	}

}
