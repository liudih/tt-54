package com.rabbit.services.serviceImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.rabbit.conf.mapper.category.CategoryProductRecommendMapper;
import com.rabbit.dto.category.CategoryProductRecommend;
@Service
public class CategoryProductRecommendService {
	
	@Autowired
	CategoryProductRecommendMapper categoryProductRecommendMapper;
	
	public List<CategoryProductRecommend> getProductRecommendBySkus(List<String> skus){
		if(skus==null || skus.size()==0){
			return Lists.newArrayList();
		}
		return categoryProductRecommendMapper.getProductRecommendBySkus(skus);
	}
}
