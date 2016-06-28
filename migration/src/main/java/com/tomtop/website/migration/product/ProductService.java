package com.tomtop.website.migration.product;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.website.dto.product.ImageItem;
import com.website.dto.product.Product;
import com.website.dto.product.TranslateItem;
import com.website.dto.product.VideoItem;

public class ProductService {

	@Inject
	ProductMapper mapper;
	@Inject
	SellingPointService sellingPointService;

	public List<Product> getProduct(String savePath, List<String> skus) {
		List<ProductAttribute> dblist = mapper.selectBySku(skus);
		this.getPromotions(savePath, dblist);
		return transformProduct(dblist);

	}

	private List<Product> transformProduct(List<ProductAttribute> dblist) {
		Multimap<String, ProductAttribute> mulmaps = Multimaps.index(dblist,
				obj -> obj.getSku());
		List<ProductCategoryEntity> pcategoryList = mapper
				.getProductCategorys(Lists.newArrayList(mulmaps.keySet()));
		Multimap<String, ProductCategoryEntity> categorymaps = Multimaps.index(
				pcategoryList, obj -> obj.getSku());
		List<ProductImageEntity> imagelist = mapper.getProductImages(Lists
				.newArrayList(mulmaps.keySet()));
		Multimap<String, ProductImageEntity> imagemaps = Multimaps.index(
				imagelist, obj -> obj.getSku());

		Collection<Product> relist = Collections2
				.transform(
						mulmaps.keySet(),
						sku -> {
							Product pitem = new Product();
							Multimap<String, ProductAttribute> valuemap = Multimaps
									.index(mulmaps.get(sku), attobj -> attobj
											.getAttribute_code());
							pitem.setCategoryIds(this
									.getCategoryIds(categorymaps.get(sku)));
							pitem.setCleanrstocks(this.getValue(Boolean.class,
									"clearance", valuemap));
							pitem.setFeatured(this.getValue(Boolean.class,
									"featured", valuemap));
							// pitem.setHot(Boolean.class,);
							this.setNewInfo(pitem, valuemap);
							pitem.setItems(this.getItems(valuemap));
							pitem.setPrice(this.getValue(Double.class, "price",
									valuemap));
							// pitem.setCost(pitem.getPrice());
							pitem.setQty(1000);
							pitem.setSku(sku.toUpperCase());
							Date spDate = this.getValue(Date.class,
									"special_to_date", valuemap);
							if (null != spDate && spDate.after(new Date())) {
								pitem.setSpecial(true);
							}
							pitem.setStatus(this.getValue(Integer.class,
									"status", valuemap));
							pitem.setVideos(this.getVideos(valuemap));
							Integer visiid = this.getValue(Integer.class,
									"visibility", valuemap);
							pitem.setVisible(false);
							if (visiid != 1) {
								pitem.setVisible(true);
							}
							pitem.setWebsiteId(1);
							pitem.setWeight(this.getValue(Double.class,
									"weight", valuemap));
							pitem.setImages(this.getImages(valuemap,
									imagemaps.get(sku)));
							pitem.setStorages(this.getStorages(valuemap));
							return pitem;
						});
		return Lists.newArrayList(relist);
	}

	private List<Integer> getStorages(
			Multimap<String, ProductAttribute> valuemap) {
		String str = this.getValue(String.class, "abroad_stock", valuemap);
		if (null != str && str.length() > 0) {
			String[] strarr = str.split(",");
			List<Integer> results = Lists.newArrayList();
			for (String key : strarr) {
				switch (key) {
				case "142": // us
					results.add(2);
					break;
				case "143": // GB
					results.add(3);
					break;
				case "144": // au
					results.add(7);
					// break;
				case "145": // cn
					results.add(1);
					break;
				case "146": // de
					results.add(5);
					break;
				default:
					results.add(1);
					break;
				}
			}
			return results;
		}
		return Lists.newArrayList(1);
		/*
		 * 144->AU 146->DE (6) 143->GB (4) 142->US (3) 145->CN (1)
		 */
	}

	public List<com.website.dto.product.ImageItem> getImages(
			Multimap<String, ProductAttribute> valuemap,
			Collection<ProductImageEntity> imagelist) {

		if (imagelist != null) {
			// /String thumimg = this.getValue(String.class, "thumbnail",
			// valuemap);
			// /String simg = this.getValue(String.class, "small_image",
			// valuemap);
			// /String baseimg = this.getValue(String.class, "image", valuemap);
			Collection<com.website.dto.product.ImageItem> list = Collections2
					.transform(
							imagelist,
							obj -> {
								com.website.dto.product.ImageItem iitem = new com.website.dto.product.ImageItem();
								iitem.setThumbnail(true);
								iitem.setSmallImage(true);
								iitem.setBaseImage(true);
								// if (obj.getImageUrl().equals(thumimg)) {
								// obj.setThumbnail(true);
								// } else if (obj.getImageUrl().equals(simg)) {
								// obj.setSmallImage(true);
								// } else if (obj.getImageUrl().equals(baseimg))
								// {
								// obj.setBaseImage(true);
								// }
								iitem.setLabel(obj.getLabel());
								iitem.setOrder(obj.getOrder());
								iitem.setImageUrl("http://www.tomtop.com/media/catalog/product"
										+ obj.getImageUrl());
								return iitem;
							});
			return Lists.newArrayList(list);
		}
		return null;
	}

