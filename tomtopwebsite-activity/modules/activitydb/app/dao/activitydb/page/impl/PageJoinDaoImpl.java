package dao.activitydb.page.impl;

import java.util.List;

import javax.inject.Inject;

import values.activity.page.PageJoinQuery;
import mapper.activitydb.page.PageJoinMapper;
import dao.activitydb.page.IPageJoinDao;
import entity.activity.page.PageJoin;

public class PageJoinDaoImpl implements IPageJoinDao {

	@Inject
	private PageJoinMapper pageJoinMapper;
	
	@Override
	public Integer getPageJoinsBycjoinerAndIpageId(String cjoiner,
			Integer ipageid) {
		return pageJoinMapper.getPageJoinsBycjoinerAndIpageId(cjoiner, ipageid);
	}
	
	public int addPageJoin(PageJoin pageJoin){
		return pageJoinMapper.addPageJoin(pageJoin);
	}

	@Override
	public List<PageJoin> getJoinedCount(int activityPageId, String memberID, int siteID) {
		return pageJoinMapper.getJoinedCount(activityPageId,memberID,String.valueOf(siteID));
	}

	@Override
	public int getJoinMemberCount(int activityPageId, int siteID) {
		Integer count = 0;
		count = pageJoinMapper.getJoinMemberCount(activityPageId, String.valueOf(siteID));
		return count == null ? 0 : count;
	}

}
