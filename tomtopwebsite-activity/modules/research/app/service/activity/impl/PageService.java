package service.activity.impl;

import interceptors.CacheResult;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dao.activitydb.page.IPageDao;
import dao.activitydb.page.IPageItemDao;
import dao.activitydb.page.IPageItemNameDao;
import dao.activitydb.page.IPageJoinDao;
import dao.activitydb.page.IPagePrizeDao;
import dao.activitydb.page.IPagePrizeResultDao;
import dao.activitydb.page.IPageQualificationDao;
import dao.activitydb.page.IPageRuleDao;
import dao.activitydb.page.IPageTitleDao;
import dto.image.Img;
import dto.member.MemberBase;
import dto.order.Order;
import entity.activity.page.Page;
import entity.activity.page.PageItem;
import entity.activity.page.PageJoin;
import entity.activity.page.PagePrize;
import entity.activity.page.PagePrizeResult;
import entity.activity.page.PageQualification;
import entity.activity.page.PageRule;
import entity.activity.page.PageTitle;
import service.activity.IPageService;
import valueobjects.base.activity.result.PrizeResult;

public class PageService implements IPageService {
	@Inject
	IPageDao pageDao;

	@Inject
	IPageTitleDao pageTitleDao;

	@Inject
	IPageItemDao pageItemDao;

	@Inject
	IPageItemNameDao pageItemNameDao;
	
	@Inject
	IPageQualificationDao pageQualificationDao;
	
	@Inject
	IPageRuleDao pageRuleDao;
	
	@Inject
	IPagePrizeDao pagePrizeDao;
	
	@Inject
	IPageJoinDao pageJoinDao;
	
	@Inject
	IPagePrizeResultDao pagePrizeResultDao;
	
	/*
	 * (non-Javadoc)
	 * <p>Title: getPageByUrl</p>
	 * <p>Description: 通过url查询活动页面</p>
	 * @param title
	 * @param websiteId
	 * @return
	 * @see service.vote.IPageService#getPageByUrl(java.lang.String, int)
	 */
	@Override
	//@CacheResult
	public Page getPageByUrl(String title, int websiteId) {
		return pageDao.getPageByUrl(title, websiteId);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPTByPageIdAndLId</p>
	 * <p>Description: 通过活动id和语言id查询活动标题</p>
	 * @param iid
	 * @param languageId
	 * @return
	 * @see service.vote.IPageService#getPTByPageIdAndLId(java.lang.Integer, int)
	 */
	@Override
	@CacheResult(expiration = 600)
	public PageTitle getPTByPageIdAndLId(Integer iid, int languageId) {
		return pageTitleDao.getPTByPageIdAndLId(iid, languageId);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPageItemByPageId</p>
	 * <p>Description: 通过活动id查询活动项</p>
	 * @param iid
	 * @return
	 * @see service.vote.IPageService#getPageItemByPageId(java.lang.Integer)
	 */
	@Override
	@CacheResult(expiration = 600)
	public List<PageItem> getPageItemByPageId(Integer iid) {
		return pageItemDao.getPageItemByPageId(iid);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPINameByPIIdAndLId</p>
	 * <p>Description: 通过活动项目id和语言id查询活动项目名称</p>
	 * @param iid
	 * @param languageId
	 * @return
	 * @see service.vote.IPageService#getPINameByPIIdAndLId(java.lang.Integer, int)
	 */
	@Override
	@CacheResult(expiration = 600)
	public String getPINameByPIIdAndLId(Integer iid, int languageId) {
		return pageItemNameDao.getPINameByPIIdAndLId(iid, languageId);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPQByPageId</p>
	 * <p>Description: 通过活动id获取筛选条件</p>
	 * @param pageId
	 * @return
	 * @see service.vote.IPageService#getPQByPageId(int)
	 */
	@Override
	public List<PageQualification> getPQByPageId(int pageId) {
		return pageQualificationDao.getPQByPageId(pageId);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPageRuleByPageId</p>
	 * <p>Description: 通过活动id查询活动规则</p>
	 * @param pageId
	 * @return
	 * @see service.vote.IPageService#getPageRuleByPageId(int)
	 */
	@Override
	public List<PageRule> getPageRuleByPageId(int pageId) {
		return pageRuleDao.getPageRuleByPageId(pageId);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPPByPageId</p>
	 * <p>Description: 通过活动id查询活动结果</p>
	 * @param pageId
	 * @return
	 * @see service.vote.IPageService#getPPByPageId(int)
	 */
	@Override
	public List<PagePrize> getPPByPageId(int pageId) {
		return pagePrizeDao.getPPByPageId(pageId);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getJoinedCount</p>
	 * <p>Description: 通过活动和用户查询用户参加活动的次数</p>
	 * @param activityPageId
	 * @param memberID
	 * @param siteID
	 * @return
	 * @see service.activity.IPageService#getJoinedCount(int, java.lang.String, int)
	 */
	@Override
	public List<PageJoin> getJoinedCount(int activityPageId, String memberID, int siteID) {
		return pageJoinDao.getJoinedCount(activityPageId,memberID,siteID);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: addPageJoin</p>
	 * <p>Description: 新增用户参与活动的记录</p>
	 * @param pageJoin
	 * @return
	 * @see service.activity.IPageService#addPageJoin(entity.activity.page.PageJoin)
	 */
	@Override
	public int addPageJoin(PageJoin pageJoin) {
		return pageJoinDao.addPageJoin(pageJoin);
	}

	@Override
	public List<PrizeResult> getPrizeResultByPageId(int pageId,int website) {
		return pagePrizeResultDao.getPrizeResultByPageId(pageId,website);
	}

	@Override
	public int getJoinMemberCount(int activityPageId, int siteID) {
		return pageJoinDao.getJoinMemberCount(activityPageId, siteID);
	}

	@Override
	public List<PagePrizeResult> getPrizeResultByPIdAndMId(int activityPageId, String memberID, int pid) {
		return pagePrizeResultDao.getPrizeResultByPIdAndMId(activityPageId, memberID, pid);
	}

}
