package services.member.registration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import mapper.member.MemberBaseMapper;
import mapper.member.MemberEmailVerifyMapper;
import mapper.member.MemberOtherIdMapper;
import play.Logger;
import play.Play;
import play.libs.F.Function0;
import play.libs.F.Promise;
import services.IEmailAccountService;
import services.IEmailTemplateService;
import services.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.MemberEmailService;
import services.member.login.CryptoUtils;
import services.member.login.ILoginService;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import valueobjects.member.ResultType;
import base.util.mail.EmailUtil;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import context.WebContext;
import email.util.EmailSpreadUtil;
import emailobjects.RegEmailModel;
import dto.EmailAccount;
import dto.member.MemberBase;
import dto.member.MemberEmailVerify;
import dto.member.MemberOtherId;
import events.member.ActivationEvent;
import events.member.RegistrationEvent;

public class MemberRegistrationService implements IMemberRegistrationService {
	@Inject
	IMemberEnquiryService enquiry;

	@Inject
	MemberBaseMapper mapper;

	@Inject
	MemberOtherIdMapper otherIdMapper;

	@Inject
	MemberEmailVerifyMapper emailVerifiMapper;

	@Inject
	CryptoUtils crypto;

	@Inject
	EventBus eventBus;

	@Inject
	FoundationService foundation;

	@Inject
	ILoginService loginService;

	@Inject
	IEmailAccountService emailAccountService;

	@Inject
	IEmailTemplateService emailTemplateService;
	
	@Inject
	EmailSpreadUtil emailSpread;

	final int codeInvalid = -4;

	final int inActivate = -2;

	final int success = 1;

	final int faild = -1;

