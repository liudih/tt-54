package extensions.loyalty.campaign.signin;

import javax.inject.Inject;

import entity.loyalty.MemberSign;
import extension.point.ISigninProvider;

import java.util.Calendar;
import java.util.Date;

import mapper.loyalty.MemberPointMapper;

import org.apache.commons.lang3.time.DateUtils;

import play.Logger;

public class SigninPointProvider implements ISigninProvider {

	@Inject
	MemberPointMapper memberPointMapper;

	@Override
	public boolean checkMemberSignToday(String email, int siteId) {
		MemberSign memberSign = memberPointMapper.getMemberSign(email, siteId);
		Logger.debug("##########################"+email+"###"+siteId);
		if (memberSign != null) {
			Date lastSignTime = memberSign.getDlastsigndate();
			Date lastSignDate = DateUtils.truncate(lastSignTime, Calendar.DATE);
			Long diffSeconds = (System.currentTimeMillis() - lastSignDate
					.getTime()) / 1000;
			if (diffSeconds < 86400) {
				return true;
			}
		}
		return false;
	}
}
