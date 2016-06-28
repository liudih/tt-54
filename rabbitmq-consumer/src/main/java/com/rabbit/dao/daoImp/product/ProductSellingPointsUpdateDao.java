package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductSellingPointsMapper;
import com.rabbit.dao.idao.product.IProductSellingPointsUpdateDao;
import com.rabbit.dto.product.ProductSellingPoints;
@Component
public class ProductSellingPointsUpdateDao implements
		IProductSellingPointsUpdateDao {

	@Autowired
	ProductSellingPointsMapper productSellingPointsMapper;

	@Override
	public int addBatch(List<ProductSellingPoints> list) {
		return this.productSellingPointsMapper.addBatch(list);
	}

	@Override
	public int deleteByListingId(String listingId) {
		return this.productSellingPointsMapper.deleteByListingId(listingId);
	}

	@Override
	public int deleteByListingIdAndLanguageId(String listingId, int languageId) {
		return this.productSellingPointsMapper.deleteByListingIdAndLanguage(
				listingId, languageId);
	}

}
