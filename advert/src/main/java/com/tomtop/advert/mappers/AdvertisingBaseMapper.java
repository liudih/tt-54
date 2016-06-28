package com.tomtop.advert.mappers;

import java.util.List;



import com.tomtop.advert.models.Advertising;
import com.tomtop.advert.models.AdertContext;

public interface AdvertisingBaseMapper {

	/**
	 * 传入获取广告时需要的参数对象，输出广告对象集合
	 * 
	 * @param pac
	 *            参数对象
	 * @return 广告
	 */
	List<Advertising> getAdvertisingByContext(AdertContext pac);

}