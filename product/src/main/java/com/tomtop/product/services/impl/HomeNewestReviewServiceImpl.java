package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeNewestReviewDao;
import com.tomtop.product.models.bo.HomeNewestReviewBo;
import com.tomtop.product.services.IHomeNewestReviewService;

/**
 * 首页评论分享
 * 
 * @author liulj
 *
 */
@Service("homeNewestReviewService")
public class HomeNewestReviewServiceImpl implements IHomeNewestReviewService {

	@Resource(name = "homeNewestReviewDao")
	private IHomeNewestReviewDao dao;

	@Override
	public List<HomeNewestReviewBo> getListByClientLang(int client, int lang) {
		return Lists.newArrayList(Lists.newArrayList(Lists.transform(
				dao.getListByClientLang(client, lang),
				p -> BeanUtils.mapFromClass(p, HomeNewestReviewBo.class))));
	}

}