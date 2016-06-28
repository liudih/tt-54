package services.product.google.feeds.impl;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import mapper.product.MerchantsProductMapper;
import mapper.google.category.GoogleFeedsMapper;
import mapper.product.MerchantsProductMapper;
import services.product.google.feeds.IGoogleFeedsBaseService;
import valueobjects.base.Page;
import dto.product.google.category.GoogleFeedsBase;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchMerchantsProductDto;

public class GoogleFeedsBaseService implements IGoogleFeedsBaseService {

	@Inject
	GoogleFeedsMapper feedsMapper;
	@Inject
	MerchantsProductMapper merchantsProductMapper;
	@Override
	public Page<GoogleFeedsBase> getAllFeeds(int p, int pageSize,
			GoogleFeedsBase feedsBase) {
		List<GoogleFeedsBase> fList = feedsMapper.getAllFeeds(p, pageSize,
				feedsBase);
		int total = feedsMapper.getCount();
		return new Page<GoogleFeedsBase>(fList, total, p, pageSize);
	}

	@Override
	public int getCount() {
		return feedsMapper.getCount();
	}

	@Override
	public int addFeeds(GoogleFeedsBase feedsBase) {
		return feedsMapper.addFeeds(feedsBase);
	}

	@Override
	public int deleteFeedsById(int id) {
		return feedsMapper.delFeedsById(id);
	}
	@Override
	public List<MerchantsProductDto> pushProductMerchants(){
		return feedsMapper.pushProductMerchants();
	}

	@Override
	public int searchCountProductMerchants(SearchMerchantsProductDto searchDto) {
		return feedsMapper.searchCountProductMerchants(searchDto);
	}

	@Override
	public List<MerchantsProductDto> searchProductMerchants(int page,
			int pageSize, SearchMerchantsProductDto searchDto) {
		return feedsMapper.searchProductMerchants(page, pageSize, searchDto);
	}

	@Override
	public MerchantsProductDto queryProductMerchants(String nodeId) {
		return merchantsProductMapper.queryProductMerchants(nodeId);
	}
}
