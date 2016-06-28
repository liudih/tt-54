package services.member.login;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import mapper.member.MemberBaseMapper;
import mapper.member.MemberLoginHistoryMapper;

import org.apache.commons.lang3.time.DateUtils;

import play.Logger;
import play.Play;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.common.UUIDGenerator;
import services.member.IMemberEnquiryService;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import valueobjects.member.MemberInSession;
import valueobjects.member.MemberOtherIdentity;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import context.WebContext;
import dto.member.LoginEnum;
import dto.member.MemberBase;
import dto.member.MemberLoginHistory;
import dto.member.login.IThirdLoginParameter;
import dto.member.login.OtherLoginParameter;
import events.member.LoginEvent;
import extensions.member.login.ILoginProcess;
import extensions.member.login.ILoginProvider;
import extensions.member.login.IThirdPartyLoginService;

public class LoginService implements ILoginService {

	private static final String DOMAIN = ".tomtop.com";
	private static final String HOST = "tomtop.com";

	private static final SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd");

	@Inject
	FoundationService foundation;

	@Inject
	MemberBaseMapper mapper;

	@Inject
	IMemberEnquiryService enquiry;

	@Inject
	MemberLoginHistoryMapper history;

	@Inject
	EventBus eventBus;

	@Inject
	Set<ILoginProvider> loginProviders;

	@Inject
	CryptoUtils crypto;

	@Inject
	Set<ILoginProcess> loginProcess;

	@Inject
	Set<IThirdPartyLoginService> thirdPartyLoginServices;