	// 设置邮箱验证邮件过期时间为3天
	final int MAIL_VERIFY_TIME = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.registration.IMemberRegistrationService#register(valueobjects
	 * .member.MemberRegistration, boolean)
	 */
	@Override
	public MemberRegistrationResult register(MemberRegistration reg,
			boolean activated, WebContext context) {
		MemberBase member = enquiry.getMemberByMemberEmail(reg.getEmail()
				.toLowerCase(), context);
		if (member != null) {
			if (member.isBactivated()) {
				return new MemberRegistrationResult(reg, false,
						ResultType.MEMBER_EXISTS, null);
			}
			return new MemberRegistrationResult(reg, false,
					ResultType.MEMBER_NOT_ACTIVATED, null);
		}
		member = new MemberBase();
		member.setCemail(reg.getEmail().toLowerCase());
		member.setCpasswd(crypto.getHash(reg.getPassword(), 2));
		member.setIgroupid(1); // default 1
		member.setCcountry(reg.getCountry());
		member.setBnewsletter(reg.isReceiveNewsletter());
		member.setBactivated(activated);
		member.setCvhost(foundation.getVhost());
		member.setDcreatedate(new Date());
		member.setIwebsiteid(foundation.getSiteID(context));
		member.setCfirstname(reg.getFirstname());
		member.setClastname(reg.getLastname());

		mapper.insertSelective(member);
		// 同时插入到验证邮箱表
		MemberEmailVerify emailVerify = new MemberEmailVerify();
		emailVerify.setCemail(reg.getEmail().toLowerCase());
		emailVerify.setIdaynumber(MAIL_VERIFY_TIME);
		emailVerifiMapper.insert(emailVerify);
		// fire event
		eventBus.post(new RegistrationEvent(foundation.getLoginContext()
				.getLTC(), foundation.getLoginContext().getSTC(), foundation
				.getClientIP(), foundation.getSiteID(), reg.getEmail()
				.toLowerCase()));

		Logger.debug("Member {} Registration Done!", reg.getEmail());
		return new MemberRegistrationResult(reg, true, ResultType.SUCCESS, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.registration.IMemberRegistrationService#register(valueobjects
	 * .member.MemberRegistration, valueobjects.member.MemberOtherIdentity)
	 */
	@Override
	public MemberRegistrationResult register(MemberRegistration reg,
			MemberOtherIdentity otherId, WebContext context) {
		MemberBase mb = enquiry.getMemberByOtherIdentity(otherId, context);
		if (mb != null) {
			if (mb.isBactivated()) {
				return new MemberRegistrationResult(reg, false,
						ResultType.MEMBER_EXISTS, null);
			}
			return new MemberRegistrationResult(reg, false,
					ResultType.MEMBER_NOT_ACTIVATED, null);
		}
		MemberRegistrationResult res = null;
		if (mapper.getUserByEmail(reg.getEmail().toLowerCase(),
				foundation.getSiteID(context)) == null) {
			// XXX activated for demo purpose, process not yet completed
			res = register(reg, true, context);
		} else {
			res = new MemberRegistrationResult(reg, true,
					ResultType.MEMBER_EXIST_LINKED, null);
		}
		MemberOtherId other = new MemberOtherId();
		other.setBvalidated(false);
		other.setCemail(otherId.getEmail().toLowerCase());
		other.setCsource(otherId.getSource());
		other.setCsourceid(otherId.getId());
		otherIdMapper.insert(other);
		Logger.debug("Member {} Registration done by {}", reg.getEmail(),
				otherId.getSource());
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.registration.IMemberRegistrationService#resendActivationEmail
	 * (java.lang.String)
	 */
	@Override
	public boolean resendActivationEmail(String email) {
		MemberEmailVerify emailVeifi = emailVerifiMapper.getByEmail(email);
		try {
			Integer resendtimes = Play.application().configuration()
					.getInt("member.register.resendemailtime");
			if (null == resendtimes) {
				Logger.error("The number of retransmissions limited read from the configuration file is empty!");
				return false;
			}
			if (null != emailVeifi) {
				Date dbSendDate = emailVeifi.getDsenddate();
				Date now = new Date();
				if (null != dbSendDate && dbSendDate.getYear() == now.getYear()
						&& dbSendDate.getMonth() == now.getMonth()
						&& dbSendDate.getDay() == now.getDay()) {

					if (null == emailVeifi.getIresendcount()) {

						emailVeifi.setIresendcount(0);
						emailVeifi.setDsenddate(new Date());
						emailVerifiMapper
								.updateByPrimaryKeySelective(emailVeifi);
						return true;
					}
					if (emailVeifi.getIresendcount() < resendtimes) {
						emailVeifi.setDsenddate(new Date());
						emailVerifiMapper.updateByPrimaryKeySelective(emailVeifi);
						return true;
					}
				} else {
					emailVeifi.setIresendcount(0);
					emailVeifi.setDsenddate(new Date());
					emailVerifiMapper
							.updateByPrimaryKeySelective(emailVeifi);
					return true;
				}
			} else {
				// 同时插入到验证邮箱表
				MemberEmailVerify newemailVerify = new MemberEmailVerify();
				newemailVerify.setCemail(email.toLowerCase());
				newemailVerify.setIdaynumber(MAIL_VERIFY_TIME);
				newemailVerify.setIresendcount(0);
				newemailVerify.setDsenddate(new Date());
				emailVerifiMapper.insert(newemailVerify);
				return true;
			}

		} catch (Exception e) {
			Logger.error(
					"From the configuration file to read the resendtimes failed",
					e);
			return false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.registration.IMemberRegistrationService#activate(java
	 * .lang.String)
	 */
	@Override
	public boolean activate(String activationCode) {
		MemberBase base = mapper
				.getNonActiveUserByActivationCode(activationCode);
		if (base != null) {
			base.setBactivated(true);
			if (mapper.updateByPrimaryKey(base) == 1) {
				Logger.debug("Member {} Activated", base.getCemail());

				// fire event
				eventBus.post(new ActivationEvent(foundation.getLoginContext()
						.getLTC(), foundation.getLoginContext().getSTC(),
						foundation.getClientIP(), foundation.getSiteID(), base
								.getCemail(), foundation.getLanguage()));

				loginService.forceLogin(base);
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.registration.IMemberRegistrationService#getEmail(java
	 * .lang.String)
	 */
	@Override
	public boolean getEmail(String email, WebContext context) {
		boolean n = true;
		MemberBase memberBase = mapper.getUserByEmail(email.toLowerCase(),
				foundation.getSiteID(context));
		if (memberBase == null) {
			n = false;
		}
		return n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.registration.IMemberRegistrationService#asyncRegSuccess
	 * (java.lang.String)
	 */

	@Override
	public Promise<Map<String, Object>> asyncRegSuccess(String toemail) {
		return Promise.promise(new Function0<Map<String, Object>>() {
			public Map<String, Object> apply() {
				return regSuccess(toemail);
			}
		});

	}

	private Map<String, Object> regSuccess(String toemail) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		MemberEmailVerify emailSuccess = emailVerifiMapper.getByEmail(toemail);
		if (emailSuccess != null) {
			sendEmailRegSuccess(toemail);
		}
		return resultMap;
	}

	private boolean sendEmailRegSuccess(String toemail) {
		Integer language = foundation.getLanguage();
		// 用邮件协议类型作为查询条件
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(MemberEmailService.MAIL_CTYPE);
		RegEmailModel regEmailModel = new RegEmailModel(
				email.model.EmailType.Activate_Success.getType(), language,
				toemail);
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService
					.getEmailContent(regEmailModel);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with register success email content");
			e.printStackTrace();
		}
		if (emailaccount == null) {
			Logger.info("sendEmailRegSuccess  email server account null!");
			return false;
		}
		// return EmailUtil.send(title, content, emailaccount, toemail);
		return emailSpread.send(emailaccount.getCemail(), toemail, title,
				content);
	}

}
