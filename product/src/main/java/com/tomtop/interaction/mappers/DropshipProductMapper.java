package com.tomtop.interaction.mappers;

import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.DropshipProductDto;

public interface DropshipProductMapper {

	@Select("select count(iid) from t_dropship_product where cemail =#{0} and bstate=true and iwebsiteid=#{1} ")
	Integer getDropshipPrdouctCountByEmail(String email, Integer siteId);
	
	int insertSelective(DropshipProductDto record);
	
	DropshipProductDto getDropshipProductDto(DropshipProductDto record);
}