package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.BaseLayoutBo;

public interface IBaseLayoutService {
	/**
	 * 根具标识获取list
	 * 
	 * @param code
	 * @param client
	 * @param lang
	 * @return
	 */
	List<BaseLayoutBo> getListByCode(String code, int client, int lang);
}