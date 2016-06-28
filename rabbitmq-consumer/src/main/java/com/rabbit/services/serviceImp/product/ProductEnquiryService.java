package com.rabbit.services.serviceImp.product;




import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.rabbit.common.enums.search.criteria.ProductLabelType;
import com.rabbit.conf.mapper.product.ProductBaseMapper;
import com.rabbit.conf.mapper.product.ProductCategoryMapperMapper;
import com.rabbit.conf.mapper.product.ProductEntityMapMapper;
import com.rabbit.conf.mapper.product.ProductImageMapper;
import com.rabbit.conf.mapper.product.ProductMultiattributeAttributeMapper;
import com.rabbit.dao.idao.product.IProductLabelEnquiryDao;
import com.rabbit.dao.idao.product.IProductSellingPointsEnquiryDao;
import com.rabbit.dao.idao.product.IProductStorageMapEnquiryDao;
import com.rabbit.dao.idao.product.IProductTranslateEnquiryDao;
import com.rabbit.dao.idao.product.IProductUrlEnquiryDao;
import com.rabbit.dao.idao.product.IProductVideoEnquiryDao;
import com.rabbit.dto.ProductAttributeItem;
import com.rabbit.dto.product.ProductBase;
import com.rabbit.dto.product.ProductCategoryMapper;
import com.rabbit.dto.product.ProductImage;
import com.rabbit.dto.product.ProductLabel;
import com.rabbit.dto.product.ProductMultiattributeAttribute;
import com.rabbit.dto.product.ProductSellingPoints;
import com.rabbit.dto.product.ProductStorageMap;
import com.rabbit.dto.product.ProductTranslate;
import com.rabbit.dto.product.ProductUrl;
import com.rabbit.dto.product.ProductVideo;
import com.rabbit.dto.transfer.product.AttributeItem;
import com.rabbit.dto.transfer.product.ImageItem;
import com.rabbit.dto.transfer.product.ProductBack;
import com.rabbit.dto.transfer.product.MultiProduct;
import com.rabbit.dto.transfer.product.TranslateItem;
import com.rabbit.dto.transfer.product.VideoItem;
import com.rabbit.services.iservice.product.IProductEnquiryService;

/*import dto.ProductAttributeItem;
import dto.product.ProductCategoryMapper;
import dto.product.ProductImage;
import dto.product.ProductLabel;
import dto.product.ProductMultiattributeAttribute;
import dto.product.ProductSellingPoints;
import dto.product.ProductStorageMap;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;
import dto.product.ProductVideo;*/
@Service
public class ProductEnquiryService implements IProductEnquiryService {

