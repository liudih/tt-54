package valueobjects.product;

import java.util.List;

import dto.product.ProductBase;
import dto.product.ProductCategoryMapper;
import dto.product.ProductEntityMap;
import dto.product.ProductImage;
import dto.product.ProductLabel;
import dto.product.ProductSellingPoints;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;
import dto.product.ProductVideo;

public class FullListingObject {

	final int siteId;

	final String listingId;

	final ProductBase pBase;

	// 产品评论
	final List<Integer> commentId;

	// 产品发货仓库表
	final List<dto.product.ProductStorageMap> storageMap;

	// 产品视频表
	final List<ProductVideo> pVideo;

	// 产品图片表
	final List<ProductImage> productImage;

	// 产品标签表
	final List<ProductLabel> pLabel;

	// 产品自定义属性表
	final List<ProductEntityMap> entityMap;

	// 产品翻译信息表
	final List<ProductTranslate> pTranslate;

	// 产品详情路由表
	final List<ProductUrl> pUrl;

	// 产品卖点表
	final List<ProductSellingPoints> sellingPoints;

	// 产品品类对应表
	final List<ProductCategoryMapper> caMapper;

	public FullListingObject(int siteId, String listingId, ProductBase pBase,
			List<Integer> commentId, List<dto.product.ProductStorageMap> storageMap,
			List<ProductVideo> pVideo, List<ProductImage> productImage,
			List<ProductLabel> pLabel, List<ProductEntityMap> entityMap,
			List<ProductTranslate> pTranslate, List<ProductUrl> pUrl,
			List<ProductSellingPoints> sellingPoints,
			List<ProductCategoryMapper> caMapper) {
		super();
		this.siteId = siteId;
		this.listingId = listingId;
		this.pBase = pBase;
		this.commentId = commentId;
		this.storageMap = storageMap;
		this.pVideo = pVideo;
		this.productImage = productImage;
		this.pLabel = pLabel;
		this.entityMap = entityMap;
		this.pTranslate = pTranslate;
		this.pUrl = pUrl;
		this.sellingPoints = sellingPoints;
		this.caMapper = caMapper;
	}

	public int getSiteId() {
		return siteId;
	}

	public String getListingId() {
		return listingId;
	}

	public ProductBase getpBase() {
		return pBase;
	}

	public List<Integer> getCommentId() {
		return commentId;
	}

	public List<dto.product.ProductStorageMap> getStorageMap() {
		return storageMap;
	}

	public List<ProductVideo> getpVideo() {
		return pVideo;
	}

	public List<ProductImage> getProductImage() {
		return productImage;
	}

	public List<ProductLabel> getpLabel() {
		return pLabel;
	}

	public List<ProductEntityMap> getEntityMap() {
		return entityMap;
	}

	public List<ProductTranslate> getpTranslate() {
		return pTranslate;
	}

	public List<ProductUrl> getpUrl() {
		return pUrl;
	}

	public List<ProductSellingPoints> getSellingPoints() {
		return sellingPoints;
	}

	public List<ProductCategoryMapper> getCaMapper() {
		return caMapper;
	}

}
