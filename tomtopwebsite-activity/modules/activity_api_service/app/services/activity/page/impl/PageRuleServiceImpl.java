package services.activity.page.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.Logger.ALogger;
import services.activity.page.IPageRuleService;
import valueobject.activity.page.PageRule;
import values.activity.page.PageRuleQuery;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.activitydb.page.IPagePrizeDao;
import dao.activitydb.page.IPageRuleDao;
import forms.activity.page.PageRuleForm;

public class PageRuleServiceImpl implements IPageRuleService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	private IPageRuleDao dao;

	@Inject
	private IPagePrizeDao prizedao;

	@Override
	public valueobjects.base.Page<PageRuleForm> getPage(int page, int pageSize,
			PageRuleForm pageForm) {
		// TODO Auto-generated method stub
		List<PageRuleQuery> pages = dao.getPage(page, pageSize,
				pageForm.getCurl());
		List<PageRuleForm> despages = Lists.transform(pages,
				new Function<PageRuleQuery, PageRuleForm>() {
					@Override
					public PageRuleForm apply(PageRuleQuery arg0) {
						// TODO Auto-generated method stub
						PageRuleForm dest = new PageRuleForm();
						try {
							BeanUtils.copyProperties(dest, arg0);
						} catch (Exception e) {
							logger.error("化对象失败" + e.getMessage());
						}
						return dest;
					}
				});
		int total = dao.getCount(pageForm.getCurl());
		return new valueobjects.base.Page<PageRuleForm>(despages, total, page,
				pageSize);
	}

	@Override
	public PageRule getById(Integer id) {
		// TODO Auto-generated method stub
		entity.activity.page.PageRule rule = dao.getById(id);
		PageRule dest = new valueobject.activity.page.PageRule();
		try {
			BeanUtils.copyProperties(dest, rule);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
		}
		return dest;
	}

	@Override
	public int add(PageRule rule) {
		// TODO Auto-generated method stub
		entity.activity.page.PageRule dest = new entity.activity.page.PageRule();
		try {
			BeanUtils.copyProperties(dest, rule);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("化对象失败" + e.getMessage());
			return 0;
		}
		return dao.add(dest);
	}

	@Override
	public int updateById(PageRule rule) {
		// TODO Auto-generated method stub
		entity.activity.page.PageRule dest = new entity.activity.page.PageRule();
		try {
			BeanUtils.copyProperties(dest, rule);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("化对象失败" + e.getMessage());
			return 0;
		}
		return dao.updateByIid(dest);
	}

	@Override
	public int deleteByid(int iid) {
		// TODO Auto-generated method stub
		int row = dao.deleteByid(iid);
		if (row > 0) {
			prizedao.deleteByRuleid(iid);
		}
		return row;
	}

	@Override
	public int getCountByPageid(Integer pageid) {
		// TODO Auto-generated method stub
		return dao.getCountByPageid(pageid);
	}

	@Override
	public List<PageRule> getListByPageid(int pageid) {
		// TODO Auto-generated method stub
		List<entity.activity.page.PageRule> list = dao.getListByPageid(pageid);
		List<PageRule> destlist = Lists.newArrayList();
		if (list.size() > 0) {
			destlist = Lists.transform(list, p -> {
				PageRule dest = new PageRule();
				try {
					BeanUtils.copyProperties(dest, p);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("化对象失败" + e.getMessage());
				}
				return dest;
			});
		}
		return destlist;
	}
}
