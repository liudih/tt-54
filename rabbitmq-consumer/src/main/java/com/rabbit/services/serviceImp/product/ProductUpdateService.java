package com.rabbit.services.serviceImp.product;

/*import ProductUpdateEvent;*/

/*import ProductUpdateEvent;*/

/*import ProductEntityMap;*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.eventbus.EventBus;
import com.rabbit.common.enums.InventoryTypeEnum;
import com.rabbit.common.enums.search.criteria.ProductLabelType;
import com.rabbit.conf.mapper.product.ProductBaseMapper;
import com.rabbit.conf.mapper.product.ProductCategoryMapperMapper;
import com.rabbit.conf.mapper.product.ProductEntityMapMapper;
import com.rabbit.conf.mapper.product.ProductImageMapper;
import com.rabbit.conf.mapper.product.ProductMultiattributeAttributeMapper;
import com.rabbit.conf.mapper.product.ProductMultiattributeBaseMapper;
import com.rabbit.conf.mapper.product.ProductMultiattributeSkuMapper;
import com.rabbit.dao.idao.product.IAttributeEnquiryDao;
import com.rabbit.dao.idao.product.IInventoryHistoryEnquiryDao;
import com.rabbit.dao.idao.product.IInventoryHistoryUpdateDao;
import com.rabbit.dao.idao.product.IProductBaseEnquiryDao;
import com.rabbit.dao.idao.product.IProductBaseUpdateDao;
import com.rabbit.dao.idao.product.IProductBundleSaleEnquiryDao;
import com.rabbit.dao.idao.product.IProductBundleSaleUpdateDao;
import com.rabbit.dao.idao.product.IProductLabelEnquiryDao;
import com.rabbit.dao.idao.product.IProductLabelUpdateDao;
import com.rabbit.dao.idao.product.IProductRecommendUpdateDao;
import com.rabbit.dao.idao.product.IProductSalePriceEnquiryDao;
import com.rabbit.dao.idao.product.IProductSalePriceUpdateDao;
import com.rabbit.dao.idao.product.IProductSellingPointsEnquiryDao;
import com.rabbit.dao.idao.product.IProductSellingPointsUpdateDao;
import com.rabbit.dao.idao.product.IProductStorageMapUpdateDao;
import com.rabbit.dao.idao.product.IProductTranslateEnquiryDao;
import com.rabbit.dao.idao.product.IProductTranslateUpdateDao;
import com.rabbit.dao.idao.product.IProductUrlEnquiryDao;
import com.rabbit.dao.idao.product.IProductUrlUpdateDao;
import com.rabbit.dao.idao.product.IProductVideoUpdateDao;
import com.rabbit.dao.idao.product.IProductViewCountUpdateDao;
import com.rabbit.dto.Storage;
import com.rabbit.dto.product.InventoryHistory;
import com.rabbit.dto.product.ProductBase;
import com.rabbit.dto.product.ProductBundleSale;
import com.rabbit.dto.product.ProductCategoryMapper;
import com.rabbit.dto.product.ProductEntityMap;
import com.rabbit.dto.product.ProductImage;
import com.rabbit.dto.product.ProductLabel;
import com.rabbit.dto.product.ProductMultiattributeAttribute;
import com.rabbit.dto.product.ProductMultiattributeBase;
import com.rabbit.dto.product.ProductMultiattributeSku;
import com.rabbit.dto.product.ProductSalePrice;
import com.rabbit.dto.product.ProductSellingPoints;
import com.rabbit.dto.product.ProductStorageMap;
import com.rabbit.dto.product.ProductTranslate;
import com.rabbit.dto.product.ProductUrl;
import com.rabbit.dto.product.ProductVideo;
import com.rabbit.dto.product.SkuRelateListingId;
import com.rabbit.dto.search.DepotEntity;
import com.rabbit.dto.search.ProductTypeEntity;
import com.rabbit.dto.transfer.Attribute;
import com.rabbit.dto.transfer.product.AttributeItem;
import com.rabbit.dto.transfer.product.ImageItem;
import com.rabbit.dto.transfer.product.MultiProduct;
import com.rabbit.dto.transfer.product.ProductBack;
import com.rabbit.dto.transfer.product.PromotionPrice;
import com.rabbit.dto.transfer.product.TranslateItem;
import com.rabbit.properties.ConfigInfo;
import com.rabbit.services.iservice.IStorageService;
import com.rabbit.services.iservice.base.IFoundationService;
import com.rabbit.services.iservice.product.IImageUpdateService;
import com.rabbit.services.iservice.product.IProductUpdateService;
import com.rabbit.services.search.serviceImp.IndexingService;

@Service
@Transactional(propagation=Propagation.REQUIRES_NEW , value = "isap")
public class ProductUpdateService implements IProductUpdateService {
	private static Object deleteImgLock=new Object();
	private static Object updatePriceLock=new Object();
	private static Object updateStatusLock=new Object();
	private static Logger log = Logger.getLogger(ProductUpdateService.class
			.getName());
	@Autowired
	ProductBaseMapper productBaseMapper;
	/*
	 * @Autowired ProductCategoryMapMapper productCategoryMapMapper;
	 */
	@Autowired
	ProductImageMapper productImageMapper;
	@Autowired
	ProductMultiattributeAttributeMapper productMultiattributeAttributeMapper;
	@Autowired
	ProductMultiattributeBaseMapper productMultiattributeBaseMapper;
	@Autowired
	ProductMultiattributeSkuMapper productMultiattributeSkuMapper;
	@Autowired
	ProductEntityMapMapper productEntityMapMapper;
	@Autowired
	ProductValidation productValidation;
	@Autowired
	IProductBundleSaleEnquiryDao productBundleSaleEnquiryDao;
	@Autowired
	EventBus eventBus;
	@Autowired
	CategoryEnquiryService categoryEnquiryService;
	@Autowired
	AttributeUpdateService attributeUpdateService;
	@Autowired
	StorageService storageEnquiryService;
	@Autowired
	IProductBaseEnquiryDao productBaseEnquityDao;
	@Autowired
	IProductBaseUpdateDao productBaseUpdateDao;
	@Autowired
	IProductLabelUpdateDao productLabelUpdateDao;
	@Autowired
	IProductLabelEnquiryDao productLabelEnquiryDao;
	@Autowired
	IProductUrlEnquiryDao productUrlEnquityDao;
	@Autowired
	IProductUrlUpdateDao productUrlUpdateDao;
	@Autowired
	IProductViewCountUpdateDao productViewCountUpdateDao;
	@Autowired
	IProductVideoUpdateDao productVideoUpdateDao;
	@Autowired
	IProductTranslateEnquiryDao productTranslateEnquityDao;
	@Autowired
	IProductTranslateUpdateDao productTranslateUpdateDao;
	@Autowired
	IAttributeEnquiryDao attributeEnquityDao;
	@Autowired
	IProductBundleSaleUpdateDao productBundleSaleUpdateDao;
	@Autowired
	IProductStorageMapUpdateDao productStorageMapUpdateDao;
	@Autowired
	IProductSellingPointsEnquiryDao productSellingPointsEnquityDao;
	@Autowired
	IProductSellingPointsUpdateDao productSellingPointsUpdateDao;
	@Autowired
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;
	@Autowired
	IProductSalePriceUpdateDao productSalePriceUpdateDao;
	@Autowired
	UpdateProductStatusAndQuantityAndLabelService updateProductStatusAndQuantityAndLabelService;
	@Autowired
	IProductRecommendUpdateDao productRecommendUpdateDao;
	@Autowired
	IInventoryHistoryUpdateDao iInventoryHistoryUpdateDao;
	@Autowired
	IInventoryHistoryEnquiryDao iInventoryHistoryEnquiryDao;
	@Autowired
	ProductCategoryMapperMapper productCategoryMapperMapper;
	@Autowired
	IImageUpdateService imageUpdateService;

	@Autowired
	IStorageService storageService;

	@Autowired
	IFoundationService foundationService;

	@Autowired
	ProductEnquiryService productEnquiryService;

	@Autowired
	ConfigInfo configInfo;

	@Autowired
	ProductUpdateElasticsearchService elasticsearchService;
	
	@Autowired
	IndexingService indexingService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#createProduct(com.website.dto.
	 * product.Product, java.lang.String)
	 */
	/* @Transactional */
	public String createProduct(ProductBack pitem, String username)
			throws Exception {
		
		//此处为了兼容上线 erp对接， 老网站会直接生成一次lisingid， rabbitmq 生产者会生成一次
		//那么我们这边的lisingid将会不正确，需要从数据库里面捞出来set进去我们现在的搜索引擎里面
		List<ProductBase> productBaseList = this.validateBySkuAndWebsiteId(pitem.getSku(), pitem.getWebsiteId());
		
		if(null == productBaseList || productBaseList.size() ==0){
			String result = createValidation(pitem);
			String psku = null;
			String listingid = "";
			if ("".equals(result)) {
				if (StringUtils.isEmpty(pitem.getListingId())) {
					log.info("recreate listingID");
					listingid = this.generationListingId();
				} else {
					listingid = pitem.getListingId();
				}

				try {
					Boolean mainsku = true;
					log.info("Creating Listing: {}," + listingid + "    SKU: {}"
							+ pitem.getSku());
					if (pitem instanceof MultiProduct) {
						MultiProduct mitem = (MultiProduct) pitem;
						mainsku = this.needSetlistingToMain(
								(mitem.getMainSku() != null ? mitem.getMainSku()
										: false), pitem.getWebsiteId(), mitem
										.getParentSku());
						// ~ 这个需要重新判断是否设置为主产品
						System.out.println("main->" + mainsku);
						mitem.setMainSku(mainsku);
					}
					pitem.setVisible(mainsku);
					pitem.setIsMain(mainsku);
					this.saveProductBase(pitem, username, listingid);
					this.updateQtyInventory(listingid, pitem.getQty(),
							pitem.getWebsiteId());
					this.saveProductUrl(pitem, username, listingid);
					this.saveProductCategory(pitem, username, listingid);
					this.saveProductSellingPoint(pitem, username, listingid);
					this.saveProductTranslate(pitem.getItems(), listingid,
							pitem.getSku());
					this.saveProductImage(pitem.getImages(), pitem.getSku(),
							listingid);
					this.saveProductVideo(pitem, username, listingid);
					this.saveProductEntiytMap(pitem.getAttributes(),
							pitem.getSku(), pitem.getWebsiteId(), username,
							listingid, false);
					this.saveStorage(pitem, username, listingid);
					this.saveProductLable(listingid, pitem.getWebsiteId(),
							this.getProductTypes(pitem));
					if (pitem instanceof MultiProduct) {
						MultiProduct mitem = (MultiProduct) pitem;
						this.addProductMultiAttr(mitem.getSku(),
								mitem.getParentSku(), mitem.getWebsiteId(),
								mitem.getMultiAttributes(), listingid, username);
						pitem.setSpu(mitem.getParentSku());
					} 
					
					
//					if (mainsku) {
						
						if (pitem instanceof MultiProduct) {
							elasticsearchService.addMonitorRecord(pitem.getSku(),
									"RABBIT_PRODUCT_PUSHMUTIL_TYPE", "1",
									JSONObject.toJSONString(pitem), "");
						} else {
							elasticsearchService.addMonitorRecord(pitem.getSku(),
									"RABBIT_PRODUCT_PUSH_TYPE", "1",
									JSONObject.toJSONString(pitem), "");
						}
						// post数据elasticsearch
						try {
							//新搜索引擎索引创建
							String json = elasticsearchService.getElasticsearchJson(pitem);

							boolean flag = elasticsearchService.createProduct(json);
							if (!flag) {
								elasticsearchService.addMonitorRecord(pitem.getSku(),
										"createProduct error", "0",
										json, "create-new-index-interface-error");
								log.error("createProduct error,listingid:"
										+ listingid + ",sku:" + pitem.getSku());
							}
							
							//老搜索引擎索引创建
							String oldElaStr = indexingService.getOldElasticsearchJson(pitem);
							flag = elasticsearchService.createWWWElasticSearch(oldElaStr);
							if (!flag) {
								elasticsearchService.addMonitorRecord(pitem.getSku(),
										"createProduct error", "0",
										oldElaStr, "create-old-index-interface-error");
								log.error("createProduct error,listingid:"
										+ listingid + ",sku:" + pitem.getSku());
							}
						} catch (Exception ex) {
							elasticsearchService.addMonitorRecord(pitem.getSku(),
									"createProduct index error", "0",
									JSONObject.toJSONString(pitem), ex.getMessage());
						}
						// eventBus.post(new ProductUpdateEvent(pitem, listingid,
						// ProductUpdateEvent.ProductHandleType.insert));
//					}
				} catch (Exception ex) {
					elasticsearchService.addMonitorRecord(pitem.getSku(),
							"createProduct error", "0",
							JSONObject.toJSONString(pitem), ex.getMessage());
					
					delete(listingid, psku, pitem.getSku());
					log.error("createProduct error", ex);
					throw ex;
				}
			} else {
				elasticsearchService.addMonitorRecord(
						pitem.getSku(), "validate-fail", "0",
						result,
						"validate-fail");
				throw new Exception(result);
				// result = result + "--" + pitem.getSku();
			}
			return listingid;
		}else{
			ProductBase productBase = productBaseList.get(0);
			pitem.setListingId(productBase.getClistingid());
			
			if (pitem instanceof MultiProduct) {
				pitem.setSpu(((MultiProduct) pitem).getParentSku());
				pitem.setIsMain(((MultiProduct) pitem).getMainSku());
				elasticsearchService.addMonitorRecord(pitem.getSku(),
						"RABBIT_PRODUCT_PUSHMUTIL_TYPE", "1",
						JSONObject.toJSONString(pitem), "");
			} else {
				elasticsearchService.addMonitorRecord(pitem.getSku(),
						"RABBIT_PRODUCT_PUSH_TYPE", "1",
						JSONObject.toJSONString(pitem), "");
			}
			
			// post数据elasticsearch
			try {
				
				//新搜索引擎索引创建
				String json = elasticsearchService.getElasticsearchJson(pitem);

				boolean flag = elasticsearchService.createProduct(json);
				if (!flag) {
					elasticsearchService.addMonitorRecord(pitem.getSku(),
							"createProduct error", "0",
							json, "create-new-index-interface-error");
				}
				
				//老搜索引擎索引创建
				String oldElaStr = indexingService.getOldElasticsearchJson(pitem);
				flag = elasticsearchService.createWWWElasticSearch(oldElaStr);
				if (!flag) {
					elasticsearchService.addMonitorRecord(pitem.getSku(),
							"createProduct error", "0",
							oldElaStr, "create-old-index-interface-error");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				elasticsearchService.addMonitorRecord(pitem.getSku(),
						"createProduct index error", "0",
						JSONObject.toJSONString(pitem), ex.getMessage());
			}
			
			
			return pitem.getListingId();
		}
		
	}


	private Boolean saveMultiAttribute(List<AttributeItem> multiattrbutes,
			String psku, Integer websiteid, String username) {
		List<ProductMultiattributeAttribute> list = Lists.newArrayList();
		for (AttributeItem mitem : multiattrbutes) {
			ProductMultiattributeAttribute pma = productMultiattributeAttributeMapper
					.select(psku, websiteid, mitem.getKeyId());
			if (pma == null) {
				ProductMultiattributeAttribute mattr = new ProductMultiattributeAttribute();
				mattr.setBshowimg(mitem.getShowImg() != null ? mitem
						.getShowImg() : false);
				mattr.setIkey(mitem.getKeyId());
				mattr.setCparentsku(psku);
				mattr.setIwebsiteid(websiteid);
				list.add(mattr);
			} else {
				pma.setBshowimg(mitem.getShowImg());
				productMultiattributeAttributeMapper.updateByPrimaryKey(pma);
			}
		}
		if (list.size() > 0) {
			int rows = productMultiattributeAttributeMapper.batchInsert(list);
			return rows > 0;
		}
		return true;
	}

	private Boolean saveMultiAttributeBase(String parentSku, Integer websiteId,
			String username) {
		if (productMultiattributeBaseMapper.getMultiattributeBase(parentSku,
				websiteId) != null) {
			return false;
		}
		ProductMultiattributeBase mbase = new ProductMultiattributeBase();
		mbase.setCcreateuser(username);
		mbase.setClastupdateuser(username);
		mbase.setCparentsku(parentSku);
		mbase.setIwebsiteid(websiteId);
		mbase.setDcreatedate(new Date());
		mbase.setDlastupdatedate(new Date());
		int rows = productMultiattributeBaseMapper.batchInsert(Lists
				.newArrayList(mbase));
		return rows > 0;
	}

	private Boolean saveMultiattributeSku(String sku, String parentSku) {
		if (productMultiattributeSkuMapper.select(sku) != null) {
			productMultiattributeSkuMapper.update(sku, parentSku);
			return false;
		}
		ProductMultiattributeSku msku = new ProductMultiattributeSku();
		msku.setCparentsku(parentSku);
		msku.setCsku(sku);
		int rows = productMultiattributeSkuMapper.batchInsert(Lists
				.newArrayList(msku));
		return rows > 0;

	}

	private String addProductMultiAttr(String sku, String parentSku,
			Integer websiteId, List<AttributeItem> attributes,
			String listingid, String username) {
		productBaseUpdateDao.updateSpuBySkuAndSite(sku, websiteId, parentSku);
		this.saveMultiAttributeBase(parentSku, websiteId, username);
		this.saveMultiattributeSku(sku, parentSku);
		if (attributes == null) {
			return "";
		}
		List<AttributeItem> attrList = Lists.transform(attributes, obj -> {
			Attribute att = getAttributeId(obj);
			obj.setKeyId(att.getKeyId());
			obj.setValueId(att.getValueId());
			obj.setShowImg(obj.getShowImg());
			obj.setVisible(obj.getVisible());
			obj.setLanguangeId(obj.getLanguangeId());
			if (att.getKeyId() == null || att.getValueId() == null) {
				return null;
			}
			return obj;
		});
		Collection<AttributeItem> citems = Collections2.filter(attrList,
				obj -> obj != null);
		if (citems == null || citems.size() == 0)
			return "";
		List<AttributeItem> titems = Lists.newArrayList(citems);
		this.saveMultiAttribute(titems, parentSku, websiteId, username);
		this.saveProductEntiytMap(titems, sku, websiteId, username, listingid,
				true);
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#addProductMultiAttribute(java.
	 * lang.String, java.lang.String, java.lang.Integer, java.util.List,
	 * java.lang.String)
	 */
	@Override
	public String addProductMultiAttribute(Boolean mainSku, String sku, String parentsku,
			Integer websiteId, List<AttributeItem> multiAttributes,
			String username) {
		try {
			if (parentsku == null) {
				return "invalid parent sku!";
			}
			
			List<ProductBase> pbases = productBaseEnquityDao
					.getProductBaseBySkuAndWebsiteId(sku, websiteId);
			if (pbases == null) {
				return "invalid sku: " + sku + System.lineSeparator();
			}
			String re = "";


			for (ProductBase pbase : pbases) {
				re += addProductMultiAttr(sku, parentsku, websiteId,
						multiAttributes, pbase.getClistingid(), username);
				if (re.length() == 0) {

					List<ProductBack> plist = productEnquiryService
							.getProductsByListingIds(Lists.newArrayList(pbase.getClistingid()));
					
					if(mainSku != null && mainSku){
						this.needSetlistingToMain(mainSku, websiteId, parentsku);
						// ~ 这个需要重新判断是否设置为主产品
						System.out.println("main->" + mainSku);
						
						productBaseMapper.updateMainListing(pbase.getClistingid(), true);
					}
					
					plist = productEnquiryService
							.getProductsByListingIds(Lists.newArrayList(pbase.getClistingid()));
					
					String json = elasticsearchService.getElasticsearchJson(plist.get(0));
					elasticsearchService.updateProductPart(pbase.getClistingid(), json);
					
//					elasticsearchService.updateProductPart(
//							pbase.getClistingid(), jsonobj.toJSONString());

					// eventBus.post(new
					// ProductUpdateEvent(pbase.getClistingid(),
					// ProductUpdateEvent.ProductHandleType.update));
				}
			}
			return re;
		} catch (Exception ex) {
			return "sku: " + sku + "--" + ex.getMessage();
		}
	}

	private String saveProductLable(String listingid, int websiteId,
			List<ProductLabelType> types) {
		if (types == null || types.size() == 0) {
			return "";
		}
		String re = "";
		List<ProductLabel> existslist = this.productLabelEnquiryDao
				.getProductLabel(listingid);
		List<String> existTypesList = new ArrayList<String>();
		if (null != existslist && existslist.size() > 0) {
			existTypesList = Lists.transform(existslist, obj -> obj.getCtype());
		}
		for (ProductLabelType type : types) {
			if (existTypesList.contains(type.toString())) {
				continue;
			}
			ProductLabel pl = new ProductLabel();
			pl.setCcreateuser("");
			pl.setClistingid(listingid);
			pl.setCtype(type.toString());
			pl.setIwebsiteid(websiteId);
			this.productLabelUpdateDao.insertOrUpdate(pl);
		}
		return re;
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
		
		if (pitem.getIsAllFreeShipping() != null && pitem.getIsAllFreeShipping()) {
			ptype.add(ProductLabelType.AllFreeShipping);
		}
		return ptype;
	}

	private Boolean saveStorage(ProductBack pitem, String username,
			String listingid) {
		return saveStorage(pitem.getStorages(), pitem.getSku(), username,
				listingid);
	}

	/* @Transactional */
	private Boolean saveStorage(List<Integer> storyids, String sku,
			String username, String listingid) {
		List<ProductStorageMap> list = Lists.transform(storyids, id -> {
			ProductStorageMap pmap = new ProductStorageMap();
			pmap.setCcreateuser(username);
			pmap.setClistingid(listingid);
			pmap.setCsku(sku);
			pmap.setDcreatedate(new Date());
			pmap.setIstorageid(id);
			return pmap;
		});
		int rows = this.productStorageMapUpdateDao.addProductStorageList(list);
		return rows > 0;
	}

	private Boolean saveProductEntiytMap(List<AttributeItem> aitem, String sku,
			Integer websiteid, String username, String listingid,
			Boolean isMutil) {
		if (aitem == null || aitem.size() == 0)
			return true;
		List<String> sameAttribute = new ArrayList<String>();
		List<ProductEntityMap> list = new ArrayList<ProductEntityMap>();
		List<ProductEntityMap> pemlist = productEntityMapMapper
				.getProductEntityMapByListingid(listingid);
		Map<String, ProductEntityMap> existmap = Maps.newHashMap();
		List<Integer> multiexistkeyids = Lists.newArrayList();
		if (pemlist != null) {
			existmap = Maps.uniqueIndex(pemlist, obj -> obj.getIkey() + "-"
					+ obj.getIvalue());
			multiexistkeyids = Lists.transform(pemlist, obj -> obj.getIkey());
		}
		for (AttributeItem obj : aitem) {
			Attribute att = getAttributeId(obj);
			obj.setKeyId(att.getKeyId());
			obj.setValueId(att.getValueId());
			obj.setLanguangeId(att.getLanguageId());
			String key = obj.getKeyId() + "-" + obj.getValueId();
			// 重复
			if (pemlist != null && existmap.containsKey(key)) {
				continue;
			}
			// 多属性同一个key只能有1个value
			if (isMutil && multiexistkeyids.contains(obj.getKeyId())) {
				continue;
			}
			if (sameAttribute.contains(key)) {
				continue;
			}
			sameAttribute.add(key);
			ProductEntityMap pmap = new ProductEntityMap();
			pmap.setBshowimg(obj.getShowImg());
			pmap.setIkey(obj.getKeyId());
			pmap.setClistingid(listingid);
			pmap.setCsku(sku);
			pmap.setMultiattribute(isMutil);
			pmap.setIvalue(obj.getValueId());
			pmap.setIwebsiteid(websiteid);
			pmap.setCcreateuser(username);
			pmap.setDcreatedate(new Date());
			pmap.setBshow(obj.getVisible());
			list.add(pmap);
		}
		int rows = 0;
		if (list.size() > 0)
			rows = productEntityMapMapper.batchInsert(list);
		return rows > 0;
	}

	private Attribute getAttributeId(AttributeItem item) {
		Attribute attr = new Attribute();
		attr.setKey(item.getKey().trim());
		attr.setValue(item.getValue());
		if (item.getKey() == null || item.getValue() == null)
			return attr;
		attr.setLanguageId(item.getLanguangeId());
		if (item.getKeyId() != null && item.getValueId() != null) {
			attr.setKeyId(item.getKeyId());
			attr.setValueId(item.getValueId());
			return attr;
		}
		return attributeUpdateService.saveAttribute(attr, "");
	}

	private Boolean saveProductVideo(ProductBack pitem, String username,
			String listingid) {
		if (pitem.getVideos() == null || pitem.getVideos().size() == 0) {
			return true;
		}
		List<ProductVideo> list = Lists.transform(pitem.getVideos(),
				itembase -> {
					ProductVideo pt = new ProductVideo();
					pt.setCcreateuser(username);
					pt.setClabel(itembase.getLabel());
					pt.setClistingid(listingid);
					pt.setCsku(pitem.getSku());
					pt.setCvideourl(itembase.getVideoUrl());
					pt.setDcreatedate(new Date());
					return pt;
				});
		int rows = this.productVideoUpdateDao.addProductVideoList(list);
		
		return rows > 0;
	}

	private Boolean addProductImage(List<ImageItem> imglist, String sku,
			String listingid) {
		if (imglist == null || imglist.size() == 0) {
			return true;
		}
		List<ProductImage> list = convertImageToEntity(imglist, listingid, sku);
		int rows = productImageMapper.batchInsert(list);
		
		
		List<ProductBack> plist = productEnquiryService
				.getProductsByListingIds(Lists.newArrayList(listingid));
		String json = elasticsearchService.getElasticsearchJson(plist.get(0));
		elasticsearchService.updateProductPart(listingid, json);
		
		return rows > 0;
	}
	
	private Boolean saveProductImage(List<ImageItem> imglist, String sku,
			String listingid) {
		if (imglist == null || imglist.size() == 0) {
			return true;
		}
		List<ProductImage> list = convertImageToEntity(imglist, listingid, sku);
		int rows = productImageMapper.batchInsert(list);
		
		return rows > 0;
	}

	private List<ProductImage> convertImageToEntity(List<ImageItem> imglist,
			String listingid, String sku) {
		
		// 这个地方的图片需要处理 1. 图片路径修改为相对路径 2.保存原始路径
		Map<String, String> prefixMapping = Maps.newHashMap();
		if(this.configInfo.getGuphotosImages() == null){
			throw new  RuntimeException("Images config info not read!");
		}
		prefixMapping.put(this.configInfo.getGuphotosImages().split(",")[0],
				this.configInfo.getGuphotosImages().split(",")[1]);
		prefixMapping.put(this.configInfo.getGuphotosListingImages().split(",")[0],
				this.configInfo.getGuphotosListingImages().split(",")[1]);
		prefixMapping.put(this.configInfo.getTomtopImage().split(",")[0],
				this.configInfo.getTomtopImage().split(",")[1]);

		imglist = Lists.transform(imglist, obj -> {
			obj.setCoriginalurl(obj.getImageUrl());
			obj.setBfetch(false);
			for(Map.Entry<String, String> prefix:prefixMapping.entrySet()){
				String imageUrl = obj.getImageUrl();
				if(obj.getImageUrl().contains(prefix.getKey())){
					imageUrl = imageUrl.replace(prefix.getKey(), prefix.getValue());
					obj.setImageUrl(imageUrl);
				}
			}
			
			String imgUrl = obj.getImageUrl();
			if(null != imgUrl && imgUrl.startsWith("http")){
				int index = imgUrl.indexOf("/", 7);
				imgUrl = imgUrl.substring(index + 1);
				obj.setImageUrl(imgUrl);
			}

			return obj;
		});
		
		List<ProductImage> list = Lists.transform(imglist, itembase -> {
			ProductImage pt = new ProductImage();
			pt.setBbaseimage(itembase.getBaseImage());
			pt.setBsmallimage(itembase.getSmallImage());
			pt.setBthumbnail(itembase.getThumbnail());
			pt.setCimageurl(itembase.getImageUrl());
			pt.setClabel(itembase.getLabel());
			pt.setClistingid(listingid);
			pt.setCsku(sku);
			pt.setIorder(itembase.getOrder());
			pt.setBshowondetails(itembase.getShowOnDetails());
			pt.setBfetch(itembase.getBfetch());
			pt.setCoriginalurl(itembase.getCoriginalurl());
			return pt;
		});
		return list;
	}

	private Boolean saveProductTranslate(List<TranslateItem> transItems,
			String listingid, String sku) {
		if (transItems == null || transItems.size() == 0) {
			return true;
		}
		int rows = 0;
		List<ProductTranslate> list = Lists.transform(transItems, itembase -> {
			return this.getTranslateItem(itembase, listingid, sku);
		});
		rows += this.productTranslateUpdateDao.addProductTranslateList(list);
		return rows > 0;
	}

	private ProductTranslate getTranslateItem(TranslateItem itembase,
			String listingid, String sku) {
		ProductTranslate pt = new ProductTranslate();
		pt.setIlanguageid(itembase.getLanguageId());
		pt.setCdescription(itembase.getDescription());
		pt.setCkeyword(itembase.getKeyword());
		pt.setClistingid(listingid);
		pt.setCmetadescription(itembase.getMetaDescription());
		pt.setCmetakeyword(itembase.getMetaKeyword());
		pt.setCmetatitle(itembase.getMetaTitle());
		pt.setCshortdescription(itembase.getShortDescription());
		pt.setCsku(sku);
		pt.setCtitle(itembase.getTitle());
		return pt;
	}

	private Boolean saveProductSellingPoint(ProductBack pitem, String username,
			String listingid) {
		if (null == pitem.getItems()) {
			return true;
		}
		List<List<ProductSellingPoints>> list = Lists
				.transform(
						pitem.getItems(),
						item -> {
							if (null != item.getSellingPoints()) {
								List<ProductSellingPoints> tlist = Lists.transform(
										item.getSellingPoints(),
										point -> {
											ProductSellingPoints pspoint = new ProductSellingPoints();
											pspoint.setCcontent(point);
											pspoint.setCcreateuser(username);
											pspoint.setClistingid(listingid);
											pspoint.setCsku(pitem.getSku());
											pspoint.setDcreatedate(new Date());
											pspoint.setIlanguageid(item
													.getLanguageId());
											return pspoint;
										});
								return tlist;
							}
							return null;
						});
		List<ProductSellingPoints> rlist = Lists.newArrayList();
		for (List<ProductSellingPoints> points : list) {
			if (null != points) {
				rlist.addAll(points);
			}
		}
		if (rlist.size() <= 0)
			return true;
		int rows = this.productSellingPointsUpdateDao.addBatch(rlist);
		return rows > 0;
	}

	private Boolean saveProductCategory(ProductBack pitem, String username,
			String listingid) {
		if (pitem.getCategoryIds() == null
				|| pitem.getCategoryIds().size() == 0) {
			return false;
		}
		List<ProductCategoryMapper> list = Lists.transform(
				pitem.getCategoryIds(), cid -> {
					ProductCategoryMapper pc = new ProductCategoryMapper();
					pc.setCcreateuser(username);
					pc.setClistingid(listingid);
					pc.setCsku(pitem.getSku());
					pc.setDcreatedate(new Date());
					pc.setIcategory(cid);
					return pc;
				});
		int rows = productCategoryMapperMapper.batchInsert(list);
		return rows > 0;
	}

	private String createValidation(ProductBack pitem) {
		return productValidation.valProduct(pitem);
	}
	
	
	private List<ProductBase> validateBySkuAndWebsiteId(String sku, Integer websiteId) {
		return productValidation.validateBySkuAndWebsiteId(sku, websiteId);
	}

	private String generationListingId() {
		return com.rabbit.services.UUIDGenerator.createAsString();
	}

	private Boolean needSetlistingToMain(Boolean main, int websiteid,
			String parentSku) {
		List<String> mainListings = productBaseMapper
				.getMultiProductMainListing(parentSku, websiteid);
		System.out.println(parentSku + "--parens->" + mainListings.size());
		if (main && mainListings != null && mainListings.size() > 0) {
			// ~ delete exists main listing delete
			for (String ls : mainListings) {
				productBaseMapper.updateMainListing(ls, false);

				elasticsearchService.updateProductPart(ls, "{\"bmain\":false}");

				// eventBus.post(new ProductUpdateEvent(ls,
				// ProductUpdateEvent.ProductHandleType.delete));
			}
		}
		// ~ set first record to main listing
		if (main == false && (mainListings == null || mainListings.size() == 0)) {
			return true;
		}
		return main;
	}

	@Override
	public void delete(String listingId, String psku, String sku) {
		productBaseUpdateDao.deleteByListing(listingId);
		this.productUrlUpdateDao.deleteByListingId(listingId);
		productCategoryMapperMapper.deleteByListingId(listingId);
		this.productSellingPointsUpdateDao.deleteByListingId(listingId);
		this.productTranslateUpdateDao.deleteByListingId(listingId);
		productImageMapper.deleteByListingId(listingId);
		this.productVideoUpdateDao.deleteByListingId(listingId);
		productEntityMapMapper.deleteByListingId(listingId);
		this.productStorageMapUpdateDao.deleteByListingId(listingId);
		productMultiattributeSkuMapper.deleteBySku(sku);
		this.productLabelUpdateDao.deleteByListingId(listingId);

	}

	@Override
	public Boolean saveProductBase(ProductBack pitem, String username,
			String listingid) {
		ProductBase pbase = new ProductBase();
		pbase.setBmultiattribute(false);
		pbase.setCparentsku(null);
		if (pitem instanceof MultiProduct) {
			pbase.setBmultiattribute(true);
			pbase.setCparentsku(((MultiProduct) pitem).getParentSku());
			pbase.setBmain(((MultiProduct) pitem).getMainSku());
		}
		pbase.setBpulish(true);
		pbase.setBvisible(pitem.getVisible());
		pbase.setCcreateuser(username);
		pbase.setClistingid(listingid);

		pbase.setCsku(pitem.getSku());
		pbase.setDcreatedate(new Date());
		pbase.setDnewformdate(pitem.getNewFromDate());
		pbase.setDnewtodate(pitem.getNewToDate());
		pbase.setFprice(pitem.getPrice());
		pbase.setFweight(pitem.getWeight());

		pbase.setIqty(pitem.getQty());
		pbase.setBspecial(pitem.getSpecial());
		pbase.setIstatus(pitem.getStatus());
		pbase.setIwebsiteid(pitem.getWebsiteId());
		pbase.setFcostprice(pitem.getCost());
		pbase.setFfreight(pitem.getFreight());
		pbase.setBactivity(false);

		int rows = productBaseMapper.insert(pbase);
		return rows > 0;
	}

	@Override
	public Boolean saveProductUrl(ProductBack pitem, String username,
			String listingid) {
		List<ProductUrl> list = Lists.transform(
				pitem.getItems(),
				itembase -> {
					ProductUrl pt = new ProductUrl();
					pt.setCcreateuser(username);
					pt.setClistingid(listingid);
					if (null == itembase.getUrl()
							|| itembase.getUrl().trim().length() <= 0) {
						String nurl = "";
						if (itembase.getLanguageId() == 1) {
							nurl = itembase.getTitle().trim()
									.replaceAll("[^a-zA-Z0-9]+", "-") + "-"
									+ pitem.getSku();
						} else {
							nurl = itembase.getTitle().trim()
									.replaceAll("[\\pP\\p{Punct}]", "")
									.replaceAll("[\\s]+", "-")
									+ "-" + pitem.getSku();
						}
						pt.setCurl(nurl.toLowerCase());
					} else {
						pt.setCurl(itembase.getUrl());
					}
					pt.setIwebsiteid(pitem.getWebsiteId());
					pt.setDcreatedate(new Date());
					pt.setIlanguageid(itembase.getLanguageId());
					pt.setCsku(pitem.getSku());
					return pt;
				});
		int rows = this.productUrlUpdateDao.addProductUrlList(list);
		return rows > 0;
	}

	@Override
	public List<ProductTranslate> getLanguageidByListingid(String clistingid) {
		return this.productTranslateEnquityDao
				.getLanguageIdByListingid(clistingid);
	}

	@Override
	public List<ProductTranslate> getTitleByClistingids(List<String> clistingids) {
		return this.productTranslateEnquityDao.getTitleByClistings(clistingids);
	}

	@Override
	public List<ProductUrl> getUrlByClistingIds(List<String> clistingids) {
		return this.productUrlEnquityDao
				.getProductUrlByClistingids(clistingids);
	}

	@Override
	public void insertBundle(ProductBundleSale productBundleSale) {
		String listingid = productBundleSale.getClistingid();
		String bundListing = productBundleSale.getCbundlelistingid();
		if (productBundleSaleEnquiryDao.isExist(listingid, bundListing)) {
			productBundleSaleUpdateDao.activeBundle(listingid, bundListing);
		} else {
			productBundleSaleUpdateDao.insert(productBundleSale);
		}

	}

	@Override
	public void deleteAutoBundle() {
		productBundleSaleUpdateDao.deleteAuto();

	}

	@Override
	public void alterAutoBundleSaleVisible() {
		productBundleSaleUpdateDao.alterAutoBundleSaleVisible();

	}

	@Override
	/* @Transactional */
	public boolean saveOrUpdateProductCategory(
			ProductCategoryMapper productCategoryMapper) {
		ProductCategoryMapper categoryMapper = productCategoryMapperMapper
				.getProductCategoryMapperByListingIdAndCategoryId(
						productCategoryMapper.getClistingid(),
						productCategoryMapper.getIcategory());
		if (categoryMapper != null) {
			return true;
		} else {
			return productCategoryMapperMapper
					.insertSelectiveNew(productCategoryMapper) > 0;
		}
	}

	@Override
	public List<Integer> getProductCategoryMapperByListingId(String listingId) {
		return productCategoryMapperMapper.getCategoryIdByListingId(listingId);
	}

	@Override
	public boolean updateProductCategoryWithSomeListingId(
			ArrayList<ProductCategoryMapper> productCategoryMappers,
			String listingId) {
		try {
			int deleteByListingId = productCategoryMapperMapper
					.deleteByListingId(listingId);
			if (deleteByListingId > 0) {
				for (ProductCategoryMapper productCategoryMapper : productCategoryMappers) {
					saveOrUpdateProductCategory(productCategoryMapper);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean updateByListingIdSelective(ProductBase product) {
		// TODO Auto-generated method stub
		int i = productBaseMapper.updateByListingIdSelective(product);
		if (1 == i) {
			if (product.getIstatus() != 1) {
				updateProductStatusAndQuantityAndLabelService
						.deleteProductIndexing(product.getClistingid());
			}
			return true;
		}
		return false;
	}

	@Override
	public String saveProductPromotions(
			List<PromotionPrice> ProductPromotionlist, String user) {
		if (null == ProductPromotionlist || ProductPromotionlist.size() == 0) {
			return "";
		}

		Multimap<Integer, PromotionPrice> muMap = Multimaps.index(
				ProductPromotionlist, obj -> obj.getWebsiteId());
		String result = "";
		for (Integer wid : muMap.keySet()) {

			Collection<PromotionPrice> fixPrice = muMap.get(wid);
			Collection<String> skus = Collections2.transform(fixPrice, obj -> {
				return obj.getSku();
			});

			try {
				List<ProductBase> products = productBaseMapper.getProducts(wid,
						Lists.newArrayList(skus));
				if (null == products || products.size() == 0) {
					result += " not fount sku listingid: "
							+ JSON.toJSONString(skus) + System.lineSeparator();
					continue;
				}
				List<String> alllistingIds = Lists.transform(products,
						obj -> obj.getClistingid());

				List<String> priceexistslistingIds = this.productSalePriceEnquityDao
						.getExistsListings(alllistingIds);
				if (null != priceexistslistingIds
						&& priceexistslistingIds.size() > 0) {
					Collection<ProductBase> existsskus = Collections2.filter(
							products, pr -> priceexistslistingIds.contains(pr
									.getClistingid()));
					Collection<String> errorsku = Collections2.transform(
							existsskus,
							obj -> "{websiteId:" + obj.getIwebsiteid()
									+ ",sku:\"" + obj.getCsku() + "\"}");
					result += " fount sku listingid: "
							+ JSON.toJSONString(errorsku)
							+ System.lineSeparator();
				}

				Collection<ProductBase> needfixObjs = Collections2.filter(
						products, pr -> (false == priceexistslistingIds
								.contains(pr.getClistingid())));
				if (needfixObjs == null || needfixObjs.size() == 0)
					return result;
				Map<String, ProductBase> needfixskuMap = Maps.uniqueIndex(
						needfixObjs, obj -> obj.getCsku());
				Collection<PromotionPrice> needfixprice = Collections2.filter(
						fixPrice,
						obj -> needfixskuMap.keySet().contains(obj.getSku()));

				Collection<ProductSalePrice> entitys = Collections2.transform(
						needfixprice, obj -> {

							ProductSalePrice psp = new ProductSalePrice();
							psp.setCcreateuser(user);
							psp.setClistingid(needfixskuMap.get(obj.getSku())
									.getClistingid());
							psp.setCsku(obj.getSku());
							psp.setDbegindate(obj.getBeginDate());
							psp.setDenddate(obj.getEndDate());
							psp.setFsaleprice(obj.getPrice());
							return psp;
						});
				this.productSalePriceUpdateDao.addBatch(Lists
						.newArrayList(entitys));
			} catch (Exception ex) {
				ex.printStackTrace();
				result += "error: " + ex.getMessage() + "fix skus : "
						+ JSON.toJSONString(skus) + System.lineSeparator();
			}
		}
		return result;
	}

	@Override
	public boolean saveOrUpdateProductSalePrice(
			ProductSalePrice productSalePrice) {
		if (productSalePrice.getIid() != null) {
			return this.productSalePriceUpdateDao
					.updateByPrimaryKeySelective(productSalePrice) > 0;
		} else {
			return this.productSalePriceUpdateDao
					.addProductSalePrice(productSalePrice) > 0;
		}
	}

	@Override
	public int updateCostPrice(String sku, int websiteid, double costprice) {
		return productBaseMapper.updateCostPrice(sku, websiteid, costprice);
	}

	@Override
	public String saveProductCategory(String sku, String categoryPath,
			int websiteid) {

		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteid);
		if (pbases == null) {
			return "sku invalid: " + sku + System.lineSeparator();
		}
		List<Integer> ids = categoryEnquiryService.getCategoryIds(categoryPath);
		if (ids == null || ids.size() == 0) {
			return "category path invalid: " + categoryPath
					+ System.lineSeparator();
		}

		for (ProductBase pbase : pbases) {

			productCategoryMapperMapper
					.deleteByListingId(pbase.getClistingid());
			for (Integer id : ids) {
				ProductCategoryMapper productCategoryMapper = new ProductCategoryMapper();
				productCategoryMapper.setClistingid(pbase.getClistingid());
				productCategoryMapper.setCsku(sku);
				productCategoryMapper.setDcreatedate(new Date());
				productCategoryMapper.setIcategory(id);
				this.saveOrUpdateProductCategory(productCategoryMapper);
			}

			elasticsearchService.addMonitorRecord(pbase.getCsku(),
					"RABBIT_PRODUCT_PUSH_PRODUCT_CATEGORY_TYPE", "1",
					JSONObject.toJSONString(categoryPath), "");
			
			List<ProductBack> plist = productEnquiryService
					.getProductsByListingIds(Lists.newArrayList(pbase
							.getClistingid()));
			String json = elasticsearchService.getElasticsearchJson(plist.get(0));
			elasticsearchService.updateProductPart(pbase.getClistingid(), json);
			// eventBus.post(new ProductUpdateEvent(null, pbase.getClistingid(),
			// ProductUpdateEvent.ProductHandleType.update));
		}
		return "";
	}

	@Override
	public String saveProductCategoryNew(String sku, List<Integer> ids,
			int websiteid) {
		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteid);
		if (pbases == null) {
			return "sku invalid: " + sku + System.lineSeparator();
		}
		if (ids == null || ids.size() == 0) {
			return "category ids invalid: " + ids + System.lineSeparator();
		}
		for (ProductBase pbase : pbases) {

			// 设置类目
			List<ProductTypeEntity> productTypes = new ArrayList<ProductTypeEntity>();
			productCategoryMapperMapper
					.deleteByListingId(pbase.getClistingid());
			for (Integer id : ids) {
				ProductCategoryMapper productCategoryMapper = new ProductCategoryMapper();
				productCategoryMapper.setClistingid(pbase.getClistingid());
				productCategoryMapper.setCsku(sku);
				productCategoryMapper.setDcreatedate(new Date());
				productCategoryMapper.setIcategory(id);
				this.saveOrUpdateProductCategory(productCategoryMapper);

				JSONObject json = new JSONObject();
				json.put("productTypes", productTypes);

			}

			List<ProductBack> plist = productEnquiryService
					.getProductsByListingIds(Lists.newArrayList(pbase
							.getClistingid()));
			String json = elasticsearchService.getElasticsearchJson(plist.get(0));
			elasticsearchService.updateProductPart(pbase.getClistingid(), json);
			// eventBus.post(new ProductUpdateEvent(null, pbase.getClistingid(),
			// ProductUpdateEvent.ProductHandleType.update));
		}
		return "";
	}

	@Override
	public void insertProductLabel(ProductLabel productLabel) {
		this.productLabelUpdateDao.insertOrUpdate(productLabel);

	}

	@Override
	public void deleteProductLabel(int websiteId, String type) {
		productLabelUpdateDao.deleteBySiteAndType(websiteId, type);

	}

	@Override
	public int updatePrice(String sku, int website, Double price, Double cost,
			Double freight, Boolean freeShipping) {
		
		log.debug("updatePrice--->sku:"+sku+" website:"+website+" price:"+price+" cost:"+cost
				+"  freight:"+freight+"  freeShipping:"+freeShipping);
		synchronized(updatePriceLock) { //加锁处理线程
			int rows = productBaseMapper.updatePrice(sku, website, price, freight,
					cost);
			List<String> types = Lists.newArrayList(ProductLabelType.FreeShipping
					.toString());
			if (freeShipping) {
				this.addProductLable(sku, website, Lists
						.newArrayList(ProductLabelType.FreeShipping.toString()));
			} else {
				this.deleteProductLabel(sku, website, types);
			}
			
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("price", price);
			jsonobj.put("cost", cost);
			jsonobj.put("freight", freight);
			jsonobj.put("freeShipping", freeShipping);
			elasticsearchService.addMonitorRecord(sku,
					"RABBIT_PRODUCT_UPDATE_PRICE_TYPE", "1",
					jsonobj.toJSONString(), "");
			
			if (rows > 0) {
				List<ProductBase> pbases = productBaseEnquityDao
						.getProductBaseBySkuAndWebsiteId(sku, website);
				for (ProductBase pbase : pbases) {
					List<ProductBack> plist = productEnquiryService
							.getProductsByListingIds(Lists.newArrayList(pbase
									.getClistingid()));
					String json = elasticsearchService.getElasticsearchJson(plist.get(0));
					elasticsearchService.updateProductPart(pbase.getClistingid(),
							json);
				}
			}
			return rows;
		}
	}

	@Override
	public int updateStatus(String sku, int website, Integer status) {
		// ~ 更新主产品的时候要将另一个同属spu的产品改为主产品
		log.debug("updateStatus--->sku:"+sku+" website:"+website+" status:"+status);
		synchronized(updateStatusLock) { //加锁处理线程
		
			int rows = productBaseUpdateDao.updateStatusBySkuAndSite(sku, website,
					status);
			
			List<ProductBase> pbases = productBaseEnquityDao
					.getProductBaseBySkuAndWebsiteId(sku, website);
			if (rows > 0) {
				
				for (ProductBase pbase : pbases) {
					if (status == 1) {
						if (pbase.getCparentsku() != null) {
							String parentSku = pbase.getCparentsku();
							List<String> listings = productBaseEnquityDao
									.getListingIdsBySpus(
											Lists.newArrayList(parentSku),
											pbase.getIwebsiteid());
							List<ProductBase> pbaselist = productBaseEnquityDao
									.getRelatedSkuByListingids(listings);
							List<ProductBase> activebaselist = FluentIterable
									.from(pbaselist)
									.filter(p -> p.getIwebsiteid() == website
									&& p.getIstatus() == status).toList();
							if (activebaselist != null && activebaselist.size() > 0) {
								if (null == FluentIterable
										.from(activebaselist)
										.filter(p -> p.getBmain() == true
										&& p.getBvisible() == true).first()
										.orNull()) {
									productBaseUpdateDao
									.updateProductMainAndVisible(
											pbase.getClistingid(), true,
											true);
								}
							}
						}
						
						
						elasticsearchService.addMonitorRecord(pbase.getCsku(),
								"RABBIT_PRODUCT_UPDATE_STATUS_TYPE", "1",
								JSONObject.toJSONString(status), "");
						
						List<ProductBack> plist = productEnquiryService
								.getProductsByListingIds(Lists.newArrayList(pbase
										.getClistingid()));
						String json = elasticsearchService.getElasticsearchJson(plist.get(0));
						elasticsearchService.updateProductPart(
								pbase.getClistingid(), json);
						// eventBus.post(new
						// ProductUpdateEvent(pbase.getClistingid(),
						// ProductUpdateEvent.ProductHandleType.update));
					} else {
						
						updateProductStatusAndQuantityAndLabelService
						.deleteProductIndexing(pbase.getClistingid());
					}
				}
			}
			
			for (ProductBase pbase : pbases) {
				JSONObject json = new JSONObject();
				json.put("status", status);
				elasticsearchService.updateProductPart(
						pbase.getClistingid(), json.toJSONString());
			}
			return rows;
		} 
	}

	@Override
	public int addUrl(String sku, int website, String url, Integer langid) {
		if (this.productUrlEnquityDao.getProductUrlByUrl(url, website, langid) == null) {
			List<ProductBase> pbases = productBaseEnquityDao
					.getProductBaseBySkuAndWebsiteId(sku, website);
			if (pbases != null && pbases.size() > 0) {
				ProductBase pbase = pbases.get(0);
				ProductUrl purl = new ProductUrl();
				purl.setCsku(sku);
				purl.setCurl(url);
				purl.setIwebsiteid(website);
				purl.setIlanguageid(langid);
				purl.setClistingid(pbase.getClistingid());
				int rows = productUrlUpdateDao.addProductUrl(purl);
				
				elasticsearchService.addMonitorRecord(sku,
						"RABBIT_PRODUCT_ADD_URL_TYPE", "1",
						JSONObject.toJSONString(url), "");
				
				if (rows > 0) {
					List<ProductBack> plist = productEnquiryService
							.getProductsByListingIds(Lists.newArrayList(pbase
									.getClistingid()));
					String json = elasticsearchService.getElasticsearchJson(plist.get(0));
					elasticsearchService.updateProductPart(
							pbase.getClistingid(), json);
					// eventBus.post(new
					// ProductUpdateEvent(pbase.getClistingid(),
					// ProductUpdateEvent.ProductHandleType.update));
				}
			}
		}
		return 0;
	}

	@Override
	public String addSellpoints(String sku, Integer websiteId,
			Integer languageId, List<String> points, String userName) {
		if (points == null) {
			return "invalid sell points.";
		}
		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
		if (pbases == null) {
			return "invalid sku " + sku;
		}
		for (ProductBase pbase : pbases) {
			List<ProductSellingPoints> existlist = this.productSellingPointsEnquityDao
					.getProductSellingPointsByListingIdAndLanguage(
							pbase.getClistingid(), languageId);
			List<String> existssp = Lists.transform(existlist,
					obj -> obj.getCcontent());
			Collection<String> insp = Collections2.filter(points, obj1 -> {
				return existssp.contains(obj1) == false;
			});
			if (insp != null && insp.size() > 0) {
				List<ProductSellingPoints> rlist = Lists
						.transform(
								Lists.newArrayList(insp),
								obj -> {
									ProductSellingPoints ps = new ProductSellingPoints();
									ps.setCcontent(obj);
									ps.setCcreateuser(userName);
									ps.setClistingid(pbase.getClistingid());
									ps.setCsku(sku);
									ps.setDcreatedate(new Date());
									ps.setIlanguageid(languageId);
									return ps;
								});
				this.productSellingPointsUpdateDao.addBatch(rlist);

				List<ProductBack> plist = productEnquiryService
						.getProductsByListingIds(Lists.newArrayList(pbase
								.getClistingid()));
				String json = elasticsearchService.getElasticsearchJson(plist.get(0));
				elasticsearchService.updateProductPart(pbase.getClistingid(),
						json);
				// eventBus.post(new ProductUpdateEvent(null, pbase
				// .getClistingid(),
				// ProductUpdateEvent.ProductHandleType.update));
			}
		}
		return "";
	}

	@Override
	public String addProductImages(String sku, Integer websiteId,
			List<ImageItem> imgUrl) {
		try {
			List<ProductBase> pbases = productBaseEnquityDao
					.getProductBaseBySkuAndWebsiteId(sku, websiteId);
			if (pbases == null) {
				return "invalid sku " + sku;
			}
			for (ProductBase pbase : pbases) {
				List<ProductImage> existslist = productImageMapper
						.getProductImgsByListingId(pbase.getClistingid());
				List<String> existurls = Lists.transform(existslist,
						obj -> obj.getCimageurl());
				Collection<ImageItem> inurl = Collections2
						.filter(imgUrl,
								obj -> {
									return existurls.contains(obj.getImageUrl()) == false;
								});
				if (inurl != null && inurl.size() > 0)
					this.addProductImage(Lists.newArrayList(inurl), sku,
							pbase.getClistingid());
			}
			return "";
		} catch (Exception ex) {
			return "error: " + sku + "--" + ex.getMessage();
		}
	}

	@Override
	public String updateImages(String sku, Integer websiteId,
			List<ImageItem> imgUrl) {
		
		
		try {
			if (imgUrl == null || imgUrl.size() == 0) {
				return "";
			}
			List<ProductBase> pbases = productBaseEnquityDao
					.getProductBaseBySkuAndWebsiteId(sku, websiteId);
			if (pbases == null) {
				return "invalid sku " + sku;
			}
			for (ProductBase pbase : pbases) {
				List<ProductImage> list = this.convertImageToEntity(imgUrl,
						pbase.getClistingid(), pbase.getCsku());
				for (ProductImage pi : list) {
					productImageMapper.updateImages(pi);
				}
				
				elasticsearchService.addMonitorRecord(sku,
						"RABBIT_PRODUCT_ADD_PRODUCT_IMAGE_TYPE", "1",
						JSONObject.toJSONString(imgUrl), "");
				
				List<ProductBack> plist = productEnquiryService
						.getProductsByListingIds(Lists.newArrayList(pbase.getClistingid()));
				String json = elasticsearchService.getElasticsearchJson(plist.get(0));
				elasticsearchService.updateProductPart(pbase.getClistingid(), json);
			}
			return "";
		} catch (Exception ex) {
			return "error: " + sku + "--" + ex.getMessage();
		}
	}

	@Override
	public String addProductLable(String sku, Integer websiteId,
			List<String> types) {
		try {
			List<ProductBase> pbases = productBaseEnquityDao
					.getProductBaseBySkuAndWebsiteId(sku, websiteId);
			if (pbases == null) {
				return "invalid sku " + sku;
			}
			String re = "";
			for (ProductBase pbase : pbases) {
				Map<String, ProductLabelType> dbtypes = Maps.uniqueIndex(
						Arrays.asList(ProductLabelType.values()),
						obj -> obj.toString());
				List<ProductLabelType> inserttypes = new ArrayList<ProductLabelType>();
				for (String type : types) {
					if (type == null || type.length() == 0) {
						continue;
					}
					if (dbtypes.keySet().contains(type) == false) {
						re += " new found label: " + type
								+ System.lineSeparator();
						continue;
					}
					inserttypes.add(dbtypes.get(type));
				}
				if (inserttypes.size() > 0) {
					re += this.saveProductLable(pbase.getClistingid(),
							websiteId, inserttypes);

					elasticsearchService.addMonitorRecord(sku,
							"RABBIT_PRODUCT_ADD_LABEL_TYPE", "1",
							JSONObject.toJSONString(types), "");
					
					List<ProductBack> plist = productEnquiryService
							.getProductsByListingIds(Lists.newArrayList(pbase
									.getClistingid()));
					String json = elasticsearchService.getElasticsearchJson(plist.get(0));
					elasticsearchService.updateProductPart(
							pbase.getClistingid(), json);
					// eventBus.post(new ProductUpdateEvent(null, pbase
					// .getClistingid(),
					// ProductUpdateEvent.ProductHandleType.update));
				}
			}
			return re;
		} catch (Exception ex) {
			return "error: " + sku + "--" + ex.getMessage();
		}
	}

	@Override
	public boolean updateStorageMapper(List<Integer> storagesAdd,
			List<Integer> storagesDel, String listingId, String sku,
			String createuser) {
		boolean updateflag = true;
		if (!storagesAdd.isEmpty()) {
			updateflag = saveStorage(storagesAdd, sku, createuser, listingId);
		}
		if (!storagesDel.isEmpty() && updateflag) {
			return this.productStorageMapUpdateDao.deleteProductStorageList(
					listingId, storagesDel) > 0;
		}

		return updateflag;
	}

	@Override
	public boolean updateQty(int qty, String listingId, int websiteid) {
		int i = productBaseUpdateDao.updateQtyByListing(qty, listingId);
		if (1 == i) {
			this.updateQtyInventory(listingId, qty, websiteid);
			return true;
		}
		return false;
	}

	@Override
	public void updateQtyInventory(String listingid, Integer qty, int websiteid) {
		Integer currentqty = iInventoryHistoryEnquiryDao.getQty(listingid);
		log.debug("save qty {} {}" + qty + "     :" + currentqty);
		if (currentqty == null) {
			currentqty = 0;
		}
		if (currentqty == qty)
			return;

		InventoryHistory ihistory = new InventoryHistory();
		ihistory.setBenabled(true);
		ihistory.setClistingid(listingid);
		ihistory.setCreference("qty api modify");
		ihistory.setCtype(InventoryTypeEnum.UPDATE.getValue());
		ihistory.setDcreatedate(new Date());
		ihistory.setIafterchangeqty(qty);
		ihistory.setIbeforechangeqty(currentqty);
		ihistory.setIqty(qty - currentqty);
		ihistory.setIwebsiteid(websiteid);
		log.debug("save qty {}, {}" + qty + "   " + JSON.toJSONString(ihistory));
		iInventoryHistoryUpdateDao.add(ihistory);

	}

	@Override
	public String addTransate(String sku, int websiteId,
			List<TranslateItem> tranItems) {
		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
		if (pbases == null) {
			return "invalid sku " + sku;
		}
		for (ProductBase pbase : pbases) {
			List<ProductTranslate> existlist = this.productTranslateEnquityDao
					.getLanguageIdByListingid(pbase.getClistingid());
			List<Integer> langlist = Lists.transform(existlist,
					obj -> obj.getIlanguageid());
			Collection<TranslateItem> insertitems = Collections2.filter(
					tranItems,
					obj -> (langlist.contains(obj.getLanguageId()) == false));
			if (insertitems != null && insertitems.size() > 0) {
				saveProductTranslate(Lists.newArrayList(insertitems),
						pbase.getClistingid(), pbase.getCsku());

				elasticsearchService.addMonitorRecord(pbase.getCsku(),
						"RABBIT_PRODUCT_ADD_TRANSLATION_TYPE", "1",
						JSONObject.toJSONString(insertitems), "");
				
				List<ProductBack> plist = productEnquiryService
						.getProductsByListingIds(Lists.newArrayList(pbase
								.getClistingid()));
				String json = elasticsearchService.getElasticsearchJson(plist.get(0));
				elasticsearchService.updateProductPart(pbase.getClistingid(),
						json);
				// eventBus.post(new ProductUpdateEvent(null, pbase
				// .getClistingid(),
				// ProductUpdateEvent.ProductHandleType.update));
			}
		}
		return "";
	}

	@Override
	public String updateTransate(String sku, int websiteId,
			List<TranslateItem> tranItems) {
		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
		if (pbases == null) {
			return "invalid sku " + sku;
		}
		String re = "";
		for (ProductBase pbase : pbases) {
			for (TranslateItem titem : tranItems) {
				if (titem.getLanguageId() == null && titem.getLanguageId() <= 0) {
					re = "  not found language  " + titem.getLanguageId();
				}
				this.productTranslateUpdateDao
						.alterByListingIdAndLuanguage(this.getTranslateItem(
								titem, pbase.getClistingid(), pbase.getCsku()));
				// eventBus.post(new ProductUpdateEvent(null, pbase
				// .getClistingid(),
				// ProductUpdateEvent.ProductHandleType.update));

			}

			elasticsearchService.addMonitorRecord(sku,
					"RABBIT_PRODUCT_UPDAET_TRANSLATION_TYPE", "1",
					JSONObject.toJSONString(tranItems), "");
			
			List<ProductBack> plist = productEnquiryService
					.getProductsByListingIds(Lists.newArrayList(pbase
							.getClistingid()));
			String json = elasticsearchService.getElasticsearchJson(plist.get(0));
			elasticsearchService.updateProductPart(pbase.getClistingid(), json);
		}
		if (re.length() == 0)
			return "";
		else
			return sku + re;
	}

	@Override
	public String updateStorages(String sku, int websiteId,
			List<Integer> storages) {
		if (storages == null || storages.size() == 0)
			return "";
		List<Storage> storagelists = storageEnquiryService.getAllStorages();

		List<Integer> storageIds = Lists.transform(storagelists,
				obj -> obj.getIid());
		// productStorageMapMapper
		Collection<Integer> insertids = Collections2.filter(storages,
				id -> storageIds.contains(id));

		// 組裝更新elasticsearch更新數據
		List<DepotEntity> elaList = Lists.transform(storagelists, obj -> {
			DepotEntity de = new DepotEntity();
			de.setDepotid(obj.getIid());
			de.setDepotName(obj.getCstoragename());
			return de;
		});

		List<DepotEntity> searchList = Lists.newArrayList();
		for (DepotEntity obj : elaList) {
			Integer storageId = obj.getDepotid();
			if (storages.contains(storageId)) {
				searchList.add(obj);
			}
		}
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("depots", searchList);

		if (insertids == null || insertids.size() == 0) {
			return "not found storage";
		}
		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
		if (pbases == null) {
			return "invalid sku " + sku;
		}

		for (ProductBase pbase : pbases) {
			Collection<ProductStorageMap> inserlist = Collections2.transform(
					insertids, obj -> {
						ProductStorageMap psm = new ProductStorageMap();
						psm.setClistingid(pbase.getClistingid());
						psm.setCsku(sku);
						psm.setDcreatedate(new Date());
						psm.setIstorageid(obj);
						return psm;
					});
			this.productStorageMapUpdateDao.deleteByListingId(pbase
					.getClistingid());
			this.productStorageMapUpdateDao.addProductStorageList(Lists
					.newArrayList(inserlist));
			
			elasticsearchService.addMonitorRecord(pbase.getCsku(),
					"RABBIT_PRODUCT_UPDAET_STORAGE_TYPE", "1",
					JSONObject.toJSONString(jsonobj.toJSONString()), "");
			// 更新倉庫
			elasticsearchService.updateProductPart(pbase.getClistingid(),
					jsonobj.toJSONString());
			// eventBus.post(new ProductUpdateEvent(null, pbase.getClistingid(),
			// ProductUpdateEvent.ProductHandleType.update));
		}
		return "";
	}

	@Override
	public String updateFregiht(String sku, Integer websiteId, Double freight,
			Boolean freeShipping) {
		productBaseUpdateDao.updateFreightBySkuAndSite(sku, websiteId, freight);
		List<String> types = Lists.newArrayList(ProductLabelType.FreeShipping
				.toString());
		String result = "";
		if (freeShipping) {
			result = this.addProductLable(sku, websiteId, types);
		} else {
			result = this.deleteProductLabel(sku, websiteId, types);
		}
		return result;
	}

	@Override
	public String deleteProductLabel(String sku, Integer websiteId,
			List<String> types) {
		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
		if (pbases == null) {
			return "invalid sku " + sku;
		}
		for (ProductBase pbase : pbases) {
			for (String type : types) {
				productLabelUpdateDao.deleteByListingAndType(
						pbase.getClistingid(), type);
				// eventBus.post(new ProductUpdateEvent(null, pbase
				// .getClistingid(),
				// ProductUpdateEvent.ProductHandleType.update));
			}

			elasticsearchService.addMonitorRecord(sku,
					"RABBIT_PRODUCT_DELETE_LABEL_TYPE", "1",
					JSONObject.toJSONString(types), "");
			
			List<ProductBack> plist = productEnquiryService
					.getProductsByListingIds(Lists.newArrayList(pbase
							.getClistingid()));
			String json = elasticsearchService.getElasticsearchJson(plist.get(0));
			elasticsearchService.updateProductPart(pbase.getClistingid(), json);
		}
		return "";
	}

	@Override
	public String updateProductQty(String sku, Integer websiteId, Integer qty) {
		List<ProductBase> pbases = productBaseEnquityDao
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
		if (pbases == null) {
			return "invalid sku " + sku;
		}
		for (ProductBase pbase : pbases) {
			this.updateQtyInventory(pbase.getClistingid(), qty, websiteId);
			productBaseMapper.updateProductQty(qty, pbase.getClistingid());
			
			elasticsearchService.addMonitorRecord(pbase.getCsku(),
					"RABBIT_PRODUCT_UPDAET_QTY_TYPE", "1",
					JSONObject.toJSONString(qty), "");
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("storeNum", qty);

			elasticsearchService.updateProductPart(
					pbase.getClistingid(), jsonobj.toJSONString());
		}
		return "";
	}

	@Override
	public List<ProductSalePrice> getProductSalePriceAfterCurrentDate(
			Map<String, Object> paras) {
		return this.productSalePriceEnquityDao
				.getProductSalePriceAfterCurrentDate(paras);
	}

	@Override
	public int insertProductSalePrice(ProductSalePrice record) {

		int result = this.productSalePriceUpdateDao.addProductSalePrice(record);

		return result;
	}

	@Override
	public int setProductStatusBySku(String sku) {
		return productBaseMapper.setStatusBySku(sku);
	}

	@Override
	public void incrementViewCount(int siteID, String listingID) {
		if (this.productViewCountUpdateDao.alterViewCount(siteID, listingID) == 0) {
			this.productViewCountUpdateDao.addViewCount(siteID, listingID, 1);
		}

	}

	@Override
	public int insertProductRecommend(String clistinid,
			String crecommendlisting, String ccreateuser) {
		return this.productRecommendUpdateDao.insert(clistinid,
				crecommendlisting, ccreateuser);
	}

	@Override
	public String deleteMultiProductAttribute(String parentSku,
			Integer websiteId, String key, Integer lang) {
		Integer keyid = attributeEnquityDao.getKeyId(key, lang);
		if (keyid == null) {
			return "invalid key!";
		}
		List<ProductBase> pbases = productBaseMapper
				.getProductsWithSameParentSkuByParentSku(parentSku);
		if (null == pbases || pbases.size() == 0) {
			return parentSku + " not fount child skus!";
		}
		productMultiattributeAttributeMapper.deleteAttributeBySku(parentSku,
				websiteId, keyid);
		productMultiattributeBaseMapper.delete(parentSku, websiteId);
		for (ProductBase base : pbases) {
			productEntityMapMapper.deleteByListingIdAndKeyId(
					base.getClistingid(), keyid);
		}
		return "";
	}

	@Override
	public String deleteProductAttribute(String clistingid, String key,
			Integer lang) {
		Integer keyid = attributeEnquityDao.getKeyId(key, lang);
		if (keyid == null) {
			return "invalid key!";
		}
		productEntityMapMapper.deleteByListingIdAndKeyId(clistingid, keyid);
		return "";
	}

	@Override
	public String addProductAttribute(String sku, Integer websiteId,
			String listingid, List<AttributeItem> aitem, String username) {
		ProductBase pbase = productBaseEnquityDao
				.getProductBaseByListingId(listingid);
		if (pbase == null) {
			return "invalid listingid " + listingid;
		}
		if (pbase.getCsku().equals(sku) == false) {
			return "invalid sku " + sku;
		}
		this.saveProductEntiytMap(aitem, sku, websiteId, username,
				pbase.getClistingid(), false);
		
		
		List<ProductBack> plist = productEnquiryService
				.getProductsByListingIds(Lists.newArrayList(listingid));
		String json = elasticsearchService.getElasticsearchJson(plist.get(0));
		elasticsearchService.updateProductPart(listingid, json);

		return "";
	}

	@Override
	public int deleteProductCurrentSalePrice(String listingId) {
		int result = productSalePriceUpdateDao
				.deleteProductCurrentSalePrice(listingId);

		elasticsearchService.addMonitorRecord(listingId,
				"RABBIT_PRODUCT_DELETE_CURRENT_SALEPRICE_TYPE", "1",
				JSONObject.toJSONString(listingId), "");
		
		List<ProductBack> plist = productEnquiryService
				.getProductsByListingIds(Lists.newArrayList(listingId));
		String json = elasticsearchService.getElasticsearchJson(plist.get(0));
		elasticsearchService.updateProductPart(listingId, json);
		return result;
	}

	@Override
	public int deleteProductSalePriceByDate(String listingId, Date start,
			Date end) {
		int result = this.productSalePriceUpdateDao
				.deleteProductSalePriceByDate(listingId, start, end);

		JSONObject jsonobj = new JSONObject();
		jsonobj.put("listingId", listingId);
		jsonobj.put("start", start);
		jsonobj.put("end", end);
		elasticsearchService.addMonitorRecord(listingId,
				"RABBIT_PRODUCT_DELETE_SALE_PRICE_TYPE", "1",
				jsonobj.toJSONString(), "");
		
		List<ProductBack> plist = productEnquiryService
				.getProductsByListingIds(Lists.newArrayList(listingId));
		String json = elasticsearchService.getElasticsearchJson(plist.get(0));
		elasticsearchService.updateProductPart(listingId, json);

		return result;
	}

	@Override
	public int deleteProductSellingPoints(String listingId, int languageId) {
		return this.productSellingPointsUpdateDao
				.deleteByListingIdAndLanguageId(listingId, languageId);
	}

	@Override
	public boolean deleteProductImageByListingId(String listingId,
			Integer websiteId) {
		log.debug("deleteProductImageByListingId------->listingId:"+listingId+" websiteId:"+websiteId);
		synchronized(deleteImgLock) { //加锁处理线程
		
			List<ProductImage> productImgs = productImageMapper
					.getProductImgsByListingId(listingId);
			if (null == productImgs || 0 >= productImgs.size()) {
				return true;
			}
			List<String> paths = productImgs.stream()
					.map(ProductImage::getCimageurl).collect(Collectors.toList());
			imageUpdateService.deleteImByPaths(paths);
			int deleteByListingId = productImageMapper.deleteByListingId(listingId);
			
			elasticsearchService.addMonitorRecord(listingId,
					"RABBIT_PRODUCT_DELETE_IMAGE_TYPE", "1",
					JSONObject.toJSONString(listingId), "");
			
			List<ProductBack> plist = productEnquiryService
					.getProductsByListingIds(Lists.newArrayList(listingId));
			String json = elasticsearchService.getElasticsearchJson(plist.get(0));
			elasticsearchService.updateProductPart(listingId, json);
			
			return deleteByListingId > 0 ? true : false;
		}
	}

	@Override
	public String addProductCategoryMapper(List<String> listingIdList,
			List<Integer> categoryIds) {
		String re = "";
		List<SkuRelateListingId> srlList = Lists.newArrayList();
		try {
			if (null != listingIdList && listingIdList.size() > 0) {
				srlList = productBaseMapper.getSkuByListingIds(listingIdList);
			}
			if (null != srlList && srlList.size() > 0) {
				if (null != categoryIds && categoryIds.size() > 0) {
					for (SkuRelateListingId srl : srlList) {
						for (Integer categoryId : categoryIds) {
							ProductCategoryMapper pcm = new ProductCategoryMapper();
							pcm.setClistingid(srl.getClistingid());
							pcm.setCsku(srl.getCsku());
							pcm.setIcategory(categoryId);
							productCategoryMapperMapper.insertSelectiveNew(pcm);
						}
					}
				} else {
					re += "no categoryId";
				}
			} else {
				re += "no listingId";
			}
		} catch (Exception e) {
			re += "addProductCategoryMapper error : " + e.getMessage();
		}

		for (String listingId : listingIdList) {
			elasticsearchService.addMonitorRecord(listingId,
					"RABBIT_PRODUCT_ADD_CATEGORY_TYPE", "1",
					JSONObject.toJSONString(listingId), "");

			List<ProductBack> plist = productEnquiryService
					.getProductsByListingIds(Lists.newArrayList(listingId));
			String json = elasticsearchService.getElasticsearchJson(plist
					.get(0));
			elasticsearchService.updateProductPart(listingId, json);
		}
		return re;
	}

}
