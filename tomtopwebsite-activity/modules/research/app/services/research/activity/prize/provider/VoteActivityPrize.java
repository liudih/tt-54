package services.research.activity.prize.provider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import interceptors.CacheResult;

import org.redisson.core.RMap;

import play.Logger;
import service.activity.IVoteRecordService;
import services.base.FoundationService;
import valueobjects.base.LoginContext;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.result.VoteActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import dao.activitydb.page.IVoteRecordDao;
import event.research.VoteRecordEvent;
import extensions.activity.IActivityPrizeProvider;
import extensions.base.activity.IActivitySessionService;

public class VoteActivityPrize implements IActivityPrizeProvider {

	@Inject
	IVoteRecordService iVoteRecordService;

	@Inject
	FoundationService foudactionService;

	@Inject
	EventBus ebus;

	@Inject
	IActivitySessionService iActivitySessionService;

	@Override
	public String getName() {
		return "vote";
	}

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult) {
		LoginContext lc = foudactionService.getLoginContext();
		ActivityResult result = new ActivityResult(
				ActivityStatus.FAILED);
		VoteRecordEvent vevent = new VoteRecordEvent(
				activityContext.getActivityPageItemId(), lc.getMemberID(),
				foudactionService.getVhost(), foudactionService.getSiteID());
		ebus.post(vevent);
		RMap<Integer, Integer> countmap = iActivitySessionService.getMap(this
				.getKey(activityContext));
		// countmap.expire(10, TimeUnit.SECONDS);
		int itemid = activityContext.getActivityPageItemId();
		int count = 1;
		if (countmap.containsKey(itemid)) {
			count = countmap.get(itemid);
			count++;
			countmap.fastPut(itemid, count);
		} else {
			// ~ 沒有的情況下去數據庫
			count = this.getCount(itemid, activityContext.getWebsiteId());
			countmap.fastPut(itemid, count);
			countmap.expire(600, TimeUnit.SECONDS);
			Logger.debug("--->map expire");
		}
		result = new VoteActivityResult(
				activityContext.getActivityPageItemId(), count,
				ActivityStatus.SUCC);
		return result;
	}

	private String getKey(ActivityContext activityContext) {
		String key = this.getName();
		key += activityContext.getActivityPageId();
		key += activityContext.getActivityType();
		return key;
	}

	// @CacheResult(expiration = 300)
	private int getCount(int itemid, int websiteid) {
		int count = iVoteRecordService.getPageItemCount(itemid, websiteid);
		if (count == 0) {
			return count = 1;
		}
		return count;
	}

	@Override
	public Class<?> getParam() {
		return ActivityParam.class;
	}
}
