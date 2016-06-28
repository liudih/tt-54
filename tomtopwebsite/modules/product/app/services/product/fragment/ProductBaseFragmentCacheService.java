package services.product.fragment;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import services.base.FoundationService;
import services.price.PriceService;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductExplainEnquiryDao;
import dao.product.IProductInterceptUrlEnquiryDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductUrlEnquiryDao;
import interceptors.CacheResult;
import dto.product.ProductBase;
import dto.product.ProductExplain;
import dto.product.ProductInterceptUrl;
import dto.product.ProductLabel;
import dto.product.ProductUrl;

public class ProductBaseFragmentCacheService {

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	PriceService priceService;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	@Inject
	IProductExplainEnquiryDao productExplainEnquityDao;

	@Inject
	IProductInterceptUrlEnquiryDao interceptUrlEnquiryDao;

	@Inject
	IProductLabelEnquiryDao productlabeldao;

	@Inject
	IProductBaseEnquiryDao baseenquiry;

	@CacheResult("product.badges")
	public ProductBase getProductBase(String listingid, int lang) {
		return productBaseMapper.getProductBaseByListingIdAndLanguage(
				listingid, lang);
	}

	@CacheResult("product.badges")
	public List<ProductExplain> getProductExplainList(int siteid, int lang) {
		return productExplainEnquityDao.getProductExplainsBySiteAndLan(siteid,
				lang);
	}

}
