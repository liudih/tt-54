package services.member.login;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.inject.Inject;

import mapper.member.MemberBaseMapper;
import play.Logger;
import play.Play;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import services.ILoginProvider;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.common.UUIDGenerator;
import services.member.IMemberEnquiryService;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import valueobjects.member.MemberInSession;

import com.google.common.eventbus.EventBus;

import context.WebContext;
import context.WebCookie;
import dto.member.MemberBase;
import events.member.LoginEvent;
import extensions.member.login.ILoginProcess;

public class LoginServiceV2 implements ILoginProvider {

	@Inject
	FoundationService foundation;

	@Inject
	MemberBaseMapper mapper;

	@Inject
	IMemberEnquiryService enquiry;

	@Inject
	EventBus eventBus;

	@Inject
	CryptoUtils crypto;

	@Inject
	Set<ILoginProcess> loginProcess;

	public static final String DOMAIN = ".tomtop.com";
	public static final String HOST = "tomtop.com";
	public static final String SECURE = "01d067";

	private static final SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 第三方登录
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public boolean loginForThirdParty(MemberBase member) {
		// int siteid = foundation.getSiteID();
		// MemberBase member = mapper.getUserByEmail(email.toLowerCase(),
		// siteid);
		if (member != null) {
			forceLogin(member);
			// 创建token 加上密码是为了当用户修改密码后让token失效
			String secure = Play.application().configuration()
					.getString("login.secure");
			if (secure == null) {
				secure = SECURE;
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

			String host = Context.current().request().getHeader("Host");
			Logger.debug("Host:{}", host);

			Integer id = member.getIid();
			String uuid = member.getCuuid();
			if (uuid == null) {
				uuid = UUIDGenerator.createAsString();
				mapper.updateUuidById(id, uuid);
			}

			if (host != null && host.indexOf(HOST) != -1) {
				Context.current()
						.response()
						.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/",
								DOMAIN);

				Context.current()
						.response()
						.setCookie("TT_UUID", uuid, 365 * 24 * 3600, "/",
								DOMAIN);
			} else {
				Context.current().response()
						.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/");

				Context.current().response()
						.setCookie("TT_UUID", uuid, 365 * 24 * 3600, "/");
			}
			return true;
		}
		return false;
	}

