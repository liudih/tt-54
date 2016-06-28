package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.BaseLayoutDto;

public interface IBaseLayoutDao {
	/**
	 * 根具标识获取list
	 * 
	 * @param code
	 * @param client
	 * @param lang
	 * @return
	 */
	List<BaseLayoutDto> getListByCode(String code, int client, int lang);
}