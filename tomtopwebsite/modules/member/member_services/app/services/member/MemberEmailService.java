package services.member;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import mapper.member.MemberBaseMapper;
import mapper.member.MemberEmailVerifyMapper;
import play.Logger;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import services.IEmailAccountService;
import services.IEmailTemplateService;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.base.utils.StringUtils;
import services.common.UUIDGenerator;
import base.util.md5.MD5;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import context.ContextUtils;
import context.WebContext;
import dto.EmailAccount;
import dto.EmailTemplate;
import dto.member.MemberBase;
import dto.member.MemberEmailVerify;
import email.model.EmailType;
import email.model.IEmailModel;
import email.util.EmailSpreadUtil;
import emailobjects.ActivateEmailModel;
import emailobjects.GetPrizeEmailModel;
import emailobjects.IntegralEmailModel;
import emailobjects.JoinDropShipFailureEmailModel;
import emailobjects.JoinDropShipSuccessEmailModel;
import emailobjects.JoinWholeSaleFailureEmailModel;
import emailobjects.JoinWholeSaleSuccessEmailModel;
import emailobjects.ReplyMemberFaqEmailModel;
import events.member.ActivationEvent;

public class MemberEmailService implements IMemberEmailService {

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Inject
	MemberEmailVerifyMapper emailVerifiMapper;

	@Inject
	IEmailAccountService emailAccountService;

	@Inject
	FoundationService foundationService;

	@Inject
	IEmailTemplateService emailTemplateService;

	@Inject
	EventBus eventBus;
	
	@Inject
	EmailSpreadUtil emailSpread;

	final int permitNumber = 3;

	final int once = 1;

	final int success = 1;

	final int faild = -1;

	final int inActivate = -2;

	final int timeInvalid = -3;

	final int codeInvalid = -4;

