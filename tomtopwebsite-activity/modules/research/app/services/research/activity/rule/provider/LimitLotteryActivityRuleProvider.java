package services.research.activity.rule.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

import entity.activity.page.PagePrizeResult;
import extensions.activity.IActivityPrizeProvider;
import extensions.activity.IActivityRuleProvider;
import play.Logger;
import service.activity.IPageService;
import services.base.FoundationService;
import valueobjects.base.activity.ActivityComponentParam;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.param.LimitLotteryActivityParam;
import valueobjects.base.activity.param.PrizeLotteryTimesActivityParam;
import valueobjects.base.activity.rule.ActivityRuleResult;
import valueobjects.base.activity.rule.LimitLotteryActivityRuleResult;

public class LimitLotteryActivityRuleProvider implements IActivityRuleProvider {

	@Inject
	IPageService pageService;
	@Inject
	FoundationService foudactionService;

	@Override
	public String getName() {
		return "limit lottery rule";
	}

	@Override
	public int getPriority() {
		return 11;
	}

	@Override
	public Class<?> getParam() {
		return LimitLotteryActivityParam.class;
	}

	@Override
	public Class<?> getPrizeParam() {
		return new LimitLotteryActivityRuleResult().getPrizeParam();
	}

	@Override
	public ActivityRuleResult execute(ActivityContext activityContext, List<ActivityComponentParam> prizesParams,
			Map<String, IActivityPrizeProvider> pmap) {
		ObjectMapper om = new ObjectMapper();
		//限定中奖参数（可抽奖次数，可中奖次数）
		LimitLotteryActivityParam param = null;
		//奖品中奖次数（奖品中奖次数）
		PrizeLotteryTimesActivityParam plp = null;
		//限定中奖结果
		LimitLotteryActivityRuleResult llr = new LimitLotteryActivityRuleResult();
		llr.setPassed(true);
		try {
			param = om.readValue(activityContext.getActivityComponentParam().getParam(),
					LimitLotteryActivityParam.class);
			int canJoinTimes = param.getJoinTimes();
			int canLotteryTimes = param.getLotteryTimes();
			int joinTimes = pageService.getJoinedCount(activityContext.getActivityPageId(),
					foudactionService.getLoginContext().getMemberID(), foudactionService.getSiteID()).size();
			Logger.debug("canJoinTimes-" + canJoinTimes);
			Logger.debug("canLotteryTimes-" + canLotteryTimes);
			Logger.debug("joinTimes-" + joinTimes);
			int randomNumber = getRandomNumber(1, canJoinTimes - joinTimes);
			Logger.debug("randomNumber-" + randomNumber);
			//可中奖次数大于等于随机数，则中奖；如果小于随机数，则不中奖（给安慰奖）
			if (canLotteryTimes >= randomNumber) {
				for (ActivityComponentParam aparam : prizesParams) {
					if (pmap.containsKey(aparam.getClassName())) {
						activityContext.changeComponentParam(aparam);
						plp = om.readValue(activityContext.getActivityComponentParam().getExtendsParam(),
								PrizeLotteryTimesActivityParam.class);
						if (isGetPrize(plp, activityContext, aparam)) {
							llr.setAcp(aparam);
							break;
						}
					}
				}
				return llr;
			} else {
				for (ActivityComponentParam aparam : prizesParams) {
					if (pmap.containsKey(aparam.getClassName())) {
						activityContext.changeComponentParam(aparam);
						plp = om.readValue(activityContext.getActivityComponentParam().getExtendsParam(),
								PrizeLotteryTimesActivityParam.class);
						if (plp.getPrizeLotteryTimes() == 0) {
							llr.setAcp(aparam);
							break;
						}
					}
				}
				return llr;
			}
		} catch (Exception e) {
			Logger.error("json parse error");
		}
		return llr;
	}

	/**
	 * 
	 * @Title: getRandomNumber
	 * @Description: TODO(获取两个数之间的随机数)
	 * @param @param startNumber
	 * @param @param endNumber
	 * @param @return
	 * @return int
	 * @throws
	 */
	private int getRandomNumber(int startNumber, int endNumber) {
		int randomInt = 0;
		List<Integer> baseIntList = new ArrayList<Integer>();
		for (int i = startNumber; i <= endNumber; i++) {
			baseIntList.add(i);
		}
		if (baseIntList.size() > 0) {
			randomInt = baseIntList.get(new Random().nextInt(baseIntList.size()));
		} else {
			randomInt = startNumber;
		}
		return randomInt;
	}

	/**
	 * 
	 * @Title: isGetPrize
	 * @Description: TODO(判断是否给奖品)
	 * @param @param plp
	 * @param @param activityContext
	 * @param @param aparam
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	private boolean isGetPrize(PrizeLotteryTimesActivityParam plp, ActivityContext activityContext,
			ActivityComponentParam aparam) {
		if (plp.getPrizeLotteryTimes() == 0) {
			return true;
		}
		List<PagePrizeResult> prizeResultList = pageService.getPrizeResultByPIdAndMId(
				activityContext.getActivityPageId(), foudactionService.getLoginContext().getMemberID(), aparam.getId());
		Logger.debug("prizeResultList-size:" + prizeResultList.size());
		return prizeResultList.size() < plp.getPrizeLotteryTimes();
	}
}
