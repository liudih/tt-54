package email.util;

import java.rmi.RemoteException;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.google.common.eventbus.EventBus;

import events.email.EmailLogEvent;
import events.email.EmailResendEvent;
import SpreadWS.javapackage.ServiceSoapProxy;
import play.Logger;
import play.Play;

public class EmailSpreadUtil {

	@Inject
	EventBus eventBus;

	static String loginEmail = "";
	static String password = "";

	public boolean send(String from, String toEmail, String campaignName,
			String title, String content) {
		try {
			loginEmail = Play.application().configuration()
					.getString("email.third.loginEmail");
			password = Play.application().configuration()
					.getString("email.third.password");
			if (StringUtils.isEmpty(loginEmail)
					|| StringUtils.isEmpty(password)) {
				Logger.error("Third mailbox login user name or password is empty!");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(
					"From the configuration file to read the third party login user name or password failed",
					e);
			return false;
		}
		if (StringUtils.isEmpty(campaignName)) {
			campaignName = title;
		}
		String fromName = "tomtop";
		ServiceSoapProxy proxy = new ServiceSoapProxy();

		try {
			if (StringUtils.isEmpty(from) || StringUtils.isEmpty(toEmail)
					|| StringUtils.isEmpty(title)
					|| StringUtils.isEmpty(content)) {
				String fail = "Please fill in the required information";
				EmailLogEvent event = analytical(from, toEmail, title, content,
						fail);
				eventBus.post(event);
				return false;
			}
			String result =
			// proxy.send2(loginEmail, password, campaignName,from, fromName,
			// toEmail, title, content);
			proxy.emailSend(loginEmail, password, from, fromName, toEmail,
					title, content);
			Logger.info("Send results=={},toEmail=={},title=={}", result,
					toEmail, title);
			EmailLogEvent event = analytical(from, toEmail, title, content,
					result);
			eventBus.post(event);
			return true;
		} catch (RemoteException e) {
			Logger.error("Send results is failed,toEmail=={},title=={}",
					toEmail, title, e);
			EmailLogEvent event = analytical(from, toEmail, title, content, "");
			eventBus.post(event);
			return false;
		}
	}

	public boolean send(String from, String toEmail, String title,
			String content) {
		boolean result = this.send(from, toEmail, "", title, content);
		if (false == result) {
			EmailResendEvent emailResendEvent = new EmailResendEvent(from,
					toEmail, title, content);
			eventBus.post(emailResendEvent);
		}
		return result;
	}

	public static EmailLogEvent analytical(String from, String toEmail,
			String title, String content, String cthirdresult) {
		EmailLogEvent event = new EmailLogEvent();
		event.setCfromemail(from);
		event.setCtoemail(toEmail);
		event.setCtitle(title);
		event.setCcontent(content);
		// 第三方选择emailsend方法解析
		event.setBsendstatus(StringUtils.isEmpty(cthirdresult) ? false
				: (cthirdresult.contains("-") ? true : false));
		// 第三方选择send2方法解析
		// event.setBsendstatus(StringUtils.isEmpty(cthirdresult) ? false
		// : (cthirdresult.equals("Sent success") ? true : false));
		event.setCthirdresult(cthirdresult);
		return event;
	}
}
