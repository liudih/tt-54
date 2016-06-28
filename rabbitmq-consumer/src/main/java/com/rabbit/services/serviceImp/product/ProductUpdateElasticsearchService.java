package com.rabbit.services.serviceImp.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rabbit.common.enums.search.criteria.ProductLabelType;
import com.rabbit.dao.idao.product.IProductBaseEnquiryDao;
import com.rabbit.dao.idao.product.IProductSalePriceEnquiryDao;
import com.rabbit.dto.Storage;
import com.rabbit.dto.base.SimpleLanguage;
import com.rabbit.dto.monitor.RabbitMonitorDto;
import com.rabbit.dto.product.CategoryWebsiteWithName;
import com.rabbit.dto.product.ProductBase;
import com.rabbit.dto.product.ProductSalePrice;
import com.rabbit.dto.product.ProductUrl;
import com.rabbit.dto.search.DepotEntity;
import com.rabbit.dto.search.MutilLanguage;
import com.rabbit.dto.search.ProductEntity;
import com.rabbit.dto.search.ProductImageEntity;
import com.rabbit.dto.search.ProductTypeEntity;
import com.rabbit.dto.search.TagEntity;
import com.rabbit.dto.transfer.product.AttributeItem;
import com.rabbit.dto.transfer.product.ImageItem;
import com.rabbit.dto.transfer.product.MultiProduct;
import com.rabbit.dto.transfer.product.ProductBack;
import com.rabbit.dto.transfer.product.TranslateItem;
import com.rabbit.properties.ConfigInfo;
import com.rabbit.services.iservice.IStorageService;
import com.rabbit.services.iservice.base.IFoundationService;
import com.rabbit.services.iservice.monitor.IMonitorService;
import com.rabbit.util.HttpSendRequest;

@Component("elasticsearchService")
public class ProductUpdateElasticsearchService {

	@Autowired
	ConfigInfo configInfo;
	@Autowired
	IFoundationService foundationService;
	@Autowired
	IStorageService storageService;

	@Autowired
	CategoryEnquiryService categoryEnquiryService;

	@Autowired
	IMonitorService monitorService;

	@Autowired
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;

	@Autowired
	ProductEnquiryService productEnquiryService;

	@Autowired
	IProductBaseEnquiryDao productBaseDao;
	
	/**
	 * 更新或创建老搜索引擎
	 * 
	 * @param clistingId
	 */
	public boolean saveOrUpdateWWWElasticSearch(String clistingId) {
		String remoteUrl = configInfo.getWwwElasticsearchUrl();
		
		boolean r = true;
		//如果失败，则再推送一次
		for (int i = 0; i < 2; i++) {
			String result = HttpSendRequest.sendGet(remoteUrl
					+ "/product/_createIndexByListing/listing/" + clistingId);
			if (!"failure".equals(result)) {
				r = true;
				break;
			}else{
				r = false;
				continue;
			}
		}
		return r;

	}
	
	/**
	 * 更新或创建老搜索引擎
	 * 
	 * @param clistingId
	 */
	public boolean createWWWElasticSearch(String json) {
		String remoteUrl = configInfo.getWwwElasticsearchUrl();
		
		boolean r = true;
		//如果失败，则再推送一次
		for (int i = 0; i < 2; i++) {
			boolean result = HttpSendRequest.sendPostByJson(remoteUrl
					+ "/product/_createIndex", json);
			if (result) {
				r = true;
				break;
			}else{
				r = false;
				continue;
			}
		}
		return r;
	}

	/**
	 * 创建商品
	 * 
	 * @param json
	 * @return
	 */
	public boolean createProduct(String json) {
		String remoteUrl = configInfo.getRemoteElaticsearchUrl();
		boolean r = true;
		for (int i = 0; i < 2; i++) {
			boolean result = HttpSendRequest.sendPostByJson(remoteUrl
					+ "/index/insert", json);
			if (result) {
				r = true;
				break;
			}else{
				r = false;
				continue;
			}
		}
		return r;
	}

