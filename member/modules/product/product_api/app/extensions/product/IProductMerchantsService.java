package extensions.product;

import java.util.List;
import java.util.Map;

import dto.product.ProductBase;
import dto.product.google.category.GoogleMerchantsProductDto;
import dto.product.google.category.MerchantsProductDto;

public interface IProductMerchantsService {
	
	public String getCodeForRefreshToken();
	
	public String getRefreshToken(String code);
	
	public ProductBase getProductById(String code) ;
	
	public void insertProduct(Map<String, MerchantsProductDto> MerchantsProductDtoMap,List baseProductList);
	
	public List<MerchantsProductDto> getMerchantsProductList(Map<String,String> switchParam );
	
	public void updateMerchantsProductList(List<MerchantsProductDto> merchantsProductList);
	
	public GoogleMerchantsProductDto getGoogleProductMap(String nodeData);
	
	public List<MerchantsProductDto> deleteProductBatch(List<MerchantsProductDto> merchantsProductList);
}
