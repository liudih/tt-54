package com.tomtop.product.services.wholesale.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.product.mappers.wholesale.WholeSaleBaseMapper;
import com.tomtop.product.mappers.wholesale.WholeSaleProductMapper;
import com.tomtop.product.models.bo.BaseBo;
import com.tomtop.product.models.dto.wholesale.WholeSaleBaseDto;
import com.tomtop.product.models.dto.wholesale.WholeSaleProductDto;
import com.tomtop.product.services.wholesale.IWholeSaleProductService;

@Service
public class WholeSaleProductServiceImpl implements IWholeSaleProductService {

	@Autowired
	WholeSaleBaseMapper wholeSaleBaseMapper;
	@Autowired
	WholeSaleProductMapper wholeSaleProductMapper;
	
	private final static Integer STATUS = 1;
	
	@Override
	public BaseBo addWholeSaleProduct(String email, String sku, Integer qty,
			Integer siteId) {
		BaseBo bb = new BaseBo();
		if(email == null || "".equals(email)){
		    bb.setRes(-83001);
		    bb.setMsg("email is null");
		    return bb;
		}
		if(sku == null || "".equals(sku)){
		    bb.setRes(-83002);
			bb.setMsg("sku is null");
		    return bb;
		}
		if(qty == null || qty < 5){
			qty = 5;
		}
		if(siteId == null){
			 siteId = 1;
		}
	/*	WholeSaleBaseDto wsbdto = new WholeSaleBaseDto();
		wsbdto.setCemail(email);
		wsbdto.setIwebsiteid(siteId);
		wsbdto.setIstatus(STATUS);*/
		
		WholeSaleBaseDto wsbdto = wholeSaleBaseMapper.getWholeSaleBaseDto(email,siteId,STATUS);
		if(wsbdto == null){
			 bb.setRes(-83003);
			 bb.setMsg("Not WholeSale user");
			 return bb;
		}
		int res = 0;
		WholeSaleProductDto wspdto = wholeSaleProductMapper.getWholeSaleProductsByEmailAndSkuAndWebsite(email, siteId, sku);
		if(wspdto == null){
			wspdto = new WholeSaleProductDto();
			wspdto.setCemail(email);
			wspdto.setCsku(sku);
			wspdto.setIwebsiteid(siteId);
			wspdto.setIqty(qty);
			res = wholeSaleProductMapper.addWholeSaleProduct(wspdto);
			if(res <= 0){
				 bb.setRes(-83004);
				 bb.setMsg("WholeSale product add failure");
				 return bb;
			}
		}else{
			WholeSaleProductDto upd = new WholeSaleProductDto();
			qty += wspdto.getIqty();
			upd.setIid(wspdto.getIid());
			upd.setIqty(qty);
			res = wholeSaleProductMapper.updateQtyByIid(upd);
			if(res <= 0){
				 bb.setRes(-83005);
				 bb.setMsg("WholeSale product update failure");
				 return bb;
			}
		}
		bb.setRes(1);
		
		return bb;
	}

}
