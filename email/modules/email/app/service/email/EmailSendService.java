package service.email;

import javax.inject.Inject;

import email.util.EmailSpreadUtil;
import service.email.send.IEmailSendService;

public class EmailSendService implements IEmailSendService {

	@Inject
	EmailSpreadUtil emailSpreadUtil;

	@Override
	public boolean send(String from, String toEmail, String title,
			String content) {
		return this.send(from, toEmail, title, content, "");
	}

	@Override
	public boolean send(String from, String toEmail, String title,
			String content, String fromName) {
		return emailSpreadUtil.send(from, toEmail, title, content, fromName);
	}

}
