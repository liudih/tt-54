package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.interaction.mappers.ProductCollectMapper;
import com.tomtop.product.dao.IProductCollectDao;
import com.tomtop.product.models.dto.CollectCountDto;
import com.tomtop.product.models.dto.ProductCollectDto;

/**
 * 产品收藏
 * 
 * @author liulj
 *
 */
@Repository("productCollectDao")
public class ProductCollectDaoImpl implements IProductCollectDao {

	@Autowired
	private ProductCollectMapper mapper;

	@Override
	public List<CollectCountDto> getCollectCountByListingIds(
			List<String> listingIds) {
		return mapper.getCollectCountByListingIds(listingIds);
	}

	@Override
	public CollectCountDto getCollectCountByListingId(String listingIds) {
		return mapper.getCollectCountByListingId(listingIds);
	}

	@Override
	public Integer addProductCollectDto(ProductCollectDto pcdto) {
		return mapper.insertSelective(pcdto);
	}

	@Override
	public ProductCollectDto getProductCollectDtoByEmailAndListingId(
			String listingId, String email) {
		return mapper.getCollectByMember(listingId, email);
	}

	@Override
	public List<String> getCollectByEmail(String email) {
		return mapper.getCollectListingIDByEmail(email);
	}
}