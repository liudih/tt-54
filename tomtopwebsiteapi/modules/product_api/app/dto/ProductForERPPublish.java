package dto;

import java.io.Serializable;
import java.util.List;

import dto.product.ProductBase;

public class ProductForERPPublish implements Serializable{

	private static final long serialVersionUID = 1L;

	// Product Base List
	private List<ProductBase> lproductBases;

	// Product Category Mapper ID List
	private List<Integer> lcategory;

	// Product Entity Map List
	private List<ProductEntityMapLite> lentityMap;

	// Product Group Price List
	private List<ProductGroupPriceLite> lgroupPrice;

	// Product Image List
	private List<ProductImageLite> limage;

	// Product Recommend
	private List<String> lrecommend;

	// Product Sale Price
	private ProductSalePriceLite salePrice;

	// Product Storage Map
	private List<Integer> lstorage;

	// Product Selling Points
	private List<ProductSellingPointsLite> lsellingPoints;


}
