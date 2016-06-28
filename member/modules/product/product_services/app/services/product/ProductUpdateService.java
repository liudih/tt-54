package services.product;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductEntityMapMapper;
import mapper.product.ProductImageMapper;
import mapper.product.ProductMultiattributeAttributeMapper;
import mapper.product.ProductMultiattributeBaseMapper;
import mapper.product.ProductMultiattributeSkuMapper;

import org.elasticsearch.common.collect.FluentIterable;
import org.elasticsearch.common.collect.Lists;
import org.mybatis.guice.transactional.Isolation;
import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import play.libs.Json;
import services.base.StorageService;
import services.common.UUIDGenerator;
import services.image.IImageUpdateService;
import services.product.inventory.InventoryTypeEnum;
import services.search.criteria.ProductLabelType;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.eventbus.EventBus;
import com.website.dto.Attribute;
import com.website.dto.product.AttributeItem;
import com.website.dto.product.ImageItem;
import com.website.dto.product.MultiProduct;
import com.website.dto.product.Product;
import com.website.dto.product.TranslateItem;

import dao.product.IAttributeEnquiryDao;
import dao.product.IInventoryHistoryEnquiryDao;
import dao.product.IInventoryHistoryUpdateDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductBaseUpdateDao;
import dao.product.IProductBundleSaleEnquiryDao;
import dao.product.IProductBundleSaleUpdateDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductLabelUpdateDao;
import dao.product.IProductRecommendUpdateDao;
import dao.product.IProductSalePriceEnquiryDao;
import dao.product.IProductSalePriceUpdateDao;
import dao.product.IProductSellingPointsEnquiryDao;
import dao.product.IProductSellingPointsUpdateDao;
import dao.product.IProductStorageMapUpdateDao;
import dao.product.IProductTranslateEnquiryDao;
import dao.product.IProductTranslateUpdateDao;
import dao.product.IProductUrlEnquiryDao;
import dao.product.IProductUrlUpdateDao;
import dao.product.IProductVideoUpdateDao;
import dao.product.IProductViewCountUpdateDao;
import dto.Storage;
import dto.product.InventoryHistory;
import dto.product.ProductBase;
import dto.product.ProductBundleSale;
import dto.product.ProductCategoryMapper;
import dto.product.ProductEntityMap;
import dto.product.ProductImage;
import dto.product.ProductLabel;
import dto.product.ProductMultiattributeAttribute;
import dto.product.ProductMultiattributeBase;
import dto.product.ProductMultiattributeSku;
import dto.product.ProductSalePrice;
import dto.product.ProductSellingPoints;
import dto.product.ProductStorageMap;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;
import dto.product.ProductVideo;
import dto.product.SkuRelateListingId;
import events.product.ProductUpdateEvent;

public class ProductUpdateService implements IProductUpdateService {

