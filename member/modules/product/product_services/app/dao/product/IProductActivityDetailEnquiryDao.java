package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductActivityRelationDetail;

public interface IProductActivityDetailEnquiryDao extends IProductEnquiryDao {

	public List<String> getProductBySpu(String spu,int aid);
	
	public ProductActivityRelationDetail getPriceBySpuAndIid(String spu,int activityid);
	
	public List<ProductActivityRelationDetail> getAllProductBySpuAndIid(String spu, int activityid);

}
