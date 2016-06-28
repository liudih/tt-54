package com.tomtop.product.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.CategoryPathDto;
import com.tomtop.product.models.dto.category.CategoryLable;

public interface CategoryMapper {

	@Select("select cn.icategoryid,cn.cname,cba.ilevel,cba.iparentid,cba.cpath "
			+ "from t_category_name cn INNER JOIN t_category_base cba "
			+ "on cn.icategoryid=cba.iid where cn.ilanguageid=#{1} and "
			+ "cn.icategoryid=#{0}")
	public CategoryLable getCategoryLable(Integer categoryId,Integer langId);
	
	@Select("select cn.icategoryid,cn.cname,cba.ilevel,cba.iparentid "
			+ "from t_category_name cn INNER JOIN t_category_base cba "
			+ "on cn.icategoryid=cba.iid where cn.ilanguageid=#{1} and "
			+ "cba.cpath=#{0}")
	public CategoryLable getCategoryLableByPath(String path,Integer langId);
	
	@Select("select cn.icategoryid,cn.cname,cba.ilevel,cba.cpath from t_product_category_mapper pcm "
			+ "LEFT JOIN t_category_name cn on pcm.icategory=cn.icategoryid "
			+ "LEFT JOIN t_category_base cba on cn.icategoryid=cba.iid "
			+ "where cn.ilanguageid=#{1} and pcm.clistingid=#{0} "
			+ "order by cn.icategoryid LIMIT 3")
	public List<CategoryLable> getCategoryLableByListingId(String listingId,Integer langId);
	
	@Select("select icategoryid iid,cpath from t_category_website order by icategoryid")
	public List<CategoryPathDto> getCategoryPathDto();
}
