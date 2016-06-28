package service.email.send;

public interface IEmailSendService {
	boolean send(String from, String toEmail, String title, String content);

	boolean send(String from, String toEmail, String title, String content,
			String fromName);
}
