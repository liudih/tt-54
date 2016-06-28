package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductActivityRelationDetailMapper;
import dao.product.IProductActivityDetailEnquiryDao;
import dto.product.ProductActivityRelationDetail;

public class ProductActivityDetailEnquiryDao implements
		IProductActivityDetailEnquiryDao {

	@Inject
	ProductActivityRelationDetailMapper detailMapper;

	@Override
	public List<String> getProductBySpu(String spu,int aid) {
		return detailMapper.getProductBySpu(spu,aid);
	}

	@Override
	public ProductActivityRelationDetail getPriceBySpuAndIid(String spu,
			int activityid) {
		return detailMapper.getPriceBySpuAndIid(spu, activityid);
	}
	
	@Override
	public List<ProductActivityRelationDetail> getAllProductBySpuAndIid(String spu, int activityid){
		return detailMapper.getAllProductBySpuAndIid(spu, activityid);
	}
}
