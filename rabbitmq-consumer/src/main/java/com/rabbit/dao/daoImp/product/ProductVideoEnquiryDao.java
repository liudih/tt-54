package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductVideoMapper;
import com.rabbit.dao.idao.product.IProductVideoEnquiryDao;
import com.rabbit.dto.product.ProductVideo;
@Component
public class ProductVideoEnquiryDao implements IProductVideoEnquiryDao {

	@Autowired
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
