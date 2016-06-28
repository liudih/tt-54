package com.tomtop.product.mappers.dropship;

import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.DropshipBaseDto;

public interface DropshipMapper {

	@Select("select b.cemail,l.clevelname,l.discount,l.iproductcount from t_dropship_base b "
			+ "LEFT JOIN t_dropship_level l on b.idropshiplevel=l.iid "
			+ "where b.cemail=#{0} and b.iwebsiteid=#{1} ")
	public DropshipBaseDto getDropshipBaseDto(String email,Integer siteId);
}
