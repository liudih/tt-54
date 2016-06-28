package com.tomtop.product.services.topic;

import java.util.List;

import com.tomtop.product.models.bo.TopicPageBo;

public interface ITopicPageService {

	public List<TopicPageBo> filterTopicPage(String type, Integer siteID,
			Integer languageId, Integer year, Integer month, Integer size);
}
