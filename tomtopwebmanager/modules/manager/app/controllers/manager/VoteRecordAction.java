package controllers.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import forms.activity.page.VoteRecordForm;
import play.Logger;
import play.Logger.ALogger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.activity.page.IPageItemService;
import services.activity.page.IVoteRecordService;
import valueobject.activity.page.PageItem;
import valueobject.activity.page.VoteRecord;
import views.html.member.register.hasBeen_activated;

/**
 * 投票统计管理类
 * 
 * @author Guozy
 *
 */
public class VoteRecordAction extends Controller {

	@Inject
	private IVoteRecordService iVoteRecordService;

	@Inject
	private IPageItemService iPageItemsService;
	
	
	/**
	 * 日志
	 */
	private ALogger logger = Logger.of(this.getClass());

	/**
	 * 获取初始化投票数据信息
	 * 
	 * @return
	 */
	public Result getInitVoteRecord(int num) {
		try {
			VoteRecordForm voteRecordForm = new VoteRecordForm();
			voteRecordForm.setPageNum(num);
			return ok(getVoteRecords(voteRecordForm));
		} catch (Exception e) {
			logger.error("页面错误" + e.getMessage());
			return badRequest("页面错误");
		}

	};

	/**
	 * 根据相应条件，获取数据信息
	 * 
	 * @return
	 */
	public Result search() {
		Form<VoteRecordForm> form = Form.form(VoteRecordForm.class)
				.bindFromRequest();
		VoteRecordForm voteRecordForm = null;
		try {
			voteRecordForm = form.get();
			// 定义时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (voteRecordForm.getEndDate() != null
					&& voteRecordForm.getStartDate() != null) {
				String startStr = sdf.format(voteRecordForm.getStartDate());
				String endStr = sdf.format(voteRecordForm.getEndDate());
				Date startDate = sdf.parse(startStr);
				Date endDate = sdf.parse(endStr);
				if (endDate.getTime() < startDate.getTime()) {
					return badRequest("Start time can not be more than the end of time！");
				}
			}
			return ok(getVoteRecords(voteRecordForm));
		} catch (Exception e) {
			logger.error("页面错误" + e.getMessage());
			return badRequest("页面错误");
		}
	}

	/**
	 * 获取所有数据信息
	 * 
	 * @param voteRecordForm
	 * @return
	 */
	public Html getVoteRecords(VoteRecordForm voteRecordForm) {
		voteRecordForm
				.setCmemberemail(voteRecordForm.getCmemberemail() == "" ? null
						: voteRecordForm.getCmemberemail());
		List<VoteRecordForm> voteRecords = null;
		List<VoteRecord> records = iVoteRecordService.getVoteRecords();
		Map<Integer, String> pageItemMap=new HashMap<Integer, String>();
		// 获取投票统计数据的条数
		Integer count = null;
		if (voteRecordForm.getIenable() == null) {
			voteRecords = iVoteRecordService
					.getVoteRecordByPageItemNameAndDate(voteRecordForm);
			count = iVoteRecordService.getVoteRecordCount(voteRecordForm);
		} else if (voteRecordForm.getIenable() == 1) {
			voteRecords = iVoteRecordService
					.getVoteRecordByPageItemNameAndDate(voteRecordForm);
			count = iVoteRecordService.getVoteRecordCount(voteRecordForm);
		} else if (voteRecordForm.getIenable() == 0) {
			voteRecords = iVoteRecordService
					.getVoteRecordUserByPageItemNameAndDate(voteRecordForm);
			count = iVoteRecordService.getVoteRecordUserCount(voteRecordForm);
		}
		
		if (voteRecords.size()>0 &&voteRecords!=null) {
			for (VoteRecordForm voteRecord : voteRecords) {
				PageItem pageItem=iPageItemsService.getById(voteRecord.getIpageitemid());
				pageItemMap.put(voteRecord.getIpageitemid(), pageItem.getCvalue());
			}
		}
		// 获取页面子项的所有数据信息
		List<PageItem> pageItems = iPageItemsService.getPageItems();

		// 获取投票统计页面数量
		Integer pageTotal = count / voteRecordForm.getPageSize()
				+ ((count % voteRecordForm.getPageSize() > 0) ? 1 : 0);
		return views.html.manager.vote.manage.vote_statistics.render(
				pageItemMap,records,
				pageItems, voteRecordForm, voteRecords, count,
				voteRecordForm.getPageNum(), pageTotal);
	}

}
