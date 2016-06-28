package extensions.loyalty.campaign.rule.firstloginperday;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import services.campaign.CampaignContext;
import services.campaign.IActionRule;
import services.campaign.IActionRuleParameter;
import services.member.login.LoginService;
import valueobjects.member.MemberIdentification;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;

import dto.member.MemberLoginHistory;

public class FirstLoginPerDayActionRule implements IActionRule {

	public static final String ID = "first-login-per-day";

	@Inject
	LoginService service;

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean match(CampaignContext context, IActionRuleParameter param) {
		/*MemberIdentification memberID = (MemberIdentification) context
				.getActionOn();
		List<MemberLoginHistory> histories = service.getLoginHistoryByDate(
				memberID.getSiteID(), memberID.getEmail(), new Date());
		return !histories.isEmpty() && histories.size()==1;*/
		//屏蔽登录赠送积分功能 @by ChenJ
		return false;
	}

	@Override
	public ICodec<IActionRuleParameter, JsonNode> getParameterCodec() {
		return null;
	}

}
