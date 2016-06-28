package services.activity.page.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.ExecutorType;
import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import play.Logger.ALogger;
import services.activity.page.IPageItemService;
import valueobject.activity.page.PageItem;
import valueobject.activity.page.PageItemName;
import valueobject.activity.page.VoteRecord;
import valueobjects.base.Page;
import values.activity.page.PageItemQuery;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.activitydb.page.IPageItemDao;
import dao.activitydb.page.IPageItemNameDao;
import dao.activitydb.page.IVoteRecordDao;
import forms.activity.page.PageItemForm;

public class PageItemServiceImpl implements IPageItemService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	IPageItemDao dao;

	@Inject
	IPageItemNameDao nameDao;

	@Override
	public Page<PageItemForm> getPage(int pageitem, int pagesize,
			PageItemForm itemForm) {
		List<PageItemQuery> pages = dao.getPage(pageitem, pagesize,
				itemForm.getIenable(), itemForm.getCurl(), itemForm.getItype());
		List<PageItemForm> despages = Lists.transform(pages,
				new Function<PageItemQuery, PageItemForm>() {
					@Override
					public PageItemForm apply(PageItemQuery arg0) {
						PageItemForm dest = new PageItemForm();
						try {
							BeanUtils.copyProperties(dest, arg0);
						} catch (Exception e) {
							logger.error("化对象失败" + e.getMessage());
						}
						return dest;
					}
				});
		int total = dao.getCount(itemForm.getIenable(), itemForm.getCurl(),
				itemForm.getItype());
		return new Page<PageItemForm>(despages, total, pageitem, pagesize);
	}

	@Transactional(executorType = ExecutorType.BATCH)
	@Override
	public int insertInfo(PageItemForm itemForm) {
		PageItem pageItem = getFromForm(itemForm);
		entity.activity.page.PageItem dest = new entity.activity.page.PageItem();
		try {
			BeanUtils.copyProperties(dest, pageItem);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
			return 0;
		}
		int rownum = dao.add(dest);
		if (rownum > 0) {
			for (PageItemName title : itemForm.getLangs()) {
				title.setIpageitemid(dest.getIid());
				entity.activity.page.PageItemName desName = new entity.activity.page.PageItemName();
				try {
					BeanUtils.copyProperties(desName, title);
					nameDao.insert(desName);
				} catch (Exception e) {
					logger.error("化对象失败或插入主页名称失败" + e.getMessage());
				}
			}
		}
		return rownum;
	}

	/**
	 * 把form对象转成entity对象
	 * 
	 * @param itemForm
	 * @return
	 */
	public PageItem getFromForm(PageItemForm itemForm) {
		PageItem pageitem = new PageItem();
		pageitem.setIid(itemForm.getIid());
		pageitem.setCimgtargeturl(itemForm.getCimgtargeturl());
		pageitem.setCimgurl(itemForm.getCimgurl());
		pageitem.setCvalue(itemForm.getCvalue());
		pageitem.setIpageid(itemForm.getIpageid());
		pageitem.setIpriority(itemForm.getIpriority());
		return pageitem;
	}

	@Transactional(executorType = ExecutorType.BATCH)
	@Override
	public int updateInfo(PageItemForm itemForm) {
		PageItem pageitem = getFromForm(itemForm);
		entity.activity.page.PageItem dest = new entity.activity.page.PageItem();
		try {
			BeanUtils.copyProperties(dest, pageitem);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
			return 0;
		}
		int rownum = dao.updateByIid(dest);
		if (rownum > 0) {
			for (PageItemName title : itemForm.getLangs()) {
				title.setIpageitemid(pageitem.getIid());
				entity.activity.page.PageItemName desName = new entity.activity.page.PageItemName();
				try {
					BeanUtils.copyProperties(desName, title);
					if (title.getIid() == null) {
						nameDao.insert(desName);
					} else {
						nameDao.update(desName);
					}
				} catch (Exception e) {
					logger.error("转化对象或插入更新主页名称失败," + e.getMessage());
				}
			}
		}
		return rownum;
	}

	@Override
	public int deleteByIid(int iid) {
		return dao.deleteByIid(iid);
	}

	@Override

	public List<PageItem> getPageItems() {
		List<entity.activity.page.PageItem> pageItems = dao.getAll();

		List<PageItem> pageitItems = Lists.transform(pageItems,
				new Function<entity.activity.page.PageItem, PageItem>() {

					@Override
					public PageItem apply(entity.activity.page.PageItem pageItem) {
						PageItem item = new PageItem();
						try {
							BeanUtils.copyProperties(item, pageItem);
						} catch (Exception e) {
							logger.error("转化对象失败" + e.getMessage());
						}
						return item;
					}
				});
		return pageitItems;
	}

	public PageItem getById(int id) {
		// TODO Auto-generated method stub
		PageItem dest = new PageItem();
		try {
			BeanUtils.copyProperties(dest, dao.getById(id));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("转化对象失败," + e.getMessage());
		}
		return dest;

	}

}
