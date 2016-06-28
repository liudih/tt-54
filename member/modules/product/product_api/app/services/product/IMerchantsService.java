package services.product;

import java.util.List;
import java.util.Map;

import dto.product.google.category.GoogleMerchantsProductDto;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchMerchantsProductDto;

public interface IMerchantsService {
	
	public String  pushProductMerchants() ;

	public String getCodeForRefreshToken();
	
	public String getRefreshToken(String code);
	
	public void pushMerchantsProductList();
	
	public void pullMerchantsProductList();
	
	public GoogleMerchantsProductDto getGoogleProductMap(String nodeData);

	public void autoPublishGoogleProductByCategorys();
	
	List<String> querySkuLimit(int pageSize,int page);
	
	boolean existSku(String sku);
	
	List<SearchMerchantsProductDto> queryExistMerchantProduct(List<String> cnodeIdList);
	
	Map<String,String> manageMerchantProductConfigData(List<String> searchMerchantsProductList);
	
	List<SearchMerchantsProductDto> searchMerchantProductConfigData(SearchMerchantsProductDto searchMerchantsProductDto);
	
	int countMerchantProductConfigData(SearchMerchantsProductDto searchMerchantsProductDto);
	
	List<SearchMerchantsProductDto> queryNoMerchantProductLimit(String sku,String language,String countrycurrency);

	List<SearchMerchantsProductDto> queryNoMerchantProductNoSku(
			String ctargetcountry, String clanguage, int page, int pageSize);

	void deleteMerchantProductConfigData(List<String> skulist);
	
	void deleteGoogleBackRecords(List<Integer> ids);
	
	public String checkSwitchManage(String type,String value);
	
	public String shutDownPullData();
	
	public String shutDownPushData();
	
	public String turnOnPullDataSwitch();
	
	public String turnOnPushDataSwitch();
	
	public String getPullDataSwitch();
	
	public String getPushDataSwitch();
	
	public List<MerchantsProductDto> pushSelectMerchantProduct(List<Integer> ids);
}
