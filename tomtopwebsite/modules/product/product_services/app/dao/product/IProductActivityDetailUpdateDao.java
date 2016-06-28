package dao.product;

import java.util.Map;

import dao.IProductUpdateDao;

public interface IProductActivityDetailUpdateDao extends IProductUpdateDao{
	public int addProductRelationDetail(Map<String, Object> param);
	
	public int updateProductByIidAndListingid(double price,int qty, int aid, String listingid);
	
	public int updatePriceyIidAndListingid(double price, int aid, String listingid);
}