	public boolean login(String email, String password) {
		Logger.debug("Performing Login: {}", email);
		int siteid = foundation.getSiteID();
		MemberBase member = mapper.getUserByEmail(email.toLowerCase(), siteid);
		if (member != null && password != null
				&& crypto.validateHash(password, member.getCpasswd())) {
			forceLogin(member);
			// 创建token 加上密码是为了当用户修改密码后让token失效
			String secure = Play.application().configuration()
					.getString("login.secure");
			if (secure == null) {
				secure = SECURE;
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

			String host = Context.current().request().getHeader("Host");
			Logger.debug("Host:{}", host);

			Integer id = member.getIid();
			String uuid = member.getCuuid();
			if (uuid == null) {
				uuid = UUIDGenerator.createAsString();
				mapper.updateUuidById(id, uuid);
			}

			if (host != null && host.indexOf(HOST) != -1) {
				Context.current()
						.response()
						.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/",
								DOMAIN);

				Context.current()
						.response()
						.setCookie("TT_UUID", uuid, 365 * 24 * 3600, "/",
								DOMAIN);
			} else {
				Context.current().response()
						.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/");

				Context.current().response()
						.setCookie("TT_UUID", uuid, 365 * 24 * 3600, "/");
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param member
	 */
	public void forceLogin(MemberBase member) {
		LoginEvent event = new LoginEvent(
				foundation.getLoginContext().getLTC(), foundation
						.getLoginContext().getSTC(), foundation.getClientIP(),
				foundation.getSiteID(), member.getCemail().toLowerCase(),
				foundation.getDevice());
		eventBus.post(event);
		executeLoginProcess(event);
	}

	public void executeLoginProcess(LoginEvent event) {
		for (ILoginProcess ss : loginProcess) {
			ss.execute(event);
		}
	}

	public void logout() {

		String host = Context.current().request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(HOST) != -1) {
			Context.current().response().discardCookie("TT_TOKEN", "/", DOMAIN);

			Context.current().response().discardCookie("TT_UUID", "/", DOMAIN);
		} else {
			Context.current().response().discardCookie("TT_TOKEN", "/");

			Context.current().response().discardCookie("TT_UUID", "/");
		}

	}

	private LoginContext getLoginCtx(WebContext webContext) {
		String uuid = null;
		String token = null;
		String country = null;
		String currency = null;
		if (webContext != null) {
			WebCookie uuidCookie = webContext.cookie("TT_UUID");
			WebCookie tokenCookie = webContext.cookie("TT_TOKEN");
			WebCookie countryCookie = webContext
					.cookie(FoundationService.COOKIE_COUNTRY);
			WebCookie currencyCookie = webContext
					.cookie(FoundationService.COOKIE_CURRENCY);
			if (uuidCookie != null) {
				uuid = uuidCookie.value();
			}
			if (tokenCookie != null) {
				token = tokenCookie.value();
			}
			if (countryCookie != null) {
				country = countryCookie.value();
			}
			if (currencyCookie != null) {
				currency = currencyCookie.value();
			}
		} else {
			Cookie uuidCookie = Context.current().request().cookie("TT_UUID");
			Cookie tokenCookie = Context.current().request().cookie("TT_TOKEN");

			Cookie countryCookie = Context.current().request()
					.cookie(FoundationService.COOKIE_COUNTRY);
			Cookie currencyCookie = Context.current().request()
					.cookie(FoundationService.COOKIE_CURRENCY);

			if (uuidCookie != null) {
				uuid = uuidCookie.value();
			}
			if (tokenCookie != null) {
				token = tokenCookie.value();
			}

			if (countryCookie != null) {
				country = countryCookie.value();
			}
			if (currencyCookie != null) {
				currency = currencyCookie.value();
			}
		}

		LoginContext ctx = null;
		if (uuid == null || token == null) {
			ctx = PlayLoginContextFactory.newAnonymousLoginContext();
			ctx.setCountryCode(country);
			ctx.setCurrencyCode(currency);
			return ctx;
		}

		MemberBase member = this.mapper.getUserByUuid(uuid);
		if (member == null) {
			ctx = PlayLoginContextFactory.newAnonymousLoginContext();
			ctx.setCountryCode(country);
			ctx.setCurrencyCode(currency);
			return ctx;
		}

		String email = member.getCemail().toLowerCase();
		String passwd = member.getCpasswd();

		String secure = Play.application().configuration()
				.getString("login.secure");

		if (secure == null) {
			secure = SECURE;
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

			String first = member.getCfirstname();
			String mid = member.getCmiddlename();
			String last = member.getClastname();
			String name = (first!=null?first:"")+(mid!=null?" "+mid:"")+(last!=null?" "+last:"");
			MemberInSession mis = MemberInSession.newInstance(member.getIid(),
					StringUtils.isEmpty(name) ? "" : name.toLowerCase(), member.getCemail()
							.toLowerCase(), null);
			ctx = PlayLoginContextFactory.newLoginContext(email,
					member.getIgroupid(), mis);

			ctx.setCountryCode(country);
			ctx.setCurrencyCode(currency);
			return ctx;
		}

		ctx = PlayLoginContextFactory.newAnonymousLoginContext();

		ctx.setCountryCode(country);
		ctx.setCurrencyCode(currency);

		return ctx;

	}

	@Override
	public LoginContext getLoginContext() {
		return this.getLoginCtx(null);
	}

	@Override
	public LoginContext getLoginContext(WebContext webContext) {
		return this.getLoginCtx(webContext);
	}

	/**
	 * 当用户登录时来验证账号密码,如果认真通过则会返回一个token,然后保存token到cookie来标示用户已经登录
	 * 
	 * @param email
	 * @param password
	 * @return null 验证通过
	 */
	public String getToken(MemberBase member) {
		if (member == null || member.getCemail() == null
				|| member.getCpasswd() == null) {
			return null;
		}

		// 创建token 加上密码是为了当用户修改密码后让token失效
		String secure = Play.application().configuration()
				.getString("login.secure");
		if (secure == null) {
			secure = SECURE;
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
			member.setCuuid(uuid);
			mapper.updateUuidById(id, uuid);
		}

		return token;
	}
}