	private static Logger log=Logger.getLogger(ProductEnquiryService.class.getName());
	@Autowired
	IProductTranslateEnquiryDao productTranslateEnquityDao;
	@Autowired
	ProductImageMapper productImageMapper;
	@Autowired
	ProductBaseMapper productBaseMapper;
	@Autowired
	ProductCategoryMapperMapper productCategoryMapperMapper;
	@Autowired
	IProductUrlEnquiryDao productUrlEnquityDao;
	@Autowired
	IProductSellingPointsEnquiryDao productSellingPointsEnquityDao;
	@Autowired
	IProductStorageMapEnquiryDao productStorageMapEnquityDao;
	@Autowired
	IProductLabelEnquiryDao productLabelEnquiryDao;
	@Autowired
	ProductEntityMapMapper productEntityMapMapper;
	@Autowired
	IProductVideoEnquiryDao productVideoEnquityDao;
	@Autowired
	ProductMultiattributeAttributeMapper productMultiattributeAttributeMapper;
	public List<ProductBase> getProduct(String sku, Integer websiteId) {
		return productBaseMapper
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
	}
	public List<ProductBack> getProducts(Integer websiteId,
			Date begin, Date end) {
		List<ProductBase> pblist = productBaseMapper.getProductsUsingTime(
				websiteId, begin, end);
		return getProducts(pblist);
	}
	public ProductBase getBaseByListingId(String listingId) {
		return productBaseMapper.getProductBaseByListingId(listingId);
	}
	@Override
	public List<ProductBack> getProductsByListingIds(
			List<String> listingids) {
		List<ProductBase> pblist = productBaseMapper
				.getProductByListingIds(listingids);
		return getProducts(pblist);
	}
	private List<ProductBack> getProducts(
			List<ProductBase> pblist) {
		if (pblist == null || pblist.size() == 0) {
			return null;
		}
		List<String> listingids = Lists.transform(pblist,
				obj -> obj.getClistingid());
		Collection<ProductBase> pskus = Collections2.filter(pblist,
				tobj -> (tobj.getCparentsku() != null && tobj.getCparentsku()
						.trim().length() > 0));
		List<String> parentskus = null;
		if (pskus != null && pskus.size() > 0) {
			parentskus = Lists.transform(Lists.newArrayList(pskus),
					obj -> obj.getCparentsku());
		}
		// ~ img
		List<ProductImage> imglist = productImageMapper
				.getImageUrlByClistingid(listingids);
		Multimap<String, ProductImage> timgMaps = null;
		if (null != imglist && imglist.size() > 0) {
			timgMaps = Multimaps.index(imglist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductImage> imgMaps = timgMaps;

		// ~ tran
		List<ProductTranslate> tranlist = this.productTranslateEnquityDao
				.getTitleByClistings(listingids);
		Multimap<String, ProductTranslate> ttranMaps = null;
		if (null != tranlist && tranlist.size() > 0) {
			ttranMaps = Multimaps.index(tranlist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductTranslate> tranMaps = ttranMaps;

		// ~ url
		List<ProductUrl> urllist = this.productUrlEnquityDao
				.getProductUrlByClistingids(listingids);
		Multimap<String, ProductUrl> turlMaps = null;
		if (null != urllist && urllist.size() > 0) {
			turlMaps = Multimaps.index(urllist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductUrl> urlMaps = turlMaps;

		// ~ category
		List<ProductCategoryMapper> categorylist = productCategoryMapperMapper
				.selectByListingIds(listingids);
		Multimap<String, ProductCategoryMapper> tategoryMaps = null;
		if (null != categorylist && categorylist.size() > 0) {
			tategoryMaps = Multimaps.index(categorylist,
					obj -> obj.getClistingid());
		}
		Multimap<String, ProductCategoryMapper> categoryMaps = tategoryMaps;

		// ~ points
		List<ProductSellingPoints> pointlist = this.productSellingPointsEnquityDao
				.getProductSellingPointsByListingIdsOnly(listingids);
		Multimap<String, ProductSellingPoints> tpointMaps = null;
		if (null != pointlist && pointlist.size() > 0) {
			tpointMaps = Multimaps.index(pointlist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductSellingPoints> pointMaps = tpointMaps;

		// ~ storage
		List<ProductStorageMap> storagelist = this.productStorageMapEnquityDao
				.getProductStorages(listingids);
		Multimap<String, ProductStorageMap> tstorageMaps = null;
		if (null != storagelist && storagelist.size() > 0) {
			tstorageMaps = Multimaps.index(storagelist,
					obj -> obj.getClistingid());
		}
		Multimap<String, ProductStorageMap> storageMaps = tstorageMaps;

		// ~ label
		List<ProductLabel> labellist = this.productLabelEnquiryDao
				.getBatchProductLabel(listingids);
		Multimap<String, ProductLabel> tlabelMaps = null;
		if (null != labellist && labellist.size() > 0) {
			tlabelMaps = Multimaps.index(labellist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductLabel> labelMaps = tlabelMaps;

		// ~ attribute
		List<ProductAttributeItem> attributeItems = productEntityMapMapper
				.getProductAttributeByListingids(listingids);
		Multimap<String, ProductAttributeItem> tattributeMaps = null;
		if (null != attributeItems && attributeItems.size() > 0) {
			tattributeMaps = Multimaps.index(attributeItems,
					obj -> obj.getListingId());
		}
		Multimap<String, ProductAttributeItem> attributeMaps = tattributeMaps;

		// ~ video
		List<ProductVideo> videolist = this.productVideoEnquityDao
				.getVideoBylistingIds(listingids);
		Multimap<String, ProductVideo> tvideoMaps = null;
		if (null != videolist && videolist.size() > 0) {
			tvideoMaps = Multimaps.index(videolist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductVideo> videoMaps = tvideoMaps;

		// ~ multi attribute
		Multimap<String, ProductMultiattributeAttribute> tmultiattributeMaps = null;
		if (null != parentskus && parentskus.size() > 0) {
			List<ProductMultiattributeAttribute> multiattributes = productMultiattributeAttributeMapper
					.getMultiAttributes(parentskus);
			tmultiattributeMaps = Multimaps.index(multiattributes,
					obj -> obj.getCparentsku());
		}
		Multimap<String, ProductMultiattributeAttribute> multiattributeMaps = tmultiattributeMaps;
		List<ProductBack> products = Lists
				.transform(
						pblist,
						pbase -> {

							ProductBack product = new ProductBack();
							if (pbase.getCparentsku() != null
									&& pbase.getCparentsku().length() > 0) {
								product = new MultiProduct();
								((MultiProduct) product)
										.setMainSku(pbase.getBmain());
								((MultiProduct) product)
										.setParentSku(pbase.getCparentsku());
								((MultiProduct) product)
										.setMultiAttributes(this.getAttribtes(
												pbase.getClistingid(),
												pbase.getCparentsku(),
												pbase.getIwebsiteid(),
												attributeMaps,
												multiattributeMaps, true));
							}
							try {
								product.setSku(pbase.getCsku());
								product.setListingId(pbase.getClistingid());
								product.setPrice(pbase.getFprice());
								product.setQty(pbase.getIqty());
								product.setStatus(pbase.getIstatus());
								product.setSpu(pbase.getCparentsku());
								product.setIsMain(pbase.getBmain());
								product.setFreight(pbase.getFfreight());
								product.setAttributes(this.getAttribtes(
										pbase.getClistingid(),
										pbase.getCparentsku(),
										pbase.getIwebsiteid(), attributeMaps,
										multiattributeMaps, false));
								product.setCategoryIds(this.getCategorys(
										pbase.getClistingid(), categoryMaps));
								product.setCleanrstocks(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.Clearstocks, labelMaps));
								product.setCost(pbase.getFcostprice());
								product.setFeatured(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.Featured, labelMaps));
								product.setHot(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.Hot, labelMaps));
								product.setImages(this.getImages(
										pbase.getClistingid(), imgMaps));
								product.setIsNew(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.NewArrial, labelMaps));
								
								product.setIsAllFreeShipping(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.AllFreeShipping, labelMaps));
								
								product.setItems(this.getTrans(
										pbase.getClistingid(), tranMaps,
										pointMaps, urlMaps));
								product.setNewFromDate(pbase.getDnewformdate());
								product.setNewToDate(pbase.getDnewtodate());

								product.setSpecial(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.Special, labelMaps));

								product.setStorages(this.getStorages(
										pbase.getClistingid(), storageMaps));
								product.setVideos(this.getVideos(
										pbase.getClistingid(), videoMaps));
								product.setVisible(pbase.getBvisible());
								product.setWebsiteId(pbase.getIwebsiteid());
								product.setWeight(pbase.getFweight());

							} catch (Exception ex) {
								ex.printStackTrace();
								log.error("get listing error: ", ex);
							}
							return product;
						});
		return products;
	}
	private List<VideoItem> getVideos(String listingid,
			Multimap<String, ProductVideo> videoMaps) {
		if (videoMaps != null && videoMaps.containsKey(listingid)) {
			Collection<VideoItem> slist = Collections2.transform(
					videoMaps.get(listingid), obj -> {
						VideoItem vi = new VideoItem();
						vi.setLabel(obj.getClabel());
						vi.setVideoUrl(obj.getCvideourl());
						return vi;
					});
			return Lists.newArrayList(slist);
		}
		return null;
	}
	private List<Integer> getStorages(String listingid,
			Multimap<String, ProductStorageMap> storageMaps) {
		if (storageMaps != null && storageMaps.containsKey(listingid)) {
			Collection<Integer> slist = Collections2.transform(
					storageMaps.get(listingid), obj -> obj.getIstorageid());
			return Lists.newArrayList(slist);
		}
		return null;
	}
	private List<TranslateItem> getTrans(String listingid,
			Multimap<String, ProductTranslate> tranMaps,
			Multimap<String, ProductSellingPoints> pointMaps,
			Multimap<String, ProductUrl> urlMaps) {
		if (tranMaps == null || tranMaps.containsKey(listingid) == false) {
			return null;
		}
		Collection<TranslateItem> translist = Collections2.transform(tranMaps
				.get(listingid), obj -> {
			TranslateItem tli = new TranslateItem();
			tli.setDescription(obj.getCdescription());
			tli.setKeyword(obj.getCkeyword());
			tli.setLanguageId(obj.getIlanguageid());
			tli.setMetaDescription(obj.getCmetadescription());
			tli.setMetaKeyword(obj.getCmetakeyword());
			tli.setMetaTitle(obj.getCmetatitle());
			tli.setSellingPoints(this.getPoints(listingid,
					obj.getIlanguageid(), pointMaps));
			tli.setShortDescription(obj.getCshortdescription());
			tli.setTitle(obj.getCtitle());
			tli.setUrl(this.getUrl(listingid, obj.getIlanguageid(), urlMaps));
			return tli;
		});
		return Lists.newArrayList(translist);
	}

	private List<String> getPoints(String listingid, int languageid,
			Multimap<String, ProductSellingPoints> pointMaps) {
		if (pointMaps != null && pointMaps.containsKey(listingid)) {
			Collection<ProductSellingPoints> tpoints = Collections2.filter(
					pointMaps.get(listingid),
					obj -> (obj.getIlanguageid() == languageid));
			if (tpoints != null && tpoints.size() > 0) {
				Collection<String> points = Collections2.transform(tpoints,
						obj -> obj.getCcontent());
				return Lists.newArrayList(points);
			}
		}
		return null;
	}

	private String getUrl(String listingid, Integer languageid,
			Multimap<String, ProductUrl> urlMaps) {
		if (urlMaps != null && urlMaps.containsKey(listingid)) {
			List<ProductUrl> tlist = Lists.newArrayList(Iterators.filter(
					urlMaps.get(listingid).iterator(),
					obj -> (obj.getIlanguageid() != null && obj
							.getIlanguageid() == languageid)));
			if (null != tlist && tlist.size() > 0) {
				return ((ProductUrl) tlist.get(0)).getCurl();
			}
			if (urlMaps.get(listingid) != null
					&& urlMaps.get(listingid).size() > 0) {
				String url = ((ProductUrl) urlMaps.get(listingid).toArray()[0])
						.getCurl();
				return url;
			}
		}
		return null;
	}
	private List<ImageItem> getImages(String listingid,
			Multimap<String, ProductImage> imgMaps) {
		if (imgMaps == null) {
			return null;
		}
		Collection<ImageItem> pimgs = Collections2.transform(
				imgMaps.get(listingid), obj -> {
					ImageItem ii = new ImageItem();
					ii.setBaseImage(obj.getBbaseimage());
					ii.setImageUrl(obj.getCimageurl());
					ii.setLabel(obj.getClabel());
					ii.setOrder(obj.getIorder());
					ii.setSmallImage(obj.getBsmallimage());
					ii.setThumbnail(obj.getBthumbnail());
					return ii;
				});
		return Lists.newArrayList(pimgs);
	}
	private Boolean getProductLabel(String listingid, int websiteid,
			ProductLabelType ptype, Multimap<String, ProductLabel> labelMaps) {
		if (null != labelMaps && labelMaps.size() > 0
				&& labelMaps.containsKey(listingid)) {
			Collection<ProductLabel> lables = labelMaps.get(listingid);
			Collection<ProductLabel> fl = Collections2.filter(lables,
					obj -> obj.getIwebsiteid()!=null && obj.getIwebsiteid() == websiteid
							&& obj.getCtype().equals(ptype.toString()));
			return (fl != null && fl.size() > 0);
		}
		return false;
	}
	private List<Integer> getCategorys(String listingid,
			Multimap<String, ProductCategoryMapper> categoryMaps) {
		if (categoryMaps != null && categoryMaps.containsKey(listingid)) {
			Collection<Integer> calist = Collections2.transform(
					categoryMaps.get(listingid), ca -> ca.getIcategory());
			if (calist != null && calist.size() > 0) {
				return Lists.newArrayList(calist);
			}
		}
		return null;
	}
	private List<AttributeItem> getAttribtes(
			String listingid,
			String parentsku,
			int websiteid,
			Multimap<String, ProductAttributeItem> attributeMaps,
			Multimap<String, ProductMultiattributeAttribute> multiattributeMaps,
			Boolean multi) {
		if (null == attributeMaps
				|| attributeMaps.containsKey(listingid) == false) {
			return null;
		}
		Multimap<Integer, ProductMultiattributeAttribute> tshowMaps = null;
		if (null != multiattributeMaps) {
			Collection<ProductMultiattributeAttribute> ls = multiattributeMaps
					.get(parentsku);
			tshowMaps = Multimaps.index(Collections2.filter(ls,
					pma -> pma.getIwebsiteid() == websiteid), obj -> obj
					.getIkey());
		}
		Multimap<Integer, ProductMultiattributeAttribute> showMaps = tshowMaps;
		List<AttributeItem> attitems = Lists
				.newArrayList(Collections2.transform(
						attributeMaps.get(listingid),
						obj -> {
							AttributeItem ai = new AttributeItem();
							ai.setKey(obj.getKey());
							ai.setKeyId(obj.getKeyId());
							ai.setLanguangeId(obj.getLanguageId());
							ai.setValue(obj.getValue());
							ai.setValueId(obj.getValueId());
							ai.setShowImg(false);
							if (null != parentsku && showMaps != null
									&& showMaps.containsKey(ai.getKeyId())
									&& showMaps.get(ai.getKeyId()).size() > 0) {
								ai.setShowImg(((ProductMultiattributeAttribute) showMaps
										.get(ai.getKeyId()).toArray()[0])
										.getBshowimg());
								if (multi == false) {
									return null;
								}
							}
							return ai;
						}));
		if (attitems != null && attitems.size() > 0) {
			Collection<AttributeItem> reslist = Collections2.filter(attitems,
					obj -> obj != null);
			if (null != reslist && reslist.size() > 0) {
				return Lists.newArrayList(reslist);
			}
		}
		return null;
	}
	
	public List<ProductUrl> getProductUrlByListingId(String listingid){
		return this.productUrlEnquityDao.getProductUrlByListingId(listingid);
	}
}