	private List<TranslateItem> getItems(
			Multimap<String, ProductAttribute> valuemap) {
		List<TranslateItem> itembases = Lists.newArrayList();
		TranslateItem item = new TranslateItem();
		item.setDescription(this
				.getValue(String.class, "description", valuemap));
		// item.setKeyword(this.getValue(String.class, "", valuemap));
		item.setLanguageId(1);
		item.setMetaDescription(this.getValue(String.class, "meta_description",
				valuemap));
		item.setMetaKeyword(this.getValue(String.class, "meta_keyword",
				valuemap));
		item.setMetaTitle(this.getValue(String.class, "meta_title", valuemap));
		// item.setPaymentexplain(paymentexplain);
		// item.setReturnexplain(returnexplain);
		// item.setSellingPoints(sellingPoints);
		item.setShortDescription(this.getValue(String.class,
				"short_description", valuemap));
		item.setTitle(this.getValue(String.class, "name", valuemap));
		// item.setWarrantyexplain();
		String url = this.getValue(String.class, "url_key", valuemap);
		item.setUrl(url);
		itembases.add(item);
		return itembases;
	}

	private void setNewInfo(Product pitem,
			Multimap<String, ProductAttribute> valuemap) {
		pitem.setNewFromDate(this.getValue(Date.class, "news_from_date",
				valuemap));
		pitem.setNewToDate(this.getValue(Date.class, "news_to_date", valuemap));
		pitem.setIsNew(false);
		if (null != pitem.getNewToDate()
				&& pitem.getNewToDate().after(new Date())) {
			pitem.setIsNew(true);
		}
	}

	private List<VideoItem> getVideos(
			Multimap<String, ProductAttribute> valuemap) {
		String vpath = this.getValue(String.class, "product_vedio", valuemap);
		if (vpath == null)
			return null;

		List<VideoItem> videos = Lists.newArrayList();
		VideoItem vitem = new VideoItem();
		vitem.setLabel(null);
		vitem.setVideoUrl(vpath);
		videos.add(vitem);
		return videos;
	}

	private List<Integer> getCategoryIds(
			Collection<ProductCategoryEntity> categoryList) {
		if (null == categoryList)
			return null;
		Collection<Integer> ids = Collections2.transform(categoryList,
				obj -> obj.getCategoryId());
		return Lists.newArrayList(ids);
	}

