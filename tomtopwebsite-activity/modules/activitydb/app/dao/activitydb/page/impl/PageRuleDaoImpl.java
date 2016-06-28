package dao.activitydb.page.impl;

import java.util.List;

import mapper.activitydb.page.PageRuleMapper;
import values.activity.page.PageRuleQuery;

import com.google.inject.Inject;

import dao.activitydb.page.IPageRuleDao;
import entity.activity.page.PageRule;

/**
 * 页面抽奖规则 dao 实现类
 * 
 * @author liulj
 *
 */
public class PageRuleDaoImpl implements IPageRuleDao {

	@Inject
	PageRuleMapper mapper;

	@Override
	public int getCount(String url) {
		// TODO Auto-generated method stub
		return mapper.getCount(url);
	}

	@Override
	public List<PageRuleQuery> getPage(int page, int pagesize, String url) {
		// TODO Auto-generated method stub
		return mapper.getPage(page, pagesize, url);
	}

	@Override
	public int add(PageRule rule) {
		// TODO Auto-generated method stub
		return mapper.insert(rule);
	}

	@Override
	public int deleteByid(int iid) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(iid);
	}

	@Override
	public int updateByIid(PageRule PageRule) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(PageRule);
	}

	@Override
	public PageRule getById(Integer id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int getCountByPageid(Integer pageid) {
		// TODO Auto-generated method stub
		return mapper.getCountByPageid(pageid);
	}

	@Override
	public List<PageRule> getListByPageid(int pageid) {
		// TODO Auto-generated method stub
		return mapper.getListByPageid(pageid);
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: getPageRuleByPageId</p>
	 * <p>Description: 通过活动id查询活动规则</p>
	 * @param pageId
	 * @return
	 * @see dao.activitydb.page.IPageRuleDao#getPageRuleByPageId(int)
	 */
	public List<PageRule> getPageRuleByPageId(int pageId) {
		int enable = 1;
		return mapper.getPageRuleByPageId(pageId,enable);
	}
}
