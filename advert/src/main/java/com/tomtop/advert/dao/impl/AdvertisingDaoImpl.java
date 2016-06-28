package com.tomtop.advert.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.advert.dao.IAdvertisingDao;
import com.tomtop.advert.mappers.AdvertisingBaseMapper;
import com.tomtop.advert.models.Advertising;
import com.tomtop.advert.models.AdertContext;

@Repository
public class AdvertisingDaoImpl implements IAdvertisingDao {

	@Autowired
	AdvertisingBaseMapper mapper;

	@Override
	public List<Advertising> getAdvertsByContext(
			AdertContext context) {

		return mapper.getAdvertisingByContext(context);
	}

}
