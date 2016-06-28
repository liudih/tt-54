package services.research.activity.qualification.provider;

import java.util.Calendar;
import java.util.Date;

import com.google.inject.Inject;

import service.activity.IVoteRecordService;
import services.base.FoundationService;
import valueobjects.base.LoginContext;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import extensions.activity.IActivityQualificationProvider;

public class OnceADayActivityQualificationProvider implements IActivityQualificationProvider{

	@Inject
	FoundationService foudactionService;
	
	@Inject
	IVoteRecordService iVoteRecordService;
	
	@Override
	public String getName() {
		return "once a day";
	}

	@Override
	public int getPriority() {
		return 11;
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: match</p>
	 * <p>Description: 能否投票的规则校验</p>
	 * @param activityContext
	 * @return
	 * @see extensions.activity.IActivityQualificationProvider#match(valueobjects.base.activity.ActivityContext)
	 */
	@Override
	public ActivityResult match(ActivityContext activityContext) {
		LoginContext lc = foudactionService.getLoginContext();
		int count = iVoteRecordService.getPageItemCountToday(
				activityContext.getWebsiteId(), lc.getMemberID(),
				activityContext.getActivityPageItemId(), new Date(),
				getTomorrowDate());
		if (count > 0) {
			return new ActivityResult(ActivityStatus.EXCEEDNUMBER);
		}
		return new ActivityResult(ActivityStatus.SUCC);
	}
	
	/**
	 * 
	 * @Title: getTomorrowDate
	 * @Description: TODO(获取明天的日期)
	 * @param @return
	 * @return Date
	 * @throws 
	 * @author yinfei
	 */
	private Date getTomorrowDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		date = calendar.getTime();
		return date;
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getParam</p>
	 * <p>Description: 获取参数</p>
	 * @return
	 * @see extensions.activity.IActivityQualificationProvider#getParam()
	 */
	@Override
	public Class<?> getParam() {
		return ActivityParam.class;
	}

}
