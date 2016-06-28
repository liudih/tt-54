package services.messaging;

import java.util.Map;

import javax.inject.Inject;

import mapper.member.MemberBaseMapper;
import mapper.member.MemberEmailVerifyMapper;
import play.Logger;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import valueobjects.messaging.JoinDropShipFailureMessagingModel;
import valueobjects.messaging.JoinDropShipSuccessMessagingModel;
import valueobjects.messaging.JoinWholeSaleFailureMessagingModel;
import valueobjects.messaging.JoinWholeSaleSuccessMessagingModel;

import com.google.common.collect.Maps;

import context.WebContext;
import dto.EmailAccount;
import dto.member.MemberBase;
import email.model.IEmailModel;
import entity.messaging.Broadcast;
import enums.messaging.Type;

public class MemberMessagingService {

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Inject
	MemberEmailVerifyMapper emailVerifiMapper;

	@Inject
	EmailAccountService emailAccountService;

	@Inject
	FoundationService foundationService;

	@Inject
	EmailTemplateService emailTemplateService;

	@Inject
	private IMessageService mService;

	public void sendWholeSaleResultMessaging(String toemail, Integer istatus,
			Integer websiteId, Integer language, Integer isendIid,
			WebContext context) {
		int site = foundationService.getSiteID(context);
		MemberBase member = memberBaseMapper.getUserByEmail(toemail, site);
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteId);
		String firstname = member.getCfirstname();
		if (StringUtils.isEmpty(firstname)) {
			firstname = toemail;
		}
		IEmailModel wholesaleMessaging = null;
		if (1 == istatus) {
			wholesaleMessaging = new JoinWholeSaleSuccessMessagingModel(
					email.model.EmailType.JOIN_WHOLESALE_SUCCESS_MESSAGING
							.getType(),
					language, firstname);
		} else if (2 == istatus) {
			wholesaleMessaging = new JoinWholeSaleFailureMessagingModel(
					email.model.EmailType.JOIN_WHOLESALE_FAILUR_MESSAGING
							.getType(),
					language, firstname);
		} else {
			return;
		}
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					wholesaleMessaging, websiteId);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0
					&& !StringUtils.isEmpty(titleAndContentMap.get("title"))
					&& !StringUtils.isEmpty(titleAndContentMap.get("content"))) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("wholesale title and content is null ,can not send messaging");
				return;
			}
		} catch (Exception e) {
			Logger.error("can not deal with verify wholesale messaging content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendWholeSaleResultMessaging server account null!");
			return;
		}
		Broadcast broadcast = new Broadcast();
		broadcast.setContent(content);
		broadcast.setEmail(toemail);
		broadcast.setWebsiteId(websiteId);
		broadcast.setType(Type.WHOLESALE.getCode());
		broadcast.setFrom("TOMTOP Team");
		broadcast.setSendId(isendIid);
		broadcast.setSubject(title);
		broadcast.setStatus(enums.messaging.Status.unread.getCode());
		broadcast.setSendMethod(enums.messaging.SendMethod.SYSTEM.getCode());
		int insert = mService.insert(broadcast);
		if (insert > 0) {
			Logger.info("sendWholeSaleResultMessaging successful!");
		} else {
			Logger.info("sendWholeSaleResultMessaging fair!");
		}
	}

	public void sendDropShipResultMessaging(String toemail, Integer istatus,
			Integer websiteId, Integer language, Integer isendIid,
			WebContext context) {
		int site = foundationService.getSiteID(context);
		MemberBase member = memberBaseMapper.getUserByEmail(toemail, site);
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteId);
		String firstname = member.getCfirstname();
		if (StringUtils.isEmpty(firstname)) {
			firstname = toemail;
		}
		IEmailModel wholesaleMessaging = null;
		if (1 == istatus) {
			wholesaleMessaging = new JoinDropShipSuccessMessagingModel(
					email.model.EmailType.JOIN_DROPSHIP_SUCCESS_MESSAGING
							.getType(),
					language, firstname);
		} else if (2 == istatus) {
			wholesaleMessaging = new JoinDropShipFailureMessagingModel(
					email.model.EmailType.JOIN_DROPSHIP_FAILUR_MESSAGING
							.getType(),
					language, firstname);
		} else {
			return;
		}
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					wholesaleMessaging, websiteId);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0
					&& !StringUtils.isEmpty(titleAndContentMap.get("title"))
					&& !StringUtils.isEmpty(titleAndContentMap.get("content"))) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("dropship title and content is null ,can not send messaging");
				return;
			}
		} catch (Exception e) {
			Logger.error("can not deal with verify wholesale messaging content");
			e.printStackTrace();
		}

		if (emailaccount == null) {
			Logger.info("sendDropShipResultMessaging server account null!");
			return;
		}
		Broadcast broadcast = new Broadcast();
		broadcast.setContent(content);
		broadcast.setEmail(toemail);
		broadcast.setWebsiteId(websiteId);
		broadcast.setType(Type.DROPSHIP.getCode());
		broadcast.setFrom("TOMTOP Team");
		broadcast.setSendId(isendIid);
		broadcast.setSubject(title);
		broadcast.setStatus(enums.messaging.Status.unread.getCode());
		broadcast.setSendMethod(enums.messaging.SendMethod.SYSTEM.getCode());
		int insert = mService.insert(broadcast);
		if (insert > 0) {
			Logger.info("sendDropShipResultMessaging successful!");
		} else {
			Logger.info("sendDropShipResultMessaging fair!");
		}
	}

}
