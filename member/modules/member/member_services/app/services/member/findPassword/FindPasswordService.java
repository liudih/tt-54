package services.member.findPassword;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import mapper.member.ForgetPasswdBaseMapper;
import mapper.member.MemberBaseMapper;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.F.Tuple;
import play.libs.F.Tuple3;
import services.ISystemParameterService;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.base.FoundationService;
import services.common.UUIDGenerator;
import services.member.MemberEmailService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import base.util.mail.EmailUtil;
import base.util.md5.MD5;

import com.google.common.collect.Maps;

import context.WebContext;
import dto.EmailAccount;
import dto.member.ForgetPasswdBase;
import email.util.EmailSpreadUtil;
import emailobjects.FindPassEmailModel;

public class FindPasswordService implements IFindPasswordService {

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Inject
	EmailAccountService emailAccountService;

	@Inject
	FoundationService foundationService;

	@Inject
	EmailTemplateService emailTemplateService;

	@Inject
	IForgetPasswdBaseService forgetPasswdService;

	@Inject
	ISystemParameterService parameterService;

	@Inject
	IForgetPasswdBaseService forgetPasswdBaseService;

	@Inject
	ForgetPasswdBaseMapper forgetPasswdBaseMapper;
	
	@Inject
	EmailSpreadUtil emailSpread;

	final int hasReset = 1;

	final int timeOut = 2;

	// 由于新的验证码会取代旧的验证码导致失效
	final int EXPIRE = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.findPassword.IFindPasswordService#asyncFindPass(java.
	 * lang.String, java.lang.String, java.lang.Integer)
	 */
	public Promise<F.Tuple<Integer, Integer>> asyncFindPass(
			final String toemail, String url, WebContext webContext) {
		Integer languageid = foundationService.getSiteID(webContext);
		Integer websiteId = foundationService.getLanguage(webContext);
		return Promise.promise(new Function0<F.Tuple<Integer, Integer>>() {
			public F.Tuple<Integer, Integer> apply() {
				return findPass(toemail, url, websiteId, languageid);
			}
		});

	}

	public boolean asyncFindPassword(final String toemail, String url,
			WebContext webContext) {
		Integer websiteId = foundationService.getSiteID(webContext);
		Integer languageid = foundationService.getLanguage(webContext);
		Tuple<Integer, Integer> findPass = findPass(toemail, url, websiteId,
				languageid);
		int result = findPass._1.intValue();

		return result == 1 ? true : false;
	}

	private F.Tuple<Integer, Integer> findPass(String toemail, String url,
			Integer websiteId, Integer languageId) {
		return sendEmailFindpass(toemail, url, websiteId, languageId);
	}

	@SuppressWarnings("deprecation")
	private F.Tuple<Integer, Integer> sendEmailFindpass(String toemail,
			String url, Integer websiteId, Integer language) {
		url = URLDecoder.decode(url);
		int status = 0;
		int surplus = 0;
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(MemberEmailService.MAIL_CTYPE);
		String uid = UUIDGenerator.createAsString().replace("-", "");
		Timestamp now = new Timestamp(System.currentTimeMillis());
		long date = now.getTime();// 忽略毫秒数
		String sid = date + uid;
		String cid = MD5.md5(sid);// 数字签名
		String code = randomNumber(6);
		ForgetPasswdBase users = new ForgetPasswdBase();
		users.setCid(cid);
		users.setIwebsiteid(websiteId);
		users.setCmemberemail(toemail);
		users.setCrandomcode(code);

		forgetPasswdService.insert(users); // 保存到数据库

		url = StringUtils.replace(url, "[cid]", cid);
		FindPassEmailModel model = new FindPassEmailModel(
				email.model.EmailType.UPDATE_PASS.getType(), language, toemail,
				url, code);

		Logger.debug("model======>" + model.toString());

		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(model,
					websiteId);

			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("title and content is null ,can not send email");
				return new F.Tuple<Integer, Integer>(status, surplus);
			}
		} catch (Exception e) {
			Logger.error("can not deal with verify email content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendEmailVerify  email server account null!");
			return new F.Tuple<Integer, Integer>(status, surplus);
		}
		boolean sentResult = emailSpread.send(emailaccount.getCemail(),
				toemail, title, content);
//				EmailUtil.send(title, content, emailaccount,
//				toemail);
		status = sentResult ? 1 : 0;
		return new F.Tuple<Integer, Integer>(status, surplus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.findPassword.IFindPasswordService#resetPassValide(java
	 * .lang.String)
	 */
	@Override
	public F.Tuple3<Integer, Integer, Integer> resetPassValide(String cid,
			WebContext webContext) {
		Integer languageid = foundationService.getSiteID(webContext);
		Integer websiteId = foundationService.getLanguage(webContext);
		String email = forgetPasswdService.getEmail(cid);
		Boolean buse = forgetPasswdBaseMapper.getBuseByCidAndEmail(cid, email);
		if (buse == null) {
			return new F.Tuple3<Integer, Integer, Integer>(EXPIRE, 0, 0);
		}
		if (buse == false) {
			return new F.Tuple3<Integer, Integer, Integer>(0, hasReset, 0);
		}

		String days = parameterService.getSystemParameter(languageid,
				websiteId, "FindPassValideDays");
		int day = Integer.valueOf(days).intValue();
		Date nowDate = new Date();
		Date validate = forgetPasswdBaseMapper.getDcreatedateByCid(cid);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(validate);
		calendar.add(Calendar.DATE, day);
		validate = calendar.getTime();

		if (nowDate.getTime() > validate.getTime()) {
			return new F.Tuple3<Integer, Integer, Integer>(0, 0, timeOut);
		}

		return new F.Tuple3<Integer, Integer, Integer>(0, 0, 0);
	}

	private String randomNumber(int length) {
		Random random = new Random();
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buff.append(random.nextInt(10));

		}
		return buff.toString();
	}

	@Override
	public int resetPasswordValide(String cid, WebContext context) {
		Tuple3<Integer, Integer, Integer> tuple3 = resetPassValide(cid, context);
		if (tuple3._1 != 0) {
			return 1;
		} else if (tuple3._3 != 0) {
			return 2;
		} else if (tuple3._2 != 0) {
			return 3;
		}

		return 0;
	}

}
