package services.member.login;

import java.util.Date;
import java.util.List;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import valueobjects.member.MemberInSession;
import context.WebContext;
import dto.member.MemberBase;
import dto.member.MemberLoginHistory;
import dto.member.login.IThirdLoginParameter;
import events.member.LoginEvent;

public interface ILoginService {

	public abstract boolean login(String email, String password,
			WebContext context);

	/**
	 * Login using other identity (paypal, facebook)
	 * 
	 * @param email
	 * @param source
	 * @param userId
	 */
	boolean login(String email, String source, String userId, WebContext context);

	/**
	 * Force a login without knowing the password.
	 * 
	 * @param member
	 */
	void forceLogin(MemberBase member);

	void logout();

	MemberInSession getLoginData();

	MemberInSession getLoginData(Context context);

	String getLoginEmail();

	String getLoginEmail(Context context);

	List<Html> getOtherLoginButtons();

	List<MemberLoginHistory> getLoginHistoryByDate(int siteID, String email,
			Date when);

	/**
	 * 验证密码
	 * 
	 * @author lijun
	 * @param email
	 * @param password
	 * @param context
	 * @return
	 */
	public boolean authentication(String email, String password,
			WebContext context);

	public void executeLoginProcess(LoginEvent event);

	public OtherLoginResult thirdGoogleLogin(
			IThirdLoginParameter thirdLoginParameter,String appId,
			String appSecret, WebContext content);

	public OtherLoginResult thirdFaceLogin(
			IThirdLoginParameter thirdLoginParameter, String appId,
			String appSecret, WebContext content);
}