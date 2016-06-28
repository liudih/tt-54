package com.tomtop.product.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.product.mappers.product.ProductDtlMapper;
import com.tomtop.product.mappers.product.ProductLabelMapper;
import com.tomtop.product.models.bo.ProductHotBo;
import com.tomtop.product.models.dto.ProductBase;
import com.tomtop.product.models.dto.ProductImageUrlDto;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.services.IProductHotService;
import com.tomtop.product.services.price.IPriceService;
import com.tomtop.product.services.price.IProductSpec;
import com.tomtop.product.services.price.impl.SingleProductSpec;
import com.tomtop.product.utils.PriceInterceptUtils;
@Service
public class ProductHotServiceImpl implements IProductHotService {

	@Autowired
	ProductLabelMapper productLabelMapper;
	@Autowired
	ProductDtlMapper productDtlMapper;
	@Autowired
	private IPriceService priceService;
	
	@Resource(name = "priceInterceptUtils")
	private PriceInterceptUtils priceIntercept;
	
	private final static String hot = "hot";
	private final static Integer page = 1;
	private final static Integer pageSize = 5;
	/**
	 * 获取对应type的商品listingId
	 * 
	 * @param siteId
	 * 			站点Id
	 * @param type 
	 * 			hot
	 * @param page 页数 
	 * 
	 * @param pageSize 获取多少条
	 * 
	 * @return List<String>
	 * @author renyy
	 */
	@Override
	public List<String> getProductTypeList(int siteId,String type, int page,
			int pageSize) {
		int startIndex = page*pageSize;
		List<String> listingIds = productLabelMapper.getProductLabelPageByTypeAndWebsite(siteId, type , pageSize, startIndex);
		return listingIds;
	}
	/**
	 * 获取热门商品
	 * 
	 * @param siteId
	 * 			站点Id
	 * @param type 
	 * 			hot
	 * @param page 页数 
	 * 
	 * @param pageSize 获取多少条
	 * 
	 * @return List<String>
	 * @author renyy
	 */
	@Override
	public List<ProductHotBo> getProductHotBoList(Integer langId, Integer siteId,String currency) {
		List<ProductHotBo> photboList = new ArrayList<ProductHotBo>();
		List<String> listingIds = getProductTypeList(siteId, hot, page, pageSize);
		if(listingIds == null || listingIds.size() == 0){
			return photboList;
		}
		List<ProductBase> pbList = productDtlMapper.getProductBaseByListingIds(listingIds, langId, siteId);
		
		//获取商品的价格
		List<IProductSpec> spsList = new ArrayList<IProductSpec>();
		SingleProductSpec sps = null;
		for (int i = 0; i < listingIds.size(); i++) {
			sps = new SingleProductSpec(listingIds.get(i), 1);
			spsList.add(sps);
		}
		PriceCalculationContext ctx = new PriceCalculationContext(currency);
		List<Price> priceList = priceService.getPrice(spsList, ctx);
		Map<String, Price> prices = new HashMap<String, Price>();
		Price pr = null;
		for (int i = 0; i < priceList.size(); i++) {
			pr = priceList.get(i);
			prices.put(pr.getListingId(),pr);
		}
		//获取商品的图片
		List<ProductImageUrlDto> imgUrl = productDtlMapper
				.getProductImageUrlDtoByListingIdsIsMain(listingIds, langId);
		Map<String, String> imgMap = new HashMap<String, String>();
		if (imgUrl != null && imgUrl.size() > 0) {
			for (int i = 0; i < imgUrl.size(); i++) {
				imgMap.put(imgUrl.get(i).getClistingid(), imgUrl.get(i).getCimageurl());
			}
		}
		
		ProductBase pba = null;
		ProductHotBo phbo = null;
		String listingId = "";
		String sku = "";
		String title = "";
		//遍历基本信息
		for (int j = 0; j < pbList.size(); j++) {
			pba = pbList.get(j);
			listingId = pba.getClistingid();
			sku = pba.getCsku();
			title = pba.getCtitle();
			phbo = new ProductHotBo();
			phbo.setListingId(listingId);
			phbo.setSku(sku);
			phbo.setTitle(title);
			phbo.setImgUrl(imgMap.get(listingId));
			pr = prices.get(listingId);
			phbo.setNowprice(priceIntercept.money(pr.getPrice(), currency));
			phbo.setOrigprice(priceIntercept.money(pr.getUnitBasePrice(),currency));
			photboList.add(phbo);
		}
		return photboList;
	}

}
