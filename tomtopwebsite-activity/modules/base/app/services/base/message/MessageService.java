package services.base.message;

import play.Logger;
import services.base.FoundationService;

import com.google.inject.Inject;

import entity.messaging.Broadcast;

public class MessageService {

	public enum MessageType {
		NONE, COUPON, ORDERREFUND
	}

	@Inject
	FoundationService fService;
	@Inject
	services.messaging.IMessageService mservice;

	public void send(String toemail, String title, String content,
			MessageType mtype) {
		try {
			Broadcast info = new Broadcast();
			info.setContent(this.getContent(mtype, title, content));
			info.setSubject(this.getTitle(mtype, title));
			info.setEmail(toemail);
			info.setSendId(1);
			info.setFrom("TOMTOP team");
			info.setType(enums.messaging.Type.PERSONAL.getCode());
			info.setStatus(enums.messaging.Status.unread.getCode());
			info.setSendMethod(enums.messaging.SendMethod.MANUAL.getCode());
			info.setWebsiteId(fService.getLanguage());
			int id = mservice.insert(info);
		} catch (Exception ex) {
			Logger.error("send message error: ", ex);
		}
	}

	/*
	 * 1. 中免单站内信提示： Congratulations! You win a xxxx, redeem code: xxx 2.
	 * 中Coupon站内信提示： Congratulations! You win a xxxx, coupon code:xxx
	 */

	private String getTitle(MessageType mtype, String title) {
		if (mtype == MessageType.NONE) {
			return title;
		}
		return "Congratulations! You win a " + title;
	}

	private String getContent(MessageType mtype, String title, String content) {
		if (mtype == MessageType.COUPON) {
			return "Congratulations! You win a " + title + ",coupon code:"
					+ content;
		} else if (mtype == MessageType.ORDERREFUND) {
			return "Congratulations! You win a " + title + ",redeem code:"
					+ content;
		} else {
			return content;
		}
	}

}
