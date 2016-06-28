package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IProductCategoryMapperDao;
import com.tomtop.product.mappers.product.ProductCategoryMapperMapper;

/**
 * 产品类目关系
 * 
 * @author liulj
 *
 */
@Repository("productCategoryMapperDao")
public class ProductCategoryMapperDaoImpl implements IProductCategoryMapperDao {

	@Autowired
	private ProductCategoryMapperMapper mapper;

	@Override
	public List<Integer> getListingCategoryParentIdByListingId(String listingid) {
		// TODO Auto-generated method stub
		return mapper.getListingCategoryParentIdByListingId(listingid);
	}

	@Override
	public List<String> getAllListingIdsByRootIds(List<Integer> categoryIds) {
		// TODO Auto-generated method stub
		return mapper.getAllListingIdsByRootIds(categoryIds);
	}

	@Override
	public List<String> getListingIdsByCategoryId(List<Integer> rootCategoryId,
			Integer pageSize, Integer pageNum, int client, int status,
			List<Integer> attrIds) {
		// TODO Auto-generated method stub
		return mapper.getPageListingIdsByCategoryId(rootCategoryId, pageSize,
				pageNum, client, status, attrIds);
	}

	@Override
	public int getListingIdCountByCategoryId(List<Integer> rootCategoryId,
			int client, int status, List<Integer> attrIds) {
		// TODO Auto-generated method stub
		return mapper.getListingIdCountByCategoryId(rootCategoryId, client,
				status, attrIds);
	}

	@Override
	public List<String> getListingIdsByCategoryId(List<Integer> rootCategoryId,
			int client, int status) {
		// TODO Auto-generated method stub
		return mapper.getListingIdsByCategoryId(rootCategoryId, client, status);
	}

	@Override
	public List<String> getAllListingIdsByRootIds(
			List<Integer> rootCategoryIds, int client, int status) {
		// TODO Auto-generated method stub
		return mapper.getAllListingIdsByRootIdsClientStatus(rootCategoryIds,
				client, status);
	}
}
