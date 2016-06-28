package dao.activitydb.page.impl;

import java.util.List;

import mapper.activitydb.page.PagePrizeMapper;
import values.activity.page.PagePrizeQuery;

import com.google.inject.Inject;

import dao.activitydb.page.IPagePrizeDao;
import entity.activity.page.PagePrize;

/**
 * 页面抽奖奖品dao 实现类
 * 
 * @author liulj
 *
 */
public class PagePrizeDaoImpl implements IPagePrizeDao {

	@Inject
	PagePrizeMapper mapper;

	@Override
	public int add(PagePrize prize) {
		// TODO Auto-generated method stub
		return mapper.insert(prize);
	}

	@Override
	public PagePrize getById(Integer id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int deleteByid(int iid) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(iid);
	}

	@Override
	public int updateById(PagePrize prize) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(prize);
	}

	@Override
	public int getCount(String url) {
		// TODO Auto-generated method stub
		return mapper.getCount(url);
	}

	@Override
	public List<PagePrizeQuery> getPage(int page, int pagesize, String url) {
		// TODO Auto-generated method stub
		return mapper.getPage(page, pagesize, url);
	}

	@Override
	public int deleteByRuleid(Integer ruleid) {
		// TODO Auto-generated method stub
		return mapper.deleteByRuleid(ruleid);
	}

	public List<PagePrize> getPagePrizes() {
		return mapper.selectAll();
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: getPPByPageId</p>
	 * <p>Description: 通过活动id查询活动结果</p>
	 * @param pageId
	 * @return
	 * @see dao.activitydb.page.IPagePrizeDao#getPPByPageId(int)
	 */
	public List<PagePrize> getPPByPageId(int pageId) {
		int enable = 1;
		return mapper.getPPByPageId(pageId,enable);
	}
}