	/**
	 * 删除产品索引
	 * 
	 * @param listingId
	 * @return
	 */
	public boolean deleteProductIndex(String listingId) {
		String remoteUrl = configInfo.getRemoteElaticsearchUrl();
		return HttpSendRequest.sendDelete(remoteUrl + "/index/deleteIndex/"
				+ listingId);
	}

	/**
	 * 局部更新产品多语言
	 * 
	 * @param listingId
	 * @param json
	 * @return
	 */
	public boolean updateProductPart(String listingId, String json) {
		String remoteUrl = configInfo.getRemoteElaticsearchUrl();
		this.saveOrUpdateWWWElasticSearch(listingId);
		
		boolean r = true;
		//如果失败，则再推送一次
		for (int i = 0; i < 2; i++) {
			boolean result = HttpSendRequest.sendPostByJson(remoteUrl
					+ "/index/updateIndexPart/" + listingId, json);
			if (result) {
				r = true;
				break;
			}else{
				r = false;
				continue;
			}
		}
		
		return r;
	}

	/**
	 * 局部更新产品更新指定语言
	 * 
	 * @param listingId
	 * @param json
	 * @return
	 */
	public boolean updateProductPart(String lang, String listingId, String json) {
		String remoteUrl = configInfo.getRemoteElaticsearchUrl();
		this.saveOrUpdateWWWElasticSearch(listingId);
		return HttpSendRequest.sendPostByJson(remoteUrl
				+ "/index/updateIndexPart/" + lang + "/" + listingId, json);
	}

	public boolean updateIndexByListingId(String listingId) throws JsonProcessingException {
		List<ProductBack> plist = productEnquiryService
				.getProductsByListingIds(Lists.newArrayList(listingId));
		String json = this.getElasticsearchJson(plist.get(0));

		return this.updateProductPart(listingId, json);
	}

