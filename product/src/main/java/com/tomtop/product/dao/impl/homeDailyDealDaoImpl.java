package com.tomtop.product.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeDailyDealDao;
import com.tomtop.product.mappers.mysql.HomeDailyDealMapper;
import com.tomtop.product.models.dto.HomeDailyDealDto;

@Repository("homeDailyDealDao")
public class homeDailyDealDaoImpl implements IHomeDailyDealDao {

	@Autowired
	private HomeDailyDealMapper mapper;

	@Override
	public List<HomeDailyDealDto> getListByStartDate(Date startDate,
			int client, int language) {
		List<HomeDailyDealDto> dto = mapper.getListByStartDate(startDate,
				client, language);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByStartDate(startDate, client, 1);
		} else {
			return dto;
		}
	}

}
