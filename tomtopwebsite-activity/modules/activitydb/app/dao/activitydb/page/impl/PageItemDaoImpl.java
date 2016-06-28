package dao.activitydb.page.impl;

import java.util.List;
import values.activity.page.PageItemQuery;
import mapper.activitydb.page.PageItemMapper;
import com.google.inject.Inject;

import dao.activitydb.page.IPageItemDao;
import entity.activity.page.PageItem;

/**
 * 页面子项实现类
 * 
 * @author liulj
 *
 */
public class PageItemDaoImpl implements IPageItemDao {

	@Inject
	PageItemMapper mapper;

	@Override
	public int getCount(Integer ienable, String url, Integer type) {
		return mapper.getCount(ienable, url, type);
	}

	@Override
	public List<PageItemQuery> getPage(int page, int pagesize, Integer ienable,
			String url, Integer type) {
		return mapper.getPage(page, pagesize, ienable, url, type);
	}

	@Override
	public int add(PageItem pageItem) {
		return mapper.insert(pageItem);
	}

	@Override
	public int deleteByIid(int iid) {
		return mapper.deleteByPrimaryKey(iid);
	}

	@Override
	public int updateByIid(PageItem pageItem) {
		return mapper.updateByPrimaryKey(pageItem);
	}

	@Override
	public List<PageItem> getAll() {
		return mapper.selectAll();
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPageItemByPageId</p>
	 * <p>Description: 通过活动id获取活动项目</p>
	 * @param iid
	 * @return
	 * @see dao.activitydb.page.IPageItemDao#getPageItemByPageId(java.lang.Integer)
	 */
	@Override
	public List<PageItem> getPageItemByPageId(Integer iid) {
		return mapper.getPageItemByPageId(iid);
	}

	@Override
	public PageItem getById(int id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

}
