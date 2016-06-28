package dao.activitydb.page.impl;

import java.util.Date;
import java.util.List;

import values.activity.page.PageQuery;
import mapper.activitydb.page.PageMapper;

import com.google.inject.Inject;

import dao.activitydb.page.IPageDao;
import dto.image.Img;
import dto.member.MemberBase;
import dto.order.Order;
import entity.activity.page.Page;

/**
 * 页面page dao 实现类
 * 
 * @author liulj
 *
 */
public class PageDaoImpl implements IPageDao {

	@Inject
	PageMapper mapper;

	@Override
	public int getCount(Integer ienable, String url, Integer type) {
		// TODO Auto-generated method stub
		return mapper.getCount(ienable, url, type);
	}

	@Override
	public List<PageQuery> getPage(int page, int pagesize, Integer ienable,
			String url, Integer type) {
		// TODO Auto-generated method stub
		return mapper.getPage(page, pagesize, ienable, url, type);
	}

	@Override
	public int add(Page page) {
		// TODO Auto-generated method stub
		return mapper.insert(page);
	}

	@Override
	public int deleteByIid(int iid) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(iid);
	}

	@Override
	public int updateByIid(Page page) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(page);
	}

	@Override
	public List<Page> getAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public Page getById(Integer id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int validateUrl(String url) {
		// TODO Auto-generated method stub
		return mapper.validateUrl(url);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPageByUrl</p>
	 * <p>Description: 通过url查询活动页面</p>
	 * @param title
	 * @param websiteId
	 * @return
	 * @see dao.activitydb.page.IPageDao#getPageByUrl(java.lang.String, int)
	 */
	@Override
	public Page getPageByUrl(String title, int websiteId) {
		return mapper.getPageByUrl(title, websiteId);
	}

}