	@Inject
	ProductBaseMapper productBaseMapper;
	@Inject
	ProductCategoryMapperMapper productCategoryMapperMapper;
	@Inject
	ProductImageMapper productImageMapper;
	@Inject
	ProductMultiattributeAttributeMapper productMultiattributeAttributeMapper;
	@Inject
	ProductMultiattributeBaseMapper productMultiattributeBaseMapper;
	@Inject
	ProductMultiattributeSkuMapper productMultiattributeSkuMapper;
	@Inject
	ProductEntityMapMapper productEntityMapMapper;
	@Inject
	ProductValidation productValidation;
	@Inject
	IProductBundleSaleEnquiryDao productBundleSaleEnquiryDao;
	@Inject
	EventBus eventBus;
	@Inject
	CategoryEnquiryService categoryEnquiryService;
	@Inject
	AttributeUpdateService attributeUpdateService;
	@Inject
	StorageService storageEnquiryService;
	@Inject
	IProductBaseEnquiryDao productBaseEnquityDao;
	@Inject
	IProductBaseUpdateDao productBaseUpdateDao;
	@Inject
	IProductLabelUpdateDao productLabelUpdateDao;
	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;
	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;
	@Inject
	IProductUrlUpdateDao productUrlUpdateDao;
	@Inject
	IProductViewCountUpdateDao productViewCountUpdateDao;
	@Inject
	IProductVideoUpdateDao productVideoUpdateDao;
	@Inject
	IProductTranslateEnquiryDao productTranslateEnquityDao;
	@Inject
	IProductTranslateUpdateDao productTranslateUpdateDao;
	@Inject
	IAttributeEnquiryDao attributeEnquityDao;
	@Inject
	IProductBundleSaleUpdateDao productBundleSaleUpdateDao;
	@Inject
	IProductStorageMapUpdateDao productStorageMapUpdateDao;
	@Inject
	IProductSellingPointsEnquiryDao productSellingPointsEnquityDao;
	@Inject
	IProductSellingPointsUpdateDao productSellingPointsUpdateDao;
	@Inject
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;
	@Inject
	IProductSalePriceUpdateDao productSalePriceUpdateDao;
	@Inject
	UpdateProductStatusAndQuantityAndLabelService updateProductStatusAndQuantityAndLabelService;
	@Inject
	IProductRecommendUpdateDao productRecommendUpdateDao;
	@Inject
	IInventoryHistoryUpdateDao iInventoryHistoryUpdateDao;
	@Inject
	IInventoryHistoryEnquiryDao iInventoryHistoryEnquiryDao;
	@Inject
	IImageUpdateService imageUpdateService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#createProduct(com.website.dto.
	 * product.Product, java.lang.String)
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public String createProduct(Product pitem, String username)
			throws Exception {
		String result = createValidation(pitem);
		String psku = null;
		String listingid = "";
		if ("".equals(result)) {
			listingid = this.generationListingId();
			try {
				Boolean mainsku = true;
				Logger.info("Creating Listing: {}, SKU: {}", listingid,
						pitem.getSku());
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
				}
				if (mainsku) {
					eventBus.post(new ProductUpdateEvent(pitem, listingid,
							ProductUpdateEvent.ProductHandleType.insert));
				}
			} catch (Exception ex) {
				delete(listingid, psku, pitem.getSku());
				throw ex;
			}
		} else {
			throw new Exception(result);
			// result = result + "--" + pitem.getSku();
		}
		return listingid;
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
				eventBus.post(new ProductUpdateEvent(ls,
						ProductUpdateEvent.ProductHandleType.delete));
			}
		}
		// ~ set first record to main listing
		if (main == false && (mainListings == null || mainListings.size() == 0)) {
			return true;
		}
		return main;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#delete(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
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
		// / productMultiattributeAttributeMapper.deleteBySku(pSku, websiteId);
		// productMultiattributeBaseMapper.deleteBySku(pSku, websiteId);
		productMultiattributeSkuMapper.deleteBySku(sku);
		this.productLabelUpdateDao.deleteByListingId(listingId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#saveProductBase(com.website.dto
	 * .product.Product, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Boolean saveProductBase(Product pitem, String username,
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

	private Boolean saveProductTranslate(
			List<com.website.dto.product.TranslateItem> transItems,
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

	private Boolean saveProductCategory(Product pitem, String username,
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

	private Boolean saveProductSellingPoint(Product pitem, String username,
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

	private Boolean saveStorage(Product pitem, String username, String listingid) {
		return saveStorage(pitem.getStorages(), pitem.getSku(), username,
				listingid);
	}

	@Transactional
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#saveProductUrl(com.website.dto
	 * .product.Product, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Boolean saveProductUrl(Product pitem, String username,
			String listingid) {
		List<ProductUrl> list = Lists.transform(
				pitem.getItems(),
				itembase -> {
					ProductUrl pt = new ProductUrl();
					pt.setCcreateuser(username);
					pt.setClistingid(listingid);
					if (null == itembase.getUrl()
							|| itembase.getUrl().trim().length() <= 0) {
						String nurl = itembase.getTitle().trim()
								.replaceAll("[\\pP\\p{Punct}]", "")
								.replaceAll("[ ]+", "-")
								+ "-" + pitem.getSku();
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
			return pt;
		});
		return list;
	}

	private Boolean saveProductVideo(Product pitem, String username,
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

	private String generationListingId() {
		return UUIDGenerator.createAsString();
	}

	private String createValidation(Product pitem) {

		// ~ categoryid not exist;
		return productValidation.valProduct(pitem);

		// ~ websiteid not exist;
		// ~ sku already exists;
		// ~ price >0;
		// ~ weight >0;
		// ~ qty >0;
		// ~ 多属性 是否与 categoryid 一致
		// ~ url 是否存在？
		// ~ 语言只要一张

		// ~ 多属性产品的属性值要存在品类的属性
		// ~ 输入的属性是否重复
		// ~ 子sku 是否重复

		// ~ languageid not exist;
		// return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#getLanguageidByListingid(java.
	 * lang.String)
	 */
	@Override
	public List<ProductTranslate> getLanguageidByListingid(String clistingid) {
		return this.productTranslateEnquityDao
				.getLanguageIdByListingid(clistingid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#getTitleByClistingids(java.util
	 * .List)
	 */
	@Override
	public List<ProductTranslate> getTitleByClistingids(List<String> clistingids) {
		return this.productTranslateEnquityDao.getTitleByClistings(clistingids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#getUrlByClistingIds(java.util.
	 * List)
	 */
	@Override
	public List<ProductUrl> getUrlByClistingIds(List<String> clistingids) {
		return this.productUrlEnquityDao
				.getProductUrlByClistingids(clistingids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#insertBundle(dto.product.
	 * ProductBundleSale)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#deleteAutoBundle()
	 */
	@Override
	public void deleteAutoBundle() {
		productBundleSaleUpdateDao.deleteAuto();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#alterAutoBundleSaleVisible()
	 */
	@Override
	public void alterAutoBundleSaleVisible() {
		productBundleSaleUpdateDao.alterAutoBundleSaleVisible();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#saveOrUpdateProductCategory(dto
	 * .product.ProductCategoryMapper)
	 */
	@Override
	@Transactional
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
					.insertSelective(productCategoryMapper) > 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#getProductCategoryMapperByListingId
	 * (java.lang.String)
	 */
	@Override
	public List<Integer> getProductCategoryMapperByListingId(String listingId) {
		return productCategoryMapperMapper.getCategoryIdByListingId(listingId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateProductCategoryWithSomeListingId
	 * (java.util.ArrayList, java.lang.String)
	 */
	@Override
	@Transactional
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateByListingIdSelective(dto
	 * .product.ProductBase)
	 */
	@Override
	public boolean updateByListingIdSelective(ProductBase product) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#saveProductPromotions(java.util
	 * .List, java.lang.String)
	 */
	@Override
	public String saveProductPromotions(
			List<com.website.dto.product.PromotionPrice> ProductPromotionlist,
			String user) {
		if (null == ProductPromotionlist || ProductPromotionlist.size() == 0) {
			return "";
		}

		Multimap<Integer, com.website.dto.product.PromotionPrice> muMap = Multimaps
				.index(ProductPromotionlist, obj -> obj.getWebsiteId());
		String result = "";
		for (Integer wid : muMap.keySet()) {

			Collection<com.website.dto.product.PromotionPrice> fixPrice = muMap
					.get(wid);
			Collection<String> skus = Collections2.transform(fixPrice, obj -> {
				return obj.getSku();
			});

			try {
				List<ProductBase> products = productBaseMapper.getProducts(wid,
						Lists.newArrayList(skus));
				if (null == products || products.size() == 0) {
					result += " not fount sku listingid: " + Json.toJson(skus)
							+ System.lineSeparator();
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
					result += " fount sku listingid: " + Json.toJson(errorsku)
							+ System.lineSeparator();
				}

				Collection<ProductBase> needfixObjs = Collections2.filter(
						products, pr -> (false == priceexistslistingIds
								.contains(pr.getClistingid())));
				if (needfixObjs == null || needfixObjs.size() == 0)
					return result;
				Map<String, ProductBase> needfixskuMap = Maps.uniqueIndex(
						needfixObjs, obj -> obj.getCsku());
				Collection<com.website.dto.product.PromotionPrice> needfixprice = Collections2
						.filter(fixPrice, obj -> needfixskuMap.keySet()
								.contains(obj.getSku()));

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
						+ Json.toJson(skus) + System.lineSeparator();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#saveOrUpdateProductSalePrice(dto
	 * .product.ProductSalePrice)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateCostPrice(java.lang.String,
	 * int, double)
	 */
	@Override
	public int updateCostPrice(String sku, int websiteid, double costprice) {
		return productBaseMapper.updateCostPrice(sku, websiteid, costprice);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#saveProductCategory(java.lang.
	 * String, java.lang.String, int)
	 */
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
			eventBus.post(new ProductUpdateEvent(null, pbase.getClistingid(),
					ProductUpdateEvent.ProductHandleType.update));
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#insertProductLabel(dto.product
	 * .ProductLabel)
	 */
	@Override
	public void insertProductLabel(ProductLabel productLabel) {
		this.productLabelUpdateDao.insertOrUpdate(productLabel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#deleteProductLabel(int,
	 * java.lang.String)
	 */
	@Override
	public void deleteProductLabel(int websiteId, String type) {
		productLabelUpdateDao.deleteBySiteAndType(websiteId, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#updatePrice(java.lang.String,
	 * int, java.lang.Double, java.lang.Double, java.lang.Double,
	 * java.lang.Boolean)
	 */
	@Override
	public int updatePrice(String sku, int website, Double price, Double cost,
			Double freight, Boolean freeShipping) {
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
		return rows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateStatus(java.lang.String,
	 * int, java.lang.Integer)
	 */
	@Override
	public int updateStatus(String sku, int website, Integer status) {
		// ~ 更新主产品的时候要将另一个同属spu的产品改为主产品
		int rows = productBaseUpdateDao.updateStatusBySkuAndSite(sku, website,
				status);
		if (rows > 0) {
			List<ProductBase> pbases = productBaseEnquityDao
					.getProductBaseBySkuAndWebsiteId(sku, website);
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
					eventBus.post(new ProductUpdateEvent(pbase.getClistingid(),
							ProductUpdateEvent.ProductHandleType.update));
				} else {
					updateProductStatusAndQuantityAndLabelService
							.deleteProductIndexing(pbase.getClistingid());
				}
			}
		}
		return rows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#addUrl(java.lang.String, int,
	 * java.lang.String, java.lang.Integer)
	 */
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
				if (rows > 0) {
					eventBus.post(new ProductUpdateEvent(pbase.getClistingid(),
							ProductUpdateEvent.ProductHandleType.update));
				}
			}
		}
		return 0;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#addProductMultiAttribute(java.
	 * lang.String, java.lang.String, java.lang.Integer, java.util.List,
	 * java.lang.String)
	 */
	@Override
	public String addProductMultiAttribute(String sku, String parentsku,
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
					eventBus.post(new ProductUpdateEvent(pbase.getClistingid(),
							ProductUpdateEvent.ProductHandleType.update));
				}
			}
			return re;
		} catch (Exception ex) {
			return "sku: " + sku + "--" + ex.getMessage();
		}
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
	 * services.product.IProductUpdateService#addSellpoints(java.lang.String,
	 * java.lang.Integer, java.lang.Integer, java.util.List, java.lang.String)
	 */
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
				eventBus.post(new ProductUpdateEvent(null, pbase
						.getClistingid(),
						ProductUpdateEvent.ProductHandleType.update));
			}
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#addProductImages(java.lang.String,
	 * java.lang.Integer, java.util.List)
	 */
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
					this.saveProductImage(Lists.newArrayList(inurl), sku,
							pbase.getClistingid());
			}
			return "";
		} catch (Exception ex) {
			return "error: " + sku + "--" + ex.getMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateImages(java.lang.String,
	 * java.lang.Integer, java.util.List)
	 */
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
			}
			return "";
		} catch (Exception ex) {
			return "error: " + sku + "--" + ex.getMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#addProductLable(java.lang.String,
	 * java.lang.Integer, java.util.List)
	 */
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
					eventBus.post(new ProductUpdateEvent(null, pbase
							.getClistingid(),
							ProductUpdateEvent.ProductHandleType.update));
				}
			}
			return re;
		} catch (Exception ex) {
			return "error: " + sku + "--" + ex.getMessage();
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

	private List<ProductLabelType> getProductTypes(Product pitem) {
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
		return ptype;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateStorageMapper(java.util.
	 * List, java.util.List, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Transactional
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#updateQty(int,
	 * java.lang.String, int)
	 */
	@Override
	public boolean updateQty(int qty, String listingId, int websiteid) {
		int i = productBaseUpdateDao.updateQtyByListing(qty, listingId);
		if (1 == i) {
			this.updateQtyInventory(listingId, qty, websiteid);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateQtyInventory(java.lang.String
	 * , java.lang.Integer, int)
	 */
	@Override
	public void updateQtyInventory(String listingid, Integer qty, int websiteid) {
		// iInventoryHistoryUpdateDao
		Integer currentqty = iInventoryHistoryEnquiryDao.getQty(listingid);
		Logger.debug("save qty {} {}", qty, currentqty);
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
		Logger.debug("save qty {}, {}", qty, Json.toJson(ihistory));
		iInventoryHistoryUpdateDao.add(ihistory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#addTransate(java.lang.String,
	 * int, java.util.List)
	 */
	@Override
	public String addTransate(String sku, int websiteId,
			List<com.website.dto.product.TranslateItem> tranItems) {
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
				eventBus.post(new ProductUpdateEvent(null, pbase
						.getClistingid(),
						ProductUpdateEvent.ProductHandleType.update));
			}
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateTransate(java.lang.String,
	 * int, java.util.List)
	 */
	@Override
	public String updateTransate(String sku, int websiteId,
			List<com.website.dto.product.TranslateItem> tranItems) {
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
				eventBus.post(new ProductUpdateEvent(null, pbase
						.getClistingid(),
						ProductUpdateEvent.ProductHandleType.update));
			}
		}
		if (re.length() == 0)
			return "";
		else
			return sku + re;
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
		// pt.setCpaymentexplain(itembase.getPaymentExplain());
		// pt.setCreturnexplain(itembase.getReturnExplain());
		pt.setCshortdescription(itembase.getShortDescription());
		pt.setCsku(sku);
		pt.setCtitle(itembase.getTitle());
		// pt.setCwarrantyexplain(itembase.getWarrantyExplain());
		return pt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateStorages(java.lang.String,
	 * int, java.util.List)
	 */
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
			eventBus.post(new ProductUpdateEvent(null, pbase.getClistingid(),
					ProductUpdateEvent.ProductHandleType.update));
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateFregiht(java.lang.String,
	 * java.lang.Integer, java.lang.Double, java.lang.Boolean)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#deleteProductLabel(java.lang.String
	 * , java.lang.Integer, java.util.List)
	 */
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
				eventBus.post(new ProductUpdateEvent(null, pbase
						.getClistingid(),
						ProductUpdateEvent.ProductHandleType.update));
			}
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#updateProductQty(java.lang.String,
	 * java.lang.Integer, java.lang.Integer)
	 */
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
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#getProductSalePriceAfterCurrentDate
	 * (java.util.Map)
	 */
	@Override
	public List<ProductSalePrice> getProductSalePriceAfterCurrentDate(
			Map<String, Object> paras) {
		return this.productSalePriceEnquityDao
				.getProductSalePriceAfterCurrentDate(paras);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#insertProductSalePrice(dto.product
	 * .ProductSalePrice)
	 */
	@Override
	public int insertProductSalePrice(ProductSalePrice record) {
		return this.productSalePriceUpdateDao.addProductSalePrice(record);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#setProductStatusBySku(java.lang
	 * .String)
	 */
	@Override
	public int setProductStatusBySku(String sku) {
		return productBaseMapper.setStatusBySku(sku);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.product.IProductUpdateService#incrementViewCount(int,
	 * java.lang.String)
	 */
	@Override
	public void incrementViewCount(int siteID, String listingID) {
		if (this.productViewCountUpdateDao.alterViewCount(siteID, listingID) == 0) {
			this.productViewCountUpdateDao.addViewCount(siteID, listingID, 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#insertProductRecommend(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
	public int insertProductRecommend(String clistinid,
			String crecommendlisting, String ccreateuser) {
		return this.productRecommendUpdateDao.insert(clistinid,
				crecommendlisting, ccreateuser);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#deleteMultiProductAttribute(java
	 * .lang.String, java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#deleteProductAttribute(java.lang
	 * .String, java.lang.String, java.lang.Integer)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#addProductAttribute(java.lang.
	 * String, java.lang.Integer, java.lang.String, java.util.List,
	 * java.lang.String)
	 */
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
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#deleteProductCurrentSalePrice(
	 * java.lang.String)
	 */
	@Override
	public int deleteProductCurrentSalePrice(String listingId) {
		return productSalePriceUpdateDao
				.deleteProductCurrentSalePrice(listingId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#deleteProductSalePriceByDate(java
	 * .lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public int deleteProductSalePriceByDate(String listingId, Date start,
			Date end) {
		return this.productSalePriceUpdateDao.deleteProductSalePriceByDate(
				listingId, start, end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.product.IProductUpdateService#deleteProductSellingPoints(java
	 * .lang.String, int)
	 */
	@Override
	public int deleteProductSellingPoints(String listingId, int languageId) {
		return this.productSellingPointsUpdateDao
				.deleteByListingIdAndLanguageId(listingId, languageId);
	}

	@Override
	public boolean deleteProductImageByListingId(String listingId,
			Integer websiteId) {
		List<ProductImage> productImgs = productImageMapper
				.getProductImgsByListingId(listingId);
		if (null == productImgs || 0 >= productImgs.size()) {
			return true;
		}
		List<String> paths = productImgs.stream()
				.map(ProductImage::getCimageurl).collect(Collectors.toList());
		imageUpdateService.deleteImByPaths(paths);
		int deleteByListingId = productImageMapper.deleteByListingId(listingId);
		return deleteByListingId > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: addProductCategoryMapper</p>
	 * <p>Description: 增加产品与品类映射</p>
	 * @param listingIdList
	 * @param categoryIds
	 * @return
	 * @see services.product.IProductUpdateService#addProductCategoryMapper(java.util.List, java.util.List)
	 */
	@Override
	public String addProductCategoryMapper(List<String> listingIdList,
			List<Integer> categoryIds) {
		String re = "";
		List<SkuRelateListingId> srlList = Lists.newArrayList();
		try {
			if(null != listingIdList && listingIdList.size() > 0){
				srlList = productBaseMapper.getSkuByListingIds(listingIdList);
			}
			if(null != srlList && srlList.size() > 0){
				if (null != categoryIds && categoryIds.size() > 0) {
					for(SkuRelateListingId srl : srlList){
						for (Integer categoryId : categoryIds) {
							ProductCategoryMapper pcm = new ProductCategoryMapper();
							pcm.setClistingid(srl.getClistingid());
							pcm.setCsku(srl.getCsku());
							pcm.setIcategory(categoryId);
							productCategoryMapperMapper.insertSelective(pcm);
						}
					}
				}else{
					re += "no categoryId";
				}
			}else{
				re += "no listingId";
			}
		} catch (Exception e) {
			re += "addProductCategoryMapper error : "+e.getMessage();
		}
		return re;
	}

}
