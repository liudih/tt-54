package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IProductCollectDao;
import com.tomtop.product.models.bo.BaseBo;
import com.tomtop.product.models.bo.CollectCountBo;
import com.tomtop.product.models.dto.CollectCountDto;
import com.tomtop.product.models.dto.ProductCollectDto;
import com.tomtop.product.services.IProductCollectService;

/**
 * 产品收藏
 * 
 * @author liulj
 *
 */
@Repository("productCollectService")
public class ProductCollectServiceImpl implements IProductCollectService {

	@Resource(name = "productCollectDao")
	private IProductCollectDao dao;

	@Override
	public List<CollectCountBo> getCollectCountByListingIds(
			List<String> listingIds) {
		return Lists.transform(dao.getCollectCountByListingIds(listingIds),
				p -> BeanUtils.mapFromClass(p, CollectCountBo.class));
	}

	@Override
	public CollectCountBo getCollectCountByListingId(String listingIds) {
		CollectCountBo ccb = new CollectCountBo();
		CollectCountDto ccd = dao.getCollectCountByListingId(listingIds);
		if(ccd != null){
			BeanUtils.copyPropertys(ccd,ccb);
		}else{
			ccb.setCollectCount(0);
			ccb.setListingId(listingIds);
		}
		return ccb;
	}

	@Override
	public BaseBo addCollectCount(String listingId, String email) {
		BaseBo bb = new BaseBo();
		if(listingId == null || "".equals(listingId)){
		  	bb.setRes(-81001);
		   	bb.setMsg("listingId is null");
		    return bb;
	    }
	    if(email == null || "".equals(email)){
		    bb.setRes(-81002);
		    bb.setMsg("email is null");
		    return bb;
		}
		ProductCollectDto pcdto = null;
		pcdto = dao.getProductCollectDtoByEmailAndListingId(listingId, email);
		if(pcdto == null){
			pcdto = new ProductCollectDto();
		}else{
			bb.setRes(-81003);
			bb.setMsg("You have favorites too");
			return bb;
		}
		pcdto.setClistingid(listingId);
		pcdto.setCemail(email);
		int res = dao.addProductCollectDto(pcdto);
		if(res > 0 ){
		   	bb.setRes(1);
	    }else{
		   	bb.setRes(-81004);
		  	bb.setMsg("product collect add failure");
		}
		return bb;
	}

	@Override
	public List<String> getCollectListingIdByEmail(String email) {
		return dao.getCollectByEmail(email);
	}
}