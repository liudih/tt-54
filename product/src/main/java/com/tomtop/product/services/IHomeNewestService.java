package com.tomtop.product.services;

import com.tomtop.product.models.vo.HomeNewestVo;

public interface IHomeNewestService {

	HomeNewestVo getCustomersVoices(Integer client,Integer lang);

}
