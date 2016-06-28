/*package services.erp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductEntityMapMapper;
import mapper.product.ProductGroupPriceMapper;
import mapper.product.ProductImageMapper;
import mapper.product.ProductRecommendMapper;
import mapper.product.ProductSalePriceMapper;
import mapper.product.ProductSellingPointsMapper;
import mapper.product.ProductStorageMapMapper;
import mapper.product.ProductUrlMapper;

import org.mybatis.guice.transactional.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import dto.ProductEntityMapLite;
import dto.ProductForERPPublish;
import dto.ProductGroupPriceLite;
import dto.ProductImageLite;
import dto.ProductSalePriceLite;
import dto.ProductSellingPointsLite;
import dto.ProductTranslateLite;
import dto.product.ProductBase;
import dto.product.ProductCategoryMapper;
import dto.product.ProductEntityMap;
import entity.product.ProductGroupPrice;
import dto.product.ProductImage;
import entity.product.ProductRecommend;
import dto.product.ProductSalePrice;
import dto.product.ProductSellingPoints;
import dto.product.ProductUrl;

public class ERPInsertService {

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	ProductCategoryMapperMapper productCategoryMapperMapper;

	@Inject
	ProductEntityMapMapper productEntityMapMapper;

	@Inject
	ProductGroupPriceMapper productGroupPriceMapper;

	@Inject
	ProductImageMapper productImageMapper;

	@Inject
	ProductRecommendMapper productRecommendMapper;

	@Inject
	ProductStorageMapMapper productStorageMapMapper;

	@Inject
	ProductSalePriceMapper productSalePriceMapper;

	@Inject
	ProductUrlMapper productUrlMapper;

	@Inject
	ProductSellingPointsMapper productSellingPointsMapper;

	private StringBuffer errorMessage;

	@Transactional(rethrowExceptionsAs = Exception.class)
	public boolean publishProduct(ProductForERPPublish product)
			throws Exception {
		List<ProductTranslate> translateList = parseToProductTranslate(product);
		if (null == translateList || translateList.isEmpty()) {
			setErrorMessage("Translate for product is null or empty!");
			return false;
		}
		boolean isSuccessful = true;
		for (ProductTranslate productTranslate : translateList) {
			Map<String, Object> productMap = parseToProductMap(product,
					productTranslate);
			if (null != productMap) {
				boolean temp = insertProductMap(productMap);
				if (!temp) {
					setErrorMessage("Language ID: "
							+ productTranslate.getIlanguageid() + "\tSKU: "
							+ product.getCsku() + "\tFormat Error!");
					isSuccessful = false;
				}
				setErrorMessage("Language ID: "
						+ productTranslate.getIlanguageid() + "\tSKU: "
						+ product.getCsku() + "\tFormat Successful!");
			}
		}
		return isSuccessful;
	}

	@SuppressWarnings("unchecked")
	@Transactional(rethrowExceptionsAs = Exception.class)
	private boolean insertProductMap(Map<String, Object> productMap) {
		boolean temp = true;
		try {
			ProductBase productBase = (ProductBase) productMap
					.get("productBase");
			if (null != productBase) {
				productBaseMapper.insertSelective(productBase);
			} else {
				setErrorMessage("Product format error !");
				temp = false;
			}
			ProductTranslate productTranslate = (ProductTranslate) productMap
					.get("productTranslate");
			if (null != productTranslate) {
				productTranslateMapper.insertSelective(productTranslate);
			} else {
				setErrorMessage("Product translate format error !");
				temp = false;
			}
			List<ProductCategoryMapper> productCategoryMappers = (List<ProductCategoryMapper>) productMap
					.get("productCategoryMappers");
			if (null != productCategoryMappers
					&& !productCategoryMappers.isEmpty()) {
				productCategoryMapperMapper.batchInsert(productCategoryMappers);
			} else {
				setErrorMessage("Productcategory format error !");
				temp = false;
			}
			List<ProductRecommend> productRecommends = (List<ProductRecommend>) productMap
					.get("productRecommends");
			if (null != productRecommends && !productRecommends.isEmpty()) {
				productRecommendMapper.batchInsert(productRecommends);
			}
			List<ProductImage> productImages = (List<ProductImage>) productMap
					.get("productImages");
			if (null != productImages && !productImages.isEmpty()) {
				productImageMapper.batchInsert(productImages);
			}
			List<ProductEntityMap> productEntityMaps = (List<ProductEntityMap>) productMap
					.get("productEntityMaps");
			if (null != productEntityMaps && !productEntityMaps.isEmpty()) {
				productEntityMapMapper.batchInsert(productEntityMaps);
			}
			List<ProductGroupPrice> productGroupPrices = (List<ProductGroupPrice>) productMap
					.get("productGroupPrices");
			if (null != productGroupPrices && !productGroupPrices.isEmpty()) {
				productGroupPriceMapper.batchInsert(productGroupPrices);
			}
			ProductSalePrice productSalePrice = (ProductSalePrice) productMap
					.get("productSalePrice");
			if (null != productSalePrice) {
				productSalePriceMapper.insertSelective(productSalePrice);
			}
			List<ProductStorageMap> productStorageMaps = (List<ProductStorageMap>) productMap
					.get("productStorageMaps");
			if (null != productStorageMaps && !productStorageMaps.isEmpty()) {
				productStorageMapMapper.batchInsert(productStorageMaps);
			}
			ProductUrl productUrl = (ProductUrl) productMap.get("productUrl");
			if (null != productUrl) {
				productUrlMapper.insertSelective(productUrl);
			} else {
				temp = false;
			}
			List<ProductSellingPoints> productSellingPoints = (List<ProductSellingPoints>) productMap
					.get("productSellingPoints");
			if (null != productSellingPoints && !productSellingPoints.isEmpty()) {
				productSellingPointsMapper.batchInsert(productSellingPoints);
			}
			if (!temp) {
				setErrorMessage("Other language with SKU is not validate!");
				throw new SaveException(errorMessage);
			}
		} catch (Exception e) {
			throw e;
		}
		return temp;
	}

	private Map<String, Object> parseToProductMap(ProductForERPPublish product,
			ProductTranslate productTranslate) {
		HashMap<String, Object> productMap = new HashMap<String, Object>();
		ProductBase productBase = parseToProductBase(product);
		String csku = productBase.getCsku();
		String clistingid = productBase.getClistingid();
		productTranslate.setCsku(csku);
		productTranslate.setClistingid(clistingid);
		ProductUrl productUrl = createProductUrl(productTranslate);
		List<ProductCategoryMapper> productCategoryMappers = parseToProductCategoryMapper(
				product, csku, clistingid);
		List<ProductRecommend> productRecommends = parseToProductRecommend(
				product, csku, clistingid);
		List<ProductImage> productImages = parseToProductImage(product, csku,
				clistingid);
		List<ProductEntityMap> productEntityMaps = parseToProductEntityMap(
				product, csku, clistingid);
		List<ProductGroupPrice> productGroupPrices = parseToProductGroupPrice(
				product, csku, clistingid);
		ProductSalePrice productSalePrice = parseToProductSalePrice(product,
				csku, clistingid);
		List<ProductStorageMap> productStorageMaps = parseToProductStorageMap(
				product, csku, clistingid);
		List<ProductSellingPoints> productSellingPoints = parseToProductSellingPoints(
				product, csku, clistingid);
		productMap.put("productBase", productBase);
		productMap.put("productCategoryMappers", productCategoryMappers);
		productMap.put("productRecommends", productRecommends);
		productMap.put("productImages", productImages);
		productMap.put("productEntityMaps", productEntityMaps);
		productMap.put("productGroupPrices", productGroupPrices);
		productMap.put("productSalePrice", productSalePrice);
		productMap.put("productStorageMaps", productStorageMaps);
		productMap.put("productTranslate", productTranslate);
		productMap.put("productUrl", productUrl);
		productMap.put("productSellingPoints", productSellingPoints);
		return productMap;
	}

	private List<ProductSellingPoints> parseToProductSellingPoints(
			ProductForERPPublish product, String csku, String clistingid) {
		List<ProductSellingPointsLite> pointsList = product.getLsellingPoints();
		if (null == pointsList || pointsList.isEmpty()) {
			return null;
		}
		Collection<ProductSellingPoints> productSellingPoints = Collections2
				.transform(
						pointsList,
						new Function<ProductSellingPointsLite, ProductSellingPoints>() {
							@Override
							public ProductSellingPoints apply(
									ProductSellingPointsLite sellingPointsLite) {
								if (null == sellingPointsLite) {
									return null;
								}
								ProductSellingPoints productSellingPoints = new ProductSellingPoints();
								productSellingPoints.setCcreateuser(product
										.getCcreateusr());
								productSellingPoints.setDcreatedate(product
										.getDcreatedate());
								productSellingPoints.setClistingid(clistingid);
								productSellingPoints.setCsku(csku);
								productSellingPoints
										.setCcontent(sellingPointsLite
												.getCcontent());
								productSellingPoints
										.setIlanguageid(sellingPointsLite
												.getIlanguageid());
								return productSellingPoints;
							}
						});
		return distinctToList(productSellingPoints);
	}

	private ProductUrl createProductUrl(ProductTranslate productTranslate) {
		ProductUrl productUrl = new ProductUrl();
		productUrl.setCcreateuser(productTranslate.getCcreateuser());
		productUrl.setClistingid(productTranslate.getClistingid());
		productUrl.setDcreatedate(productTranslate.getDcreatedate());
		productUrl.setIlanguage(productTranslate.getIlanguageid());
		productUrl.setSku(productTranslate.getCsku());
		String url = productTranslate.getCtitle().trim().toLowerCase();
		url = url.replaceAll("\"", "");
		url = url.replaceAll("\\W+", "-");
		productUrl.setCurl(url + "-" + productTranslate.getCsku().toLowerCase() + ".html");
		return productUrl;
	}

	private List<ProductStorageMap> parseToProductStorageMap(
			final ProductForERPPublish product, String csku,
			final String clistingid) {
		List<Integer> storageList = product.getLstorage();
		if (null == storageList || storageList.isEmpty()) {
			return null;
		}
		Collection<ProductStorageMap> productStorageMaps = Collections2
				.transform(storageList,
						new Function<Integer, ProductStorageMap>() {
							@Override
							public ProductStorageMap apply(Integer istorageid) {
								if (null == istorageid) {
									return null;
								}
								ProductStorageMap storageMap = new ProductStorageMap();
								storageMap.setCcreateuser(product
										.getCcreateusr());
								storageMap.setDcreatedate(product
										.getDcreatedate());
								storageMap.setClistingid(clistingid);
								storageMap.setCsku(csku);
								storageMap.setIstorageid(istorageid);
								return storageMap;
							}
						});
		return distinctToList(productStorageMaps);
	}

	private ProductSalePrice parseToProductSalePrice(
			ProductForERPPublish product, String csku, String clistingid) {
		ProductSalePriceLite salePriceLite = product.getSalePrice();
		if (null == salePriceLite || null == salePriceLite.getSalePrice()) {
			return null;
		}
		ProductSalePrice salePrice = new ProductSalePrice();
		salePrice.setCcreateuser(product.getCcreateusr());
		salePrice.setClistingid(clistingid);
		salePrice.setCsku(csku);
		salePrice.setDbegindate(salePriceLite.getBeginDate());
		salePrice.setDenddate(salePriceLite.getEndDate());
		salePrice.setFsaleprice(salePriceLite.getSalePrice());
		salePrice.setDcreatedate(product.getDcreatedate());
		return salePrice;
	}

	private List<ProductGroupPrice> parseToProductGroupPrice(
			final ProductForERPPublish product, final String csku,
			final String clistingid) {
		List<ProductGroupPriceLite> groupPriceLites = product.getLgroupPrice();
		if (null == groupPriceLites || groupPriceLites.isEmpty()) {
			return null;
		}
		Collection<ProductGroupPrice> productGroupPrices = Collections2
				.transform(
						groupPriceLites,
						new Function<ProductGroupPriceLite, ProductGroupPrice>() {
							@Override
							public ProductGroupPrice apply(
									ProductGroupPriceLite groupPriceLite) {
								if (null == groupPriceLite) {
									return null;
								}
								ProductGroupPrice groupPrice = new ProductGroupPrice();
								groupPrice.setCcreateuser(product
										.getCcreateusr());
								groupPrice.setDcreatedate(product
										.getDcreatedate());
								groupPrice.setClistingid(clistingid);
								groupPrice.setFprice(groupPriceLite.getFprice());
								groupPrice.setIcustomergroupid(groupPriceLite
										.getIcustomergroupid());
								groupPrice.setIqty(groupPriceLite.getIqty());
								return groupPrice;
							}
						});
		return distinctToList(productGroupPrices);
	}

	private List<ProductEntityMap> parseToProductEntityMap(
			final ProductForERPPublish product, final String csku,
			final String clistingid) {
		List<ProductEntityMapLite> entityMapLites = product.getLentityMap();
		if (null == entityMapLites || entityMapLites.isEmpty()) {
			return null;
		}
		Collection<ProductEntityMap> productEntityMaps = Collections2
				.transform(entityMapLites,
						new Function<ProductEntityMapLite, ProductEntityMap>() {
							@Override
							public ProductEntityMap apply(
									ProductEntityMapLite entityMapLite) {
								if (null == entityMapLite) {
									return null;
								}
								ProductEntityMap entityMap = new ProductEntityMap();
								entityMap.setCcreateuser(product
										.getCcreateusr());
								entityMap.setDcreatedate(product
										.getDcreatedate());
								entityMap.setClistingid(clistingid);
								entityMap.setCsku(csku);
								entityMap.setCkey(entityMapLite.getCkey());
								entityMap.setCvalue(entityMapLite.getCvalue());
								return entityMap;
							}
						});
		return distinctToList(productEntityMaps);
	}

	private List<ProductImage> parseToProductImage(
			ProductForERPPublish product, final String csku,
			final String clistingid) {
		List<ProductImageLite> imageList = product.getLimage();
		if (null == imageList || imageList.isEmpty()) {
			return null;
		}
		Collection<ProductImage> productImages = Collections2.transform(
				imageList, new Function<ProductImageLite, ProductImage>() {
					@Override
					public ProductImage apply(ProductImageLite productImageLite) {
						if (null == productImageLite) {
							return null;
						}
						ProductImage productImage = new ProductImage();
						productImage.setBbaseimage(productImageLite
								.getBbaseimage());
						productImage.setBsmallimage(productImageLite
								.getBsmallimager());
						productImage.setBthumbnail(productImageLite
								.getBthumbnail());
						productImage.setCimageurl(productImageLite
								.getCimagerurl());
						productImage.setClabel(productImageLite.getClabel());
						productImage.setIorder(productImageLite
								.getIsortorder());
						productImage.setClistingid(clistingid);
						productImage.setCsku(csku);
						return productImage;
					}
				});
		return distinctToList(productImages);
	}

	private List<ProductRecommend> parseToProductRecommend(
			final ProductForERPPublish product, final String csku,
			final String clistingid) {
		List<String> skuList = product.getLrecommend();
		if (null == skuList || skuList.isEmpty()) {
			return null;
		}
		Collection<ProductRecommend> productRecommends = Collections2
				.transform(skuList, new Function<String, ProductRecommend>() {
					@Override
					public ProductRecommend apply(String crsku) {
						if (null == crsku || crsku.trim().isEmpty()) {
							return null;
						}
						ProductRecommend productRecommend = new ProductRecommend();
						productRecommend.setCcreateuser(product.getCcreateusr());
						productRecommend.setClistingid(clistingid);
						productRecommend.setCsku(csku);
						productRecommend.setDcreatedate(product
								.getDcreatedate());
						productRecommend.setCrecommendsku(crsku);
						return productRecommend;
					}
				});
		return distinctToList(productRecommends);
	}

	private List<ProductCategoryMapper> parseToProductCategoryMapper(
			final ProductForERPPublish product, final String csku,
			final String clistingid) {
		List<Integer> categoryList = product.getLcategory();
		if (null == categoryList || categoryList.isEmpty()) {
			return null;
		}
		Collection<ProductCategoryMapper> productCategoryMappers = Collections2
				.transform(categoryList,
						new Function<Integer, ProductCategoryMapper>() {
							@Override
							public ProductCategoryMapper apply(Integer icategory) {
								if (null == icategory) {
									return null;
								}
								ProductCategoryMapper productCategoryMapper = new ProductCategoryMapper();
								productCategoryMapper.setCcreateuser(product
										.getCcreateusr());
								productCategoryMapper.setClistingid(clistingid);
								productCategoryMapper.setCsku(csku);
								productCategoryMapper.setDcreatedate(product
										.getDcreatedate());
								productCategoryMapper.setIcategory(icategory);
								return productCategoryMapper;
							}
						});
		return distinctToList(productCategoryMappers);
	}

	private List<ProductTranslate> parseToProductTranslate(
			ProductForERPPublish product) {
		List<ProductTranslateLite> btoList = product.getLtranslate();
		if (null == btoList || btoList.isEmpty()) {
			return null;
		}
		List<ProductTranslate> list = new ArrayList<ProductTranslate>();
		for (ProductTranslateLite productTranslateLite : btoList) {
			if (null == productTranslateLite
					|| null == productTranslateLite.getCtitle()
					|| productTranslateLite.getCtitle().trim().equals("")) {
				return null;
			}
			ProductTranslate productTranslate = new ProductTranslate();
			productTranslate.setCcreateuser(product.getCcreateusr());
			productTranslate.setDcreatedate(product.getDcreatedate());
			productTranslate.setCdescription(productTranslateLite
					.getCdescription());
			productTranslate.setCmetadescription(productTranslateLite
					.getCmatedescription());
			productTranslate.setCmetakeyword(productTranslateLite
					.getCmatekeyword());
			productTranslate
					.setCmetatitle(productTranslateLite.getCmetatitle());
			productTranslate.setCshortdescription(productTranslateLite
					.getCshortdescription());
			productTranslate.setCtitle(productTranslateLite.getCtitle());
			list.add(productTranslate);
		}
		return list;
	}

	private ProductBase parseToProductBase(ProductForERPPublish product) {
		if (null == product.getFprice()
				|| product.getFprice().compareTo((double) 0) < 0) {
			return null;
		}
		ProductBase productBase = new ProductBase();
		productBase.setCcreateuser(product.getCcreateusr());
		productBase.setCsku(product.getCsku());
		productBase.setCvideoaddress(product.getCvideoaddress());
		productBase.setDcreatedate(product.getDcreatedate());
		productBase.setDnewformdate(product.getDnewformdate());
		productBase.setDnewtodate(product.getDnewtodate());
		productBase.setFprice(product.getFprice());
		productBase.setIclearstocks(product.getIclearstocks());
		productBase.setIqty(product.getIqty());
		productBase.setIspecial(product.getIspecial());
		productBase.setIstatus(product.getIstatus());
		productBase.setIwebsiteid(product.getIwebsiteid());
		productBase.setClistingid(getUUID());
		return productBase;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public StringBuffer getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		if (null == this.errorMessage) {
			this.errorMessage = new StringBuffer();
		}
		this.errorMessage.append(errorMessage + "\n");
	}

	private <T> List<T> distinctToList(Collection<T> collection) {
		if (null != collection && !collection.isEmpty()) {
			HashSet<T> set = new HashSet<T>(collection);
			set.remove(null);
			List<T> list = new ArrayList<T>(set);
			return list;
		}
		return null;
	}

}*/