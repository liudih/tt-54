package dao.product;

import dao.IProductEnquiryDao;
import dto.product.ProductInterceptUrl;

public interface IProductInterceptUrlEnquiryDao extends IProductEnquiryDao {
	public ProductInterceptUrl getProductInterceptUrlByUrl(String url,int languageid);
	
	public ProductInterceptUrl getUrlByLanuageidAndListingid(int language,String listingid);
	
	public ProductInterceptUrl getProductBySkuAndLanguage(String sku,int languageid);

}