	public String getElasticsearchJson(ProductBack pt){

		List<SimpleLanguage> languages = foundationService.getAllLanguage();
		// list -> map must jdk1.8
		Map<Integer, SimpleLanguage> langMaps = languages.stream().collect(
				Collectors.toMap(SimpleLanguage::getIid, (l) -> l));

		// 产品
		ProductEntity product = new ProductEntity();

		// 设置品牌
		List<AttributeItem> attrs = pt.getAttributes();
		if (null != attrs && attrs.size() > 0) {
			for (AttributeItem obj : attrs) {
				if (obj.getKey().equalsIgnoreCase("brand")) {
					String brandValue = obj.getValue();
					product.setBrand(brandValue);
					break;
				}
			}
		}

		//是否可见
		product.setBvisible(pt.getVisible());
		//关联sku
		List<String> relatedSku = Lists.transform(
				productBaseDao.getRelatedSkuByClistingid(pt.getListingId()),
				l -> l.getCsku().toUpperCase());
		
		product.setRelatedSkus(relatedSku);
		
		// 视频
		if (null != pt.getVideos() && pt.getVideos().size() > 0) {
			List<String> videoList = Lists.transform(pt.getVideos(),
					itembase -> itembase.getVideoUrl());
			product.setVideos(videoList);
		}
		setImageList(pt, product);

		// 设置是否主产品
		if (pt instanceof MultiProduct) {
			product.setBmain(pt.getIsMain());
		} else {
			product.setBmain(true);
		}

		// spu
		if(null != pt.getSpu()){
			product.setSpu(pt.getSpu().toUpperCase());
		}
		// 状态
		product.setStatus(pt.getStatus());

		// 站点
		product.setWebSites(Lists.newArrayList(pt.getWebsiteId()));
		// 价格
		product.setCostPrice(pt.getCost());
		product.setYjPrice(pt.getPrice());
		// 仓库
		List<Integer> storageIds = pt.getStorages();
		List<Storage> storages = this.storageService
				.getStorageByStorageIds(storageIds);
		List<DepotEntity> depots = null;
		if (null != storages && storages.size() > 0) {
			depots = Lists.transform(storages, obj -> {
				DepotEntity de = new DepotEntity();
				de.setDepotid(obj.getIid());
				de.setDepotName(obj.getCstoragename());
				return de;
			});
		}
		product.setDepots(depots);

		product.setListingId(pt.getListingId());
		product.setSku(pt.getSku().toUpperCase());
		product.setWeight(pt.getWeight());

		// 设置促销价格
		List<ProductSalePrice> sales = this.productSalePriceEnquityDao
				.getAllProductSalePriceByListingId(pt.getListingId());

		if (null != sales && sales.size() > 0) {
			List<com.rabbit.dto.search.PromotionSalePrice> promotionPrice = Lists
					.transform(
							sales,
							obj -> {
								com.rabbit.dto.search.PromotionSalePrice ps = new com.rabbit.dto.search.PromotionSalePrice();

								ps.setBeginDate(com.rabbit.common.util.DateFormatUtils
										.getStrFromYYYYMMDDHHMMSS(obj
												.getDbegindate()));
								ps.setEndDate(com.rabbit.common.util.DateFormatUtils
										.getStrFromYYYYMMDDHHMMSS(obj
												.getDenddate()));
								ps.setPrice(obj.getFsaleprice());
								ps.setWebSiteId(pt.getWebsiteId());
								ps.setSku(obj.getCsku());

								return ps;
							});
			product.setPromotionPrice(promotionPrice);
		}

		// 设置标签
		List<TagEntity> tagNames = new ArrayList<TagEntity>();

		List<ProductLabelType> labelTypes = this.getProductTypes(pt);

		if (labelTypes != null && labelTypes.size() > 0) {
			tagNames = Lists.transform(labelTypes, obj -> {
				TagEntity te = new TagEntity();
				te.setTagName(obj.toString());
				te.setReleaseTime(DateFormatUtils.formatUTC(new Date(),
						"yyyy-MM-dd HH:mm:ss"));

				return te;
			});

			product.setTagsName(tagNames);
			boolean flag = false;
			for (ProductLabelType plt : labelTypes) {
				if (plt.toString().equalsIgnoreCase(
						ProductLabelType.FreeShipping.toString())
						|| plt.toString().equalsIgnoreCase(
								ProductLabelType.AllFreeShipping.toString())) {
					flag = true;
					break;
				}
			}

			if (flag) {
				product.setIsFreeShipping(true);
			} else {
				product.setIsFreeShipping(false);
			}
		}

		// 评论数
		// 收藏
		// product.setColltes(0);
		// 库存
		if (pt.getQty() != null) {
			product.setStoreNum(pt.getQty());
		}
		// 设置销量
		// product.setSalesVolume(pt.get);

		// 设置类目
		List<Integer> categoryIds = pt.getCategoryIds();

		List<TranslateItem> translateItem = pt.getItems();

		// 设置多语言属性
		List<MutilLanguage> mutilLanguages = new ArrayList<MutilLanguage>();

		List<ProductUrl> urlList = productEnquiryService
				.getProductUrlByListingId(pt.getListingId());
		Map<Integer, List<String>> urlMaps = this.getProductUrlKeyMap(urlList);
		if (null != translateItem && translateItem.size() > 0) {
			for (TranslateItem obj : translateItem) {
				Integer langId = obj.getLanguageId();
				MutilLanguage ml = new MutilLanguage();
				ml.setDesc(obj.getDescription());
				ml.setKeywords(obj.getKeyword());
				ml.setMetaDescription(obj.getMetaDescription());
				ml.setMetaKeyword(obj.getMetaKeyword());
				ml.setMetaTitle(obj.getMetaTitle());
				ml.setTitle(obj.getTitle());
				ml.setUrl(urlMaps.get(langId));
				ml.setLanguageName(langMaps.get(langId).getCname());
				ml.setLanguageId(obj.getLanguageId());

				if (pt instanceof MultiProduct) {
					MultiProduct mitem = (MultiProduct) pt;
					ml.setItems(mitem.getMultiAttributes());
				} else {
					ml.setItems(pt.getAttributes());
				}

				ml.setShortDescription(obj.getShortDescription());

				List<ProductTypeEntity> productTypes = Lists.newArrayList();

				// 类目1
				List<ProductTypeEntity> productLevel1 = Lists.newArrayList();
				// 类目2
				List<ProductTypeEntity> productLevel2 = Lists.newArrayList();
				// 类目3
				List<ProductTypeEntity> productLevel3 = Lists.newArrayList();
				if (null != categoryIds && categoryIds.size() > 0) {
					for (Integer categoryId : categoryIds) {

						// 这个是它自己
						CategoryWebsiteWithName selfCategory = this.categoryEnquiryService
								.getCategoryWebsiteByIdAndSiteIdAndLangId(
										categoryId, langId, pt.getWebsiteId());

						ProductTypeEntity p = new ProductTypeEntity();
						if (selfCategory == null) {
							continue;
						}
						p.setLanguageId(langId);
						p.setProductTypeId(selfCategory.getIcategoryid());
						p.setProductTypeName(selfCategory.getCname());
						p.setLevel(selfCategory.getIlevel());
						p.setCpath(selfCategory.getCpath());

						if (null != selfCategory.getIlevel()) {
							if (selfCategory.getIlevel() == 1) {
								productLevel1.add(p);
							} else if (selfCategory.getIlevel() == 2) {
								productLevel2.add(p);
							} else if (selfCategory.getIlevel() == 3) {
								productLevel3.add(p);
							}
						}
						productTypes.add(p);
					}
				}
				ml.setProductTypes(productTypes);
				ml.setProductLevel1(productLevel1);
				ml.setProductLevel2(productLevel2);
				ml.setProductLevel3(productLevel3);

				mutilLanguages.add(ml);
			}
		}

		product.setMutil(mutilLanguages);

		try {
			return new ObjectMapper().writeValueAsString(product);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			return "format json error";
		}
	}

