package com.tomtop.advert.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tomtop.advert.models.AdertContext;
import com.tomtop.advert.models.AdvertItem;
import com.tomtop.advert.services.IAdvertisingService;
import com.tomtop.framework.core.utils.Result;

/**
 * 获取广告
 * 
 * @author xcf
 *
 */
@Controller
@RequestMapping("/ic/v1/adverts")
public class AdvertController {

	@Autowired
	IAdvertisingService advertservice;

	/**
	 * 获取单个广告
	 * 
	 * @param id
	 *            广告唯一ID
	 * @return AdvertItem 广告对象
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Result getAdvertById(@PathParam("id") Integer id) {

		// 获取广告
		AdertContext pac = new AdertContext(null, 3, 1, 1, 3, "web");

		List<AdvertItem> advertLists = advertservice.getAdvertsByContext(pac);

		return new Result(Result.SUCCESS, advertLists);
	}

	/**
	 * 根据相关条件获取广告
	 * 
	 * @param positonId
	 *            -位置ID 广告存在于界面的位置，例如： left,right 存储在 t_advertising_positon表中
	 * @param typeId
	 *            - 广告类型ID，如product(1) category(2) newarrivals (3) 0.99(4)
	 *            index(5) hot(6) freeshipping(7) 存储在t_advertising_type表中
	 * @param websiteId
	 *            -站点
	 * @param languageId
	 *            - 语言
	 * @param device
	 *            -设备： 如果，app/web/mobile
	 * @param businessId
	 *            业务ID (比如产品就传入 listingid)，无就不用传递
	 * @return AdvertItem 广告对象
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Result getAdvertsByContext(
			@RequestParam("positonId") Integer positonId,
			@RequestParam("typeId") Integer typeId,
			@RequestParam(value = "websiteId", required = false, defaultValue = "1") Integer websiteId,
			@RequestParam(value = "languageId", required = false, defaultValue = "1") Integer languageId,
			@RequestParam(value = "device", required = false, defaultValue = "web") String device,
			@RequestParam(value = "businessId", required = false) String businessId) {

		// 获取广告
		AdertContext pac = new AdertContext(businessId, typeId, websiteId,
				languageId, positonId, device);

		List<AdvertItem> advertLists = advertservice.getAdvertsByContext(pac);

		return new Result(Result.SUCCESS, advertLists);
	}

}