	// 默认发送邮件协议
	public static final String MAIL_CTYPE = "smtp";

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.IMemberEmailService#asyncVerify(java.lang.String,
	 * java.lang.String, java.lang.String, context.WebContext)
	 */

	public Promise<Map<String, Object>> asyncVerify(final String toemail,
			final String url, final String code, WebContext context) {

		return Promise.promise(new Function0<Map<String, Object>>() {
			public Map<String, Object> apply() {
				return verify(toemail, url, code, context);
			}
		});

	}

	public Map<String, Object> verifyForWeb(String toemail, String url,
			String code, WebContext context) {
		return verify(toemail, url, code, context);
	}

	private Map<String, Object> verify(String toemail, String url, String code,
			WebContext context) {
		int surplus = 0;
		int status = 0;

		Map<String, Object> resultMap = new HashMap<String, Object>();
		int site = foundationService.getSiteID(context);
		MemberBase member = memberBaseMapper.getUserByEmail(toemail, site);

		if (member.isBactivated()) {
			resultMap.put("surplus", surplus);
			resultMap.put("status", inActivate);
			return resultMap;
		}

		String mark = MD5.md5(DateFormatUtils.getDate());

		MemberEmailVerify emailVeifi = emailVerifiMapper.getByEmail(toemail);
		if (null != emailVeifi) {
			emailVeifi.setIresendcount(null==emailVeifi.getIresendcount()?0:emailVeifi.getIresendcount() + 1);
			status = updateVerify(emailVeifi, code, mark, once) ? success
					: faild;
			sendEmailVerify(code, toemail, url);
			resultMap.put("surplus", surplus);
			resultMap.put("status", status);
			return resultMap;
		}else{
			status = addVerify(toemail, code, mark) ? success : faild;
			sendEmailVerify(code, toemail, url);
			resultMap.put("surplus", surplus);
			resultMap.put("status", status);
			return resultMap;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.IMemberEmailService#activation(java.lang.String,
	 * context.WebContext)
	 */
	public int activation(String activationcode, WebContext context) {
		MemberEmailVerify emailVeifi = emailVerifiMapper
				.getActivationCode(activationcode);

		if (!(emailVeifi != null && !StringUtils
				.isEmpty(emailVeifi.getCemail()))) {
			return codeInvalid;
		}

		Date validate = emailVeifi.getDvaliddate();
		long currentTime = System.currentTimeMillis();

		if (currentTime > validate.getTime()) {
			return timeInvalid;
		}
		int site = foundationService.getSiteID(context);
		MemberBase member = memberBaseMapper.getUserByEmail(
				emailVeifi.getCemail(), site);

		if (member.isBactivated()) {
			return inActivate;
		}

		member.setBactivated(true);
		int result = memberBaseMapper.updateByPrimaryKey(member);
		if (result > 0) {
			int siteId = foundationService.getSiteID();
			String ip = foundationService.getClientIP();
			String ltc = foundationService.getLoginContext().getLTC();
			String stc = foundationService.getLoginContext().getSTC();
			int language = foundationService.getLanguage();
			String email = emailVeifi.getCemail();
			ActivationEvent mb = new ActivationEvent(ltc, stc, ip, siteId,
					email, language);
			eventBus.post(mb);
		}
		return result > 0 ? success : faild;
	}

	public int activationForMobile(String email, String activationcode,
			WebContext context) {
		MemberEmailVerify emailVeifi = emailVerifiMapper
				.getActivationCode(activationcode);

		if (emailVeifi == null) {
			return codeInvalid;
		}

		Date validate = emailVeifi.getDvaliddate();
		long currentTime = System.currentTimeMillis();

		if (currentTime > validate.getTime()) {
			return timeInvalid;
		}
		int site = foundationService.getSiteID(context);
		MemberBase member = memberBaseMapper.getUserByEmail(
				emailVeifi.getCemail(), site);

		if (member.isBactivated()) {
			return inActivate;
		}

		member.setBactivated(true);
		int result = memberBaseMapper.updateByPrimaryKey(member);
		if (result == 1) {
			// add by lijun to send event
			int siteId = foundationService.getSiteID();
			String ip = foundationService.getClientIP();
			String ltc = foundationService.getLoginContext().getLTC();
			String stc = foundationService.getLoginContext().getSTC();
			int language = foundationService.getLanguage();
			ActivationEvent mb = new ActivationEvent(ltc, stc, ip, siteId,
					email, language);
			eventBus.post(mb);
		}
		return result > 0 ? success : faild;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.IMemberEmailService#sendEmailForSubscribe(java.lang.String
	 * , java.lang.String)
	 */
	public boolean sendEmailForSubscribe(String toeamil, String categoryLinks) {
		Integer language = foundationService.getLanguage();
		int websiteid = foundationService.getPlatformID();
		EmailTemplate template = emailTemplateService
				.getEmailTemplateBylangAndSiteAndType(language, websiteid,
						EmailType.WELCOME_SUBSCIBE.getType());
		if (template == null) {
			return false;
		}
		String bodyhtml = template.getCcontent();
		bodyhtml = bodyhtml.replace("{0}", categoryLinks);
		EmailAccount account = emailAccountService.getEmailAccount(websiteid);
		boolean result = emailSpread.send(account.getCemail(), toeamil,
				template.getCtitle(), bodyhtml);
//		 EmailUtil.send(template.getCtitle(), bodyhtml,
//		 account, toeamil);
		return result;
	}

	private boolean sendEmailVerify(String code, String toemail, String url) {
		Integer language = foundationService.getLanguage();
		// 根据邮件协议查找邮箱账户
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(MAIL_CTYPE);
		ActivateEmailModel activateEmailModel = new ActivateEmailModel(
				email.model.EmailType.ACTIVATE.getType(), language, toemail,
				url);
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService
					.getEmailContent(activateEmailModel);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with verify email content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendEmailVerify  email server account null!");
			return false;
		}
		return emailSpread.send(emailaccount.getCemail(), toemail, title, content);
	}

	private boolean addVerify(String toemail, String code, String mark) {
		MemberEmailVerify emailVeifi = new MemberEmailVerify();
		emailVeifi.setCemail(toemail);
		emailVeifi.setCactivationcode(code);
		emailVeifi.setDsenddate(new Date());
		emailVeifi.setDvaliddate(DateFormatUtils.getNowBeforeByDay(
				Calendar.DAY_OF_MONTH, 3));
		emailVeifi.setIdaynumber(1);
		emailVeifi.setBisending(true);
		emailVeifi.setCmark(mark);
		int record = emailVerifiMapper.insert(emailVeifi);
		return record > 0 ? true : false;
	}

	private boolean updateVerify(MemberEmailVerify emailVeifi, String code,
			String mark, int number) {
		emailVeifi.setDvaliddate(DateFormatUtils.getNowBeforeByDay(
				Calendar.DAY_OF_MONTH, 3));
		emailVeifi.setCactivationcode(code);
		emailVeifi.setDsenddate(new Date());
		emailVeifi.setCmark(mark);
		emailVeifi.setBisending(true);
		//emailVeifi.setIdaynumber(number);
		emailVeifi.setIresendcount(emailVeifi.getIresendcount());
		int record = emailVerifiMapper.updateByPrimaryKey(emailVeifi);
		return record > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.IMemberEmailService#sendDropShipResultEmail(java.lang
	 * .String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * context.WebContext)
	 */
	public boolean sendDropShipResultEmail(String toemail, Integer istatus,
			Integer websiteId, Integer language, WebContext context) {
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteId);
		int site = foundationService.getSiteID(context);
		MemberBase member = memberBaseMapper.getUserByEmail(toemail, site);
		String firstname = member.getCfirstname();
		if (StringUtils.isEmpty(firstname)) {
			firstname = toemail;
		}
		IEmailModel activateEmailModel = null;
		if (1 == istatus) {
			activateEmailModel = new JoinDropShipSuccessEmailModel(
					email.model.EmailType.Join_Drop_shipping.getType(),
					language, firstname);
		} else if (2 == istatus) {
			activateEmailModel = new JoinDropShipFailureEmailModel(
					email.model.EmailType.Join_Drop_shipping_Failure.getType(),
					language, firstname);
		} else {
			return false;
		}
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					activateEmailModel, websiteId);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0
					&& !StringUtils.isEmpty(titleAndContentMap.get("title"))
					&& !StringUtils.isEmpty(titleAndContentMap.get("content"))) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("sendDropShipResultEmail title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with dropship apply email content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendDropShipResultEmail email server account null!");
			return false;
		}
		return emailSpread.send(emailaccount.getCemail(), toemail, title, content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.IMemberEmailService#sendWholeSaleResultEmail(java.lang
	 * .String, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * context.WebContext)
	 */
	public boolean sendWholeSaleResultEmail(String toemail, Integer istatus,
			Integer websiteId, Integer language, WebContext context) {
		int site = foundationService.getSiteID(context);
		MemberBase member = memberBaseMapper.getUserByEmail(toemail, site);
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteId);
		String firstname = member.getCfirstname();
		if (StringUtils.isEmpty(firstname)) {
			firstname = toemail;
		}
		IEmailModel activateEmailModel = null;
		if (1 == istatus) {
			activateEmailModel = new JoinWholeSaleSuccessEmailModel(
					email.model.EmailType.JOIN_WHOLESALE_SUCCESS.getType(),
					language, firstname);
		} else if (2 == istatus) {
			activateEmailModel = new JoinWholeSaleFailureEmailModel(
					email.model.EmailType.JOIN_WHOLESALE_FAILUR.getType(),
					language, firstname);
		} else {
			return false;
		}
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					activateEmailModel, websiteId);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0
					&& !StringUtils.isEmpty(titleAndContentMap.get("title"))
					&& !StringUtils.isEmpty(titleAndContentMap.get("content"))) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("sendWholeSaleResultEmail title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with wholesale apply email content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendWholeSaleResultEmail email server account null!");
			return false;
		}
		return emailSpread.send(emailaccount.getCemail(), toemail, title, content);
	}

	@Override
	public boolean sendValideEmail(String email, String monitorUrl,
			WebContext context) {
		if (context == null) {
			throw new NullPointerException("WebContext is null");
		}

		if (monitorUrl == null || monitorUrl.length() == 0) {
			throw new NullPointerException("monitorUrl is null");
		}
		String code = UUIDGenerator.createAsString().replace("-", "");
		// 把email code 参数加到monitorUrl后面
		if (monitorUrl.indexOf("?") == -1) {
			monitorUrl = monitorUrl + "?email=" + email;
			monitorUrl = monitorUrl + "&code=" + code;
		} else {
			monitorUrl = monitorUrl + "&email=" + email;
			monitorUrl = monitorUrl + "&code=" + code;
		}

		Map<String, Object> result = this.verify(email, monitorUrl, code,
				context);
		Object status = result.get("status");
		if (status != null && "1".equals(status)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean sendValideEmail(String email, String monitorUrl) {
		WebContext webCtx = ContextUtils.getWebContext(Context.current());
		return this.sendValideEmail(email, monitorUrl, webCtx);
	}

	@Override
	public boolean sendReplyMemberFaqEmail(String toemail, String question,
			String answer, Integer websiteId, Integer language) {
		MemberBase member = memberBaseMapper.getUserByEmail(toemail, websiteId);
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteId);
		String firstname = toemail;
		if (null != member && !StringUtils.isEmpty(member.getCfirstname())) {
			firstname = member.getCfirstname();
		}
		IEmailModel activateEmailModel = new ReplyMemberFaqEmailModel(
				email.model.EmailType.REPLY_MEMBER_FAQ_ANSWER.getType(),
				language, firstname, question, answer);
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					activateEmailModel, websiteId);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0
					&& !StringUtils.isEmpty(titleAndContentMap.get("title"))
					&& !StringUtils.isEmpty(titleAndContentMap.get("content"))) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("sendReplyMemberFaqEmail title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with sendReplyMemberFaqEmail content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendReplyMemberFaqEmail email server account null!");
			return false;
		}

		return emailSpread.send(emailaccount.getCemail(), toemail, title, content);
	}

	@Override
	public boolean sendPrizeEmail(String toemail, String ctitle,
			String dcontext, WebContext webcontext) {
		int websiteId = foundationService.getSiteID(webcontext);
		int language = foundationService.getLanguage(webcontext);
		MemberBase member = memberBaseMapper.getUserByEmail(toemail, websiteId);
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteId);
		String firstname = toemail;
		if (null != member && !StringUtils.isEmpty(member.getCfirstname())) {
			firstname = member.getCfirstname();
		}
		IEmailModel prizeEmailModel = new GetPrizeEmailModel(
				email.model.EmailType.SEND_PRIZE.getType(), language, ctitle,
				dcontext, firstname);
		String content = "";
		String title = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					prizeEmailModel, websiteId);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0
					&& !StringUtils.isEmpty(titleAndContentMap.get("title"))
					&& !StringUtils.isEmpty(titleAndContentMap.get("content"))) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("sendPrizeEmail title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with sendPrizeEmail content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendPrizeEmail email server account null!");
			return false;
		}
		return emailSpread.send(emailaccount.getCemail(), toemail, title, content);
		//return EmailUtil.send(title, content, emailaccount, toemail);
	}

	@Override
	public boolean sendIntegralEmail(String toemail, Integer interal,
			WebContext webcontext) {
		int websiteId = foundationService.getSiteID(webcontext);
		int language = foundationService.getLanguage(webcontext);
		MemberBase member = memberBaseMapper.getUserByEmail(toemail, websiteId);
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteId);
		String firstname = toemail;
		if (null != member && !StringUtils.isEmpty(member.getCfirstname())) {
			firstname = member.getCfirstname();
		}
		IEmailModel prizeEmailModel = new IntegralEmailModel(
				EmailType.SEND_INTEGRAL.getType(), language, toemail, interal,
				firstname);
		String content = "";
		String title = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					prizeEmailModel, websiteId);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0
					&& !StringUtils.isEmpty(titleAndContentMap.get("title"))
					&& !StringUtils.isEmpty(titleAndContentMap.get("content"))) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("sendIntegralEmail title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with sendIntegralEmail content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendIntegralEmail email server account null!");
			return false;
		}
		return emailSpread.send(emailaccount.getCemail(), toemail, title, content);
		//return EmailUtil.send(title, content, emailaccount, toemail);
	}
	
	@Override
	public boolean deleteByEmail(String email){
		int result = emailVerifiMapper.deleteByEmail(email);
		return result>0?true:false;
	}

}
