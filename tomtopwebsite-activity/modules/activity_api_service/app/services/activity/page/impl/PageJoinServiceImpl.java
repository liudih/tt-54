package services.activity.page.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.Logger.ALogger;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import dao.activitydb.page.IPageJoinDao;
import forms.activity.page.PageJoinForm;
import services.activity.page.IPageJoinService;
import values.activity.page.PageJoinQuery;

public class PageJoinServiceImpl implements IPageJoinService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	private IPageJoinDao iPageJoinDao;

	@Override
	public Integer getPageJoinsBycjoinerAndIpageId(String cjoiner,
			Integer ipageid) {
		Integer pageJoins = iPageJoinDao.getPageJoinsBycjoinerAndIpageId(
				cjoiner, ipageid);
		return pageJoins;
	}

}
