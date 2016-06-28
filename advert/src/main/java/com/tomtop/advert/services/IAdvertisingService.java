package com.tomtop.advert.services;

import java.util.List;

import com.tomtop.advert.models.AdvertItem;
import com.tomtop.advert.models.AdertContext;

public interface IAdvertisingService {
	
	List<AdvertItem> getAdvertsByContext(AdertContext context);

}
