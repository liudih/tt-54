package com.tomtop.product.services;

import java.util.HashMap;
import java.util.List;

import com.tomtop.product.models.vo.BaseLayoutmoduleContenthProductVo;

public interface ILayoutService {
	public HashMap<String,List<BaseLayoutmoduleContenthProductVo>> getBaseLayoutmoduleContenth(Integer lang,Integer client,String layoutcode,String currency);
}
