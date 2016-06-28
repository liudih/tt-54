package services.member;

import java.util.Map;

import play.libs.F.Promise;
import context.WebContext;

public interface IMemberEmailService {

	/**
	 * 邮箱认证
	 * 
	 * @param toemail
	 *            发送邮箱
	 * @return 返回Map<String, Object>散列集 ，键surplus表示今天还能发多少封邮件,键status
	 *         发送邮件状态,成功为1,今天以发完邮箱为0,失败为-1,以认证为-4
	 */

	public abstract Promise<Map<String, Object>> asyncVerify(String toemail,
			String url, String code, WebContext context);

	/**
	 * 用户邮箱激活
	 * 
	 * @param activationcode
	 * @return
	 */
	public abstract int activation(String activationcode, WebContext context);

	public abstract boolean sendEmailForSubscribe(String toeamil,
			String categoryLinks);

	public abstract boolean sendDropShipResultEmail(String toemail,
			Integer istatus, Integer websiteId, Integer language,
			WebContext context);

	public abstract boolean sendWholeSaleResultEmail(String toemail,
			Integer istatus, Integer websiteId, Integer language,
			WebContext context);

	/**
	 * 新用户注册后发送验证邮件到邮箱
	 * 
	 * @author lijun
	 * @param email
	 *            要发送邮件的邮箱
	 * @param monitorUrl
	 *            也就是邮件中用户点击的激活url
	 * @param context
	 * @return true : 邮件发送成功
	 */
	public boolean sendValideEmail(String email, String monitorUrl,
			WebContext context);

	/**
	 * @author lijun
	 * @param email
	 * @param monitorUrl
	 * @return
	 */
	public boolean sendValideEmail(String email, String monitorUrl);

	public boolean sendReplyMemberFaqEmail(String toemail, String title,
			String answer, Integer websiteId, Integer language);

	public Map<String, Object> verifyForWeb(String toemail, String url,
			String code, WebContext context);

	public boolean sendPrizeEmail(String email, String title, String context,
			WebContext webcontext);
	
	public boolean sendIntegralEmail(String email,Integer interal,WebContext context);

}