	@SuppressWarnings("unchecked")
	private <T> T getValue(Class<?> types, String key,
			Multimap<String, ProductAttribute> valuemap) {
		if (valuemap.containsKey(key)) {

			Collection<ProductAttribute> mitems = valuemap.get(key);
			ProductAttribute mitem = (ProductAttribute) mitems.toArray()[0];
			if (mitem.getValue() == null)
				return null;
			if (types.getTypeName().equals(Boolean.class.getTypeName())) {
				return (T) Boolean.valueOf(mitem.getValue());
			} else if (types.getTypeName().equals(Integer.class.getTypeName())) {
				return (T) Integer.valueOf(mitem.getValue());
			} else if (types.getTypeName().equals(Double.class.getTypeName())) {
				return (T) Double.valueOf(mitem.getValue());
			} else if (types.getTypeName().equals(Date.class.getTypeName())) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					return (T) df.parseObject(mitem.getValue());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				return (T) mitem.getValue();
			}
		}
		return null;
	}

	public Product setSellPoint(Product p, String sellpointpath) {
		if (p.getItems() != null) {
			List<TranslateItem> tilist = Lists.transform(
					p.getItems(),
					obj -> {
						obj.setSellingPoints(sellingPointService.getSellPoint(
								p.getSku(), "EN", sellpointpath));
						return obj;
					});
			p.setItems(tilist);
		}
		return p;
	}

	public void getPromotions(String savePath, List<ProductAttribute> dblist) {
		Multimap<String, ProductAttribute> mulmaps = Multimaps.index(dblist,
				obj -> obj.getSku());
		File f = new File(savePath, "/promotions");
		if (f.exists() == false) {
			f.mkdirs();
		}
		ObjectMapper om = new ObjectMapper();
		for (String sku : mulmaps.keySet()) {
			Multimap<String, ProductAttribute> valuemap = Multimaps.index(
					mulmaps.get(sku), attobj -> attobj.getAttribute_code());
			Double price = this.getValue(Double.class, "special_price",
					valuemap);
			if (price == null) {
				continue;
			}
			com.website.dto.product.PromotionPrice pprice = new com.website.dto.product.PromotionPrice();
			pprice.setSku(sku);
			pprice.setBeginDate(this.getValue(Date.class, "special_from_date",
					valuemap));
			pprice.setEndDate(this.getValue(Date.class, "special_to_date",
					valuemap));
			if (pprice.getEndDate() == null) {

			}
			pprice.setPrice(price);
			pprice.setWebsiteId(1);

			try {
				om.writeValue(new File(f.getPath(), sku + ".json"), pprice);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getMutiProduct(List<String> skus, String savepath) {
		List<ProductAttribute> dblist = mapper.selectBySku(skus);
		List<Product> productlist = transformProduct(dblist);
		Map<String, Product> pmaps = Maps.uniqueIndex(productlist,
				obj -> obj.getSku());

		List<MutilAttribute> skulist = mapper.getProductMutilAttribute(skus);
		Multimap<String, MutilAttribute> maps = Multimaps.index(skulist,
				obj -> obj.getSpu());
		ObjectMapper om = new ObjectMapper();
		File f = new File(savepath, "/multi");
		if (f.exists() == false) {
			f.mkdirs();
		}
		for (String key : maps.keySet()) {
			try {
				com.website.dto.product.Product p = pmaps.get(key);
				List<ImageItem> imgs = p.getImages();
				Collection<MutilAttribute> list = Collections2
						.transform(
								maps.get(key),
								obj -> {
									Collection<ImageItem> tims = Collections2.filter(
											imgs,
											im -> {
												if (im.getImageUrl() == null
														|| obj.getSku() == null)
													return false;
												return im
														.getImageUrl()
														.toLowerCase()
														.contains(
																obj.getSku()
																		.toLowerCase());
											});
									if (tims != null && tims.size() > 0) {
										obj.setImages(Lists.newArrayList(tims));
									}
									return obj;
								});
				om.writeValue(new File(f.getPath(), key + ".json"),
						this.getRelationSku(list));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Collection<MutilAttribute> getRelationSku(
			Collection<MutilAttribute> mulist) {
		if (mulist == null || mulist.size() == 0) {
			return mulist;
		}
		if (mulist.size() > 1) {
			List<MutilAttribute> collist = mergeSkus(mulist);
			if (null != collist && collist.size() > 0) {
				collist.addAll(mulist);
				return collist;
			}
			return mulist;
		}
		MutilAttribute muti = (MutilAttribute) mulist.toArray()[0];
		if (muti.getKey() != null) {
			return mulist;
		}
		List<String> skus = mapper.getChildSkus(muti.getSpu());
		return Collections2.transform(skus, obj -> {
			MutilAttribute tmuti = new MutilAttribute();
			tmuti.setPrice(muti.getPrice());
			tmuti.setSku(obj);
			tmuti.setSpu(muti.getSpu());
			tmuti.setUrl(muti.getUrl());
			return tmuti;
		});
	}

	/**
	 * 合并 颜色与尺寸的sku
	 * 
	 * @return
	 */
	private List<MutilAttribute> mergeSkus(Collection<MutilAttribute> mulist) {
		Collection<MutilAttribute> sizeattrs = Collections2.filter(mulist,
				obj -> "size".equalsIgnoreCase(obj.getKey()));
		if (null != sizeattrs && sizeattrs.isEmpty() == false) {
			Collection<MutilAttribute> colorattrs = Collections2.filter(mulist,
					obj -> "color".equalsIgnoreCase(obj.getKey()));
			if (colorattrs != null && colorattrs.isEmpty() == false) {
				List<MutilAttribute> relist = new ArrayList<MutilAttribute>();
				for (MutilAttribute mb : sizeattrs) {
					for (MutilAttribute cb : colorattrs) {
						String nsku = mb.getSku().replace(mb.getSpu(),
								cb.getSku());
						relist.add(buildAttr(mb, nsku));
						relist.add(buildAttr(cb, nsku));
					}
				}
				return relist;
			}
		}
		return null;
	}

	private MutilAttribute buildAttr(MutilAttribute attr, String sku) {
		MutilAttribute re = new MutilAttribute();
		re.setKey(attr.getKey());
		re.setValue(attr.getValue());
		re.setPrice(attr.getPrice());
		re.setSku(sku);
		re.setSpu(attr.getSpu());

		re.setStatus(attr.getStatus());
		re.setWebsiteId(attr.getWebsiteId());
		re.setUrl(attr.getUrl());
		return re;
	}

}
