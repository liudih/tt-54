package services.activity.page.impl;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.ExecutorType;
import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import play.Logger.ALogger;
import services.activity.page.IPageService;
import valueobject.activity.page.PageTitle;
import values.activity.page.PageQuery;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.activitydb.page.IPageDao;
import dao.activitydb.page.IPageTitleDao;
import entity.activity.page.Page;
import forms.activity.page.PageForm;

public class PageServiceImpl implements IPageService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	private IPageDao pageDao;

	@Inject
	private IPageTitleDao pageTitleDao;

	@Override
	public valueobjects.base.Page<PageForm> getPage(int page, int pageSize,
			PageForm pageForm) {
		// TODO Auto-generated method stub
		List<PageQuery> pages = pageDao.getPage(page, pageSize,
				pageForm.getIenable(), pageForm.getCurl(), pageForm.getItype());
		List<PageForm> despages = Lists.transform(pages,
				new Function<PageQuery, PageForm>() {
					@Override
					public PageForm apply(PageQuery arg0) {
						// TODO Auto-generated method stub
						PageForm dest = new PageForm();
						try {
							BeanUtils.copyProperties(dest, arg0);
						} catch (Exception e) {
							logger.error("化对象失败" + e.getMessage());
						}
						return dest;
					}
				});
		int total = pageDao.getCount(pageForm.getIenable(), pageForm.getCurl(),
				pageForm.getItype());
		return new valueobjects.base.Page<PageForm>(despages, total, page,
				pageSize);
	}

	@Transactional(executorType = ExecutorType.BATCH)
	@Override
	public int insertInfo(PageForm pageForm) {
		// TODO Auto-generated method stub
		Page Page = getFromForm(pageForm);
		int rownum = pageDao.add(Page);
		if (rownum > 0) {
			for (PageTitle title : pageForm.getLangs()) {
				title.setIpageid(Page.getIid());
				entity.activity.page.PageTitle dest = new entity.activity.page.PageTitle();
				try {
					BeanUtils.copyProperties(dest, title);
					pageTitleDao.insert(dest);
				} catch (Exception e) {
					logger.error("化对象失败" + e.getMessage());
				}
			}
		}
		return rownum;
	}

	/**
	 * 把form对象转成entity对象
	 * 
	 * @param pageForm
	 * @return
	 */
	public Page getFromForm(PageForm pageForm) {
		Page page = new Page();
		page.setIid(pageForm.getIid());
		page.setCbannerurl(pageForm.getCbannerurl());
		page.setCurl(pageForm.getCurl());
		page.setIenable(pageForm.getIenable());
		page.setDenableenddate(pageForm.getDenableenddate());
		page.setDenablestartdate(pageForm.getDenablestartdate());
		page.setCcreateuser(pageForm.getCcreateuser());
		page.setDcreatedate(pageForm.getDcreatedate());
		page.setCupdateuser(pageForm.getCupdateuser());
		page.setDupdatedate(pageForm.getDupdatedate());
		page.setIwebsiteid(pageForm.getIwebsiteid());
		page.setItype(pageForm.getItype());
		page.setCrecommendvalues(pageForm.getCrecommendvalues());
		page.setItemplateid(pageForm.getItemplateid());
		return page;
	}

	@Transactional(executorType = ExecutorType.BATCH)
	@Override
	public int updateInfo(PageForm pageForm) {
		// TODO Auto-generated method stub
		Page page = getFromForm(pageForm);
		int rownum = pageDao.updateByIid(page);
		if (rownum > 0) {
			for (PageTitle title : pageForm.getLangs()) {
				title.setIpageid(page.getIid());
				entity.activity.page.PageTitle dest = new entity.activity.page.PageTitle();
				try {
					BeanUtils.copyProperties(dest, title);
					if (title.getIid() == null) {
						pageTitleDao.insert(dest);
					} else {
						pageTitleDao.update(dest);
					}
				} catch (Exception e) {
					logger.error("化对象失败" + e.getMessage());
				}
			}
		}
		return rownum;
	}

	@Override
	public int deleteByID(int id) {
		return pageDao.deleteByIid(id);
	}

	public valueobject.activity.page.Page getById(int id) {
		// TODO Auto-generated method stub
		Page page = pageDao.getById(id);
		valueobject.activity.page.Page dest = new valueobject.activity.page.Page();
		try {
			BeanUtils.copyProperties(dest, page);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
		}
		return dest;
	}

	@Override
	public List<valueobject.activity.page.Page> getAll() {
		// TODO Auto-generated method stub
		List<Page> list = pageDao.getAll();
		if (list != null && list.size() > 0) {
			List<valueobject.activity.page.Page> deslist = Lists.transform(
					list, new Function<Page, valueobject.activity.page.Page>() {
						@Override
						public valueobject.activity.page.Page apply(Page arg0) {
							// TODO Auto-generated method stub
							valueobject.activity.page.Page dest = new valueobject.activity.page.Page();
							try {
								BeanUtils.copyProperties(dest, arg0);
							} catch (Exception e) {
								logger.error("化对象失败" + e.getMessage());
							}
							return dest;
						}
					});
			return deslist;
		} else {
			return Lists.newArrayList();
		}
	}

	@Override
	public int validateUrl(String url) {
		// TODO Auto-generated method stub
		return pageDao.validateUrl(url);
	}

}
