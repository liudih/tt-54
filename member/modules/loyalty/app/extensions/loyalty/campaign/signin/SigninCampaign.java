package extensions.loyalty.campaign.signin;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import mapper.loyalty.MemberIntegralHistoryMapper;
import services.campaign.ActionRules;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.ICampaignInstance;
import services.campaign.MultiRules.Match;
import services.loyalty.IPointsService;
import valueobjects.member.MemberIdentification;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import entity.loyalty.MemberSign;
import events.loyalty.SigninEvent;
import extensions.loyalty.campaign.action.point.GrantPointAction;
import extensions.loyalty.campaign.action.point.GrantPointActionParameter;
import extensions.loyalty.campaign.rule.signin.SigninActionRule;

import org.apache.commons.lang3.time.DateUtils;

import services.base.utils.DateFormatUtils;
public class SigninCampaign extends CampaignSupport {

	@Inject
	CampaignContextFactory ctxFactory;

	@Inject
	MemberIntegralHistoryMapper historyMapper;

	@Inject
	IPointsService pointsService;

	@Override
	public String getId() {
		return "sign-in";
	}

	@Override
	public Class<?> getPayloadClass() {
		return SigninEvent.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		SigninEvent event = (SigninEvent) payload;
		CampaignContext ctx = ctxFactory.createContext(payload, instance);
		MemberIdentification id = new MemberIdentification(event.getEmail(),
				event.getSiteID());
		ctx.setActionOn(id);
		return ctx;
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return new SigninCampaignInstance();
	}

	@Override
	public List<ICampaignInstance> getActiveInstances(Object payload) {
		SigninEvent e = (SigninEvent) payload;
		String email = e.getEmail();
		int siteId = e.getSiteID();
		return Lists.newArrayList(fixInstance(email, siteId));
	}

	@Override
	public Optional<ICampaignInstance> getInstance(String instanceId) {
		// XXX
		return Optional.absent();
	}

	protected List<String> getPossibleActionRuleIDs() {
		return Lists.newArrayList(SigninActionRule.ID);
	}

	protected List<String> getPossibleActionIDs() {
		return Lists.newArrayList(GrantPointAction.ID);
	}

	private SigninCampaignInstance fixInstance(String email, int siteId) {
		SigninCampaignInstance i = new SigninCampaignInstance();
		i.setInstanceId("sign-in-points");
		i.setCampaign(this);
		i.setActions(getPossibleActions());
		GrantPointActionParameter p = new GrantPointActionParameter();
		p.setSource("sign");
		p.setPoints(getPoint(email, siteId));
		p.setStatus(1);
		i.setActionParams(Lists.newArrayList(p));
		i.setActionRules(new ActionRules(getPossibleActionRules(),
				Match.MATCH_ALL));
		return i;
	}

	private int getPoint(String email, int siteId) {
		MemberSign memberSign = historyMapper.getMemberSign(email, siteId);
		// 连续登陆天数
		int integral = 0;
		if (null == memberSign) { // 从来没有签到过就新增一条数据
			Logger.debug("---------new user");
			memberSign = new MemberSign();
			memberSign.setIwebsiteid(siteId);
			memberSign.setCemail(email);
			memberSign.setIsigncount(1);
			memberSign.setDlastsigndate(DateFormatUtils.getCurrentTimeD());
			historyMapper.saveMemberSign(memberSign);
			integral = pointsService.getIntegralBySignDayCheck(memberSign
					.getIsigncount());
		} else {
			Logger.debug("--------old user");
			Date lastSignTime = memberSign.getDlastsigndate();
			Date lastSignDate = DateUtils.truncate(lastSignTime, Calendar.DATE);
			Long diffSeconds = (System.currentTimeMillis() - lastSignDate
					.getTime()) / 1000;
			if (diffSeconds > (86400 * 2)) {
				memberSign.setIsigncount(0);
			}
			memberSign.setDlastsigndate(DateFormatUtils.getCurrentTimeD());
			memberSign.setIsigncount(memberSign.getIsigncount() + 1);
			historyMapper.updateMemberSign(siteId, email,
					memberSign.getDlastsigndate(), memberSign.getIsigncount());
			integral = pointsService.getIntegralBySignDayCheck(memberSign
					.getIsigncount());
		}
		return integral;
	}
}
