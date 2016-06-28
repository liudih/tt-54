package dao.product;

import java.util.Date;
import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductActivityRelation;
import dto.product.ProductBase;

public interface IProductActivityRelationEnquiryDao extends IProductEnquiryDao {

	public List<ProductActivityRelation> getAllProduct();

	public List<ProductActivityRelation> getProductByDate(Date date,int pageSize, int page);

	public int getCount(Date date);

	public List<ProductBase> getProductsWithSameParentSkuByListingId(
			String listingId, Integer websiteId);

	public ProductActivityRelation getProductBySpu(String cparentspu,int aid);
	
	public int updateProductByIid(int iid,boolean status);
	
	public int updateProductBySpuAndIid(Date fromdate,Date todate,String spu,int iid);
	
	public int getIidByListingidAndSpu(String listingid,String spu);
}
