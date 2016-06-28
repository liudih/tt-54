package services.product;

import java.util.List;

import javax.inject.Inject;

import mapper.category.CategoryProductRecommendMapper;

import com.google.common.collect.Lists;

import dto.category.CategoryProductRecommend;

public class CategoryProductRecommendService {
	
	@Inject
	CategoryProductRecommendMapper categoryProductRecommendMapper;
	
	public List<CategoryProductRecommend> getProductRecommendBySkus(List<String> skus){
		if(skus==null || skus.size()==0){
			return Lists.newArrayList();
		}
		return categoryProductRecommendMapper.getProductRecommendBySkus(skus);
	}
}
