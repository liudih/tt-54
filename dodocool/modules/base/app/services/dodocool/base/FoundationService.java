package services.dodocool.base;

import play.Logger;
import play.libs.Json;
import play.mvc.Http.Context;
import services.IFoundationService;
import services.ILanguageService;
import session.ISessionService;
import valueobjects.base.CommonLoginContextFactory;
import valueobjects.base.LoginContext;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.Country;
import dto.Language;
import extensions.InjectorInstance;

public class FoundationService {

	@Inject
	ILanguageService languageService;

	@Inject
	IFoundationService foundationService;

	@Inject
	ISessionService sessionService;

	public static final String LOGIN_SESSION_NAME = "LOGIN_CONTEXT_DODOCOOL";

	public int getLanguageId() {
		return foundationService.getLanguage(this.getWebContext());
	}

	public Integer getSiteID() {
		WebContext wc = this.getWebContext();
		Logger.debug("webcontenx  new-> {}",
				Json.toJson(wc));
		return foundationService.getSiteID(wc);
	}

	public String getCurrency() {
		return foundationService.getCurrency(this.getWebContext());
	}

	public Language _getLanguage() {
		Integer languageId = this.getLanguageId();
		return languageService.getLanguage(languageId);
	}

	public static Language _getLanguageObj() {
		return InjectorInstance.getInstance(FoundationService.class)
				._getLanguage();
	}

	public LoginContext getLoginservice() {
		WebContext webCtx = this.getWebContext();
		LoginContext ctx = (LoginContext) sessionService.get(
				LOGIN_SESSION_NAME, webCtx);
		if (ctx == null) {
			ctx = CommonLoginContextFactory.newAnonymousLoginContext(webCtx);
			sessionService.set(LOGIN_SESSION_NAME, ctx, webCtx);
		}
		return ctx;
	}

	public boolean isLogined() {
		LoginContext loginContext = this.getLoginservice();
		if (null != loginContext && null != loginContext.getMemberID()) {
			return true;
		}
		return false;
	}

	public Country getCountryObj() {
		return foundationService.getCountryObj(this.getWebContext());
	}

	public String getClientIP() {
		return Context.current().request().remoteAddress();
	}

	public WebContext getWebContext() {
		return ContextUtils.getWebContext(Context.current());
	}

}
