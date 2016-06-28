package com.tomtop.product.mappers.wholesale;

import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.wholesale.WholeSaleBaseDto;

public interface WholeSaleBaseMapper {

	@Select("select iid, cfullname, cemail, iwebsiteid, ctelephone, ccountrysn,"
			+ "cshipurl,cskype, ccomment,cshippingaddress, fpurchaseamount, istatus,"
			+ "dcreatedate,ilanguageid from t_wholesale_base where cemail=#{0} "
			+ "and iwebsiteid=#{1} and istatus=#{2}")
	public WholeSaleBaseDto getWholeSaleBaseDto(String email, Integer iwebsiteId,Integer istatus);
}
