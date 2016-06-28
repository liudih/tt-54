package com.tomtop.advert.dao;

import java.util.List;



import com.tomtop.advert.models.Advertising;
import com.tomtop.advert.models.AdertContext;

public interface IAdvertisingDao {
	
	List<Advertising> getAdvertsByContext(AdertContext context);

}
