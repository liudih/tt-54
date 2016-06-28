package com.tomtop.product.services.dropship.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.interaction.mappers.DropshipProductMapper;
import com.tomtop.product.mappers.dropship.DropshipMapper;
import com.tomtop.product.mappers.product.ProductDtlMapper;
import com.tomtop.product.models.bo.BaseBo;
import com.tomtop.product.models.dto.DropshipBaseDto;
import com.tomtop.product.models.dto.DropshipProductDto;
import com.tomtop.product.models.dto.ProductBase;
import com.tomtop.product.services.dropship.IDropshipProductService;

@Service
public class DropshipProductServiceImpl implements IDropshipProductService {

	private static final Logger logger = LoggerFactory.getLogger(DropshipProductServiceImpl.class);
	
	@Autowired
	DropshipProductMapper dropshipProductMapper;
	
	@Autowired
	DropshipMapper dropshipMapper;
	
	@Autowired
	ProductDtlMapper productDtlMapper;
	
	@Override
	public BaseBo addProductDropshipByListingId(String email, String listingId, Integer siteId) {
		BaseBo bb = new BaseBo();
		//先判断用户是否为dropship
		DropshipBaseDto dbdto = dropshipMapper.getDropshipBaseDto(email, siteId);
		if(dbdto == null){
		 	bb.setRes(-82001);
	    	bb.setMsg("user is not dropship");
	    	return bb;
		}
		Integer productCount = dbdto.getIproductcount();//用户可以收藏的dropship数量
		Integer pcount = dropshipProductMapper.getDropshipPrdouctCountByEmail(email, siteId);
		if(pcount == null){
			pcount = 0;
		}
		//根据listingId获取所有的sku
		List<String> skuList = new ArrayList<String>();
		ProductBase pb = productDtlMapper.getProductBaseByListingId(listingId,1, siteId);
		skuList.add(pb.getCsku());
		String parentSku = pb.getCparentsku();
		if(parentSku != null){
			List<ProductBase> pbList = productDtlMapper.getProductBaseByParentSku(listingId,1, parentSku, siteId);
			if(pbList != null && pbList.size() > 0 ){
				for (int i = 0; i < pbList.size(); i++) {
					skuList.add(pbList.get(i).getCsku());
				}
			}
		}
		Integer leftCount = productCount - pcount;
		if(leftCount == 0 ){
			bb.setRes(-82002);
			bb.setMsg("Collection number reaches the upper limit");
			return bb;
		}
		Integer skuLen = skuList.size();
		leftCount = leftCount - skuLen;
		if(leftCount <= 0 ){
			bb.setRes(-82003);
			bb.setMsg("the number of collection sku is beyond the capacity ");
			return bb;
		}	
		DropshipProductDto dpdto = null;
		int res = 0;
		String sku = "";
		for (int i = 0; i < skuLen; i++) {
			sku = skuList.get(i);
			dpdto = new DropshipProductDto();
			dpdto.setCemail(email);
			dpdto.setCsku(sku);
			dpdto.setBstate(true);
			dpdto.setIwebsiteid(siteId);
			res = dropshipProductMapper.insertSelective(dpdto);
			if(res == 0){
				logger.error("dropship product add error [" + sku + "]-[" + email + "]" );
			}
		}
		bb.setRes(1);
		return bb;
	}

	@Override
	public BaseBo addProductDropshipBySku(String email, String sku,
			Integer siteId) {
		BaseBo bb = new BaseBo();
		if(email == null || "".equals(email)){
			    bb.setRes(-82004);
			    bb.setMsg("email is null");
			    return bb;
		}
		if(sku == null || "".equals(sku)){
			    bb.setRes(-82005);
			    bb.setMsg("sku is null");
			    return bb;
		}
		if(siteId == null){
			 siteId = 1;
		}
		 
		//先判断用户是否为dropship
		DropshipBaseDto dbdto = dropshipMapper.getDropshipBaseDto(email, siteId);
		if(dbdto == null){
		 	bb.setRes(-82006);
	    	bb.setMsg("user is not dropship");
	    	return bb;
		}
		Integer productCount = dbdto.getIproductcount();//用户可以收藏的dropship数量
		Integer pcount = dropshipProductMapper.getDropshipPrdouctCountByEmail(email, siteId);
		if(pcount == null){
			pcount = 0;
		}
		Integer leftCount = productCount - pcount;
		if(leftCount == 0 ){
			bb.setRes(-82007);
			bb.setMsg("Collection number reaches the upper limit");
			return bb;
		}
		
		DropshipProductDto dpdto = new DropshipProductDto();
		dpdto.setCemail(email);
		dpdto.setCsku(sku);
		dpdto.setBstate(true);
		dpdto.setIwebsiteid(siteId);
		DropshipProductDto dp = dropshipProductMapper.getDropshipProductDto(dpdto);
		if(dp != null){
			bb.setRes(-82008);
			bb.setMsg("drop ship product has favorites");
			return bb;
		}
		int res = 0;
		res = dropshipProductMapper.insertSelective(dpdto);
		if(res == 0){
			logger.error("dropship product add error [" + sku + "]-[" + email + "]" );
		}
		bb.setRes(1);
		return bb;
	}

}
