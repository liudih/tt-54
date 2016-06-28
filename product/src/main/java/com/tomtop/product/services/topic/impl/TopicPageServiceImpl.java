package com.tomtop.product.services.topic.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.product.mappers.product.topic.TopicPageMapper;
import com.tomtop.product.models.bo.TopicPageBo;
import com.tomtop.product.models.dto.TopicPageDto;
import com.tomtop.product.services.topic.ITopicPageService;
import com.tomtop.product.utils.DateFormatUtils;

@Service
public class TopicPageServiceImpl implements ITopicPageService {

	@Autowired
	TopicPageMapper topicPageMapper;
	
	/**
	 * 获取Hot Events
	 * 
	 * @param type 
	 * @param siteId 站点
	 * @param langId 语言ID
	 * @param year 年
	 * @param month 月
	 * @param size 获取数量
	 * 
	 * @return List<TopicPageBo>
	 */
	@Cacheable(value = "product_topic", keyGenerator = "customKeyGenerator")
	@Override
	public List<TopicPageBo> filterTopicPage(String type, Integer siteID,
			Integer languageId, Integer year, Integer month, Integer size) {
		List<TopicPageBo> tpboList = new ArrayList<TopicPageBo>();
		Map<String, Date> timeInterval = getTimeInterval(year, month);
		List<TopicPageDto> tpdtoList = topicPageMapper.getTopicPageDto(type, languageId,
				timeInterval.get("start"), timeInterval.get("end"), siteID,
				size);
		if(tpdtoList != null && tpdtoList.size() > 0){
			TopicPageBo tpbo = null;
			for (int i = 0; i < tpdtoList.size(); i++) {
				tpbo = new TopicPageBo();
				tpbo.setId(tpdtoList.get(i).getIid());
				tpbo.setType(tpdtoList.get(i).getCtype());
				tpbo.setTitle(tpdtoList.get(i).getCtitle());
				tpbo.setHtmlUrl(tpdtoList.get(i).getChtmlurl());
				tpboList.add(tpbo);
			}
		}else{
			if(languageId != 1){
				tpdtoList = topicPageMapper.getTopicPageDto(type, 1,
						timeInterval.get("start"), timeInterval.get("end"), siteID,
						size);
				TopicPageBo tpbo = null;
				for (int i = 0; i < tpdtoList.size(); i++) {
					tpbo = new TopicPageBo();
					tpbo.setId(tpdtoList.get(i).getIid());
					tpbo.setType(tpdtoList.get(i).getCtype());
					tpbo.setTitle(tpdtoList.get(i).getCtitle());
					tpbo.setHtmlUrl(tpdtoList.get(i).getChtmlurl());
					tpboList.add(tpbo);
				}
			}
		}
		
		return tpboList;
	}
	
	private Map<String, Date> getTimeInterval(Integer year, Integer month) {
		Map<String, Date> timeInterval = Maps.newHashMap();
		if (null != year && null == month) {
			Date startDate = DateFormatUtils.getFormatDateByStr(year + "-1-1");
			timeInterval.put("start", startDate);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(startDate);
			gc.add(Calendar.YEAR, 1);
			timeInterval.put("end", gc.getTime());
		} else if (null != year && null != month) {
			Date startDate = DateFormatUtils.getFormatDateByStr(year + "-"
					+ month + "-1");
			timeInterval.put("start", startDate);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(startDate);
			gc.add(Calendar.MONTH, 1);
			timeInterval.put("end", gc.getTime());
		}
		return timeInterval;
	}
}
