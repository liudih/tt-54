package services.research.activity.qualification.provider;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import play.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.order.Order;
import entity.activity.page.PageJoin;
import service.activity.IPageService;
import services.base.FoundationService;
import services.order.IOrderEnquiryService;
import valueobjects.base.LoginContext;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.EnoughOrderMoneyActivityParam;
import valueobjects.base.activity.param.JoinActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import extensions.activity.IActivityQualificationProvider;

public class EnoughOrderMoneyActivityQualificationProvider implements
		IActivityQualificationProvider {

	@Inject
	FoundationService foudactionService;

	@Inject
	IOrderEnquiryService orderEnquiryService;
	
	@Inject
	IPageService pageService;

	@Override
	public String getName() {
		return "enough-order-Money";
	}

	@Override
	public int getPriority() {
		return 40;
	}

	@Override
	public Class<?> getParam() {
		return EnoughOrderMoneyActivityParam.class;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext) {
		EnoughOrderMoneyActivityParam param = null;
		LoginContext loginContext = foudactionService.getLoginContext();
		ObjectMapper om = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			if (activityContext.getActivityComponentParam() == null
					|| activityContext.getActivityComponentParam().getParam() == null) {
				Logger.error(" {} param can't be null",
						EnoughOrderMoneyActivityQualificationProvider.class.getName());
				return new ActivityResult(ActivityStatus.FAILED);
			}
			param = om.readValue(activityContext.getActivityComponentParam().getParam(),
					EnoughOrderMoneyActivityParam.class);
			Logger.debug(loginContext.getMemberID()+foudactionService.getSiteID()+sdf.parse(param.getBeginTime())+param.getTotalMoney());
			List<Order> orderList = orderEnquiryService.getOrderByMemberAndPayDate(loginContext.getMemberID(), 
					foudactionService.getSiteID(), sdf.parse(param.getBeginTime()),param.getTotalMoney());
			Logger.debug(orderList.size()+"");
			orderList = FluentIterable.from(orderList).toSortedList((p1, p2) -> p1.getIid().compareTo(p2.getIid())).asList();
			if (null != orderList && orderList.size() > 0) {
				List<PageJoin> pageJoinListcanJoinList = pageService.getJoinedCount(activityContext.getActivityPageId(),loginContext.getMemberID(),foudactionService.getSiteID());
				if(orderList.size() - pageJoinListcanJoinList.size() > 0){
					JoinActivityParam joinActivityParam = new JoinActivityParam();
					joinActivityParam.setOrderNumber(orderList.get(pageJoinListcanJoinList.size()).getCordernumber());
					ActivityResult ActivityResult = new ActivityResult();
					ActivityResult.setFixedStatus(ActivityStatus.SUCC);
					ActivityResult.setJoinParam(joinActivityParam);
					return ActivityResult;
				} else {
					return new ActivityResult(ActivityStatus.NOT_ENOUGH_MONEY);
				}
			} else {
				return new ActivityResult(ActivityStatus.NOT_ENOUGH_MONEY);
			}
		} catch (Exception e) {
			Logger.error("query enoughOrderMoney order error", e);
			return new ActivityResult(ActivityStatus.FAILED);
		}
	}

}
