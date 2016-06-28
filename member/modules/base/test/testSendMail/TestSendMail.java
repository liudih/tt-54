package testSendMail;

	import java.util.Properties;

	import javax.mail.Message;
	import javax.mail.PasswordAuthentication;
	import javax.mail.Session;
	import javax.mail.Transport;
	import javax.mail.internet.InternetAddress;
	import javax.mail.internet.MimeMessage;

	public class TestSendMail {
		public static void main(String[] args) {
		//發送郵件
			 Properties props = new Properties();
		     props.put("mail.smtp.host" , "smtp.qq.com");
		     props.put("mail.stmp.user" , "username");

		     //To use TLS
		     props.put("mail.smtp.auth", "true"); 
		     props.put("mail.smtp.starttls.enable", "true");
		     props.put("mail.smtp.password", "password");
		     //To use SSL
		     props.put("mail.smtp.socketFactory.port", "465");
		     props.put("mail.smtp.socketFactory.class", 
		         "javax.net.ssl.SSLSocketFactory");
		     props.put("mail.smtp.auth", "true");
		     props.put("mail.smtp.port", "25");


		     Session session  = Session.getDefaultInstance( props , new javax.mail.Authenticator(){
		         protected PasswordAuthentication getPasswordAuthentication() {
		             return new PasswordAuthentication(
		                 "648508153@qq.com", "");//鏈接服務器用户名和密码
		         }
		 });
		     
		     String to = "648508153@qq.com";
		     String from = "648508153@qq.com";
		     String subject = "Forgot password back(TOMTOP.com)";
		     Message msg = new MimeMessage(session);
		     try {
		         msg.setFrom(new InternetAddress(from));
		         msg.setRecipient(Message.RecipientType.TO, 
		             new InternetAddress(to));
		         msg.setSubject(subject);
		         msg.setText("Dear cemail hello:"+'\r'+'\u0008'+"You in tomtop (TOMTOP.com), click the 'forgot your password' button, so the system automatically sent this E-mail for you.You can click the following link to modify your password:"+'\r'+'\u0008'+"uuid"+'\r'+'\u0008'+"This link is valid for two hours, please click the link within two hours, modified allowed to retrieve password three times a day.If you don't need to modify the password, or you never click on the 'forgot password' button, please ignore this E-mail.");
		         Transport transport = session.getTransport("smtp");
		         transport.connect("smtp.qq.com" , 25 , "648508153@qq.com", "");// 鏈接服務器用户名和密码
		         transport.send(msg);
		         System.out.println("發送成功!!");
		     }
		     catch(Exception exc) {
		         System.out.println(exc);
		     }
		}

}
