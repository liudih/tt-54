package com.tomtop.product.services;

import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.search.entiry.LangageBase;

/**
 * 基础信息
 * 
 * @author liulj
 *
 */
public interface IBaseInfoService {
	public LangageBase getlangageBase(Integer langid);

	public CurrencyBo getCurrencyRate(String currency);
}
