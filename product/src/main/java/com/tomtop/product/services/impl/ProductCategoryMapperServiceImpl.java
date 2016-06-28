package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tomtop.product.dao.IProductCategoryMapperDao;
import com.tomtop.product.services.IProductCategoryMapperService;

/**
 * 产品类目关系
 * 
 * @author liulj
 *
 */
@Service("productCategoryMapperService")
public class ProductCategoryMapperServiceImpl implements
		IProductCategoryMapperService {

	@Resource(name = "productCategoryMapperDao")
	private IProductCategoryMapperDao dao;

	@Override
	public List<Integer> getListingCategoryParentIdByListingId(String listingid) {
		// TODO Auto-generated method stub
		return dao.getListingCategoryParentIdByListingId(listingid);
	}

	@Override
	public List<String> getAllListingIdsByRootIds(List<Integer> categoryIds) {
		// TODO Auto-generated method stub
		return dao.getAllListingIdsByRootIds(categoryIds);
	}

	@Override
	public List<String> getListingIdsByCategoryId(List<Integer> rootCategoryId,
			Integer pageSize, Integer pageNum, int client, int status,
			List<Integer> attrIds) {
		// TODO Auto-generated method stub
		return dao.getListingIdsByCategoryId(rootCategoryId, pageSize, pageNum,
				client, status, attrIds);
	}

	@Override
	public int getListingIdCountByCategoryId(List<Integer> rootCategoryId,
			int client, int status, List<Integer> attrIds) {
		// TODO Auto-generated method stub
		return dao.getListingIdCountByCategoryId(rootCategoryId, client,
				status, attrIds);
	}

	@Override
	public List<String> getListingIdsByCategoryId(List<Integer> rootCategoryId,
			int client, int status) {
		// TODO Auto-generated method stub
		return dao.getListingIdsByCategoryId(rootCategoryId, client, status);
	}

	@Override
	public List<String> getAllListingIdsByRootIds(
			List<Integer> rootCategoryIds, int client, int status) {
		// TODO Auto-generated method stub
		return dao.getAllListingIdsByRootIds(rootCategoryIds, client, status);
	}
}