	/**
	 * url list to map
	 * 
	 * @param urlList
	 * @return
	 */
	private Map<Integer, List<String>> getProductUrlKeyMap(
			List<ProductUrl> urlList) {

		Map<Integer, List<String>> urlMap = Maps.newHashMap();

		if (null != urlList && urlList.size() > 0) {
			Iterator<ProductUrl> e = urlList.iterator();
			while (e.hasNext()) {
				ProductUrl cm = e.next();
				List<String> tempList = null;
				if (urlMap.containsKey(cm.getIlanguageid())) {
					tempList = urlMap.get(cm.getIlanguageid());
					tempList.add(cm.getCurl());

				} else {
					tempList = new ArrayList<String>();
					tempList.add(cm.getCurl());
				}

				urlMap.put(cm.getIlanguageid(), tempList);
			}
		}

		return urlMap;
	}

	private void setImageList(ProductBack pt, ProductEntity product) {
		// 图片
		List<ProductImageEntity> imgs = Lists.newArrayList();
		// 这个地方的图片需要处理 1. 图片路径修改为相对路径 2.保存原始路径
		Map<String, String> prefixMapping = Maps.newHashMap();
		if (this.configInfo.getGuphotosImages() == null) {
			throw new RuntimeException("Images config info not read!");
		}
		prefixMapping.put(this.configInfo.getGuphotosImages().split(",")[0],
				this.configInfo.getGuphotosImages().split(",")[1]);
		prefixMapping.put(
				this.configInfo.getGuphotosListingImages().split(",")[0],
				this.configInfo.getGuphotosListingImages().split(",")[1]);
		prefixMapping.put(this.configInfo.getTomtopImage().split(",")[0],
				this.configInfo.getTomtopImage().split(",")[1]);

		if (null != pt.getImages() && pt.getImages().size() > 0) {
			List<ImageItem> imglist = Lists.transform(
					pt.getImages(),
					obj -> {
						for (Map.Entry<String, String> prefix : prefixMapping
								.entrySet()) {
							String imageUrl = obj.getImageUrl();
							if (obj.getImageUrl().contains(prefix.getKey())) {
								imageUrl = imageUrl.replace(prefix.getKey(),
										prefix.getValue());
								obj.setImageUrl(imageUrl);
							}
						}

						String imgUrl = obj.getImageUrl();
						if (null != imgUrl && imgUrl.startsWith("http")) {
							int index = imgUrl.indexOf("/", 7);
							imgUrl = imgUrl.substring(index + 1);
							obj.setImageUrl(imgUrl);
						}

						return obj;
					});
			imgs = Lists.transform(imglist, obj -> {
				ProductImageEntity image = new ProductImageEntity();
				image.setIsBase(obj.getBaseImage());
				image.setIsSmall(obj.getSmallImage());
				image.setOrder(obj.getOrder());
				image.setUrl(obj.getImageUrl());
				return image;
			});
			product.setImgs(imgs);
		}
	}

