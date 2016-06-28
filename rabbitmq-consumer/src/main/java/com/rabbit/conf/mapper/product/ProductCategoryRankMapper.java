package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.rabbit.dto.product.ProductCategoryRank;
import com.rabbit.dto.valueobjects.product.CategoryRankProduct;

public interface ProductCategoryRankMapper {
    int insertSelective(ProductCategoryRank record);

    @Select("select clistingid listingId,isales sales "
    		+ "from t_product_category_rank "
    		+ "where icategoryid=#{0} "
    		+ "order by isales desc limit #{1}")
    List<CategoryRankProduct> select(Integer icategoryid, Integer size);

    @Update("update t_product_category_rank set isales = isales + #{0} where clistingid = #{1}")
    int updateSalesByListingId(Integer sales, String listingId);
    
    @Select("select isales from t_product_category_rank where clistingid=#{0} and iwebsiteid=#{1}")
    ProductCategoryRank selectSales(String clistingid,Integer website);
}