	@Inject
	LoginServiceV2 login;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.login.ILoginService#login(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean login(String email, String password, WebContext context) {
		Logger.debug("Performing Login: {}", email);
		int siteid = foundation.getSiteID(context);
		MemberBase member = mapper.getUserByEmail(email.toLowerCase(), siteid);
		if (member != null && password != null
				&& crypto.validateHash(password, member.getCpasswd())) {
			forceLogin(member);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.login.ILoginService#login(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean login(String email, String source, String userId,
			WebContext context) {
		MemberOtherIdentity oi = new MemberOtherIdentity(source, userId, email);
		MemberBase member = enquiry.getMemberByOtherIdentity(oi, context);
		if (member != null) {
			login.loginForThirdParty(member);
//			forceLogin(member);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.login.ILoginService#forceLogin(dto.member.MemberBase)
	 */
	@Override
	public void forceLogin(MemberBase member) {
		LoginEvent event = null;
		if (member.getCaccount() == null || member.getCaccount().equals("")) {
			MemberInSession mis = MemberInSession.newInstance(member.getIid(),
					member.getCemail().toLowerCase(), member.getCemail()
							.toLowerCase(), foundation.getSessionID());
			foundation.setLoginContext(PlayLoginContextFactory.newLoginContext(
					member.getCemail(), member.getIgroupid(), mis));
			event = new LoginEvent(foundation.getLoginContext().getLTC(),
					foundation.getLoginContext().getSTC(),
					foundation.getClientIP(), foundation.getSiteID(), member
							.getCemail().toLowerCase(), foundation.getDevice());
		} else {
			MemberInSession mis = MemberInSession.newInstance(member.getIid(),
					member.getCaccount().toLowerCase(), member.getCemail()
							.toLowerCase(), foundation.getSessionID());
			foundation.setLoginContext(PlayLoginContextFactory.newLoginContext(
					member.getCemail(), member.getIgroupid(), mis));
			event = new LoginEvent(foundation.getLoginContext().getLTC(),
					foundation.getLoginContext().getSTC(),
					foundation.getClientIP(), foundation.getSiteID(), member
							.getCemail().toLowerCase(), foundation.getDevice());
		}
		eventBus.post(event);
		executeLoginProcess(event);
	}

	@Override
	public void executeLoginProcess(LoginEvent event) {
		for (ILoginProcess ss : loginProcess) {
			ss.execute(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.login.ILoginService#logout()
	 */
	@Override
	public void logout() {
		foundation.setLoginContext(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.login.ILoginService#getLoginData()
	 */
	@Override
	public MemberInSession getLoginData() {
		MemberInSession mis = (MemberInSession) foundation.getLoginContext()
				.getPayload();
		return mis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.login.ILoginService#getLoginData(play.mvc.Http.Context)
	 */
	@Override
	public MemberInSession getLoginData(Context context) {
		MemberInSession mis = (MemberInSession) foundation.getLoginContext(
				context).getPayload();
		return mis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.login.ILoginService#getLoginEmail()
	 */
	@Override
	public String getLoginEmail() {
		LoginContext ctx = foundation.getLoginContext();
		if (ctx.isLogin()) {
			return ctx.getMemberID();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.login.ILoginService#getLoginEmail(play.mvc.Http.Context)
	 */
	@Override
	public String getLoginEmail(Context context) {
		if (foundation.getLoginContext(context).isLogin()) {
			return getLoginData(context).getEmail();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.login.ILoginService#getOtherLoginButtons()
	 */
	@Override
	public List<Html> getOtherLoginButtons() {
		return FluentIterable
				.from(Ordering
						.natural()
						.onResultOf((ILoginProvider lp) -> lp.getDisplayOrder())
						.sortedCopy(loginProviders))
				.transform(lp -> lp.getLoginButton()).toList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.login.ILoginService#getLoginHistoryByDate(int,
	 * java.lang.String, java.util.Date)
	 */
	@Override
	public List<MemberLoginHistory> getLoginHistoryByDate(int siteID,
			String email, Date when) {
		Date from = DateUtils.truncate(when, Calendar.DATE);
		Date to = DateUtils.addMilliseconds(DateUtils.addDays(from, 1), -1);
		Logger.debug("Login History Search: {}-{}", from, to);
		return history.findByDateRange(siteID, email, from, to);
	}

	/**
	 * 
	 * @see ILoginService#authentication()
	 */
	@Override
	public boolean authentication(String email, String password,
			WebContext context) {
		try {
			int siteid = foundation.getSiteID(context);
			MemberBase member = mapper.getUserByEmail(email.toLowerCase(),
					siteid);
			if (member != null && password != null
					&& crypto.validateHash(password, member.getCpasswd())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			Logger.error("do authentication for {} failed", email, e);
			return false;
		}
	}

	@Override
	public OtherLoginResult thirdGoogleLogin(
			IThirdLoginParameter thirdLoginParameter, String appId,
			String appSecret, WebContext content) {

		if (thirdLoginParameter instanceof OtherLoginParameter) {
			OtherLoginParameter gpara = (OtherLoginParameter) thirdLoginParameter;
			return this.googelLogin(gpara.getCode(), gpara.getState(),
					gpara.getReredirectUri(), appId, appSecret, content);
		}
		return null;
	}

	private OtherLoginResult googelLogin(String code, String state,
			String reredirectUri, String appId, String appSecret,
			WebContext content) {

		Set<IThirdPartyLoginService> lservice = Sets.filter(
				thirdPartyLoginServices, p -> "google".equals(p.getName()));
		if (lservice == null || lservice.size() == 0) {
			LoginEnum em = LoginEnum.LoginNot;
			OtherLoginResult re = new OtherLoginResult(em, null);
			return re;
		}
		for (IThirdPartyLoginService tloginservice : lservice) {
			return tloginservice.login(code, state, reredirectUri, appId,
					appSecret, content);
		}
		LoginEnum em = LoginEnum.LoginNot;
		OtherLoginResult re = new OtherLoginResult(em, null);
		return re;
	}

	@Override
	public OtherLoginResult thirdFaceLogin(
			IThirdLoginParameter thirdLoginParameter, String appId,
			String appSecret, WebContext content) {
		if (thirdLoginParameter instanceof OtherLoginParameter) {
			OtherLoginParameter gpara = (OtherLoginParameter) thirdLoginParameter;
			return this.facelLogin(gpara.getCode(), gpara.getState(),
					gpara.getReredirectUri(), appId, appSecret, content);
		}
		return null;
	}

	private OtherLoginResult facelLogin(String code, String state,
			String reredirectUri, String appId, String appSecret,
			WebContext content) {

		Set<IThirdPartyLoginService> lservice = Sets.filter(
				thirdPartyLoginServices, p -> "facebook".equals(p.getName()));
		if (lservice == null || lservice.size() == 0) {
			LoginEnum em = LoginEnum.LoginNot;
			OtherLoginResult re = new OtherLoginResult(em, null);
			return re;
		}
		for (IThirdPartyLoginService tloginservice : lservice) {
			return tloginservice.login(code, state, reredirectUri, appId,
					appSecret, content);
		}
		LoginEnum em = LoginEnum.LoginNot;
		OtherLoginResult re = new OtherLoginResult(em, null);
		return re;
	}

	/**
	 * 当用户登录时来验证账号密码,如果认真通过则会返回一个token,然后保存token到cookie来标示用户已经登录
	 * 
	 * @param email
	 * @param password
	 * @return null 验证通过
	 */
	public String getToken(String email, String password, WebContext context) {
		if (email == null || email.length() == 0) {
			return null;
		}
		int site = this.foundation.getSiteID(context);
		MemberBase member = mapper.getUserByEmail(email.toLowerCase(), site);

		if (member != null && password != null
				&& crypto.validateHash(password, member.getCpasswd())) {
			// 创建token 加上密码是为了当用户修改密码后让token失效
			String secure = Play.application().configuration()
					.getString("login.secure");
			if(secure == null){
				secure = this.login.SECURE;
			}
			// 取日期
			Date date = new Date();
			String dateStr = dateFormater.format(date);

			StringBuilder key = new StringBuilder();
			key.append(member.getCemail());
			key.append(member.getCpasswd());
			key.append(secure);
			key.append(dateStr);
			String token = crypto.md5(key.toString());

			Integer id = member.getIid();
			String uuid = member.getCuuid();
			if (uuid == null) {
				uuid = UUIDGenerator.createAsString();
				mapper.updateUuidById(id, uuid);
			}

			return token;
		}

		return null;
	}
	
	/**
	 * 给APP用(去掉日期)，当用户登录时来验证账号密码,如果认真通过则会返回一个token,然后保存token到cookie来标示用户已经登录
	 * 
	 * @param email
	 * @param password
	 * @return null 验证通过
	 */
	public String getTokenForApp(String email, String password, WebContext context) {
		if (email == null || email.length() == 0) {
			return null;
		}
		int site = this.foundation.getSiteID(context);
		MemberBase member = mapper.getUserByEmail(email.toLowerCase(), site);

		if (member != null && password != null
				&& crypto.validateHash(password, member.getCpasswd())) {
			// 创建token 加上密码是为了当用户修改密码后让token失效
			String secure = Play.application().configuration()
					.getString("login.secure");
			if(secure == null){
				secure = this.login.SECURE;
			}

			StringBuilder key = new StringBuilder();
			key.append(member.getCemail());
			key.append(member.getCpasswd());
			key.append(secure);
			String token = crypto.md5(key.toString());

			Integer id = member.getIid();
			String uuid = member.getCuuid();
			if (uuid == null) {
				uuid = UUIDGenerator.createAsString();
				mapper.updateUuidById(id, uuid);
			}

			return token;
		}

		return null;
	}

	/**
	 * 获取登录上下文
	 * 
	 * @param uuid
	 * @param token
	 * @return 如果没有登录则返回null
	 */
	public LoginContext getLoginContext(String uuid, String token) {
		LoginContext ctx = null;
		if (uuid == null || token == null) {
			ctx = PlayLoginContextFactory.newAnonymousLoginContext();
			return ctx;
		}
		MemberBase member = this.mapper.getUserByUuid(uuid);
		if (member == null) {
			ctx = PlayLoginContextFactory.newAnonymousLoginContext();
			return ctx;
		}
		String email = member.getCemail().toLowerCase();
		String passwd = member.getCpasswd();

		String secure = Play.application().configuration()
				.getString("login.secure");
		
		if(secure == null){
			secure = this.login.SECURE;
		}
		
		
		// 取日期
		Date date = new Date();
		String dateStr = dateFormater.format(date);

		StringBuilder key = new StringBuilder();
		key.append(email);
		key.append(passwd);
		key.append(secure);
		key.append(dateStr);
		String secretKey = crypto.md5(key.toString());

		if (secretKey.equals(token)) {

			MemberInSession mis = MemberInSession.newInstance(member.getIid(),
					member.getCemail().toLowerCase(), member.getCemail()
							.toLowerCase(), null);
			ctx = PlayLoginContextFactory.newLoginContext(email,
					member.getIgroupid(), mis);

			return ctx;
		}

		ctx = PlayLoginContextFactory.newAnonymousLoginContext();
		return ctx;
	}
}
