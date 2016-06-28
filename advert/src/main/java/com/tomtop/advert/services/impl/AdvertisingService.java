package com.tomtop.advert.services.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.advert.dao.IAdvertisingDao;
import com.tomtop.advert.models.AdvertItem;
import com.tomtop.advert.models.Advertising;
import com.tomtop.advert.models.AdertContext;
import com.tomtop.advert.services.IAdvertisingService;

@Service
public class AdvertisingService implements IAdvertisingService {

	final static int PAGE_SIZE = 10;

	@Autowired
	IAdvertisingDao advertDao;
  

	public List<Advertising> getAdvertisingByContext(
			AdertContext context) {
		return this.getAdvertisingByContext(context.getBusinessId(),
				context.getAdvertisingType(), context.getWebsiteId(),
				context.getLanguageId(), context.getPositonId(),
				context.getDevice());
	}

	private List<Advertising> getAdvertisingByContext(String businessId,
			Integer advertisingType, Integer websiteId, Integer languageId,
			Integer positonId, String device) {
		AdertContext pcontext = new AdertContext(
				businessId, advertisingType, websiteId, languageId, positonId,
				device);
		List<Advertising> advertisingBaseList = advertDao
				.getAdvertsByContext(pcontext);
		return advertisingBaseList;
	}
	
	

	private List<AdvertItem> convertValue(List<Advertising> advertisingList) {
		if (null == advertisingList) {
			return null;
		}
		return Lists.transform(advertisingList, ab -> {

			String hrefUrl = ab.getChrefurl();
			String title = ab.getCtitle();
			String imageUrl = ab.getCimageurl();
			// 需要判断是图片库里面的图片，添加前缀，否则还是直接写的路径
				if (imageUrl.startsWith("advertising/image")) {
					imageUrl = "/img/" + imageUrl;
				}

				String advertisingStr = "<a href=\"" + hrefUrl
						+ "\"><img title=\"" + title + "\" src=\"" + imageUrl
						+ "\"></a>";
				return new AdvertItem(title, imageUrl, hrefUrl, advertisingStr);
				// return advertisingStr;
			});
	}

	@Override
	public List<AdvertItem> getAdvertsByContext(AdertContext context) {
		List<Advertising> advertisingList = this.getAdvertisingByContext(context);
		
		return this.convertValue(advertisingList);
	}
 
}
