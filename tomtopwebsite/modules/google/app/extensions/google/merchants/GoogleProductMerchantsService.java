package extensions.google.merchants;

import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import dto.product.ProductBase;
import dto.product.google.category.GoogleMerchantsProductDto;
import dto.product.google.category.MerchantsProductDto;
import extensions.product.IProductMerchantsService;


public class GoogleProductMerchantsService extends BaseMerchantsProduct implements IProductMerchantsService {
	@Inject	
	ProductMerchantsManager productMerchantsManager;

	@Override
	public ProductBase getProductById(String code) {
		return productMerchantsManager.getProduct(code);
	}

	@Override
	public void insertProduct(Map<String, MerchantsProductDto> MerchantsProductDtoMap,List baseProductList) {
		 productMerchantsManager.insertProduct( MerchantsProductDtoMap, baseProductList);
	}

	@Override
	public String getCodeForRefreshToken() {
		return productMerchantsManager.getCodeForRefreshToken();
	}

	@Override
	public String getRefreshToken(String code) {
		 return productMerchantsManager.getRefreshToken(code);
	}

	@Override
	public List<MerchantsProductDto> getMerchantsProductList(Map<String,String> switchParam) {
		return productMerchantsManager.pullMerchantsProductList(switchParam);
	}

	@Override
	public void updateMerchantsProductList(
			List<MerchantsProductDto> merchantsProductList) {
		 productMerchantsManager.pushMerchantsProductList(merchantsProductList);		
	}

	@Override
	public GoogleMerchantsProductDto getGoogleProductMap(String nodeData) {
		return productMerchantsManager.getGoogleProductMap(nodeData);
	}


	@Override
	public List<MerchantsProductDto> deleteProductBatch(
			List<MerchantsProductDto> merchantsProductList) {
		return productMerchantsManager.deleteProductBatch(merchantsProductList);
	}
	@Override
	public List<MerchantsProductDto> pullMerchantsProductByIdList(List<String> productIdList){
		return productMerchantsManager.pullMerchantsProductByIdList(productIdList);
	}
	@Override
	public void updateProductPriceAndAvailability(List<MerchantsProductDto> list){
		 productMerchantsManager.updateProductPriceAndAvailability(list);
	}
	
}