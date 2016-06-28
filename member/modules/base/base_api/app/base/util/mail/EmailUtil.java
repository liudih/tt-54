package base.util.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import play.Logger;
import dto.EmailAccount;

public class EmailUtil {
	public static boolean send(String title, String content,
			EmailAccount account, String toEmail) {
		if (account == null || toEmail == null || title == null
				|| title.equals("") || content == null || content.equals("")) {
			return false;
		}
		String hostname = account.getCsmtphostName();
		String email = account.getCemail();
		String pwd = account.getCpassword();
		String stmptype = account.getCtype();
		String username = account.getCusername();
		int port = account.getIserverport();
		Properties props = new Properties();
		props.put("mail.smtp.host", hostname);
		props.put("mail.stmp.user", "username");

		// To use TLS
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.password", "password");
		// To use SSL
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, pwd);
			}
		});

		try {
			String nick = javax.mail.internet.MimeUtility.encodeText(username);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(nick + "<" + email + ">"));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
					toEmail));
			msg.setSubject(title);
			msg.setContent(content, "text/html;charset=utf-8");
			msg.setSentDate(new Date());
			Transport transport = session.getTransport(stmptype);
			transport.connect(hostname, port, email, pwd);
			Transport.send(msg);
			Logger.info("send email success: {}", toEmail);
			return true;

		} catch (Exception e) {
			Logger.error("send email error: {}", e.getMessage());
		}
		return false;
	}
	
}
