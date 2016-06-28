package services.mobile.member;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.libs.F;
import services.member.IMemberEnquiryService;
import services.member.login.ILoginService;
import services.mobile.IMobileSessionService;
import services.mobile.MobileService;
import services.mobile.order.CartInfoService;
import services.mobile.personal.SettingService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import utils.CommonDefn;
import utils.ValidataUtils;
import valueobjects.base.CommonLoginContextFactory;
import valueobjects.base.LoginContext;
import valueobjects.member.MemberInSession;
import valuesobject.mobile.member.MobileLoginContext;
import valuesobject.mobile.member.result.MobileMember;
import context.WebContext;
import dto.member.MemberBase;
import entity.mobile.Setting;
import facades.cart.Cart;

public class LoginService {

	@Inject
	IMobileSessionService sessionServer;

	@Inject
	MobileService mobileService;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	ILoginService loginService;

	@Inject
	CartInfoService cartInfoService;

	@Inject
	SettingService settingService;

	@Inject
	UserService userService;

	@Inject
	IOrderEnquiryService orderEnquiryService;

	final static int UNKNOWN = -1;
	final static int SUCCESS = 1;
	final static int PASSWORD_ERROR = -2;

	public F.Tuple<Integer, MobileMember> login(String email, String pwd,
			WebContext context) {
		MemberBase member = memberEnquiryService.getMemberByMemberEmail(
				email.toLowerCase(), context);
		int status = PASSWORD_ERROR;
		MobileMember resultMember = null;
		if (member == null) {
			status = UNKNOWN;
		} else if (pwd != null && loginService.login(email, pwd, context)) {
			String token = loginService.getTokenForApp(email, pwd, context);
			if (StringUtils.isNotBlank(token)) {
				resultMember = getMobileMember(member, email);
				forceLogin(resultMember, member.getIgroupid(), member);
				// 新的登录机制需要传 token 和member.UUID
				setMobileLogint(token, member);
				return F.Tuple(SUCCESS, resultMember);
			}
		} else {
			status = PASSWORD_ERROR;
		}
		// recordFaildOnce();
		return F.Tuple(status, null);
	}

	/**
	 * 新的登录机制需要传 token 和member.UUID
	 * 
	 * @param token
	 * @param member
	 */
	private void setMobileLogint(String token, MemberBase member) {
		MobileLoginContext mcontext = new MobileLoginContext();
		mcontext.setCookieToken(token);
		mcontext.setCookieUUID(member.getCuuid());
		mobileService.setMLoginContext(mcontext);
	}

	public MobileMember getMobileMember(MemberBase member, String email) {
		MobileMember resultMember = new MobileMember();
		String mEmail = email;
		if (StringUtils.isNotBlank(member.getCemail())) {
			mEmail = member.getCemail();
		}
		String firstName = member.getCforumsnickname();
		String country = member.getCcountry();
		String fullName = null;
		if (firstName == null) {
			fullName = mEmail.split("@")[0];
		} else {
			fullName = firstName;
		}
		resultMember.setName(fullName);
		resultMember.setCountry(country == null ? "" : country);
		resultMember.setEmail(mEmail);
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
		resultMember.setMsgqty(userService.getUnMsgCount(mEmail));
		// 查询未支付订单数
		resultMember.setOrqty(ValidataUtils.validataInt(orderEnquiryService
				.countByEmailAndStatus(mEmail,
						IOrderStatusService.PAYMENT_PENDING,
						mobileService.getWebSiteID(), 1, true)));
		String uuid = mobileService.getUUID();
		// 同步购物车
		if (StringUtils.isNotBlank(uuid)) {
			cartInfoService.synchroMemberCart(uuid, mEmail);
			Cart cart = cartInfoService.getCurrentCart(uuid, false);
			if (cart != null) {
				resultMember.setCartqty(cart.getAllItems().size());
			}
		}
		return resultMember;
	}

	public void forceLogin(MobileMember member, int groupid, MemberBase base) {
		WebContext context = mobileService.getWebContext();
		Serializable playload = MemberInSession.newInstance(base.getIid(),
				member.getName(), member.getEmail(), null);
		LoginContext ctx = CommonLoginContextFactory.newLoginContext(context,
				member.getEmail(), groupid, playload);
		this.setLoginContext(ctx);
	}

	public boolean isLogin() {
		LoginContext ctx = this.getLoginContext();
		if (ctx != null && ctx.isLogin()) {
			return true;
		}
		return false;
	}

	public void loginOut() {
		this.setLoginContext(null);
	}

	public void setLoginContext(LoginContext ctx) {
		sessionServer.setLoginContext(ctx);
	}

	public LoginContext getLoginContext() {
		return sessionServer.getLoginContext();
	}

	public String getLoginMemberEmail() {
		LoginContext ctx = getLoginContext();
		if (ctx != null) {
			MemberInSession member = (MemberInSession) ctx.getPayload();
			if (member != null) {
				return member.getEmail();
			}
		}
		return null;
		// return "test@test.com";
	}

	public String getLoginMemberEmail(boolean b) {
		if (b == true) {
			return getLoginMemberEmail();
		} else {
			return CommonDefn.TESTEMAIL;
		}
	}
}
