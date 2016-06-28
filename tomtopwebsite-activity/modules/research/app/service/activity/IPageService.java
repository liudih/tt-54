package service.activity;

import java.util.Date;
import java.util.List;

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
import valueobjects.base.activity.result.PrizeResult;

public interface IPageService {

	/**
	 * 
	 * @Title: getPageByUrl
	 * @Description: TODO(通过url查询活动页面)
	 * @param @param title
	 * @param @param websiteId
	 * @param @return
	 * @return Page
	 * @throws 
	 * @author yinfei
	 */
	Page getPageByUrl(String title, int websiteId);

	/**
	 * 
	 * @Title: getPTByPageIdAndLId
	 * @Description: TODO(通过活动id和语言id查询活动标题)
	 * @param @param iid
	 * @param @param languageId
	 * @param @return
	 * @return PageTitle
	 * @throws 
	 * @author yinfei
	 */
	PageTitle getPTByPageIdAndLId(Integer iid, int languageId);

	/**
	 * 
	 * @Title: getPageItemByPageId
	 * @Description: TODO(通过活动id查询活动项)
	 * @param @param iid
	 * @param @return
	 * @return List<PageItem>
	 * @throws 
	 * @author yinfei
	 */
	List<PageItem> getPageItemByPageId(Integer iid);

	/**
	 * 
	 * @Title: getPINameByPIIdAndLId
	 * @Description: TODO(通过活动项目id和语言id查询活动项目名称)
	 * @param @param iid
	 * @param @param languageId
	 * @param @return
	 * @return String
	 * @throws 
	 * @author yinfei
	 */
	String getPINameByPIIdAndLId(Integer iid, int languageId);

	/**
	 * 
	 * @Title: getPQByPageId
	 * @Description: TODO(通过活动id获取筛选条件)
	 * @param @param pageId
	 * @param @return
	 * @return List<PageQualification>
	 * @throws 
	 * @author yinfei
	 */
	List<PageQualification> getPQByPageId(int pageId);

	/**
	 * 
	 * @Title: getPageRuleByPageId
	 * @Description: TODO(通过活动id查询活动规则)
	 * @param @param pageId
	 * @param @return
	 * @return List<PageRule>
	 * @throws 
	 * @author yinfei
	 */
	List<PageRule> getPageRuleByPageId(int pageId);

	/**
	 * 
	 * @Title: getPPByPageId
	 * @Description: TODO(通过活动id查询活动结果)
	 * @param @param pageId
	 * @param @return
	 * @return List<PagePrize>
	 * @throws 
	 * @author yinfei
	 */
	List<PagePrize> getPPByPageId(int pageId);

	/**
	 * 
	 * @Title: getJoinedCount
	 * @Description: TODO(通过活动和用户查询用户参加活动的次数)
	 * @param @param activityPageId
	 * @param @param memberID
	 * @param @param siteID
	 * @param @return
	 * @return int
	 * @throws 
	 * @author yinfei
	 */
	List<PageJoin> getJoinedCount(int activityPageId, String memberID, int siteID);

	/**
	 * 
	 * @Title: addPageJoin
	 * @Description: TODO(新增用户参与活动的记录)
	 * @param @param pageJoin
	 * @param @return
	 * @return int
	 * @throws 
	 * @author yinfei
	 */
	int addPageJoin(PageJoin pageJoin);

	List<PrizeResult> getPrizeResultByPageId(int pageId, int website);

	int getJoinMemberCount(int activityPageId, int siteID);

	List<PagePrizeResult> getPrizeResultByPIdAndMId(int activityPageId, String memberID, int id);

}
