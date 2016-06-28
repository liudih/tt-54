package services.research.activity.qualification.provider;

import java.text.SimpleDateFormat;

import play.Logger;
import play.mvc.Http.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.member.MemberBase;
import service.activity.IPageService;
import services.base.FoundationService;
import services.member.IMemberEnquiryService;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.NewMemberActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import extensions.activity.IActivityQualificationProvider;

public class NewMemberActivityQualificationProvider implements
		IActivityQualificationProvider {

	@Inject
	FoundationService foudactionService;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	IPageService pageService;

	@Override
	public String getName() {
		return "new-member";
	}

	@Override
	public int getPriority() {
		return 30;
	}

	@Override
	public Class<?> getParam() {
		return NewMemberActivityParam.class;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext) {
		NewMemberActivityParam param = null;
		ObjectMapper om = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			if (activityContext.getActivityComponentParam() == null
					|| activityContext.getActivityComponentParam().getParam() == null) {
				Logger.error(" {} param can't be null",
						NewMemberActivityQualificationProvider.class.getName());
				return new ActivityResult(ActivityStatus.FAILED);
			}
			param = om.readValue(activityContext.getActivityComponentParam()
					.getParam(), NewMemberActivityParam.class);
			MemberBase MemberBase = memberEnquiryService
					.getMemberByMemberEmail(foudactionService.getLoginContext()
							.getMemberID(), ContextUtils.getWebContext(Context
							.current()));
			if (null != param && null != MemberBase) {
				if (MemberBase.getDcreatedate().after(
						sdf.parse(param.getBeginTime()))) {
					return new ActivityResult(ActivityStatus.SUCC);
				} else {
					return new ActivityResult(ActivityStatus.NOT_NEW_MEMBER);
				}
			} else {
				return new ActivityResult(ActivityStatus.FAILED);
			}
		} catch (Exception e) {
			Logger.error("error", e);
			return new ActivityResult(ActivityStatus.FAILED);
		}
	}

}
