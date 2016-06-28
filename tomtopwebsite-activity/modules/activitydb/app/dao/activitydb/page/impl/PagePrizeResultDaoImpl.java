package dao.activitydb.page.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.activitydb.page.PagePrizeResultMapper;
import valueobjects.base.activity.result.PrizeResult;
import values.activity.page.PagePrizeResultQuery;
import dao.activitydb.page.IPagePrizeResultDao;
import entity.activity.page.PagePrize;
import entity.activity.page.PagePrizeResult;

public class PagePrizeResultDaoImpl implements IPagePrizeResultDao {

	@Inject
	private PagePrizeResultMapper pagePrizeResultMapper;

	@Override
	public List<PagePrizeResultQuery> getPagePrizeResultsByDCreateDateAndAcitvityName(
			PagePrizeResultQuery pagePrizeResultQuery) {
		return pagePrizeResultMapper
				.getPagePrizeResultsByDCreateDateAndAcitvityName(
						pagePrizeResultQuery.getIpageid(),
						pagePrizeResultQuery.getStartDate(),
						pagePrizeResultQuery.getEndDate(),
						pagePrizeResultQuery.getPageSize(),
						pagePrizeResultQuery.getPageNum());
	}

	@Override
	public int getPagePrizeResultsCount(
			PagePrizeResultQuery pagePrizeResultQuery) {
		return pagePrizeResultMapper.getCount(
				pagePrizeResultQuery.getIpageid(),
				pagePrizeResultQuery.getStartDate(),
				pagePrizeResultQuery.getEndDate());
	}

	@Override
	public List<PagePrizeResultQuery> getPagePrizeResultsByDateAndNameAndPrizeAndEmailAndCountry(
			PagePrizeResultQuery pagePrizeResultQuery) {
		return pagePrizeResultMapper
				.getPagePrizeResultsByDateAndNameAndPrizeAndEmailAndCountry(
						pagePrizeResultQuery.getIpageid(),
						pagePrizeResultQuery.getCemail(),
						pagePrizeResultQuery.getCcountry(),
						pagePrizeResultQuery.getIprizeid(),
						pagePrizeResultQuery.getStartDate(),
						pagePrizeResultQuery.getEndDate(),
						pagePrizeResultQuery.getPageSize(),
						pagePrizeResultQuery.getPageNum());
	}

	@Override
	public int getPagePrizeLotteryCount(
			PagePrizeResultQuery pagePrizeResultQuery) {
		return pagePrizeResultMapper.getPagePrizeLotteryCount(
				pagePrizeResultQuery.getIpageid(),
				pagePrizeResultQuery.getCemail(),
				pagePrizeResultQuery.getCcountry(),
				pagePrizeResultQuery.getIprizeid(),
				pagePrizeResultQuery.getStartDate(),
				pagePrizeResultQuery.getEndDate());
	}

	/**
	 * 新增ovr
	 * 
	 * @param result
	 * @return
	 */
	public int add(PagePrizeResult result) {
		return pagePrizeResultMapper.insert(result);
	}

	@Override
	public List<PrizeResult> getPrizeResultByPageId(int pageId,int website) {
		return pagePrizeResultMapper.getPrizeResultByPageId(pageId,website);
	}

	@Override
	public List<PagePrizeResult> getPrizeResultByPIdAndMId(int activityPageId, String memberID, int pid) {
		return pagePrizeResultMapper.getPrizeResultByPIdAndMId(activityPageId, memberID, pid);
	}

}