	private List<ProductLabelType> getProductTypes(ProductBack pitem) {
		List<ProductLabelType> ptype = new ArrayList<ProductLabelType>();
		if (pitem.getHot() != null && pitem.getHot()) {
			ptype.add(ProductLabelType.Hot);
		}
		if (pitem.getIsNew() != null && pitem.getIsNew()) {
			ptype.add(ProductLabelType.NewArrial);
		}
		if (pitem.getCleanrstocks() != null && pitem.getCleanrstocks()) {
			ptype.add(ProductLabelType.Clearstocks);
		}
		if (pitem.getFeatured() != null && pitem.getFeatured()) {
			ptype.add(ProductLabelType.Featured);
		}
		if (pitem.getSpecial() != null && pitem.getSpecial()) {
			ptype.add(ProductLabelType.Special);
		}
		if (pitem.getFreight() != null && pitem.getFreight() > 0) {
			ptype.add(ProductLabelType.FreeShipping);
		}
		if (pitem.getIsAllFreeShipping() != null
				&& pitem.getIsAllFreeShipping()) {
			ptype.add(ProductLabelType.AllFreeShipping);
		}

		return ptype;
	}

	public void addMonitorRecord(String key, String optType, String state,
			String nodeData, String failReason) {
		RabbitMonitorDto rabbitMonitorDto = new RabbitMonitorDto();
		String date = com.rabbit.common.util.DateFormatUtils
				.getUtcDateStr(new Date());
		rabbitMonitorDto.setRecordState(state);
		rabbitMonitorDto.setRecordKey(key);
		rabbitMonitorDto.setCreatedOn(com.rabbit.common.util.DateFormatUtils
				.getUtcDateStr(new Date()));
		rabbitMonitorDto.setCreatedBy("api");
		rabbitMonitorDto.setIsDeleted("0");
		rabbitMonitorDto.setLastUpdatedBy("api");
		rabbitMonitorDto.setLastUpdatedOn(date);
		rabbitMonitorDto.setOptType(optType);
		if (StringUtils.isNotEmpty(failReason)) {
			failReason = failReason.length() > 5000 ? failReason.substring(0,
					5000) : failReason;
		}
		rabbitMonitorDto.setNodeData(nodeData);
		rabbitMonitorDto.setFailReason(failReason);
		monitorService.addMonitorRecord(rabbitMonitorDto);
	}

	@Autowired
	IProductBaseEnquiryDao productBaseEnquityDao;

