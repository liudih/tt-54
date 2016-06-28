package com.tomtop.es.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 产品数据格式
 * @author ztiny
 * @Date 2015-12-19
 */
public class ProductEntity implements Serializable{
	
	private static final long serialVersionUID = 2502281211062701703L;
	//产品ID
	public String listingId;
	//产品对应的sku
	public String sku;
	//产品父spu
	public String spu;
	//国际化字段
	private List<MutilLanguage> mutil  = new ArrayList<MutilLanguage>();
	//站点
	public List<Integer> webSites = new ArrayList<Integer>();
	//促销价
	public List<PromotionPrice> promotionPrice = new ArrayList<PromotionPrice>();
	//是否促销
	public Boolean isOnSale = false;
	//成本价
	public Double costPrice=0.00;
	//原价
	public Double yjPrice=0.00;
	//重量
	public Double weight=0.00;
	//仓库 
	public List<DepotEntity> depots = new ArrayList<DepotEntity>();
	//发布时间
	public String releaseTime;
	//产品排序
	public int orderScore =999;
	//查看次数
	private int viewcount=0;
	//是否为活动产品
	private boolean bactivity;
	//品牌
	private String brand;
	//列表页产品默认图片
	private String defaultImgUrl;
	//图片URL
	public List<ProductImageEntity> imgs = new ArrayList<ProductImageEntity>();
	//产品收藏数
	public Integer colltes=0;
	//商品评论
	private ReviewStartNumBo review;
	//是否主商品
	private Boolean bmain = false;
	//标签
	private List<TagEntity> tagsName = new ArrayList<TagEntity>();
	//状态
	private Integer status=1;
	//库存
	private Integer storeNum =9999;
	//是否免邮商品
	private boolean isFreeShipping =  false;
	//视频
	public List<String> videos = new ArrayList<String>();
	//3天销量
	private Integer sales3Count;
	//7天销量	
	private Integer sales7Count;
	//30天销量
	private Integer sales30Count;
	//60天销量
	private Integer sales60Count;
	//90天销量
	private Integer sales90Count;
	//180天销量
	private Integer sales180Count;
	//365天销量
	private Integer sales365Count;
	//total总销量
	private Integer salesTotalCount;

	
	public List<String> getVideos() {
		return videos;
	}
	public void setVideos(List<String> videos) {
		this.videos = videos;
	}
	public boolean getIsFreeShipping() {
		return isFreeShipping;
	}
	public void setIsFreeShipping(boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getSpu() {
		return spu;
	}
	public void setSpu(String spu) {
		this.spu = spu;
	}

	public List<Integer> getWebSites() {
		return webSites;
	}
	public void setWebSites(List<Integer> webSites) {
		this.webSites = webSites;
	}

	public Boolean getIsOnSale() {
		return isOnSale;
	}
	public void setIsOnSale(Boolean isOnSale) {
		this.isOnSale = isOnSale;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public Double getYjPrice() {
		return yjPrice;
	}
	public void setYjPrice(Double yjPrice) {
		this.yjPrice = yjPrice;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public List<DepotEntity> getDepots() {
		return depots;
	}
	public void setDepots(List<DepotEntity> depots) {
		this.depots = depots;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public int getOrderScore() {
		return orderScore;
	}
	public void setOrderScore(int orderScore) {
		this.orderScore = orderScore;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getDefaultImgUrl() {
		return defaultImgUrl;
	}
	public void setDefaultImgUrl(String defaultImgUrl) {
		this.defaultImgUrl = defaultImgUrl;
	}
	public List<ProductImageEntity> getImgs() {
		return imgs;
	}
	public void setImgs(List<ProductImageEntity> imgs) {
		this.imgs = imgs;
	}
	public Integer getColltes() {
		return colltes;
	}
	public void setColltes(Integer colltes) {
		this.colltes = colltes;
	}
	public ReviewStartNumBo getReview() {
		return review;
	}
	public void setReview(ReviewStartNumBo review) {
		this.review = review;
	}
	public Boolean getBmain() {
		return bmain;
	}
	public void setBmain(Boolean bmain) {
		this.bmain = bmain;
	}
	public List<TagEntity> getTagsName() {
		return tagsName;
	}
	public void setTagsName(List<TagEntity> tagsName) {
		this.tagsName = tagsName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(Integer storeNum) {
		this.storeNum = storeNum;
	}
	public List<MutilLanguage> getMutil() {
		return mutil;
	}
	public void setMutil(List<MutilLanguage> mutil) {
		this.mutil = mutil;
	}
	public List<PromotionPrice> getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(List<PromotionPrice> promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public Integer getSales3Count() {
		return sales3Count;
	}
	public void setSales3Count(Integer sales3Count) {
		this.sales3Count = sales3Count;
	}
	public Integer getSales7Count() {
		return sales7Count;
	}
	public void setSales7Count(Integer sales7Count) {
		this.sales7Count = sales7Count;
	}
	public Integer getSales30Count() {
		return sales30Count;
	}
	public void setSales30Count(Integer sales30Count) {
		this.sales30Count = sales30Count;
	}
	public Integer getSales60Count() {
		return sales60Count;
	}
	public void setSales60Count(Integer sales60Count) {
		this.sales60Count = sales60Count;
	}
	public Integer getSales90Count() {
		return sales90Count;
	}
	public void setSales90Count(Integer sales90Count) {
		this.sales90Count = sales90Count;
	}
	public Integer getSales180Count() {
		return sales180Count;
	}
	public void setSales180Count(Integer sales180Count) {
		this.sales180Count = sales180Count;
	}
	public Integer getSales365Count() {
		return sales365Count;
	}
	public void setSales365Count(Integer sales365Count) {
		this.sales365Count = sales365Count;
	}
	public Integer getSalesTotalCount() {
		return salesTotalCount;
	}
	public void setSalesTotalCount(Integer salesTotalCount) {
		this.salesTotalCount = salesTotalCount;
	}
	public int getViewcount() {
		return viewcount;
	}
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	public boolean getBactivity() {
		return bactivity;
	}
	public void setBactivity(boolean bactivity) {
		this.bactivity = bactivity;
	}
	
}
