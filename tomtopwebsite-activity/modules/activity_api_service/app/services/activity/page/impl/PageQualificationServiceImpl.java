package services.activity.page.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.Logger.ALogger;
import services.activity.page.IPageQualificationService;
import valueobject.activity.page.PageQualification;
import values.activity.page.PageQualificationQuery;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.activitydb.page.IPageQualificationDao;
import forms.activity.page.PageQualificationForm;

public class PageQualificationServiceImpl implements IPageQualificationService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	private IPageQualificationDao dao;

	@Override
	public valueobjects.base.Page<PageQualificationForm> getPage(int page,
			int pageSize, PageQualificationForm pageForm) {
		// TODO Auto-generated method stub
		List<PageQualificationQuery> pages = dao.getPage(page, pageSize,
				pageForm.getCurl());
		List<PageQualificationForm> despages = Lists.transform(pages,
				new Function<PageQualificationQuery, PageQualificationForm>() {
					@Override
					public PageQualificationForm apply(
							PageQualificationQuery arg0) {
						// TODO Auto-generated method stub
						PageQualificationForm dest = new PageQualificationForm();
						try {
							BeanUtils.copyProperties(dest, arg0);
						} catch (Exception e) {
							logger.error("化对象失败" + e.getMessage());
						}
						return dest;
					}
				});
		int total = dao.getCount(pageForm.getCurl());
		return new valueobjects.base.Page<PageQualificationForm>(despages,
				total, page, pageSize);
	}

	@Override
	public PageQualification getById(Integer id) {
		// TODO Auto-generated method stub
		entity.activity.page.PageQualification qualification = dao.getById(id);
		PageQualification dest = new valueobject.activity.page.PageQualification();
		try {
			BeanUtils.copyProperties(dest, qualification);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
		}
		return dest;
	}

	@Override
	public int add(PageQualification qualification) {
		// TODO Auto-generated method stub
		entity.activity.page.PageQualification dest = new entity.activity.page.PageQualification();
		try {
			BeanUtils.copyProperties(dest, qualification);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("化对象失败" + e.getMessage());
			return 0;
		}
		return dao.add(dest);
	}

	@Override
	public int deleteById(int iid) {
		// TODO Auto-generated method stub
		return dao.deleteByIid(iid);
	}

	@Override
	public int updateById(PageQualification qualification) {
		// TODO Auto-generated method stub
		entity.activity.page.PageQualification dest = new entity.activity.page.PageQualification();
		try {
			BeanUtils.copyProperties(dest, qualification);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("化对象失败" + e.getMessage());
			return 0;
		}
		return dao.updateByIid(dest);
	}
}
