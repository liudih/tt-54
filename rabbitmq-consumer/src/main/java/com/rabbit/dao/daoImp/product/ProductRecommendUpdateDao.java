package com.rabbit.dao.daoImp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductRecommendMapper;
import com.rabbit.dao.idao.product.IProductRecommendUpdateDao;
@Component
public class ProductRecommendUpdateDao implements IProductRecommendUpdateDao {

	@Autowired
	ProductRecommendMapper productRecommendMapper;

	@Override
	public int insert(String clistinid, String crecommendlisting,
			String ccreateuser) {
		return this.productRecommendMapper.add(clistinid, crecommendlisting,
				ccreateuser);
	}

}