	public boolean updateProductMutil(String sku, Integer websiteId,
			Integer level, Integer icategoryId, Integer sortNumber)
			throws JsonProcessingException {
		boolean flag = true;
		// ProductEntity product = new ProductEntity();

		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
		if (pbases == null) {
			return false;
		}
		for (ProductBase pbase : pbases) {
			List<ProductBack> plist = productEnquiryService
					.getProductsByListingIds(Lists.newArrayList(pbase
							.getClistingid()));

			ProductBack pt = plist.get(0);
			List<TranslateItem> translateItem = pt.getItems();
			List<SimpleLanguage> languages = foundationService.getAllLanguage();
			// list -> map must jdk1.8
			Map<Integer, SimpleLanguage> langMaps = languages.stream().collect(
					Collectors.toMap(SimpleLanguage::getIid, (l) -> l));
			// 设置多语言属性
			List<MutilLanguage> mutilLanguages = new ArrayList<MutilLanguage>();

			List<Integer> categoryIds = pt.getCategoryIds();
			List<ProductUrl> urlList = productEnquiryService
					.getProductUrlByListingId(pt.getListingId());
			Map<Integer, List<String>> urlMaps = this
					.getProductUrlKeyMap(urlList);
			// TranslateItem test = new TranslateItem();
			// test.setLanguageId(8);
			// translateItem.add(test);
			if (null != translateItem && translateItem.size() > 0) {
				for (TranslateItem obj : translateItem) {
					Integer langId = obj.getLanguageId();
					MutilLanguage ml = new MutilLanguage();
					ml.setDesc(obj.getDescription());
					ml.setKeywords(obj.getKeyword());
					ml.setMetaDescription(obj.getMetaDescription());
					ml.setMetaKeyword(obj.getMetaKeyword());
					ml.setMetaTitle(obj.getMetaTitle());
					ml.setTitle(obj.getTitle());
					ml.setUrl(urlMaps.get(langId));
					if (translateItem.size() > 1) {
						ml.setLanguageName(langMaps.get(langId).getCname());
						ml.setLanguageId(obj.getLanguageId());
					}

					if (pt instanceof MultiProduct) {
						MultiProduct mitem = (MultiProduct) pt;
						ml.setItems(mitem.getMultiAttributes());
					} else {
						ml.setItems(pt.getAttributes());
					}

					ml.setShortDescription(obj.getShortDescription());

					List<ProductTypeEntity> productTypes = Lists.newArrayList();

					// 类目1
					List<ProductTypeEntity> productLevel1 = Lists
							.newArrayList();
					// 类目2
					List<ProductTypeEntity> productLevel2 = Lists
							.newArrayList();
					// 类目3
					List<ProductTypeEntity> productLevel3 = Lists
							.newArrayList();

					if (null != categoryIds && categoryIds.size() > 0) {
						for (Integer categoryId : categoryIds) {

							// 这个是它自己
							CategoryWebsiteWithName selfCategory = this.categoryEnquiryService
									.getCategoryWebsiteByIdAndSiteIdAndLangId(
											categoryId, langId,
											pt.getWebsiteId());

							ProductTypeEntity p = new ProductTypeEntity();
							if (selfCategory == null) {
								continue;
							}
							p.setLanguageId(langId);
							p.setProductTypeId(selfCategory.getIcategoryid());
							p.setProductTypeName(selfCategory.getCname());
							p.setLevel(selfCategory.getIlevel());
							p.setCpath(selfCategory.getCpath());

							if (null != selfCategory.getIlevel()) {
								if (icategoryId.intValue() == categoryId
										.intValue()
										&& selfCategory.getIlevel() == 1) {
									p.setSort(sortNumber);
								} else if (icategoryId.intValue() == categoryId
										.intValue()
										&& selfCategory.getIlevel() == 2) {
									p.setSort(sortNumber);
								} else if (icategoryId.intValue() == categoryId
										.intValue()
										&& selfCategory.getIlevel() == 3) {
									p.setSort(sortNumber);
								} else {
									p.setSort(999);
								}

								if (selfCategory.getIlevel() == 1) {
									productLevel1.add(p);
								} else if (selfCategory.getIlevel() == 2) {
									productLevel2.add(p);
								} else if (selfCategory.getIlevel() == 3) {
									productLevel3.add(p);
								}
							}

							productTypes.add(p);
						}
					}
					ml.setProductTypes(productTypes);
					ml.setProductLevel1(productLevel1);
					ml.setProductLevel2(productLevel2);
					ml.setProductLevel3(productLevel3);
					mutilLanguages.add(ml);

				}
			}

			JSONObject json = new JSONObject();
			json.put("mutil", mutilLanguages);

			flag = this.updateProductPart(pbase.getClistingid(),
					new ObjectMapper().writeValueAsString(json));
		}
		return flag;
	}
}
