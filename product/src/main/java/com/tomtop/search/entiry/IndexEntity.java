package com.tomtop.search.entiry;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tomtop.product.models.bo.ReviewStartNumBo;

public class IndexEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3328070324788115308L;
	//产品ID
	private String listingId;
	//产品对应的sku
	private String sku;
	//产品父sku
	private String spu;
	//站点
	private List<Integer> webSites = new ArrayList<Integer>();
	//促销价
	private List<PromotionPrice> promotionPrice = new ArrayList<PromotionPrice>();
	//是否促销
	private Boolean isOnSale;
	//成本价
	private Double costPrice;
	//原价
	private Double yjPrice;
	//重量
	private Double weight;
	//查看次数
	private int  viewcount;
	//仓库 
	private List<DepotEntity> depots = new ArrayList<DepotEntity>();
	//发布时间
	private String releaseTime;
	//产品排序
	private Double orderScore;
	//单语言产品属性对象
	private MutilLanguage mutil;
	//品牌
	private String brand;
	//产品收藏数
	private Integer colltes;
	//商品评论
	private ReviewStartNumBo review;
	//列表页产品默认图片
	private String defaultImgUrl;
	//是否主产品
	private Boolean bmain;
	//图片URL
	private List<ProductImageEntity> imgs = new ArrayList<ProductImageEntity>();
	//标签
	private List<TagEntity> tagsName = new ArrayList<TagEntity>();
	//状态
	private Integer status;
	//库存
	private Long storeNum;
	//是否免邮商品
	private boolean isFreeShipping;
	//是否活动商品
	private boolean bactivity;
	//视频
	private List<String> videos = new ArrayList<String>();
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
		
	public String getSpu() {
		return spu;
	}
	public void setSpu(String spu) {
		this.spu = spu;
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
	public List<Integer> getWebSites() {
		return webSites;
	}
	public void setWebSites(List<Integer> webSites) {
		this.webSites = webSites;
	}
	public boolean isOnSale() {
		return isOnSale;
	}
	public void setOnSale(boolean isOnSale) {
		this.isOnSale = isOnSale;
	}
	public Double getCostPrice() {
		if(costPrice == null){
			costPrice = 0.00;
		}
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	public double getYjPrice() {
		return yjPrice;
	}
	public void setYjPrice(double yjPrice) {
		this.yjPrice = yjPrice;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
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
	public double getOrderScore() {
		return orderScore;
	}
	public int getViewcount() {
		return viewcount;
	}
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	public void setOrderScore(double orderScore) {
		this.orderScore = orderScore;
	}
	public MutilLanguage getMutil() {
		return mutil;
	}
	public void setMutil(MutilLanguage mutil) {
		this.mutil = mutil;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getColltes() {
		return colltes;
	}
	public void setColltes(int colltes) {
		this.colltes = colltes;
	}
	public ReviewStartNumBo getReview() {
		return review;
	}
	public void setReview(ReviewStartNumBo review) {
		this.review = review;
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
	public List<TagEntity> getTagsName() {
		return tagsName;
	}
	public void setTagsName(List<TagEntity> tagsName) {
		this.tagsName = tagsName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(Long storeNum) {
		this.storeNum = storeNum;
	}
	public Boolean getBmain() {
		return bmain;
	}
	public void setBmain(Boolean bmain) {
		this.bmain = bmain;
	}
	public boolean getIsFreeShipping() {
		return isFreeShipping;
	}
	public void setIsFreeShipping(boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	public List<PromotionPrice> getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(List<PromotionPrice> promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public Boolean getIsOnSale() {
		return isOnSale;
	}
	public void setIsOnSale(Boolean isOnSale) {
		this.isOnSale = isOnSale;
	}
	public List<String> getVideos() {
		return videos;
	}
	public void setVideos(List<String> videos) {
		this.videos = videos;
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
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public void setYjPrice(Double yjPrice) {
		this.yjPrice = yjPrice;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public void setOrderScore(Double orderScore) {
		this.orderScore = orderScore;
	}
	public void setColltes(Integer colltes) {
		this.colltes = colltes;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public boolean isBactivity() {
		return bactivity;
	}
	public void setBactivity(boolean bactivity) {
		this.bactivity = bactivity;
	}
	public boolean isCleanStrock(){
		if(tagsName != null && tagsName.size() > 0){
			for (int i = 0; i < tagsName.size(); i++) {
				if("clearstocks".equals(tagsName.get(i).getTagName().trim())){
					return true;
				}
			}
		}
		return false;
	}
}
