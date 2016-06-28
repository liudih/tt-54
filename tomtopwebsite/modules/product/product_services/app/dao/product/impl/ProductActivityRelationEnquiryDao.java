package dao.product.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductActivityRelationMapper;
import dao.product.IProductActivityRelationEnquiryDao;
import dto.product.ProductActivityRelation;
import dto.product.ProductBase;

public class ProductActivityRelationEnquiryDao implements
		IProductActivityRelationEnquiryDao {

	@Inject
	ProductActivityRelationMapper activityRelationMapper;

	@Override
	public List<ProductActivityRelation> getAllProduct() {
		return activityRelationMapper.getAllProduct();
	}

	@Override
	public List<ProductActivityRelation> getProductByDate(Date date,int pageSize, int page) {
		return activityRelationMapper.getProductByDate(date,pageSize, page);
	}

	@Override
	public int getCount(Date date) {
		return activityRelationMapper.getCount(date);
	}

	@Override
	public List<ProductBase> getProductsWithSameParentSkuByListingId(
			String listingId, Integer websiteId) {
		return activityRelationMapper.getProductsWithSameParentSkuByListingId(
				listingId, websiteId);
	}

	@Override
	public ProductActivityRelation getProductBySpu(String cparentspu,int aid) {
		return activityRelationMapper.getProductBySpu(cparentspu,aid);
	}
	
	@Override
	public int updateProductByIid(int iid,boolean status){
		return activityRelationMapper.updateProductByIid(iid,status);
	}
	
	@Override
	public int updateProductBySpuAndIid(Date fromdate,Date todate,String spu,int iid){
		return activityRelationMapper.updateProductBySpuAndIid(fromdate, todate, spu, iid);
	}

	@Override
	public int getIidByListingidAndSpu(String listingid, String spu) {
		return activityRelationMapper.getIidByListingidAndSpu(listingid, spu);
	}

}
