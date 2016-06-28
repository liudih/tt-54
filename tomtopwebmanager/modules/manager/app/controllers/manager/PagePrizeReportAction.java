package controllers.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import forms.activity.page.PageJoinForm;
import forms.activity.page.PagePrizeResultForm;
import play.Logger;
import play.Logger.ALogger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.activity.page.IPageJoinService;
import services.activity.page.IPagePrizeResultService;
import services.activity.page.IPagePrizeService;
import services.activity.page.IPageService;
import services.activity.page.IPageTitleService;
import valueobject.activity.page.Page;
import valueobject.activity.page.PagePrize;
import valueobject.activity.page.PageTitle;

/**
 * 页面奖品统计报表的管理Action类
 * 
 * @author Guozy
 *
 */
public class PagePrizeReportAction extends Controller {

	/**
	 * 日志
	 */
	private ALogger logger = Logger.of(this.getClass());

	@Inject
	private IPageService iPageService;

	@Inject
	private IPagePrizeResultService iPagePrizeResultService;

	@Inject
	private IPageTitleService iPageTitleService;

	@Inject
	private IPagePrizeService iPagePrizeService;

	@Inject
	private IPageJoinService iPageJoinService;

	private Integer EN_LANGUAGE = 1;

	/**
	 * 获取初始化页面奖品统计结果数据信息
	 * 
	 * @param num
	 * @return
	 */
	public Result getInitPagePrizeReports(int num) {
		try {
			PagePrizeResultForm form = new PagePrizeResultForm();
			form.setPageNum(num);
			return ok(getPagePrizeReports(form));
		} catch (Exception e) {
			logger.error("页面错误" + e.getMessage());
			return badRequest("页面错误");
		}
	};

	/**
	 * 根据相应条件查找数据信息
	 * 
	 * @return
	 */
	public Result search() {
		Form<PagePrizeResultForm> form = Form.form(PagePrizeResultForm.class)
				.bindFromRequest();
		PagePrizeResultForm pagePrizeResultForm = null;
		try {
			pagePrizeResultForm = form.get();
			// 定义时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (pagePrizeResultForm.getEndDate() != null
					&& pagePrizeResultForm.getStartDate() != null) {
				String startStr = sdf
						.format(pagePrizeResultForm.getStartDate());
				String endStr = sdf.format(pagePrizeResultForm.getEndDate());
				Date startDate = sdf.parse(startStr);
				Date endDate = sdf.parse(endStr);
				if (endDate.getTime() < startDate.getTime()) {
					return badRequest("Start time can not be more than the end of time！");
				}
			}
			return ok(getPagePrizeReports(pagePrizeResultForm));
		} catch (Exception e) {
			logger.error("页面错误" + e.getMessage());
			return badRequest("页面错误");
		}
	};

	/**
	 * 查询数据所有信息
	 * 
	 * @param form
	 * @return
	 */
	public Html getPagePrizeReports(PagePrizeResultForm form) {
		List<PagePrizeResultForm> pagePrizeResultForms = iPagePrizeResultService
				.getPagePrizeResultsByDCreateDateAndAcitvityName(form);
		// 获取页面的所有数据信息
		List<Page> pageList = iPageService.getAll();
		// 创建页面标题的集合
		List<PageTitle> pageTitles = new ArrayList<PageTitle>();
		if (pageList != null) {
			for (Page page : pageList) {
				// 获取标题的数据信息
				PageTitle pageTitle = iPageTitleService.getPTByPageIdAndLId(
						page.getIid(), EN_LANGUAGE);
				pageTitles.add(pageTitle);
			}
		}
		// 统计表中的数据信息
		int count = iPagePrizeResultService.getPagePrizeResultsCount(form);
		// 获取统计页面数量
		Integer pageTotal = count / form.getPageSize()
				+ ((count % form.getPageSize() > 0) ? 1 : 0);
		return views.html.manager.prize.statistics.manage.activity_report_statistics
				.render(pagePrizeResultForms, pageTitles, form, count,
						form.getPageNum(), pageTotal);

	};

	/**
	 * 获取初始化页面中奖统计结果数据信息
	 * 
	 * @param num
	 * @return
	 */
	public Result getInitPagePrizeLotteryResults(int num) {
		try {
			PagePrizeResultForm form = new PagePrizeResultForm();
			form.setPageNum(num);
			return ok(getPagePrizeLotteryResults(form));
		} catch (Exception e) {
			logger.error("页面错误" + e.getMessage());
			return badRequest("页面错误");
		}
	};

	/**
	 * 根据相应条件,获取抽奖结果信息
	 * 
	 * @return
	 */
	public Result searchLottery() {
		Form<PagePrizeResultForm> form = Form.form(PagePrizeResultForm.class)
				.bindFromRequest();
		PagePrizeResultForm pagePrizeResultForm = null;
		try {
			pagePrizeResultForm = form.get();
			// 定义时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (pagePrizeResultForm.getEndDate() != null
					&& pagePrizeResultForm.getStartDate() != null) {
				String startStr = sdf
						.format(pagePrizeResultForm.getStartDate());
				String endStr = sdf.format(pagePrizeResultForm.getEndDate());
				Date startDate = sdf.parse(startStr);
				Date endDate = sdf.parse(endStr);
				if (endDate.getTime() < startDate.getTime()) {
					return badRequest("Start time can not be more than the end of time！");
				}
			}
			return ok(getPagePrizeLotteryResults(pagePrizeResultForm));
		} catch (Exception e) {
			logger.error("页面错误" + e.getMessage());
			return badRequest("页面错误");
		}
	};

	/**
	 * 查询抽奖结果数据所有信息
	 * 
	 * @param form
	 * @return
	 */
	public Html getPagePrizeLotteryResults(PagePrizeResultForm form) {
		form.setCemail(form.getCemail() == "" ? null : form.getCemail());
		form.setCcountry(form.getCcountry() == "" ? null : form.getCcountry());
		List<PagePrizeResultForm> pagePrizeResultForms = iPagePrizeResultService
				.getPagePrizeResultsByDateAndNameAndPrizeAndEmailAndCountry(form);
		// 获取页面的所有数据信息
		List<Page> pageList = iPageService.getAll();
		// 创建页面标题的集合
		List<PageTitle> pageTitles = new ArrayList<PageTitle>();
		// 获取奖品的所有数据信息
		List<PagePrize> pagePrizes = iPagePrizeService.getPagePrizes();
		if (pageList != null) {
			for (Page page : pageList) {
				// 获取标题的数据信息
				PageTitle pageTitle = iPageTitleService.getPTByPageIdAndLId(
						page.getIid(), EN_LANGUAGE);
				pageTitles.add(pageTitle);
			}
		}
		// 统计表中的数据信息
		int count = iPagePrizeResultService.getPagePrizeLotteryCount(form);
		// 获取统计页面数量
		Integer pageTotal = count / form.getPageSize()
				+ ((count % form.getPageSize() > 0) ? 1 : 0);
		Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
		Integer pageJoinFormCount = null;
		for (PagePrizeResultForm resultForm : pagePrizeResultForms) {
			pageJoinFormCount = iPageJoinService
					.getPageJoinsBycjoinerAndIpageId(resultForm.getCemail(),
							resultForm.getIpageid());
			resultMap.put(resultForm.getIid(), pageJoinFormCount);
		}

		return views.html.manager.prize.statistics.manage.activity_lottery_result_statistics
				.render(resultMap, pagePrizes, pagePrizeResultForms,
						pageTitles, form, count, form.getPageNum(), pageTotal);

	};
}
