package services.activity.page.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.Logger.ALogger;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import dao.activitydb.page.IPagePrizeResultDao;
import forms.activity.page.PagePrizeResultForm;
import services.activity.page.IPagePrizeResultService;
import values.activity.page.PagePrizeResultQuery;

/**
 * 页面奖品统计报表的实现类
 * 
 * @author Guozy
 *
 */
public class PagePrizeResultServiceImpl implements IPagePrizeResultService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	private IPagePrizeResultDao iPagePrizeResultDao;

	@Override
	public List<PagePrizeResultForm> getPagePrizeResultsByDCreateDateAndAcitvityName(
			PagePrizeResultForm pagePrizeResultForm) {
		PagePrizeResultQuery pagePrizeResultQuery = new PagePrizeResultQuery();
		List<PagePrizeResultForm> pagePrizeResultForms = new ArrayList<PagePrizeResultForm>();
		try {
			BeanUtils.copyProperties(pagePrizeResultQuery, pagePrizeResultForm);
			List<PagePrizeResultQuery> resultsList = iPagePrizeResultDao
					.getPagePrizeResultsByDCreateDateAndAcitvityName(pagePrizeResultQuery);

			pagePrizeResultForms = Lists.transform(resultsList,
					new Function<PagePrizeResultQuery, PagePrizeResultForm>() {

						@Override
						public PagePrizeResultForm apply(
								PagePrizeResultQuery result) {
							PagePrizeResultForm prizeResultForm = new PagePrizeResultForm();
							try {
								BeanUtils.copyProperties(prizeResultForm,
										result);
							} catch (Exception e) {
								logger.error("转化对象失败" + e.getMessage());
							}
							return prizeResultForm;
						}
					});
		} catch (Exception e) {
			logger.error("转化对象失败" + e.getMessage());
		}
		return pagePrizeResultForms;
	}

	@Override
	public int getPagePrizeResultsCount(PagePrizeResultForm pagePrizeResultForm) {
		PagePrizeResultQuery pagePrizeResultQuery = new PagePrizeResultQuery();
		int result = 0;
		try {
			BeanUtils.copyProperties(pagePrizeResultQuery, pagePrizeResultForm);
			result = iPagePrizeResultDao
					.getPagePrizeResultsCount(pagePrizeResultQuery);
		} catch (Exception e) {
			logger.error("转换对象失败" + e.getMessage());
		}
		return result;
	}

	@Override
	public List<PagePrizeResultForm> getPagePrizeResultsByDateAndNameAndPrizeAndEmailAndCountry(
			PagePrizeResultForm pagePrizeResultForm) {
		PagePrizeResultQuery pagePrizeResultQuery = new PagePrizeResultQuery();
		List<PagePrizeResultForm> pagePrizeResultForms = new ArrayList<PagePrizeResultForm>();
		try {
			BeanUtils.copyProperties(pagePrizeResultQuery, pagePrizeResultForm);
			List<PagePrizeResultQuery> resultsList = iPagePrizeResultDao
					.getPagePrizeResultsByDateAndNameAndPrizeAndEmailAndCountry(pagePrizeResultQuery);

			pagePrizeResultForms = Lists.transform(resultsList,
					new Function<PagePrizeResultQuery, PagePrizeResultForm>() {

						@Override
						public PagePrizeResultForm apply(
								PagePrizeResultQuery result) {
							PagePrizeResultForm prizeResultForm = new PagePrizeResultForm();
							try {
								BeanUtils.copyProperties(prizeResultForm,
										result);
							} catch (Exception e) {
								logger.error("转化对象失败" + e.getMessage());
							}
							return prizeResultForm;
						}
					});
		} catch (Exception e) {
			logger.error("转化对象失败" + e.getMessage());
		}
		return pagePrizeResultForms;
	}

	@Override
	public int getPagePrizeLotteryCount(PagePrizeResultForm pagePrizeResultForm) {
		PagePrizeResultQuery pagePrizeResultQuery = new PagePrizeResultQuery();
		int result = 0;
		try {
			BeanUtils.copyProperties(pagePrizeResultQuery, pagePrizeResultForm);
			result = iPagePrizeResultDao
					.getPagePrizeLotteryCount(pagePrizeResultQuery);
		} catch (Exception e) {
			logger.error("转换对象失败" + e.getMessage());
		}
		return result;
	}

}
