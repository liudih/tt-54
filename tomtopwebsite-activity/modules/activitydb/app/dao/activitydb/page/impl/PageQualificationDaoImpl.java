package dao.activitydb.page.impl;

import java.util.List;

import mapper.activitydb.page.PageQualificationMapper;
import values.activity.page.PageQualificationQuery;

import com.google.inject.Inject;

import dao.activitydb.page.IPageQualificationDao;
import entity.activity.page.PageQualification;

/**
 * 页面page dao 实现类
 * 
 * @author liulj
 *
 */
public class PageQualificationDaoImpl implements IPageQualificationDao {

	@Inject
	PageQualificationMapper mapper;

	@Override
	public int getCount(String url) {
		// TODO Auto-generated method stub
		return mapper.getCount(url);
	}

	@Override
	public List<PageQualificationQuery> getPage(int page, int pagesize,
			String url) {
		// TODO Auto-generated method stub
		return mapper.getPage(page, pagesize, url);
	}

	@Override
	public int add(PageQualification qualification) {
		// TODO Auto-generated method stub
		return mapper.insert(qualification);
	}

	@Override
	public int deleteByIid(int iid) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(iid);
	}

	@Override
	public int updateByIid(PageQualification PageQualification) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(PageQualification);
	}

	@Override
	public PageQualification getById(Integer id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPQByPageId</p>
	 * <p>Description: 通过活动id获取筛选条件</p>
	 * @param pageId
	 * @return
	 * @see dao.activitydb.page.IPageQualificationDao#getPQByPageId(int)
	 */
	@Override
	public List<PageQualification> getPQByPageId(int pageId) {
		int enable = 1;
		return mapper.getPQByPageId(pageId,enable);
	}
}
