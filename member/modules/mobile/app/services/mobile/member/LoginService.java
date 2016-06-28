package services.mobile.member;

import javax.inject.Inject;

import mapper.member.MemberBaseMapper;
import mapper.order.OrderMapper;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F;
import services.base.FoundationService;
import services.member.login.ILoginService;
import services.mobile.IMobileSessionService;
import services.mobile.MobileService;
import services.mobile.order.CartInfoService;
import services.mobile.personal.SettingService;
import utils.CommonDefn;
import utils.ValidataUtils;
import valuesobject.mobile.member.MobileLoginContext;
import valuesobject.mobile.member.Session;
import valuesobject.mobile.member.SessionMember;
import valuesobject.mobile.member.result.MobileMember;
import context.WebContext;
import dto.member.MemberBase;
import entity.mobile.Setting;

public class LoginService {

	@Inject
	IMobileSessionService sessionServer;

	@Inject
	MobileService mobileService;

	@Inject
	MemberBaseMapper mapper;

	@Inject
	ILoginService loginService;

	@Inject
	FoundationService foundation;

	@Inject
	CartInfoService cartInfoService;

	@Inject
	SettingService settingService;

	@Inject
	UserService userService;

	@Inject
	OrderMapper orderMapper;

	final static int UNKNOWN = -1;
	final static int SUCCESS = 1;
	final static int PASSWORD_ERROR = -2;

	public F.Tuple<Integer, MobileMember> login(String email, String pwd,
			WebContext context) {
		int site = foundation.getSiteID(context);
		MemberBase member = mapper.getUserByEmail(email.toLowerCase(), site);
		int status;
		MobileMember resultMember = null;
		if (member == null) {
			status = UNKNOWN;
		} else if (pwd != null && loginService.login(email, pwd, context)) {
			resultMember = new MobileMember();
			String eamil = member.getCemail();
			String firstName = member.getCfirstname();
			String country = member.getCcountry();
			String fullName = null;
			if (firstName == null) {
				fullName = eamil.split("@")[0];
			} else {
				fullName = firstName;
			}
			resultMember.setName(fullName);
			resultMember.setCountry(country == null ? "" : country);
			resultMember.setEmail(eamil);
			Setting setting = settingService.getSetting();
			resultMember.setCurrency(mobileService.getCurrency());
			resultMember.setLanguage(mobileService.getLanguageID() + "");
			if (setting != null) {
				resultMember.setCurrency(ValidataUtils.validataStr(setting
						.getCurrency()));
				resultMember.setLanguage(ValidataUtils.validataStr(setting
						.getLanguageid()));
			}
			// 查询用户为读消息数
			resultMember.setMsgqty(userService.getUnMsgCount(email));
			// 查询未支付订单数
			resultMember.setOrqty(ValidataUtils.validataInt(orderMapper
					.getCountByEmailAndStatus(email, 1,
							mobileService.getWebSiteID(), 1, true)));
			String uuid = mobileService.getUUID();
			// 同步购物车
			if (StringUtils.isNotBlank(uuid)) {
				Logger.debug("----uuid------" + uuid + "-----email---" + email);
				cartInfoService.synchroMemberCart(uuid, email);
			}
			forceLogin(resultMember);
			return F.Tuple(SUCCESS, resultMember);
		} else {
			status = PASSWORD_ERROR;
		}
		// recordFaildOnce();
		return F.Tuple(status, null);
	}

	public void forceLogin(MobileMember member) {
		MobileLoginContext ctx = new MobileLoginContext();
		ctx.setLogin(true);
		ctx.setPayload(new SessionMember(1, member.getEmail()));
		Session session = sessionServer.getAuthSession();
		if (session == null) {
			session = new Session();
		}
		session.setLoginContext(ctx);
		sessionServer.setAuth(session);

	}

	public boolean isCaptchas() {
		MobileLoginContext ctx = getLoginContext();
		if (ctx != null) {
			return ctx.isCaptchas();
		}
		return false;
	}

	public boolean isLogin() {
		MobileLoginContext ctx = this.getLoginContext();
		if (ctx != null && ctx.isLogin()) {
			return true;
		}
		return false;
	}

	public void loginOut() {
		this.setLoginContext(null);
	}

	public void recordFaildOnce() {
		MobileLoginContext ctx = getLoginContext();
		if (ctx == null) {
			return;
		}
		int count = ctx.getReqCount();
		ctx.setReqCount(1 + count);
		this.setLoginContext(ctx);
	}

	public void setLoginContext(MobileLoginContext ctx) {
		Session session = sessionServer.getAuthSession();
		if (session == null) {
			session = new Session();
		}
		session.setLoginContext(ctx);
		sessionServer.setAuth(session);
	}

	public MobileLoginContext getLoginContext() {
		Session session = sessionServer.getAuthSession();
		if (session == null) {
			return null;
		}
		return session.getLoginContext();

	}

	public String getLoginMemberEmail() {
		SessionMember member = (SessionMember) getLoginContext().getPayload();
		if (member != null) {
			return member.getEmail();
		}
		return null;
	}

	public String getLoginMemberEmail(boolean b) {
		if (b == true) {
			return getLoginMemberEmail();
		} else {
			return CommonDefn.TESTEMAIL;
		}
	}

}
