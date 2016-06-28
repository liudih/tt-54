package services.activity.page.impl;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.Logger.ALogger;
import services.activity.page.IPageTitleService;
import valueobject.activity.page.PageTitle;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.activitydb.page.IPageTitleDao;

public class PageTitleServiceImpl implements IPageTitleService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	IPageTitleDao dao;

	@Override
	public PageTitle getById(int id) {
		entity.activity.page.PageTitle title = new entity.activity.page.PageTitle();
		PageTitle dest = new PageTitle();
		try {
			BeanUtils.copyProperties(dest, title);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
		}
		return dest;
	}

	@Override
	public List<PageTitle> getListByPageid(Integer pageid) {
		List<entity.activity.page.PageTitle> list = dao.getListByPageid(pageid);
		List<PageTitle> desList = Lists.transform(list,
				new Function<entity.activity.page.PageTitle, PageTitle>() {

					@Override
					public PageTitle apply(entity.activity.page.PageTitle arg0) {
						PageTitle dest = new PageTitle();
						try {
							BeanUtils.copyProperties(dest, arg0);
						} catch (Exception e) {
							logger.error("化对象失败" + e.getMessage());
						}
						return dest;
					}

				});
		return desList;
	}

	@Override
	public int insert(PageTitle pageTitle) {
		entity.activity.page.PageTitle dest = new entity.activity.page.PageTitle();
		try {
			BeanUtils.copyProperties(dest, pageTitle);
			dao.insert(dest);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
		}
		return 0;
	}

	@Override
	public int update(PageTitle pageTitle) {
		entity.activity.page.PageTitle dest = new entity.activity.page.PageTitle();
		try {
			BeanUtils.copyProperties(dest, pageTitle);
			dao.update(dest);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
		}
		return 0;
	}

	@Override
	public int deleteByID(int id) {
		return dao.deleteByID(id);
	}

	@Override
	public PageTitle getPTByPageIdAndLId(Integer iid, int languageId) {
		entity.activity.page.PageTitle pageTitle = dao.getPTByPageIdAndLId(iid,
				languageId);
		PageTitle title = new PageTitle();
		try {
			BeanUtils.copyProperties(title, pageTitle);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
		}
		return title;
	}

}