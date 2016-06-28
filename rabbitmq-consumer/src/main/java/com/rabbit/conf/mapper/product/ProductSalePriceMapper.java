package com.rabbit.conf.mapper.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.rabbit.dto.ProductSalePriceLite;
import com.rabbit.dto.product.ProductSalePrice;

public interface ProductSalePriceMapper {

	int addProductSalePrice(ProductSalePrice record);

	int addBatch(List<ProductSalePrice> list);

	int updateByPrimaryKeySelective(ProductSalePrice record);

	@Select("SELECT p.fsaleprice,p.dbegindate,p.denddate,p.dcreatedate,p.ccreateuser FROM t_product_saleprice p "
			+ "WHERE clistingid = #{listingId} " + "ORDER BY p.denddate desc")
	List<ProductSalePrice> getAllProductSalePriceByListingId(String listingId);

	@Select("<script>"
			+ "SELECT p.clistingid, p.fsaleprice,p.dbegindate,p.denddate FROM t_product_saleprice p "
			+ "WHERE clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "ORDER BY p.denddate desc" + "</script>")
	List<ProductSalePrice> getAllProductSalePriceByListingIds(
			@Param("list") List<String> listingIDs);

	@Select("SELECT b.fprice, p.clistingid, p.fsaleprice,p.dbegindate,p.denddate FROM t_product_saleprice p "
			+ " inner join t_product_base b on p.clistingid=b.clistingid "
			+ "WHERE p.clistingid = #{listingId} AND NOW()"
			+ "BETWEEN p.dbegindate AND p.denddate "
			+ "ORDER BY p.denddate desc limit 1")
	ProductSalePriceLite getProductSalePriceLite(String listingId);

	List<ProductSalePrice> getProductSalePrice(Date now,
			@Param("list") List<String> listingIds);

	@Select("select iid, clistingid, csku, fsaleprice, dbegindate,denddate, ccreateuser,"
			+ "dcreatedate from t_product_saleprice "
			+ " where denddate < #{1} and denddate >= #{0}")
	List<ProductSalePrice> getProductSaleByDenddate(Date beginDate, Date endDate);

	@Select({ "<script> select clistingid from t_product_saleprice where clistingid in"
			+ " <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach></script> " })
	List<String> getExistsListings(@Param("list") List<String> listingIds);

	@Select("select clistingid from t_product_saleprice p where NOW()"
			+ "BETWEEN p.dbegindate AND p.denddate ")
	List<String> getSaleListings();

	/**
	 * 查询当前时间后有效的记录
	 * 
	 * @author lijun
	 * @param listingId
	 * @return
	 */
	List<ProductSalePrice> getProductSalePriceAfterCurrentDate(
			Map<String, Object> paras);

	@Select("SELECT * FROM t_product_saleprice WHERE clistingid = #{0} "
			+ "AND (#{1} BETWEEN dbegindate AND denddate OR #{2} BETWEEN dbegindate AND denddate)")
	List<ProductSalePrice> getProductSalePriceByDate(String listingId,
			Date start, Date end);

	@Delete("delete from t_product_saleprice where clistingid=#{0} and  NOW() BETWEEN dbegindate AND denddate ")
	int deleteProductCurrentSalePrice(String listingId);
	
	@Delete("DELETE FROM t_product_saleprice WHERE clistingid = #{0} "
			+ "AND (#{1} BETWEEN dbegindate AND denddate OR #{2} BETWEEN dbegindate AND denddate)")
	int deleteProductSalePriceByDate(String listingId,
			Date start, Date end);
	
	@Update("update t_product_saleprice set fsaleprice = #{0},dbegindate= #{1},denddate= #{2} where clistingid = #{3}")
	int updateByListingid(double fsaleprice,Date dbegindate,Date denddate,String clistingid);
	
	/**
	 * 该listingId商品是否是折扣商品
	 * @param listingId
	 * @return 1：折扣商品  0：非折扣商品
	 */
	@Select("select count(clistingid) from t_product_saleprice where clistingid = #{0} and dbegindate <= now() and now() <= denddate")
	int isOffPrice(String listingId);
}