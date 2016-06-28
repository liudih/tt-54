package com.rabbit.services.search.serviceImp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.rabbit.conf.mapper.product.ProductCategoryMapperMapper;
import com.rabbit.conf.mapper.product.ProductEntityMapMapper;
import com.rabbit.conf.mapper.product.ProductStorageMapMapper;
import com.rabbit.dao.idao.product.IProductBaseEnquiryDao;
import com.rabbit.dao.idao.product.IProductLabelEnquiryDao;
import com.rabbit.dao.idao.product.IProductSalePriceEnquiryDao;
import com.rabbit.dao.idao.product.IProductViewCountEnquiryDao;
import com.rabbit.dto.category.CategoryProductRecommend;
import com.rabbit.dto.product.ProductCategoryMapper;
import com.rabbit.dto.product.ProductEntityMap;
import com.rabbit.dto.product.ProductLabel;
import com.rabbit.dto.product.ProductSalePrice;
import com.rabbit.dto.product.ProductStorageMap;
import com.rabbit.dto.search.old.ProductIndexingContext_New;
import com.rabbit.dto.search.old.RecommendDoc;
import com.rabbit.dto.transfer.product.MultiProduct;
import com.rabbit.dto.transfer.product.ProductBack;
import com.rabbit.dto.transfer.product.TranslateItem;
import com.rabbit.dto.valueobjects.price.Price;
import com.rabbit.dto.valueobjects.product.ProductBaseTranslate;
import com.rabbit.dto.valueobjects.product.ProductViewCount;
import com.rabbit.dto.valueobjects.product.spec.SingleProductSpec;
import com.rabbit.services.serviceImp.base.WebsiteService;
import com.rabbit.services.serviceImp.price.PriceService;
import com.rabbit.services.serviceImp.product.CategoryEnquiryService;
import com.rabbit.services.serviceImp.product.CategoryProductRecommendService;
import com.rabbit.services.serviceImp.product.ProductBaseTranslateService;

  
@Component("indexService")
public class IndexingService {
	@Autowired
	CategoryEnquiryService categoryEnquiryService;
	@Autowired
	ProductEntityMapMapper attributeMapper;

	@Autowired
	ProductCategoryMapperMapper categoryMapper;

	@Autowired
	CategoryEnquiryService categoryEnquiry;

	@Autowired
	ProductBaseTranslateService productBaseTranslateService;

	@Autowired
	PriceService priceService;

	@Autowired
	WebsiteService websiteEnquiry;

	@Autowired
	IProductViewCountEnquiryDao productViewCountEnquityDao;

	@Autowired
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;

	@Autowired
	IProductLabelEnquiryDao productLabelEnquiryDao;

	@Autowired
	IProductBaseEnquiryDao productBaseDao;

	@Autowired
	ProductStorageMapMapper storageMapper;

	@Autowired
	CategoryProductRecommendService categoryProductRecommendService;
	
	 

	public String getOldElasticsearchJson(ProductBack pt) throws JsonProcessingException, IOException{
		List<TranslateItem> translateItem = pt.getItems();
		if(translateItem == null || translateItem.size() == 0){
			return "";
		}
		TranslateItem obj = translateItem.get(0);
		
		ProductIndexingContext_New pic = new ProductIndexingContext_New();
		pic.setSiteId(pt.getWebsiteId());
		pic.setListingId(pt.getListingId());
		ProductBaseTranslate pBase = new ProductBaseTranslate();
		if (pt instanceof MultiProduct) {
			pBase.setBmultiattribute(true);
		}else{
			pBase.setBmultiattribute(false);
		}
		String clistingId = pt.getListingId();
		String csku = pt.getSku();
		pBase.setBspecial(pt.getSpecial());
		pBase.setBvisible(pt.getVisible());
		pBase.setCdescription(obj.getDescription());
		pBase.setCkeyword(obj.getKeyword());
		pBase.setClistingid(clistingId);
		pBase.setCparentsku(pt.getSpu());
		pBase.setCshortdescription(obj.getShortDescription());
		pBase.setCsku(csku);
		pBase.setCtitle(obj.getTitle());
		pBase.setDcreatedate(pt.getNewFromDate());
		pBase.setDnewtodate(pt.getNewToDate());
		pBase.setFprice(pt.getPrice());
		pBase.setFweight(pt.getFreight());
		 
		pBase.setIlanguageid(obj.getLanguageId());
		pBase.setIqty(pt.getQty());
		pBase.setIstatus(pt.getStatus());
		pBase.setIwebsiteid(pt.getWebsiteId());
		pic.setProduct(pBase);
		
		// 设置类目
		List<ProductCategoryMapper> categories = categoryMapper
				.selectByListingId(clistingId);
		pic.setCategories(categories);
		
		List<ProductBaseTranslate> translates = productBaseTranslateService
				.getTranslateByListingid(clistingId);
		
		pic.setOtherInfos(translates);
		
		List<ProductEntityMap> attributes = attributeMapper
				.getProductEntityMapByListingid(clistingId);
		
		pic.setAttributes(attributes);
				
		List<ProductSalePrice> sales =  this.productSalePriceEnquityDao
				.getAllProductSalePriceByListingId(clistingId);
		pic.setSales(sales);
				
		List<ProductLabel> tags =  this.productLabelEnquiryDao.getProductLabel(clistingId);
		pic.setTags(tags);
		
		ProductViewCount viewCount  = getViewCount(clistingId);
		Integer count = viewCount != null ? viewCount.getIviewcount() : 0;
		pic.setViewCount(count);
				
		List<String> relatedSku = Lists.transform(
				productBaseDao.getRelatedSkuByClistingid(clistingId),
				l -> l.getCsku());
		pic.setRelatedSku(relatedSku);
		
		List<ProductStorageMap> storages =  storageMapper
				.getStorageIdByListingid(clistingId);
		pic.setStorage(storages);
		
		List<CategoryProductRecommend> redList =  categoryProductRecommendService
					.getProductRecommendBySkus(Lists.newArrayList(csku));
		if(redList != null && redList.size() >0){
			List<RecommendDoc> recList = Lists.transform(
				redList,
				r -> {
					if (r != null) {
						return new RecommendDoc(r.getCategoryid(), r
								.getSequence(), r.getIwebsiteid(), r
								.getCdevice());
					} else {
						return null;
					}
				});
			pic.setCategoryRecommend(recList);
		}
		
		Price p = new Price(new SingleProductSpec(clistingId, pt.getQty()));
		p.setUnitCost(pt.getCost());
		p.setUnitPrice(pt.getPrice());
		pic.setPrice(p);
//		String str = JSON.toJSONString(pic);
//		ObjectMapper om=new ObjectMapper();
//		JsonNode node = om.readTree(str);
//		if (node.isArray()) {
//			Iterator<JsonNode> nodeiterator = node.iterator();
//			while (nodeiterator.hasNext()) {
//				ProductIndexingContext_New cbase = JSON.parseObject(
//						nodeiterator.next().asText(),
//						ProductIndexingContext_New.class);
//			}
//		} else {
//			ProductIndexingContext_New cbase = JSON.parseObject(node.toString(),
//					ProductIndexingContext_New.class);
//		}
		return JSON.toJSONString(pic);
		 
	}
	 

	protected ProductViewCount getViewCount(String listingID) {
		List<ProductViewCount> count = this.productViewCountEnquityDao
				.getViewCountListByListingIds(Lists.newArrayList(listingID));
		if (count.isEmpty()) {
			return null;
		} else {
			return count.get(0);
		}
	}

}
