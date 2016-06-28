package services.product.google.feeds;

import java.util.List;

import valueobjects.base.Page;
import dto.product.google.category.GoogleFeedsBase;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchMerchantsProductDto;

public interface IGoogleFeedsBaseService {

	Page<GoogleFeedsBase> getAllFeeds(int p, int pageSize,
			GoogleFeedsBase feedsBase);

	int getCount();

	int addFeeds(GoogleFeedsBase feedsBase);
	
	int deleteFeedsById(int id);
	
	List<MerchantsProductDto> pushProductMerchants();
	
	int searchCountProductMerchants(SearchMerchantsProductDto searchDto);
	
	List<MerchantsProductDto> searchProductMerchants(int page, int pageSize,SearchMerchantsProductDto searchDto);
	
	MerchantsProductDto queryProductMerchants(String nodeId);
}
