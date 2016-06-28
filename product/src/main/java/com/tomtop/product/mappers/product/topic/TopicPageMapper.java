package com.tomtop.product.mappers.product.topic;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tomtop.product.models.dto.TopicPageDto;

public interface TopicPageMapper {


	List<TopicPageDto> getTopicPageDto(@Param("type") String type,
			@Param("languageId") Integer languageId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("siteId") Integer siteId, @Param("size") Integer size);
}
